package application;
import javafx.application.Application;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.sql.*;

public class Classname extends Application 
{
    private TextArea textArea;
    @Override
    public void start(Stage primaryStage) 
    {
        primaryStage.setTitle("Student Info");
        Label cityLabel = new Label("City:");
        TextField cityField = new TextField();
        Button displayButton = new Button("Display");
        displayButton.setPrefWidth(280);
        displayButton.setPrefHeight(30);
        displayButton.setOnAction(e -> displayStudents(cityField.getText()));
        
        textArea = new TextArea();
        textArea.setEditable(false);
        textArea.setPrefHeight(200);
        
        Region spacer = new Region(); 
        spacer.setPrefHeight(20);  
        
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(80);
        grid.setVgap(5);
        
        grid.add(cityLabel, 0, 0);
        grid.add(cityField, 1, 0);
        grid.add(displayButton, 1, 1);
        grid.add(spacer, 0, 4); 
        grid.add(textArea, 0, 5, 2, 2);

        BorderPane root = new BorderPane();
        root.setCenter(grid);   
        Scene scene = new Scene(root, 500, 300);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    //Method for displaying students from DB
    private void displayStudents(String city) 
    {
        textArea.clear();
        try 
        {
            Connection conn = DriverManager.getConnection("XXX","XXX", "XXX");
            PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Students WHERE city = ?"); //city input from UI 
            stmt.setString(1, city);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            { 
            	//formatting display using \t and \n
                textArea.appendText(rs.getString("studentID") + "\t" +
                        rs.getString("firstName") + "\t" +
                        rs.getString("lastName") + "\t" +
                        rs.getString("address") + "\t" +
                        rs.getString("city") + "\t" +
                        rs.getString("province") + "\t" +
                        rs.getString("postalCode") + "\n");
            }

            if (textArea.getText().isEmpty()) 
            { 
            	//when data is not found
            	Alert alert = new Alert(Alert.AlertType.INFORMATION);
            	alert.setTitle("Information Dialog");
            	alert.setHeaderText(null);
            	alert.setContentText("NO DATA FOUND for the City you entered");
            	alert.showAndWait();
            }
            rs.close();
            stmt.close();
            conn.close();
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) 
    {
    	launch(args);
    }
}
