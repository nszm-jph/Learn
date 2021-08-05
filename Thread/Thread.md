## 四、线程安全与数据同步

### 4.3.3 使用synchronized 需要注意的问题

1. **与monitor 关联的对象不能为空**
2. **synchronized 作用域太大**
3. **不同的monitor 企图锁相同的方法**
4. **多个锁的交叉导致死锁**

### 4.4 This Monitor 和Class Monitor 的详细介绍

```java
public class ThisMonitor {

    public synchronized void method1() {
        System.out.printf(currentThread().getName() + " enter to method1");
        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void method2() {
        System.out.printf(currentThread().getName() + " enter to method2");
        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ThisMonitor thisMonitor = new ThisMonitor();
        new Thread(thisMonitor::method1, "T1").start();
        new Thread(thisMonitor::method2, "T2").start();
    }
}

```

**使用synchronize d 关键字同步类的不同实例方法，争抢的是同一个monitor 的lock ，而与之关联的引用则是ThisMonitor 的实例引用**

```java
package com.learn.thread.basics;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.currentThread;

public class ClassMonitor {

    public static synchronized void method1() {
        System.out.printf(currentThread().getName() + " enter to method1");
        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void method2() {
        System.out.printf(currentThread().getName() + " enter to method2");
        try {
            TimeUnit.MINUTES.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(ClassMonitor::method1, "T1").start();
        new Thread(ClassMonitor::method2, "T2").start();
    }
}
```

**用synchronized 同步某个类的不同静态方法争抢的也是同一个monitor 的lock，与该monitor 关联的引用是`ClassMonitord.class` 实例**

### 4.5 程序死锁的原因以及如何诊断

#### 4.5.1 程序死锁

1. 交叉锁可导致程序出现死锁
   线程A 持有R1的锁等待获取R2 的锁， 线程B持有R2 的锁等待获取R1的锁；

2. 内存不足
   当并发请求系统可用内存时，如果此时系统内存不足，则可能会出现死锁的情况；举个例子，两个线程T1和T2 ， 执行某个任务，其中T1已经获取了10MB 内存， T2 获取了20MB 内存， 如果每个线程的执行单元都需要30MB 的内存，但是剩余可用的内存刚好为20MB ，那么两个线程有可能都在等待彼此能够释放内存资源；

3. 一问一答式的数据交换
   服务端开启某个端口，等待客户端访问，客户端发送请求立即等待接收，由于某种原因服务端错过了客户端的请求，仍然在等待一问一答式的数据交换， 此时服务端和客户端都在等待着双方发送数据；

4. 数据库锁
   无论是数据库表级别的锁，还是行级别的锁，比如某个线程执行for update 语句退出了
   事务，其他线程访问该数据库时都将陷入死锁；

5. 文件锁
   某线程获得了文件锁意外退出，其他读取该文件的线程也将会进入死锁直到系统释放文件句柄资源；

6. 死循环引起的死锁

   程序由于代码原因或者对某些异常处理不得当，进入了死循环，虽然查看线程堆技信息不会发现任何死锁的迹象，但是程序不工作， CPU 占有率又居高不下，这种死锁一般称为系统假死，是一种最为致命也是最难排查的死锁现象，由于重现困难，进程对系统资源的使用量又达到了极限，想要做出dump 有时候，也是非常困难的；

## 五、线程间通信

### 5.2 单线程间通信

#### 5.2.2 wait 和notify 方法详解

```java
public class EventQueue {

    private final int max;

    static class Event {

    }

    private final LinkedList<Event> eventQueue = new LinkedList<>();

    private static final int DEFAULT_MAX_EVENT = 10;

    public EventQueue() {
        this(DEFAULT_MAX_EVENT);
    }

    public EventQueue(int max) {
        this.max = max;
    }

    public void offer(Event event) {
        synchronized (eventQueue) {
            // 提交任务，如果队列已满，则阻塞当前线程
            if (eventQueue.size() >= max) {
                try {
                    console(" this queue is full.");
                    eventQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            console(" the new event is submitted");
            eventQueue.addLast(event);
            eventQueue.notify();
        }
    }

    public Event take() {
        synchronized (eventQueue) {
            // 处理任务，如果队列已空，则阻塞当前线程
            if (eventQueue.isEmpty()) {
                try {
                    console(" the queue is empty");
                    eventQueue.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            Event event = eventQueue.removeFirst();
            this.eventQueue.notify();
            console(" the event " + event + "is handled.");
            return event;
        }
    }

    private void console(String message) {
        System.out.printf("%S:%s\n", Thread.currentThread().getName(), message);
    }
}
```

```java
public class EventClient {

    public static void main(String[] args) {
        final EventQueue eventQueue = new EventQueue();
        new Thread(() -> {
            for ( ; ; ) {
                eventQueue.offer(new EventQueue.Event());
            }
        }, "Producer").start();

        new Thread(() -> {
            for ( ; ; ) {
                eventQueue.take();
                try {
                    TimeUnit.MILLISECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Consumer").start();
    }
}
```

**wait 方法的作用：**

1. wait 方法的这三个重载方法都将调用wait(long timeout)这个方法， 前文使用的wait()方法等价于wait(0), 0 代表着永不超时；
2. Object 的wait(long timeout)方法会导致当前线程进入阻塞，直到有其他线程调用了Object 的notify 或者notifyAll 方法才能将其唤醒，或者阻塞时间到达了timeout时间而自动唤醒；
3. wait 方法必须拥有该对象的monitor ，也就是wait 方法必须在同步方法中使用；
4. 当前线程执行了该对象的wait 方法之后，将会放弃对该monitor 的所有权并且进入与该对象关联的wait set 中，也就是说一旦线程执行了某个object 的wait 方法之后，它就会释放对该对象monitor 的所有权，其他线程也会有机会继续争抢该monitor 的所有权；

**notify 方法的作用：**

​	public final native void notify();

1. 唤醒单个正在执行该对象wait 方法的线程；
2. 如果有某个线程由于执行该对象的wait 方法而进入阻塞则会被唤醒，如果没有则会忽略；
3. 被唤醒的线程需要重新获取对该对象所关联monitor的lock才能继续执行；

### 5.3 多线程间通信
