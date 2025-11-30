```py
import torchvision
from torch.utils.data import DataLoader
from torch.utils.tensorboard import SummaryWriter

dataset_transform = torchvision.transforms.Compose([
    torchvision.transforms.ToTensor()
])
# 测试数据集
test_set = torchvision.datasets.CIFAR10(root="./dataset", train=False, transform=dataset_transform, download=True)

test_loader = DataLoader(dataset=test_set, batch_size=4, shuffle=True, num_workers=0, drop_last=False)

# 测试数据集中第一张图片的shape及target
img, target = test_set[0]
print(img.shape) # torch.Size([3, 32, 32])
print(target) # 3

# Dataloader中每一项的shape及target
for data in test_loader:
    imgs, targets = data
    print(imgs.shape) # 如 torch.Size([4, 3, 32, 32])
    print(targets) # 如 tensor([6, 5, 1, 4])
```

如果Dataloader的drop_last设置为true，会把最后一个batch舍去，避免最后一个batch不足batch_size