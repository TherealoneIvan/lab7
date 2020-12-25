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
        storageData = new HashMap<>();
        ZMQ.Context context = ZMQ.context(1);
        ZMQ.Socket frontend =
                context.socket(SocketType.ROUTER);
        ZMQ.Socket backend =
                context.socket(SocketType.ROUTER);
        frontend.bind("tcp://localhost:5559");
        backend.bind("tcp://localhost:5560");
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
                System.out.println("qwe");
                String[] parsedMsg = message.getLast().toString().split(" ");
                for (int i = 0 ; i < parsedMsg.length;i++)
                    System.out.println("parsedMsg " + parsedMsg[i]);
                    for (Map.Entry<ZFrame , StorageData> data : storageData.entrySet()){
                        System.out.println(data.getValue().getStartSeq() + " == " + data.getValue().getEndSeq());
                        if (data.getValue().getStartSeq() <= Integer.parseInt(parsedMsg[INPUT_DIGIT])
                                && data.getValue().getEndSeq() >= Integer.parseInt(parsedMsg[INPUT_DIGIT])
                                && System.currentTimeMillis() - data.getValue().getTimeLife() > LIFE_CICLE){
                            message.wrap(data.getKey());
                            System.out.println("data " + data.getKey());
                        }
                        message.send(backend);
                        if (parsedMsg[0].equals("GET")){
                            break;
                        }
                    }
            }
            if (items.pollin(1)) {
                message = ZMsg.recvMsg(backend);
                ZFrame adress = message.unwrap();
                if (message.getFirst().toString().contains("NOTIFY")){
                    message.pop();
//                    System.out.println("got message");
                    int startSeq = Integer.parseInt(message.popString());
                    int endSeq = Integer.parseInt(message.popString());
                    storageData.put(adress , new StorageData(startSeq , endSeq , System.currentTimeMillis()));

                }else {
                    System.out.println(message.getLast().toString());
                    message.send(frontend);
                }
            }

        }
    }
}