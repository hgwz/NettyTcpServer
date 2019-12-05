package server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

/**
 * @author yuli
 * @Description:TODO
 * @ClassName TcpServer
 **/
public class TcpServer {

    // 服务器地址端口
    private static final String IP = "127.0.0.1";
    private static final int PORT = 9999;

    /** 用于分配处理业务线程的线程组个数 */
    protected static final int BIZGROUPSIZE = Runtime.getRuntime().availableProcessors() * 2;
    /** 业务出现线程大小 */
    protected static final int BIZTHREADSIZE = 4;

    /*
     * NioEventLoopGroup实际上就是个线程池,
     * NioEventLoopGroup在后台启动了n个NioEventLoop来处理Channel事件,
     * 每一个NioEventLoop负责处理m个Channel,
     * NioEventLoopGroup从NioEventLoop数组里挨个取出NioEventLoop来处理Channel
     */
    private static final EventLoopGroup bossGroup = new NioEventLoopGroup(BIZGROUPSIZE);
    private static final EventLoopGroup workerGroup = new NioEventLoopGroup(BIZTHREADSIZE);

    //    线程内容
    protected static void run() throws Exception {
        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<SocketChannel>() {

            @Override
            public void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
//                Decode是对发送的信息进行编码、
//                @param maxFrameLength  帧的最大长度
//                @param lengthFieldOffset length字段偏移的地址
//                @param lengthFieldLength length字段所占的字节
//                @param lengthAdjustment 修改帧数据长度字段中定义的值，
//                可以为负数 因为有时候我们习惯把头部记入长度,若为负数,则说明要推后多少个字段
//                @param initialBytesToStrip 解析时候跳过多少个长度
                pipeline.addLast(new StringEncoder());
                pipeline.addLast(new StringDecoder());
                pipeline.addLast(new TcpServerHandler());
            }
        });
//        异步绑定端口
        b.bind(IP, PORT).sync();
        System.out.println("TCP Server Started");
    }
//            关闭端口
    protected static void shutdown() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("Starting TCP Server...");
        TcpServer.run();
        // TcpServer.shutdown();
    }


}
