package com.example.pain_t;


//imported classes
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.embed.swing.SwingFXUtils;

import javax.imageio.ImageIO;

import java.io.*;

import static javafx.scene.paint.Color.*;

public class Pain_tApplication extends Application
{
    ImageView myImageView;  //imageView object used to open image and display in window
    Canvas can;             //canvas object to be referenced throughout program
    StackPane stPane;       //stack pane object that combines canvas and imageview objects
    Label label;            //label used for display of line weight
    double lineWeight;      //value of label used to modify line in function
    ColorPicker colorPicker;    //color picker object
    Image image;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException
    {
        stage.setTitle("Pain_t");       //window title
        MenuBar menuBar = new MenuBar();    //menu bar object to hold menu bar

        Menu File = new Menu("File");    //File menu bar item
        Menu Edit = new Menu("Edit");    //Edit menu bar item
        Menu Help = new Menu("Help");   //Help menu bar item

        menuBar.getMenus().add(File);       //adding menus to menu bar
        menuBar.getMenus().add(Edit);
        menuBar.getMenus().add(Help);

        MenuItem openImg = new MenuItem("Open Image");  //sub menus to add to File
        MenuItem save = new MenuItem("Save");
        MenuItem saveAs = new MenuItem("Save As");
        MenuItem quit = new MenuItem("Quit Program");

        File.getItems().add(openImg);       //adding to File
        File.getItems().add(save);
        File.getItems().add(saveAs);
        File.getItems().add(quit);

        MenuItem color = new MenuItem("Color Picker");  //sub menus to add to Edit
        Menu weight = new Menu("Line Weight");
        MenuItem draw = new MenuItem("Draw Line");
        Menu Line = new Menu("Line");

        Slider weightSlide = new Slider(0,100,12);      //slider used in line weight chooser
        label = new Label("12");
        weightSlide.valueProperty().addListener(new ChangeListener<Number>()    //function updates labeled value based on slider position
        {@Override
        public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
            label.textProperty().setValue(String.valueOf(newValue.intValue()));}});
        label.textProperty().setValue("12");            //default value of label
        CustomMenuItem weightLabel = new CustomMenuItem();
        weightLabel.setContent(label);                  //assigning label to new custom menu item object
        weight.getItems().add(weightLabel);             //adding label to weight submenu
        CustomMenuItem weightSlider = new CustomMenuItem();
        weightSlider.setContent(weightSlide);
        weightSlider.setHideOnClick(false);
        weight.getItems().add(weightSlider);            //adding the slider to weight submenu

        Button setWeight = new Button("Set Weight");    //set weight button that confirms new weight selection
        CustomMenuItem weightButton = new CustomMenuItem();
        weightButton.setContent(setWeight);
        weight.getItems().add(weightButton);        //adding button to weight submenu

        Line.getItems().add(color);                 //adding menu items to line submenu
        Line.getItems().add(weight);
        Line.getItems().add(draw);
        Edit.getItems().add(Line);                  //adding line submenu into edit menu

        Menu helpSub = new Menu("About");
        Help.getItems().add(helpSub);

        MenuItem aboutSub = new MenuItem("Pain_t V 1.02\nCreated by Ethan Bilodeau");   //content of about submenu
        helpSub.getItems().add(aboutSub);



        openImg.setOnAction(openImageEventListener);    //calling event handler to open image
        saveAs.setOnAction(saveAsEventListener);        //calling event handler to save image to new file
        quit.setOnAction(closeEventListener);        //calling event handler to close program
        color.setOnAction(colorEventListener);          //calling event handler to open color picker
        draw.setOnAction(drawEventListener);            //event handler to draw on canvas
        setWeight.setOnAction(setWeightEventListener);  //event handler to change line wight


        BorderPane root = new BorderPane();                            //vbox to put image and menubar into
        stPane = new StackPane();                       //new stack pane object
        ScrollPane scPane = new ScrollPane();           //new scroll pane obejct
        myImageView = new ImageView();                  //new image view object

        scPane.setPannable(true);                       //scroll pane allows panning
        scPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);     //scroll bars show when image is too big for window size
        scPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);


        can = new Canvas(400,400);                  //default canvas
        stPane.getChildren().add(can);                     //canvas added to stack pane

        scPane.setContent(stPane);                        //stack pane set as the content of the scroll pane
        root.setCenter(scPane);
        root.setTop(menuBar);
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
            FileChooser fileChooser = new FileChooser();        //new file explorer object
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG"); //Set extension filter
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);
            File file = fileChooser.showOpenDialog(null); //Show open file dialog
            String path = file.getAbsolutePath();
            try {
                InputStream inputStream = new FileInputStream(path);    //taking input file and making it into imageview object myImageView
                image = new Image(inputStream);
                myImageView.setImage(image);
                can = new Canvas(myImageView.getFitHeight(),myImageView.getFitWidth());
                stPane.getChildren().add(myImageView);
                stPane.getChildren().add(can);
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

    EventHandler<ActionEvent> setWeightEventListener       //set weight handle function
            = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent t)
        {
            lineWeight = Double.parseDouble(label.getText());       //line weight casted as double, value taken from label in menu item
        }
    };

    EventHandler<ActionEvent> colorEventListener       //color chooser handle function
            = new EventHandler<ActionEvent>() {
        @Override
        public void handle(ActionEvent t) {
            StackPane root = new StackPane();          //creating new stack pane to hold color picker
            Scene scene = new Scene(root, 200, 50);       //scene holds stack pane and set to hold just the color picker
            Stage colorStage = new Stage();
            colorStage.setTitle("Color Picker");        //title of color picker window
            colorStage.setScene(scene);
            colorStage.show();                          //displaying window
            try {
                colorPicker = new ColorPicker();        //new color picker object
                root.getChildren().add(colorPicker);    //adding object to stack pane
            } catch (Exception e) {
                System.out.println("Color does not exist");
            }
        }

    };

    EventHandler<ActionEvent> drawEventListener       //draw handle function
            = new EventHandler<ActionEvent>()
    {
        @Override
        public void handle(ActionEvent t)
        {
            final GraphicsContext graphicsContext = can.getGraphicsContext2D();
            graphicsContext.drawImage(image, 0, 0,can.getWidth(), can.getHeight());
            initDraw(graphicsContext);
            can.addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    graphicsContext.beginPath();
                    graphicsContext.moveTo(event.getX(), event.getY());
                    graphicsContext.stroke();
                }
            });

            can.addEventHandler(MouseEvent.MOUSE_DRAGGED, new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event)
                {
                    graphicsContext.lineTo(event.getX(), event.getY());
                    graphicsContext.stroke();
                }
            });

            can.addEventHandler(MouseEvent.MOUSE_RELEASED, new EventHandler<MouseEvent>()
            {
                @Override
                public void handle(MouseEvent event) {}
            });
        }
    };

    private void initDraw(GraphicsContext gc)
    {
        double canvasWidth = gc.getCanvas().getWidth();
        double canvasHeight = gc.getCanvas().getHeight();

        gc.setFill(LIGHTGRAY);
        gc.setStroke(BLACK);
        gc.setLineWidth(5);

        gc.strokeRect(
                0,              //x of the upper left corner
                0,              //y of the upper left corner
                canvasWidth,    //width of the rectangle
                canvasHeight);  //height of the rectangle

        try
        {
            gc.setFill(colorPicker.getValue());
            gc.setStroke(colorPicker.getValue());
            gc.setLineWidth(lineWeight);
        }
        catch(Exception e)
        {
            gc.setFill(BLACK);
            gc.setStroke(BLACK);
            gc.setLineWidth(12);
        }
    }
}