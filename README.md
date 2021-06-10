# VirtualTerminal
启动后有三个线程：
1 定时发送经纬度
2 接收服务端响应
3 接收标准输入流设置参数

-x 设置纬度
-y 设置经度
-t 设置定时发送经纬度时间间隔，单位毫秒

例：-y 121.80178 -x 31.356974 -t 20000

# mqtt

client 主类设置网关和子设备三属性 

-ac 添加额外子设备
-ac 设备id 产品code 密钥

例：-c 20210603009 AFIeiEN7 kdBR57zfjmoNnFz6

-sp 发送属性
-sp 设备id 属性json
例： -sp 20210603009 {"full-open":0,"full-close":1}

