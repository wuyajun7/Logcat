# Logcat
打印项目中的日志，移动端可视化

## 版本更新：
1.1.0.2 添加LogcatView布局
1.1.0.1 添加Logcat悬浮布局

## 接入方式：

Step 1. Add the JitPack repository to your build file

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
    
Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.wuyajun7:Logcat:1.1.0.2'
	}

## 使用方式：

```
// 启动悬浮日志  
LogcatBus.getDefault().startLogcat();  
// 关闭悬浮日志  
LogcatBus.getDefault().overLogcat();  
// 悬浮日志布局添加日志数据  
LogcatBus.getDefault().post(log);  
    
// 日志布局列表触摸事件  
LogcatView.setItemTouchListener  
// 日志布局添加日志数据  
LogcatView.addLogToView(log);  
```
