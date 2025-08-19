/**
 * Project: Implement a Data-Driven Security Tool Tracker
 * 
 * This project aims to create a Java-based system that allows security professionals to track and manage various security tools 
 * and their configurations. The system will be data-driven, meaning it will rely on a database to store and retrieve information.
 * 
 * The system will have the following features:
 * 1. Tool Management: Ability to add, remove, and update security tools and their configurations.
 * 2. Configuration Management: Ability to track and manage configuration changes for each tool.
 * 3. Alerting System: Ability to set up alerts for configuration changes, tool updates, and other security-related events.
 * 4. Reporting: Ability to generate reports on tool usage, configuration changes, and alert history.
 * 
 * This project file, 2g74_implement_a_dat.java, will serve as the main entry point for the system.
 * 
 */

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ToolTracker {
    private Connection conn;

    public ToolTracker() {
        // Initialize database connection
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/security_tool_db", "root", "password");
        } catch (ClassNotFoundException | SQLException e) {
            System.err.println("Error initializing database connection: " + e.getMessage());
        }
    }

    public void addTool(String toolName, String toolConfig) {
        // Add new tool to the database
        String query = "INSERT INTO tools (name, config) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, toolName);
            pstmt.setString(2, toolConfig);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error adding tool: " + e.getMessage());
        }
    }

    public void updateToolConfig(String toolName, String newConfig) {
        // Update tool configuration in the database
        String query = "UPDATE tools SET config = ? WHERE name = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, newConfig);
            pstmt.setString(2, toolName);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error updating tool configuration: " + e.getMessage());
        }
    }

    public List<String> getTools() {
        // Retrieve a list of all tools from the database
        List<String> tools = new ArrayList<>();
        String query = "SELECT name FROM tools";
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                tools.add(rs.getString("name"));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving tools: " + e.getMessage());
        }
        return tools;
    }

    public static void main(String[] args) {
        ToolTracker tracker = new ToolTracker();
        tracker.addTool("Nmap", "default config");
        tracker.updateToolConfig("Nmap", "updated config");
        List<String> tools = tracker.getTools();
        for (String tool : tools) {
            System.out.println("Tool: " + tool);
        }
    }
}