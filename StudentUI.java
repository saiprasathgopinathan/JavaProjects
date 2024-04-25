package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;

public class StudentUI extends Application
{
	Label errorlabel = new Label();
	Button dispbtn = new Button("Display");
	ListView<String> selectedCoursesList = new ListView<>(); // this is for displaying
	TextField nameField = new TextField();
	TextField addressField = new TextField();
	TextField cityField = new TextField();
	TextField provinceField = new TextField();
	TextField pincodeField = new TextField();
	TextField emailField = new TextField();
	TextField numberField = new TextField();
	RadioButton csebtn = new RadioButton("Computer Science");
	RadioButton businessbtn = new RadioButton("Business");
	Button addButton = new Button("Add Course");
	
	// this is for selecting
	ComboBox<String> courseList = new ComboBox<>();
	
	// for selecting only one Radio button
    ToggleGroup majorToggleGroup = new ToggleGroup();
	
	CheckBox volunteerchckbx = new CheckBox("Volunteer Work");
	CheckBox studentchckbx = new CheckBox("Student Council");
	
	TextArea displayArea = new TextArea();
	
	@Override
	public void start(Stage primaryStage)
	{
		courseList.setDisable(true);
	    csebtn.setToggleGroup(majorToggleGroup);
	    businessbtn.setToggleGroup(majorToggleGroup);
		
		GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(5);

		BorderPane borderpane = new BorderPane();
		VBox courseBox = new VBox();		
        courseBox.setPadding(new Insets(10));
        courseBox.setSpacing(5);

		gridPane.add(new Label("Full Name:"), 0, 0);
        gridPane.add(nameField, 1, 0);
        gridPane.add(new Label("Address:"), 0, 1);
        gridPane.add(addressField, 1, 1);
        gridPane.add(new Label("City:"), 0, 2);
        gridPane.add(cityField, 1, 2);
        gridPane.add(new Label("Province:"), 0, 3);
        gridPane.add(provinceField, 1, 3);
        gridPane.add(new Label("Postal Code:"), 0, 4);
        gridPane.add(pincodeField, 1, 4);
        gridPane.add(new Label("Email:"), 0, 6);
        gridPane.add(emailField, 1, 6);
        gridPane.add(new Label("Phone Number:"), 0, 5);
        gridPane.add(numberField, 1, 5);
        gridPane.add(businessbtn, 0, 7);
        gridPane.add(csebtn, 1, 7);
        gridPane.add(new Label("Courses:"), 0, 8);
        gridPane.add(courseList, 1, 8);
        gridPane.add(addButton, 2, 8);
        gridPane.add(volunteerchckbx, 0, 9);
        gridPane.add(studentchckbx, 1, 9);
        gridPane.add(dispbtn, 1, 10);
        borderpane.setCenter(gridPane);
        borderpane.setRight(selectedCoursesList);
        borderpane.setBottom(displayArea);
        
        majorToggleGroup.selectedToggleProperty().addListener((observable, oldVal, newVal) -> {
        	if(newVal == csebtn)
        	{
        		courseList.getItems().clear();
        		courseList.setDisable(false);
        		courseList.getItems().addAll("Java", "python", "C#");
        	}
        	else if(newVal == businessbtn)
        	{
        		courseList.getItems().clear();
        		courseList.setDisable(false);
        		courseList.getItems().addAll("Finance", "Administraction", "Cost");
        	}
        });
        
        addcourse();
        display();
        
        // Setting up the scene this is needed for Display
        Scene scene = new Scene(borderpane, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
	}
	
	public void addcourse()
	{
        addButton.setOnAction(event -> {
        	String selectedCourse = courseList.getSelectionModel().getSelectedItem();
        	if(selectedCourse != null && selectedCoursesList.getItems().contains(selectedCourse) == false)
        	{
        		selectedCoursesList.getItems().add(selectedCourse);
        	}
        });
	}
	
	public void validate()
	{		
		if(numberField.getText().matches(".*[a-zA-Z].*"))
		{
			displayArea.setText("Can't contain Alphabets. Please enter Numbers alone");
			return;
		}
		if(!emailField.getText().contains("@") || !emailField.getText().contains("."))
		{
			displayArea.setText("Please enter a valid emailID");
			return;
		}
	}
	
	public void display()
	{
        dispbtn.setOnAction(event -> {
        	
        	if(numberField.getText() != "")
        	{
        		validate();
        	}
        	
        	if(emailField.getText() != "")
        	{
        		System.out.println("Email is "+emailField.getText());
        		validate();
        	}
        	
        	StringBuilder inputDetails = new StringBuilder();
        	inputDetails.append("Full Name: ").append(nameField.getText()+"\n");
        	inputDetails.append("Address: ").append(addressField.getText()+"\n");
        	inputDetails.append("City: ").append(cityField.getText()+"\n");
        	inputDetails.append("Province: ").append(provinceField.getText()+"\n");
        	inputDetails.append("Postal Code: ").append(pincodeField.getText()+"\n");
        	inputDetails.append("Email: ").append(emailField.getText()+"\n");
        	inputDetails.append("PhoneNumber: ").append(numberField.getText()+"\n");
        	inputDetails.append("Courses: \n");
        	selectedCoursesList.getItems().forEach(course -> inputDetails.append(" - ").append(course+"\n"));
        	inputDetails.append("Volunteer Work").append(volunteerchckbx.isSelected() ? " - Yes\n" : " - No\n");
        	inputDetails.append("Student Council").append(studentchckbx.isSelected() ? " - Yes\n" : " - No\n");
        	displayArea.setText(inputDetails.toString()); // this only converts and displays
        });
	}
	
	public static void main(String args[])
	{
		launch(args);
	}
}
