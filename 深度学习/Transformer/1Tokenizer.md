## Tokenizer简介
### 数据预处理

- Step1 分词：使用分词器对文本数据进行分词（字、字词）；
- Step2 构建词典：根据数据集分词的结果，构建词典映射（这一步并不绝对，如果采用预训练词向量，词典映射要根据词向量文件进行处理)；
- Step3 数据转换：根据构建好的词典，将分词处理后的数据做映射，将文本序列转换为数字序列；
- Step4 数据填充与截断：在以batch输入到模型的方式中，需要对过短的数据进行填充，过长的数据进行截断，保证数据长度符合模型能接受的范围，同时patch内的数据维度大小一致。

## Tokenizer基本使用

- 加载保存(from_pretrained/save_pretrained)
- 句子分词(tokenize)
- 查看词典(vocab)
- 索引转换(convert_tokens_to_ids/convert_ids_to_tokens)
- 填充截断（padding/truncation)
- 其他输入(attention_mask/token_type_ids)

我只需要把我的数据准备好，然后调用tokenizer(inputs)就好了，比如：

```py
from transformers import AutoTokenizer
sen = "弱小的我也有梦想"
inputs = tokenizer(sen, padding="max_length", max_length=15)
inputs
```

处理batch数据：

```py
sens = ["弱小的我也有大梦想",
        "有梦想谁都了不起",
        "追逐梦想的心，比梦想本身，更可贵"]
res = tokenizer(sens, return_token_type_ids=True)
# token_type_ids 标记每个 token 属于第几个句子
res
```

```json
{'input_ids': [[101, 3727, 3459, 5718, 3976, 2135, 4461, 3197, 4614, 3898, 102], [101, 4461, 4614, 3898, 7363, 7838, 2146, 2080, 7533, 102], [101, 7717, 7728, 4614, 3898, 5718, 3792, 10064, 4839, 4614, 3898, 4476, 7590, 10064, 4449, 2756, 7495, 102]], 'token_type_ids': [[0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0], [0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]], 'attention_mask': [[1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1], [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]]}

```

## 经过分词处理之后的数据集中多了inputs_ids, token_type_ids, attention_mask, labels这几个features，这些是干什么的？

### input_ids

是将文本中的每一个词或子词（Token）映射到分词器**词汇表（Vocabulary）中对应的数字索引**。

模型通过这些 ID 来识别每一个输入的 token 是什么。

### attention_mask（注意力掩码）

它是一个由 `1` 和 `0` 组成的二进制序列，告诉模型在计算注意力时应该**关注**哪些 token，以及应该**忽略**哪些 token。`1` 表示该位置是一个**真实**的 token（需要模型关注），`0` 表示该位置是用来**填充（Padding）**的 token（模型应该忽略）

### token_type_ids（分段类型ID）

标记每个token属于哪个句子

### labels（标签）

监督学习任务的真实答案或目标值，分类任务中时通常是个数字
