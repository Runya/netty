package MyHttpServer.requests.concrete;

import MyHttpServer.requests.MyRequest;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by byte on 05.11.14.
 *
 */
public class StartPage  extends MyRequest {
    @Override
    public FullHttpResponse messageReceived(ChannelHandlerContext ctx, HttpRequest request) {
//        status.addRequestCount();
        String host = request.headers().get("HOST");
        ByteBuf content =  Unpooled.copiedBuffer("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "<a href=\"http://" + host + "/hello\">hello</a>\n" +
                "<a href=\"http://" + host + "/redirect?url=http://"+host+"/status\">redirect</a>\n" +
                "<a href=\"http://" + host + "/status\">status</a>\n" +
                "</body>\n" +
                "</html>", CharsetUtil.UTF_8);
        FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, OK, content);
        return res;
    }
}
