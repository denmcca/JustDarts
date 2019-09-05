package interfaces;

import java.awt.*;

public abstract class GameObject {
    protected boolean shouldRemove = false;
    private Point position = new Point(); // TODO: move to Drawable
    //protected float distance; // TODO: z-buffer for third dimension calculations

    // orientation
    private String name = "";

    public void setPosition(int newPosX, int newPosY){
        position.setLocation(newPosX, newPosY);
    }

    public void setPosition(Point pos){
        position = new Point(pos);
    }
    public Point getPosition(){
        return position;
    }

    public void setPosX(int newPosX){
        position.x = newPosX;
    }
    public int getPosX(){
        return position.x;
    }

    public void setPosY(int newPosY) {
        position.y = newPosY;
    }
    public int getPosY(){
        return position.y;
    }

    public void setName(String newName){
        name = newName;
    }
    public String getName(){
        return name;
    }

    public void reset(){ init(); }

    protected abstract void init();
    public abstract void update();

    public boolean shouldRemove() {
        return shouldRemove;
    }
}
