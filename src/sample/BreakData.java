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

public class BreakData {
    private static BreakData instance = new BreakData();
    private static String fileName = "schoolBreaks.txt"; //File where school breaks is currently stored
    private DateTimeFormatter formatter;
    
    //I used the observableList because it allows for items to be added or deleted from in real time without
    //The program needing to be restarted.
    private ObservableList<Break> breaks;

    public BreakData(){
        //initializes formatter so it displayes the dates properly
        formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
    }

    public static BreakData getInstance() {
        return instance;
    }

    public ObservableList<Break> getBreaks(){
        return breaks;
    }

    public void addBreak(Break newBreak ){
        breaks.add(newBreak);
    }

    //This function loads the breaks from the .txt file, breaks them into pieces using the 
    //split string method and stores them in their appropriate cells on the break table in
    //the gui
    public void loadBreaks() throws IOException {
        breaks = FXCollections.observableArrayList();
        Path path = Paths.get(fileName);
        BufferedReader br = Files.newBufferedReader(path);

        String input;
        try{
            while((input = br.readLine()) != null){
                String[] breakPieces = input.split("\t");

                String name = breakPieces[0];
                String stringStartDate =  breakPieces[1];
                String stringEndDate =  breakPieces[2];

                LocalDate startDate = LocalDate.parse(stringStartDate, formatter);
                LocalDate endDate = LocalDate.parse(stringEndDate, formatter);
                Break newBreak = new Break(name, startDate, endDate);
                breaks.add(newBreak);
            }
        }catch(IOException e) {
            e.printStackTrace();
        } finally{
            if(br != null){
                br.close();
            }
        }
    }

    //Stores the breaks in the .txt file by iterating through the breaks and storing
    //them piece by piece into the .txt file.
    public void storeBreaks()throws IOException{
        Path path = Paths.get(fileName);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            Iterator<Break> iter = breaks.iterator();
            while(iter.hasNext()){
                Break newBreak = iter.next();
                bw.write(String.format("%s\t%s\t%s", newBreak.getName(), newBreak.getBreakStart().format(formatter),
                        newBreak.getBreakEnd().format(formatter)));
                bw.newLine();
            }
        }finally {
            if (bw != null){
                bw.close();
            }

        }
    }

    public void deleteBreak(Break oldBreak){
        breaks.remove(oldBreak);
    }

    //Function used in the main program to determine if what the calculator is going over is a date
    //if it is a break it returns true and the calculator skips that date, otherwise it counts it.
    public boolean isBreak(LocalDate date){
        for(Break b : breaks){
            if((date.compareTo(b.getBreakStart()) >=0) && (date.compareTo(b.getBreakEnd()) <=0)){
                return true;
            }
        }
        return false;
    }
}
