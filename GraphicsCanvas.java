import java.awt.*;
//alayna wuz here
/**
 * GraphicsCanvas does some helpful stuff with buffered images
 * to eliminate flickering in a Java AWT canvas. Other than that
 * it's just a normal panel.
 * 
 * To use it, construct it with your desired width and height,
 * add it to your window, call initializeBuffer, and then call
 * getGraphics2D to get the Graphics2D object that lets you
 * draw things.
 */
public class GraphicsCanvas extends Panel {
    int width, height;
    Image bufferImage;
    Graphics2D graphics;

    public GraphicsCanvas(int width, int height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Sets up the canvas's buffer. Must be called after it has
     * been added to a window.
     */
    public void initializeBuffer() {
        bufferImage = this.createImage(width, height);
        if (bufferImage == null) {
            throw new IllegalStateException("Could not initialize buffer! Make sure you have added the panel to a window first.");
        }
    }

    /**
     * Gets the buffer's graphics object. Drawing into this will
     * not show up onscreen right away; make sure to update or
     * repaint your window after you are done drawing.
     */
    public Graphics2D getGraphics2D() {
        if (bufferImage == null) {
            throw new IllegalStateException("Could not getGraphics2D because you need to call initializeBuffer first!");
        }

        if (graphics == null) {
            graphics = (Graphics2D) bufferImage.getGraphics();
        }

        return graphics;
    }

    /**
     * Clears the contents of the buffer.
     */
    public void clear() {
        Graphics2D bufferGraphics = getGraphics2D();
        bufferGraphics.clearRect(0, 0, width, height);
    }

    @Override
    public void update(Graphics g) {
        /*
        Normally update would clear the window and then paint. But we want
        to only paint because we're using our buffer to handle that stuff.
        */
        paint(g);
    }
    
    @Override
    public void paint(Graphics g) {
        g.drawImage(bufferImage, 0, 0, this);
    }

    // ignore. stupid warnings!
    private static final long serialVersionUID = 1L;
}
