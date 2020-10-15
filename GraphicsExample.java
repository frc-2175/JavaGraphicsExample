import java.awt.*;
import java.awt.event.*;

public class GraphicsExample {
    public static void main(String[] args) throws InterruptedException {
        Frame window = createWindow();
        GraphicsCanvas canvas = createCanvas(window);
        MouseHelper mouse = new MouseHelper(canvas);
        KeyboardHelper keyboard = new KeyboardHelper(canvas);

        Graphics2D g = canvas.getGraphics2D(); // a short name is ok because we will be using it a lot!

        // This is where our real program starts!

        double startTime = getCurrentTime();

        int characterX = 100;
        int characterY = 100;
        int characterSize = 20;
        int characterSpeed = 2;

        int numButtonClicks = 0;

        while (true) {
            canvas.clear(); // clear everything we drew on the last frame

            // Get how long it has been since the program started
            double timeSinceStart = getCurrentTime() - startTime;

            // Draw some example stuff
            Font plainFont = new Font("Serif", Font.PLAIN, 24);
            drawText(g, "Hello, this is a graphics example!", Color.BLACK, plainFont, 20, 20);
            drawText(g, "Press the arrow keys and move the mouse.", Color.BLACK, plainFont, 20, 50);
            
            drawRectangle(g, Color.ORANGE, 400, 200, 100, 50);
            drawRectangleOutline(g, Color.GRAY, 3, 450, 175, 100, 100);
            drawEllipse(g, Color.GREEN, 200, 200, 100, 50);
            drawCircle(g, Color.RED, 400, 400, 75);
            drawLine(g, Color.CYAN, 2, 200, 200, 400, 400);
            drawTriangle(g, Color.BLUE, 50, 200, 150, 200, 100, 300);

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
            drawRectangle(g, fancyColor, 600, 400, (int)(Math.cos(timeSinceStart) * 100), (int)(Math.sin(timeSinceStart) * 100));

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

            drawCircle(g, Color.MAGENTA, characterX, characterY, characterSize);
            if (mouse.isInRectangle(characterX, characterY, characterSize, characterSize)) {
                drawText(g, "Hello!", Color.BLACK, plainFont, characterX, characterY);
            }

            if (doButton(g, mouse, Color.BLUE, 300, 100, 50, 25)) {
                numButtonClicks += 1;
            }
            drawText(g, "Button clicked " + numButtonClicks + " times", Color.BLACK, plainFont, 360, 120);

            // Draw a little mouse cursor
            drawCircle(g, mouse.isPrimaryButtonDown() ? Color.YELLOW : Color.BLUE, mouse.getX()-5, mouse.getY()-5, 10);

            canvas.repaint(); // tell the canvas to actually show all the stuff we just did
            mouse.resetForNextFrame();
            keyboard.resetForNextFrame();
            Thread.sleep(1000 / 60); // pause for the number of milliseconds that gives us 60 frames per second
        }
    }

    // ----------------------------------------------------
    // Functions to help with the more fiddly window setup
    // ----------------------------------------------------

    public static Frame createWindow() {
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

        return window;
    }

    public static GraphicsCanvas createCanvas(Frame window) {
        GraphicsCanvas canvas = new GraphicsCanvas(window.getWidth(), window.getHeight());
        window.add(canvas);
        window.validate();

        canvas.initializeBuffer();
        
        Graphics2D g = canvas.getGraphics2D();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); // draw everything with antialiasing (smoother edges)
        
        return canvas;
    }

    // --------------------------------------------------------
    // Helper functions for drawing, and just writing programs
    // --------------------------------------------------------

    public static void drawLine(Graphics2D g, Color color, int thickness, int x1, int y1, int x2, int y2) {
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.drawLine(x1, y1, x2, y2);
    }

    public static void drawEllipse(Graphics2D g, Color color, int x, int y, int width, int height) {
        g.setColor(color);
        g.fillOval(x, y, width, height);
    }

    public static void drawEllipseOutline(Graphics2D g, Color color, int thickness, int x, int y, int width, int height) {
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.drawOval(x, y, width-1, height-1); // This function draws an oval that is one pixel too wide for some reason. I don't know why!
    }

    public static void drawCircle(Graphics2D g, Color color, int x, int y, int radius) {
        drawEllipse(g, color, x, y, radius, radius);
    }

    public static void drawCircleOutline(Graphics2D g, Color color, int thickness, int x, int y, int radius) {
        drawEllipseOutline(g, color, thickness, x, y, radius, radius);
    }

    public static void drawRectangle(Graphics2D g, Color color, int x, int y, int width, int height) {
        g.setColor(color);
        g.fillRect(Math.min(x, x + width), Math.min(y, y + height), Math.abs(width), Math.abs(height));
    }

    public static void drawRectangleOutline(Graphics2D g, Color color, int thickness, int x, int y, int width, int height) {
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.drawRect(Math.min(x, x + width), Math.min(y, y + height), Math.abs(width), Math.abs(height));
    }

    public static void drawTriangle(Graphics2D g, Color color, int x1, int y1, int x2, int y2, int x3, int y3) {
        g.setColor(color);
        g.fillPolygon(new int[]{x1, x2, x3}, new int[]{y1, y2, y3}, 3);
    }

    public static void drawTriangleOutline(Graphics2D g, Color color, int thickness, int x1, int y1, int x2, int y2, int x3, int y3) {
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.drawPolygon(new int[]{x1, x2, x3}, new int[]{y1, y2, y3}, 3);
    }

    public static void drawText(Graphics2D g, String text, Color color, Font font, int x, int y) {
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, x, y);
    }

    /**
     * Draws a rectangular button and returns true if the button was clicked.
     * 
     * <pre>
     * if (button(g, mouse, Color.BLUE, 300, 300, 100, 25)) {
     *     System.out.println("The button was clicked!");
     * }
     * </pre>
     */
    public static boolean doButton(Graphics2D g, MouseHelper mouse, Color color, int x, int y, int width, int height) {
        boolean mouseOver = mouse.isInRectangle(x, y, width, height);
        
        Color desiredColor = color;
        if (mouseOver) {
            Color lighter = new Color(
                Math.min(color.getRed() + 50, 255),
                Math.min(color.getGreen() + 50, 255),
                Math.min(color.getBlue() + 50, 255),
                color.getAlpha()
            );
            desiredColor = lighter;

            if (mouse.isPrimaryButtonDown()) {
                Color darker = new Color(
                    Math.max(color.getRed() - 30, 0),
                    Math.max(color.getGreen() - 30, 0),
                    Math.max(color.getBlue() - 30, 0),
                    color.getAlpha()
                );
                desiredColor = darker;
            }
        }
        
        drawRectangle(g, desiredColor, x, y, width, height);

        if (mouseOver && mouse.isPrimaryButtonUpThisFrame()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets the current time in seconds instead of milliseconds, for convenience.
     */
    public static double getCurrentTime() {
        return System.currentTimeMillis() / 1000.0;
    }
}