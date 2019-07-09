https://www.jianshu.com/p/0d396b0d779a

什么是epoll机制呢?

nativePollOnce()方法

https://www.jianshu.com/p/7bc2b86c4d89

管道和epoll机制实现线程状态的管理，
配合起来实现了Android主线程的消息队列模型，
而这只是Android的一部分。
epoll机制通过Looper.cpp的addfd实现了对其他描述符的监听，
在4.1版本之后应用程序会等待VSYNC信号发起View的绘制操作，
就是通过它实现的，通过分析SurfaceFlinger和Choreographer，
可以很好的理解Android的绘制流程。
