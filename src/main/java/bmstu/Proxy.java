package bmstu;


import org.zeromq.SocketType;
import org.zeromq.ZMQ;

public class Proxy {
    public static void main(String[] args) {
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
                

            }
            if (items.pollin(1)) {
                message = backend.recv(0);

            }

        }
    }
}