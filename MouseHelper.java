import java.awt.*;
import java.awt.event.*;

/**
 * A MouseHelper object helps keeps track of mouse position and mouse clicks in your UI.
 */
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

    /**
     * Constructs a {@link MouseHelper} object for the given UI component.
     */
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

    /**
     * Gets the mouse's position as a {@link Point}.
     */
    public Point getXY() {
        return new Point(getX(), getY());
    }

    /**
     * Returns true if the mouse's primary button is held down.
     * (The primary button is usually left click.)
     */
    public boolean isPrimaryButtonDown() {
        return primaryButtonDown;
    }

    /**
     * Returns true if the mouse's secondary button is held down.
     * (The secondary button is usually right click, or two-finger click on many trackpads.)
     */
    public boolean isSecondaryButtonDown() {
        return secondaryButtonDown;
    }

    /**
     * Returns true if the mouse's middle button is held down.
     */
    public boolean isMiddleButtonDown() {
        return middleButtonDown;
    }

    /**
     * Returns true if the mouse's primary button was pressed on the current frame.
     * (The primary button is usually left click.)
     */
    public boolean isPrimaryButtonDownThisFrame() {
        return primaryButtonThisFrame == MouseAction.Down;
    }
    
    /**
     * Returns true if the mouse's secondary button was pressed on the current frame.
     * (The secondary button is usually right click, or two-finger click on many trackpads.)
     */
    public boolean isSecondaryButtonDownThisFrame() {
        return secondaryButtonThisFrame == MouseAction.Down;
    }

    /**
     * Returns true if the mouse's middle button was pressed on the current frame.
     */
    public boolean isMiddleButtonDownThisFrame() {
        return middleButtonThisFrame == MouseAction.Down;
    }

    /**
     * Returns true if the mouse's primary button was released on the current frame.
     * (The primary button is usually left click.)
     */
    public boolean isPrimaryButtonUpThisFrame() {
        return primaryButtonThisFrame == MouseAction.Up;
    }
    
    /**
     * Returns true if the mouse's secondary button was released on the current frame.
     * (The secondary button is usually right click, or two-finger click on many trackpads.)
     */
    public boolean isSecondaryButtonUpThisFrame() {
        return secondaryButtonThisFrame == MouseAction.Up;
    }

    /**
     * Returns true if the mouse's middle button was released on the current frame.
     */
    public boolean isMiddleButtonUpThisFrame() {
        return middleButtonThisFrame == MouseAction.Up;
    }

    /**
     * Call this function at the end of your frame to make sure that the "ThisFrame" functions
     * do the right thing.
     */
    public void resetForNextFrame() {
        primaryButtonThisFrame = null;
        secondaryButtonThisFrame = null;
        middleButtonThisFrame = null;
    }

    /**
     * Returns true if the mouse position is inside the given rectangle.
     * (See the {@link Rectangle} class.)
     */
    public boolean isInRectangle(Rectangle rect) {
        int mouseX = getX();
        int mouseY = getY();

        return (
            rect.x <= mouseX && mouseX <= rect.x + rect.width
            && rect.y <= mouseY && mouseY <= rect.y + rect.height
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