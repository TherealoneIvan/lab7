package bmstu;


import org.zeromq.SocketType;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Proxy {

    public static final int COMMAND_ARGUMENT = 0;
    public static final String CLIENT_PUT_COMM = "PUT";
    public static final int INPUT_DIGIT = 1;
    public static final int LIFE_CICLE = 10000;

    public static HashMap<ZFrame , StorageData> storageData;
    public static void main(String[] args) {
//        ArrayList<StorageData> storageData = new ArrayList<>();
        storageData = new HashMap<>();
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
        ZMsg message;

        while (!Thread.currentThread().isInterrupted()) {
            items.poll();
            if (items.pollin(0)) {
                message = ZMsg.recvMsg(frontend);
                String[] parsedMsg = message.toString().split("");
                if (parsedMsg[COMMAND_ARGUMENT].equals(CLIENT_PUT_COMM)){
                    for (Map.Entry<ZFrame , StorageData> data : storageData.entrySet()){
                        if (data.getStartSeq() <= Integer.parseInt(parsedMsg[INPUT_DIGIT])
                                && data.getEndSeq() >= Integer.parseInt(parsedMsg[INPUT_DIGIT])
                                && System.currentTimeMillis() - data.getTimeLife() > LIFE_CICLE){

                        }
                    }
                }

            }
            if (items.pollin(1)) {
                message = ZMsg.recvMsg(backend);
                ZFrame adress = message.unwrap();
                if (message.getFirst().toString().contains("NOTIFY")){
                    message.pop();
                    int startSeq = Integer.parseInt(message.popString());
                    int endSeq = Integer.parseInt(message.popString());
                    storageData.put(adress , new StorageData(startSeq , endSeq , System.currentTimeMillis()));
                }
            }

        }
    }
}