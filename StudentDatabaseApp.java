package application;

import javafx.application.Application;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.*;

public class StudentDatabaseApp extends Application {

    // Database connection parameters
    static final String JDBC_URL = "XXX";
    static final String USERNAME = "XXX";
    static final String PASSWORD = "XXX";

    // JavaFX components
    private TextField idTextField;
    private TextField nameTextField;
    private TextField feesTextField;
    private TextField DiscountTextField;
    private TableView<Student> tableView;
    private ObservableList<Student> studentList;
    private Button connectButton;
    private Button displayButton;
    private Button resetButton;
    private Button deleteButton;
    private Button updateButton;
    private Button insertButton;
    private Button applyDiscountButton;
    private Button quitButton;

    @Override
    public void start(Stage primaryStage) 
    {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label idLabel = new Label("Student's Id No:");
        idTextField = new TextField();
        Label nameLabel = new Label("Student's Name:");
        nameTextField = new TextField();
        Label feesLabel = new Label("Student's Tuition Fees:");
        feesTextField = new TextField();
        Label discountLabel = new Label("Student's Discount:");
        DiscountTextField = new TextField();


        tableView = new TableView<>();
        studentList = FXCollections.observableArrayList();
        tableView.setItems(studentList);

        TableColumn<Student, Integer> idColumn = new TableColumn<>("Student Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Student, Double> feesColumn = new TableColumn<>("Tuition Fees");
        feesColumn.setCellValueFactory(new PropertyValueFactory<>("fees"));

        TableColumn<Student, Double> discountColumn = new TableColumn<>("Discount");
        discountColumn.setCellValueFactory(new PropertyValueFactory<>("discount"));

        tableView.getColumns().addAll(idColumn, nameColumn, feesColumn, discountColumn);

        connectButton = new Button("Connect");
        connectButton.setOnAction(e -> connectToDatabase());

        displayButton = new Button("Display Records");
        displayButton.setOnAction(e -> displayRecord());

        resetButton = new Button("Reset");
        resetButton.setOnAction(e -> resetFields());

        deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> deleteRecord());

        updateButton = new Button("Update Tuition Fees");
        updateButton.setOnAction(e -> updateFees());

        insertButton = new Button("Insert Data");
        insertButton.setOnAction(e -> insertData());

        applyDiscountButton = new Button("Apply Discount");
        applyDiscountButton.setOnAction(e -> applyDiscount());

        quitButton = new Button("Quit");
        quitButton.setOnAction(e -> quitApplication());

        gridPane.add(idLabel, 0, 0);
        gridPane.add(idTextField, 1, 0);

        gridPane.add(nameLabel, 0, 1);
        gridPane.add(nameTextField, 1, 1);

        gridPane.add(feesLabel, 0, 2);
        gridPane.add(feesTextField, 1, 2);

        gridPane.add(discountLabel, 0, 3);
        gridPane.add(DiscountTextField, 1, 3);

        gridPane.add(connectButton, 0, 4);
        gridPane.add(displayButton, 1, 4);
        gridPane.add(resetButton, 0, 5);
        gridPane.add(deleteButton, 1, 5);
        gridPane.add(updateButton, 0, 6);
        gridPane.add(insertButton, 1, 6);
        gridPane.add(applyDiscountButton, 0, 7);
        gridPane.add(quitButton, 0, 8);

        Scene scene = new Scene(gridPane, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Student Database Application");
        primaryStage.show();
    }

    private void connectToDatabase() 
    {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            dropExistingTable(connection); // Drop existing table if it exists
            createStudentTable(connection);
            showAlert(Alert.AlertType.INFORMATION, "Connection Status", null, "Connected to the Database. Student table created.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void dropExistingTable(Connection connection) throws SQLException 
    {
        String dropTableSQL = "DROP TABLE Student";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(dropTableSQL);
            System.out.println("Existing Student table dropped.");
        } catch (SQLException e) {
            // Ignore if the table doesn't exist
            if (!e.getSQLState().equals("42Y55")) {
                throw e;
            }
        }
    }

    private void createStudentTable(Connection connection) throws SQLException 
    {
        String createTableSQL = "CREATE TABLE Student (" +
                "StId NUMBER PRIMARY KEY, " +
                "StName VARCHAR2(100), " +
                "StFees NUMBER)";
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createTableSQL);
            System.out.println("Student table created successfully.");
        }
    }

