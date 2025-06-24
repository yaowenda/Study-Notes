JWT

<img src="assets/image-20250623184438825.png" alt="image-20250623184438825" style="zoom: 80%;" />

凭据存储在客户端，但和Cookie不一样，有Cookie就能登录，但是JWT不一样

JWT通常由三部分组成：Header（头部）、Payload（负载）和Signature（签名），这三部分用点号（`.`）连接在一起形成一个完整的JWT字符串。

Header：包含了令牌的类型以及所使用的签名算法（如HMAC SHA256或RSA等）

Payload：包含声明（Claims）。声明是关于实体（通常是用户）和其他数据的声明。JWT定义了三种类型的声明：

- 注册声明：这些是一组预定义的声明，不是强制的，但是推荐使用。例如：`iss`（发行人）、`exp`（过期时间）、`sub`（主题）、`aud`（受众）等。
- 公共声明：这些是根据需要自定义的声明，并且不与已注册声明冲突。
- 私有声明：这是为了共享使用而同意的定制声明，既不是注册声明也不是公共声明。

Signature：用于验证消息在此期间没有被更改，并且对于使用私钥签名的情况，还可以验证JWT的发送者的身份。该签名是通过将Base64编码的Header和Payload使用指定的算法（Header中指定）进行加密生成的。





pom.xml

```xml
        <dependency>
            <groupId>com.auth0</groupId>
            <artifactId>java-jwt</artifactId>
            <version>3.18.0</version>
        </dependency>
```

JwtController

```java

public class JwtController {
    
    public static void main(String[] args) {
        //模拟用户的jwt的创建 jwt加密
        String jwttoken = JWT.create()
                // 设置header
                //.withHeader()

                // 设置payload部分
                .withClaim("userId", 1)
                .withClaim("username", "admin")
                .withClaim("password", "123456")

                //设置过期时间
//                .withExpiresAt()
                //设置signatrue部分
                .sign(Algorithm.HMAC256("yaowenda"));

        System.out.println(jwttoken);


        jwtcheck(jwttoken);
    }


    //模拟jwt身份的检测 jwt数据解密
    public static void jwtcheck(String jwtdata){
//        String jwtdata = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjEyMzQ1NiIsInVzZXJJZCI6MSwidXNlcm5hbWUiOiJhZG1pbiJ9.ca1h86Uo7lt0JTawfDQO-WlaI3qWdQTNZaFHl98BoCQ";
        JWTVerifier jwt = JWT.require(Algorithm.HMAC256("yaowenda")).build();
        //解密注册数据
        DecodedJWT verify = jwt.verify(jwtdata);
        //提取注册解密数据 payload部分
        Integer userId = verify.getClaim("userId").asInt();
        String username = verify.getClaim("username").asString();
        String password =verify.getClaim("password").asString();
        System.out.println("userId:"+userId);
        System.out.println("username:"+username);
        System.out.println("password:"+password);

        //提取签名部分
        verify.getSignature();

        //提取header部分
        verify.getHeader();

    }
}
```

输出结果：

eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjEyMzQ1NiIsInVzZXJJZCI6MSwidXNlcm5hbWUiOiJhZG1pbiJ9.ca1h86Uo7lt0JTawfDQO-WlaI3qWdQTNZaFHl98BoCQ
userId:1
username:admin
password:123456



jwt的检验：

```html
<form action="../jwtcheck" method="post">
    jwtdata:<input type="text" name="jwtdata"></input>
    <input type="submit"></input>
</form>
```

```java
@PostMapping("/jwtcheck")
    @ResponseBody
    //模拟jwt身份的检测 jwt数据解密
    public static String jwtcheck(String jwtdata){
//        String jwtdata = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjEyMzQ1NiIsInVzZXJJZCI6MSwidXNlcm5hbWUiOiJhZG1pbiJ9.ca1h86Uo7lt0JTawfDQO-WlaI3qWdQTNZaFHl98BoCQ";
        JWTVerifier jwt = JWT.require(Algorithm.HMAC256("yaowenda")).build();
        //解密注册数据
        DecodedJWT verify = jwt.verify(jwtdata);
        //提取注册解密数据 payload部分
        Integer userId = verify.getClaim("userId").asInt();
        String username = verify.getClaim("username").asString();
        String password =verify.getClaim("password").asString();
        System.out.println("userId:"+userId);
        System.out.println("username:"+username);
        System.out.println("password:"+password);

        //提取签名部分
        verify.getSignature();

        //提取header部分
        verify.getHeader();
        
        if(username.equals("admin")){
            return "admin page";
        } else {
            return "user page";
        }

        

    }
```

![image-20250623235419928](assets/image-20250623235419928.png)

得到：

eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJwYXNzd29yZCI6IjEyMTIxMiIsInVzZXJJZCI6MSwidXNlcm5hbWUiOiJ5YW93ZW5kYSJ9.iJVgONbQgEjw7QKOaxs4k43lQD0Nere03CQmre89Wbs

把它输入进下面的jwtdata：

![image-20250623235459034](assets/image-20250623235459034.png)

没问题



从黑客角度，我把下面这个东西改了，改成admin，然后把这个JWT Token输入到jwtdata框中，就能进入admin页面了？

![image-20250623235623496](assets/image-20250623235623496.png)

试了一下：

![image-20250623235741888](assets/image-20250623235741888.png)

直接报错，因为秘钥没输入，但是黑客又怎么知道秘钥呢？

![image-20250623235832943](assets/image-20250623235832943.png)

所以JWT机制强大就在于，你没秘钥，你在这里破解账号密码没用，就算你知道了admin的账号密码，你也进不去，秘钥不对的话人家根本就不执行代码



**登录仍然是需要验证username和password的**

JWT直接做这个事情：能解密就认定是正确的操作者，比如解密出来username是admin，就认为的确是admin在操作，因为如果你是伪造的账号密码，由于你不知道秘钥，你伪造的那个token根本不会让代码执行，代码能执行就说明是正常用户

假设你登录了一个网站，登陆之后服务器返回了一个 JWT token，里面包含：

```
HTTP/1.1 200 OK
Content-Type: application/json

{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.xxxxx"
}
```

客户端在每次请求时带上这个 JWT（通常放在 `Authorization` 请求头中）。服务器收到请求后，**不检查用户名和密码了**，而是验证 JWT 是否合法（签名是否有效、是否过期等）。如果验证通过，就信任其中的用户信息（如 `user_id` 或 `username`），并进行业务处理。