package sample;

import sample.datamodel.ToDoItem;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class Controller {

    private List<ToDoItem> toDoItems;                   //lista naszych zadań

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

    }
}
