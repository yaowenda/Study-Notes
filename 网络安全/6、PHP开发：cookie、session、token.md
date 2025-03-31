# Cookie和Session

都是用来在Web应用程序中跟踪用户状态的机制



不同点：

1、存储位置不同

​	Cookie是存储在客户端上的，而Session存储在服务端上

2、安全性不同：

​	Cookie存储在客户端，可能被黑客窃取利用，而Session存储在服务器上，更加安全。

3、存储容量不同：

​	Cookie的存储容量有限，一般为4KB，而Session的存储容量理论上没有限制，取决于服务器硬件配置

4、生命周期不同：

​	Cookie可以设置过期时间，即便关闭浏览器或重新打开电脑，Cookie仍然存在，直到过期或被删除。而Session一般默认在关闭浏览器之后就会过期。

5、访问方式不同：

​	Cookie可以通过Javascript访问，而Session只能在服务器端进行访问

6、使用场景不同：

​	Cookie一般用于存储小型的数据，如用户的用户名和密码等信息。而Session一般用于存储大型的数据，如购物车、登录状态等



一般来说，如果存储敏感信息数据较大，建议用Session。如果只需要存储少量数据且需要在客户端进行访问，可以选择使用Cookie



# token运用

token.php文件：

页面刷新一次token变一次

```php+HTML
<?php
session_start();
$token = bin2hex(random_bytes(16));
//将token绑定到session中
$_SESSION['token'] = $token;
//将token绑定到Cookie中
//setcookie('token', $token, time()+3600, '/');


?>

<div class="login">
    <h2>后台登录</h2>
    <form action="token_check.php" method="post">
        <input type="hidden" name="csrf_token" value="<?php echo $token; ?>">
        <label for="username">用户名:</label>
        <input type="text" name="username" id="username" required>
        <label for="password">密码:</label>
        <input type="password" name="password" id="password" required>
        <input type="submit" value="登录">
    </form>
</div>
```

token_check.php文件：

```php+HTML
<?php
session_start();
$token = $_POST['token'] ?? ''; //从提交的表单中获取

if($token !== $_SESSION['token']) {//和session中的token作比较
    //token不匹配，禁止访问
    header('HTTP/1.1 403 Forbidden');
    echo 'Access Denied';
    exit;
} else {
    $_SESSION['token'] = bin2hex(random_bytes(16)); //刷新一下token，防止爆破
    
    if($_POST['username']=='admin' && $_POST['password']=='123456') {
    echo '登陆成功'
    
} else {
    echo '登录失败'
}
}



```

