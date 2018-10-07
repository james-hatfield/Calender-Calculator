package sample;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

import java.time.LocalDate;


//This class is used for storing break objects. Designed to hold
//two dates, the beginning date and the ending date. The calculator
//will exclude all dates including and inbetween the start and end date.
public class Break {
    private SimpleStringProperty name = new SimpleStringProperty();
    private ObjectProperty<LocalDate> breakStart;
    private ObjectProperty<LocalDate> breakEnd;


    public Break(String name, LocalDate breakStart, LocalDate breakEnd) {
        this.name.set(name);
        this.breakStart = new SimpleObjectProperty<>(breakStart);
        this.breakEnd = new SimpleObjectProperty<>(breakEnd);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public LocalDate getBreakStart() {
        return breakStart.get();
    }

    public void setBreakStart(LocalDate breakStart) {
        this.breakStart.set(breakStart);
    }

    public LocalDate getBreakEnd() {
        return breakEnd.get();
    }


    public void setBreakEnd(LocalDate breakEnd) {
        this.breakEnd.set(breakEnd);
    }
}
