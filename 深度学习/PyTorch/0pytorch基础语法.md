torch.tensor([……]) 和 torch.Tensor([……]) 是不一样的

torch.tensor() 会 根据输入数据**推断 dtype 和 shape**

```py
# 整数列表 → 自动用 long (int64)
a = torch.tensor([1, 2, 3])
print(a.dtype)  # torch.int64

# 浮点列表 → 自动用 float32
b = torch.tensor([1.0, 2.0, 3.0])
print(b.dtype)  # torch.float32

# 显式指定类型
c = torch.tensor([1, 2, 3], dtype=torch.float32)
print(c.dtype)  # torch.float32
```

```py
# 即使传整数，也变成 float32！
x = torch.Tensor([1, 2, 3])
print(x)        # tensor([1., 2., 3.])
print(x.dtype)  # torch.float32 ❗

# 传浮点数 → 也是 float32
y = torch.Tensor([1.5, 2.5])
print(y.dtype)  # torch.float32

# 也可以用来创建指定形状的未初始化张量（旧风格）
z = torch.Tensor(2, 3)  # shape (2,3)，内容是随机垃圾值
```

