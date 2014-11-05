package requests;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import status.Status;


/**
 * Created by byte on 04.11.14.
 *
 */
public abstract class MyRequest {
    protected Status status = Status.getStatistics();

    public abstract FullHttpResponse messageReceived(ChannelHandlerContext ctx, HttpRequest request);


}
