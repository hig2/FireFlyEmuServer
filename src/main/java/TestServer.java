import java.net.*;
import java.io.*;
import java.util.Arrays;

public class TestServer extends Thread {
    private ServerSocket serverSocket;

    public TestServer(int port) throws IOException {
        serverSocket = new ServerSocket(port);
        serverSocket.setSoTimeout(20000);
    }

    public void run() {
        byte[] buffer = new byte[1024];
        String strFrame = "$1 0 0 0 0 0 0 0 0 0 0 1;";
        Socket client = null;

        while (true){
            while(client == null){
                try {
                    System.out.println("Ожидание клиента на порт " + serverSocket.getLocalPort() + "...");
                    client = serverSocket.accept();
                } catch (IOException e) {
                    System.out.println("время ожидания истекло");
                }
            }

            System.out.println("Просто подключается к " + client.getRemoteSocketAddress());
            DataInputStream in = null;
            DataOutputStream out = null;
            try {
                in = new DataInputStream(client.getInputStream());
                out = new DataOutputStream(client.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }

            while(client != null) {
                try {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                    if(in.available() > 0){
                        int read = in.read(buffer);
                        for (int i = 0; i < read;i++){
                            System.out.print(buffer[i] + " ");
                        }
                        System.out.println("");

                    }
                    out.writeUTF(strFrame);


                } catch (SocketTimeoutException s) {
                    System.out.println("Время сокета истекло!");
                } catch (IOException e) {
                        client = null;
                    e.printStackTrace();
                    System.out.println("Соединение потеряно, реконект!");
                }
            }
        }
    }

    public static void main(String [] args) {
        int port = 1801;
        if(args.length > 0){
           port = Integer.parseInt(args[0]);
        }

        try {
            Thread t = new TestServer(port);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}