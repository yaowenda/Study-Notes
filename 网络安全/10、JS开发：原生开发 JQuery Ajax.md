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
                    location.href='index.php';
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

	$success=array('msg'=>'ok');
	if($user=='qishui' && $pass == '123'){
        $success['infoCode']=1;
    } else {
        $success['infoCode']=0;
    }
	echo json_encode($success)
        
?>
```



什么是回调？