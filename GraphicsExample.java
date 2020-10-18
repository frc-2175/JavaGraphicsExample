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
        int characterSize = 10;
        int characterSpeed = 2;

        int numButtonClicks = 0;

        while (true) {
            canvas.clear(); // clear everything we drew on the last frame

            // Get how long it has been since the program started
            double timeSinceStart = getCurrentTime() - startTime;

            // Draw some example stuff
            Font plainFont = new Font("Serif", Font.PLAIN, 24);
            drawText(g, "Hello, this is a graphics example!", Color.BLACK, plainFont, new Point(20, 20));
            drawText(g, "Press the arrow keys and move the mouse.", Color.BLACK, plainFont, new Point(20, 50));
            
            drawRectangle(g, Color.ORANGE, new Rectangle(400, 200, 100, 50));
            drawRectangleOutline(g, Color.GRAY, 3, new Rectangle(450, 175, 100, 100));
            drawEllipse(g, Color.GREEN, new Point(200, 200), 30, 15);
            drawCircle(g, Color.RED, new Point(400, 400), 40);
            drawLine(g, Color.CYAN, 2, new Point(200, 200), new Point(400, 400));
            drawTriangle(g, Color.BLUE, new Point(50, 200), new Point(150, 200), new Point(100, 300));

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
            drawRectangle(g, fancyColor, new Rectangle(
                600,
                400,
                (int)(Math.cos(timeSinceStart) * 100),
                (int)(Math.sin(timeSinceStart) * 100)
            ));

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

            Point characterPosition = new Point(characterX, characterY);
            drawCircle(g, Color.MAGENTA, characterPosition, characterSize);
            if (mouse.isInRectangle(getCircleBoundingBox(characterPosition, characterSize))) {
                drawText(g, "Hello!", Color.BLACK, plainFont, new Point(characterX, characterY));
            }

            if (doButton(g, mouse, Color.BLUE, new Rectangle(300, 100, 50, 25))) {
                numButtonClicks += 1;
            }
            drawText(g, "Button clicked " + numButtonClicks + " times", Color.BLACK, plainFont, new Point(360, 120));

            // Draw a little mouse cursor
            drawCircle(g, mouse.isPrimaryButtonDown() ? Color.YELLOW : Color.BLUE, mouse.getXY(), 5);
            drawMousePosition(g, mouse);

            canvas.repaint(); // tell the canvas to actually show all the stuff we just did
            mouse.resetForNextFrame();
            keyboard.resetForNextFrame();
            Thread.sleep(1000 / 60); // pause for the number of milliseconds that gives us 60 frames per second
        }
    }

    // ----------------------------------------------------
    // Functions to help with the more fiddly window setup
    // ----------------------------------------------------

    /**
     * Uses Java's built-in AWT package to make a window we can use for drawing.
     */
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

    /**
     * Creates a {@link GraphicsCanvas} to fit inside the provided window.
     */
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

    static Font defaultFont = new Font("Serif", Font.PLAIN, 18);

    /**
     * Draws a line from the start point to the end point. You can set the line's color and thickness.
     */
    public static void drawLine(Graphics2D g, Color color, int thickness, Point start, Point end) {
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.drawLine(start.x, start.y, end.x, end.y);
    }

    /**
     * Draws an ellipse, centered at a point, with an x radius and a y radius. You can set the ellipse's color.
     */
    public static void drawEllipse(Graphics2D g, Color color, Point center, int radiusX, int radiusY) {
        g.setColor(color);
        g.fillOval(center.x - radiusX, center.y - radiusY, radiusX * 2, radiusY * 2);
    }

    /**
     * Draws the outline of an ellipse, centered at a point, with an x radius and a y radius.
     * You can set the outline's color and thickness.
     */
    public static void drawEllipseOutline(Graphics2D g, Color color, int thickness, Point center, int radiusX, int radiusY) {
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.drawOval(center.x - radiusX, center.y - radiusY, radiusX * 2 - 1, radiusY * 2 - 1); // This function draws an oval that is one pixel too wide for some reason. I don't know why!
    }

    /**
     * Draws a circle, centered at a point, with a given radius. You can set the circle's color.
     */
    public static void drawCircle(Graphics2D g, Color color, Point center, int radius) {
        drawEllipse(g, color, center, radius, radius);
    }

    /**
     * Draws the outline of a circle, centered at a point, with a given radius.
     * You can set the outline's color and thickness.
     */
    public static void drawCircleOutline(Graphics2D g, Color color, int thickness, Point center, int radius) {
        drawEllipseOutline(g, color, thickness, center, radius, radius);
    }

    /**
     * Draws a rectangle. You can set the rectangle's color.
     * (See the {@link Rectangle} class.)
     */
    public static void drawRectangle(Graphics2D g, Color color, Rectangle rect) {
        Rectangle normalizedRect = Rectangle.normalize(rect);

        g.setColor(color);
        g.fillRect(normalizedRect.x, normalizedRect.y, normalizedRect.width, normalizedRect.height);
    }

    /**
     * Draws the outline of a rectangle. See the {@link Rectangle} class.
     * You can set the outline's color and thickness.
     * (See the {@link Rectangle} class.)
     */
    public static void drawRectangleOutline(Graphics2D g, Color color, int thickness, Rectangle rect) {
        Rectangle normalizedRect = Rectangle.normalize(rect);
        
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.drawRect(normalizedRect.x, normalizedRect.y, normalizedRect.width, normalizedRect.height);
    }

    /**
     * Draws a triangle with the three given corner points. You can set the triangle's color.
     */
    public static void drawTriangle(Graphics2D g, Color color, Point p1, Point p2, Point p3) {
        g.setColor(color);
        g.fillPolygon(new int[]{p1.x, p2.x, p3.x}, new int[]{p1.y, p2.y, p3.y}, 3);
    }

    /**
     * Draws the outline of a triangle with the three given corner points.
     * You can set the outline's color and thickness.
     */
    public static void drawTriangleOutline(Graphics2D g, Color color, int thickness, Point p1, Point p2, Point p3) {
        g.setColor(color);
        g.setStroke(new BasicStroke(thickness));
        g.drawPolygon(new int[]{p1.x, p2.x, p3.x}, new int[]{p1.y, p2.y, p3.y}, 3);
    }

    /**
     * Draws text at some position. You can set the text's color and font.
     * The bottom left of the text will be at the given point.
     */
    public static void drawText(Graphics2D g, String text, Color color, Font font, Point bottomLeft) {
        g.setColor(color);
        g.setFont(font);
        g.drawString(text, bottomLeft.x, bottomLeft.y);
    }

    /**
     * Draws the mouse position next to the mouse cursor to help you figure out what numbers you want.
     */
    public static void drawMousePosition(Graphics2D g, MouseHelper mouse) {
        String text = "X: " + mouse.getX() + ", Y: " + mouse.getY();
        drawText(g, text, Color.BLACK, defaultFont, mouse.getXY());
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
    public static boolean doButton(Graphics2D g, MouseHelper mouse, Color color, Rectangle rect) {
        boolean mouseOver = mouse.isInRectangle(rect);
        
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
        
        drawRectangle(g, desiredColor, rect);

        if (mouseOver && mouse.isPrimaryButtonUpThisFrame()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Gets a {@link Rectangle} that fits around an ellipse with the given center and radii.
     * Especially useful when used with {@link MouseHelper#isInRectangle}.
     */
    public static Rectangle getEllipseBoundingBox(Point center, int rx, int ry) {
        return new Rectangle(center.x - rx, center.y - ry, rx * 2, ry * 2);
    }

    /**
     * Gets a {@link Rectangle} that fits around a circle with the given center and radius.
     * Especially useful when used with {@link MouseHelper#isInRectangle}.
     */
    public static Rectangle getCircleBoundingBox(Point center, int radius) {
        return getEllipseBoundingBox(center, radius, radius);
    }

    /**
     * Gets the current time in seconds instead of milliseconds, for convenience.
     */
    public static double getCurrentTime() {
        return System.currentTimeMillis() / 1000.0;
    }
}