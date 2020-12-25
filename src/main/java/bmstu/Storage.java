package bmstu;

import org.zeromq.SocketType;
import org.zeromq.ZMQ;
import org.zeromq.ZMsg;

import java.util.ArrayList;
import java.util.Scanner;

public class Storage {
    public static final String SPACE_REGEX = " ";
    public static final String GET_COMM = "GET";
    public static final String PUT_COMM = "PUT";
    public static final int PUT_KEY_INDEX = 1;
    public static final int PUT_VAL_INDEX = 2;
    static Scanner input = new Scanner(System.in);
    static String arguments = input.nextLine();
    static String [] parsedArg = arguments.split(SPACE_REGEX);
    static Integer start = Integer.parseInt(parsedArg[0]);
    static Integer end = Integer.parseInt(parsedArg[1]);
    static ArrayList<String> arrayOfValues = itemsArrayBuilder(parsedArg);
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
                msg.addLast(arrayOfValues.toString());
                msg.send(storageSocket);
                startTime = System.currentTimeMillis();
            }
            if (storage.pollin(0)){
                ZMsg messageFromProxy = ZMsg.recvMsg(storageSocket);
                messageFromProxy.unwrap();
                String[] parsedMessage = messageFromProxy.toString().split(SPACE_REGEX);
                if (parsedMessage[0].equals(GET_COMM)){
                    messageFromProxy.addLast(parsedArg.toString());
                    messageFromProxy.send(storageSocket);
                }else if (parsedMessage[0].equals(PUT_COMM)){
                    Integer putKey = Integer.parseInt(parsedMessage[PUT_KEY_INDEX]);
                    String putValue = parsedMessage[PUT_VAL_INDEX];
                    arrayOfValues.set(putKey - start,putValue);
                }
            }
        }
    }
    public static ArrayList<String> itemsArrayBuilder(String[] arg){
        ArrayList<String> tmpArr = new ArrayList<>();
        for(String items : arg ){
            tmpArr.add(items);
        }
        return tmpArr;
    }
}
