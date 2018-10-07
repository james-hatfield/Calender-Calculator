package sample;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.time.LocalDate;

public class AddBreaksDialog {
    @FXML
    private TextField breakName;
    @FXML
    private DatePicker breakStart;
    @FXML
    private DatePicker breakEnd;

    public void processBreak() throws IOException {
        String name = breakName.getText().trim();
        LocalDate startDate = breakStart.getValue();
        LocalDate endDate = breakEnd.getValue();

        Break newBreak = new Break(name, startDate, endDate);
        BreakData.getInstance().addBreak(newBreak);
        BreakData.getInstance().storeBreaks();
    }

    public void editBreak(Break newBreak){
        breakName.setText(newBreak.getName());
        breakStart.setValue(newBreak.getBreakStart());
        breakEnd.setValue(newBreak.getBreakEnd());
    }

    public void updateBreak(Break newBreak){
        newBreak.setName(breakName.getText());
        newBreak.setBreakStart(breakStart.getValue());
        newBreak.setBreakEnd(breakEnd.getValue());
    }
}
