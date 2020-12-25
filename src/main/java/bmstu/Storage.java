package bmstu;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.Scanner;

public class Storage {
    public static final String SPACE_REGEX = " ";
    static Scanner input = new Scanner(System.in);
    static String arguments = input.nextLine();
    static String [] parsedArg = arguments.split(" ");
    static Integer start = Integer.parseInt(parsedArg[0]);
    static Integer end = Integer.parseInt(parsedArg[1]);
    public static void main(String[] args){
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket storageSocket =
                context.socket(SocketType.DEALER);
        storageSocket.connect("tcp://localhost:5560");
        ZMQ.Poller storage = context.poller (1);
        storage.register(storageSocket , ZMQ.Poller.POLLIN);
        long startTime = System.currentTimeMillis();
        while (!Thread.currentThread().isInterrupted()) {
            if (System.currentTimeMillis() - startTime > 5000){
                ZMsg msg = new ZMsg();
                msg.addLast("NOTIFY");
                msg.addLast(String.valueOf(start));
                msg.addLast(String.valueOf(end));
                msg.addLast(parsedArg.toString());
                msg.send(storageSocket);
                startTime = System.currentTimeMillis();
            }
            if (storage.pollin(0)){
                ZMsg messageFromProxy = ZMsg.recvMsg(storageSocket);
                messageFromProxy.unwrap();
                String[] parsedMessage = messageFromProxy.toString().split(SPACE_REGEX);
                if (parsedMessage[0].equals())
            }
        }
    }
}
