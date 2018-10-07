package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.File;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

//This is the main Application where all of the classes are brought together and
//utilized.
public class Controller {
    @FXML
    private BorderPane mainGridPane;
    @FXML
    private Button fileButton;
    @FXML
    private Label fileName;
    @FXML
    private ListView<Integer> listViewNumbers;
    @FXML
    private DatePicker datePickerDate;
    @FXML
    private Label evaluation40;
    @FXML
    private Label evaluation80;
    @FXML
    private Label evaluation130;
    @FXML
    private TableView<Break> breakTableView;
    @FXML
    private TableView<Holiday> holidayTableView;
    @FXML
    private TableColumn<Holiday, LocalDate> dateColumn;
    @FXML
    private TableColumn<Break, LocalDate> breakStart;
    @FXML
    private TableColumn<Break, LocalDate> breakEnd;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    private Set<Job> jobs = new LinkedHashSet<>();
    private XSSFRow row;
    private boolean fileClosed = false;

    //The instance holds all of the holidays or breaks respective to their classes.
    //Once the initialize method is called, the holiday table view and break table view
    //are loaded with the dates that are inside their respective .txt files. The setCellFactory
    //is set to updateItem function and allows the table to instantly display whatever items
    //have been added or deleted without reloading the entire program
    public void initialize() throws IOException {
        holidayTableView.setItems(HolidayData.getInstance().getHolidays());

        dateColumn.setCellFactory(column -> new TableCell<Holiday, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(formatter.format(date));
                }
            }
        });

        breakTableView.setItems(BreakData.getInstance().getBreaks());
        breakStart.setCellFactory(column -> new TableCell<Break, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(formatter.format(date));
                }
            }
        });

        breakEnd.setCellFactory(column -> new TableCell<Break, LocalDate>() {
            @Override
            protected void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                if (empty) {
                    setText("");
                } else {
                    setText(formatter.format(date));
                }
            }
        });
    }
    
    //This is the main calculator where everything comes together. The calculator takes in the current totalDaysPaid
    //variable which is stored in the jobs set and loaded into a listView for the user to see. Once picked, the start and end
    //dates for the respective type of employee are found and ran against the actual start and end dates of the employee that 
    //the user input. It counts to 40, 80, and 130 showing the appropriate assessment dates for that particular employee.
    @FXML
    public void handleCalculateButtonClick() {
        if ((fileClosed) && (datePickerDate.getValue() != null)) {
            int selectedEmployee = listViewNumbers.getSelectionModel().getSelectedItem();
            Job job = new Job();
            for (Job j : jobs) {
                if (j.getTotalDaysPaid() == selectedEmployee) {
                    job = new Job(j);
                }
            }
            int count = 0;
            int daysSkipped = 0;
            boolean countChecked = true;
            LocalDate employeeStartDate = datePickerDate.getValue();
            LocalDate officialStartDate = job.getStartDate();
            LocalDate officialEndDate = job.getEndDate();
            LocalDate nextYearStartDate = job.getNextYearStart();
            LocalDate nextYearEndDate = job.getNextYearEnd();

            for (int i = 0; count < 130; i++) {
                if (((employeeStartDate.plusDays(i).compareTo(officialStartDate) >= 0) && (employeeStartDate.plusDays(i).compareTo(officialEndDate) <= 0)
                        || (employeeStartDate.plusDays(i).compareTo(nextYearStartDate) >=0) && employeeStartDate.plusDays(i).compareTo(nextYearEndDate) <=0)){
                    if ((employeeStartDate.plusDays(i).getDayOfWeek() != DayOfWeek.SATURDAY) && (employeeStartDate.plusDays(i).getDayOfWeek() != DayOfWeek.SUNDAY)){
                        if (!HolidayData.getInstance().isHoliday(employeeStartDate.plusDays(i))){
                            if (!BreakData.getInstance().isBreak(employeeStartDate.plusDays(i)) || (job.getTotalDaysPaid() >= 260)){
                                count++;
                                countChecked = true;
                            }else{
                                daysSkipped++;
                            }
                        }else{
                            daysSkipped++;
                        }
                    }else{
                        daysSkipped++;
                    }
                } else{
                        daysSkipped++;
                    }
                if ((count == 40) && (countChecked)) {
                    evaluation40.setText(formatter.format(employeeStartDate.plusDays((count + daysSkipped) - 1)));
                    countChecked = false;
                }
                if ((count == 80) && (countChecked)) {
                    evaluation80.setText(formatter.format(employeeStartDate.plusDays((count + daysSkipped) - 1)));
                    countChecked = false;
                }
                if ((count == 130) && (countChecked)) {
                    evaluation130.setText(formatter.format(employeeStartDate.plusDays((count + daysSkipped) - 1)));
                    countChecked = false;
                }

            }
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information Error");
            alert.setHeaderText("Missing Information Error");
            alert.setContentText("Please make sure the correct dates and file is selected.");
            alert.showAndWait();
        }
    }

    //Opens up the file chooser so the user can select a an excel file for the parser.
    @FXML
    public void handleFileButtonClick() throws Exception {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Please Select a SpreadSheet");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xlsx)", "*.xlsx");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(mainGridPane.getScene().getWindow());
        if (file != null) {
            open(file);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("File Not Found");
            alert.setContentText("File was not found or cannot be read.");
            alert.showAndWait();
        }

    }
    //Brings up a new form for the user to fill out and add a holiday to their list of exceptions
    public void showAddHolidayDialog() throws IOException {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainGridPane.getScene().getWindow());
        dialog.setTitle("Add Holiday Menu");
        dialog.setContentText("Add New Holiday");
        dialog.setHeaderText("Use this Dialogue to create a new Holiday");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/AddHolidayDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            errorLoadingFile(loader);
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AddHolidayDialog controller = loader.getController();
            controller.processResults();
        }

    }
    //Deletes a holiday from the txt file and from the table view
    public void deleteHoliday() {
        Holiday holiday = holidayTableView.getSelectionModel().getSelectedItem();
        if (holiday == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Item Selection Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an Item to delete.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Holiday");
            alert.setContentText("Are you sure you want to delete " + holiday.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                HolidayData.getInstance().deleteHoliday(holiday);
                try {
                    HolidayData.getInstance().storeHolidays();
                } catch (IOException e) {
                    Alert warning = new Alert(Alert.AlertType.INFORMATION);
                    warning.setTitle("Error Storing File");
                    warning.setHeaderText(null);
                    warning.setContentText("File was not saved properly or may be corrupt.");
                    warning.showAndWait();
                }

            }
        }
    }
    //Edits a holiday object
    public void editHoliday() {
        Holiday selectedHoliday = holidayTableView.getSelectionModel().getSelectedItem();
        if (selectedHoliday == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Item Selection Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an Item to edit.");
            alert.showAndWait();
        } else {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainGridPane.getScene().getWindow());
            dialog.setTitle("Edit Holiday");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/AddHolidayDialog.fxml"));
            try {
                dialog.getDialogPane().setContent(loader.load());
            } catch (IOException e) {
                errorLoadingFile(loader);
            }
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            AddHolidayDialog controller = loader.getController();
            controller.editHoliday(selectedHoliday);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                controller.updateHoliday(selectedHoliday);
                try {
                    HolidayData.getInstance().storeHolidays();
                } catch (IOException e) {
                    errorStoringFile();
                }

            }
        }
    }

    //Calls the AddBreakDialog so the user can fill out the form and create a new break.
    @FXML
    public void showAddBreaksDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainGridPane.getScene().getWindow());
        dialog.setTitle("Add Break Menu");
        dialog.setContentText("Add New Break");
        dialog.setHeaderText("Use this Dialogue to create a new Break");
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/AddBreaksDialog.fxml"));
        try {
            dialog.getDialogPane().setContent(loader.load());
        } catch (IOException e) {
            errorLoadingFile(loader);
            return;
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            AddBreaksDialog controller = loader.getController();
            try {
                controller.processBreak();
            } catch (IOException e) {
                errorStoringFile();
            }
        }
    }
    
    //Allows the user to delete a break 
    @FXML
    public void deleteBreak() {
        Break newBreak = breakTableView.getSelectionModel().getSelectedItem();
        if (newBreak == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Item Selection Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an Item to delete.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Break");
            alert.setContentText("Are you sure you want to delete " + newBreak.getName() + "?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                BreakData.getInstance().deleteBreak(newBreak);
                try {
                    BreakData.getInstance().storeBreaks();
                } catch (IOException e) {
                    Alert warning = new Alert(Alert.AlertType.INFORMATION);
                    warning.setTitle("Error Storing File");
                    warning.setHeaderText(null);
                    warning.setContentText("File was not saved properly or may be corrupt.");
                    warning.showAndWait();
                }
            }
        }
    }
    
    //This function allows the user to edit a break by selecting the break and clicking the edit button
    @FXML
    public void editBreak() {
        Break selectedBreak = breakTableView.getSelectionModel().getSelectedItem();
        if (selectedBreak == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Item Selection Error");
            alert.setHeaderText(null);
            alert.setContentText("Please select an Item to edit.");
            alert.showAndWait();
        } else {
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.initOwner(mainGridPane.getScene().getWindow());
            dialog.setTitle("Edit Break");
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/AddBreaksDialog.fxml"));
            try {
                dialog.getDialogPane().setContent(loader.load());
            } catch (IOException e) {
                errorLoadingFile(loader);
            }
            dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            AddBreaksDialog controller = loader.getController();
            controller.editBreak(selectedBreak);

            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                controller.updateBreak(selectedBreak);
                try {
                    HolidayData.getInstance().storeHolidays();
                } catch (IOException e) {
                    errorStoringFile();
                }

            }
        }
    }

    //This is the excel parser. This part of the application runs once the user has selected the 
    //appropriate excel file to be load. It goes through the entire spreadsheet and stores the start and end
    //dates of the current year and next year, as well as the total days paid for the employees. There is a 
    //lot of duplicate information in the spreadsheet, so I store it all in a set and overrode the compartator in
    //the Job class to compare totalDaysPaid as those were the unique attributes
    private void open(File file) throws Exception {
        XSSFWorkbook workbook = new XSSFWorkbook(file);
        XSSFSheet spreadSheet = workbook.getSheetAt(0);
        Iterator<Row> rowIterator = spreadSheet.iterator();
        rowIterator.next();
        rowIterator.next();
        rowIterator.next();
        rowIterator.next();
        rowIterator.next();

        while (rowIterator.hasNext()) {
            row = (XSSFRow) rowIterator.next();
            Iterator<Cell> cellIterator = row.cellIterator();
            Job job = new Job();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                DataFormatter dataFormatter = new DataFormatter();
                String cellStringValue = dataFormatter.formatCellValue(cell);
                switch (cell.getColumnIndex()) {
                    case 0:
                    case 1:
                        break;
                    case 2:
                        if (!cellStringValue.equals("")) {
                            String[] date = cellStringValue.split("/");
                            LocalDate newDate = LocalDate.of(2000 + Integer.parseInt(date[2]), Integer.parseInt(date[0]), Integer.parseInt(date[1]));
                            job.setStartDate(newDate);

                        }
                        break;
                    case 3:
                        if (!cellStringValue.equals("")) {
                            String[] date2 = cellStringValue.split("/");
                            LocalDate newDate2 = LocalDate.of(2000 + Integer.parseInt(date2[2]), Integer.parseInt(date2[0]), Integer.parseInt(date2[1]));
                            job.setEndDate(newDate2);
                        }
                        break;
                    case 4:
                        if (!cellStringValue.equals("")) {
                            job.setTotalDaysPaid(Integer.parseInt(cellStringValue));
                        }
                        break;
                    case 5:
                        break;
                    case 6:
                        if (!cellStringValue.equals("")) {
                            String[] date3 = cellStringValue.split("/");
                            LocalDate newDate3 = LocalDate.of(2000 + Integer.parseInt(date3[2]), Integer.parseInt(date3[0]), Integer.parseInt(date3[1]));
                            job.setNextYearStart(newDate3);
                        }

                        break;
                    case 7:
                        if (!cellStringValue.equals("")) {
                            String[] date4 = cellStringValue.split("/");
                            LocalDate newDate4 = LocalDate.of(2000 + Integer.parseInt(date4[2]), Integer.parseInt(date4[0]), Integer.parseInt(date4[1]));
                            job.setNextYearEnd(newDate4);
                        }
                        break;
                    default:
                        break;
                }
            }
            if (job.getStartDate() != null) {
                jobs.add(job); //This is a linkHashSet as defined at the beginning of the Controller Class.
                               //I chose to store the items in this data structure due to the fact that the excel spreadsheet is
                               //already in order so I wouldn't have to sort it late and the set would prevent any duplicates that
                               //I didn't need
            }
        }
        workbook.close(); //closes the workbook
        fileClosed = true; //Enables the calculator to actually work
        fileButton.setDisable(true); //disables the button so they can't select a different file in this instance of the program
        fileName.setText("Currently Reading: " + file.getName());

        ObservableList<Integer> paidDays = FXCollections.observableArrayList();

        for (Job job : jobs) {
            paidDays.add(job.getTotalDaysPaid()); //creates an observable ArrayList with the jobs set to be used to populate the ListView
        }
        listViewNumbers.setItems(paidDays);
    }

    //Displays error message if the file can't be loaded
    private void errorLoadingFile(FXMLLoader loader){
        Alert warning = new Alert(Alert.AlertType.INFORMATION);
        warning.setTitle("Error Loading File");
        warning.setHeaderText(null);
        warning.setContentText("The file" + loader.getLocation().getFile() + " was not loader properly.");
        warning.showAndWait();
    }
    //Displays error message if the file can't be stored.
    private void errorStoringFile(){
        Alert warning = new Alert(Alert.AlertType.INFORMATION);
        warning.setTitle("Error Storing File");
        warning.setHeaderText(null);
        warning.setContentText("File was not saved properly or may be corrupt.");
        warning.showAndWait();
    }
}
