```js
//引入express模块 流行的Node.js web框架
const express = require('express');
//创建一个 Express 应用实例
const app = express();

//get路由，当在浏览器访问这里时，Express会接收到这个GET请求然后执行括号里的韩式
app.get('/login', (req, res) => {
    res.send('<hr>登录页面<hr>');//发送给浏览器
});

const server = app.listen(3000, () => {
    console.log('web的3000端口启动成功');
});

```

访问127.0.0.1:3000/login 页面显示“登录页面



传输数据 登录页面

```html
<div class="login-box">
  <h2>用户登录</h2>
  <form action="/login" method="post">
    <input type="text" name="username" placeholder="用户名" required>
    <input type="password" name="password" placeholder="密码" required>
    <button type="submit">登录</button>
  </form>
</div>
```

```js
//引入express模块 流行的Node.js web框架
const express = require('express');
const bodyParser = require('body-parser');//post请求解析中间件
//创建一个 Express 应用实例
const app = express();

var urlencodedParser = bodyParser.urlencoded({ extended: false });

const server = app.listen(3000, () => {
    console.log('web的3000端口启动成功');
});

//get方式处理登录
app.get('/login', (req, res) => {
    const u = req.query.username;//query
    const p = req.query.password;
    if(u == 'admin' && p == '123') {
        console.log(u);
        console.log(p);
        res.send('登录成功');
    } else {
        res.send('登录失败');
    }
});

//post方式处理登录
app.post('/login', urlencodedParser, (req, res) => {
    const u = req.body.username;//body
    const p = req.body.password;
    if(u == 'admin' && p == '123') {
        console.log(u);
        console.log(p);
        res.send('登录成功');
    } else {
        res.send('登录失败');
    }
});

app.get('/', (req, res) => {
    res.sendFile(__dirname + '/login.html');
});
```

