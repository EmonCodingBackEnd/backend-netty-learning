# backend-netty-learning

[TOC]

Netty学习：

Netty：

https://www.bilibili.com/video/BV1DJ411m7NR/?p=24&spm_id_from=pageDriver&vd_source=b850b3a29a70c8eb888ce7dff776a5d1

数据结构与算法：

https://www.bilibili.com/video/BV1E4411H73v/?spm_id_from=333.337.search-card.all.click&vd_source=b850b3a29a70c8eb888ce7dff776a5d1

JVM：

https://www.bilibili.com/video/BV1PJ411n7xZ/?spm_id_from=333.788.recommend_more_video.2&vd_source=b850b3a29a70c8eb888ce7dff776a5d1

MySQL：https://www.bilibili.com/video/BV1iq4y1u7vj/?vd_source=b850b3a29a70c8eb888ce7dff776a5d1

Spring：

https://www.bilibili.com/video/BV1kR4y1b7Qc/?vd_source=b850b3a29a70c8eb888ce7dff776a5d1

SpringBoot2：

https://www.bilibili.com/video/BV19K4y1L7MT/

SpringCloud:

https://www.bilibili.com/video/BV18E411x7eT/?vd_source=b850b3a29a70c8eb888ce7dff776a5d1

前端三件套：

https://www.bilibili.com/read/cv5650633

https://www.bilibili.com/video/BV1p84y1P7Z5/?p=4&spm_id_from=pageDriver&vd_source=b850b3a29a70c8eb888ce7dff776a5d1

# 一、开始

## Netty的介绍

1）Netty是由JBOSS提供的一个Java开源框架，现为Github上的独立项目。

2）Netty是一个异步的、基于事件驱动的网络应用框架，用以快速开发高性能、高可用性的网络IO程序。

3）Netty主要针对在TCP协议下，面向Clients端的高并发应用，或者Peer-to-Peer场景下的大量数据持续传输的应用。

4）Netty本质上是一个NIO框架，适用于服务器通讯相关的多种应用场景。

## I/O模型及适用场景

1）BIO（`同步阻塞`）方式适用于**连接数目比较小且固定**的架构，这种方式对服务器资源要求比较高，并发局限于应用中，JDK1.4以前的唯一选择，但程序简单易于理解。

2）NIO（`同步非阻塞`）方式适用于**连接数目多且连接比较短（轻操作）**的架构，比如聊天服务器，弹幕系统，服务器间通讯等。编程比较复杂，JDK1.4开始支持。【Netty】

3）AIO（`异步非阻塞`）方式适用于**连接数目多且连接比较长（重操作）**的架构，比如相册服务器，充分调用OS参与并发操作，编程比较复杂，JDK7开始支持。

## Java NIO基本介绍

1）Java NIO全称java non-blocking IO，是指JDK提供的新特性。从JDK1.4开始，Java提供了一系列的输入/输出的新特性，被称为NIO（即New IO），是**同步非阻塞**的。

2）NIO相关的类都被放在`java.nio`包及其子包下，并且对原`java.io`包中的很多类进行改写。

3）NIO有三大核心部分：

- Channel（通道）
- Buffer（缓冲区）
- Selector（选择器）

4）NIO是**面向缓冲区，或者面向块**编程的。数据读取到一个它稍后处理的缓冲区，需要时可以在缓冲区中前后移动，这就增加了处理过程中的灵活性，使用它可以提供非阻塞式的高伸缩性网络。

5）Java NIO的非阻塞模式，使一个线程从某通道发出请求或者读取数据，但是它仅能得到目前可用的数据，如果目前没有数据可用时，就什么都不会获取，而**不是保持线程阻塞**，所以直至数据变得可以读取之前，该线程可以继续做其他的事情。非阻塞写也是如此，一个线程请求写入一些数据到某通道，但不需要等待它完全写入，这个线程同时可以去做别的事情。

6）通俗理解：NIO是可以做到用一个线程来处理多个操作的。假设有10000个请求过来，根据实际情况，可以分配50或者100个线程来处理。不像之前的阻塞IO那样，非得分配10000个线程。

7）HTTP2.0使用了多路复用的技术，做到同一个连接并发处理多个请求，而且并发请求的数量比HTTP1.1大了好几个数量级。

## NIO和BIO的比较

1）BIO以流的方式处理数据，而NIO以快的方式处理数据，块I/O的效率比流I/O高很多。

2）BIO是阻塞的，NIO则是非阻塞的。

