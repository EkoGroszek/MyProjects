package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.datamodel.ToDoData;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainwindow.fxml"));
        primaryStage.setTitle("TODO List");
        primaryStage.setScene(new Scene(root, 900, 500));
        primaryStage.show();
        //test with using iltelijjIdea
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {                                   // zapis listy do pl;iku po zamknięciu apki
        try {
            ToDoData.getInstance().storeToDoItems();
        }catch (IOException e){
            System.out.println("error");
        }
    }

    @Override
    public void init() throws Exception {                                   // odczyt listy z pliku po odpaleniu apki
        try {
            ToDoData.getInstance().loadToDoItems();
        }catch (IOException e){
            System.out.println("error");
        }
    }
}
