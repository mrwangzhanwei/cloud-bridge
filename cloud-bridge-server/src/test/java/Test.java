import com.cloud_bridge.cluster.ConnectServers;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;
import java.util.List;
/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/25  22:44
 */
public class Test {
    public static void main(String[] args) {
        ConnectServers servers = new ConnectServers();
        servers.connect();
//        try {
////            Class<?> bootstrapClass = Class.forName("io.netty.bootstrap.ServerBootstrap");
////            ServerBootstrap bootstrap = (ServerBootstrap)bootstrapClass.newInstance();
//            ServerBootstrap bootstrap = new ServerBootstrap();
//            Class<? extends ServerBootstrap> bootstrapClass = bootstrap.getClass();
//            Method group = bootstrapClass.getMethod("group", EventLoopGroup.class, EventLoopGroup.class);
//            Class<?>[] types = group.getParameterTypes();
//            for (Class<?> type : types) {
//                System.out.println(type.getName());
//            }
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }


    }
}
