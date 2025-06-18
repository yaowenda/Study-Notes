  ## Java反射

User类

```java
package com.example.reflectdemo;

public class User {
    //成员变量
    public String name = "Tom";
    public int age = 18;
    private String gender = "Male";

    protected String job = "sec";

    //成员方法
    public void userinfo(String name, int age, String gender, String job){
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.job = job;
    }

    protected void users(String name, int age, String gender, String job){
        this.name = name;
        this.age = age;
    }

    //无参数构造方法
    public User(){
        System.out.println("无参数");
    }

    //一个参数的构造方法
    public User(String name){
        System.out.println(name);
    }
    //两个参数的构造方法
    private User(String name, int age){
        System.out.println(name);
        System.out.println(age);
    }


}

```

获取类的方法

GetClass类

```java
package com.example.reflectdemo;

import javax.servlet.http.HttpServlet;

public class GetClass {
    public static void main(String[] args) throws ClassNotFoundException {
        //1、根据全限定类名：Class.forName("全路径类名")
        Class aClass = Class.forName("com.example.reflectdemo.User");
        System.out.println(aClass.getName());

        //2、根据类对象：类.class
        Class aClass1 = User.class;
        System.out.println(aClass1.getName());

        //3、根据对象：对象.getClass()
        User user = new User();
        Class aClass2 = user.getClass();

        //4、通过类加载器获取Class对象
        ClassLoader classLoader = HttpServlet.class.getClassLoader();
        Class aClass3 = classLoader.loadClass("com.example.reflectdemo.User");
    }
}

```



获取成员变量：



**利用反射获取成员变量**

Class类中用于获取成员变量的方法

- Field[] getFields(): 返回所有公共成员变量对象的数组
- Field[] getDeclaredFields(): 返回所有成员变量对象的数组
- Field getField(String name): 返回单个公共成员变量对象
- Field getDeclaredField(String name): 返回单个成员变量对象

```java
public class GetField {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException {

        Class aClass = Class.forName("com.example.reflectdemo.User");

        System.out.println("获取公共的成员变量：\n");
        Field[] fields = aClass.getFields();
        for (Field field : fields) {
            System.out.println(field.getName());
        }

        System.out.println("获取所有的成员变量：\n");
        Field[] fields1 = aClass.getDeclaredFields();
        for (Field field : fields1) {
            System.out.println(field.getName());
        }

        System.out.println("获取单个公共成员变量：\n");
        Field fields2 = aClass.getField("name"); //获取单个公共成员变量
        System.out.println(fields2.getName());

        System.out.println("获取单个私有成员变量：\n");
        Field fields3 = aClass.getDeclaredField("gender");
        System.out.println(fields3.getName());
    }
}


```

赋值：

Field类中用于创建对象的方法

- void set(Object obj, Object value): 赋值
- Object get(Object obj) 获取值。

```java
public class GetField {
    public static void main(String[] args) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {

        Class aClass = Class.forName("com.example.reflectdemo.User");

        //获取公共成员变量name的值
        User u = new User();
        Field field = aClass.getField("name"); //返回单个公共成员变量对象
        Object a = field.get(u);//Field类的get(Object obj)方法用于获取指定对象obj中该字段（成员变量）的值
        System.out.println(a);

        field.set(u, "Jerry"); //赋值
        Object aa = field.get(u);//Field类的get(Object obj)方法用于获取指定对象obj中该字段（成员变量）的值
        System.out.println(aa);

    }
}
```

输出结果：

无参数
Tom
Jerry



原因：new了就会调用无参数的构造方法



**利用反射获取构造方法**

Class类中用于获取构造方法的方法

- Constructor<?>[] getConstructors(): 返回所有公共构造方法对象的数组
- Constructor<?>[] getDeclaredConstructors(): 返回所有构造方法对象的数组
- Constructor<T> getConstructor(Class<?>... parameterTypes): 返回单个**公共**构造方法对象
- Constructor<T> getDeclaredConstructor(Class<?>... parameterTypes): 返回单个构造方法对象

Constructor类中用于创建对象的方法

- T newInstance(Object... initargs): 根据指定的构造方法创建对象
- setAccessible(boolean flag): 设置为true,表示取消访问检查

```java
public class GetConstructor {

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException
    {
        Class aClass = Class.forName("com.example.reflectdemo.User");
        //获取公共的构造方法
        System.out.println("获取公共的构造方法：\n");
        Constructor[] constructors = aClass.getConstructors();
        for (Constructor constructor : constructors){
            System.out.println(constructor);
        }

        System.out.println("\n获取所有的构造方法：\n");
        //获取所有的构造方法
        Constructor[] declaredConstructors = aClass.getDeclaredConstructors();
        for (Constructor declaredConstructor : declaredConstructors){
            System.out.println(declaredConstructor);
        }

        System.out.println("\n获取单个公共构造方法：\n");
        //获取单个参数的
        Constructor constructor1 = aClass.getConstructor(String.class);
        System.out.println(constructor1);
        //获取两个参数的构造方法(不能，因为是私有）
        Constructor constructor2 = aClass.getConstructor(String.class, int.class);
        System.out.println(constructor2);

        System.out.println("\n获取单个私有构造方法：\n");
        Constructor declaredConstructor = aClass.getDeclaredConstructor(String.class, int.class);
        System.out.println(declaredConstructor);
    }
}
```

对构造方法进行操作：

```java
// 根据指定的构造方法创建对象
        Constructor con2 = aClass.getDeclaredConstructor(String.class, int.class);//这是私有的构造方法
        con2.setAccessible(true);//临时开启访问权限 反射爆破，用于放射后操作私有属性或方法
        User uu = (User) con2.newInstance("Tom11", 30);
        System.out.println(uu);
```



### 利用反射获取成员方法

**Class类中用于获取成员方法的方法**

- `Method[] getMethods()`: 返回所有公共成员方法对象的数组，**包括继承的**
- `Method[] getDeclaredMethods()`: 返回所有成员方法对象的数组，不包括继承的
- `Method getMethod(String name, Class<?>... parameterTypes)`: 返回单个公共成员方法对象
- `Method getDeclaredMethod(String name, Class<?>... parameterTypes)`: 返回单个成员方法对象

------

**Method类中用于创建对象的方法**

Object invoke(Object obj, Object... args): 运行方法

- 参数一：用`obj`对象调用该方法
- 参数二：调用方法的传递的参数（如果没有就不写）
- 返回值：方法的返回值（如果没有就不写）
