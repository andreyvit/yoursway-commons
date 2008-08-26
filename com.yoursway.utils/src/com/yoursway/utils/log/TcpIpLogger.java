package com.yoursway.utils.log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TcpIpLogger implements Logger {
    
    private static final int PORT = 4321;
    
    private PrintWriter writer = null;
    
    public void add(LogEntry entry) {
        try {
            connect();
            writer.println(entry.message());
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
    
    private void connect() throws UnknownHostException, IOException {
        if (writer != null)
            return;
        
        Socket socket = new Socket("localhost", PORT);
        writer = new PrintWriter(socket.getOutputStream(), true);
    }
    
    public static void main(String[] args) {
        try {
            ServerSocket server = new ServerSocket(PORT);
            int i = 0;
            
            while (true) {
                final int client = i++;
                final Socket socket = server.accept();
                
                Thread thread = new Thread() {
                    @Override
                    public void run() {
                        try {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(socket
                                    .getInputStream()));
                            
                            while (true) {
                                String line = reader.readLine();
                                if (line == null)
                                    break;
                                
                                System.out.println(client + ": " + line);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                
                thread.start();
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
