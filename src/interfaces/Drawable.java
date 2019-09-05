package interfaces;

import managers.RenderManager;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public abstract class Drawable extends GameObject {
    protected ArrayList<Image> frames;
    protected ArrayList<Integer> frameIndexSequence;
    protected int currentFrame;
    protected Dimension imgBaseDim = new Dimension();
    protected static int SCALE = 1;
    protected int scaleMod = 1;
    protected static Dimension WINDOW_SIZE = new Dimension(640, 480);
    protected boolean shouldDraw;

    public Drawable() {
        frames = new ArrayList<>();
        frameIndexSequence = new ArrayList<>();
        currentFrame = 0;
        shouldDraw = true;
    }



    public void draw(JPanel p, Graphics2D g) {
        if (shouldDraw)
            // paints current frame from frame list based on current frame from sequence index
            g.drawImage(frames.get(frameIndexSequence.get(currentFrame)), getPosX(), getPosY(), null);
    }

    protected void prepareFramesList(String[] imageFilePaths) {
        for (String fileName : imageFilePaths) {
            frames.add(createImage(RenderManager.PATH_TO_DIR + fileName, imgBaseDim.width, imgBaseDim.height));
        }
    }

    private Image createImage(String imgPath, int width, int height)
    {
        try {
            InputStream is = getClass().getClassLoader().getResourceAsStream(imgPath);
            Image image = ImageIO.read(Objects.requireNonNull(is));
            return image.getScaledInstance(width * SCALE, height * SCALE, Image.SCALE_SMOOTH);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void update() {

    }

    public void enableDraw() {
        shouldDraw = true;
    }

    public void disableDraw() {
        shouldDraw = false;
    }

    public void setFrameIndexSequence(Integer[] frameIndexSequenceIn){
        this.frameIndexSequence = new ArrayList<>(Arrays.asList(frameIndexSequenceIn));
    }

    public static void SET_WINDOW_SIZE(int width, int height) {
        System.out.printf("WINDOW_SIZE changed to %d x % d\n", width, height);
        WINDOW_SIZE = new Dimension(width, height);
    }

    // sets the scale for all drawable game objects (static)
    public static void SET_SCALE(int scale) {
        SCALE = scale;
    }

    public int getWidth() {
        return frames.get(frameIndexSequence.get(currentFrame)).getWidth(null);
    }

    public int getHeight() {

        return frames.get(frameIndexSequence.get(currentFrame)).getHeight(null);
    }

    public void setName(String name) {
        super.setName(name);
    }

    public Image getCurrentFrameImage() {
        return frames.get(currentFrame);
    }

    public void setCurrentFrame(int currentFrame) {
        if (currentFrame > -1 && currentFrame < frames.size())
            this.currentFrame = currentFrame;
        else System.out.printf("Invalid currentFrame given: out of range (%d)\n", currentFrame);

    }
}