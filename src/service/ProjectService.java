package service;
import java.util.ArrayList;
import java.util.List;
import model.Project;


public class ProjectService {

    // Internal list of all projects
    private final List<Project> projects = new ArrayList<>();

    // Simple ID generator for projects (auto-increment)
    private long nextProjectId = 1L;

    //Creates a new project, assigns an auto-generated ID
    public Project createProject(String name, String description) {
        Project project = new Project(nextProjectId, name, description);
        nextProjectId++;
        projects.add(project);
        return project;
    }

    //Adds an existing project object to the list.
    public void addProject(Project project) {
        // Ensure that no duplicate ID exists (very simple check)
        if (findProjectById(project.getId()) != null) {
            throw new IllegalArgumentException(
                    "A project with ID " + project.getId() + " already exists.");
        }
        projects.add(project);

        // Optional: adjust nextProjectId if needed
        if (project.getId() >= nextProjectId) {
            nextProjectId = project.getId() + 1;
        }
    }

    //Returns all projects.
    public List<Project> getAllProjects() {
        return new ArrayList<>(projects); // return a copy to avoid external modification
    }

    //Finds a project by its ID.
    public Project findProjectById(long projectId) {
        for (Project project : projects) {
            if (project.getId() == projectId) {
                return project;
            }
        }
        return null;
    }

    //Removes a project by ID.
    public boolean removeProjectById(long projectId) {
        return projects.removeIf(project -> project.getId() == projectId);
    }

    //Finds all projects whose name contains the given search term (case-insensitive).
    public List<Project> findProjectsByName(String namePart) {
        List<Project> result = new ArrayList<>();
        if (namePart == null || namePart.isBlank()) {
            return result;
        }

        String lower = namePart.toLowerCase();

        for (Project project : projects) {
            if (project.getName() != null &&
                project.getName().toLowerCase().contains(lower)) {
                result.add(project);
            }
        }
        return result;
    }
}
