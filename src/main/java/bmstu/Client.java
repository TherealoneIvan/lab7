package bmstu;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.Scanner;

public class Client {

    public static final String TCP_LOCALHOST_5559 = "tcp://localhost:5559";

    public static void main(String[] args){
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket requester =
                context.socket(SocketType.REQ);
        requester.connect(TCP_LOCALHOST_5559);
        System.out.println("launch and connect client.");
        Scanner input = new Scanner(System.in);
        while (true){
            String msg = input.nextLine();
            ZMsg req = new ZMsg();
            req.add(msg);
            req.send(requester);
            ZMsg answ = ZMsg.recvMsg(requester);
            System.out.println(answ);
        }
    }
}
