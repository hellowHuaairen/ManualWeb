package handle;

import bean.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import utils.KryoSerializer;

/**
 * Created by wangzhiguo on 2019/1/28 0028.
 */
public class KryoEncoder extends MessageToByteEncoder<Message> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception {
        KryoSerializer.serialize(message, out);
        ctx.flush();
    }
}