3）BIO基于字节流和字符流进行操作，而NIO基于Channel（通道）和Buffer（缓冲区）进行操作，数据总是从通道读取到缓冲区中，或者从缓冲区写入到通道中。Select（选择器）用于监听多个通道的事件（比如：连接请求，数据到达等等），因此使用单个线程就可以监听多个客户端通道。

## NIO三大核心原理示意图

![0008](images/0008-1679730999923-2.png)

1）每一个Channel都会对应一个Buffer；

2）每一个Selector对应一个线程；一个线程对应多个Channel；

3）该图反应了有三个Channel注册到该Selector

4）程序切换到哪一个Channel是由事件决定的，Event就是一个重要的概念

5）Select会根据不同的事件，在各个通道上切换

6）Buffer就是一个内存块，底层是有一个数组

7）数据的读取和写入是通过Buffer，这个和BIO不同，BIO中要么是输入流，或者是输出流，不能双向，但是NIO的Buffer是可以读取和写入的，需要flip操作切换模式

8）Channel是双向的，可以返回底层操作系统的情况，比如Linux，底层的操作系统通道就是双向的。

## 缓冲区

Buffer类定义了所有的缓冲区都具有的四个属性来提供关于其所包含的数据元素的信息：

不变性：mark<=position<=limit<=capacity

| 属性     | 描述                                                         |
| -------- | ------------------------------------------------------------ |
| mark     | 标记                                                         |
| position | 位置，下一个要被读或写的元素的索引，每次读写缓冲区数据时都会改变其值，为下次读写作准备。 |
| limit    | 表示缓冲区的当前终点，不能对缓冲区超过极限的位置进行读写操作。且极限是可以修改的。 |
| capacity | 容量，即可以容纳的最大数据量；在缓冲区创建时被设定并且不能改变。 |

### ByteBuffer API

- 缓冲区创建相关API

  - 创建直接缓冲区

  ```java
  public static ByteBuffer allocateDirect(int capacity)
  ```

  - 设置缓冲区的初始容量

  ```java
  public static ByteBuffer allocate(int capacity)
  ```

  - 把一个数组放到缓冲区中使用

  ```java
  public static ByteBuffer wrap(byte[] array)
  ```

  - 构造初始化位置offset和上界length的缓冲区

  ```java
  public static ByteBuffer wrap(byte[] array, int offset, int length)
  ```

- 缓冲区存取相关API

  - 从当前位置position上get，get之后，position会自动+1

  ```java
  public abstract byte get();
  ```

  - 从绝对位置get

  ```java
  // 索引从0开始
  public abstract byte get(int index);
  ```

  - 从当前位置上面put，put之后，potition会自动+1

  ```java
  public abstract ByteBuffer put(byte b);
  ```

  - 从绝对位置上put

  ```java
  public abstract ByteBuffer put(int index, byte b);
  ```


### Buffer API

```java
// JDK1.4引入的API
// 返回此缓冲区的容量
public final int capacity();
// 返回此缓冲区的位置
public final int position();
// 设置此缓冲区的位置
public final Buffer position(int newPosition);
// 返回此缓冲区的限制
public final int limit()
// 设置此缓冲区的限制
public final Buffer limit(int newLimit)
// 在此缓冲区的位置设置标记
public final Buffer mark()
// 将此缓冲区的位置重置为以前标记的位置
public final Buffer reset()
// 清除此缓冲区，即将各个标记恢复到初始状态，但是数据并没有真正擦除
public final Buffer clear()
// 翻转此缓冲区（读写切换）
public final Buffer flip()
// 重绕此缓冲区：方法将position设置为0，但是不会动buffer里的数据，这样可以从头开始重新读取数据，limit的值不会变，这意味着limit依旧标志着能读多少数据。
public final Buffer rewind();
// 返回当前位置与限制之间的元素数
public final int remaining()
// 告知在当前位置和限制之间是否有元素
public final boolean hasRemaining()
// 告知此缓冲区是否为只读缓冲区
public abstract boolean isReadOnly();

// JDK1.6时引入的API
// 告知此缓冲区是否具有可访问的底层实现数组
public abstract boolean hasArray();
// 返回此缓冲区的底层实现数组
public abstract Object array();
// 返回此缓冲区的底层实现数组中第一个缓冲区元素的偏移量
public abstract int arrayOffset();
// 告知此缓冲区是否为直接缓冲区
public abstract boolean isDirect();
```

## 通道

1）NIO的通道类似于流，但有些区别如下：

- 通道可以同时进行读写，而流只能读或者只能写。
- 通道可以实现异步读写数据。
- 通道可以从缓冲读数据，也可以写入数据到缓存。

