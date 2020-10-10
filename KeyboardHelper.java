import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class KeyboardHelper implements KeyListener {
    Set<Integer> pressedKeys = new HashSet<>();
    Set<Integer> pressedKeysThisFrame = new HashSet<>();
    Set<Integer> releasedKeysThisFrame = new HashSet<>();

    public KeyboardHelper(Component component) {
        component.addKeyListener(this);
    }

    public void resetForNextFrame() {
        pressedKeysThisFrame.clear();
        releasedKeysThisFrame.clear();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
        pressedKeysThisFrame.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
        releasedKeysThisFrame.add(e.getKeyCode());
    }

    public boolean isKeyDown(int key) {
        return pressedKeys.contains(key);
    }

    public boolean isKeyDownThisFrame(int key) {
        return pressedKeysThisFrame.contains(key);
    }

    public boolean isKeyUpThisFrame(int key) {
        return releasedKeysThisFrame.contains(key);
    }
}
