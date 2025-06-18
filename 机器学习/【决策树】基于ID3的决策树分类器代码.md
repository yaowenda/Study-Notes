```py
from math import log

def createDataSet():
    dataSet = [[0, 0, 0, 0, 'no'],
               [0, 0, 0, 1, 'no'],
               [0, 1, 0, 1, 'yes'],
               [0, 1, 1, 0, 'yes'],
               [0, 0, 0, 0, 'no'],
               [1, 0, 0, 0, 'no'],
               [1, 0, 0, 1, 'no'],
               [1, 1, 1, 1, 'yes'],
               [1, 0, 1, 2, 'yes'],
               [1, 0, 1, 2, 'yes'],
               [2, 0, 1, 2, 'yes'],
               [2, 0, 1, 1, 'yes'],
               [2, 1, 0, 1, 'yes'],
               [2, 1, 0, 2, 'yes'],
               [2, 0, 0, 0, 'no']]
    labels = ['F1-AGE', 'F2-WORK', 'F3-HOME', 'F4-LOAN']
    return dataSet, labels

def createTree(dataset,labels,featLabels):
    classList = [example[-1] for example in dataset] #获取类别标签

    # 递归停止条件1：所有数据的类别都相同
    if classList.count(classList[0]) == len(classList): #如果类别完全相同则停止继续划分
        return classList[0] # 随便返回哪个都行，因为相同类别
    
    # 递归停止条件2：所有特征都已经用完
    if len(dataset[0]) == 1: #列数=1说明只剩下标签这一列，没有可用于划分的特征了
        return majorityCnt(classList)
    
    # 核心步骤：选择最优特征进行分割
    bestFeat = chooseBestFeatureToSplit(dataset) #选择最优特征进行分割
    bestFeatLabel = labels[bestFeat] #获取最优特征的标签
    featLabels.append(bestFeatLabel)
    myTree = {bestFeatLabel: {}} #key是最佳特征的名称，值是一个空字典，用来存放该节点下的各分支

    del(labels[bestFeat]) # 删除已经使用的特征标签

    featValue = [example[bestFeat] for example in dataset] #获取当前特征的所有取值
    uniqueVals = set(featValue) #set()去重，featValue数组变成了{0,1}或者{0,1,2}
    for value in uniqueVals:
        # 复制标签列表，因为python中列表是可变对象，所以要复制一份
        subLabels = labels[:]
        # 递归构建子树
        myTree[bestFeatLabel][value] = createTree(splitDataSet(dataset, bestFeat, value), subLabels, featLabels)
    return myTree

# 当某个分支不能再继续分裂的时候，选出这个节点的最终分类结果
# classList列表包含当前节点的所有样本的类别标签
def majorityCnt(classList):
    #统计每个类别出现了几次
    classCount = {}
    for vote in classList:
        if vote not in classCount.keys():
            classCount[vote] = 0
        classCount[vote] += 1

    # 按照类别出现的次数进行降序排序
    sortedClassCount = sorted(classCount.items(), key=lambda item: item[1], reverse=True)
    return sortedClassCount[0][0]  # 返回出现次数最多的类别标签

def chooseBestFeatureToSplit(dataset):
    numFeatures = len(dataset[0]) - 1  # 特征数量，减去最后一列的标签
    baseEntropy = calcShannonEnt(dataset)  # 计算数据集的初始熵
    bestInfoGain = 0.0  # 初始化最佳信息增益
    bestFeature = -1  # 最佳特征的索引

    for i in range(numFeatures):
        # 获取当前特征的所有取值
        featList = [example[i] for example in dataset]
        uniqueVals = set(featList)  # 去重，得到当前特征的所有唯一取值
        newEntropy = 0.0  # 初始化新的熵
 
        for value in uniqueVals:
            subDataset = splitDataSet(dataset, i, value)  # 按照当前特征的取值划分数据集
            prob = len(subDataset) / float(len(dataset))  # 当前子集占总数据集的比例
            newEntropy += prob * calcShannonEnt(subDataset)  # 加权计算子集的熵

        infoGain = baseEntropy - newEntropy  # 信息增益
        if infoGain > bestInfoGain:  # 如果信息增益更大，则更新最佳特征
            bestInfoGain = infoGain
            bestFeature = i

    return bestFeature

# 计算给定数据集的熵
def calcShannonEnt(dataset):
    numEntries = len(dataset)  # 数据集的样本数量
    labelCounts = {}  # 用于存储每个类别标签的计数

    # 统计每个标签出现的次数
    for featVec in dataset:
        currentLabel = featVec[-1]  # 获取当前样本的类别标签
        if currentLabel not in labelCounts.keys():
            labelCounts[currentLabel] = 0
        labelCounts[currentLabel] += 1  # 计数

    shannonEnt = 0.0  # 初始化熵值
    # 对于每个标签，计算其概率并计算熵
    for key in labelCounts:
        prob = float(labelCounts[key]) / numEntries  # 当前类别的概率
        shannonEnt -= prob * log(prob, 2)  # 使用对数计算熵

    return shannonEnt



def splitDataSet(dataset, axis, value):
    # 按照特征axis的取值value划分数据集
    retDataSet = []
    for featVec in dataset:
        if featVec[axis] == value:  # 如果当前样本的特征值等于value(value是有几个特征axis)
            reducedFeatVec = featVec[:axis]  # 保留特征值之前的部分
            reducedFeatVec.extend(featVec[axis + 1:])  # 添加特征值之后的部分
            retDataSet.append(reducedFeatVec)  # 将处理后的样本添加到新数据集中
    return retDataSet

if __name__ == '__main__':
    dataset, labels = createDataSet()  # 创建数据集和标签
    featLabels = []  # 用于存储特征标签
    myTree = createTree(dataset, labels, featLabels)  # 创建决策树
    print("决策树结构:", myTree)  # 打印决策树结构

```

结果：

决策树结构: {'F3-HOME': {0: {'F2-WORK': {0: 'no', 1: 'yes'}}, 1: 'yes'}}