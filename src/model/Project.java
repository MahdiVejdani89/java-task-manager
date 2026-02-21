package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Project {

    // ------------------------------ Variables ------------------------------
    private long id;
    private String name;
    private String description;
    private final List<Task> tasks = new ArrayList<>();

    // ------------------------------ Constructor ------------------------------
    public Project(long id, String name, String descString){
        this.id = id;
        this.name = name;
        this.description = descString;
    }

    // ------------------------------ getters & Setters ------------------------------
    public long getId(){
        return id;
    }

    public void setId(long id){     //Change project id
        this.id = id;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){   //Update project name
        this.name = name;
    }

    public String getDescription(){
        return description;
    }

    public void setDescription(String description){     //update project description
        this.description = description;
    }

    // ------------------------------ Task management ------------------------------

    // adding a Task
    public void addTask(Task task){
        tasks.add(task);
    }

    //remove a task by its id
    public void removeTaskById(long taskId){
        tasks.removeIf(task -> task.getId() == taskId);
    }

    public List<Task> getTasks(){
        return Collections.unmodifiableList(tasks);
    }

    // ------------------------------ Create a toString() methode ------------------------------
    @Override
    public String toString() {
        return "Id:" + id +
                ", Name: " + name +
                ", Number of tasks: " + tasks.size();
    }
}