2）BIO中的stream是单向的，例如FileInputStream对象只能进行读取数据的操作，而NIO中的通道（Channel）是双向的，可以读操作，也可以写操作。

3）Channel在NIO中是一个接口 `public interface Channel extends Closeable`

4）常用的Channel类有：

- FileChannel：用于文件的数据读写
- DatagramChannel：用于UDP的数据读写
- ServerSocketChannel：用于TCP的数据读写
- SocketChannel：用于TCP的数据读写

### FileChannel类

主要用来对本地文件进行IO操作，常见的方法有：

```java
// 从通道读取数据并放到缓冲区中
public abstract int read(ByteBuffer dst)
// 把缓冲区中的数据写到通道中
public abstract int write(ByteBuffer src)
// 从目标通道中复制数据到当前通道
public abstract long transferFrom(ReadableByteChannel src, long position, long count)
// 把数据从当前通道复制给目标通道
public abstract long transferTo(long position, long count, WritableByteChannel target)
```

### 关于Buffer和Channel的注意事项和细节

1）ByteBuffer支持类型化的put和get，put放入的是什么数据类型，get就应该使用相应的数据类型来取出，负责可能有BufferUnderflowException异常。

2）可以将一个普通Buffer转成只读Buffer。

3）NIO还提供了MappedByteBuffer，可以让文件直接在内存（堆外内存）中进行修改，而如何同步到文件由NIO来完成。

4）NIO还支持通过多个Buffer（即Buffer数组）完成读写操作，即Scattering（[ˈskætərɪŋ]分散）和Gethering（[[ˈgetərɪŋ]]聚合）。

## 选择器

1）Java的NIO，用非阻塞的IO方式。可以用一个线程，处理多个客户端连接，就会使用到Selector（选择器）。

2）Selector能够检测多个注册的的通道上是否有事件发生（注意：多个Channel以事件的方式可以注册到同一个Selector），如果有事件发生，便获取事件然后针对每个事件进行相应的处理。这样就可以只用一个线程去管理多个通道，也就是管理多个连接和请求。

3）只有在连接真正有读写事件发生时，才会进行读写，就大大减少了系统开学，并且不必为每个连接都创建一个线程，不用去维护多个线程。

4）避免了多线程之间的上下文切换导致的开销。

### 特点再说明：

1）Netty的IO线程NioEventLoop聚合了Selector（选择器，也叫多路复用器），可以同时并发处理成百上千个客户端连接。

2）当线程从某客户端Socket通道进行读写数据时，若没有数据可用，该线程可以进行其他任务。

3）线程通常将非阻塞I/O的空闲时间用于在其他通道上执行I/O操作，所以单独的线程可以管理多个输入和输出通道。

4）由于读写操作都是非阻塞的，这就可以充分提升I/O线程的运行效率，避免由于频繁I/O阻塞导致的线程挂起。

5）一个IO线程可以并发处理N个客户端连接和读写操作，这从根本上解决了传统同步阻塞I/O一连接一线程模型，架构的性能、弹性伸缩能力和可靠性都得到了极大的提升。

### Selector API

```java
// 得到一个选择器对象
public static Selector open()
// 监控所有注册的通道，当其中有I/O操作可以进行时，将对应的SelectionKey加入到内部结合中并返回，参数用来设置超时时间
public abstract int selectNow(); // 不阻塞，立马返还
public abstract int select() // 阻塞
public abstract int select(long timeout)  // 阻塞 timeout 毫秒， 在 timeout 毫秒后返回
public abstract Selector wakeup(); // 唤醒Selector
// 从内部集合中得到所有的SelectionKey
public abstract Set<SelectionKey> selectedKeys();
```

### 注意事项

1）NIO中的ServerSocketChannel功能类似ServerSocket，SocketChannel功能类似Socket

### NIO非阻塞网络编程原理分析图

NIO非阻塞网络编程相关的（Selector、SelectionKey、ServerSocketChannel和SocketChannel）关系梳理图。

![image-20230328085629703](images/image-20230328085629703.png)

1）当客户端连接时，会通过ServerSocketChannel得到对应的SocketChannel

2）将SocketChannel注册到Selector上，

```java
public final SelectionKey register(Selector sel, int ops)
```

一个 Selector 上可以注册多个 SocketChannel。

3）注册后返回一个 SelectionKey，会和该 Selector 关联（集合）

4）Selector 进行监听 select 方法，返回有事件发生的通道的个数。

5）进一步得到各个 SelectionKey

6）再通过 SelectionKey 反向获取 SocketChannel，方法 channel()

7）可以通过得到的 channel，完成业务处理。

