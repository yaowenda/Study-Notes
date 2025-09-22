## SQL注入

### JDBC

1、采用 `Statement` 方法拼接 SQL 语句

2、PrepareStatement 会对 SQL 语句进行预编译，**但如果直接采取拼接的方式构造 SQL，此时进行预编译也无用。**

3、JDBCTemplate 是 `Spring` 对 JDBC 的封装，如果使用拼接语句便会产生注入安全写法：SQL 语句占位符（?） + PrepareStatement 预编译

### Mybatis

首先要知道：

```
${} 是字符串替换（拼接）
#{} 是参数占位符（预编译） 它负责把#{}换成？
```

**like注入**

like 注入：模糊搜索时，直接使用`'%#{q}%'`会报错，部分研发图方便直接改成`'%${q}%'`从而造成注入。

Mybatis模糊查询: `Select * from users where username like '%#{username}%'`，这样Mybatis处理生成预编译SQL：`Select * from users where username like '%?%'`，而数据库会**字面理解** `'%?%'` 这个模式。它不是在寻找包含参数值的用户名，而是在寻找**字面上包含一个问号`?`字符**的用户名。

在这种情况下使用 # 程序会报错,把 # 号改成 $ 可以解决，**但这样写会存在sql注入漏洞**

但是如果java代码层面没有对用户输入的内容做处理，那么将会产生SQL注入漏洞。

正确写法: Select * from users where username like concat("%",#(username),"%")

POC: xxx%' union select database(),user(),@@version,4,5 -- -

**order by注入**

order by注入：由于使用#{ }会将对象转成字符串，形成`order by "user" desc`造成错误，因此很多研发会采用${}来解决，从而造成注入。

## XXE

```
/**
 * 审计的函数
 * 1. XMLReader
 * 2. SAXReader
 * 3. DocumentBuilder
 * 4. XMLStreamReader
 * 5. SAXBuilder
 * 6. SAXParser
 * 7. SAXSource
 * 8. TransformerFactory
 * 9. SAXTransformerFactory
 * 10. SchemaFactory
 * 11. Unmarshaller
 * 12. XPathExpression
 */
```

## SSTI

https://www.cnblogs.com/bmjoker/p/13508538.html