## 一、实现一些部门看全部数据，一些部门看自己部门的数据

实现公司高管、公司人力资源部能看所有信息，其他部门看自己部门的信息



把原来controller中的list方法改为：

先获取登陆者的工号，再通过工号查询部门，再通过部门筛选该部门的人员

```java
/**
     * 查询考勤统计列表
     */
    @PreAuthorize("@ss.hasPermi('attendance_statistics:attendance_statistics:list')")
    @GetMapping("/list")
    public TableDataInfo list(AttendanceStatistics attendanceStatistics)
    {
        // 获取当前登录用户工号
        String personnel = SecurityUtils.getLoginUser().getUser().getPeoJobNumber();
        //通过工号获取到他的所属部门
        String department = personnelManagementPeopleService.getDepartmentByJobNumber(personnel);
        // 判断用户是否为管理员
        //boolean isAdmin = SecurityUtils.isAdmin(SecurityUtils.getUserId());

        startPage();


        // 如果是管理员，则查询所有记录，否则过滤当前用户
        if (SecurityUtils.hasAnyRole("admin", "公司人力资源部")) {
            List<AttendanceStatistics> list = attendanceStatisticsService.selectAttendanceStatisticsList(attendanceStatistics); // 管理员查询所有数据
            return getDataTable(list);
        } else {
            attendanceStatistics.setAttDepartment(department);

            List<AttendanceStatistics> list = attendanceStatisticsService.selectAttendanceStatisticsList(attendanceStatistics); // 普通用户查询自己的数据
            return getDataTable(list);
        }
    }
```

其中的personnelManagementPeopleService.getDepartmentByJobNumber()方法是自己写的，这个方法是通过工号查询所属部门。这个方法已经写好了



其中`SecurityUtils.hasAnyRole`是我加在ruoyi-common/src/utils的SecurityUtils.java中的，他的作用是“验证用户是否拥有指定角色列表中的任意一个角色”，服务器上写好了



具体是这样写的：

```java
/**
     * 验证用户是否拥有指定角色列表中的任意一个角色
     *
     * @param roles 需要验证的角色列表
     * @return 用户是否具备指定角色列表中的任意一个角色
     */
    public static boolean hasAnyRole(String... roles)
    {
        List<SysRole> roleList = getLoginUser().getUser().getRoles();
        Collection<String> userRoles = roleList.stream().map(SysRole::getRoleKey).collect(Collectors.toSet());
        return Arrays.stream(roles).anyMatch(role -> hasRole(userRoles, role));
    }
```



因为通过部门筛选人员，所以mapper.xml中需要有部门的查询条件：

```xml
<select id="selectPersonnelManagementPeopleList" parameterType="PersonnelManagementPeople" resultMap="PersonnelManagementPeopleResult">
    <include refid="selectPersonnelManagementPeopleVo"/>
    <where>
        <if test="peoJobNumber != null  and peoJobNumber != ''"> and peo_job_number = #{peoJobNumber}</if>
        <if test="peoDepartment != null  and peoDepartment != ''"> and peo_department = #{peoDepartment}</if>
        <if test="peoName != null  and peoName != ''"> and peo_name like concat('%', #{peoName}, '%')</if>
    </where>
</select>
```



**前端：**

原来有一个部门的筛选框，现在需要隐藏掉，只有admin和公司人力资源部能看见：

```vue
<el-form-item label="所属部门" prop="peoDepartment" v-hasRole="['admin','公司人力资源部']">
  <el-select v-model="queryParams.peoDepartment" placeholder="请选择所属部门" clearable style="width: 200px" @change="handleDepartmentChange" >
    <el-option v-for="dict in dict.type.subordinate_department" :key="dict.value" :label="dict.label"
               :value="dict.value" />
  </el-select>
</el-form-item>
```

就是在第一行最后加了`v-hasRole="['admin','公司人力资源部']"`







## 二、导出需要做修改

