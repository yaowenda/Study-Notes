# CVE-2021-43008

Adminer是一个PHP编写的开源数据库管理工具，支持MySQL、MariaDB、PostgreSQL、SQLite、MS SQL、Oracle、Elasticsearch、MongoDB等数据库。

在其版本1.12.0到4.6.2之间存在一处因为MySQL LOAD DATA LOCAL导致的文件读取漏洞。



下载mysql-fake-server，打开fake-mysql-gui-0.0.4.jar

![image-20250326215205954](D:\typora\笔记\vulhub\assets\image-20250326215205954.png)

在页面上，登录窗口：（ip是自己电脑ip）

![image-20250326215242751](D:\typora\笔记\vulhub\assets\image-20250326215242751.png)

点击登录，fake-mysql-gui-0.0.4.jar同目录下获得etc/passwd文件



# CVE-2021-21311

