package utils;

import com.esotericsoftware.kryo.Kryo;

/**
 * Created by wangzhiguo on 2019/1/28 0028.
 */
public class ThreadLocalKryoFactory extends utils.KryoFactory {
    private final ThreadLocal<Kryo> holder  = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            return createKryo();
        }
    };
    public Kryo getKryo() {
        return holder.get();
    }
}
