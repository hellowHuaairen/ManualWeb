package server;

import bean.ControllerContext;
import bean.ViewBean;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.EmptyByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import utils.*;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by wangzhiguo on 2019/1/28 0028.
 */
public class NettyHttpServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 加载xml文件的action的配置
     */
    private LoadWebConfig loadWebConfig;
    /**
     * 加载主机的action配置
     */
    private LoadAnnotationWebConfig loadAnnotationWebConfig;
    public NettyHttpServerHandler(LoadWebConfig loadWebConfig, LoadAnnotationWebConfig loadAnnotationWebConfig){
        this.loadWebConfig = loadWebConfig;
        this.loadAnnotationWebConfig = loadAnnotationWebConfig;
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

        ControllerContext context = new ControllerContext();

        if (msg instanceof HttpRequest) {
            DefaultHttpRequest request = (DefaultHttpRequest) msg;
            String returnView = "";
            //1. 组装上下文 context
            if(StringUtils.isNotEmpty(request.getUri())){
                Map<String, Object> urlParams = HtmlUtils.getUrlParams(request.getUri());
                context.setParams(urlParams);
                //2. 获取要被执行的方法
                String executeMethod = HtmlUtils.getControllerMethod(request.getUri());

                if(StringUtils.isNotEmpty(executeMethod)){
                    //3. 通过反射机制执行action的方法,返回结果视图索引
                    returnView = returnView(executeMethod, context);
                }
            }
            //4.返回结果视图
            String returnViewStr = ReadWebTemplate.readTemplate(returnView);
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(returnViewStr.getBytes("utf-8")));
//        response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/plain;charset=UTF-8");
            //text/html类型输入网页形式
            response.headers().set(HttpHeaders.Names.CONTENT_TYPE, "text/html;charset=UTF-8");
            response.headers().set(HttpHeaders.Names.CONTENT_LENGTH, response.content().readableBytes());
            response.headers().set(HttpHeaders.Names.CONNECTION, HttpHeaders.Values.KEEP_ALIVE);
            ctx.write(response);
            ctx.flush();
        }
        if (msg instanceof HttpContent) {
            LastHttpContent httpContent = (LastHttpContent) msg;
            ByteBuf byteData = httpContent.content();
            if (byteData instanceof EmptyByteBuf) {
                System.out.println("Content：无数据");
            } else {
                String content = new String(ByteUtils.objectToByte(byteData));
                System.out.println("Content:" + content);
            }
        }


    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
        cause.printStackTrace();
    }

    /**
     * 获取返回视图
     * @param executeMethod
     * @param context
     * @return
     */
    private String returnView(String executeMethod, ControllerContext context) throws Exception {

        String returnView = "";
        //通过配置文件加载的action的配置
        List<ViewBean> resultViews = loadWebConfig.getResultViews();
        //通过注解形式加载的action的配置
        List<ViewBean> resultAnnotationViews = loadAnnotationWebConfig.getResultViews();
        resultViews.addAll(resultAnnotationViews);
        if(CollectionUtils.isNotEmpty(resultViews) && StringUtils.isNotEmpty(executeMethod)){
            for(ViewBean view: resultViews){
                //action 的name属性相同时
                if(executeMethod.equals(view.getActionName())){
                    Class clazz = Class.forName(view.getControllerClass());
                    Method[] methods = clazz.getDeclaredMethods();
                    Object controller = clazz.newInstance();
                    for(Method method: methods){
                        if(method.getName().equals(view.getControllerClassMethod())){
                            returnView = (String)method.invoke(controller,context);
                        }
                    }
                }
            }
        }else {
            throw new Exception("executeMethod or context is null!");
        }

        return returnView;
    }
}
