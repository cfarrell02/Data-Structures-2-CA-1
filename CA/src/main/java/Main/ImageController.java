package Main;

import Models.ComponentGroup;
import Models.PixelGroup;
import Utilities.*;
import javafx.application.Platform;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import javafx.scene.image.*;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.*;

import java.util.HashMap;

import java.util.Map;

import static Utilities.Utilities.*;

/**
 * Controls the JAVAFX UI
 * @author cianfarrell
 * @version 1.0
 */
public class ImageController{
    @FXML
    private ImageView imageView,greyImage;
    @FXML
    private ListView<String> groupList;
    public static Image  mainImage, original;
    public static int[] pixels;
    @FXML
    private SplitPane main;
    @FXML private Button newGroup;
    @FXML
    private MenuItem revert;
    @FXML
    private Label imageInfo, imageName, leftstatus, rightstatus, pixelDetails;
    @FXML
    private Slider tolerance, noise;
    @FXML
    private ProgressBar progress;
    private FileChooser fileChooser;
    private String lastObject;
    private PixelGroup currentGroup;
    public static Map<String, ComponentGroup> components;
    public static int x, y;
    public static int height,width;
    ImageService imageService;

    /**
     * This method establishes values for certain things. It also applied "Set on" processes to specific elements
     * This is used as it is able take in values unlike in FXML
     */
    @FXML
    private void initialize() {
        components = new HashMap<>();
        fileChooser = new FileChooser();
        groupList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        newGroup.setOnAction(e ->
                displayGrey(mainImage, tolerance.getValue(),noise.getValue(),true)
        );
        imageView.setOnMousePressed(e -> {
            x = (int) e.getX();
            y = (int) e.getY();
            imageView.setImage(markPosition(mainImage,x,y));
            displayInfo(x,y);
        });

        tolerance.setOnMouseReleased(e->{
            if(!components.isEmpty()){
                x = components.get(groupList.getSelectionModel().getSelectedItem()).getSourceX();
                y = components.get(groupList.getSelectionModel().getSelectedItem()).getSourceY();}
            displayGrey(mainImage, tolerance.getValue(),noise.getValue(),false);
        });
        noise.setOnMouseReleased(e->{
            if(!components.isEmpty()){
                x = components.get(groupList.getSelectionModel().getSelectedItem()).getSourceX();
                y = components.get(groupList.getSelectionModel().getSelectedItem()).getSourceY();}
            displayGrey(mainImage, tolerance.getValue(),noise.getValue(),false);
        });


        groupList.setOnMousePressed(e -> {
            ComponentGroup cg = components.get(groupList.getSelectionModel().getSelectedItem());
            PixelGroup pg = cg.getPixelGroup(0);
            tolerance.setValue(cg.getTolerance());
            noise.setValue(cg.getNoise());
            if(pg!=null){
                x = pg.getRoot()%width;
                y = pg.getRoot()/width;
            }
            displayRaw();
        });
        imageView.imageProperty().addListener((o, oldImage, newImage) -> {
            main.setDisable(false);
            progress.progressProperty().unbind();
            progress.progressProperty().setValue(0);
            if(imageService != null && imageService.isRunning()) {

                greyImage.setImage(toImage(new WritableImage(width,height), mainImage.getPixelReader().getColor(x, y), pixels, width, height));
                groupList.getItems().clear();
                int totalGroups = 0;
                for (Map.Entry<String, ComponentGroup> entry : components.entrySet()) {
                    groupList.getItems().add(entry.getKey());
                    totalGroups += entry.getValue().getGroups().size();
                }
                components.get(lastObject).setSliders(tolerance.getValue(),noise.getValue());
                groupList.getSelectionModel().select(lastObject);
                imageView.imageProperty().unbind();
                imageInfo.setText(
                        "\nImage Height: " + original.getHeight() + "px" +
                                "\nImage Width: " + original.getWidth() + "px"+
                                "\nTotal Groups: "+totalGroups);

            }

        });
        //  String user = System.getProperty("user.name");
        //  fileChooser.setInitialDirectory(new File("C:\\Users\\"+user+"\\Pictures"));
        main.setDisable(true);

    }

    /**
     * Method to randomise the colours component group by component group.
     */
    @FXML
    private void randomiseColours(){
        if(greyImage.getImage()!=null && groupList.getSelectionModel().getSelectedItem()!=null) {
            greyImage.setImage(new WritableImage(width, height));

            for (String name : groupList.getSelectionModel().getSelectedItems()) {
                ComponentGroup cg = components.get(name);
                greyImage.setImage(toImage(greyImage.getImage(), randomColour(), cg.getGroupPixels(), width, height));
            }
        }
    }

    /**
     * Method to randomise group colours, group by group.
     */
    @FXML
    private void randomiseGroupColours(){
        if(greyImage.getImage()!=null && groupList.getSelectionModel().getSelectedItem()!=null) {
            greyImage.setImage(new WritableImage(width, height));

            for (String name : groupList.getSelectionModel().getSelectedItems()) {
                ComponentGroup cg = components.get(name);
                for(PixelGroup pg : cg.getGroups()) {
                    greyImage.setImage(drawGroup(greyImage.getImage(), randomColour(), cg.getGroupPixels(), width, height,pg));
                }
            }
        }
    }

