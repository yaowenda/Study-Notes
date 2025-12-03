## 介绍

Transformers及相关库

- Transformers:核心库，模型加载、模型训练、流水线等
- Tokenizer:分词器，对数据进行预处理，文本到token,序列的互相转换
- Datasets:数据集库，提供了数据集的加载、处理等方法
- Evaluate:评估函数，提供各种评价指标的计算函数
- PEFT:高效微调模型的库，提供了几种高效微调的方法，小参数量撬动大模型
- Accelerate:分布式训练，提供了分布式训练解决方案，包括大模型的加载与推理解决方案
- Optimum:优化加速库，支持多种后端，如Onnxruntime、OpenVinos等
- Gradio:可视化部署库，几行代码快速实现基于Web交互的算法演示系统

 

创建虚拟环境

```
conda create -n huggingfaceTransformer python=3.10
```

```
conda activate huggingfaceTransformer
```

pypi 换清华源

```
pip config set global.index-url https://mirrors.tuna.tsinghua.edu.cn/pypi/web/simple
```

虚拟环境中安装pytorch

```
pip3 install torch torchvision
```

```
pip install transformers datasets evaluate peft accelerate gradio optimum sentencepiece
```

```
pip install jupyterlab scikit-learn pandas matplotlib tensorboard nltk rouge
```

练习：

```py
# 导入gradio
import gradio as gr
# 导入transformers相关包
from transformers import pipeline
# 通过Interface加载pipeline并启动阅读理解服务
# 如果无法通过这种方式加载，可以采用离线加载的方式
gr.Interface.from_pipeline(pipeline("question-answering", model="uer/roberta-base-chinese-extractive-qa")).launch()
```

搭建了一个简单的应用：

![image-20251126151811214](assets/image-20251126151811214.png)

## 基础组件 Pipeline

将数据预处理、模型调用、结果后处理三部分组装成的流水线

使我们能够直接输入文本便获得最终的答案

![image-20251126151037239](assets/image-20251126151037239.png)

### pipeline的创建和使用

```py
from transformers import *
pipe = pipeline("text-classification", model="distilbert-base-uncased-finetuned-sst-2-english")
pipe("I've been waiting for a Hugging Face course my whole life.") # [{'label': 'POSITIVE', 'score': 0.9982948899269104}]
pipe("i am sad") # pipe("i am sad")
```

预先加载模型，再创建pipeline

```py
from transformers import pipeline
from transformers import *
model = AutoModelForSequenceClassification.from_pretrained("tabularisai/multilingual-sentiment-analysis")
tokenizer = AutoTokenizer.from_pretrained("tabularisai/multilingual-sentiment-analysis")
pipe = pipeline("text-classification", model=model, tokenizer=tokenizer)


```

```py
qa_model = pipeline("question-answering", "timpal0l/mdeberta-v3-base-squad2")
question = "Where do I live?"
context = "My name is Tim and I live in Sweden."
qa_model(question = question, context = context)
```

### pipeline的其它实例

```py
checkpoint = "google/owlvit-base-patch32"
detector = pipeline(model=checkpoint, task="zero-shot-object-detection")
```

```py
import requests
from PIL import Image
# https://unsplash.com/photos/oj0zeY2Ltk4/download?ixid=MnwxMjA3fDB8MXxzZWFyY2h8MTR8fHBpY25pY3xlbnwwfHx8fDE2Nzc0OTE1NDk&force=true&w=640

url = "https://images.unsplash.com/photo-1596120236172-231999844ade?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"
im = Image.open(requests.get(url, stream=True).raw)
im
```

![image-20251126161733316](assets/image-20251126161733316.png)

```py
predictions = detector(
    im,
    candidate_labels=["hat", "sunglasses", "book"],
)
predictions


[{'score': 0.3982282876968384,
  'label': 'sunglasses',
  'box': {'xmin': 638, 'ymin': 417, 'xmax': 783, 'ymax': 482}},
 {'score': 0.2595130503177643,
  'label': 'book',
  'box': {'xmin': 491, 'ymin': 519, 'xmax': 920, 'ymax': 780}},
 {'score': 0.18115784227848053,
  'label': 'hat',
  'box': {'xmin': 69, 'ymin': 315, 'xmax': 478, 'ymax': 661}},
 {'score': 0.13411781191825867,
  'label': 'sunglasses',
  'box': {'xmin': 713, 'ymin': 433, 'xmax': 781, 'ymax': 482}},
 {'score': 0.11613769084215164,
  'label': 'sunglasses',
  'box': {'xmin': 638, 'ymin': 419, 'xmax': 784, 'ymax': 481}}]
```

```py
from PIL import ImageDraw

draw = ImageDraw.Draw(im)

for prediction in predictions:
    box = prediction["box"]
    label = prediction["label"]
    score = prediction["score"]
    xmin, ymin, xmax, ymax = box.values()
    draw.rectangle((xmin, ymin, xmax, ymax), outline="red", width=1)
    draw.text((xmin, ymin), f"{label}: {round(score,2)}", fill="red")

im
```

![image-20251126161811693](assets/image-20251126161811693.png)

### pipeline背后的实现

```py
from transformers import *
import torch
tokenizer = AutoTokenizer.from_pretrained("tabularisai/multilingual-sentiment-analysis")
model = AutoModelForSequenceClassification.from_pretrained("tabularisai/multilingual-sentiment-analysis")
```

```python
input_text = "我觉得不太行"
inputs = tokenizer(input_text, return_tensors="pt")
inputs

{'input_ids': tensor([[ 101, 3976, 7162, 3775, 2080, 3199, 7069,  102]]), 'attention_mask': tensor([[1, 1, 1, 1, 1, 1, 1, 1]])}

```

```python
res = model(**inputs)
res

SequenceClassifierOutput(loss=None, logits=tensor([[ 1.2099,  2.5130, -1.2441, -0.9963, -1.3152]],
       grad_fn=<AddmmBackward0>), hidden_states=None, attentions=None)
```

```python
logits = res.logits
logits = torch.softmax(logits, dim=-1)
logits

tensor([[0.2017, 0.7425, 0.0173, 0.0222, 0.0161]], grad_fn=<SoftmaxBackward0>)
```

```python
pred = torch.argmax(logits).item()
pred

1
```

```py
model.config.id2label

{0: 'Very Negative',
 1: 'Negative',
 2: 'Neutral',
 3: 'Positive',
 4: 'Very Positive'}
```

```py
result = model.config.id2label.get(pred)
result

'Negative'
```

