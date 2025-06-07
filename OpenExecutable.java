import java.io.IOException;

public class OpenExecutable {
    public static void main(String[] args) {
        // Correctly specify the application path without extra quotes
        String applicationPath = "C:\\Windows\\notepad.exe";

        try {
            // Execute the application
            Process process = Runtime.getRuntime().exec(applicationPath);
            process.waitFor(); // Optional: Wait for the process to complete
        } catch (IOException | InterruptedException e) {
            System.out.println("Error opening application: " + e.getMessage());
        }
    }
}