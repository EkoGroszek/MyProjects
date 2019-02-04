package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import sample.datamodel.ToDoData;
import sample.datamodel.ToDoItem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Controller {

    private List<ToDoItem> toDoItems;                   //lista naszych zadań

    @FXML
    private ListView<ToDoItem> toDoListView;           // parametr do wyświetlania listy zadań w pasku po lewo
    @FXML
    private TextArea itemDetailsTextarea;               // prametr do wyświetlania detali zadania w polu tekstowym
    @FXML
    private Label deadlineLabel;                        // parametr od etykiedy daty zakończenia zadania
    @FXML
    private BorderPane mainBorderPain;

    public void initialize() {


        toDoListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ToDoItem>() {            //handler który działa zawsze i reaguje na zmianę zaznaczonego zdania a nie na kliknięcie myszką
            @Override
            public void changed(ObservableValue<? extends ToDoItem> observable, ToDoItem oldValue, ToDoItem newValue) {
                if (newValue != null) {
                    ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();                                 //pobieram do zmiennej "item" dane z aktualnie klikniętego zadania
                    itemDetailsTextarea.setText(item.getDetails());                                                     //przekazuje do pola teksotwgo "detale zadania"
                    DateTimeFormatter dF = DateTimeFormatter.ofPattern("d MMMM yyyy");                                 //formater do daty
                    deadlineLabel.setText(dF.format(item.getDeadline()));                                               //przekazuje do etykiety "Termin zadania"
                }
            }
        });

        toDoListView.getItems().setAll(ToDoData.getInstance().getToDoItems());                                          // przekazanie listy zadań do parametru który ją wyświetla
        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);                                        // linia odpowiedzialna za zaznacznie elementów listy (SINGLE - tylko jeden na raz | MULTIPLE - z CTRL można zaznaczyć kilka)
        toDoListView.getSelectionModel().selectFirst();                                                                 // zaznacza pierwsze zadanie na liście odrazu po właczniu apki

    }

    @FXML
    public void showNewItemDialog() {                                                                                   // funkcja od wyskakującego okienka z wprowadzaniem nowego zadania
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPain.getScene().getWindow());
        dialog.setTitle("Nowe zadanie");
        //dialog.setHeaderText("Taki nagłówek ale odgordzony od reszty");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("todoItemDialog.fxml"));                                    // przkazanie "okna wprowadzania nowego zadania" do loadera

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Nie mogę załadować dialogu");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            DialogController controller = fxmlLoader.getController();
            ToDoItem newItem = controller.processResult();
            toDoListView.getItems().setAll(ToDoData.getInstance().getToDoItems());                                      // po dodaniu nowego zadania pokazuje sie ono odrazu na liście
            toDoListView.getSelectionModel().select(newItem);
            System.out.println("OK pressed");
        }else{
            System.out.println("Cancle pressed");
        }
    }

    @FXML
    public void handleClickListView() {                                                                                 //handler do klikania listy zadań
        ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();                                             //pobieram do zmiennej "item" dane z aktualnie klikniętego zadania
        itemDetailsTextarea.setText(item.getDetails());                                                                 //przekazuje do pola teksotwgo "detale zadania"
        deadlineLabel.setText(item.getDeadline().toString());                                                           //przekazuje do etykiety "Termin zadania"
    }
}
