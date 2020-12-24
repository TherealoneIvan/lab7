package bmstu;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class Client {
    public static void main(String[] args){
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket requester =
                context.socket(SocketType.REQ);
        requester.connect("tcp://localhost:5559");
        System.out.println("launch and connect client.");
        Scaner input = new Scaner 
    }
}
