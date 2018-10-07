package sample;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.time.LocalDate;

//Much like the AddBreakDialog, this brings up a form for the user to 
//add Holiday dates to, to be stored to the file and Table View.
public class AddHolidayDialog {

    @FXML
    private TextField holidayName;
    @FXML
    private DatePicker date;

    public void processResults() throws IOException {
        String name = holidayName.getText().trim();
        LocalDate newDate = date.getValue();

        Holiday newHoliday = new Holiday(name, newDate);
        HolidayData.getInstance().addHoliday(newHoliday);
        HolidayData.getInstance().storeHolidays();
    }

    public void editHoliday(Holiday holiday){
        holidayName.setText(holiday.getName());
        date.setValue(holiday.getDate());
    }

    public void updateHoliday(Holiday holiday){
        holiday.setName(holidayName.getText());
        holiday.setDate(date.getValue());
    }
}
