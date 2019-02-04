package sample;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import sample.datamodel.ToDoData;
import sample.datamodel.ToDoItem;

import java.time.LocalDate;

public class DialogController {

    @FXML
    private TextField taskName;
    @FXML
    private TextArea deetailsArea;
    @FXML
    private DatePicker deadlinePicker;

    public ToDoItem processResult() {
        String task = taskName.getText().trim();
        String details = deetailsArea.getText().trim();
        LocalDate deadlineValue = deadlinePicker.getValue();

        ToDoItem newItem = new ToDoItem(task, details, deadlineValue);
        ToDoData.getInstance().addToDoItem(newItem);
        return newItem;
    }

}
