给el-table加上

```
@row-click="showSubTable" highlight-current-row
```



点击即选中：this.$refs.listTable.toggleRowSelection(row);

需要把el-table加上 ref="listTable"

```
async showSubTable(row) {
      this.$refs.listTable.toggleRowSelection(row);
      try {
        const res = await getApplicationDetail(row.appliFormId); // 请求接口
        this.selectedDetailList = res || [];
      } catch (err) {
        this.$message.error("加载明细失败");
      }
    },
```

