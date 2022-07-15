import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

class Server extends JFrame{

    ServerSocket server;
    Socket socket;

    BufferedReader br;//for reading
    PrintWriter out;//for writing

    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto",Font.TRUETYPE_FONT,25);



public Server()
{
    try{
        server = new ServerSocket(7777);
        System.out.println("server is ready to accept");
        System.out.println("waiting... ");
        socket = server.accept();

        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream());

        createGUI();
        handleEvents();

        startReading();
        // startWriting();

    }catch(Exception e){
        // e.printStackTrace();
    }
}
private void handleEvents() {

    messageInput.addKeyListener(new KeyListener(){

        @Override
        public void keyTyped(KeyEvent e) {
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
            
        }

        @Override
        public void keyReleased(KeyEvent e) {
            // System.out.println("key released "+e.getKeyCode());
            if(e.getKeyCode() == 10){
                // System.out.println("you have pressed enter button");
                String contentToSend = messageInput.getText();
                messageArea.append("Me : "+contentToSend+"\n");
                out.println(contentToSend);
                out.flush();               
                messageInput.setText("");
                messageInput.requestFocus();     
            }
            
        }

    });
}
private void createGUI(){

    this.setTitle("Server Messager[END]");
    this.setSize(600,700);
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    //coding for component
    heading.setFont(font);
    messageArea.setFont(font);
    messageInput.setFont(font);
    heading.setIcon(new ImageIcon("logo.png"));
    heading.setHorizontalTextPosition(SwingConstants.CENTER);
    heading.setVerticalTextPosition(SwingConstants.BOTTOM);
    heading.setHorizontalAlignment(SwingConstants.CENTER);
    heading.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
    messageArea.setEditable(false);
    messageInput.setHorizontalAlignment(SwingConstants.CENTER);

    //Form layout set
    this.setLayout(new BorderLayout());

    //adding the component to frame
    this.add(heading,BorderLayout.NORTH);
    JScrollPane jScrollPane = new JScrollPane(messageArea);
    this.add(jScrollPane,BorderLayout.CENTER);
    this.add(messageInput,BorderLayout.SOUTH);

    this.setVisible(true);

}


public void startReading(){

    Runnable r1 = () -> {
        System.out.println("reader started..");
        try {
            while(true){
                    String msg = br.readLine();
                    if(msg.equals("exit")){
                        System.out.println("client terminated the chat");
                        JOptionPane.showMessageDialog(this, "Server Terminated the chat");
                        messageInput.setEnabled(false);
                        socket.close();

                        break;
                    }
                    // System.out.println("client : "+msg);
                    messageArea.append("Client : "+ msg+"\n");
            }
        } catch (Exception e) { System.out.println("Connection closed");
        }
    };
    new Thread(r1).start();

}

public void startWriting(){

    Runnable r2 = () -> {
        System.out.println("writer started...");
        try {
            while(true && !socket.isClosed()){
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    out.println(content);
                    out.flush();

                    if(content.equals("exit")){
                        socket.close();
                        break;
                    }
    
            }
            
        } catch (Exception e) 
        {
            System.out.println("Connection is closed");;
        }

    };
     
    new Thread(r2).start();

}
    public static void main(String[] args) {

        System.out.println("Starting...");
        new Server();
    }

}