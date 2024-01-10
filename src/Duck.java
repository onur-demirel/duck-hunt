import javafx.animation.KeyFrame;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.util.Duration;
import java.io.File;
import java.util.ArrayList;

/**
 * Duck class
 * This class is used to create a duck object
 * It contains the duck's image, speed, and motion
 * It also contains the duck's sound effects, flying and falling images and animations
 */
public class Duck {
    private Boolean isShot = false; // whether the duck is shot or not
    private Integer flyingImageIndex = 0;
    private Integer fallingImageIndex = 0;
    private Double xSpeed;
    private Double ySpeed;
    private final ArrayList<Image> flyingImages = new ArrayList<>();
    private final ArrayList<Image> fallingImages = new ArrayList<>();
    private final KeyFrame flyingMotionKeyFrame;
    private final KeyFrame wingFlappingKeyFrame;
    private final KeyFrame fallingMotionKeyFrame;
    private final KeyFrame fallingWingFlappingKeyFrame;
    private final ImageView duckImageView = new ImageView();
    private static final Media fallingSound = new Media(new File("../assets/effects/DuckFalls.mp3").toURI().toString());
    /**
     * This constructor creates a duck object.
     * @param duckColor The color of the duck.
     * @param scale The scale of the duck.
     * @param movesDiagonal Whether the duck moves diagonally or not.
     * @param scaledWidth The width of the scene.
     * @param scaledHeight The height of the scene.
     */
    public Duck(String duckColor, Double scale, Boolean movesDiagonal, Double scaledWidth, Double scaledHeight){
        xSpeed = (10 * scale);
        ySpeed = (10 * scale);
        if (movesDiagonal) { // if the duck moves diagonally
            for (int i = 1; i <= 3; i++) { // add the flying images to the array list
                Image duckImage = new Image("file:../assets/duck_" + duckColor + "/" + i + ".png", 0, 0, true, true);
                duckImage = new Image("file:../assets/duck_" + duckColor + "/" + i + ".png", duckImage.getWidth() * scale, duckImage.getHeight() * scale, true, true);
                flyingImages.add(duckImage);
            }
        } else {
            for (int i = 4; i <= 6; i++) { // add the flying images to the array list
                Image duckImage = new Image("file:../assets/duck_" + duckColor + "/" + i + ".png", 0, 0, true, true);
                duckImage = new Image("file:../assets/duck_" + duckColor + "/" + i + ".png", duckImage.getWidth() * scale, duckImage.getHeight() * scale, true, true);
                flyingImages.add(duckImage);
            }
        }
        for (int i = 7; i <= 8; i++) { // add the falling images to the array list
            Image duckImage = new Image("file:../assets/duck_" + duckColor + "/" + i + ".png", 0, 0, true, true);
            duckImage = new Image("file:../assets/duck_" + duckColor + "/" + i + ".png", duckImage.getWidth() * scale, duckImage.getHeight() * scale, true, true);
            fallingImages.add(duckImage);
        }
        wingFlappingKeyFrame = new KeyFrame(Duration.millis(150), e -> { // create the wing flapping key frame
            duckImageView.setImage(flyingImages.get(this.flyingImageIndex));
            this.flyingImageIndex++;
            if (flyingImageIndex == flyingImages.size()) { // if the image index is equal to the size of the array list, reset it to 0
                flyingImageIndex = 0;
            }
        });
        fallingWingFlappingKeyFrame = new KeyFrame(Duration.millis(100), e -> { // create the falling wing flapping key frame
            duckImageView.setImage(fallingImages.get(this.fallingImageIndex));
            this.fallingImageIndex++;
            if (fallingImageIndex == fallingImages.size()) { // if the image index is equal to the size of the array list, keep it at the last image
                fallingImageIndex = fallingImages.size()-1;
            }
        });
        fallingMotionKeyFrame = new KeyFrame(Duration.millis(100), e -> { // create the falling motion key frame
            duckImageView.setRotate(0); // reset the rotation
            duckImageView.setTranslateY(duckImageView.getTranslateY() + Math.abs(ySpeed)); // move the duck down
            if (duckImageView.getTranslateY() > scaledHeight-duckImageView.getImage().getHeight() || duckImageView.getTranslateY() < 0) { // if the duck hits the bottom of the scene
                ySpeed = 0.0;
            }
        });
        if (movesDiagonal){ // if the duck moves diagonally
            flyingMotionKeyFrame = new KeyFrame(Duration.millis(150), e -> { // create the flying motion key frame
                duckImageView.setTranslateX(duckImageView.getTranslateX() + xSpeed); // move the duck horizontally
                duckImageView.setTranslateY(duckImageView.getTranslateY() - ySpeed); // move the duck vertically
                if (duckImageView.getTranslateY() < 0 + duckImageView.getImage().getHeight()/10) { // if the duck hits the top of the scene
                    ySpeed = -ySpeed; // reverse the vertical speed
                    duckImageView.setTranslateY(duckImageView.getTranslateY() + Math.abs(ySpeed)); // move the duck down to prevent it from getting stuck
                    if (xSpeed < 0){ // if the duck is moving left
                        if (duckImageView.getScaleX() != -1){ // if the duck is not facing left
                            duckImageView.setScaleX(-1); // make the duck face left
                        }
                        if (duckImageView.getRotate() != -90){ // if the duck is not rotated 90 degrees
                            duckImageView.setRotate(-90); // rotate the duck 90 degrees
                        }
                    } else if (xSpeed > 0){ // if the duck is moving right
                        if (duckImageView.getScaleX() != 1){ // if the duck is not facing right
                            duckImageView.setScaleX(1); // make the duck face right
                        }
                        if (duckImageView.getRotate() != 90){ // if the duck is not rotated 90 degrees
                            duckImageView.setRotate(90); // rotate the duck 90 degrees
                        }
                    }
                }
                if (duckImageView.getTranslateY()+duckImageView.getImage().getHeight() >= scaledHeight*0.99) { // if the duck hits the bottom of the scene
                    ySpeed = -ySpeed; // reverse the vertical speed
                    if (xSpeed < 0){ // if the duck is moving left
                        if (duckImageView.getScaleX() != -1){
                            duckImageView.setScaleX(-1);
                        }
                        if (duckImageView.getRotate() != 0){
                            duckImageView.setRotate(0);
                        }
                    } else if (xSpeed > 0){ // if the duck is moving right
                        if (duckImageView.getScaleX() != 1){
                            duckImageView.setScaleX(1);
                        }
                        if (duckImageView.getRotate() != 0){
                            duckImageView.setRotate(0);
                        }
                    }
                }
                if (duckImageView.getTranslateX() <= 0 + duckImageView.getImage().getWidth()/10) { // if the duck hits the left side of the scene
                    xSpeed = -xSpeed; // reverse the horizontal speed
                    if (ySpeed > 0){ // if the duck is moving down
                        if (duckImageView.getScaleX() != 1){
                            duckImageView.setScaleX(1);
                        }
                        if (duckImageView.getRotate() != 0){
                            duckImageView.setRotate(0);
                        }
                    } else if (ySpeed < 0){ // if the duck is moving up
                        if (duckImageView.getScaleX() != 1){
                            duckImageView.setScaleX(1);
                        }
                        if (duckImageView.getRotate() != 90){
                            duckImageView.setRotate(90);
                        }
                    }
                }
                if (duckImageView.getTranslateX()+duckImageView.getImage().getWidth() >= scaledWidth) { // if the duck hits the right side of the scene
                    xSpeed = -xSpeed; // reverse the horizontal speed
                    duckImageView.setTranslateX(scaledWidth-duckImageView.getImage().getWidth()); // move the duck back into the scene, to ensure it doesn't get stuck
                    if (ySpeed > 0){ // if the duck is moving down
                        if (duckImageView.getScaleX() != -1){
                            duckImageView.setScaleX(-1);
                        }
                        if (duckImageView.getRotate() != 0){
                            duckImageView.setRotate(0);
                        }
                    } else if (ySpeed < 0){ // if the duck is moving up
                        if (duckImageView.getScaleX() != -1){
                            duckImageView.setScaleX(-1);
                        }
                        if (duckImageView.getRotate() != -90){
                            duckImageView.setRotate(-90);
                        }
                    }
                }
            });
        }
        else  { // if the duck moves horizontally
            final int[] UpOrDown = {(int) (Math.random() * 2)}; // randomly choose whether the duck moves up or down, to make it more realistic
            flyingMotionKeyFrame = new KeyFrame(Duration.millis(150), e -> { // create the flying motion key frame
                duckImageView.setTranslateX(duckImageView.getTranslateX() + xSpeed); // move the duck horizontally
                if (UpOrDown[0] % 2 == 0) { // if the duck is moving up
                    duckImageView.setTranslateY(duckImageView.getTranslateY() + ySpeed /2);
                } else { // if the duck is moving down
                    duckImageView.setTranslateY(duckImageView.getTranslateY() - ySpeed /2);
                }
                UpOrDown[0]++; // increment the counter
                // if the duck hits the right or left side of the scene
                if (duckImageView.getTranslateX() > scaledWidth-duckImageView.getImage().getWidth() || duckImageView.getTranslateX() < 0 + duckImageView.getImage().getWidth()/10) {
                    xSpeed = -xSpeed; // reverse the horizontal speed
                    duckImageView.setScaleX(duckImageView.getScaleX()*-1); // make the duck face the other way
                }
            });
        }
    }

