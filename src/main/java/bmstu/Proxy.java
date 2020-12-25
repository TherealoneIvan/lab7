package bmstu;


import org.zeromq.SocketType;
import org.zeromq.ZFrame;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Proxy {
    public static final int INPUT_DIGIT = 1;
    public static final int CLIENT = 0;
    public static final int STORAGE = 1;
    public static final String TCP_LOCALHOST_5559 = "tcp://localhost:5559";
    public static final String TCP_LOCALHOST_5560 = "tcp://localhost:5560";
    public static final String SPACE_REGEX = " ";
    public static final String GET = "GET";
    public static final String NOTIFY = "NOTIFY";

    public static HashMap<ZFrame , StorageData> storageData;
    public static void main(String[] args) {
        storageData = new HashMap<>();
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket client =
                context.socket(SocketType.ROUTER);
        ZMQ.Socket storage =
                context.socket(SocketType.ROUTER);
        client.bind(TCP_LOCALHOST_5559);
        storage.bind(TCP_LOCALHOST_5560);
        System.out.println("launch and connect broker.");

        ZMQ.Poller items = context.poller(2);
        items.register(client, ZMQ.Poller.POLLIN);
        items.register(storage, ZMQ.Poller.POLLIN);
        ZMsg message;

        while (!Thread.currentThread().isInterrupted()) {
            items.poll();
            boolean found = false;
            if (items.pollin(CLIENT)) {
                message = ZMsg.recvMsg(client);
                String[] parsedMsg = message.getLast().toString().split(SPACE_REGEX);
                for (int i = 0 ; i < parsedMsg.length;i++)
                    System.out.println("parsedMsg " + parsedMsg[i]);
                    for (Map.Entry<ZFrame , StorageData> data : storageData.entrySet()){
                        System.out.println(data.getValue().getStartSeq() + " == " + data.getValue().getEndSeq());
                        if (data.getValue().getStartSeq() <= Integer.parseInt(parsedMsg[INPUT_DIGIT])
                                && data.getValue().getEndSeq() >= Integer.parseInt(parsedMsg[INPUT_DIGIT])){
                            found = true;
                            message.wrap(data.getKey().duplicate());
                        }
                        message.send(storage);
                        if (parsedMsg[0].equals(GET)){
                            break;
                        }
                    }
            }
            if (items.pollin(STORAGE)) {
                message = ZMsg.recvMsg(storage);
                ZFrame adress = message.unwrap();
                if (message.getFirst().toString().contains(NOTIFY)){
                    message.pop();
                    int startSeq = Integer.parseInt(message.popString());
                    int endSeq = Integer.parseInt(message.popString());
                    storageData.put(adress , new StorageData(startSeq , endSeq , System.currentTimeMillis()));
                }else {
                    System.out.println(message.getLast().toString());
                    message.send(client);
                }
            }

        }
    }
}