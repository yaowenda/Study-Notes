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



