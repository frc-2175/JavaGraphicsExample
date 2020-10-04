import java.awt.*;
import java.awt.event.*;

public class MouseHelper implements MouseListener {
    Component component;

    boolean primaryButtonDown = false;
    boolean secondaryButtonDown = false;
    boolean middleButtonDown = false;

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
                break;
            case MouseEvent.BUTTON2:
                secondaryButtonDown = true;
                break;
            case MouseEvent.BUTTON3:
                middleButtonDown = true;
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                primaryButtonDown = false;
                break;
            case MouseEvent.BUTTON2:
                secondaryButtonDown = false;
                break;
            case MouseEvent.BUTTON3:
                middleButtonDown = false;
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}