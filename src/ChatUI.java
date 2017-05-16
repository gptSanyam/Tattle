import javax.swing.*;
import java.awt.*;

/**
 * Created by sanyamgupta on 06/05/17.
 */
public class ChatUI {

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
    JTextArea chatArea = new JTextArea();
    JScrollPane chatScroll = new JScrollPane(chatArea);
    JTextField typeHere = new JTextField();

    int ROOT_PANEL_WIDTH = 600;
    int ROOT_PANEL_HEIGHT = 400;
    int LABEL_HEIGHT = 20;
    int TEXT_FIELD_HEIGHT = 30;
    int CHAT_PANEL_WIDTH = 450;
    int CONTACTS_PANEL_WIDTH = ROOT_PANEL_WIDTH - CHAT_PANEL_WIDTH;

    Dimension ROOT_PANEL_DIM = new Dimension(ROOT_PANEL_WIDTH, ROOT_PANEL_HEIGHT);
    Dimension CONTACTS_PANEL_DIM = new Dimension(CONTACTS_PANEL_WIDTH, ROOT_PANEL_HEIGHT);
    Dimension CHAT_PANEL_DIM = new Dimension(CHAT_PANEL_WIDTH, ROOT_PANEL_HEIGHT);
    Dimension CONTACT_LABEL_DIM = new Dimension(CONTACTS_PANEL_WIDTH, LABEL_HEIGHT);
    Dimension CHAT_LABEL_DIM = new Dimension(CHAT_PANEL_WIDTH, LABEL_HEIGHT);
    Dimension CONTACTS_LIST_DIM = new Dimension(CONTACTS_PANEL_WIDTH, ROOT_PANEL_HEIGHT - LABEL_HEIGHT - TEXT_FIELD_HEIGHT);
    Dimension CHAT_BOX_DIM = new Dimension(CHAT_PANEL_WIDTH, ROOT_PANEL_HEIGHT - LABEL_HEIGHT - TEXT_FIELD_HEIGHT);
    Dimension CONTACTS_TEXT_BOX_DIM = new Dimension(CONTACTS_PANEL_WIDTH, TEXT_FIELD_HEIGHT);
    Dimension CHAT_TEXT_BOX_DIM = new Dimension(CHAT_PANEL_WIDTH, TEXT_FIELD_HEIGHT);

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

        chatScroll.setPreferredSize(CHAT_BOX_DIM);
        chatPanel.add(chatScroll);

        typeHere.setPreferredSize(CHAT_TEXT_BOX_DIM);
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
        //contactsList.addListSelectionListener(this);
        contactsScroll = new JScrollPane(contactsList);
        contactsScroll.setPreferredSize(CONTACTS_LIST_DIM);
        contactsPanel.add(contactsScroll);

        addContactField = new JTextField();
        addContactField.setPreferredSize(CONTACTS_TEXT_BOX_DIM);
        contactsPanel.add(addContactField);
        //addContactField.addActionListener(this);
    }

    public static void createAndShowGUI(){
        ChatUI ui = new ChatUI();

    }

    public class ChatPanel extends JComponent{

    }

    public static void main(String[] args){
        javax.swing.SwingUtilities.invokeLater(new Runnable(){
            public void run(){
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                boolean connected = connectToServer();
                if(connected){
                    createAndShowGUI();
                }
                else{
                    //Create a dialog and ask the user to check internet connectivity and try again
                }

            }
        });
    }

    private static boolean connectToServer() {
        return true;
    }
}
