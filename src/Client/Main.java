package Client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Main extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    //главное окно
    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class.getResource("WindowClient.fxml"));
            StackPane Pane =  loader.load();
            primaryStage = new Stage();
            primaryStage.setTitle("Client window");
            Scene scene = new Scene(Pane);
            primaryStage.setResizable(false);
            WindowClientController controller = loader.getController();
            primaryStage.setScene(scene);
            primaryStage.show();

            //слушатель закрытия окна
            primaryStage.setOnCloseRequest(we -> {
                Client.setMessage("$c10se");

                try{
                    Thread.sleep(500);
                } catch (InterruptedException e) {}

            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
