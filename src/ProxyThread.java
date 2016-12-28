import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

class ProxyThread extends Thread {

    private final Socket clientSocket;
    private String host;
    private int port;
    private String methode;
    
    public ProxyThread(Socket socket) 
    {
        this.clientSocket = socket;
    }
    
    public void config(String connect)
    {
    	String[] tmp = connect.split(" ", 3);
    	if(tmp[0].equals("CONNECT"))
    	{
        	String[] tmp2 = tmp[1].split(":");
        	this.host = tmp2[0];
        	this.port = Integer.parseInt(tmp2[1]);    		
    	}
    	else
    	{
    		String[] tmp2 = tmp[1].split("/", 4);
    		this.host = tmp2[2];
    		this.port = 80;
    	}
    	this.methode = tmp[0];
    }
    	
    public void run() {
        try {
            // Read request
            InputStream incommingIS = clientSocket.getInputStream();
            byte[] b = new byte[8196];
            int len = incommingIS.read(b);

            if (len > 0) {

                String connect = new String(b, 0, len);
                this.config(connect);
                if(this.port>0)
                {
                	if(this.methode.equals("CONNECT"))
                	{
                		System.out.println("ouverture connexion.. 443 MAGGEUUUUUUUUUUUUUUUUUUUUUUUUUUUUULE..\n");
                    	System.out.println("REQUEST (" + this.host +":"+ this.port+")" + System.getProperty("line.separator") + "-------");
                        System.out.println(connect);
                        // Write request
                        Socket socket = new Socket(this.host, this.port);

                        // Copy response
                        OutputStream incommingOS = clientSocket.getOutputStream();
                        String connected = "HTTP/1.1 200 OK\r\n\r\n";
                        incommingOS.write(connected.getBytes());
                        System.out.println("fermeture connexion..\n\n");
                        incommingOS.close();
                        incommingIS.close();
                        socket.close();
                	}
                	else
                	{
                		System.out.println("\t\t########## NEW SOCKET : " + this.host + ":" + this.port + "##########\r\n");
                    	System.out.println("\tREQUEST :");
                        System.out.println(connect);
                        // Write request
                        Socket socket = new Socket(this.host, this.port);
                        OutputStream outgoingOS = socket.getOutputStream();
                        outgoingOS.write(b, 0, len);

                        // Copy response
                        OutputStream incommingOS = clientSocket.getOutputStream();
                        InputStream outgoingIS = socket.getInputStream();
                        for (int length; (length = outgoingIS.read(b)) != -1;) {
                            System.out.println("RESPONSE"+ System.getProperty("line.separator") + "-------");
                        	System.out.println(new String(b, 0, length));
                            incommingOS.write(b, 0, length);
                        }
                        System.out.println("fermeture connexion..\n\n");
                        incommingOS.close();
                        outgoingIS.close();
                        outgoingOS.close();
                        incommingIS.close();

                        socket.close();
                	}
                }
                
            } else {
                incommingIS.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}