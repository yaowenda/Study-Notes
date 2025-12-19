**Absolute permeability estimation from microtomography using deep-learning surrogates** — J. de Castro Vargas Fernandes et al., *Scientific Reports*, 2024.
 *为什么相关*：直接用 micro-CT（数字岩）与深度学习做渗透率估计，方法学上靠近你要替换/扩展的“图像→物性”流程，可借鉴数据增强、超分辨/替代数值模拟（surrogate）策略。

**Nuclear Magnetic Resonance Logging-Based Permeability …** — Z. Liang et al., *Processes (MDPI)*, 2024.
 *为什么相关*：这篇基于 NMR（T₂）数据做渗透率计算/建模，提供了 NMR→渗透率的经验/特征工程思路，你可以把这些 NMR 特征作为 1D 输入给 Transformer 的 Query。

**Permeability Prediction of Carbonate Reservoir Based on Machine Learning** — J. Zhao et al., *Energies (MDPI)*, 2024.
 *为什么相关*：机器学习预测渗透率的案例（虽以碳酸盐岩为主），包含特征选择、模型对比和误差分析，可作为基线对照。 [MDPI](https://www.mdpi.com/1996-1073/17/6/1458?utm_source=chatgpt.com)

**Multi-modal Multi-scale TRansformer (M2TR)** — Wang et al., (原始方法，Deepfake 检测方向)，2021 —（作为方法学原文/架构参考）
 *为什么相关*：M2TR 是你在描述里提到的跨模态多尺度 Transformer 的原始/近源工作（融合 RGB 与频域信息）。虽不是岩心方向，但其 **CMF（cross-modality fusion）** 思路非常适合改造为 CT（Key/Value）与 NMR 曲线（Query）的融合机制。 [arXiv+1](https://arxiv.org/abs/2104.09770?utm_source=chatgpt.com)

**Cross-Modality Fusion Transformer for Multispectral Object Detection (CFT)** — Qingyun et al., arXiv 2021（方法学参考）
 *为什么相关*：提出了简单有效的跨模态 fusion transformer 架构，能作为实现 CMF 的工程参考（如何把不同空间/维度的特征对齐并做注意力）。 [arXiv](https://arxiv.org/abs/2111.00273?utm_source=chatgpt.com)

**A 2025 系统综述：Multimodal Deep Learning and Fusion（系统综述 / 综评）** — F. Manzoor et al., 2025（预印本 / 综述）
 *为什么相关*：2025 年的多模态综述可以帮助你了解最近一两年（含 2024–2025）跨模态 transformer 的主流融合策略与实践风险（数据不平衡、对齐、解释性）。对构建实验设计与对比基线很有价值。

___

**Absolute permeability estimation from microtomography using transformer-based deep learning models** — *Scientific Reports*, 2024.
 *说明*：直接用 micro-CT（digital rock images）+ transformer 模型来快速估计渗透率，讨论了 transformer 在孔隙结构表征上的优势，可作为仅基于图像的 baseline。

**Permeability Prediction of Carbonate Reservoir Based on Machine Learning** — *Energies (MDPI)*, 2024.
 *说明*：用传统 ML（例如 XGBoost）与工程特征进行渗透率预测，能作为你做融合模型时的工程 baseline（特别是可见哪些曲线特征常用）。

**Multifractal estimation of NMR T2 cut-off value in low-permeability rocks** — (期刊 PDF，2024).
 *说明*：聚焦 NMR T2 谱线在低渗岩中的分析、T2 截断值与孔类型的关系，能提供把 T2 曲线转化为“有意义特征”或先验的做法（对你的 Query 编码设计很有帮助）。

**MR–CT image fusion method based on deep learning** — *BMC Medical Imaging*（或等效期刊），2024.
 *说明*：虽然是医学影像（MR/CT）融合，但方法/架构（特征提取 + 深度融合块）与 CT（图像）与 NMR（另一个模态）融合有直接启发，可借鉴 fusion 模块设计与 loss 设定。

**A Deep Learning-Based Multimodal Model (Multi_CycGT) — 2024**（跨模态 + transformer 的示例，虽然应用领域不同）
 *说明*：展示了将 1D/2D/图结构数据用混合网络（GCN + Transformer）融合预测的方法论，可为你设计将 NMR（1D）编码为 token/embedding 的策略提供参考。

**(补充)** 近期综述/应用方向 & 2025 的若干工作（可关注） — 有多篇 2025 年关于用 ViT/Transformer 预测孔隙/渗透的工作以及将“多尺度图像 + ML”结合以提高泛化的论文（示例检索到 2025 的若干题目）。如果你需要，我可以继续把 2025 年完整 DOI / PDF 下载链接再抓出来。

___

A comprehensive review of computer vision for reservoir modelling

总结：这篇综述讨论了计算机视觉在储层建模中的进展，特别是多模态融合技术如基于 Transformer 的跨注意力网络（cross-attention networks），用于整合成像数据（如 CT）和谱图数据。这与您的 CMF 模块高度契合，可作为融合 CT-NMR 的参考框架。

Intelligent Method of Saturation Prediction Using Multiscale Data Integration

总结：提出了一种智能饱和度预测方法，结合数据增强和多尺度数据整合，使用深度学习融合多模态输入（如图像和曲线数据）。适用于页岩饱和度反演，可借鉴您的输出（饱和度分布图），并扩展到渗透率联合预测。

Second‐Order Degradation Modeling and Multiscale Feature Fusion for Digital Rock Images

总结：使用二阶退化模型模拟低质量数字岩心图像，并通过多尺度特征融合提升分割性能。涉及图像处理和融合机制，与您的 CT 图像特征提取及 NMR 曲线 Query-Key 注意力机制相关，可用于处理异构数据。

Evaluation of oil saturation in shale from an NMR T1–T2 map based on deep learning 

总结：基于深度学习的 NMR T1-T2 图谱评估页岩油饱和度，帮助识别流体类型和储量。这可与您的课题结合，扩展到 CT-NMR 融合预测饱和度分布。

Comprehensive permeability-transform solutions for shale and tight rocks 

总结：使用全球沉积盆地数据约束页岩渗透率和孔隙度范围，涉及多模态数据整合（如图像和渗透测试）。虽未直接提 Transformer，但可作为您的渗透率预测基准，并融入跨模态融合创新。