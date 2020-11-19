# 一对一视频通话

本文介绍如何使用StrangerChat视频通话SDK快速实现视频通话。 商务合作与技术交流请加QQ群：1160896626

## 1、示例项目

StrangerChat在GitHub上提供开源的实时视频通话示例项目[StrangerChatAndroid](https://github.com/linkvxiaohong/StrangerChatAndroid)。在实现相关功能前，你可以下载并查看源码。

![](https://github.com/linkv-io/StrangerChat/blob/master/Snapshot/StrangerChat.gif?raw=true)




### Demo下载体验

[点我安装Demo或扫描下方二维码下载](https://www.pgyer.com/zDgs)

![](https://www.pgyer.com/app/qrcode/zDgs)

### 前提条件

*  Android 5.1或以上版本的设备
* 有效的`AppID`和`AppSign`

## <a name='2'></a>2、集成SDK

* 在工程的build.gradle文件添加如下内容：

  ```xml
  maven {
  		url 'http://maven.linkv.fun/repository/liveme-android/'
  		credentials {
  				username = 'LivemesdkPublicUser'
  				password = 'public'
  		}
  }
  ```


![img](https://raw.githubusercontent.com/linkvxiaohong/StrangerChatAndroid/outer/images/image-maven-config.png)

* 在app的build.gradle文件添加社交SDK依赖：

  

## 3、设置工程配置

### 添加权限

复制以下代码，粘贴到安卓清单文件里面。

```java
// 需要使用麦克风权限，否则无法发布音频直播，无法与主持人音频连麦.
<uses-permission android:name="android.permission.RECORD_AUDIO" />

// 需要使用摄像头权限，否则无法发布视频直播，无法与主持人视频连麦.
<uses-permission android:name="android.permission.CAMERA" />
```

### 允许使用http协议

> 由于目前 SDK 还需要使用 http 域名，所以请加入以下配置。

在res目录下创建xml目录，xml目录下创建network_security_config.xml文件。复制以下代码，粘贴到 network_security_config.xml 里面。

```xml
<?xml version="1.0" encoding="utf-8"?>
<network-security-config>
    <base-config cleartextTrafficPermitted="true" />
</network-security-config>
```

![img](https://raw.githubusercontent.com/linkvxiaohong/StrangerChatAndroid/outer/images/image-http-xml.png)



然后在安卓清单文件的Application节点中添加android:networkSecurityConfig="@xml/network_security_config"。