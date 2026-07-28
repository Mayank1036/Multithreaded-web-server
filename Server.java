import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

/**
 * MultiThreaded Web Server implementation in Java.
 * Listens for incoming client socket connections on port 8010 
 * and spawns a dedicated worker thread to handle each client concurrently.
 */
public class Server {

    /**
     * Returns a Consumer lambda that handles communication with a connected socket client.
     */
    public Consumer<Socket> getConsumer() {
        return (clientSocket) -> {
            try (Socket socket = clientSocket;
                 PrintWriter toSocket = new PrintWriter(socket.getOutputStream(), true)) {
                toSocket.println("Hello from server " + socket.getInetAddress());
            } catch (IOException ex) {
                System.err.println("Client handler exception: " + ex.getMessage());
            }
        };
    }
    
    public static void main(String[] args) {
        int port = 8010;
        Server server = new Server();
        
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("[+] Server successfully listening on port " + port);
            System.out.println("[+] Awaiting incoming connections...");
            
            while (true) {
                Socket clientSocket = serverSocket.accept();
                
                // Spawn a new worker thread for each incoming client connection
                Thread thread = new Thread(() -> server.getConsumer().accept(clientSocket));
                thread.start();
            }
        } catch (IOException ex) {
            System.err.println("Server exception: " + ex.getMessage());
        }
    }
}
