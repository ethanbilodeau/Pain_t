package com.example.pain_t;


//imported classes
import javafx.application.Application;
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

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.RenderedImage;
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

        File.getItems().add(openImg);       //adding to File
        File.getItems().add(save);
        File.getItems().add(saveAs);

        openImg.setOnAction(openImageEventListener);    //calling event handler to open image
        saveAs.setOnAction(saveAsEventListener);        //calling event handler to save image to new file

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

    EventHandler<ActionEvent> saveAsEventListener       //calling open image handle function-does not work
            = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent t)//does not work
        {
            File saveFile = new File("~ethan/Desktop/saved_image.png");
            try
            {
                //BufferedImage bi = myImageView;
                File outputfile = new File("~/ethan/Desktop/saved_image.png");
                ImageIO.write((RenderedImage) myImageView, "png", (ImageOutputStream) outputfile);
            }
            catch(IOException e)
            {
                System.out.println("could not save");
            }
        }
    };
}