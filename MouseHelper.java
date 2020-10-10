import java.awt.*;
import java.awt.event.*;

public class MouseHelper implements MouseListener {
    Component component;

    boolean primaryButtonDown = false;
    boolean secondaryButtonDown = false;
    boolean middleButtonDown = false;

    static enum MouseAction {
        Down,
        Up,
    }

    MouseAction primaryButtonThisFrame = null;
    MouseAction secondaryButtonThisFrame = null;
    MouseAction middleButtonThisFrame = null;

    public MouseHelper(Component component) {
        this.component = component;
        component.addMouseListener(this);
    }

    public int getX() {
        int componentX = (int) component.getLocationOnScreen().getX();
        int mouseScreenX = (int) MouseInfo.getPointerInfo().getLocation().getX();
        return mouseScreenX - componentX;
    }

    public int getY() {
        int componentY = (int) component.getLocationOnScreen().getY();
        int mouseScreenY = (int) MouseInfo.getPointerInfo().getLocation().getY();
        return mouseScreenY - componentY;
    }

    public boolean isPrimaryButtonDown() {
        return primaryButtonDown;
    }

    public boolean isSecondaryButtonDown() {
        return secondaryButtonDown;
    }

    public boolean isMiddleButtonDown() {
        return middleButtonDown;
    }

    public boolean isPrimaryButtonDownThisFrame() {
        return primaryButtonThisFrame == MouseAction.Down;
    }
    
    public boolean isSecondaryButtonDownThisFrame() {
        return secondaryButtonThisFrame == MouseAction.Down;
    }

    public boolean isMiddleButtonDownThisFrame() {
        return middleButtonThisFrame == MouseAction.Down;
    }

    public boolean isPrimaryButtonUpThisFrame() {
        return primaryButtonThisFrame == MouseAction.Up;
    }
    
    public boolean isSecondaryButtonUpThisFrame() {
        return secondaryButtonThisFrame == MouseAction.Up;
    }

    public boolean isMiddleButtonUpThisFrame() {
        return middleButtonThisFrame == MouseAction.Up;
    }

    public void resetForNextFrame() {
        primaryButtonThisFrame = null;
        secondaryButtonThisFrame = null;
        middleButtonThisFrame = null;
    }

    public boolean isInRectangle(int x, int y, int width, int height) {
        int mouseX = getX();
        int mouseY = getY();

        return (
            x <= mouseX && mouseX <= x + width
            && y <= mouseY && mouseY <= y + height
        );
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                primaryButtonDown = true;
                primaryButtonThisFrame = MouseAction.Down;
                break;
            case MouseEvent.BUTTON2:
                secondaryButtonDown = true;
                secondaryButtonThisFrame = MouseAction.Down;
                break;
            case MouseEvent.BUTTON3:
                middleButtonDown = true;
                middleButtonThisFrame = MouseAction.Down;
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                primaryButtonDown = false;
                primaryButtonThisFrame = MouseAction.Up;
                break;
            case MouseEvent.BUTTON2:
                secondaryButtonDown = false;
                secondaryButtonThisFrame = MouseAction.Up;
                break;
            case MouseEvent.BUTTON3:
                middleButtonDown = false;
                middleButtonThisFrame = MouseAction.Up;
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}