JS文件上传过滤：

​	1、自己写一个一样的页面，把过滤方法删去，上传到（action)他的文件上传地址

​	2、burp抓包改包

​	3、禁用JS



JS--登录功能 **JQuery  Ajax** 

https://www.runoob.com/jquery/jquery-tutorial.html

https://www.w3school.com.cn/jquery/index.asp

```html
<div class="login">
    <h2>后台登录</h2>
    <label for="username">用户名:</label>
    <input type="text" name="username" id="username" class="user" required>
    <label for="password">密码:</label>
    <input type="password" name="password" id="password" class="pass" required>
    <button>登录</button>
</div>
```



```js
<script src="js/jqury-1.12.4.js"></script>
<script>
    $("button").click(function(){
    	$.ajax({
            type: 'POST',
            url: 'logincheck.php',
            data: {
                myuser:$('.user').val(),
                mypass:$('.pass').val(),
                
            },
            success: function(res) {
                //如果发送数据成功，他用function接收res
                if(res['infoCode']==1){
                    alert('登陆成功');
                    location.href='index.php';//跳转写在前端是有问题的，返回包把0改为1就能登录
                }else{
                    alert('登录失败');
                }
            },
            dataType: 'json',
        })
})
</script>
```

.val()是jquery中的一个方法，用于获取或设置表单元素（如 `<input>`、`<textarea>`、`<select>` 等）的值。`.prop()`与`.val()`效果相同。

`.val()` 获取的值与选择器（`class` 或 `id`）无关，而是与你选择的 DOM 元素有关。无论是通过 `class` 还是 `id` 选择元素，只要选中了正确的表单元素（如 `<input>`、`<textarea>` 等），`.val()` 都能正确获取其值。

如果有多个相同的class，.val()会默认返回第一个匹配的。id在一个页面里是唯一的



`logincheck.php`

```php
<?php
    $user = $_POST['myuser'];
	$pass = $_POST['mypass'];

	$success=array('msg'=>'ok'); //array('msg'=>'ok')定义了一个数组，键是 msg，值是 'ok'，意味着当前的返回状态是“ok”。
	if($user=='qishui' && $pass == '123'){
        $success['infoCode']=1;
        //echo '<script>location.href="index.php"</script>'; 写这里才对
    } else {
        $success['infoCode']=0;
    }
	echo json_encode($success) //json_encode将php数组转换成json 例如 {"msg":"ok","infoCode":1}
        
?>
```

这里的echo必须要有，否则回调拿不到res

**什么是回调？**

回调函数是一个在任务完成后被“回头”调用的函数，常用于异步场景。就像你去打印店打印文件，你告诉老板：“你打印完了给我打电话。” 当打印机完成后，老板就“回调”你，通知你来取。

success: function(res) {……} 是一个回调函数，当浏览器成功地从 `logincheck.php` 接收到服务器的返回数据之后，jQuery 会自动执行这个 `function(res)` 函数，把服务器的响应结果 `res` 传给它。

### ⛓️ 整个过程：

1. 用户点击“登录”按钮。
2. JavaScript 通过 Ajax 向服务器发送用户名和密码。
3. **这是一个异步操作**，浏览器不会等它完成，而是继续执行后续代码。
4. 当服务器处理完登录逻辑后，返回一个 JSON 数据，比如 `{infoCode: 1}`。
5. 这个返回的数据会被 **`success` 回调函数接收**，并执行里面的逻辑（比如 `alert` 弹出登录成功/失败）。