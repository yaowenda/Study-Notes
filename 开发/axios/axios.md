## axios对接后端接口，实现增删改查

### getList接口

    <script setup>
        async function getList() {
                    const res = await axios({
                        url: "",
                        method: "GET"
                    })

                    list.value = res.data.list //后端返回的结构中有data，data下有list
                }

        getList()
    </script>

在getList函数中通过axios请求接口，接口的地址用字符串的形式，然后是请求方式。然后用res接收返回的数据。async和await成对出现（没细学）。

vue3的setup语法create钩子函数就是直接调用就可以了，直接在script中写getList()

这样每次打开页面，页面会自动请求一次接口，获取到list并赋给list变量。

### 增加接口 
POST方法
参数：{
    "value": //待办事项内容
    "isComplete": // 是否完成
}


    async function add() {
        await axios({
            url: "",
            method: "POST",
            data: {
                value: str.value, //输入框的内容 v-model双向绑定
                isComplete: false
            }
        })
        getList() //重新请求一次接口，更新页面
    }

### 修改接口 
POST
参数：{
    "id": //事项的id
}


    async function update(id) {
        await axios({
            url: "",
            method: "POST",
            data: {
                id
            }
        })
        getList()
    }

data里面的id需要通过触发时传入，找到那个单选框，在其中添加@click="update(item._id)" _id是getList接口返回的list里面的id，具有唯一性

### 删除接口
POST
参数：{
    "id": //事项的id
}

    async function del(id) {
        await axios({
            url: "",
            method: "POST",
            data: {
                id：id
            }
        })
        getList()
    }

找到这个函数触发的位置，@click="del(item._id)"