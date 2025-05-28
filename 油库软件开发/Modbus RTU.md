https://blog.csdn.net/as480133937/article/details/123197782



```cpp
#include <iostream>
#include <libmodbus1/config.h>
#include <unistd.h>
#include<libmodbus1/modbus.h>

// 配置参数
const int TIMER = 1; // 读取间隔时间(秒)
const int SERVER_ADDRESS = 1;
const int START_ADDRESS = 101;
const int START_ADDRESS_2 = 1;
const int QUANTITY = 100;
const int QUANTITY_2 = 4;
//const char *COM_PORT = "/dev/ttyS0"; // 示例的Linux串口设备文件
const char *COM_PORT = "COM6";
const int BAUD_RATE = 9600;

void modbus_master_rtu() {
    // 创建Modbus RTU连接
    modbus_t *ctx = modbus_new_rtu(COM_PORT, BAUD_RATE, 'E', 8, 1);
    if (ctx == NULL) {
        std::cerr << "Unable to create the libmodbus context" << std::endl;
        return;
    }

    // 设置从站地址
    modbus_set_slave(ctx, SERVER_ADDRESS);

    // 建立连接
    if (modbus_connect(ctx) == -1) {
        std::cerr << "Connection failed: " << modbus_strerror(errno) << std::endl;
        modbus_free(ctx);
        return;
    }

    uint16_t tab_reg[QUANTITY];
    uint16_t tab_reg_2[QUANTITY_2];

    // 无限循环读取数据
    while (true) {
        // 读取第一组保持寄存器
        int rc = modbus_read_registers(ctx, START_ADDRESS, QUANTITY, tab_reg);
        // 读取第二组保持寄存器
        int rc2 = modbus_read_registers(ctx, START_ADDRESS_2, QUANTITY_2, tab_reg_2);

        // 处理读取结果
        if (rc == QUANTITY) {
            for (int i = 0; i < rc; i++) {
                std::cout << tab_reg[i];
                if (i < rc - 1) {
                    std::cout << ", ";
                }
            }
            std::cout << std::endl;
        } else {
            std::cerr << "Error reading registers: " << modbus_strerror(errno) << std::endl;
        }

        if (rc2 == QUANTITY_2) {
            for (int i = 0; i < rc2; i++) {
                std::cout << tab_reg_2[i];
                if (i < rc2 - 1) {
                    std::cout << ", ";
                }
            }
            std::cout << std::endl;
        } else {
            std::cerr << "Error reading registers: " << modbus_strerror(errno) << std::endl;
        }

        // 暂停执行,等待下一轮读取
        sleep(TIMER);
    }

    modbus_close(ctx);
    modbus_free(ctx);
}

int main() {
    modbus_master_rtu();
    return 0;
}

```



python程序设计 92

计算机与网络通信 94、、、

数据结构 92、、、

web应用安全 99、、、

密码学 97、、、

算法设计与分析 100、、、

网络攻防技术 98、、、

java程序设计 99、、、

操作系统 93、、、、

机器学习 95、、、

计算机组成原理 97、、、



本论文系统梳理了当前伪造人脸检测领域的主流检测方法与研究趋势，并聚焦AIGC背景下深度伪造检测问题，首次将针对扩散模型伪造人脸的检测方法进行了总结。根据特征选择的不同，提出了面向图像/视频维度的检测方法分类框架：图像检测分为基于空间域与频率域的方法，视频检测分为基于时空一致性、生物特征与多模态特征的方法。调研了110余篇文献，归纳了典型数据集与评价指标，分析了各类方法的核心原理、优劣、适用场景，构建了1张人脸伪造检测发展时间线图与6张调表性文献方法原理图。最后，对该领域当前研究瓶颈进行了剖析，并提出了未来研究方向建议。