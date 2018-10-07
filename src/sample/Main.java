package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/sample.fxml"));
        primaryStage.setTitle("Calendar Calculator");
        primaryStage.setScene(new Scene(root, 350, 350));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    //Calls the init function making sure that the files are there so that the controller
    //can uses the files to load the tables.
    @Override
    public void init() throws Exception {
        try{
            HolidayData.getInstance().loadHolidays();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

        try{
            BreakData.getInstance().loadBreaks();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    //ensures that the files are properly stored when they are edited or deleted from.
    @Override
    public void stop() throws Exception {
        try{
            HolidayData.getInstance().storeHolidays();
        }catch(IOException e){
            System.out.println(e.getMessage());
        }

        try{
            BreakData.getInstance().storeBreaks();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
