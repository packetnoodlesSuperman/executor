一、Android单线程模型的；理解
    1. 当一个Android程序启动时，Android系统会同时启动一个对应的主线程(Main Thread)
    2. 这个主线程(Main Thread)主要的任务就是对UI相关的事件进行处理(例如显示文本，处理点击事件，显示图片等)
    3. 系统对每一个组件的调用都是从主线程中分发出去的，所以又常被称为UI线程
    4. 只能在UI线程(Main Thread)中对UI进行处理
    5. UI操作并不是线程安全的
    6. Android对UI处理的相关method都不是synchronized，所以当你试图用其他线程来对UI进行操作时，会抛出异常
    7. 通常Android基类中以on开头的方法是在Main线程被回调的
    
二、Android多线程的实现
    1. Handler
    2. AsyncTask （没用过）
    3. ThreadPoolExecutor
    4. IntentService
    5. RxJava