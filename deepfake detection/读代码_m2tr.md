# Muti-scale Transformer

## Transformer Head的初始化模块：

负责构建QKV向量，以及对注意力输出进行进一步处理

```python
# 多注意力头初始化
    def __init__(self, patchsize, d_model):
        #patchsize patch的大小
        #d_model 输入的通道数
        super().__init__()
        self.patchsize = patchsize
        ## Query嵌入层：将输入特征转换为查询向量
        self.query_embedding = nn.Conv2d(
            d_model, d_model, kernel_size=1, padding=0
        ) # 第一个参数是输入的通道数，第二个参数是输出的通道数，第三个参数是卷积核的大小，第四个参数是填充的大小。
        # Value嵌入层：将输入特征转换为值向量
        self.value_embedding = nn.Conv2d(
            d_model, d_model, kernel_size=1, padding=0
        )
        # Key嵌入层：将输入特征转换为键向量
        self.key_embedding = nn.Conv2d(
            d_model, d_model, kernel_size=1, padding=0
        )
        # 输出处理层：
        self.output_linear = nn.Sequential(
            nn.Conv2d(d_model, d_model, kernel_size=3, padding=1), # 3x3卷积处理，提取局部空间特征，保持尺寸(padding=1)
            nn.BatchNorm2d(d_model), # 通过BatchNorm进行归一化，保持训练稳定，加快收敛
            nn.LeakyReLU(0.2, inplace=True), # 使用LeakyReLU作为激活函数，引入非线性特性，LeakyReLU相比于ReLU，在负区间上提供了一个较小的梯度，即使输入值为负，也不容易死神经元。
        )
```

1*1卷积不涉及空间邻域的滑动，所以没有必要填充，不会丢失边界信息，padding=0

**输出处理层：**对注意力输出做了一个标准的残差块前处理，增强模型的表达能力、鲁棒性和收敛速度



## 多尺度多头注意力的前向传播模块

和标准的Transformer一样，它也构建了QKV，然后用注意力机制计算输出，但它多了一步：**每个head的patch尺寸是不同的**。

**输入一个二维特征图 → 对其在不同尺度下做注意力计算 → 多个注意力头输出 → 通道拼接 → 残差处理 → 输出结果**。





标准注意力机制计算的格式（通常是 `(batch_size, sequence_length, embedding_dimension)`）
