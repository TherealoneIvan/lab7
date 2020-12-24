package bmstu;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;


import java.net.Socket;

public class Client {
    public static void main(String[] args){
        ZMQ.Context context = ZMQ.context(1);
        Socket frontend =
                context.socket(SocketType.REQ);
    }
}
