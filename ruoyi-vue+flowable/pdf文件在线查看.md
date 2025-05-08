```js
<el-table-column label="机统28文件" align="center" prop="document" width="120">
          <template slot-scope="scope">
            <div class="el-table_cell">
              <el-button v-if="isValidFilePath(scope.row.document)" type="text"
                @click="handleFileView(scope.row.document)">
                查看
              </el-button>
              <span v-else>{{ scope.row.document }}</span>
            </div>
          </template>
```

```vue
<!-- 文件选择对话框 -->
    <el-dialog title="请选择要查看的文件" :visible.sync="fileDialogVisible" width="30%" append-to-body>
      <div class="file-list">
        <div v-for="(file, index) in currentFiles" :key="index" class="file-item">
          <a :href="getFullUrl(file)" target="_blank" style="color: #409EFF; text-decoration: underline;"
            @click="fileDialogVisible = false">
            {{ getFileName(file) }}
          </a>
        </div>
      </div>
    </el-dialog>
```

```js
data() {
    return {
      fileDialogVisible: false,
      currentFiles: [],
```

```js
// 打开文件选择对话框
    handleFileView(files) {
      if (!files) return;
      const fileArray = this.getFilePathArray(files);
      if (fileArray.length > 0) {
        if (fileArray.length === 1) {
          // 单个文件直接打开
          window.open(this.getFullUrl(fileArray[0]), '_blank');
        } else {
          // 多个文件显示选择对话框
          this.currentFiles = fileArray;
          this.fileDialogVisible = true;
        }
      }
    },
    // 将文件路径字符串拆分为数组
    getFilePathArray(pathString) {
      if (!pathString) return [];
      // 使用逗号分隔文件路径
      return pathString.split(',').filter(path => path.trim());
    },
    //文件在线预览
    isValidFilePath(path) {
      if (!path) return false;
      // 处理多文件情况
      if (path.includes(',')) {
        return path.split(',').some(p => p.trim().startsWith("/profile/upload"));
      }
      return path.startsWith("/profile/upload");
    },
    getFullUrl(path) {
      const devORprod = process.env.VUE_APP_BASE_API; // 获取当前前端运行环境
      // const port = window.location.port; // 获取当前前端运行端口
      // 获取当前页面的域名或IP
      const currentUrl = window.location.origin;
      const baseUrl = `${currentUrl}${devORprod}`;
      return baseUrl + path;
    },
    getFileName(path) {
      const pathParts = path.split('/');
      return pathParts[pathParts.length - 1].split('.')[0];
    },

```

