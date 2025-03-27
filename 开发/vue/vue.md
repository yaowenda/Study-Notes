Vue.js - 渐进式JavaScript 框架

    <script setup>
    // 写javascript的地方
    </script>
    
    <template>
        <!-- 写html的地方 -->
    </template>
    
    <style scoped>
    /* 写css的地方 */
    </style>

### 创建vue项目
安装node.js
全局安装vue CLI：`npm install -g @vue/cli`
执行vue ui 报错：vue : 无法加载文件 C:\Users\86151\AppData\Roaming\npm\vue.ps1，因为在此系统上禁止运行脚本。
在powershell中运行 `Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass`
然后再powershell运行vue ui

### 运行项目
`ctrl+j` 打开控制台，`npm i`安装所需依赖 `npm run serve`

### vue3中定义变量

在vue3中创建变量和vue2、javascript不太一样，在vue中定义变量需要通过ref来定义

    <script>
    import { ref } from "vue";
    
    const str = ref('dadas') 
    </script>

如果想在script中输出其值：console.log(str.value) //要有.value才能输出值

如果在template中输出其值：{{str}} 在template中不需要.value，直接用str即可

### 监听事件

监听事件用@

如果想点击按钮触发函数

    <script setup>
        const str = ref("123")
        function add() {
            console.log(str.value);
        }
    
    </script>
    
    <div @click="add" class="todo-button">Add todo</div>

鼠标移入：@mouseenter 鼠标移出：@mouseleave

### 双向绑定 v-model
输入的值自动绑定到str中：

    <div class="todo-form">
        <input v-model="str" class="todo-input" type="text" placeholder="请输入待办事项">
        <div @click="add" class="todo-button">Add todo</div>
    </div>
    
    <script setup>
    import { ref } from "vue";
    const str = ref("") //清空
    function add() {
        console.log(str.value);
    }
      
    </script>

输入框输入的内容会绑定到str中，当点击按钮时被输出

![alt text](assets/image.png)

既然叫双向绑定，那么当str的值改变时，input中的值也会改变

    <script setup>
        import { ref } from "vue";
        const str = ref("")
        function add() {
            console.log(str.value);
    
            str.value = "12345"
        }
    
    </script>

当点击按钮，输入框中的值也会变成12345

![alt text](assets/image-1.png)

双向绑定还可以用于其他地方

    <div>
        <input v-model="str" type="checkbox">
        <span class="name">吃饭</span>
    </div>
    
    <script setup>
    import { ref } from "vue";
    const str = ref(true)
    function add() {
        console.log(str.value);
    
        str.value = !str.value
    }
    </script>

这样每点击一次按钮，checkbox的值就会取反

### 动态绑定

动态绑定类名：

![alt text](assets/image-2.png)

实现当打钩，则变透明，不打勾则正常透明度

之前定义了两个样式 item 和 complete

    <script setup>
        import { ref } from "vue";
        const str = ref('item') // 初始时item样式
        function add() {
            console.log(str.value);
    
            str.value = 'complete' // 打勾时complete样式
        }
    </script>
    
    <div :class="str"> <!-- 动态绑定类名 -->
            <div>
                <input @click="add" type="checkbox">
                <span class="name">吃饭</span>
            </div>
            <div class="del">del</div>
        </div>

上面是写死的，优化：

    <div :class="[str? 'item' : 'complete']"> <!-- 用三元运算符 -->
        <div>
            <input @click="add" type="checkbox">
            <span class="name">吃饭</span>
        </div>
            <div class="del">del</div>
    </div>
    
    <script setup>
    import { ref } from "vue";
    const str = ref(true)
    function add() {
        console.log(str.value);
    
        str.value = !str.value;
    }
    </script>

这样点一次checkbox，就会变成false，再点一次就会变成true

### 循环

    <script setup>
        import { ref } from "vue";
        // const str = ref(true)
        const list = ref(['吃饭', '睡觉', '打豆豆'])
    
        // function add() {
        //     str.value = !str.value
        // }
    </script>
    
    <div v-for="(item, index) in list" 
        :key="index"
        :class="[str? 'item' : 'complete']">
        <div>
            <input @click="add" type="checkbox">
            <span class="name">{{ item }}</span> <!-- 这里是插值表达式 {{  }} -->
        </div>
        <div class="del">del</div>
    </div>

