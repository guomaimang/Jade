---
date: 2022-09-01
article: true
order: 3
headerDepth: 1
---

# Specification Definition

## 服务端

### 数据传输

完全依赖 Json

图片则转换为Base64

### 从服务端获取动态的Json

```
[
    { "id":"11", "time":"2022-11-11 19:07", "posterName":"hnss", "message":"Trip in England.", "location":"[43,11]" },
    { "id":"8" , "time":"2022-11-10 19:07", "posterName":"guomaimang", "message":"A rainy day...", "location":"[99,18]" },
    { "id":"3" , "time":"2022-08-11 03:07", "posterName":"hongshu", "message":"This is my new Pet!" , "location":"[-43,11]"}
]
```

- https://juejin.cn/post/6844903765603336206
- https://xu3352.github.io/java/2018/04/04/gson-parse-json-arrays
- [使用Gson解析json格式的字符串](https://blog.csdn.net/wjr1949/article/details/72685113)
- [特殊字符导致json字符串转换成json对象出错](https://cloud.tencent.com/developer/article/1674348)
- [由json字串生成json对象时的转义问题](https://blog.csdn.net/k469785635/article/details/72910629)

> 注: Json 字符串 不能有任何换行符，或者无效空格

### 时间戳

各处统一使用 HKT，且格式为 “yyyy-MM-dd hh:mm:ss”

对于sql

```
select str_to_date('08.09.2008 08:09:30', '%m.%d.%Y %h:%i:%s'); -- 2008-08-09 08:09:30
```

- [Mysql处理时间](https://www.cnblogs.com/ggjucheng/p/3352280.html)

对于Java

```
Date dNow = new Date( );
SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
System.out.println("当前时间为: " + ft.format(dNow));
```

### JOSN通讯中错误代号

- 0: 无错误
- -1: 未知错误
- -2: 用户鉴权错误

## 数据库

[JDBC连接MySQL的IDEA代码操作（5种方式的最优解）](https://blog.csdn.net/wx1528159409/article/details/87779523)

- name: gem-project
- db: gem-project
- password: ***
- ip address: 20.2.70.63:3306

## 客户端

### Trend

- [Android的ListView简单使用的实例(附Demo)](https://blog.csdn.net/qq_36243942/article/details/82085986)
- [Android ViewHolder 的基本使用](https://www.jianshu.com/p/a14feb480804)
- [Android Jetpack：LiveData与ViewModel的使用教程](https://blog.csdn.net/PYJTRK/article/details/122754438)
- [LiveData && ViewModel使用详解](https://juejin.cn/post/6844903814173212680#heading-7)
- [ViewModel 使用及原理解析](https://juejin.cn/post/6844903801044877325)
- [ListView 使用详解](https://juejin.cn/post/6844903911401193479#heading-14)
- [ViewModel 的基本用法](https://blog.csdn.net/u010356768/article/details/109671555)
- [从 ViewModel 执行网络操作](https://stackoverflow.com/questions/46583131/performing-network-operations-from-viewmodel)
- [如何在Fragment中获取context](https://blog.csdn.net/DeMonliuhui/article/details/51511136)
- [使用AndroidViewModel初始化报错](https://blog.51cto.com/u_15249199/2846234)

### Trend 详情页

- [Android Activity启动另一个Activity 并传递数据](https://blog.csdn.net/kidults/article/details/80777025)
- [Android Activity管理机制详解](https://blog.51cto.com/shanyou/3310370)

### 获取地理位置

- [Android GPS 动态获取地理位置](https://www.twle.cn/l/yufei/android/android-basic-gps-dynamic.html)
- [Mapbox](https://docs.mapbox.com/mapbox-gl-js/example/custom-marker-icons/)
- [根据经纬度查询详细地址API](https://apis.map.qq.com/ws/geocoder/v1?location=22.287502,114.149268&get_poi=0&key=OA4BZ-FX43U-E5VV2-45M6S-C4HD3-NIFFI&output=json)

### 请求权限

- [Android开发之 permission动态权限获取](https://blog.51cto.com/u_14397532/5058241)
- [Android Fragment 调用宿主Activity 里面的方法](https://blog.csdn.net/qq_33210042/article/details/108472447)

### 上传图片过程

首先客户端发起POST

```
POST comp4342.hjm.red:8044 HTTP/1.1 
Content-Type: application/json;charset=utf-8

{"user": "hanjiaming", 
 "identifier": "fnr4f34g7bjb4hr", 
 "message":"A rainy Day", 
 "location":"[28,32]",
 "picture": "rf3rfuy845nt7457tcnv4"
}
```

上传的 Json 为

```
{"user": "hanjiaming", 
 "identifier": "fnr4f34g7bjb4hr", 
 "message":"A rainy Day", 
 "location":"[28,32]",
 "picture": "rf3rfuy845nt7457tcnv4"
}
```

服务器回应为

```
{
    "statue":"Success",
    "commit":"No error",
    "code":"0",
}
```

如果code为2，表示用户登入已过期，需要让用户重新登入

- [POST 请求的三种常见数据提交格式](https://segmentfault.com/a/1190000019508338)
- [Android Volley初探：Volley基本用法](https://www.jianshu.com/p/5dd50bcbcd6d)
- [Android Volley使用（一）Volley的基本用法](https://blog.csdn.net/u010356768/article/details/87720280)
- [Android Studio ImageView 不显示图片的解决](https://blog.csdn.net/weixin_chen/article/details/105909185)
- [Android Studio 上传文件实例_拍照、相机上传图片](https://www.jianshu.com/p/32ecd705caa9)
- [安卓开发：以Base64字符串形式上传文件——从Android选择文件并上传](https://blog.csdn.net/freezingxu/article/details/77923023)
- [Android图片上传的两种方式](https://blog.csdn.net/haoyuegongzi/article/details/120903881)

### 登入过程

首先客户端发起POST

```
POST comp4342.hjm.red:8044 HTTP/1.1 
Content-Type: application/json;charset=utf-8

{"user": "hanjiaming", 
 "password": "123456"
}
```

服务器回应为

```
{
    "statue":"Fail",
    "commit":"No this User",
    "code":"-1",
}
```

### 注册过程

首先客户端发起POST

```
POST comp4342.hjm.red:8044 HTTP/1.1 
Content-Type: application/json;charset=utf-8

{"user": "hanjiaming", 
 "password": "123456",
}
```

服务器回应为

```
{
    "statue":"Fail",
    "commit":"username exist.",
    "code":"-1",
}
```

### 本地数据库 Sqlite

- [Android SQLite数据库的创建及两种方式实现增删改查](https://blog.csdn.net/ezconn/article/details/108655624)

### 地图

- [用安卓 WebView 做一个“套壳”应用,如何在APP内](https://cloud.tencent.com/developer/article/1674348)
- 

### 网络通信

需要魔改 Android Volley

- [Android Volley使用（一）Volley的基本用法](https://blog.csdn.net/u010356768/article/details/87720280)
- [Volley完全解析之进阶最佳实践与二次封装(二十七)](https://static.kancloud.cn/digest/fastdev4android/109666)
- [Android 之 Dialog 使用最全面 - 异步加载动画](https://juejin.cn/post/7012069081943638023)

![1669002648789.png](https://static-file.hjm.red/2022/11/21/012a0ede440ec.png)

### 用户中心

- [java如何获取当前日期和时间](https://blog.csdn.net/topdeveloperr/article/details/91571311)