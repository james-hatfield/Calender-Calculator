package sample;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import java.time.LocalDate;

public class Holiday {
    private SimpleStringProperty name = new SimpleStringProperty();
    private ObjectProperty<LocalDate> date;


    public Holiday(String name, LocalDate date) {
        this.name.set(name);
        this.date = new SimpleObjectProperty<>(date);
    }

    public String getName() {
        return name.get();
    }


    public LocalDate getDate() {
        return date.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    @Override
    public String toString() {
        return name + date.toString();
    }
}
