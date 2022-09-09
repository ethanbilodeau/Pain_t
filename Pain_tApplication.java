package com.example.pain_t;


//imported classes
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;

import java.io.*;

public class Pain_tApplication extends Application
{
    ImageView myImageView;  //imageView object used to open image and display in window

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException
    {
        stage.setTitle("Pain_t");       //window title

        MenuBar menuBar = new MenuBar();    //menu bar object to hold menu bar

        Menu File = new Menu("File");    //first menu bar item
        menuBar.getMenus().add(File);       //adding to menu bar

        MenuItem openImg = new MenuItem("Open Image");  //sub menus to add to File
        MenuItem save = new MenuItem("Save");
        MenuItem saveAs = new MenuItem("Save As");
        MenuItem close = new MenuItem("Close Program");

        File.getItems().add(openImg);       //adding to File
        File.getItems().add(save);
        File.getItems().add(saveAs);
        File.getItems().add(close);

        openImg.setOnAction(openImageEventListener);    //calling event handler to open image
        saveAs.setOnAction(saveAsEventListener);        //calling event handler to save image to new file
        close.setOnAction(closeEventListener);        //calling event handler to close program

        myImageView = new ImageView();                  //new image view object
        VBox root = new VBox();                            //vbox to put image and menubar into
        root.getChildren().addAll(menuBar,myImageView);
        Scene scene = new Scene(root, 900,600);     //creating scene and defining size, adding vbox into it

        stage.setScene(scene);
        stage.show();                   //finalizing and showing application
    }

    EventHandler<ActionEvent> openImageEventListener           //calling open image handle function
            = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent t)
        {
            //new file explorer object
            FileChooser fileChooser = new FileChooser();
            //Set extension filter
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
            //Show open file dialog
            File file = fileChooser.showOpenDialog(null);
            String path = file.getAbsolutePath();
            //label.setText(path);
            try {
                //taking input file and making it into imageview object myImageView
                InputStream inputStream = new FileInputStream(path);
                Image image = new Image(inputStream);
                myImageView.setImage(image);
            } catch (FileNotFoundException ex) {
                System.out.println("none found");       //catch if file could not upload
            }
        }
    };

    EventHandler<ActionEvent> saveAsEventListener       //calling open saveAs handle function
            = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent t)//does not work
        {
            FileChooser fileExplorer = new FileChooser();       //creating new file explorer object
            fileExplorer.setTitle("Save Image");

            File file = fileExplorer.showSaveDialog(null);      //making fieExplore object a save file window
            if (file != null) {
                try {
                    ImageIO.write(SwingFXUtils.fromFXImage(myImageView.getImage(),      //writing image as png
                            null), "png", file);
                } catch (IOException ex) {
                    System.out.println("can not be saved");
                }
            }
        }
    };

    EventHandler<ActionEvent> closeEventListener       //calling close application handle function
            = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent t)
        {
            try {
                System.exit(0);     //closing program
            } catch (Exception e) {
                System.out.println("This program is unstoppable");      //program has become sentient and will not let you close it
            }
        }
    };
}