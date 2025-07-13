## 前端：

按钮：

```vue
<el-col :span="1.5">
    <el-button type="info" plain icon="el-icon-upload2" size="mini" @click="handleImport"
     v-hasPermi="['system:category:import']">导入</el-button>
</el-col>
```

用户导入对话框（不用改）：

```vue
<!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".xlsx, .xls" :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport" :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess" :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline"
            @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
```

需要导入一个：

```
import { getToken } from "@/utils/auth";
```

```js
// 用户导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/system/category/CategoryImportData"
      },
```

上面的接口地址注意改



```js
 /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "导入";
      this.upload.open = true;
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('system/category/CategoryImportTemplate', {
      }, `目录分类_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    }
```

上面的接口地址注意改



## 后端：

controller：

```java
@PostMapping("/CategoryImportTemplate")
    public void CategoryImportTemplate(HttpServletResponse response)
    {
        ExcelUtil<DirectoryCategoryExportExcel> util = new ExcelUtil<DirectoryCategoryExportExcel>(DirectoryCategoryExportExcel.class);
        util.importTemplateExcel(response, "目录分类数据");
    }

    @Log(title = "目录分类", businessType = BusinessType.IMPORT)
    @PreAuthorize("@ss.hasPermi('system:category:import')")
    @PostMapping("/CategoryImportData")
    public AjaxResult CategoryImportData(MultipartFile file, boolean updateSupport) throws Exception
    {
        ExcelUtil<DirectoryCategory> util = new ExcelUtil<DirectoryCategory>(DirectoryCategory.class);
        List<DirectoryCategory> categoryList = util.importExcel(file.getInputStream());
        String operName = getUsername();
        String message = directoryCategoryService.importCategory(categoryList, updateSupport, operName);
        return success(message);
    }
```

其中涉及到自己定义的实体类，用于生成excel内容：

```java
public class DirectoryCategoryExportExcel extends BaseEntity {

    private static final long serialVersionUID = 1L;


    /** 分类名称（唯一） */
    @Excel(name = "分类名称")
    private String categoryName;

    /** 标准编号 */
    @Excel(name = "标准编号")
    private String standardNumber;

    /** 备注信息 */
    @Excel(name = "备注")
    private String categoryRemark;



}
```

服务层：

```java
    @Autowired
    protected Validator validator;

/**
     * 导入数据
     *
     * @param categoryList 数据列表
     * @param isUpdateSupport 是否更新支持，如果已存在，则进行更新数据
     * @param operName 操作用户
     * @return 结果
     */
    @Override
    public String importCategory(List<DirectoryCategory> categoryList, Boolean isUpdateSupport, String operName) {
        if (StringUtils.isNull(categoryList) || categoryList.isEmpty()) {
            throw new ServiceException("导入数据不能为空！");
        }

        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();

        for (DirectoryCategory category : categoryList) {
            try {
                // 1. 验证是否存在该分类（根据唯一字段 name 判断）
                DirectoryCategory existing = directoryCategoryMapper.selectCategoryByCategoryName(category.getCategoryName());

                if (existing == null) {
                    // 2. 校验数据合法性
                    BeanValidators.validateWithException(validator, category);

                    // 3. 设置必要的系统字段
                    category.setCreatedBy(operName);
                    category.setCreatedAt(new Date());
                    String delFlag = "0";
                    category.setDelFlag(delFlag);

                    // 4. 执行插入
                    directoryCategoryMapper.insertDirectoryCategory(category);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、分类 “" + category.getCategoryName() + "” 导入成功");
                } else if (isUpdateSupport) {
                    // 5. 如果允许更新，则更新已有记录
                    BeanValidators.validateWithException(validator, category);

                    category.setId(existing.getId()); // 设置主键进行更新
                    category.setUpdateBy(operName);
                    category.setUpdatedAt(new Date());

                    directoryCategoryMapper.updateDirectoryCategory(category);
                    successNum++;
                    successMsg.append("<br/>" + successNum + "、分类 “" + category.getCategoryName() + "” 更新成功");
                } else {
                    // 6. 不允许更新的情况下，记录失败
                    failureNum++;
                    failureMsg.append("<br/>" + failureNum + "、分类 “" + category.getCategoryName() + "” 已存在");
                }
            } catch (Exception e) {
                failureNum++;
                String msg = "<br/>" + failureNum + "、分类 “" + category.getCategoryName() + "” 导入失败：";
                failureMsg.append(msg + e.getMessage());
//                log.error(msg, e);
            }
        }

        if (failureNum > 0) {
            failureMsg.insert(0, "很抱歉，导入失败！共 " + failureNum + " 条数据错误，详情如下：");
            throw new ServiceException(failureMsg.toString());
        } else {
            successMsg.insert(0, "恭喜您，导入成功！共 " + successNum + " 条，详情如下：");
        }

        return successMsg.toString();
    }
```

mapper：

```java
public DirectoryCategory selectCategoryByCategoryName(String CategoryName);
```

mapper.xml:

```xml
<select id="selectCategoryByCategoryName" parameterType="String" resultMap="DirectoryCategoryResult">
        <include refid="selectDirectoryCategoryVo"/>
        where category_name = #{CategoryName} and directory_category.del_flag = '0'
    </select>
```

