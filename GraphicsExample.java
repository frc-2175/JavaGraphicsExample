import java.awt.*;
import java.awt.event.*;
import java.util.HashSet;
import java.util.Set;

public class GraphicsExample {
    public static void main(String[] args) throws InterruptedException {
        Frame window = new Frame(); // Frame is a thing from java.awt that shows a window
        window.setSize(800, 600);
        window.setVisible(true); // this makes the window actually show up
        window.setResizable(false); // resizing makes stuff more complicated!
        window.addWindowListener(new WindowAdapter() { // make the program quit when we click the close button
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0); // Stop the program. The number is an error code for the operating system, but 0
                                // means no error.
            }
        });

        // Set up our canvas and add it to the window.
        GraphicsCanvas canvas = new GraphicsCanvas(window.getWidth(), window.getHeight());
        window.add(canvas);
        canvas.initializeBuffer();
        Graphics2D g = canvas.getGraphics2D(); // a short name is ok because we will be using it a lot!

        MouseHelper mouse = new MouseHelper(canvas);
        KeyboardHelper keyboard = new KeyboardHelper(canvas);

        // Now we get to do our game / program / whatever!

        int characterX = 100;
        int characterY = 100;
        int characterSize = 20;
        int characterSpeed = 2;

        double startTime = System.currentTimeMillis() / 1000.0;

        while (true) {
            canvas.clear(); // clear everything we drew on the last frame

            // Get how long it has been since the program started
            double timeSinceStart = (System.currentTimeMillis() / 1000.0) - startTime;

            // Draw everything with antialiasing (smoother edges)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Draw some example stuff
            Font plainFont = new Font("Serif", Font.PLAIN, 24);
            text(g, "Hello, this is a graphics example!", Color.BLACK, plainFont, 20, 20);
            text(g, "Press the arrow keys and move the mouse.", Color.BLACK, plainFont, 20, 50);
            
            rectangle(g, Color.ORANGE, 400, 200, 100, 50);
            rectangleOutline(g, Color.GRAY, 3, 450, 175, 100, 100);
            ellipse(g, Color.GREEN, 200, 200, 100, 50);
            circle(g, Color.RED, 400, 400, 75);
            line(g, Color.CYAN, 2, 200, 200, 400, 400);

            // Custom colors can be done by doing `new Color()` with four numbers
            // ranging from 0 to 255. Google "color picker" to make it easier to find
            // the color you want! In this example I'm just doing a thing that cycles
            // through colors.
            Color fancyColor = new Color(
                (int) (timeSinceStart * 100) % 255, // red
                0, // green
                255, // blue
                255 // "alpha" (0 is transparent, 255 is opaque)
            );
            rectangle(g, fancyColor, 600, 400, (int)(Math.cos(timeSinceStart) * 100), (int)(Math.sin(timeSinceStart) * 100));

            // Move and draw our little character
            if (keyboard.isKeyDown(KeyEvent.VK_LEFT)) {
                characterX -= characterSpeed;
            }
            if (keyboard.isKeyDown(KeyEvent.VK_RIGHT)) {
                characterX += characterSpeed;
            }
            if (keyboard.isKeyDown(KeyEvent.VK_UP)) {
                characterY -= characterSpeed;
            }
            if (keyboard.isKeyDown(KeyEvent.VK_DOWN)) {
                characterY += characterSpeed;
            }

            circle(g, Color.MAGENTA, characterX, characterY, characterSize);
            if (mouse.isInRectangle(characterX, characterY, characterSize, characterSize)) {
                text(g, "Hello!", Color.BLACK, plainFont, characterX, characterY);
            }
            
            // Draw a little mouse cursor
            circle(g, mouse.isPrimaryButtonDown() ? Color.YELLOW : Color.BLUE, mouse.getX()-5, mouse.getY()-5, 10);

            canvas.repaint(); // tell the canvas to actually show all the stuff we just did
            Thread.sleep(1000 / 60); // pause for the number of milliseconds that gives us 60 frames per second
        }
    }

    // ------------------------------------
    // Helper functions for drawing!
    // ------------------------------------

    public static void line(Graphics2D g, Color color, int thickness, int x1, int y1, int x2, int y2) {
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.drawLine(x1, y1, x2, y2);
    }

    public static void ellipse(Graphics2D g, Color color, int x, int y, int width, int height) {
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }

    public static void ellipseOutline(Graphics2D g, Color color, int thickness, int x, int y, int width, int height) {
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.drawOval(x, y, width-1, height-1); // This function draws an oval that is one pixel too wide for some reason. I don't know why!
    }

    public static void circle(Graphics2D g, Color color, int x, int y, int radius) {
        ellipse(g, color, x, y, radius, radius);
    }

    public static void circleOutline(Graphics2D g, Color color, int thickness, int x, int y, int radius) {
        ellipseOutline(g, color, thickness, x, y, radius, radius);
    }

    public static void rectangle(Graphics2D g, Color color, int x, int y, int width, int height) {
        g.setColor(color);
        g.fillRect(Math.min(x, x + width), Math.min(y, y + height), Math.abs(width), Math.abs(height));
    }

    public static void rectangleOutline(Graphics2D g, Color color, int thickness, int x, int y, int width, int height) {
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.drawRect(Math.min(x, x + width), Math.min(y, y + height), Math.abs(width), Math.abs(height));
    }

    public static void text(Graphics2D g, String text, Color color, Font font, int x, int y) {
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, x, y);
    }

    // ---------------------------------
    // Helper classes for user input!
    // ---------------------------------

    public static class MouseHelper implements MouseListener {
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

    public static class KeyboardHelper implements KeyListener {
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
}