package gui.login;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.effect.Lighting;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import main.ChatApplication;
import main.ChatClient;

import java.io.IOException;
import java.rmi.NotBoundException;

public class LoginController {
    private ChatClient chatClient = null;
    private ChatApplication chatApplication;

    @FXML
    private TextField userField;
    @FXML
    private TextField serverField;
    @FXML
    private TextField portField;


    /* ----------------------------- ERROR MESSAGES ----------------------------- */
    static final String ERROR_EMPTY_USER = "Required !";
    static final String ERROR_EMPTY_SERVER = "Required ! !";
    static final String ERROR_EMPTY_PORT = "Required ! ! !";
    static final String ERROR_FORMAT_PORT = "Only numbers allowed !";


    /* ----------------------------- METHODS ----------------------------- */
    public void initialize() {
        this.chatApplication = ChatApplication.getApplication();
        userField.setText("romeo");  //TODO REMOVE !!!
    }

    public void connectButtonAction() throws IOException, NotBoundException {
        String userNameString = this.userField.getText();
        String serverString = serverField.getText();
        String portString = portField.getText();
        boolean correct = true;

        if (userNameString.isEmpty()) {
            this.userField.setText(ERROR_EMPTY_USER);
            flashTextField(this.userField);
            correct = false;
        }

        if (serverString.isEmpty()) {
            this.serverField.setText(ERROR_EMPTY_SERVER);
            flashTextField(this.serverField);
            correct = false;
        }

        if (portString.isEmpty()) {
            this.portField.setText(ERROR_EMPTY_PORT);
            flashTextField(this.portField);
            correct = false;
        } else if (!isInteger(portString)) {
            this.portField.setText(ERROR_FORMAT_PORT);
            flashTextField(this.portField);
            correct = false;
        }

        if (correct) {
            int port = Integer.parseInt(portString);

            ChatApplication.connectToServer(userNameString, serverString, port);

        }

    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    /* ----------------------------- KEY PRESSED ----------------------------- */
    public void keyPressed(KeyEvent ke) throws IOException, NotBoundException {
        if (ke.getCode().equals(KeyCode.ENTER)) connectButtonAction();
    }

    /* ----------------------------- FIELD PRESSED ----------------------------- */
    public void userNameClicked() {
        userField.clear();
        userField.setEffect(null);
    }

    public void serverClicked() {
        serverField.clear();
        serverField.setEffect(null);
    }

    public void portClicked() {
        portField.clear();
        portField.setEffect(null);
    }

    /* ----------------------------- VISUAL EFFECTS ----------------------------- */
    public void flashTextField(TextField t) {
        Lighting errorLighting = new Lighting();
        t.setEffect(errorLighting);
    }
}
