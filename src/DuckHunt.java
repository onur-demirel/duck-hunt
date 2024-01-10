import javafx.animation.FadeTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.ImageCursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.media.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;

/**
 * The main class of the game. This class extends the JavaFX Application class and is the entry point of the game.
 * This class is responsible for creating the main stage and setting the scene to the title screen.
 * This class also contains the event handler for the title screen, options screen, and game level screen.
 */
public class DuckHunt extends Application {
    private static Double SCALE = 1.0; //default scale
    private static Double VOLUME = 0.001; //default volume
    private Image crosshair; //crosshair image
    private BackgroundImage backgroundImage; //background image
    private Image foregroundImage; //foreground image
    private MediaPlayer mediaPlayer; //media player, defined as global variable to ensure access from all methods
    private final boolean[] levelComplete = new boolean[]{false}; //boolean array to check if level is complete, defined as array to ensure access from lambda expressions
    private final boolean[] levelFailed = new boolean[]{false}; //boolean array to check if level is failed, defined as array to ensure access from lambda expressions
    private final boolean[] gameOver = new boolean[]{false}; //boolean array to check if game is over, defined as array to ensure access from lambda expressions
    private final boolean[] gameComplete = new boolean[]{false}; //boolean array to check if game is complete, defined as array to ensure access from lambda expressions

