![](http://image.yanrt.com/uploads/allimg/160383/4.gif)

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
* 2,创建一个extends Application的MyAppilcation;并在onCreate方法中初始化LeakCanary:书写LeakCanary.install(this);
* ps:记得在AndroidManifest文件中配置你的MyApplication

## 大概流程描述
1. 新建一个工程， 配置好leakCanary内存泄漏检测环境。
2. 布局好UI 。（界面布局仅一个button， activity_main.xml文件中加个Button即可，Button 的ID为send_btn）.
3. 在MainActity中添加如下代码
 

分析：
在一个界面中加载数据loadData，加载数据完成后，发送Message，然后在在Handler的handlerMessage（）中去处理我们的消息。
运行代码，我们做如下操作：
1. 点击按钮，在20秒内点击手机返回键，关闭MainActivity
2. 等待10秒左右
将会发现，LeakCanary提示内存泄漏：
分析泄漏的原因：
由于mhandler是Handler的匿名内部类的实例，所以它持有外部类Activity的引用，我们知道消息队列是在一个Looper线程中不断轮询处理消息，而消息队列中的Message持有mHandler实例的引用，mHandler又持有Activity的引用，那么当这个Activity退出时，消息队列中还有未处理的消息或者正在处理消息，将导致该Activity的内存资源无法及时回收，引发内存泄漏。

处理办法： 使用静态内部类，将上面的代码改一下：
 
修改代码后运行，重复上面相同的操作，LeakCanary没有报出内存泄漏。

还有一个问题，在我们平时使用handlerMessage处理消息时，很多时候需要去使用外部类MainActivity的函数，比如我们需要更新MainActivity的UI 。
但是现在我们的问题时，我们静态内部类没有MainActivity的引用，没办法直接去访问MainActivity的属性和函数啊，怎么办呢 ？

Handler使用方式升级版： 使用弱引用 -解决静态内部类访问外部类 
名词解释：弱引用-----可以被JAVA 虚拟机顺利垃圾回收的一种引用方式
代码流程如下：
1.  修改一下我们刚才例子中的UI ：在MainActity的布局文件 activity_main.xml中添加一个TextView ，并且在MainActity的oncreat找到并初始化它。
代码和效果示意如下：
    

2. 我们在handlerMessage中，给TextView设置值，请注意红色方框内的弱引用使用方式：
 

我们一起来分析上面的做法：
创建一个静态Handler内部类，然后对Handler持有的外部对象使用弱引用，这样在回收时也可以回收Handler持有的对象，解决了我们内存泄漏以及访问外部对象的问题。
但是，这样子还不够完美： 我们退出MainActivity后，Looper线程的消息队列中还是可能会有待处理的消息，啥意思呢？就是我们MainActivity退出后，消息队列里还有消息，即我们的例子中，20秒后，还收到消息队列中的消息。
更完美的做法：我们应该在Activity关闭的时候，移除消息队列中的消息。
 


## 这样你的Leakcanary就可以检测你的内存是否泄露了,具体测试泄露代码和修正代码,请参考本Demo;
## Demo代码中注释部分为会造成内存泄漏的代码,优化代码为正常代码.


