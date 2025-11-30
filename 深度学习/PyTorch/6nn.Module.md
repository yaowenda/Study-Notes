```py
import torch
from torch import nn
import torch.nn.functional as F

class Tudui(nn.Module):
    def __init__(self, *args, **kwargs) -> None:
        super().__init__(*args, **kwargs)
        self.conv_1 =  nn.Conv2d(1,20,5)
        self.conv_2 = nn.Conv2d(20,20,5)

    def forward(self,x):
        x = F.relu(self.conv_1(x))
        return F.relu(self.conv_2(x))

tudui = Tudui()
x = torch.rand(1, 1, 32, 32)
result = tudui(x) # 调用继承自nn.Module的模型实例，会自动执行该类中的forward方法
print(result)
```