    /**
     * Method converts image to a black and white array based upon a few specific details
     * @param image Image to be converted
     * @param tolerance Tolerance in which the pixels are selected for the array
     * @param smallestGroup The minimum size of groups, anything smaller is removed
     * @param newGroup
     * @return An array with each matching pixel given a value, blanks are set to -1
     */
    @FXML
    public int[] displayGrey(Image image, double tolerance,double smallestGroup, boolean newGroup){
        width = (int) image.getWidth();
        height = (int) image.getHeight();
        PixelReader pr = image.getPixelReader();
        Color color = image.getPixelReader().getColor(x, y);
        int[] pixels = new int[width * height];
        //Makes an Array and adds in every pixel that is within the range of the chosen colour
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color color1 = pr.getColor(i, j);
                if (Utilities.compareRGB(color,color1,tolerance))
                    pixels[(j * width) + i] = (j * width) + i;
                else
                    pixels[(j * width) + i] = -1;
            }

        }

        if(newGroup){
            lastObject = AlertBox.displayTextBox("Name","Enter name of selected component: ");
            if(lastObject==null) return null;
        }
        else
            lastObject = groupList.getSelectionModel().getSelectedItem();

        imageService = new ImageService(image, pixels, width, height,
                smallestGroup,lastObject
                ,color,false);
        imageView.imageProperty().bind(imageService.valueProperty());
        progress.progressProperty().bind(imageService.progressProperty());
        imageService.start();
        main.setDisable(true);
        return pixels;

    }

    /**
     * Displays certain info into the tooltip about the pixel being selected
     * @param mouseX X coord of the pixel
     * @param mouseY Y coord of the pixel
     */
    private void displayInfo(int mouseX,int mouseY) {
        try{
            ComponentGroup cg = components.get(groupList.getSelectionModel().getSelectedItem());
            if(cg!=null) {

                int root = findRoot(cg.getGroupPixels(), (mouseY * width) + mouseX);
                currentGroup = cg.getPixelGroupByRoot(root);
                if (currentGroup != null) {
                    imageView.setImage(markGroup(currentGroup, markImage(cg, mainImage, 1)));
                    Tooltip tp = new Tooltip("Type: " + cg.getName() + "\nGroup Size: " + currentGroup.getSize() + " px\nGroup Number: " + (cg.getGroups().indexOf(currentGroup) + 1));
                    rightstatus.setText("Group Size: " + currentGroup.getSize() + " px; Group Number: " + (cg.getGroups().indexOf(currentGroup) + 1));
                    tp.hideDelayProperty().setValue(new Duration(60000));
                    Tooltip.install(imageView, tp);
                }}}catch (NullPointerException e){
            AlertBox.display("Error!",e.getMessage());
        }
    }

    /**
     * Displays all the raw pixels from the arrays. Displays any group that is selected in the GUI.
     * Components are set to the original colour by default
     */
    @FXML
    private void displayRaw(){
        imageView.imageProperty().unbind();
        greyImage.setImage(new WritableImage(width,height));
        imageView.setImage(mainImage);

        for(String name :groupList.getSelectionModel().getSelectedItems()){
            ComponentGroup cg = components.get(name);
            greyImage.setImage(toImage(greyImage.getImage(),cg.getColor(),cg.getGroupPixels(),width,height));
            imageView.setImage(markImage(cg,imageView.getImage(),1));
        }
        String name = groupList.getSelectionModel().getSelectedItem();

        pixelDetails.setText(components.get(name).toString());

    }

    /**
     * Loads in an image from a file chooser and inserts it into the code.
     */
    @FXML
    private void display() {
        ImageApplication.window.getScene().setOnKeyPressed(event -> {
            if(currentGroup!=null&&(event.getCode().equals(KeyCode.DELETE)||event.getCode().equals(KeyCode.BACK_SPACE))){
                ComponentGroup cg = components.get(lastObject);
                cg.removePixelGroup(currentGroup);
                imageView.setImage(markImage(cg,mainImage,1));
                greyImage.setImage(toImage((new WritableImage(width,height)),cg.getColor(),cg.getGroupPixels(),width,height));
                groupList.getItems().clear();
                for (Map.Entry<String, ComponentGroup> entry : components.entrySet())
                    groupList.getItems().add(entry.getKey());
                components.get(lastObject).setSliders(tolerance.getValue(),noise.getValue());
                groupList.getSelectionModel().select(lastObject);
            }
        });
        components.clear();
        groupList.getItems().clear();
        fileChooser.getExtensionFilters().clear();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.tif", "*.tiff",
                "*.bmp", "*.gif", "*.png", "*.wbmp", "*.jpeg");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File file = fileChooser.showOpenDialog(ImageApplication.window);



        if (file != null) {
            main.setDisable(false);
            revert.setDisable(false);
            FileInputStream input;
            try {
                input = new FileInputStream(file.getPath());

                Image image = new Image(input,imageView.getFitWidth(),imageView.getFitHeight(),false,false);
                // Image image = new Image(input);
                imageName.setText(file.getName());
                leftstatus.setText("Loaded: " + file.getName());
                mainImage = image;
                original = mainImage;
            } catch (FileNotFoundException e) {
                AlertBox.display("Error!","File not found: \n"+e.getMessage());
            }
            revert();
        }

    }

    /**
     * Simple method to reset everything in the gui
     */
    @FXML
    private void revert() {
        if(imageService != null)
            imageService.cancel();
        components.clear();
        groupList.getItems().clear();
        imageView.imageProperty().unbind();
        mainImage = original;
        imageView.setImage(mainImage);
        imageInfo.setText(
                "\nImage Height: " + original.getHeight() + "px" +
                        "\nImage Width: " + original.getWidth() + "px"+
                "\nTotal Groups: 0");

    }

    /**
     * Closes application
     */
    @FXML
    private void exit(){
        if(AlertBox.displayConfirmation("Quit?","Quit?","Are you sure you want to quit?"))
            Platform.exit();
    }


}


