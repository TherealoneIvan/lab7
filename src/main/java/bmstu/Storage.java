package bmstu;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class Storage {
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