    private void insertData() 
    {
        String id = idTextField.getText();
        String name = nameTextField.getText();
        String fees = feesTextField.getText();
        String disc = DiscountTextField.getText();

        if (id.isEmpty() || name.isEmpty() || fees.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Please fill in all fields.");
            return;
        }

        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String insertSQL = "INSERT INTO Student (StId, StName, StFees) VALUES (?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setString(1, id);
                preparedStatement.setString(2, name);
                preparedStatement.setString(3, fees);
                preparedStatement.executeUpdate();
                showAlert(Alert.AlertType.INFORMATION, "Success", "Data Inserted", "Data inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", null, "Failed to insert data.");
        }
    }

    private void displayRecord() 
    {
    	int id;
    	String name;
    	double fees;
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) 
        {
            String selectSQL = "SELECT * FROM Student";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(selectSQL)) 
            {
                Stage stage = new Stage();
                TableView<Student> tableView = new TableView<>();

                TableColumn<Student, Integer> idColumn = new TableColumn<>("Student Id");
                idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

                TableColumn<Student, String> nameColumn = new TableColumn<>("Name");
                nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

                TableColumn<Student, Double> feesColumn = new TableColumn<>("Tuition Fees");
                feesColumn.setCellValueFactory(new PropertyValueFactory<>("fees"));

                tableView.getColumns().addAll(idColumn, nameColumn, feesColumn);

                ObservableList<Student> studentList = FXCollections.observableArrayList();

                while (resultSet.next()) 
                {
                    id = resultSet.getInt("StId");
                    name = resultSet.getString("StName");
                    fees = resultSet.getDouble("StFees");
                    studentList.add(new Student(id, name, fees));
                }
                                
                tableView.setItems(studentList);
                VBox vbox = new VBox(tableView);
                vbox.setPadding(new Insets(10));
                Scene scene = new Scene(vbox);
                stage.setScene(scene);
                stage.setTitle("Student Records");
                stage.show();
            }
        } 
        catch (SQLException e) 
        {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", null, "Failed to fetch data.");
        }
    }

    private void resetFields() 
    {
        idTextField.clear();
        nameTextField.clear();
        feesTextField.clear();
        DiscountTextField.clear();
    }

    private void deleteRecord() 
    {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String id = idTextField.getText();
            String deleteSQL = "DELETE FROM Student WHERE StId = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {
                preparedStatement.setString(1, id);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Record Deleted", "Record deleted successfully.");
                    resetFields();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Deletion Failed", "No record found for the given Student Id.");
                }
            }
        } catch (SQLException e) 
        {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", null, "Failed to delete record.");
        }
    }

    private void updateFees() 
    {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String id = idTextField.getText();
            String newFees = feesTextField.getText();
            String updateSQL = "UPDATE Student SET StFees = ? WHERE StId = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                preparedStatement.setString(1, newFees);
                preparedStatement.setString(2, id);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    showAlert(Alert.AlertType.INFORMATION, "Success", "Fees Updated", "Tuition fees updated successfully.");
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Update Failed", "No record found for the given Student Id.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", null, "Failed to update fees.");
        }
    }

    private void applyDiscount() 
    {
        try {
            int id = Integer.parseInt(idTextField.getText());
            double originalFees = Double.parseDouble(feesTextField.getText());
            double discount = Double.parseDouble(DiscountTextField.getText());
            double discountedFees = calculateDiscountedFees(originalFees, discount);
            updateFeesInDatabase(id, discountedFees);
            showAlert(Alert.AlertType.INFORMATION, "Success", "Discount Applied", "Discount applied successfully.");
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.ERROR, "Error", null, "Invalid fee or discount value.");
        }
    }

    private double calculateDiscountedFees(double originalFees, double discount) 
    {
        // Ensure discount is not greater than original fees
        if (discount >= originalFees) {
            showAlert(Alert.AlertType.WARNING, "Warning", null, "Discount cannot exceed or equal original fees.");
            return originalFees;
        }
        // Calculate discounted fees
        return originalFees - discount;
    }

    private void updateFeesInDatabase(int id, double newFees) 
    {
        try (Connection connection = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD)) {
            String updateSQL = "UPDATE Student SET StFees = ? WHERE StId = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                preparedStatement.setDouble(1, newFees);
                preparedStatement.setInt(2, id);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    displayRecord();
                } else {
                    showAlert(Alert.AlertType.ERROR, "Error", "Update Failed", "No record found for the given Student Id.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", null, "Failed to update fees in the database.");
        }
    }

    private void quitApplication() 
    {
        Platform.exit();
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) 
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) 
    {
        launch(args);
    }
}
