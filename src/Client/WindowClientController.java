package Client;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ResourceBundle;

public class WindowClientController implements Initializable {

    @FXML
    private JFXTextField text;

    @FXML
    private JFXCheckBox CBdel;

    @FXML
    private JFXCheckBox CBclear;

    @FXML
    private JFXCheckBox CBgetstr;

    @FXML
    private StackPane stackPane;

    @FXML
    private JFXTextField textIP;

    private Thread server;

    private Client client;

    //метода передачи сообщений в
    public void SendMessage() {
        client.setMessage(text.getText());
    }

    //инициализация окна Dialog
    public void Dialog(String from, String answer) {
        JFXDialogLayout content = new JFXDialogLayout();
        Text txt = new Text(from);
        txt.setFont(Font.font("Verdana", 18));
        content.setHeading(txt);
        content.setBody(new Text(answer));
        JFXButton btn = new JFXButton("Ok");
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER);
        btn.setOnAction(event -> dialog.close());
        content.setActions(btn);
        dialog.show();
    }

    //отправка запроса на удаление строки максимальной длины
    public void DeleteString() {
        client.setMessage("$deletemax");
        CBdel.setSelected(false);
    }

    //запрос на очистку массива
    public void ClearArray() {
        client.setMessage("$clear");
        CBclear.setSelected(false);
    }

    //запрос на получение строки из массива
    public void GetString() {
        try {
            int i = Integer.parseInt(text.getText()) - 1;
            client.setMessage("$get");
            client.setNum(String.valueOf(i));
        } catch (NumberFormatException e) {
            Dialog("ENTER NUMBER TO TEXT FIELD!", "");
        }
        CBgetstr.setSelected(false);
    }

    //подключение к серверу
    public void ConnectServer() {
        if (!server.isAlive()) {
            client.setIP(textIP.getText());
            server = new Thread(client);
            server.start();
        } else {
            Dialog("Cient already connected to server!", "");
        }
    }

    //закрытие потока
    public void closeThread() {
        client.CloseSocket();
        server.stop();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        client = new Client(this, "127.0.0.1");
        server = new Thread(client);
    }
}