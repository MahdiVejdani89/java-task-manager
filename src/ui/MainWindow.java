package ui;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import javax.swing.*;
import model.Priority;
import model.Project;
import model.Status;
import model.Task;
import service.ProjectService;
import service.TaskService;

/**
 * Main application window using Swing.
 * This version includes improved layout, padding, borders, and nicer controls.
 */
public class MainWindow extends JFrame {

    // Core services that provide all project- and task-related business logic.
    // The UI never manipulates data directly; it always interacts through these services
    private final ProjectService projectService;
    private final TaskService taskService;

    // Model + view components for the project list.
    // The DefaultListModel holds the data, and the JList displays it.
    // Any change to the model automatically updates the UI.
    private final DefaultListModel<Project> projectListModel = new DefaultListModel<>();
    private final JList<Project> projectList = new JList<>(projectListModel);

    // same structure for the tasks
    private final DefaultListModel<Task> taskListModel = new DefaultListModel<>();
    private final JList<Task> taskList = new JList<>(taskListModel);

    // creating buttons
    private final JButton addProjectButton = new JButton("Add Project");
    private final JButton addTaskButton = new JButton("Add Task");
    private final JButton markDoneButton = new JButton("Mark Task as Done");

    // Auto increment for task-id
    private long nextTaskId = 1L;

    //Constructor
    public MainWindow(ProjectService projectService, TaskService taskService) {
        this.projectService = projectService; //getting the projectService
        this.taskService = taskService; //getting the taskService

        setTitle("Project Manager");
        setSize(900, 550);
        setLocationRelativeTo(null); // Opens the window in the middle of scrren
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        style();              // fonts, padding
        initUI();             // layout and panels
        initListeners();      // event handling
        loadProjects();       // load initial data
    }

    //Applies modern UI styling: font, padding, list spacing.
    private void style() {

        Font font = new Font("Segoe UI", Font.PLAIN, 14);

        projectList.setFont(font);
        taskList.setFont(font);

        addProjectButton.setFont(font);
        addTaskButton.setFont(font);
        markDoneButton.setFont(font);

        // padding inside lists
        projectList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        taskList.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    //Builds UI structure with panels and layout.
    private void initUI() {

        setLayout(new BorderLayout(10, 10)); // spacing between areas
        ((JComponent) getContentPane()).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // ---------------- Left Panel: Projects ----------------
        JPanel leftPanel = new JPanel(new BorderLayout(5, 5));
        leftPanel.setBorder(BorderFactory.createTitledBorder("Projects"));
        leftPanel.add(new JScrollPane(projectList), BorderLayout.CENTER);
        leftPanel.setPreferredSize(new Dimension(350, 500));

        // ---------------- Right Panel: Tasks ----------------
        JPanel rightPanel = new JPanel(new BorderLayout(5, 5));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Tasks"));
        rightPanel.add(new JScrollPane(taskList), BorderLayout.CENTER);

        // ---------------- Bottom Panel: Buttons ----------------
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        bottomPanel.add(addProjectButton);
        bottomPanel.add(addTaskButton);
        bottomPanel.add(markDoneButton);

        // Add components to main frame
        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    //Event Listeners for UI actions.
    private void initListeners() {

        // Click on a project → load its tasks
        projectList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                Project selectedProject = projectList.getSelectedValue();
                loadTasksForProject(selectedProject);
            }
        });

        addProjectButton.addActionListener(e -> onAddProject());

        
        addTaskButton.addActionListener(e -> onAddTask());
        markDoneButton.addActionListener(e -> onMarkTaskDone());
    }

    /**
     * Loads all projects into the UI.
     */
    private void loadProjects() {
        projectListModel.clear();
        List<Project> projects = projectService.getAllProjects();
        for (Project p : projects) {
            projectListModel.addElement(p);
        }
    }

    /**
     * Loads the tasks of the selected project.
     */
    private void loadTasksForProject(Project project) {
        taskListModel.clear();

        if (project == null) return;

        for (Task task : project.getTasks()) {
            taskListModel.addElement(task);
        }
    }

    /**
     * Add new Project (dialog prompt)
     */
    private void onAddProject() {
        String name = JOptionPane.showInputDialog(this, "Project name:");
        if (name == null || name.isBlank()) return;

        String description = JOptionPane.showInputDialog(this, "Project description:");
        if (description == null) description = "";

        Project project = projectService.createProject(name, description);
        projectListModel.addElement(project);

        projectList.setSelectedValue(project, true);
    }

    /**
     * Add new Task to selected project.
     */
    private void onAddTask() {

        Project project = projectList.getSelectedValue();

        if (project == null) {
            JOptionPane.showMessageDialog(this, "Select a project first!", "No Project", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String title = JOptionPane.showInputDialog(this, "Task title:");
        if (title == null || title.isBlank()) return;

        String description = JOptionPane.showInputDialog(this, "Task description:");
        if (description == null) description = "";

        String dateString = JOptionPane.showInputDialog(
            this,
            "Due date (YYYY-MM-DD):",
            LocalDate.now().plusDays(3).toString()
        );

        if (dateString == null || dateString.isBlank()) return;

        LocalDate dueDate;
        try {
            dueDate = LocalDate.parse(dateString);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ask Priority
        Priority priority = (Priority) JOptionPane.showInputDialog(
            this,
            "Select Priority:",
            "Task Priority",
            JOptionPane.QUESTION_MESSAGE,
            null,
            Priority.values(),
            Priority.MEDIUM
        );
        if (priority == null) return;

        // Ask Status
        Status status = (Status) JOptionPane.showInputDialog(
            this,
            "Select Status:",
            "Task Status",
            JOptionPane.QUESTION_MESSAGE,
            null,
            Status.values(),
            Status.TODO
        );
        if (status == null) return;

        Task task = new Task(
            nextTaskId++,
            title,
            description,
            status,
            priority,
            dueDate
        );

        taskService.addTaskToProject(project, task);
        loadTasksForProject(project);
    }

    /**
     * Mark task as DONE.
     */
    private void onMarkTaskDone() {

        Project project = projectList.getSelectedValue();
        Task task = taskList.getSelectedValue();

        if (project == null) {
            JOptionPane.showMessageDialog(this, "No project selected!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (task == null) {
            JOptionPane.showMessageDialog(this, "Select a task!", "Error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        taskService.markTaskAsDone(task);
        loadTasksForProject(project);
    }
}