package bmstu;

import javax.naming.Context;
import java.net.Socket;

public class Client {
    public static void main(String[] args){
        Context context = ZMQ.context(1);
        Socket frontend =
                context.socket(SocketType.ROUTER);
    }
}
