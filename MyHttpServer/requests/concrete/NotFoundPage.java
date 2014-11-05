package MyHttpServer.requests.concrete;

import MyHttpServer.requests.MyRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;

import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by byte on 05.11.14.
 *
 */
public class NotFoundPage extends MyRequest {
    @Override
    public FullHttpResponse messageReceived(ChannelHandlerContext ctx, HttpRequest req) {
//        status.addRequestCount();
        FullHttpResponse res = new DefaultFullHttpResponse(HTTP_1_1, NOT_FOUND);
        return res;
    }
}
