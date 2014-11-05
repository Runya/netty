package requests.concrete;


import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import requests.MyRequest;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by byte on 05.11.14.
 *
 */
public class HelloPage extends MyRequest {

    private int wait_time = 10*1000;

    @Override
    public FullHttpResponse messageReceived(ChannelHandlerContext ctx, HttpRequest request) {

//        status.addRequestCount();
        ByteBuf content = Unpooled.copiedBuffer("Hello World!!!", CharsetUtil.UTF_8);
        FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK, content);
        try {
            Thread.sleep(wait_time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return res;

    }
}
