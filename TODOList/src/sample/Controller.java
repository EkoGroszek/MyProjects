package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import sample.datamodel.ToDoData;
import sample.datamodel.ToDoItem;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class Controller {

    private List<ToDoItem> toDoItems;                   //lista naszych zadań

    @FXML
    private ListView<ToDoItem> toDoListView;           // parametr do wyświetlania listy zadań w pasku po lewo
    @FXML
    private TextArea itemDetailsTextarea;               // prametr do wyświetlania detali zadania w polu tekstowym
    @FXML
    private Label deadlineLabel;                        // parametr od etykiedy daty zakończenia zadania
    @FXML
    private BorderPane mainBorderPain;                  //parametr od formatowania głównego okna aplikacji
    @FXML
    private ContextMenu listContextMenu;
    @FXML
    private ToggleButton handleFilterButton;            // guzik od filtrowania

    private FilteredList<ToDoItem> filteredList;        //lista filtrowana


    public void initialize() {

        listContextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Usuń");

        deleteMenuItem.setOnAction(new EventHandler<ActionEvent>() {                                                     // usuwanmie zadań z listy
            @Override
            public void handle(ActionEvent event) {
                ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();
                deleteItem(item);
            }
        });

        listContextMenu.getItems().addAll(deleteMenuItem);
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

        filteredList = new FilteredList<ToDoItem>(ToDoData.getInstance().getToDoItems(),
                new Predicate<ToDoItem>() {
                    @Override
                    public boolean test(ToDoItem item) {
                        return true;
                    }
                });
        SortedList<ToDoItem> sortedList = new SortedList<>(filteredList, new Comparator<ToDoItem>() {  //przeciążenie komparatrora listy
            @Override
            public int compare(ToDoItem o1, ToDoItem o2) {
                return o1.getDeadline().compareTo(o2.getDeadline());
            }
        });

        //toDoListView.setItems(ToDoData.getInstance().getToDoItems());                                                   // przekazanie listy zadań do parametru który ją wyświetla
        toDoListView.setItems(sortedList);
        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);                                        // linia odpowiedzialna za zaznacznie elementów listy (SINGLE - tylko jeden na raz | MULTIPLE - z CTRL można zaznaczyć kilka)
        toDoListView.getSelectionModel().selectFirst();                                                                 // zaznacza pierwsze zadanie na liście odrazu po właczniu apki

        toDoListView.setCellFactory(new Callback<ListView<ToDoItem>, ListCell<ToDoItem>>() {
            @Override
            public ListCell<ToDoItem> call(ListView<ToDoItem> param) {
                ListCell<ToDoItem> cell = new ListCell<ToDoItem>() {
                    @Override
                    protected void updateItem(ToDoItem item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setText(null);
                        } else {
                            setText(item.getShortDescription());
                            if (item.getDeadline().isBefore(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.RED);
                            } else if (item.getDeadline().equals(LocalDate.now().plusDays(1))) {
                                setTextFill(Color.BROWN);
                            }
                        }
                    }
                };

                cell.emptyProperty().addListener(                                                                        // w razie czego gdyby cell było puste
                        (obs, wasEmpty, isNowEmpty) -> {
                            if (isNowEmpty) {
                                cell.setContextMenu(null);
                            } else {
                                cell.setContextMenu(listContextMenu);
                            }
                        }

                );

                return cell;


            }
        });

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

            // toDoListView.getItems().setAll(ToDoData.getInstance().getToDoItems());                                      // po dodaniu nowego zadania pokazuje sie ono odrazu na liście
            toDoListView.getSelectionModel().select(newItem);

        }
    }

    @FXML
    public void handleClickListView() {                                                                                 //handler do klikania listy zadań
        ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();                                             //pobieram do zmiennej "item" dane z aktualnie klikniętego zadania
        itemDetailsTextarea.setText(item.getDetails());                                                                 //przekazuje do pola teksotwgo "detale zadania"
        deadlineLabel.setText(item.getDeadline().toString());                                                           //przekazuje do etykiety "Termin zadania"
    }

    public void deleteItem(ToDoItem item) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Usuń zadanie z listy");
        alert.setHeaderText("Usuń zadanie: " + item.getShortDescription());
        alert.setContentText("Czy jesteś pewien?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && (result.get() == ButtonType.OK)) {
            ToDoData.getInstance().deleteToDoItem(item);
        }
    }

    public void handleFilterButton() {
        if (handleFilterButton.isSelected()) {
            handleFilterButton.setText("Wszystkie zadania");
            filteredList.setPredicate(new Predicate<ToDoItem>() {
                @Override
                public boolean test(ToDoItem item) {
                    return (item.getDeadline().equals(LocalDate.now()));
                }
            });

        } else {
            handleFilterButton.setText("Dzisiejsze zadania");
            filteredList.setPredicate(new Predicate<ToDoItem>() {
                @Override
                public boolean test(ToDoItem item) {
                    return true;
                }
            });

        }
    }
}
