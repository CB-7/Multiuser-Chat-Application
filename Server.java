import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class Server
{
    //  For storing userNames
    static  ArrayList<String> userNames=new ArrayList<>();
    //  For storing Sockets Output Stream
    static  ArrayList<PrintWriter> printWriters=new ArrayList<>();

    public static void main(String[] args) throws Exception
    {
        ServerSocket ss=new ServerSocket(1024);

        while(true)
        {
            System.out.println("Waiting for Clients...");
            Socket socket= ss.accept();
            System.out.println("Connection Established");
            ChatHandler handler=new ChatHandler(socket);
            handler.start();

        }
    }
}


class ChatHandler extends Thread
{
    Socket socket;
    BufferedReader in;
    PrintWriter out;
    String name;

    public ChatHandler(Socket socket) throws Exception
    {

        this.socket=socket;
        in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out=new PrintWriter(socket.getOutputStream(),true);

    }



    public void run()
    {
        try
        {
           Boolean flag=false;
            //  Loop till Client Provides a Unique userName
           while(true)
           {
               if(flag)
               {
                   out.println("NameAlreadyExists");
               }
               else
               {
                   out.println("NameRequired");
               }

               name=in.readLine();

               if(name==null)
                   return;
               if(!Server.userNames.contains(name))
               {
                   Server.userNames.add(name);
                   break;
               }
               flag=true;

           }     // end of while


            out.println("NameAccepted"+name);
            Server.printWriters.add(out);



            //   Continuously reading Clients message
            while(true)
            {
                String message=in.readLine();
                if(message==null)
                    return;


                for(PrintWriter writer:Server.printWriters)
                {
                    writer.println(name+": "+message);
                }
            }


        }
        catch(Exception e)
        {
            System.out.println(e);
        }

    }
}