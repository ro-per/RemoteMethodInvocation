package gui.chat;

import com.sun.istack.internal.Nullable;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.Lighting;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.WindowEvent;
import main.ChatApplication;
import main.ChatClient;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PublicChatController {
    private final Logger logger = Logger.getLogger(PublicChatController.class.getName());
    private ChatClient chatClient = null;
    /* ----------------------------- @FXML ----------------------------- */
    @FXML
    private TextField msgField;
    @FXML
    private Label chatTitle;
    @FXML
    private ListView<String> chatPanePublic;
    @FXML
    private ListView<String> userPane;

    public void initialize() {
        chatPanePublic.setItems(ChatApplication.chatClient.getPublicMessages());
        userPane.setItems(ChatApplication.chatClient.getUsers());
        String loggedInAs = "Logged in as (" + ChatApplication.chatClient.getUser().toString() + ")";
        chatTitle.setText(loggedInAs);// logged in as ...
    }

    /* ----------------------------- CONSTRUCTOR ----------------------------- */
    public PublicChatController() {
    }


    /* ----------------------------- SEND BROADCAST ----------------------------- */
    public void sendBroadcastAction() throws IOException {
        String text = msgField.getText();
        if (!text.isEmpty()) {
            ChatApplication.chatClient.sendBroadcastMsg(text);
            msgField.clear();
        } else {
            flashTextField(this.msgField);
        }
    }

    /* ----------------------------- KEY PRESSED ----------------------------- */
    public void keyPressed(KeyEvent ke) throws IOException {
        if (ke.getCode().equals(KeyCode.ENTER)) sendBroadcastAction();
    }

    @FXML
    public void chooseUser() throws MalformedURLException {

        String selectedUser = userPane.getSelectionModel().getSelectedItem();
        String currentUser = ChatApplication.chatClient.getUser().getName();

        boolean self = selectedUser.equals(currentUser);

        if (!self) {
            ChatApplication.chatClient.sendRequestPrivateMSG("I want to send a message", selectedUser);
            ChatApplication.launchPrivateChat(selectedUser);
        }
    }

    /* ----------------------------- FIELD PRESSED ----------------------------- */
    public void messageFieldClicked() {
        msgField.setEffect(null);
    }


    /* ----------------------------- VISUAL EFFECTS ----------------------------- */
    public void flashTextField(TextField t) {
        Lighting errorLighting = new Lighting();
        t.setEffect(errorLighting);
    }


    /* ----------------------------- EXIT ----------------------------- */
    public void closePublicChat(WindowEvent event) throws IOException {
        //Only  perform leave is chatclient is started
        if (chatClient != null) {
            chatClient.disconnectUser();
        }
        Platform.exit();
        System.exit(0);
    }

    public void setChatClient(ChatClient chatClient) {
        this.chatClient = chatClient;
    }


    /*  -------------------------------- LOGGER -------------------------------- */
    private void info(String msg, @Nullable Object... params) {
        logger.log(Level.INFO, msg, params);
    }

    private void error(String msg, @Nullable Object... params) {
        logger.log(Level.WARNING, msg, params);
    }

}
