package apps;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class mediaplayer extends Application 
{
    BorderPane borderpane = new BorderPane();
    MenuBar menuBar = new MenuBar();
    GridPane gridpane = new GridPane();
    File file;
    MediaPlayer mediaPlayer;
    String path;
    Slider volumeSlider = new Slider();

    @Override
    public void start(Stage primaryStage) 
    {
        // Create menus and menu items
        Menu fileMenu = new Menu("File");
        MenuItem openMenuItem = new MenuItem("Open");
        openMenuItem.setOnAction(event -> 
        {
        	FileChooser chooser = new FileChooser();
        	chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("video files", "*.mp4"));
        	file = chooser.showOpenDialog(null);
        	path = file.getAbsolutePath();
        	if(file != null)
        	{
        		mediaPlayer = new MediaPlayer(new Media(file.toURI().toString()));
                MediaView mediaView = new MediaView(mediaPlayer);       
                borderpane.setCenter(mediaView);
        	}
        });

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(event -> 
        {
        	Platform.exit();
        });

        // Add menu items to menu
        fileMenu.getItems().addAll(openMenuItem, exitMenuItem);
        menuBar.getMenus().addAll(fileMenu);
        borderpane.setTop(menuBar);

        Button playButton = new Button("Play");
        playButton.setOnAction(event -> 
        {
        	if(mediaPlayer != null)
        	{
                if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) 
                {
                    mediaPlayer.pause();
                    playButton.setText("Play");
                } 
                else 
                {
                    mediaPlayer.play();
                    playButton.setText("Pause");
                }        		
        	}
        });
        gridpane.setHgap(5); // Adjust the gap as needed
        
        Button forwardButton = new Button("->");
        forwardButton.setOnAction(event -> 
        {
        	mediaPlayer.seek(mediaPlayer.getCurrentTime().add(javafx.util.Duration.seconds(5)));
        });

        Button backwardButton = new Button("<-");
        backwardButton.setOnAction(event -> 
        {
        	if(mediaPlayer.getCurrentTime().toSeconds() > 5)
        	{
        		mediaPlayer.seek(mediaPlayer.getCurrentTime().subtract(javafx.util.Duration.seconds(5)));
        	}
        });
        
        Button speedupbtn = new Button("SpeedUp");
        speedupbtn.setOnAction(evet -> {
        	mediaPlayer.setRate(1.5);
        });

        Button slowdownbtn = new Button("SlowDown");
        slowdownbtn.setOnAction(evet -> {
        	mediaPlayer.setRate(0.75);
        });

        Button normalbtn = new Button("Normalize");
        normalbtn.setOnAction(evet -> {
        	mediaPlayer.setRate(1.00);
        });
        volumeSlider.setMin(0.0*100);
        volumeSlider.setMax(1.0*100);
        volumeSlider.setValue(0.5*100); // Set an initial volume value, for example, 0.5 (50%)

        // Add a listener to the slider's value property
        volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (mediaPlayer != null) {
                mediaPlayer.setVolume(newValue.doubleValue()*100);
            }
        });
        
        gridpane.add(forwardButton, 2, 0);
        gridpane.add(playButton, 1, 0);
        gridpane.add(backwardButton, 0, 0);
        gridpane.add(speedupbtn, 3, 0);
        gridpane.add(normalbtn, 4, 0);
        gridpane.add(slowdownbtn, 5, 0);
        gridpane.add(volumeSlider, 6, 0);
        borderpane.setBottom(gridpane);

        // Additional setup for your application
        Scene scene = new Scene(borderpane, 800, 900);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Media Player");
        primaryStage.show();        
    }

    public static void main(String args[]) 
    {
        launch(args);
    }
}
