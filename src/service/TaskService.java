package service;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import model.Priority;
import model.Project;
import model.Status;
import model.Task;

public class TaskService {

    //Adding a new task to the Project
    public void addTaskToProject(Project project, Task task){
        project.addTask(task);
    }

    // find a task by its id, otherwise return null
    public Task findTaskById(Project project, long taskId){
        for(Task task : project.getTasks()){
            if(task.getId() == taskId){
                return task;
            }
        }
        return null;
    }

    // marking a task as done
    public void markTaskAsDone(Task task){
        task.setStatus(Status.DONE);
    }

    //Filters tasks by their status (todo, in progress, done).
    public List<Task> filterByStatus(Project project, Status status) {
        List<Task> result = new ArrayList<>();
        for (Task task : project.getTasks()) {
            if (task.getStatus() == status) {
                result.add(task);
            }
        }
        return result;
    }

    //Filters tasks by their priority (low, medium, high).
    public List<Task> filterByPriority(Project project, Priority priority) {
        List<Task> result = new ArrayList<>();
        for (Task task : project.getTasks()) {
            if (task.getPriority() == priority) {
                result.add(task);
            }
        }
        return result;
    }

    //tasks that their deadline is before today and not done.
    public List<Task> getOverdueTasks(Project project) {
        LocalDate today = LocalDate.now();
        List<Task> result = new ArrayList<>();

        for (Task task : project.getTasks()) {
            if (task.getDate().isBefore(today) &&
                task.getStatus() != Status.DONE) {
                result.add(task);
            }
        }
        return result;
    }

    // Sorting the task by their deadline
    public List<Task> sortTasksByDeadline(Project project) {
        List<Task> sorted = new ArrayList<>(project.getTasks());
        sorted.sort(Comparator.comparing(Task::getDate));
        return sorted;
    }
}