使用 v-for 指令时，需要为每个循环项添加一个唯一的 key 属性

### watch 侦听器
当侦听的变量发生变化，可以执行对应的函数

    <script setup>
        import { ref,watch } from "vue";
        const str = ref('')
        function add(newValue,oldValue){
            console.log('新值'+newValue,'旧值'+oldValue);
        }
        watch(str, add)
    </script>
    
    <div class="todo-form">
        <input v-model="str" class="todo-input" type="text" placeholder="请输入待办事项">
        <div @click="add" class="todo-button">Add todo</div>
    </div>

由于双向绑定，当输入时，变量str发生变化，就会执行add函数

![alt text](assets/image-3.png)

#### 深度侦听（侦听对象）
现在侦听一个对象：

    <script setup>
        import { ref,watch } from "vue";
        const str = ref({
            text: '',
        })
        function add(newValue,oldValue){
            console.log('新值'+newValue,'旧值'+oldValue);
        }
        watch(str, add)
    </script>
    
    <input v-model="str.text" class="todo-input" type="text" placeholder="请输入待办事项">

发现当输入框发生变化，并不会触发add函数，这是因为侦听的是一个对象，对象并没有变，是对象的属性变了，所以需要使用深度侦听：
watch(str, add, {deep: true})

### 组件
在vue中，每个页面都可以认为是一个组件

假如，页面需要重复用到一个组件，就可以把它写到src/components中
例如一个按钮组件：

    <script setup>
    </script>
    
    <template>
        <div class="BaseButton">点我</div>
    
    </template>
    
    <style scope>
        .BaseButton{
            width: 50px;
            height: 50px;
            border-radius: 10px;
            background-color: purple;
            border-sizing: border-box;
            padding: 5px;
            line-height: 50px;
            text-align: center;
            margin: 10px;
        }
    </style>

在页面中引入（一定要带.vue）：

    <script setup>
        import BaseButton from '@/components/BaseButton.vue'
        //被导入的组件叫做子组件
    </script>
    
    <template>
        <BaseButton></BaseButton>
        <BaseButton></BaseButton>
        <BaseButton></BaseButton>
        
    </template>

![alt text](assets/image-4.png)

那么如何改变按钮的文字呢？

#### props

props 是父组件向子组件传递数据的方式，子组件通过 props 接收数据

子组件：

    <script setup>
        import {defineProps} from 'vue'
        const props = defineProps(['text']) //可以定义多个props
    </script>
    
    <template>
        <div class="BaseButton">{{ props.text }}</div>
    
    </template>

父组件：

    <template>
        <BaseButton text="你好"></BaseButton>
        <BaseButton text="他好"></BaseButton>
        
        <BaseButton text="大家好"></BaseButton>


​        

    </template>

除了父组件往子组件传值，子组件也可以向父组件传值：

子组件：

    <script setup>
        import {defineProps, defineEmits} from 'vue'
        const props = defineProps(['text']) //可以定义多个props
        const emit = defineEmits(['ok'])
    
        function send() {
            emit('ok', 'hello')
        }
    </script>
    
    <template>
        <div @click="send" class="BaseButton">{{ props.text }}</div>
    
    </template>

首先，从vue中引入了一个工具类defineEmits，用来定义emit事件，然后定义了一个事件叫ok，当点击按钮，就执行send函数，send函数中调用emit函数，emit发出ok事件，并带一个参数hello

父组件：

    <script setup>
        import BaseButton from '@/components/BaseButton.vue'
        function add(str) {
            console.log(str);
            
        }
    </script>
    
    <template>
        <BaseButton @ok="add" text="你好"></BaseButton>

在父组件监听了ok事件，当这个事件被触发时，就会执行add函数，因为在父组件触发ok事件时带了个hello参数。所以，当点击按钮时，就会在控制台打印hello
