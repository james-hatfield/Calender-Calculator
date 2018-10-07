package sample;

import java.time.LocalDate;
import java.util.Objects;

//This class is used for the parser to store the dates and totalDaysPaid into
//for calculation later. The equals comparator is overridden to only compare totalDaysPaid
//because all employees who have the same totalDaysPaid have the same start and end dates. This means
//that there is a lot of duplicate information, so I compared each job that was being parsed with a job that
//was already stored in the set and if they were different, it was able to be stored into the set.
public class Job {
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate nextYearStart;
    private LocalDate nextYearEnd;
    private int totalDaysPaid;

    public Job(){
        this.startDate = null;
        this.endDate = null;
        this.nextYearStart = null;
        this.nextYearEnd = null;
        this.totalDaysPaid = 0;
    }

    public Job(Job another){
        this.startDate = another.startDate;
        this.endDate = another.endDate;
        this.totalDaysPaid = another.totalDaysPaid;
        this.nextYearStart = another.nextYearStart;
        this.nextYearEnd = another.nextYearEnd;
    }

    public void setNextYearStart(LocalDate nextYearStart) {
        this.nextYearStart = nextYearStart;
    }

    public void setNextYearEnd(LocalDate nextYearEnd) {
        this.nextYearEnd = nextYearEnd;
    }

    public LocalDate getNextYearStart() {
        return nextYearStart;
    }

    public LocalDate getNextYearEnd() {
        return nextYearEnd;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public int getTotalDaysPaid() {
        return totalDaysPaid;
    }

    public void setTotalDaysPaid(int totalDaysPaid) {
        this.totalDaysPaid = totalDaysPaid;
    }

    @Override
    public String toString() {
        return "startDate: " + startDate + "\n"+
                "endDate: " + endDate + "\n" +
                "nextYearStart " + nextYearStart + "\n" +
                "nextYearEnd " + nextYearEnd + "\n"+
                "totalDaysPaid: " + totalDaysPaid + "\n";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Job)) return false;
        Job newJob = (Job) o;
        return totalDaysPaid == newJob.totalDaysPaid;
    }

    @Override
    public int hashCode() {

        return Objects.hash(startDate, endDate, totalDaysPaid, nextYearStart, nextYearEnd);
    }
}
