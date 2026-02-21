package app;
import javax.swing.*;
import service.ProjectService;
import service.TaskService;
import ui.MainWindow;  

public class App {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}

            ProjectService projectService = new ProjectService();
            TaskService taskService = new TaskService();

            MainWindow window = new MainWindow(projectService, taskService);
            window.setVisible(true);
        });
    }
}