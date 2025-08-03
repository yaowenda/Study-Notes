```
<el-table-column label="是否多单位" align="center" prop="multiUnit">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.multi_unit" :value="scope.row.multiUnit" />
        </template>
      </el-table-column>
```

