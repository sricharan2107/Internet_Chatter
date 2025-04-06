import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.io.*;

public class ChatBot {
    public static DataInputStream dataInputStream = null;
    public static ServerSocket serverSocket;
    public static Socket socket;
    public static String name;
    public static String user2;

    public static void main(String args[]) {
        ChatBot chatBot = new ChatBot();
        Scanner sc = new Scanner(System.in);
        if(args.length == 0) {
            System.out.println("Enter your name to connect to the server");
            name = sc.nextLine();
        } else {
            name = args[0];
        }
        
        try {
            serverSocket = new ServerSocket(0);
            System.out.println("Program is running on port " + serverSocket.getLocalPort());
            
            System.out.println("Enter a port to be connected and the user- ");
            int port = sc.nextInt();
            user2 = sc.nextLine();
            user2 = user2.trim();
            ClientHandler clientHandler = new ClientHandler(port, name, user2);//"Alice");
            Thread thread = new Thread(clientHandler);
            thread.start();
        } catch (IOException e) {
            System.out.println("Exception while  creating a server socket " + e);
        }

        try {
            socket = serverSocket.accept();
            chatBot.executeReadingThread();
        } catch (IOException e) {
            System.out.println("Exception while establishing connection " + e);
        }
    }

    public void executeReadingThread() {
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            while (true) {
                try {
                    if (dataInputStream.available() > 0) {
                        String string = dataInputStream.readUTF();
                        if (string.startsWith("transfer")) {
                            String args[] = string.split(" ");
                            receiveFile("new" + args[1]);
                        } else {
                            System.out.println(user2 + ": " + string);
                        }
                    }
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }

            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    public void receiveFile(String fileName) {
        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            int noOfBytes = 0;
            //System.out.print("here");
            FileOutputStream fileOutputStream
                    = new FileOutputStream(fileName);
            long size
                    = dataInputStream.readLong();
            byte[] dataInBytes = new byte[1024];
            while (size > 0
                    && (noOfBytes = dataInputStream.read(
                    dataInBytes, 0,
                    dataInBytes.length))
                    != -1) {
                fileOutputStream.write(dataInBytes, 0, noOfBytes);
                size -= noOfBytes;
            }
            System.out.println("File received successfully!");
            fileOutputStream.close();
        } catch (IOException ioException) {
            System.out.println("Exception on uploading file to server " + ioException);
        }
    }
    
}
