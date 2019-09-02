package JustDarts.interfaces;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Drawable extends GameObject {
    protected float frameDelay = 0;
    protected ArrayList<ImageIcon> frames;
    protected ArrayList<Integer> frameIndexSequence;
    protected int currentFrame;
    protected Dimension imgBaseDim = new Dimension();
    public static int SCALE = 1;
    public static Dimension WINDOW_SIZE = new Dimension(640, 480);
    protected boolean shouldDraw;

    public Drawable() {
        frames = new ArrayList<>();
        frameIndexSequence = new ArrayList<>();
        currentFrame = 0;
        shouldDraw = true;
    }

    public void draw(JPanel p, Graphics g) {
        if (shouldDraw)
            // paints current frame from frame list based on current frame from sequence index
            frames.get(frameIndexSequence.get(currentFrame)).paintIcon(p, g, getPosX(), getPosY());
    }

    protected void prepareFramesList(String[] imageFilePaths) {
        String pathToImageDir = "../img/";
        for (String fileName : imageFilePaths) {
            frames.add(createImageIcon(pathToImageDir + fileName, imgBaseDim.width, imgBaseDim.height));
        }
    }

    public ImageIcon createImageIcon(String imgPath, int width, int height)
    {
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource(imgPath));
            icon.setImage(icon.getImage().getScaledInstance(width * SCALE,
                    height * SCALE, Image.SCALE_SMOOTH));
            return icon;
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

    public static void SET_SCALE(int scale) {
        SCALE = scale;
    }

    public void setPosX(int posX){
        super.setPosX(posX);
    }

    public int getWidth() {
        return frames.get(frameIndexSequence.get(currentFrame)).getIconWidth();
    }

    public int getHeight() {

        return frames.get(frameIndexSequence.get(currentFrame)).getIconHeight();
    }

    public void setName(String name) {
        super.setName(name);
    }

    public ImageIcon getCurrentFrameImage() {
        return frames.get(currentFrame);
    }

    public void setCurrentFrame(int currentFrame) {
        if (currentFrame > -1 && currentFrame < frames.size())
            this.currentFrame = currentFrame;
        else System.out.printf("Invalid currentFrame given: out of range (%d)\n", currentFrame);

    }
}