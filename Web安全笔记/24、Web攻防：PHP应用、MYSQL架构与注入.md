### MySQL 5.0 及以上版本：自带的数据库名 `information_schema`

- **`information_schema`**：存储数据库下的数据库名及表名、列名信息的数据库。

#### 表结构说明：

1. **`information_schema.schemata`**：记录数据库名信息的表。
2. **`information_schema.tables`**：记录表名信息的表。
3. **`information_schema.columns`**：记录列名信息的表。

#### 列名值说明：

- **`schema_name`**：在 `information_schema.schemata` 表中记录数据库名信息的列名值。
- **`table_schema`**：在 `information_schema.tables` 表中记录数据库名的列名值。
- **`table_name`**：在 `information_schema.tables` 表中记录表名的列名值。
- **`column_name`**：在 `information_schema.columns` 表中记录列名的列名值。



**获取相关数据：**

1. 数据库版本 - 看是否符合 information_schema 查询 - `version()`
2. 数据库用户 - 看是否符合 ROOT 型注入攻击 - `user()`
3. 当前操作系统 - 看是否支持大小写或文件路径选择 - `@@version_compile_os`
4. 数据库名字 - 为后期猜解指定数据库下的表，列做准备 - `database()`