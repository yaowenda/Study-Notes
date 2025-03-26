## 作用域

作用域分全局作用域、函数作用域、块级作用域

    {
        var a = 1;
        console.log(a); // 1
    }
    console.log(a); // 1
    // 通过var定义的变量可以跨块作用域访问到。
    // if for while等语句块，也会产生块级作用域

    (function A() {
        var b = 2;
        console.log(b); // 2
    })();
    // console.log(b); // 报错，
    // 可见，通过var定义的变量不能跨函数作用域访问到


## var let const
var定义的变量，没有块的概念，**可以跨块访问, 不能跨函数访问**。
let定义的变量，**只能在块作用域里访问**，不能跨块访问，也不能跨函数访问。
const用来定义常量，**使用时必须初始化**(即必须赋值)，**只能在块作用域里访问，而且不能修改**。

    // 块作用域
    {
        var a = 1;
        let b = 2;
        const c = 3;
        // c = 4; // 报错
        var aa;
        let bb;
        // const cc; // 报错
        console.log(a); // 1
        console.log(b); // 2
        console.log(c); // 3
        console.log(aa); // undefined
        console.log(bb); // undefined
    }
    console.log(a); // 1
    // console.log(b); // 报错
    // console.log(c); // 报错

    // 函数作用域
    (function A() {
        var d = 5;
        let e = 6;
        const f = 7;
        console.log(d); // 5
        console.log(e); // 6  
        console.log(f); // 7 
    })();
    // console.log(d); // 报错
    // console.log(e); // 报错
    // console.log(f); // 报错


常量的含义是指向的对象不能修改，但是**可以修改对象中的属性**

如：

const obj = {name:'aaa',age:18}

obj.name='bbb'