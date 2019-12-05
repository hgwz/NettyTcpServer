package server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author yuli
 * @ClassName TcpServerHandler
 **/
public class TcpServerHandler extends SimpleChannelInboundHandler<Object> {

    /**
        * 打印接收到的内容，并回传
        * @param [ctx, msg]
        * @return void
        */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg.equals("01")){
            System.out.println("receive command ：" + msg);
            ctx.channel().writeAndFlush("command 01 executed!\n");
        }else if(msg.equals("02")){
            System.out.println("receive command ：" + msg);
            ctx.channel().writeAndFlush("command 02 executed!\n");
        }else {
            System.out.println("unknown command：" + msg);
            ctx.channel().writeAndFlush("unknown command!\n");
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("exceptionCaught! cause:" + cause.toString());
        ctx.close();
    }
}
