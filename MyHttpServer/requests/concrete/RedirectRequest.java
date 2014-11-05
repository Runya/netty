package MyHttpServer.requests.concrete;

import MyHttpServer.requests.MyRequest;
import MyHttpServer.status.Status;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;

import java.util.List;
import java.util.Map;

/**
 * Created by byte on 05.11.14.
 *
 */
public class RedirectRequest extends MyRequest {
    @Override
    public FullHttpResponse messageReceived(ChannelHandlerContext ctx, HttpRequest request) {
//        status.addRequestCount();
        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(request.getUri());
        Map<String, List<String>> params = queryStringDecoder.parameters();
        List<String> vals = params.get("url");
        String url;
        if (vals.size()>0)
        url = vals.get(0); else url = "http:\\"+request.headers().get("HOST");
        FullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.FOUND);
        response.headers().set(HttpHeaders.Names.LOCATION, url);
        Status.getStatistics().addUrlrediurect(url);
        return response;
    }
}
