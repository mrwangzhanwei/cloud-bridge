import com.cloud_bridge.api.PubAndSubClient;
import com.cloud_bridge.client.NettyPubAndSubClient;
import com.cloud_bridge.model.Message;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 王战伟
 * @email wangzhanwei@lumlord.com
 * @date 2020/3/25  22:44
 */
@Slf4j
public class Main {
    public static void main(String[] args) {
        NettyPubAndSubClient client = NettyPubAndSubClient.getInstance().connect("127.0.0.1","9999");
        client.auth("wzw", "666", new PubAndSubClient.AutuListener() {
            @Override
            public void authOk(Message message) {
                log.info("成功"+message.toString());
            }

            @Override
            public void authFail(Message message) {
                log.info("失败"+message.toString());
            }
        });



    }
}
