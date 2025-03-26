    <template>
    <!-- 写html的地方 -->
    <div class="todo-app">
            <div class="title">My Todo List</div>

            <div class="todo-form">
                <input v-model="str" class="todo-input" type="text" placeholder="请输入待办事项">
                <div @click="add" class="todo-button">Add todo</div>
            </div>

            <div v-for="(item, index) in list" 
                :key="index"
                :class="[item.isComplete ? 'complete' : 'item']"
                >
                <div>
                    <input v-model="item.isComplete" type="checkbox">
                    <span class="name">{{ item.text }}</span> <!-- 这里是插值表达式 {{  }} -->
                </div>
                <div @click="del(index)" class="del">del</div>
                
            </div>
        </div>
    </template>

    <script setup>
        import { ref } from "vue";
        const str = ref('')
        const list = ref([
            {
                'isComplete': false,
                'text': '吃饭',
            },
            {
                'isComplete': false,
                'text': '睡觉',
            },
            {
                'isComplete': false,
                'text': '打豆豆',
            }
        ])
        function add(){
            list.value.push({
                'isComplete': false,
                'text': str.value,
            })
            str.value = ''
        }

        function del(index) {
            list.value.splice(index, 1)
        }
        
    </script>

    <style scoped>
    /* 写css的地方 */
    body  {
                background: linear-gradient(to right, purple, blue);
            }
            .del {
                color: red;
            }
            .complete {
                display: flex;
                box-sizing: border-box;
                width: 80%;
                height: 50px;
                margin: 15px auto;
                padding: 16px;
                box-shadow: rgba(149, 157, 165, 0.2) 0px 8px 20px;
                border-radius: 20px;

                align-items: center;
                justify-content: space-between;
                text-decoration: line-through;
                opacity: 0.4;
            }
            .item {
                display: flex;
                box-sizing: border-box;
                width: 80%;
                height: 50px;
                margin: 15px auto;
                padding: 16px;
                box-shadow: rgba(149, 157, 165, 0.2) 0px 8px 20px;
                border-radius: 20px;

                align-items: center;
                justify-content: space-between;
            }
            .todo-input { 
                
                border: 1px solid #dfe1e5; 
                outline: none;
                width: 60%;
                height: 50px;
                border-radius: 20px 0 0 20px;
                padding-left: 20px;
            }
            .todo-button {
                border: 1px solid #dfe1e5; 
                outline: none;
                width: 100px;
                height: 52px;
                border-radius: 0 20px 20px 0;
                background: linear-gradient(to right, purple, blue);
                color: #ffff;
                line-height: 52px;
                text-align: center;
                cursor: pointer;
                user-select: none;
            }
            .todo-app {
                width: 98%;
                height: 500px;
                background-color: #ffff;
                border: 1px solid #ccc; /* 添加边框 */
                border-radius: 15px; /* 设置圆角半径 */
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1); /* 添加阴影 */
                margin-top: 40px;
                margin-left: 1%; /*宽度是98%，那么边距设为1%就是居中了 */
            }
            .title {
                font-size: 30px; /*字体大小 */
                font-weight: 600; /*字体粗细 */
                text-align: center; /*把字横向居中 */
                margin-top: 30px;
                margin-bottom: 20px;
            }
            .todo-form {
                display: flex;
                margin-left: 30px;
            }
    </style>
`