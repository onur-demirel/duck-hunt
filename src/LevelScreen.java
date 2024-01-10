import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.geometry.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * This class is the scene that is displayed when the user is playing the game.
 * It contains the background, foreground, and ducks.
 * It also contains the ammo count and level number.
 * It also contains the animation for the ducks.
 */
public class LevelScreen extends Scene {
    private final double scaledHeight;
    private int ammoCount;
    private final Text ammoText;
    private int birdCount;
    private ImageView duck1 = new ImageView();
    private ImageView duck2 = new ImageView();
    private ImageView duck3 = new ImageView();
    private Timeline duck1Animation = new Timeline();
    private Timeline duck2Animation = new Timeline();
    private Timeline duck3Animation = new Timeline();
    private final ArrayList<Duck> ducks = new ArrayList<>();

    /**
     * This constructor creates the scene and sets the background, foreground, and ducks.
     * It also sets the ammo count and level number.
     * It also sets the animation for the ducks.
     * It also sets the size of the scene.
     * It also sets the position of the ammo count and level number.
     * @param root The root pane of the scene.
     * @param backgroundImage The background image.
     * @param foregroundImage The foreground image.
     * @param scale The scale of the instance.
     * @param level The level number of the game.
     */
    public LevelScreen(StackPane root, BackgroundImage backgroundImage, Image foregroundImage, double scale, Integer level) {
        super(root);
        root.setAlignment(Pos.TOP_LEFT);
        System.out.println("LevelScreen");
        Image img = backgroundImage.getImage();
        double imgWidth = img.getWidth();
        double scaledWidth = imgWidth * scale;
        double imgHeight = img.getHeight();
        scaledHeight = imgHeight*scale;
        root.setPrefSize(scaledWidth, scaledHeight);
        root.setBackground(new Background(backgroundImage));
        birdCount = ((int) (level/2.5))+1; // 1, 1, 2, 2, 3, 3
        ammoCount = birdCount*3; // 3, 3, 6, 6, 9, 9
        Text levelText = new Text("Level " + level + "/6");
        levelText.setTextAlignment(TextAlignment.CENTER);
        Color color = Color.rgb(241, 159, 20);
        levelText.setFill(color);
        levelText.setFont(Font.font("Arial", FontWeight.BOLD, 7.5*scale));
        ammoText = new Text("Ammo Left: " + ammoCount);
        ammoText.setTextAlignment(TextAlignment.CENTER);
        ammoText.setFill(color);
        ammoText.setFont(Font.font("Arial", FontWeight.BOLD, 7.5*scale));
        HBox topHBox = new HBox();
        topHBox.setAlignment(Pos.TOP_RIGHT);
        topHBox.setPadding(new Insets(scaledHeight*0.01, scaledWidth *0.01, scaledHeight*0.01, scaledWidth *0.01));
        topHBox.setSpacing(scaledWidth *0.25);
        topHBox.getChildren().add(0, new Text(" "));
        topHBox.getChildren().addAll(levelText, ammoText);
        root.getChildren().add(topHBox);
        String[] colors = {"red", "blue", "black"};
        String duck1Color = colors[(int) (Math.random()*3)]; // duck color is random between red, blue, and black to add variety
        String duck2Color = colors[(int) (Math.random()*3)];
        String duck3Color = colors[(int) (Math.random()*3)];
        Random random = new Random();
        double maxY = 0.4; // ducks will spawn in the top 40% of the screen at most to ensure visibility
        double minY = 0.05; // ducks will spawn in the top 5% of the screen at least to ensure visibility
        double maxX = 0.85; // ducks will spawn in the 85% portion of the screen from the left edge at most to ensure visibility
        double minX = 0.05; // ducks will spawn in the 5% portion of the screen from the left edge at least to ensure visibility
        double startingX; // starting x position of the duck will be random between the min and max x values to add variety and randomness
        double startingY; // starting y position of the duck will be random between the min and max y values to add variety and randomness
        if (level == 1){ // level 1 has 1 duck that moves horizontally
            ducks.add(new Duck(duck1Color, scale, false, scaledWidth, scaledHeight)); // creates a duck object that moves horizontally
            duck1 = ducks.get(0).getDuckImageView();
            startingX = scaledWidth *(random.nextDouble()*(maxX-minX)+minX); // random starting x position
            startingY = scaledHeight*(random.nextDouble()*(maxY-minY)+minY); // random starting y position
            duck1.setTranslateY(startingY);
            duck1.setTranslateX(startingX);
            duck1Animation = new Timeline();
            duck1Animation.setCycleCount(Timeline.INDEFINITE); // duck animation repeats indefinitely
            duck1Animation.getKeyFrames().add(ducks.get(0).getWingFlappingKeyFrame()); // adds wing flapping key frame
            duck1Animation.getKeyFrames().add(ducks.get(0).getFlyingMotionKeyFrame()); // adds flying motion key frame
            root.getChildren().add(duck1);
            duck1Animation.play();
        } else if (level == 2){ // level 2 has 1 duck that moves diagonally
            ducks.add(new Duck(duck1Color, scale, true, scaledWidth, scaledHeight)); // creates a duck object that moves diagonally
            duck1 = ducks.get(0).getDuckImageView();
            startingX = scaledWidth *(random.nextDouble()*(maxX-minX)+minX);
            startingY = scaledHeight*(random.nextDouble()*(maxY-minY)+minY);
            duck1.setTranslateY(startingY);
            duck1.setTranslateX(startingX);
            duck1Animation = new Timeline();
            duck1Animation.setCycleCount(Timeline.INDEFINITE);
            duck1Animation.getKeyFrames().add(ducks.get(0).getWingFlappingKeyFrame());
            duck1Animation.getKeyFrames().add(ducks.get(0).getFlyingMotionKeyFrame());
            root.getChildren().add(duck1);
            duck1Animation.play();
        } else if (level == 3){ // level 3 has 2 ducks that move horizontally
            ducks.add(new Duck(duck1Color, scale, false, scaledWidth, scaledHeight));
            ducks.add(new Duck(duck2Color, scale, false, scaledWidth, scaledHeight));
            duck1 = ducks.get(0).getDuckImageView();
            startingX = scaledWidth *(random.nextDouble()*(maxX-minX)+minX);
            startingY = scaledHeight*(random.nextDouble()*(maxY-minY)+minY);
            duck1.setTranslateY(startingY);
            duck1.setTranslateX(startingX);
            duck1Animation = new Timeline();
            duck1Animation.setCycleCount(Timeline.INDEFINITE);
            duck1Animation.getKeyFrames().add(ducks.get(0).getWingFlappingKeyFrame());
            duck1Animation.getKeyFrames().add(ducks.get(0).getFlyingMotionKeyFrame());
            root.getChildren().add(duck1);
            duck1Animation.play();
            duck2 = ducks.get(1).getDuckImageView();
            if (whichHalf(startingX/scaledWidth)) {
                minX = startingX/scaledWidth;
            } else {
                maxX = startingX/scaledWidth;
            }
            if (whichHalf(startingX/scaledHeight)) {
                minY = startingY/scaledHeight;
            } else {
                maxY = startingY/scaledHeight;
            }
            System.out.println("minX: " + minX + " maxX: " + maxX + " minY: " + minY + " maxY: " + maxY);
            startingX = scaledWidth *(random.nextDouble()*(maxX-minX)+minX);
            startingY = scaledHeight*(random.nextDouble()*(maxY-minY)+minY);
            duck2.setTranslateY(startingY);
            duck2.setTranslateX(startingX);
            duck2Animation = new Timeline();
            duck2Animation.setCycleCount(Timeline.INDEFINITE);
            duck2Animation.getKeyFrames().add(ducks.get(1).getWingFlappingKeyFrame());
            duck2Animation.getKeyFrames().add(ducks.get(1).getFlyingMotionKeyFrame());
            root.getChildren().add(duck2);
            duck2Animation.play();
        } else if (level == 4){ // level 4 has 2 ducks that move diagonally
            ducks.add(new Duck(duck1Color, scale, true, scaledWidth, scaledHeight));
            ducks.add(new Duck(duck2Color, scale, true, scaledWidth, scaledHeight));
            duck1 = ducks.get(0).getDuckImageView();
            startingX = scaledWidth *(random.nextDouble()*(maxX-minX)+minX);
            startingY = scaledHeight*(random.nextDouble()*(maxY-minY)+minY);
            duck1.setTranslateY(startingY);
            duck1.setTranslateX(startingX);
            duck1Animation = new Timeline();
            duck1Animation.setCycleCount(Timeline.INDEFINITE);
            duck1Animation.getKeyFrames().add(ducks.get(0).getWingFlappingKeyFrame());
            duck1Animation.getKeyFrames().add(ducks.get(0).getFlyingMotionKeyFrame());
            root.getChildren().add(duck1);
            duck1Animation.play();
            duck2 = ducks.get(1).getDuckImageView();
            if (whichHalf(startingX/scaledWidth)) {
                minX = startingX/scaledWidth;
            } else {
                maxX = startingX/scaledWidth;
            }
            if (whichHalf(startingX/scaledHeight)) {
                minY = startingY/scaledHeight;
            } else {
                maxY = startingY/scaledHeight;
            }
            System.out.println("minX: " + minX + " maxX: " + maxX + " minY: " + minY + " maxY: " + maxY);
            startingX = scaledWidth *(random.nextDouble()*(maxX-minX)+minX);
            startingY = scaledHeight*(random.nextDouble()*(maxY-minY)+minY);
            duck2.setTranslateY(startingY);
            duck2.setTranslateX(startingX);
            duck2Animation = new Timeline();
            duck2Animation.setCycleCount(Timeline.INDEFINITE);
            duck2Animation.getKeyFrames().add(ducks.get(1).getWingFlappingKeyFrame());
            duck2Animation.getKeyFrames().add(ducks.get(1).getFlyingMotionKeyFrame());
            root.getChildren().add(duck2);
            duck2Animation.play();
        } else if (level == 5){ // level 5 has 3 ducks that move in mixed manner
            ducks.add(new Duck(duck1Color, scale, true, scaledWidth, scaledHeight));
            ducks.add(new Duck(duck2Color, scale, true, scaledWidth, scaledHeight));
            ducks.add(new Duck(duck3Color, scale, false, scaledWidth, scaledHeight));
            duck1 = ducks.get(0).getDuckImageView();
            startingX = scaledWidth *(random.nextDouble()*(maxX-minX)+minX);
            startingY = scaledHeight*(random.nextDouble()*(maxY-minY)+minY);
            duck1.setTranslateY(startingY);
            duck1.setTranslateX(startingX);
            duck1Animation = new Timeline();
            duck1Animation.setCycleCount(Timeline.INDEFINITE);
            duck1Animation.getKeyFrames().add(ducks.get(0).getWingFlappingKeyFrame());
            duck1Animation.getKeyFrames().add(ducks.get(0).getFlyingMotionKeyFrame());
            root.getChildren().add(duck1);
            duck1Animation.play();
            duck2 = ducks.get(1).getDuckImageView();
            if (whichHalf(startingX/scaledWidth)) {
                minX = startingX/scaledWidth;
            } else {
                maxX = startingX/scaledWidth;
            }
            if (whichHalf(startingX/scaledHeight)) {
                minY = startingY/scaledHeight;
            } else {
                maxY = startingY/scaledHeight;
            }
            System.out.println("minX: " + minX + " maxX: " + maxX + " minY: " + minY + " maxY: " + maxY);
            startingX = scaledWidth *(random.nextDouble()*(maxX-minX)+minX);
            startingY = scaledHeight*(random.nextDouble()*(maxY-minY)+minY);
            duck2.setTranslateY(startingY);
            duck2.setTranslateX(startingX);
            ducks.get(1).setSpeed(1.10); // set speed of duck2 to be faster to make it more challenging
            duck2Animation = new Timeline();
            duck2Animation.setCycleCount(Timeline.INDEFINITE);
            duck2Animation.getKeyFrames().add(ducks.get(1).getWingFlappingKeyFrame());
            duck2Animation.getKeyFrames().add(ducks.get(1).getFlyingMotionKeyFrame());
            root.getChildren().add(duck2);
            duck2Animation.play();
            duck3 = ducks.get(2).getDuckImageView();
            if (whichHalf(startingX/scaledWidth)) {
                minX = startingX/scaledWidth;
            } else {
                maxX = startingX/scaledWidth;
            }
            if (whichHalf(startingX/scaledHeight)) {
                minY = startingY/scaledHeight;
            } else {
                maxY = startingY/scaledHeight;
            }
            startingX = scaledWidth *(random.nextDouble()*(maxX-minX)+minX);
            startingY = scaledHeight*(random.nextDouble()*(maxY-minY)+minY);
            duck3.setTranslateY(startingY);
            duck3.setTranslateX(startingX);
            ducks.get(2).setSpeed(1.15); // set speed of duck3 to be faster to make it more challenging
            duck3Animation = new Timeline();
            duck3Animation.setCycleCount(Timeline.INDEFINITE);
            duck3Animation.getKeyFrames().add(ducks.get(2).getWingFlappingKeyFrame());
            duck3Animation.getKeyFrames().add(ducks.get(2).getFlyingMotionKeyFrame());
            root.getChildren().add(duck3);
            duck3Animation.play();
        } else if (level == 6){ // level 6 has 3 ducks that move diagonally
            ducks.add(new Duck(duck1Color, scale, true, scaledWidth, scaledHeight));
            ducks.add(new Duck(duck2Color, scale, true, scaledWidth, scaledHeight));
            ducks.add(new Duck(duck3Color, scale, true, scaledWidth, scaledHeight));
            duck1 = ducks.get(0).getDuckImageView();
            startingX = scaledWidth *(random.nextDouble()*(maxX-minX)+minX);
            startingY = scaledHeight*(random.nextDouble()*(maxY-minY)+minY);
            duck1.setTranslateY(startingY);
            duck1.setTranslateX(startingX);
            ducks.get(0).setSpeed(1.25); // set speed of duck1 to be faster to make it more challenging
            duck1Animation = new Timeline();
            duck1Animation.setCycleCount(Timeline.INDEFINITE);
            duck1Animation.getKeyFrames().add(ducks.get(0).getWingFlappingKeyFrame());
            duck1Animation.getKeyFrames().add(ducks.get(0).getFlyingMotionKeyFrame());
            root.getChildren().add(duck1);
            duck1Animation.play();
            duck2 = ducks.get(1).getDuckImageView();
            startingX = scaledWidth *(random.nextDouble()*(maxX-minX)+minX);
            startingY = scaledHeight*(random.nextDouble()*(maxY-minY)+minY);
            if (whichHalf(startingX/scaledWidth)) {
                minX = startingX/scaledWidth;
            } else {
                maxX = startingX/scaledWidth;
            }
            if (whichHalf(startingX/scaledHeight)) {
                minY = startingY/scaledHeight;
            } else {
                maxY = startingY/scaledHeight;
            }
            System.out.println("minX: " + minX + " maxX: " + maxX + " minY: " + minY + " maxY: " + maxY);
            duck2.setTranslateY(startingY);
            duck2.setTranslateX(startingX);
            ducks.get(1).setSpeed(1.5); // set speed of duck2 to be faster to make it more challenging
            duck2Animation = new Timeline();
            duck2Animation.setCycleCount(Timeline.INDEFINITE);
            duck2Animation.getKeyFrames().add(ducks.get(1).getWingFlappingKeyFrame());
            duck2Animation.getKeyFrames().add(ducks.get(1).getFlyingMotionKeyFrame());
            root.getChildren().add(duck2);
            duck2Animation.play();
            duck3 = ducks.get(2).getDuckImageView();
            if (whichHalf(startingX/scaledWidth)) {
                minX = startingX/scaledWidth;
            } else {
                maxX = startingX/scaledWidth;
            }
            if (whichHalf(startingX/scaledHeight)) {
                minY = startingY/scaledHeight;
            } else {
                maxY = startingY/scaledHeight;
            }
            System.out.println("minX: " + minX + " maxX: " + maxX + " minY: " + minY + " maxY: " + maxY);
            startingX = scaledWidth *(random.nextDouble()*(maxX-minX)+minX);
            startingY = scaledHeight*(random.nextDouble()*(maxY-minY)+minY);
            duck3.setTranslateY(startingY);
            duck3.setTranslateX(startingX);
            ducks.get(2).setSpeed(1.75); // set speed of duck3 to be faster to make it more challenging
            duck3Animation = new Timeline();
            duck3Animation.setCycleCount(Timeline.INDEFINITE);
            duck3Animation.getKeyFrames().add(ducks.get(2).getWingFlappingKeyFrame());
            duck3Animation.getKeyFrames().add(ducks.get(2).getFlyingMotionKeyFrame());
            root.getChildren().add(duck3);
            duck3Animation.play();
        }
        ImageView foregroundView = new ImageView(foregroundImage);
        root.getChildren().add(foregroundView); // add foreground image to scene
    }

