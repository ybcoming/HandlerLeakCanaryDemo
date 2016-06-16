# HandlerLeakCanaryDemo
关于Handler引起内存泄露的Demo

## Handler造成的内存泄漏
* 平时在处理网络任务或者封装一些请求回调等api都应该会借助Handler来处理。 
* Handler如此常用，一个不小心，比较容易造成我们app的内存泄漏。

## 你将能学到什么
* 如何使用LeakCanary检测内存泄漏
* 平时使用的Handler是如何造成内存泄漏的
* Handler造成内存泄漏是如何优化的

## 关于Leakcanary的使用
* 1,引入依赖:在app的build.gradle里引入:
                                        
                                        debugCompile 'com.squareup.leakcanary:leakcanary-android:1.4-beta2'
                                        releaseCompile 'com.squareup.leakcanary:leakcanary-android-no-op:1.4-beta2'
                                        testCompile 'com.squareup.leakcanary:leakcanary-android-no-op:1.4-beta2'
* 2,
