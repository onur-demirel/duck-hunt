import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import java.util.ArrayList;

/**
 * This class is the scene for the options screen.
 * It contains the background images, crosshair images, and the text.
 * It also contains methods to change the background image and crosshair.
 */
public class OptionsScreen extends Scene {
    private final ArrayList<BackgroundImage> backgroundImages = new ArrayList<>();
    private final ArrayList<Image> crosshairImages = new ArrayList<>();
    private final double scaledWidth;
    private final double scaledHeight;
    /**
     * This constructor creates the options screen.
     * @param root The root of the scene.
     * @param scale The scale of the scene.
     */
    public OptionsScreen(StackPane root, double scale) {
        super(root);
        System.out.println("OptionsScreen");
        Image img = new Image("file:../assets/background/1.png");
        double imgWidth = img.getWidth();
        scaledWidth = imgWidth*scale;
        double imgHeight = img.getHeight();
        scaledHeight = imgHeight*scale;
        root.setPrefSize(scaledWidth, scaledHeight);
        for (int i = 1; i <= 6; i++) { // loop through all the background images and add them to the arraylist
            backgroundImages.add(new BackgroundImage(new Image("file:../assets/background/" + i + ".png"), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(scaledWidth, scaledHeight, false, false, false, false)));
        }
        root.setBackground(new Background(backgroundImages.get(0))); // set the background to the first image
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(scaledHeight*0.05, 0, 0, 0));
        Text startText = new Text("USE ARROW KEYS TO NAVIGATE\nPRESS ENTER TO START\nPRESS ESC TO EXIT");
        startText.setTextAlignment(TextAlignment.CENTER);
        Color color = Color.rgb(241, 159, 20);
        startText.setFill(color);
        startText.setFont(Font.font("Arial", FontWeight.BOLD, 7.5*scale));
        root.getChildren().add(startText);
        for (int i = 1; i <= 7; i++) { // loop through all the crosshair images and add them to the arraylist
            crosshairImages.add(new Image("file:../assets/crosshair/" + i + ".png"));
        }
        Image crosshair = crosshairImages.get(0);
        ImageView crosshairView = new ImageView(crosshair); // set the crosshair to the first image
        crosshairView.setScaleX(scale); // scale the crosshair
        crosshairView.setScaleY(scale); // scale the crosshair
        crosshairView.setTranslateY(scaledHeight*0.45-(crosshairView.getImage().getHeight()*scale)*0.5); // move the crosshair to the center of the screen vertically
        root.getChildren().add(crosshairView);
    }
    /**
     * This method changes the background image.
     * @param root The root of the scene.
     * @param changeDirection The direction to change the background image, next or previous.
     */
    public void changeBackgroundImage(StackPane root, String changeDirection) {
        System.out.println(root.getBackground().getImages().size());
        switch (changeDirection){
            case "next":
                if (backgroundImages.indexOf(root.getBackground().getImages().get(0)) == backgroundImages.size() - 1) {
                    root.setBackground(new Background(backgroundImages.get(0)));
                } else {
                    root.setBackground(new Background(backgroundImages.get(backgroundImages.indexOf(root.getBackground().getImages().get(0)) + 1)));
                }
                break;
            case "previous":
                if (backgroundImages.indexOf(root.getBackground().getImages().get(0)) == 0) {
                    root.setBackground(new Background(backgroundImages.get(backgroundImages.size() - 1)));
                } else {
                    root.setBackground(new Background(backgroundImages.get(backgroundImages.indexOf(root.getBackground().getImages().get(0)) - 1)));
                }
                break;
        }
    }
    /**
     * This method changes the crosshair.
     * @param root The root of the scene.
     * @param changeDirection The direction to change the crosshair, next or previous.
     */
    public void changeCrosshair(StackPane root, String changeDirection) {
        ImageView crosshairView = new ImageView();
        for (int i = 0; i < root.getChildren().size(); i++) { // loop through all the children of the root and find the crosshair
            if (root.getChildren().get(i) instanceof ImageView) {
                crosshairView = (ImageView) root.getChildren().get(i);
            }
        }
        switch (changeDirection){ // change the crosshair to the next or previous image
            case "next":
                if (crosshairImages.indexOf(crosshairView.getImage()) == crosshairImages.size() - 1) { // if the crosshair is the last image, set it to the first image
                    crosshairView.setImage(crosshairImages.get(0));
                } else { // else set it to the next image
                    crosshairView.setImage(crosshairImages.get(crosshairImages.indexOf(crosshairView.getImage()) + 1));
                }
                break;
            case "previous":
                if (crosshairImages.indexOf(crosshairView.getImage()) == 0) { // if the crosshair is the first image, set it to the last image
                    crosshairView.setImage(crosshairImages.get(crosshairImages.size() - 1));
                } else { // else set it to the previous image
                    crosshairView.setImage(crosshairImages.get(crosshairImages.indexOf(crosshairView.getImage()) - 1));
                }
                break;
        }
    }
    /**
     * This method returns the crosshair image.
     * @param root The root of the scene.
     * @return The crosshair image.
     */
    public Image getCrosshair(StackPane root){
        ImageView crosshairView = new ImageView();
        for (int i = 0; i < root.getChildren().size(); i++) {
            if (root.getChildren().get(i) instanceof ImageView) {
                crosshairView = (ImageView) root.getChildren().get(i);
            }
        }
        return crosshairView.getImage();
    }
    /**
     * This method returns the background image.
     * @param root The root of the scene.
     * @return The background image.
     */
    public BackgroundImage getBackgroundImage(StackPane root){
        return root.getBackground().getImages().get(0);
    }

    /**
     * This method returns the foreground image.
     * @param root The root of the scene.
     * @return The foreground image.
     */
    public Image getForegroundImage(StackPane root){
        return new Image("file:../assets/foreground/" + (backgroundImages.indexOf(root.getBackground().getImages().get(0))+1) + ".png", scaledWidth, scaledHeight, true, true);
    }
    /**
     * This method returns the scene.
     * @return The scene.
     */
    public Scene getThisScene() {
        return this;
    }
}
