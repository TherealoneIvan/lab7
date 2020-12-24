package bmstu;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.Scanner;

public class Storage {
    Scanner input = new Scanner(System.in);
    String arguments = 
    public static void main(String[] args){
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket backend =
                context.socket(SocketType.DEALER);
        ZMQ.Poller storage = context.poller (1);
        storage.register(backend , ZMQ.Poller.POLLIN);
        while (!Thread.currentThread().isInterrupted()) {

        }
    }
}
