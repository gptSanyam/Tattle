import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * Created by sanyamgupta on 06/05/17.
 */
public class ChatUI implements ActionListener, ListSelectionListener{

    JFrame rootFrame;
    JPanel contactsPanel;
    JPanel chatPanel;

    //Contacts panel components
    final JLabel contactsLabel = new  JLabel("Contacts");
    DefaultListModel listModel = new DefaultListModel();
    JList contactsList;
    JScrollPane contactsScroll;
    //JButton addContactButton = new JButton("Add Contact");
    JTextField addContactField = new JTextField(20);

    //Chat Panel components
    JLabel contactName = new JLabel();
    static JTextArea chatArea = new JTextArea();
    JScrollPane chatScroll = new JScrollPane(chatArea);
    JTextField typeHere = new JTextField();

    int ROOT_PANEL_WIDTH = 600;
    int ROOT_PANEL_HEIGHT = 400;
    int LABEL_HEIGHT = 20;
    int TEXT_FIELD_HEIGHT = 30;
    int CONTACTS_PANEL_WIDTH = 150;
    int CHAT_PANEL_WIDTH = ROOT_PANEL_WIDTH - CONTACTS_PANEL_WIDTH;

    Dimension ROOT_PANEL_DIM = new Dimension(ROOT_PANEL_WIDTH, ROOT_PANEL_HEIGHT);
    Dimension CONTACTS_PANEL_DIM = new Dimension(CONTACTS_PANEL_WIDTH, ROOT_PANEL_HEIGHT);
    Dimension CHAT_PANEL_DIM = new Dimension(CHAT_PANEL_WIDTH, ROOT_PANEL_HEIGHT);
    Dimension CONTACT_LABEL_DIM = new Dimension(CONTACTS_PANEL_WIDTH, LABEL_HEIGHT);
    Dimension CHAT_LABEL_DIM = new Dimension(CHAT_PANEL_WIDTH, LABEL_HEIGHT);
    Dimension CONTACTS_LIST_DIM = new Dimension(CONTACTS_PANEL_WIDTH, ROOT_PANEL_HEIGHT - LABEL_HEIGHT - TEXT_FIELD_HEIGHT);
    Dimension CHAT_BOX_DIM = new Dimension(CHAT_PANEL_WIDTH, ROOT_PANEL_HEIGHT - LABEL_HEIGHT - TEXT_FIELD_HEIGHT);
    Dimension CONTACTS_TEXT_BOX_DIM = new Dimension(CONTACTS_PANEL_WIDTH, TEXT_FIELD_HEIGHT);
    Dimension CHAT_TEXT_BOX_DIM = new Dimension(CHAT_PANEL_WIDTH, TEXT_FIELD_HEIGHT);

    static BlockingQueue<String> outpostMessage = new LinkedBlockingQueue<>();

    ChatUI(){
        rootFrame = new JFrame("Tattle");
        rootFrame.setLayout(new BoxLayout(rootFrame.getContentPane(), BoxLayout.X_AXIS));
        rootFrame.setSize(ROOT_PANEL_DIM);
        rootFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //populate contacts panel
        setupContactsPanel();
        //populate chat panel
        setupChatPanel();
        rootFrame.getContentPane().add(contactsPanel);
        rootFrame.getContentPane().add(chatPanel);
        rootFrame.pack();
        rootFrame.setVisible(true);
    }

    private void setupChatPanel(){
        chatPanel = new JPanel();
        chatPanel.setPreferredSize(CHAT_PANEL_DIM);
        chatPanel.setLayout(new BoxLayout(chatPanel, BoxLayout.Y_AXIS));

        contactName.setPreferredSize(CHAT_LABEL_DIM);
        chatPanel.add(contactName);

        chatArea.setText("");
        chatArea.setEditable(false);
        chatScroll.setPreferredSize(CHAT_BOX_DIM);
        chatPanel.add(chatScroll);

        typeHere.setPreferredSize(CHAT_TEXT_BOX_DIM);
        typeHere.addActionListener(this);
        chatPanel.add(typeHere);
    }

    private void setupContactsPanel() {
        contactsPanel = new JPanel();
        contactsPanel.setSize(CONTACTS_PANEL_DIM);
        contactsPanel.setLayout(new BoxLayout(contactsPanel, BoxLayout.Y_AXIS));

        contactsLabel.setPreferredSize(CONTACT_LABEL_DIM);
        contactsPanel.add(contactsLabel);

        contactsList = new JList(listModel);
        contactsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        contactsList.setLayoutOrientation(JList.VERTICAL);
        contactsList.addListSelectionListener(this);
        contactsScroll = new JScrollPane(contactsList);
        contactsScroll.setPreferredSize(CONTACTS_LIST_DIM);
        contactsPanel.add(contactsScroll);

        addContactField = new JTextField();
        addContactField.setPreferredSize(CONTACTS_TEXT_BOX_DIM);
        contactsPanel.add(addContactField);
        addContactField.addActionListener(this);
    }

    public static void createAndShowGUI(){
        ChatUI ui = new ChatUI();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == addContactField){
            addNewContact();
        }
        else{
            if(e.getSource() == typeHere){
                sendMessage();
            }
        }
    }

    private void sendMessage() {
        String message = typeHere.getText();
        chatArea.append(message+"\n");
        try {
            outpostMessage.put(message);
        } catch (InterruptedException e) {
            chatArea.append("Unable to send: "+message+"\n");
            e.printStackTrace();
        }
        typeHere.setText("");
    }

    private void addNewContact() {
        String newContact = addContactField.getText();
        listModel.addElement(newContact);
        addContactField.setText("");
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {
        if(!e.getValueIsAdjusting()){
            String selectedContact = (String)contactsList.getSelectedValue();
            if(selectedContact!=null)
            {
                changeContact(selectedContact);
            }
        }
    }

    private void changeContact(String contact) {
        contactName.setText(contact);
        loadChatForContact(contact);
    }

    private void loadChatForContact(String contact) {
        chatArea.setText("");
    }

    public static void main(String[] args){

        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                //boolean connected = connectToServer(); //we are trying to connect to the server inside the UI thread, can we take it out? Or make it asynchronous

                //ask user to sign in
                createAndShowGUI();


            }
        });

        boolean connected = connectToServer();
    }

    private static boolean connectToServer() {

        String hostName = "127.0.0.1";
        int portNumber = Integer.parseInt("5051");

        String USERNAME = "Rahul";
        String DEST = "Sanyam";

        try (
                Socket chatSocket = new Socket(hostName, portNumber);
                PrintWriter out =
                        new PrintWriter(chatSocket.getOutputStream(), true);
                BufferedReader in =
                        new BufferedReader(
                                new InputStreamReader(chatSocket.getInputStream()))



        ) {
            (new Thread(new ClientWriterThread(in, chatArea))).start();
            String userInput;
            //authenticate user
            out.println(USERNAME+"; "+"; ");
            System.out.println("Connected to server");
            while ((userInput = outpostMessage.take()) != null) {
                out.println(USERNAME+";"+DEST+";"+userInput);

            }
            return true;
        } catch (UnknownHostException e) {
            System.err.println("Host doesn't exists " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " +
                    hostName);
            System.exit(1);
        } catch (InterruptedException e){
            System.err.println("Interrupted exception on input queue");
            System.exit(1);
        }

        return false;
    }

}
