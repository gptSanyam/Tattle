import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;

public class ClientWriterThread implements Runnable{

    BufferedReader br;
    JTextArea writeArea;

    public ClientWriterThread(BufferedReader in, JTextArea txtArea) {
        // TODO Auto-generated constructor stub
        System.out.println("Started new writer thread.");
        br = in;
        writeArea = txtArea;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            String message;
            while((message = br.readLine())!=null ){
                System.out.println("Received: "+message);
                writeArea.append(message+"\n");
            }

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}