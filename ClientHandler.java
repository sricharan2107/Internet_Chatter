import java.io.*;
import java.util.*;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ClientHandler implements Runnable {
    public static final String IP = "127.0.0.1";
    public static Socket socket;
    public int port;
    public String name;
    public String user2;
    public DataOutputStream dataOutputStream = null;
    public DataInputStream dataInputStream = null;

    public ClientHandler(int port, String name, String user2) {
        this.port = port;
        this.name = name;
        this.user2 = user2;
    }

    public void transferFile(String filePath) {
        try {
            File file = new File(filePath);
            FileInputStream fileInputStream = new FileInputStream(file);
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataOutputStream.writeLong(file.length());
            dataOutputStream.flush();
            int noOfBytes = 0;
            byte[] dataInBytes = new byte[1024];
            while ((noOfBytes = fileInputStream.read(dataInBytes))
                    != -1) {
                dataOutputStream.write(dataInBytes, 0, noOfBytes);
                dataOutputStream.flush();
            }
            fileInputStream.close();
        } catch (IOException ioException) {
            System.out.println("Exception Occurred while writing sending file to client " + ioException);
        }
    }

    public void executeCommands(String command) {
        try {
            if (command.startsWith("transfer")) {
                String args[] = command.split(" ");
                File file = new File(args[1]);
                if(!file.exists()) {
                    System.out.println("File not found!");
                    return;
                }
                dataOutputStream.writeUTF(command);
                dataOutputStream.flush();
                transferFile(args[1]);
            } else {
                dataOutputStream.writeUTF(command);
                dataOutputStream.flush();
            }
        } catch (IOException ioException) {
            System.out.println("Exception occurred while writing to the other peer " + ioException);
        }


    }

    @Override
    public void run() {
        try {
            socket = new Socket(IP, port); 
            socket.setSoTimeout(1000 * 2);
            System.out.println(name + " established connection with " + user2);
        } catch (SocketTimeoutException unknownHostException) {
            System.out.println("Unknown Host Exception occurred when establishing " +
                    "connection with Server " + unknownHostException);
        } catch (IOException ioException) {
            //System.out.println("IO Exception occurred when establishing " +
                   // "connection with Server " + ioException);
                   System.out.println("No user is available with the given port number");
        } finally {
            if(socket == null) {
                while(socket == null){
                    Scanner sc = new Scanner(System.in);
                    System.out.println("Enter a port to be connected and the user- ");
                    int port = sc.nextInt();
                    user2 = sc.nextLine();
                    user2 = user2.trim();
                    try{
                        socket = new Socket(IP, port);
                    } catch(IOException e) {
                        //e.printStackTrace();
                        System.out.println("No user is available with the given port number");
                    }
                    
                }                
                System.out.println(name + " established connection with " + user2);
                ClientHandler.this.user2= user2;
            }
            
        }


        try {
            dataInputStream = new DataInputStream(socket.getInputStream());
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                String command = bufferedReader.readLine();
                executeCommands(command);

            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

    }
}
