import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

/**
 * A KeyboardHelper object helps keep track of which keys are pressed.
 */
public class KeyboardHelper implements KeyListener {
    Set<Integer> pressedKeys = new HashSet<>();
    Set<Integer> pressedKeysThisFrame = new HashSet<>();
    Set<Integer> releasedKeysThisFrame = new HashSet<>();

    /**
     * Constructs a {@link KeyboardHelper} object for the given UI component.
     */
    public KeyboardHelper(Component component) {
        component.addKeyListener(this);
    }

    /**
     * Call this function at the end of your frame to make sure that the "ThisFrame" functions
     * do the right thing.
     */
    public void resetForNextFrame() {
        pressedKeysThisFrame.clear();
        releasedKeysThisFrame.clear();
    }

    /**
     * Returns true if the given key is held down.
     * @param key A key code. Usually you will do something like KeyEvent.VK_WHATEVER.
     */
    public boolean isKeyDown(int key) {
        return pressedKeys.contains(key);
    }

    /**
     * Returns true if the given key was pressed on the current frame.
     * @param key A key code. Usually you will do something like KeyEvent.VK_WHATEVER.
     */
    public boolean isKeyDownThisFrame(int key) {
        return pressedKeysThisFrame.contains(key);
    }

    /**
     * Returns true if the given key was released on the current frame.
     * @param key A key code. Usually you will do something like KeyEvent.VK_WHATEVER.
     */
    public boolean isKeyUpThisFrame(int key) {
        return releasedKeysThisFrame.contains(key);
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
}