如果有些部门只能看见自己部门的数据，那么导出的时候这个部门也应该只能看见自己部门的数据



导出按钮触发的方法在前端生成代码的时候生成了：

```js
/** 导出按钮操作 */
    handleExport() {
      this.download('attendance_statistics/attendance_statistics/export', {
        ...this.queryParams
      }, `考勤统计_${new Date().getTime()}.xlsx`)
    }
  }
```



**因为按照最初的逻辑，导出按钮直接把数据库中所有数据全都导出，但是实际需求是一些部门只能导出该部门的，一些部门能导出全部的**



所以对controller中的export方法修改，思路与控制list方法权限类似：

```java
/**
     * 导出考勤统计列表
     */
    @PreAuthorize("@ss.hasPermi('attendance_statistics:attendance_statistics:export')")
    @Log(title = "考勤统计", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, AttendanceStatistics attendanceStatistics)
    {
        // 获取当前登录用户工号
        String personnel = SecurityUtils.getLoginUser().getUser().getPeoJobNumber();
        //通过工号获取到他的所属部门
        String department = personnelManagementPeopleService.getDepartmentByJobNumber(personnel);


        // 如果是管理员，则查询所有记录，否则过滤当前用户
        if (SecurityUtils.hasAnyRole("admin", "公司人力资源部")) {
            List<AttendanceStatistics> list = attendanceStatisticsService.selectAttendanceStatisticsList(attendanceStatistics); // 管理员查询所有数据
            ExcelUtil<AttendanceStatistics> util = new ExcelUtil<AttendanceStatistics>(AttendanceStatistics.class);
            util.exportExcel(response, list, "考勤统计数据");
        } else {
            attendanceStatistics.setAttDepartment(department);
            List<AttendanceStatistics> list = attendanceStatisticsService.selectAttendanceStatisticsList(attendanceStatistics); // 普通用户查询自己的数据
            ExcelUtil<AttendanceStatistics> util = new ExcelUtil<AttendanceStatistics>(AttendanceStatistics.class);
            util.exportExcel(response, list, "考勤统计数据");
        }

    }
```

如果是角色是admin或者公司人力资源部，就能导出全部，也可以按部门筛选。如果是其他部门，只能导出本部门的数据。

其中`SecurityUtils.hasAnyRole`是我加在ruoyi-common/src/utils的SecurityUtils.java中的，他的作用是“验证用户是否拥有指定角色列表中的任意一个角色”



需要确保mapper.xml中selectAttendanceStatisticsList方法有按部门筛选的条件



### 我还做了按日期区间导出的功能，普通项目部能按日期区间导出考勤数据，高管可以按部门和日期区间导出考勤数据，有类似需要的可以问我



## 3、要求公司高管能看但不能操作

```vue
<el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          size="medium"
          @click="handleRedirect"
          v-hasRole="['admin', '公司人力资源部']"
        >新增入职申请</el-button>
      </el-col>

      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-edit"
          size="medium"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['employee_onboarding:employee_onboarding:edit']"
          v-hasRole="['admin', '公司人力资源部']"
        >修改</el-button>
      </el-col>
```

如上所示，在按钮上加`v-hasRole="['admin', '公司人力资源部']"`，指定哪些角色能看见

列表的最后一列也有修改和删除按钮，也加上v-hasRole="['admin', '公司人力资源部']"：

```vue
<el-table-column  label="操作" align="center" class-name="small-padding fixed-width" width="120" >
          <template slot-scope="scope" >
            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                       v-hasPermi="['personnel_management_people:personnel_management_people:edit']"
                       v-hasRole="['admin', '公司人力资源部']">修改</el-button>
            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                       v-hasPermi="['personnel_management_people:personnel_management_people:remove']"
                       v-hasRole="['admin', '公司人力资源部']">删除</el-button>
          </template>
        </el-table-column>
```





### 要求角色管理中创建的角色是规范的，没必要存在的角色删去

