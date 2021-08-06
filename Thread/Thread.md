## 三、Thread API的详细介绍

### 3.1 线程sleep

sleep 方法会使当前线程进入指定毫秒数的休眠，暂停执行，虽然给定了一个休眠的时间， 但是最终要以系统的定时器和调度器的精度为准， 休眠有一个非常重要的特性，那就是其不会放弃monitor 锁的所有权

### 3.2 线程yield

yield 方法属于一种启发式的方法，其会提醒调度器我愿意放弃当前的CPU 资源，如果CPU 的资源不紧张，则会忽略这种提醒；

调用yield 方法会使当前线程从RUNNING 状态切换到RUNNABLE 状态；（如果CPU 调度器没有忽略这个提示的话）

### 3.3 设置线程的优先级

如果CPU 比较忙， 设置优先级可能会获得更多的CPU 时间片，但是闲时优先级的高低几乎不会有任何作用；

线程的优先级不能小于l 也不能大于10；

如果指定的线程优先级大于线程所在group 的优先级，那么指定的优先级将会失效，取而代之的是group 的最大优先级；

### 3.6 设置线程上下文类加载器

`public ClassLoader getContextClassLoader() `获取线程上下文的类加载器，简单来说就是这个线程是由哪个类加器加载的，如果是在没有修改线程上下文类加载器的情况下， 则保持与父线程同样的类加载器；

`public void setContextClassLoader(ClassLoader c1) `设置该线程的类加载器，这个方法可以打破JAVA 类加载器的父委托机制，有时候该方法也被称为JAVA 类加载器的后门

### 3.7 线程interrupt

#### 3.7.1 interrupt

如下方法的调用会使得当前线程进入阻塞状态，而调用当前线程的interrupt 方法，就可以打断阻塞：