    /**
     * main method for environments that do not support JavaFX Application launch
     * @param args command line arguments
     */
    public static void main(String[] args) {
        if (args.length > 0) {
            if (args.length == 1) {
                System.out.println("Scale: " + args[0]);
                setScale(Double.parseDouble(args[0]));
            } else if (args.length == 2) {
                System.out.println("Scale: " + args[0]);
                setScale(Double.parseDouble(args[0]));
                System.out.println("Volume: " + args[1]);
                setVolume(Double.parseDouble(args[1]));
            }
        }
        launch(args);
    }
    @Override
    public void start(Stage mainStage) {
        mainStage.setTitle("HUBBM Duck Hunt");
        mainStage.getIcons().add(new Image("file:../assets/favicon/1.png")); //setting favicon
        mainStage.setResizable(false); //disabling resizing
        StackPane root = new StackPane();
        mainStage.setScene(new TitleScreen(root, SCALE).getThisScene()); //setting scene to title screen
        mainStage.show(); //showing main stage
        Media titleSound = new Media(new File("../assets/effects/Title.mp3").toURI().toString()); //loading title music
        mediaPlayer = new MediaPlayer(titleSound); //creating media player
        mediaPlayer.setVolume(VOLUME);
        mediaPlayer.play(); //playing title music
        root.requestFocus(); //requesting focus for key events
        TitleScreenEventHandler(mainStage); //calling event handler for title screen
    }
    /**
     * Event handler method for the title screen scene. The title music is played in loop.
     * This method is responsible for handling the key events on the title screen.
     * This method is also responsible for setting the scene to the options screen when the ENTER key is pressed.
     * This method is also responsible for closing the application when the ESCAPE key is pressed.
     * @param mainStage the main stage of the application
     */
    private void TitleScreenEventHandler(Stage mainStage) {
        StackPane root = (StackPane) mainStage.getScene().getRoot();
        Media titleSound = new Media(new File("../assets/effects/Title.mp3").toURI().toString());
        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING) && !mediaPlayer.getMedia().getSource().equals(titleSound.getSource())) { //checking if title music is playing
            mediaPlayer.stop();
            mediaPlayer = new MediaPlayer(titleSound);
            mediaPlayer.setVolume(VOLUME);
            mediaPlayer.play();
        } else if (mediaPlayer.getStatus().equals(MediaPlayer.Status.STOPPED) && !mediaPlayer.getMedia().getSource().equals(titleSound.getSource())) { //checking if title music is stopped
            mediaPlayer = new MediaPlayer(titleSound);
            mediaPlayer.setVolume(VOLUME);
            mediaPlayer.play();
        }
        mediaPlayer.setOnEndOfMedia(() -> { //checking if title music has ended, and replaying if so
            mediaPlayer.seek(Duration.ZERO);
            mediaPlayer.play();
        });
        root.setOnKeyReleased(e -> { //handling key events
            switch (e.getCode()) {
                case ENTER:
                    System.out.println("ENTER from TitleScreen");
                    root.getChildren().clear(); //clearing root
                    StackPane optionsRoot = new StackPane(); //creating new root for options screen
                    mainStage.setScene(new OptionsScreen(optionsRoot, SCALE).getThisScene()); //setting scene to options screen
                    mainStage.show();
                    optionsRoot.requestFocus();
                    OptionsScreenEventHandler(mainStage); //calling event handler for options screen
                    break;
                case ESCAPE:
                    System.out.println("ESCAPE from TitleScreen");
                    mainStage.close(); //closing application
                    break;
            }
        });
    }
    /**
     * Event handler method for the options screen scene. The title music is continued to be played in loop.
     * This method is responsible for handling the key events on the options screen.
     * This method is also responsible for setting the scene to the title screen when the ESCAPE key is pressed.
     * This method is also responsible for setting the scene to the game level screen when the ENTER key is pressed.
     * The level screen is not loaded until the intro music is played to the end.
     * @param mainStage the main stage of the application
     */
    private void OptionsScreenEventHandler(Stage mainStage) {
        StackPane root = (StackPane) mainStage.getScene().getRoot();
        OptionsScreen optionsScreen = (OptionsScreen) mainStage.getScene();
        Media introSound = new Media(new File("../assets/effects/Intro.mp3").toURI().toString()); //loading intro music
        root.setOnKeyReleased(e -> { //handling key events, OnKeyReleased is used to prevent multiple key presses
            switch (e.getCode()) { //checking which key is pressed
                case ENTER: //ENTER key is pressed
                    System.out.println("ENTER from OptionsScreen");
                    System.out.println(mediaPlayer.statusProperty().getValue()); //printing status of media player to check if intro music is playing
                    if (mediaPlayer.statusProperty().getValue().equals(MediaPlayer.Status.PLAYING) && !mediaPlayer.getMedia().equals(introSound)) { //checking if intro music is playing
                        mediaPlayer.stop();
                        mediaPlayer = new MediaPlayer(introSound);
                        mediaPlayer.setVolume(VOLUME);
                        mediaPlayer.play();
                        mediaPlayer.setOnEndOfMedia(() -> { //checking if intro music has ended, and loading level screen if so
                            mediaPlayer.stop();
                            StackPane levelRoot = new StackPane();
                            mainStage.setScene(new LevelScreen(levelRoot, backgroundImage, foregroundImage, SCALE, 1).getThisScene());
                            mainStage.getScene().setCursor(new ImageCursor(crosshair));
                            mainStage.show();
                            levelRoot.requestFocus();
                            LevelScreenEventHandler(mainStage, 1);
                        });
                    }
                    break;
                case ESCAPE: //ESCAPE key is pressed
                    System.out.println("ESCAPE from OptionsScreen");
                    root.getChildren().clear();
                    StackPane titleRoot = new StackPane();
                    mainStage.setScene(new TitleScreen(titleRoot, SCALE).getThisScene()); //setting scene to title screen
                    mainStage.show();
                    titleRoot.requestFocus();
                    TitleScreenEventHandler(mainStage);
                    break;
                case RIGHT:
                    System.out.println("RIGHT from OptionsScreen");
                    optionsScreen.changeBackgroundImage(root, "next"); //changing background image
                    break;
                case LEFT:
                    System.out.println("LEFT from OptionsScreen");
                    optionsScreen.changeBackgroundImage(root, "previous"); //changing background image
                    break;
                case UP:
                    System.out.println("UP from OptionsScreen");
                    optionsScreen.changeCrosshair(root, "next"); //changing crosshair
                    break;
                case DOWN:
                    System.out.println("DOWN from OptionsScreen");
                    optionsScreen.changeCrosshair(root, "previous"); //changing crosshair
                    break;
            }
            crosshair = optionsScreen.getCrosshair((StackPane) optionsScreen.getThisScene().getRoot()); //getting crosshair image from options screen
            backgroundImage = optionsScreen.getBackgroundImage((StackPane) optionsScreen.getThisScene().getRoot()); //getting background image
            foregroundImage = optionsScreen.getForegroundImage((StackPane) optionsScreen.getThisScene().getRoot()); //getting foreground image
        });
    }
    /**
     * Event handler method for the game level screen scene. <br>
     * This method is responsible for handling the key events on the game level screen. <br>
     * This method is also responsible for setting the scene to the title screen when the ESCAPE key is pressed.<br>
     * This method is also responsible for setting the scene to the game level screen when the ENTER key is pressed. <br>
     * This method is also responsible for handling the mouse click events,
     * which represent gunshots on the game level screen.
     * @param mainStage the main stage of the application
     * @param l the level of the game
     */
    private void LevelScreenEventHandler(Stage mainStage, Integer l) {
        StackPane root = (StackPane) mainStage.getScene().getRoot();
        LevelScreen levelScreen = (LevelScreen) mainStage.getScene();
        ArrayList<ImageView> ducks = levelScreen.getDuckImageViews();
        ArrayList<Timeline> duckAnimations = levelScreen.getDuckAnimations();
        ArrayList<Duck> duckObjects = levelScreen.getDuckObjects();
        Media gunshotSound = new Media(new File("../assets/effects/Gunshot.mp3").toURI().toString());
        final int[] level = {l}; //level of the game is stored in an array to be able to change it in the event handler
        root.setOnMouseClicked(e -> { //handling mouse click events
            double x = e.getX() + crosshair.getWidth()/2; //getting x coordinate of mouse click, added half of crosshair width to get the center of the crosshair
            double y = e.getY() + crosshair.getHeight()/2; //getting y coordinate of mouse click added half of crosshair height to get the center of the crosshair
            if (levelScreen.getAmmoCount() > 0 && levelScreen.getDuckCount() > 0) {  //checking if there is ammo left and if there are ducks left
                mediaPlayer = new MediaPlayer(gunshotSound); //playing gunshot sound
                mediaPlayer.setVolume(VOLUME);
                mediaPlayer.play();
                levelScreen.updateAmmoText(); //updating ammo text
            } else { //if there is no ammo left or there are no ducks left
                return; //returning from method
            }
            for (int i = 0; i < ducks.size(); i++) { //looping through ducks
                ImageView duck = ducks.get(i);
                if ((duck.getBoundsInParent().contains(x, y) || duck.getBoundsInParent().intersects(x, y, 1, 1)) && !duckObjects.get(i).getShot()) { //checking if duck is already shot and if mouse click is on duck
                    System.out.println("Duck " + i + " was shot");
                    Timeline duckAnimation = duckAnimations.get(i);
                    duckAnimation.getKeyFrames().clear(); //clearing duck animation key frames
                    Duck duckObject = duckObjects.get(i);
                    duckAnimation.getKeyFrames().add(duckObject.getFallingWingFlappingKeyFrame()); //adding falling wing flapping key frame to duck animation
                    duckAnimation.getKeyFrames().add(duckObject.getFallingMotionKeyFrame()); //adding falling motion key frame to duck animation
                    duckAnimation.setCycleCount(30); //setting cycle count of duck animation to 30 so that the animation is not played infinitely
                    mediaPlayer = new MediaPlayer(duckObject.getFallingSound());
                    mediaPlayer.setVolume(VOLUME);
                    mediaPlayer.play();
                    duckAnimation.play();
                    System.out.println(((LevelScreen) mainStage.getScene()).getAmmoCount());
                    levelScreen.duckShot();
                    duckObjects.get(i).setShot(true);
                    if (levelScreen.getDuckCount() == 0) { //checking if there are no ducks left, if so, level is complete
                        levelComplete[0] = true;
                        level[0]++;
                        break;
                    }
                }
                if (levelScreen.getAmmoCount() == 0 && levelScreen.getDuckCount() != 0) { //checking if there is no ammo left and if there are ducks left
                    levelFailed[0] = true;
                    break;
                }
            }
            if (levelScreen.getAmmoCount() == 0 && levelScreen.getDuckCount() != 0 && levelFailed[0]) { //checking if there is no ammo left and if there are ducks left and if level is failed
                System.out.println("Game Over");
                StackPane gameOverRoot = new StackPane();
                gameOverRoot.setAlignment(Pos.TOP_CENTER);
                Text gameOverText = new Text("GAME OVER");
                gameOverText.setTextAlignment(TextAlignment.CENTER);
                Color color = Color.rgb(241, 159, 20);
                gameOverText.setFill(color);
                gameOverText.setFont(Font.font("Arial", FontWeight.BOLD, 15* SCALE));
                gameOverText.setTranslateY(levelScreen.getScaledHeight()*0.35);
                Text restartText = new Text("Press ENTER to play again\nPress ESC to exit");
                restartText.setTextAlignment(TextAlignment.CENTER);
                restartText.setFill(color);
                restartText.setFont(Font.font("Arial", FontWeight.BOLD, 15* SCALE));
                restartText.setTranslateY(levelScreen.getScaledHeight()*0.35 + 20* SCALE);
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.6), restartText); //creating fade transition for restart text to make it blink
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.setCycleCount(Timeline.INDEFINITE);
                fadeTransition.setAutoReverse(true);
                gameOverRoot.getChildren().addAll(gameOverText, restartText);
                root.getChildren().add(gameOverRoot);
                mediaPlayer = new MediaPlayer(new Media(new File("../assets/effects/GameOver.mp3").toURI().toString())); //playing game over sound
                mediaPlayer.setVolume(VOLUME);
                mediaPlayer.play();
                fadeTransition.play();
                gameOverRoot.requestFocus();
                levelFailed[0] = false;
                gameOver[0] = true; //setting game over to true to be able to restart the game
            } else if (levelScreen.getDuckCount() == 0 && levelComplete[0] && (level[0] - 1) == 6) { //checking if there are no ducks left and if level is complete and if level is 6
                System.out.println("Game Complete");
                StackPane gameCompleteRoot = new StackPane();
                gameCompleteRoot.setAlignment(Pos.TOP_CENTER);
                Text gameCompleteText = new Text("You have completed the game!");
                gameCompleteText.setTextAlignment(TextAlignment.CENTER);
                Color color = Color.rgb(241, 159, 20);
                gameCompleteText.setFill(color);
                gameCompleteText.setFont(Font.font("Arial", FontWeight.BOLD, 15* SCALE));
                gameCompleteText.setTranslateY(levelScreen.getScaledHeight()*0.35);
                Text restartText = new Text("Press ENTER to play again\nPress ESC to exit");
                restartText.setTextAlignment(TextAlignment.CENTER);
                restartText.setFill(color);
                restartText.setFont(Font.font("Arial", FontWeight.BOLD, 15* SCALE));
                restartText.setTranslateY(levelScreen.getScaledHeight()*0.35 + 20* SCALE);
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.6), restartText); //creating fade transition for restart text to make it blink
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.setCycleCount(Timeline.INDEFINITE);
                fadeTransition.setAutoReverse(true);
                gameCompleteRoot.getChildren().addAll(gameCompleteText, restartText);
                root.getChildren().add(gameCompleteRoot);
                mediaPlayer = new MediaPlayer(new Media(new File("../assets/effects/GameCompleted.mp3").toURI().toString())); //playing game complete sound
                mediaPlayer.setVolume(VOLUME);
                mediaPlayer.play();
                fadeTransition.play();
                gameCompleteRoot.requestFocus();
                levelComplete[0] = false;
                gameComplete[0] = true; //setting game complete to true to be able to restart the game
            }
            else if (levelScreen.getDuckCount() == 0 && levelComplete[0]) { //checking if there are no ducks left and if level is complete
                System.out.println("Level " + (level[0] - 1) + " Complete");
                StackPane levelCompleteRoot = new StackPane();
                levelCompleteRoot.setAlignment(Pos.TOP_CENTER);
                Text youWinText = new Text("YOU WIN!");
                youWinText.setTextAlignment(TextAlignment.CENTER);
                Color color = Color.rgb(241, 159, 20);
                youWinText.setFill(color);
                youWinText.setFont(Font.font("Arial", FontWeight.BOLD, 15* SCALE));
                youWinText.setTranslateY(levelScreen.getScaledHeight()*0.35);
                Text nextLevelText = new Text("Press ENTER to play next level");
                nextLevelText.setTextAlignment(TextAlignment.CENTER);
                nextLevelText.setFill(color);
                nextLevelText.setFont(Font.font("Arial", FontWeight.BOLD, 15* SCALE));
                nextLevelText.setTranslateY(levelScreen.getScaledHeight()*0.425);
                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.6), nextLevelText); //creating fade transition for next level text to make it blink
                fadeTransition.setFromValue(1.0);
                fadeTransition.setToValue(0.0);
                fadeTransition.setCycleCount(Timeline.INDEFINITE);
                fadeTransition.setAutoReverse(true);
                levelCompleteRoot.getChildren().addAll(youWinText, nextLevelText);
                root.getChildren().add(levelCompleteRoot);
                mediaPlayer = new MediaPlayer(new Media(new File("../assets/effects/LevelCompleted.mp3").toURI().toString())); //playing level complete sound
                mediaPlayer.setVolume(VOLUME);
                mediaPlayer.play();
                mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.stop());
                fadeTransition.play();
                levelCompleteRoot.requestFocus();
                levelComplete[0] = false;
            }
        });
        root.setOnKeyReleased(e -> {
            switch (e.getCode()) {
                case ESCAPE: //checking if escape is pressed
                    System.out.println("ESCAPE from LevelScreen");
                    root.getChildren().clear();
                    StackPane titleRoot = new StackPane();
                    mainStage.setScene(new TitleScreen(titleRoot, SCALE).getThisScene());
                    mainStage.getScene().setCursor(ImageCursor.DEFAULT);
                    mainStage.show();
                    titleRoot.requestFocus();
                    TitleScreenEventHandler(mainStage);
                    break;
                case ENTER: //checking if enter is pressed
                    System.out.println("ENTER from LevelScreen");
                    if (levelScreen.getDuckCount() == 0 && gameComplete[0]) { //checking if there are no ducks left and if game is complete
                        System.out.println("Enter pressed and game complete");
                        level[0] = 1;
                        root.getChildren().clear();
                        mediaPlayer.stop();
                        StackPane newGameRoot = new StackPane();
                        mainStage.setScene(new LevelScreen(newGameRoot, backgroundImage, foregroundImage, SCALE, 1).getThisScene()); //starting a new game
                        gameComplete[0] = false;
                        mainStage.getScene().setCursor(new ImageCursor(crosshair));
                        mainStage.show();
                        newGameRoot.requestFocus();
                        LevelScreenEventHandler(mainStage, 1);
                    } else if (levelScreen.getDuckCount() == 0) { //checking if there are no ducks left and if level is complete
                        System.out.println("Enter pressed and level complete");
                        root.getChildren().clear();
                        mediaPlayer.stop();
                        StackPane newLevelRoot = new StackPane();
                        mainStage.setScene(new LevelScreen(newLevelRoot, backgroundImage, foregroundImage, SCALE, level[0]).getThisScene()); //starting the next level
                        mainStage.getScene().setCursor(new ImageCursor(crosshair));
                        mainStage.show();
                        newLevelRoot.requestFocus();
                        LevelScreenEventHandler(mainStage, level[0]);
                    } else if (gameOver[0]){ //checking if game is over
                        System.out.println("Enter pressed and game over");
                        root.getChildren().clear();
                        mediaPlayer.stop();
                        StackPane restartedGameRoot = new StackPane();
                        mainStage.setScene(new LevelScreen(restartedGameRoot, backgroundImage, foregroundImage, SCALE, 1).getThisScene()); //restarting the game
                        mainStage.getScene().setCursor(new ImageCursor(crosshair));
                        mainStage.show();
                        restartedGameRoot.requestFocus();
                        LevelScreenEventHandler(mainStage, 1);
                    }
                    else {
                        System.out.println("Enter pressed but level not complete"); //checking if enter is pressed but level is not complete
                    }
                    break;
            }
        });
    }

    /**
     * Sets the scale of the game
     * @param scale the scale of the game visuals
     */
    public static void setScale(Double scale) {
        SCALE = scale;
    }
    /**
     * Gets the scale of the game
     * @return the scale of the game visuals
     */
    public Double getScale() {
        return SCALE;
    }
    /**
     * Sets the volume of the game
     * @param volume the volume of the game
     */
    public static void setVolume(Double volume) {
        VOLUME = volume;
    }
    /**
     * Gets the volume of the game
     * @return the volume of the game
     */
    public Double getVolume() {
        return VOLUME;
    }
}