    /**
     * This method returns the scene
     * @return the scene
     */
    public Scene getThisScene() {
        return this;
    }
    /**
     * This method updates the ammo text
     */
    public void updateAmmoText() {
        ammoCount--;
        ammoText.setText("Ammo Left: " + ammoCount);
    }
    /**
     * This method returns the ammo count
     * @return the ammo count
     */
    public Integer getAmmoCount() {
        return ammoCount;
    }
    /**
     * This method returns the duck image views
     * @return the duck image views in an arraylist
     */
    public ArrayList<ImageView> getDuckImageViews() {
        ArrayList<ImageView> ducks = new ArrayList<>();
        ducks.add(duck1);
        ducks.add(duck2);
        ducks.add(duck3);
        return ducks;
    }
    /**
     * This method returns the duck animations
     * @return the duck animations in an arraylist
     */
    public ArrayList<Timeline> getDuckAnimations() {
        ArrayList<Timeline> duckAnimations = new ArrayList<>();
        duckAnimations.add(duck1Animation);
        duckAnimations.add(duck2Animation);
        duckAnimations.add(duck3Animation);
        return duckAnimations;
    }
    /**
     * This method returns the Duck objects
     * @return the Duck objects in an arraylist
     */
    public ArrayList<Duck> getDuckObjects() {
        return ducks;
    }

    /**
     * This method returns the bird count
     * @return the bird count
     */
    public int getDuckCount() {
        return birdCount;
    }

    /**
     * This method updates the bird count
     */
    public void duckShot(){
        birdCount--;
    }
    /**
     * This method returns the scaled height of the scene
     * @return the scaled height of the scene
     */
    public double getScaledHeight() {
        return scaledHeight;
    }

    /**
     * This method decides whether the passed location is greater than or less than 0.5
     * @param location the location component to be compared
     * @return true if the location is less than 0.5, false otherwise
     */
    private Boolean whichHalf(double location) {
        return location < 0.5;
    }
}
