## 前言

#### // 为什么每个人的微信名称是不一样的，但代码是一样的，什么逻辑？
let name = "";
let obj = {};//对微信服务器发起网络请求拿到数据

name = obj.name;

    <div>{{name}}</div>

#### //让大括号中的代码执行三次

for(let i = 0; i < 3; i++) {
    console.log(i);
}

## 练习

![alt text](assets/image-1.png)

javascript代码写在`<script></script>`标签中，该标签在body中
创建变量用`let`，输出多个变量的内容用逗号隔开。修改不需要使用let。

### 另外一种数据类型，对象

![alt text](assets/image-3.png)

![alt text](assets/image-4.png)

删除属性：

    delete obj.sex;
    console.log(obj);

### 数组

    <script>
        let arr = [1,2,3,4,5,6,7];
        console.log(arr[0]); // 1
        arr.push(999); //将括号中的元素加到数组的最后一位
        console.log(arr); 
    
        //在数组的第一位添加元素 unshift
        arr.unshift(888);
        // [888,1,2,3,4,5,6,7]
        
        //在数组的任意位置添加元素 splice(插入位置的索引, 要删除的元素个数, 要插入的元素)
        arr.splice(3,0,777);
        // [888,1,2,777,3,4,5,6,7]
    
        // 可以同时插入多个元素
        arr.splice(2, 0, 666, 555); // 在索引2的位置插入666和555
        console.log(arr); // [888,1,666,555,2,777,3,4,5,6,7]
    
        //删除数组中的元素
        arr.splice(2, 2); // 从索引2开始删除2个元素
        
    </script>

### 等号

    <script>
        let num = 5;
        let str = "5";
    
        console.log(num == str); //true
        console.log(num === str); //false
    </script>

### 方法

    <script>
        function add() {
            console.log("nihao");
            console.log("你好");
        }
    
        add(); //调用了函数才会执行
        add(); // 调用两次执行两次
    </script>

**参数、返回值：**

    <script>
        function add(a, b) {
            let num = a + b;
            console.log(num);
            
        }
        add(2,3)
    </script>
    
    <script>
    function add(a, b) {
        let num = a + b;
        return num; //return的作用 1、终止函数进行 2、返回值
        console.log(num);
        
    }
    console.log(add(2,3)) // 5
    </script>