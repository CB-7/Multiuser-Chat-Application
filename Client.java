import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

class Client
{
    // Components of Swing Library
    static JFrame chatWindow= new JFrame("Chat Application");
    static JTextArea chatArea=new JTextArea(22,41);
    static JTextField textField=new JTextField(41);
    static JLabel blankLabel=new JLabel("      ");
    static JLabel nameLabel=new JLabel("      ");
    static JButton sendButton=new JButton("Send");
    static BufferedReader in;
    static PrintWriter out;


    public Client()
    {
        // Adding components to chatWindow
        chatWindow.setLayout(new FlowLayout());
        chatWindow.add(nameLabel);
        chatWindow.add(new JScrollPane(chatArea));
        chatWindow.add(blankLabel);
        chatWindow.add(textField);
        chatWindow.add(sendButton);
        chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatWindow.setSize(450,510);
        chatWindow.setVisible(true);
        textField.setEditable(false);
        chatArea.setEditable(false);

        // Adding Action Listener  to send button and textField
        sendButton.addActionListener(new Listener());
        textField.addActionListener(new Listener());

    }


    void startChat() throws Exception
    {
        String ipAddress=JOptionPane.showInputDialog(chatWindow,"Enter IP Address","Ip Address Required!!!",JOptionPane.PLAIN_MESSAGE);
        Socket socket=new Socket(ipAddress,1024);

        in=new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out=new PrintWriter(socket.getOutputStream(),true);

        while(true)
        {
           String str=in.readLine();
           if(str.equals("NameRequired"))
           {
               String name=JOptionPane.showInputDialog(chatWindow,"Enter Username:","Name Required",JOptionPane.PLAIN_MESSAGE);
               out.println(name);
           }
           else if(str.equals("NameAlreadyExists"))
           {
               String name=JOptionPane.showInputDialog(chatWindow,"Enter Another Name:","Username Already Exists",JOptionPane.ERROR_MESSAGE);
               out.println(name);
           }
           else if(str.startsWith("NameAccepted"))
           {
               textField.setEditable(true);
               nameLabel.setText("You are logged in as: "+str.substring(12));
           }
           else
           {
               chatArea.append(str + "\n");
           }

        }
    }



    public static void main(String[] args) throws Exception
    {
       Client client=new Client();
       client.startChat();
    }
}


class Listener implements ActionListener
{
    @Override
    public void actionPerformed(ActionEvent e)
    {
        Client.out.println(Client.textField.getText());
        Client.textField.setText("");
    }
}