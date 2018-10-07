package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;

//Much like the BreakData class, this class is responsible for handling all of the holiday object and
//storing them into an instance that the program can use to load them into a table for the user to see
public class HolidayData {
    private static HolidayData instance = new HolidayData();
    private static String fileName = "schoolHolidays.txt";
    private DateTimeFormatter formatter;

    private ObservableList<Holiday> holidays;

    public HolidayData(){
        formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    }

    public static HolidayData getInstance() {
        return instance;
    }

    public ObservableList<Holiday> getHolidays(){
        return holidays;
    }

    public void addHoliday(Holiday holiday){
        holidays.add(holiday);
    }


    public void loadHolidays() throws IOException {
        holidays = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);
        BufferedReader br = Files.newBufferedReader(path);

        String input;
        try{
            while((input = br.readLine()) != null){
                String[] holidayPieces = input.split("\t");

                String name = holidayPieces[0];
                String dateString =  holidayPieces[1];

                LocalDate date = LocalDate.parse(dateString, formatter);
                Holiday holiday = new Holiday(name, date);
                holidays.add(holiday);
            }
        }catch(IOException e) {
            e.printStackTrace();
        } finally{
            if(br != null){
                br.close();
            }
        }
    }

    public void storeHolidays()throws IOException{
        Path path = Paths.get(fileName);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            Iterator<Holiday> iter = holidays.iterator();
            while(iter.hasNext()){
                Holiday holiday = iter.next();
                bw.write(String.format("%s\t%s", holiday.getName(), holiday.getDate().format(formatter)));
                bw.newLine();
            }
        }finally {
            if (bw != null){
                bw.close();
            }

        }
    }

    public void deleteHoliday(Holiday holiday){
        holidays.remove(holiday);
    }

    public boolean isHoliday(LocalDate date){
        for(Holiday holiday : holidays){
            if(date.equals(holiday.getDate())){
                return true;
            }
        }
        return false;
    }
}