- Object 的wait 方法
- Thread 的sleep(long）方法
- Thread 的join 方法
- InterruptibleChannel 的io操作
- Selector 的wakeup 方法

一旦线程在阻塞的情况下被打断，都会抛出一个称为Interrupted Exception 的异常

#### 3.7.2 islnterrupted

判断当前线程是否被中断，该方法仅仅是对interrupt 标识的一个判断，并不会影响标识发生任何改变

```java
package com.learn.thread.basics.chapter_three;

import java.util.concurrent.TimeUnit;

public class ThreadisInterrupted {
    public static void main(String[] args) throws InterruptedException {
        Thread thread1 = new Thread() {
            @Override
            public void run () {
                while (true) {

                }
            }
        };

        thread1.setDaemon(true);
        thread1.start();
        TimeUnit.MILLISECONDS.sleep(2);
        System.out.printf("Thread is interrupted? %s \n", thread1.isInterrupted());
        thread1.interrupt();
        TimeUnit.MILLISECONDS.sleep(2);
        System.out.printf("Thread is interrupted? %s \n", thread1.isInterrupted());

        Thread thread2 = new Thread() {
            @Override
            public void run () {
                while (true) {
                    try {
                        TimeUnit.MINUTES.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };

        thread2.setDaemon(true);
        thread2.start();
        TimeUnit.MILLISECONDS.sleep(2);
        System.out.printf("Thread is interrupted? %s \n", thread2.isInterrupted());
        thread2.interrupt();
        TimeUnit.MILLISECONDS.sleep(2);
        System.out.printf("Thread is interrupted? %s \n", thread2.isInterrupted());
    }
}
```

thread2由于在run 方法中使用了sleap 这个可中断方法，它会捕获到中断信号，并且会擦除interrupt 标识，因此程序的执行结果都会是false;

可中断方法捕获到了中断信号之后，为了不影响线程中其他方法的执行，将线程的interrupt 标识复位是一种很合理的设计;

#### 3.7.3 interrupted

interrupted 是一个静态方法，虽然其也用于判断当前线程是否被中断，调用该方法会直接擦除掉线程的interrupt 标识，需要注意的是，如果当前线程被打断了，那么第一次调用interrupted 方法会返回true ， 并且立即擦除了interrupt 标识；第二次包括以后的调用永远都会返回false ，除非在此期间线程又一次地被打断；

如果一个线程在没有执行可中断方法之前就被打断，那么其接下来将执行可中断方法，可中断方法会立即中断；

### 3.8 线程join

join 某个线程A ， 会使当前线程B 进入等待， 直到线程A 结束生命周期，或者到达给定的时间，那么在此期间B 线程是处于BLOCKED 的，而不是A 线程

#### 3.8.2 join 万法结合实战

FightQuery：不管是Thread 的run 方法，还是Runnable 接口， 都是void 返回类型，如果你想通过某个线程的运行得到结果，就需要自己定义一个返回的接口

```java
package com.learn.thread.basics.chapter_three;

import java.util.List;

public interface FightQuery {
    List<String> get();
}
```

FightQueryTask：其实就是一个线程的子类，主要用于到各大航空公司获取数据

```java
package com.learn.thread.basics.chapter_three;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class FightQueryTask extends Thread implements FightQuery {

    private final String origin;
    private final String destination;
    private final List<String> flightList =new ArrayList<>();

    public FightQueryTask(String airline, String origin, String destination) {
        super("[" + airline + "]");
        this.origin = origin;
        this.destination = destination;
    }

    @Override
    public void run() {
        System.out.printf("%s-query from %s to %s \n", getName(), origin,
                destination);
        int randomVal = ThreadLocalRandom.current().nextInt(10);
        try {
            TimeUnit.SECONDS.sleep(randomVal);
            this.flightList.add(getName() + "-" + randomVal);
            System.out.printf ("The Fight: %s list query successful \n", getName());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> get() {
        return this.flightList;
    }
}
```



```java
package com.learn.thread.basics.chapter_three;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FightQueryExample {
    private static final List<String> FIGHT_COMPANY = Arrays.asList("CSA", "CEA", "HNA");

    public static void main(String[] args) {
        List<String> results = search("SH", "BJ");
        System.out.println("===============result================");
        results.forEach(System.out::println);
    }

    private static List<String> search(String original, String dest) {
        final List<String> result = new ArrayList<>();

        List<FightQueryTask> tasks = FIGHT_COMPANY.stream()
                .map(f -> createSearchTask(f, original, dest))
                .collect(Collectors.toList());

        tasks.forEach(Thread::start);

        tasks.forEach(t ->
        {
            try {
                t.join();
            } catch (InterruptedException e) {
            }
        });

        tasks.stream().map(FightQueryTask::get).forEach(result::addAll);

        return result;
    }

    private static FightQueryTask createSearchTask(String fight, String original, String dest) {
        return new FightQueryTask(fight, original, dest);
    }
}
```

### 3.9 如何关闭一个线程

1. 线程结束生命周期正常结束
2. 捕获中断信号关闭线程
3. 使用volatile 开关控制



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

多线程间通信需要用到Object 的notifyAll 方法，无法保证唤醒的是什么线程，可能导致`Linkedlist` 为空时执行`removeFirst`方法、`Linkedlist` 元素为10 时执行`addLast` 方法

### 5.4 自定义显式锁BooleanLock

`synchronized `关键字提供了一种排他式的数据同步机制，而这种阻塞有两个很明显的缺陷：

1.无法控制阻塞时长;

2.阻塞不可被中断;

构造一个显式的`BooleanLock` ，使其在具备`synchronized`关键字所有功能的同时·又具备可中断和lock 超时的功能

Lock接口：

```java
package com.learn.thread.basics.chapter_five;

import java.util.List;
import java.util.concurrent.TimeoutException;

public interface Lock {

    void lock() throws InterruptedException;

    void lock(long mills) throws InterruptedException, TimeoutException;

    void unlock();

    List<Thread> getBlockedThreads();
}
```

BooleanLock：

```java
package com.learn.thread.basics.chapter_five;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;

import static java.lang.System.currentTimeMillis;
import static java.lang.System.in;

public class BooleanLock implements Lock{

    private Thread currentThread;

    private boolean locked = false;

    private final List<Thread> blockedList = new ArrayList<>();

    @Override
    public void lock() throws InterruptedException {
        synchronized (this) {
            while (locked) {
                try {
                    if(!blockedList.contains(Thread.currentThread())) {
                        blockedList.add(Thread.currentThread());
                    }
                    this.wait();
                } catch (InterruptedException interruptedException) {
                    // 线程被中断时，移除相应线程，防止内存溢出
                    blockedList.remove(Thread.currentThread());
                    throw interruptedException;
                }

            }
            blockedList.remove(Thread.currentThread());
            this.locked = true;
            this.currentThread = Thread.currentThread();
        }
    }

    @Override
    public void lock(long mills) throws InterruptedException, TimeoutException {
        synchronized (this) {
            if (mills < 0) {
                this.lock();
            }
            else {
                long remainingMills = mills;
                long endMills = currentTimeMillis() + remainingMills;
                while (locked) {
                    if (remainingMills < 0) {
                        throw new TimeoutException ("can not get the lock during " + mills);
                    }
                    if (!blockedList.contains(Thread.currentThread())) {
                        blockedList.add(Thread.currentThread());
                    }
                    this.wait(remainingMills);
                    remainingMills = endMills - currentTimeMillis();
                }
                blockedList.remove(Thread.currentThread());
                this.locked = true;
                this.currentThread = Thread.currentThread();
            }
        }
    }

    @Override
    public void unlock() {
        synchronized (this) {
            if (currentThread == Thread.currentThread()) {
                this.locked = false;
                this.notifyAll();
            }
        }
    }

    @Override
    public List<Thread> getBlockedThreads() {
        return Collections.unmodifiableList(blockedList);
    }
}
```

