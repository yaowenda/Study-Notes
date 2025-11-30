```py
import torchvision

# 下载数据集
trian_set = torchvision.datasets.CIFAR10(root="./dataset", train=True, download=True)
test_set = torchvision.datasets.CIFAR10(root="./dataset", train=False, download=True)

print(test_set[3]) # (<PIL.Image.Image image mode=RGB size=32x32 at 0x25DD9011610>, 0) 0代表类别
```

```py
img, target = test_set[3]
img.show() # 查看图片
```

图像要给pytorch使用，需要转换为tensor类型

```py
import torchvision
from torch.utils.tensorboard import SummaryWriter

dataset_transform = torchvision.transforms.Compose([
    torchvision.transforms.ToTensor()
])

# 下载数据集 通过 transform=dataset_transform 将图像转换为tensor
trian_set = torchvision.datasets.CIFAR10(root="./dataset", train=True, transform=dataset_transform, download=True)
test_set = torchvision.datasets.CIFAR10(root="./dataset", train=False, transform=dataset_transform, download=True)

# 加入到tensorboard
writer = SummaryWriter("logs")
for i in range(10):
    img, target = test_set[i]
    writer.add_image("test_set", img, i)

writer.close()


```

