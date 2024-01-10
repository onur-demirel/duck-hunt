import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

/**
 * This class is the scene for the title screen.
 * It contains the background image and the text.
 */
public class TitleScreen extends Scene {
    /**
     * This constructor creates the title screen.
     * @param root The root of the scene.
     * @param scale The scale of the scene.
     */
    public TitleScreen(StackPane root, double scale) {
        super(root);
        Image img = new Image("file:../assets/welcome/1.png");
        double imgWidth = img.getWidth();
        double scaledWidth = imgWidth*scale;
        double imgHeight = img.getHeight();
        double scaledHeight = imgHeight*scale;
        root.setBackground(new Background(new BackgroundImage(img, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, new BackgroundSize(scaledWidth, scaledHeight, false, false, false, false))));
        root.setPrefSize(scaledWidth, scaledHeight);
        root.setAlignment(Pos.CENTER);
        Text startText = new Text("PRESS ENTER TO START\nPRESS ESC TO EXIT");
        startText.setTextAlignment(TextAlignment.CENTER);
        startText.setTranslateY(scaledHeight*0.20);
        Color color = Color.rgb(241, 159, 20); // color picked from the PDF file
        startText.setFill(color);
        startText.setFont(Font.font("Arial", FontWeight.BOLD, 12.5*scale)); // font size is approximate
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.4), startText); // fade in and out the text
        fadeTransition.setFromValue(0);
        fadeTransition.setToValue(1);
        fadeTransition.setCycleCount(Animation.INDEFINITE);
        fadeTransition.setAutoReverse(true);
        root.getChildren().add(startText);
        fadeTransition.play();
    }
    /**
     * This method returns the current scene.
     * @return The current scene.
     */
    public Scene getThisScene() {
        return this;
    }
}
