package requests.concrete;


import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import requests.MyRequest;
import status.Status;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created by byte on 05.11.14.
 *
 */
public class StatisticsPage extends MyRequest {
    @Override
    public FullHttpResponse messageReceived(ChannelHandlerContext ctx, HttpRequest request) {
//        status.addRequestCount();
        Status statistics = Status.getStatistics();
        StringBuilder buf = new StringBuilder();
        buf.append("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>");

        buf.append("The total number of requests: ").append(statistics.getCountRequest()).append("<br>");

        buf.append(statistics.getActivConnectionsTable());

        buf.append(statistics.getActivConnectionsTableLastSesion());

        buf.append(statistics.getRedirectUrlTable());

        buf.append(statistics.getConnectinInfo());

        buf.append("</body>\n" +
                "</html>");

        return new DefaultFullHttpResponse(
                HTTP_1_1, OK ,
                Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8));
    }
}
