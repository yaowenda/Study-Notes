### 创建远程仓库

### 配置SSH公钥



生成SSH公钥

ssh-keygen -t rsa

获取公钥

cat ~/.ssh/id_rsa.pub

测试连接：

ssh -T git@gitee.com



### 添加远程仓库

![image-20250311190131169](C:\Users\86151\AppData\Roaming\Typora\typora-user-images\image-20250311190131169.png)

复制，git面板输入命令：**git remote add origin git@gitee.com:yaowenda/git_test.git**

意思是添加一个远程仓库，仓库的名字叫origin

检查：

![image-20250311190315288](C:\Users\86151\AppData\Roaming\Typora\typora-user-images\image-20250311190315288.png)

### 推送到远程仓库

git push origin master:master

完整语法：git push [-f] [--set-upstream] [本地分支名]:[远端分支名]

如果本地分支和远端分支名相同，可以只写本地分支

-f 表示强制覆盖

--set-upstream表示推送到远端的同时建立起和远端的关联

![image-20250311191720352](C:\Users\86151\AppData\Roaming\Typora\typora-user-images\image-20250311191720352.png)

这样建立了关联之后，可以省略分支名和远端名，直接git push



git branch -vv：

git branch是查看有哪些分支，加一个-vv能显示出本地分支对应的远端分支



### clone

例：git clone git@gitee.com:yaowenda/git_test.git

克隆只进行一次

#### 抓取和拉取

抓取**fetch **

​	将仓库里面的更新都抓取到本地，**不会进行合并**（意味着要自己merge）

​	git fetch [remote name] [branch name]

​	不指定远端名称和分支名，默认抓取所有分支

拉取**pull**

​	将仓库里面的更新都抓取到本地，**自动合并**（fetch+merge）

​	git pull [remote name] [branch name]

​	不指定远端名称和分支名，默认拉取所有分支



### 解决合并冲突

在一段时间内，A、B用户修改了同一个文件，且修改了同一行位置的代码，此时会发生合并冲突。

A用户在本地修改代码后优先推送到远程仓库，此时B用户在本地修订代码，提交到本地仓库后，也需要推送到远程仓库，此时B用户晚于A用户，**故需要先拉取远程仓库的提交，经过合并后才能推送到云端分支**,如下图所示。

![image-20250311194029522](C:\Users\86151\AppData\Roaming\Typora\typora-user-images\image-20250311194029522.png)

在B用户拉取代码时，因为A、B用户同一段时间修改了同一个文件的相同位置代码，故会发生合并冲突。

远端分支也是分支，所以合并时冲突的解决方式也和解决本地分支冲突相同相同，在此不再赘述。

![image-20250311194329803](C:\Users\86151\AppData\Roaming\Typora\typora-user-images\image-20250311194329803.png)



### 在idea中使用git

第一步是在setting中设置git安装地址，能自动检测

初始化：

![image-20250311195659014](C:\Users\86151\AppData\Roaming\Typora\typora-user-images\image-20250311195659014.png)

点里面的**Create Git Repository**

commit and push：

![image-20250311200437624](C:\Users\86151\AppData\Roaming\Typora\typora-user-images\image-20250311200437624.png)



**因为多人开发项目，所以先pull，然后再开发，如果两个人在同一段时间内改了相同的文件，后push的是无法push的，需要先解决冲突，然后add 然后commit and push**



#### 创建分支：

在这里好处是可以在任意地方创建分支

![1741695750194](C:\Users\86151\Documents\WeChat Files\wxid_ectne75nfs1922\FileStorage\Temp\1741695750194.jpg)



### 注意：

1、切换分支前先提交本地的修改

2、代码即使提交

3、遇见问题也不删除文件目录