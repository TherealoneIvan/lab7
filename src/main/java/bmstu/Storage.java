package bmstu;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.Scanner;

public class Storage {
    Scanner input = new Scanner(System.in);
    String arguments = input.nextLine();
    String [] parsedArg = arguments.split(" ");
    Integer start = Integer.parseInt(parsedArg[0]);
    Integer end = Integer.parseInt(parsedArg[1]);
    public static void main(String[] args){
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket backend =
                context.socket(SocketType.DEALER);
        ZMQ.Poller storage = context.poller (1);
        storage.register(backend , ZMQ.Poller.POLLIN);
        long startTime = System.currentTimeMillis();
        while (!Thread.currentThread().isInterrupted()) {
            if (System.currentTimeMillis() - startTime > 5000){
                ZMsg msg = new ZMsg();
                msg.add("NOTI")

                startTime = System.currentTimeMillis();
            }
        }
    }
}