    /**
     * This method returns the ImageView of the duck
     * @return the ImageView of the duck
     */
    public ImageView getDuckImageView() {
        return duckImageView;
    }
    /**
     * This method returns the status of the duck
     * @return the status of the duck
     */
    public Boolean getShot() {
        return isShot;
    }
    /**
     * This method sets the status of the duck
     * @param shot the status of the duck
     */
    public void setShot(Boolean shot) {
        isShot = shot;
    }
    /**
     * This method returns the falling sound of the duck
     * @return the falling sound of the duck
     */
    public Media getFallingSound() {
        return fallingSound;
    }

    /**
     * This method sets the speed of the duck
     * @param speedMultiplier the speed multiplier of the duck
     */
    public void setSpeed(Double speedMultiplier){
        this.xSpeed = speedMultiplier * this.xSpeed;
        this.ySpeed = speedMultiplier * this.ySpeed;
    }
    /**
     * This method returns the key frame for the flying motion animation
     * @return the key frame for the flying motion animation
     */
    public KeyFrame getFlyingMotionKeyFrame() {
        return flyingMotionKeyFrame;
    }
    /**
     * This method returns the key frame for the wing flapping animation
     * @return the key frame for the wing flapping animation
     */
    public KeyFrame getWingFlappingKeyFrame() {
        return wingFlappingKeyFrame;
    }
    /**
     * This method returns the key frame for the falling wing flapping animation
     * @return the key frame for the falling wing flapping animation
     */
    public KeyFrame getFallingWingFlappingKeyFrame() {
        return fallingWingFlappingKeyFrame;
    }
    /**
     * This method returns the key frame for the falling motion animation
     * @return the key frame for the falling motion animation
     */
    public KeyFrame getFallingMotionKeyFrame() {
        return fallingMotionKeyFrame;
    }
    /**
     * This method returns the speed of the duck
     * @return the speed of the duck
     */
    public Double getSpeed(){
        return xSpeed;
    }
}
