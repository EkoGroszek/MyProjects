package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import sample.datamodel.ToDoItem;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private List<ToDoItem> toDoItems;                   //lista naszych zadań

    @FXML
    private ListView<ToDoItem> toDoListView;           // parametr do wyświetlania listy zadań w pasku po lewo
    @FXML
    private TextArea itemDetailsTextarea;               // prametr do wyświetlania detali zadania w polu tekstowym
    @FXML
    private Label deadlineLabel;                        // parametr od etykiedy daty zakończenia zadania

    public void initialize() {
        ToDoItem item1 = new ToDoItem("Testowe przypomnienie o urodzinach", "Kup Oli coś na urodziny dzbanie", LocalDate.of(2019, Month.APRIL, 25));
        ToDoItem item2 = new ToDoItem("Lekarz", "Idź do lekarza dzbanie ", LocalDate.of(2019, Month.MAY, 12));
        ToDoItem item3 = new ToDoItem("Skończ projekt", "Skończ ten projekt w końcu ", LocalDate.of(2019, Month.APRIL, 20));
        ToDoItem item4 = new ToDoItem("Samobój", "Weź sie zabij albo co", LocalDate.of(2019, Month.JANUARY, 28));
        ToDoItem item5 = new ToDoItem("Zakupy", "Idź na zakupy kup mleko", LocalDate.of(2019, Month.MARCH, 1));

        toDoItems = new ArrayList<>();
        toDoItems.add(item1);
        toDoItems.add(item2);
        toDoItems.add(item3);
        toDoItems.add(item4);
        toDoItems.add(item5);

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

        toDoListView.getItems().setAll(toDoItems);                                                                      // przekazanie listy zadań do parametru który ją wyświetla
        toDoListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);                                        // linia odpowiedzialna za zaznacznie elementów listy (SINGLE - tylko jeden na raz | MULTIPLE - z CTRL można zaznaczyć kilka)
        toDoListView.getSelectionModel().selectFirst();                                                                 // zaznacza pierwsze zadanie na liście odrazu po właczniu apki

    }

    @FXML
    public void handleClickListView() {                                                                                 //handler do klikania listy zadań
        ToDoItem item = toDoListView.getSelectionModel().getSelectedItem();                                             //pobieram do zmiennej "item" dane z aktualnie klikniętego zadania
        itemDetailsTextarea.setText(item.getDetails());                                                                 //przekazuje do pola teksotwgo "detale zadania"
        deadlineLabel.setText(item.getDeadline().toString());                                                           //przekazuje do etykiety "Termin zadania"
        //System.out.println(item);
//        StringBuilder sb = new StringBuilder(item.getDetails());
//        sb.append("\n\n\n\n");
//        sb.append("Termin zadania : ");
//        sb.append(item.getDeadline().toString());
//        itemDetailsTextarea.setText(sb.toString());                                                                     //wyświetlam detale kliknętego aktualnie zadania

    }
}
