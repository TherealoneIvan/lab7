package bmstu;


import org.zeromq.SocketType;
import org.zeromq.ZMQ;

import java.util.ArrayList;

public class Proxy {

    public static final int COMMAND_ARGUMENT = 1;

    public static void main(String[] args) {
        ArrayList<StorageData> storageData = new ArrayList<>();
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket frontend =
                context.socket(SocketType.ROUTER);
        ZMQ.Socket backend =
                context.socket(SocketType.ROUTER);
        frontend.bind("tcp://*:5559");
        backend.bind("tcp://*:5560");
        System.out.println("launch and connect broker.");

        ZMQ.Poller items = context.poller(2);
        items.register(frontend, ZMQ.Poller.POLLIN);
        items.register(backend, ZMQ.Poller.POLLIN);
        boolean more = false;
        byte[] message;

        while (!Thread.currentThread().isInterrupted()) {
            items.poll();
            if (items.pollin(0)) {
                message = frontend.recv(0);
                String[] parsedMsg = message.toString().split("");
                if (parsedMsg[COMMAND_ARGUMENT] = "PUT"){
                    for (StorageData data : storageData){
                        if (data.getStartSeq() <= )
                    }
                }

            }
            if (items.pollin(1)) {
                message = backend.recv(0);

            }

        }
    }
}