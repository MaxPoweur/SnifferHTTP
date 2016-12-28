package model;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class ThreadProxy extends Thread 
{
    private Socket socketClient;
    private String host;
    private int hostPort;
    private String request;
    private String answer;
    private String method;
    private String proxyAdress;
    private int proxyPort;
    
    private static final int BUFFER_SIZE = 8196;

    public ThreadProxy(Socket socket) throws IOException
	{
        this.socketClient = socket;
        this.proxyAdress = null;
        this.proxyPort = 0;
    }
    
    public ThreadProxy(Socket socket, String proxyAdress, int proxyPort) throws IOException
	{
        this(socket);
        this.proxyAdress = proxyAdress;
        this.proxyPort = proxyPort;
    }

    public void config()
    {
    	String[] tmp = this.request.split(" ", 3);
    	if(tmp[0].equals("CONNECT"))
    	{
        	String[] tmp2 = tmp[1].split(":");
        	this.host = tmp2[0];
        	this.hostPort = Integer.parseInt(tmp2[1]);    		
    	}
    	else
    	{
    		String[] tmp2 = tmp[1].split("/", 4);
    		this.host = tmp2[2];
    		this.hostPort = 80;
    	}
    	this.method = tmp[0];
    	this.answer = "";
    }
    
    public String getMethod()
    {
    	return this.method;
    }
    
    public String getRequest()
    {
    	return this.request;
    }
    
    public String getAnswer()
    {
    	return this.answer;
    }
    
    public void run() 
	{
    	InputStream clientIncomingStream=null;
    	try
    	{
    		clientIncomingStream = this.socketClient.getInputStream();
            byte[] b = new byte[BUFFER_SIZE];
            int length = clientIncomingStream.read(b); // Read the request from the client [!Only BUFFER_SIZE!]

            if(length > 0) // If having any incoming stream
            {
            	this.request = new String(b, 0, length);
                this.config(); // Let's extract all required datas (host, port, ..)
                
                Socket serverSocket;
                if(this.proxyAdress==null || this.proxyPort==0)
                	serverSocket = new Socket(this.host, this.hostPort);
                else
                	serverSocket = new Socket(this.proxyAdress, this.proxyPort);

                serverSocket.setSoTimeout(750);
                OutputStream serverOutgoingStream = serverSocket.getOutputStream();
                serverOutgoingStream.write(b, 0, length); // Send the request to the server

                OutputStream clientOutgoingStream = this.socketClient.getOutputStream();
                InputStream serverIncomingStream = serverSocket.getInputStream();

                while((length = serverIncomingStream.read(b)) != -1) // Read the answer from the server
                {
                	this.answer += new String(b, 0, length);
	                clientOutgoingStream.write(b, 0, length); // Send the answer to the client
                }
                
                /*Partie affichage
                
                System.out.println("\t\t########## NEW SOCKET : " + this.host + ":" + this.hostPort + " ##########\r\n");
                 
                System.out.println("REQUEST\r\n----------");
                System.out.println(this.request);
                System.out.println("----------\r\nEND OF REQUEST");

            	System.out.println("ANSWER \r\n----------");
                System.out.println(this.answer);
                System.out.println("----------\r\nEND OF ANSWER");
                
                System.out.println("\t\t########## END OF SOCKET : " + this.host + ":" + this.hostPort + " ##########\r\n");
                
                */
                
                // Close all streams and server socket
                serverIncomingStream.close();
                serverOutgoingStream.close();
                clientIncomingStream.close();
                clientOutgoingStream.close();
                serverSocket.close();
            }
        }
    	catch (SocketTimeoutException e)
    	{
            //System.err.print("Timeout exceeded for the remote server to respond.");
        }
    	catch (IOException e)
    	{
            //e.printStackTrace();
        }
    	finally
    	{
            try
            {
            	clientIncomingStream.close();
                this.socketClient.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }
}