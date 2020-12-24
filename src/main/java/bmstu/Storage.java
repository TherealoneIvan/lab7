package bmstu;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.Scanner;

public class Storage {
    static Scanner input = new Scanner(System.in);
    static String arguments = input.nextLine();
    static String [] parsedArg = arguments.split(" ");
    static Integer start = Integer.parseInt(parsedArg[0]);
    static Integer end = Integer.parseInt(parsedArg[1]);
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
                msg.add("NOTIFY");
                msg.add(String.valueOf(start));
                msg.add(" ");
                msg.add(String.valueOf(end));
                msg.add(parsedArg.toString());
                backend.send(msg.toString());
                startTime = System.currentTimeMillis();
            }
            if (storage.pollin(0)){
                ZMsg messageFromProxy = ZMsg
            }
        }
    }
}
