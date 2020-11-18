package gui.chat;

import main.*;
import com.sun.istack.internal.Nullable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.Lighting;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PrivatChatController {
    private final Logger logger = Logger.getLogger(PrivatChatController.class.getName());


    private String correspondent;
    /* ----------------------------- @FXML ----------------------------- */
    @FXML
    private TextField msgField;
    @FXML
    private Label chatTitle;
    @FXML
    private ListView<String> chatPanePrivate;
    @FXML
    private Button send_button;


    public void initialize() {

        chatPanePrivate.setItems(ChatApplication.chatClient.getPrivateMessages());

        this.correspondent = ChatApplication.correspondent;

        String loggedInAs = "Logged in as (" + ChatApplication.chatClient.getUser().toString() + ")";
        String chattingTo = "your talking to (" + correspondent + ")";
        chatTitle.setText(loggedInAs + " | " + chattingTo);

    }

    /* ----------------------------- CONSTRUCTOR ----------------------------- */
    public PrivatChatController() {
    }

    public PrivatChatController(String correspondent) {
        this.correspondent = correspondent;
    }

    /* ----------------------------- SEND PRIVATE ----------------------------- */
    public void sendPrivateAction() throws IOException {
        String text = msgField.getText();
        if (!text.isEmpty()) {
            chatClient.sendPrivateMsg(text, correspondent);
            msgField.clear();
        } else {
            flashTextField(this.msgField);
        }
    }

    /* ----------------------------- KEY PRESSED ----------------------------- */
    public void keyPressed(KeyEvent ke) throws IOException {
        if (ke.getCode().equals(KeyCode.ENTER)) sendPrivateAction();
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
    public void closePrivateChat() throws IOException {
//        ChatApplication.chatClient.sendPrivateMsg("I left", correspondent);
        ChatApplication.resetPrivateChat();
    }


    /*  -------------------------------- LOGGER -------------------------------- */
    private void info(String msg, @Nullable Object... params) {
        logger.log(Level.INFO, msg, params);
    }

    private void error(String msg, @Nullable Object... params) {
        logger.log(Level.WARNING, msg, params);
    }
}
