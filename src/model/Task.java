package model;

import java.time.LocalDate;

public class Task {

    // ------------------------------ Variables ------------------------------
    private long id;
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private LocalDate date;

    // ------------------------------ Constructor ------------------------------
    public Task(long id, String title, String description, Status status, Priority priority, LocalDate date){

        this.id = id;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.date = date;


    }

    // ------------------------------ getters & Setters ------------------------------
    public long getId(){
        return id;
    }

    public void setId(long id){
        this.id = id;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public Status getStatus(){
        return status;
    }
    
    // Set the Status
    public void setStatus(Status status){
        this.status = status;
    }

    public Priority getPriority(){
        return priority;
    }

    public void setPriority(Priority priority){
        this.priority = priority;
    }

    public LocalDate getDate(){
        return date;
    }

    public void setDate(LocalDate date){
        this.date = date;
    }

    // ------------------------------ Create a toString() methode ------------------------------
    @Override

    public String toString(){
        return "Id: " + id +
                ", Title: " + title +
                ", Description: " + description + 
                ", Status: " + status +
                ", Priority: " + priority +
                ", Deadline: " + date;
    }
}
