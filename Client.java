import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * MultiThreaded Client Simulator in Java.
 * Spawns multiple client threads to simulate parallel connection requests to the Web Server.
 */
public class Client {
    
    /**
     * Creates a Runnable instance for connecting to the server and reading its response.
     */
    public Runnable getRunnable() {
        return () -> {
            int port = 8010;
            try {
                InetAddress address = InetAddress.getByName("localhost");
                try (Socket socket = new Socket(address, port);
                     PrintWriter toSocket = new PrintWriter(socket.getOutputStream(), true);
                     BufferedReader fromSocket = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                    
                    toSocket.println("Hello from Client " + socket.getLocalSocketAddress());
                    String response = fromSocket.readLine();
                    System.out.println("[Client Thread] Server response: " + response);
                }
            } catch (IOException e) {
                System.err.println("Client socket exception: " + e.getMessage());
            }
        };
    }
    
    public static void main(String[] args) {
        Client client = new Client();
        int clientCount = 100;
        
        System.out.println("[+] Launching " + clientCount + " parallel client threads...");
        for (int i = 0; i < clientCount; i++) {
            Thread thread = new Thread(client.getRunnable());
            thread.start();
        }
    }
}
