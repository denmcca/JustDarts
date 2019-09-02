package JustDarts.interfaces;

import JustDarts.managers.InputManager;

import javax.swing.text.Position;
import java.awt.*;

public abstract class GameObject {
    protected boolean shouldRemove = false;
    private Point position = new Point(); // TODO: move to Drawable
    private float zBuffer; // for third dim spec (implement later)

    // orientation
    String name = "";
    // connects game object to input management
    public InputManager inputManager;

    public void setPosition(int newPosX, int newPosY){
        System.out.printf("%s %d %d\n", name, newPosX, newPosY);
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

    public void setPosY(int newPosY){
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

    public void subscribeToInputManager(InputManager inputMgr) {
        inputManager = inputMgr;
    }

    public void reset(){ init(); }

    protected abstract void init();
    public abstract void update();

    public boolean shouldRemove() {
        return shouldRemove;
    }
}
