```py
import torch
import torchvision
from torch import nn
from torch.nn import Conv2d
from torch.utils.data import DataLoader

test_data = torchvision.datasets.CIFAR10(root="./dataset", train=True, transform=torchvision.transforms.ToTensor(), download=True)

dataloader = DataLoader(test_data, batch_size=64)

class MyClass(nn.Module):

    def __init__(self, *args, **kwargs) -> None:
        super().__init__(*args, **kwargs)
        self.conv2d_1 =Conv2d(3,7,3, stride=1, padding=0)

    def forward(self,x):
        x = self.conv2d_1(x)
        return x

myclass = MyClass()
# print(myclass)
for data in dataloader:
    imgs, targets = data
    output = myclass(imgs)
    print(output.shape)
```

输出：

torch.Size([64, 7, 30, 30])

该数据集中的图像是RGB3通道，所以输入通道数是3，输出通道数是7代表卷积核的数量是7。因此会创建7个不同的3x3x3的卷积核（因为输入是 3 通道，每个核也要有 3 个通道才能做卷积）。对于每一张输入图像（3×32×32），这 7 个卷积核分别与其做卷积运算，得到 **7 张特征图（feature maps）**

7个通道 tensorboard是无法显示的，会报错