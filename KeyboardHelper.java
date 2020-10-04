import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class KeyboardHelper implements KeyListener {
    Set<Integer> pressedKeys = new HashSet<>();

    public KeyboardHelper(Component component) {
        component.addKeyListener(this);
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    public boolean isKeyDown(int key) {
        return pressedKeys.contains(key);
    }
}
