package apps;

import java.sql.*;
import javafx.application.Application;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;

public class jd_trial extends Application 
{
	GridPane gridpane = new GridPane();
	Button addbutton = new Button("Add to db");
	Button fetchbutton = new Button("Fetch from db");
	Button updatebutton = new Button("Update in db");
	Button deletebutton = new Button("Delete from db");
	static TextField nameField = new TextField("Name");
	static TextField cityField = new TextField("City");
	static TextField addressField = new TextField("Address");
	static TextField mobileField = new TextField("PhoneNo");
	// http://localhost/phpmyadmin/index.php?route=/sql&db=testdb&table=employee&pos=0
	@Override
	public void start(Stage primaryStage)
	{
		addbutton.setOnAction(event -> {
			connect();
		});
		fetchbutton.setOnAction(event -> {
			fetch();
		});
		updatebutton.setOnAction(event -> {
			update();
		});

		deletebutton.setOnAction(event -> {
			delete();
		});
		
		gridpane.setHgap(0); // Adjust the gap as needed
		gridpane.setVgap(3); // Adjust the gap as needed
		gridpane.add(nameField, 0, 0, 10, 1);
		gridpane.add(cityField, 0, 1, 10, 1);
		gridpane.add(addressField, 0, 2, 10, 1);
		gridpane.add(mobileField, 0, 3, 10, 1);
		gridpane.add(addbutton, 0, 4);
		
		gridpane.add(fetchbutton, 1, 4);
		gridpane.add(updatebutton, 2, 4);
		gridpane.add(deletebutton, 3, 4);
        
		Scene scene = new Scene(gridpane, 500, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Adding Details to DB");
        primaryStage.show();
	}
	
	public void delete() 
	{
        Connection con = null;
        PreparedStatement ps = null;
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "");
            System.out.println("Success");
            String sql = "DELETE FROM EMPLOYEE WHERE Name = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, nameField.getText());
            int i = ps.executeUpdate();
            if(i > 0)
            {
            	System.out.println("Successfully Deleted");
            }
        }
        catch(Exception e)
        {
        	System.out.println("Exception: "+e.getMessage());
        }
	}

	public void update() 
	{
        Connection con = null;
        PreparedStatement ps = null;
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "");
            System.out.println("Success");
            String sql = "UPDATE employee SET MobileNo='"+mobileField.getText()+"', City='"+cityField.getText()+"', Address='"+addressField.getText()
            +"'WHERE Name = '"+nameField.getText()+"'";
            ps = con.prepareStatement(sql);
            int i = ps.executeUpdate(); // to retrieve data from the DB
            if(i > 0)
            {
            	System.out.println("Sucessfully updated");
            }
            
            con.close();
            ps.close();
        }
        catch(Exception e)
        {
        	System.out.println("Exception: "+e.getMessage());
        }
	}
	public static void fetch()
	{
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "");
            System.out.println("Success");
            String sql = "SELECT * FROM employee WHERE Name = ?";
            ps = con.prepareStatement(sql);
            ps.setString(1, nameField.getText());
            rs = ps.executeQuery(); // to retrieve data from the DB
            if(rs.next())
            {
            	mobileField.setText(rs.getString("MobileNo"));
            	cityField.setText(rs.getString("City"));
            	addressField.setText(rs.getString("Address"));
            }
            
            con.close();
            ps.close();
        }
        catch(Exception e)
        {
        	System.out.println("Exception: "+e.getMessage());
        }
	}
	
	public static void connect()
	{
        Connection con = null;
        PreparedStatement ps = null;
        try 
        {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/testdb", "root", "");
            System.out.println("Success");
            String sql = "INSERT INTO employee(name, city, address, mobileno) VALUES(? ,? ,? ,?)";
            ps = con.prepareStatement(sql);
            ps.setString(1, nameField.getText());
            ps.setString(2, cityField.getText());
            ps.setString(3, addressField.getText());
            ps.setString(4, mobileField.getText());
            int i = ps.executeUpdate();
            if(i > 0)
            {
            	System.out.println("record added successfully");
            }
            con.close();
            ps.close();
        }
        catch(Exception e)
        {
        	System.out.println("Exception: "+e.getMessage());
        }
	}
	
    public static void main(String[] args) 
    {
    	launch(args);
    }
}
