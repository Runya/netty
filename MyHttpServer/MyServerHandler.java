package MyHttpServer;

import MyHttpServer.requests.MyRequest;
import MyHttpServer.requests.concrete.*;
import MyHttpServer.status.Connection;
import MyHttpServer.status.Status;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;

import java.time.LocalDateTime;
import java.util.HashMap;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpResponseStatus.*;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import static MyHttpServer.Handler.*;

/**
 * Created by byte on 04.11.14.
 *
 */
public class MyServerHandler extends SimpleChannelInboundHandler<Object> {


    private double star_time;
    private LocalDateTime request_time;
    private String ip;
    private int receivedBytes;
    private int sentBytes;
    private String uri;


    private HashMap<String, MyRequest> requestMap = new HashMap<String, MyRequest>(){
        {
            put(startPage, new StartPage());
            put(notFound, new NotFoundPage());
            put(helloPage, new HelloPage());
            put(redirectPage, new RedirectRequest());
            put(statisticsPage, new StatisticsPage());
        }

        @Override
        public MyRequest get(Object key) {
            MyRequest request = super.get(key);
            if (request != null)
            return request;
            return super.get(notFound);
        }
    };



    public MyServerHandler(String hostString) {
        ip = hostString;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        if (uri != null &&uri.equals("favicon.ico")) return;
        star_time = System.currentTimeMillis();
        request_time  = LocalDateTime.now();
        Status.getStatistics().addRequestCount();
        Status.getStatistics().addActiveConection(ip);
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        if (uri != null &&uri.equals("/favicon.ico")) return;
        double work_time = System.currentTimeMillis() - star_time;
        work_time/=1000.0;
        double speed  = receivedBytes / work_time;
        Connection conection = new Connection(ip, uri, request_time, sentBytes, receivedBytes, speed);
        Status.getStatistics().add(conection);
        ctx.flush();
    }

    private FullHttpResponse messageReceived(String key, ChannelHandlerContext ctx, HttpRequest req){
        return requestMap.get(key).messageReceived(ctx, req);
    }

    static int count = 0;
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof HttpRequest){
            receivedBytes = msg.toString().length();
            HttpRequest req =  (HttpRequest) msg;

            // Handle a bad requests.
            if (!req.getDecoderResult().isSuccess()) {
                sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, BAD_REQUEST));
                return;
            }

            // Allow only GET methods.
            if (req.getMethod() != GET) {
                sendHttpResponse(ctx, req, new DefaultFullHttpResponse(HTTP_1_1, FORBIDDEN));
                return;
            }

            uri = req.getUri();


            if ("/".equals(req.getUri())){
                sendHttpResponse(ctx, req, messageReceived(startPage, ctx, req));
                return;
            }

            if ("/favicon.ico".equals(uri)){
                return;
            }

            if ("/hello".equals(req.getUri())){
                sendHttpResponse(ctx, req, messageReceived(helloPage, ctx, req));
                return;
            }

            if ((req.getUri()).startsWith("/redirect")){
                sendHttpResponse(ctx, req, messageReceived(redirectPage, ctx, req));
                return;
            }

            if ("/status".equals(req.getUri())){
                sendHttpResponse(ctx, req, messageReceived(statisticsPage, ctx, req));
                return;
            }

            sendHttpResponse(ctx, req, messageReceived(notFound, ctx, req));

        }
    }


    private  void sendHttpResponse(

            ChannelHandlerContext ctx, HttpRequest req, FullHttpResponse res) {

        sentBytes = res.content().writerIndex();

        // Generate an error page if response getStatus code is not OK (200).
        if (res.getStatus().code() != 200) {
            ByteBuf buf = Unpooled.copiedBuffer(res.getStatus().toString(), CharsetUtil.UTF_8);
            res.content().writeBytes(buf);
            buf.release();
            HttpHeaders.setContentLength(res, res.content().readableBytes());
        }

        // Send the response and close the connection if necessary.
        ChannelFuture f = ctx.channel().writeAndFlush(res);
        if (HttpHeaders.isKeepAlive(req) || res.getStatus().code() != 200) {
            f.addListener(ChannelFutureListener.CLOSE);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
