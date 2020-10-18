
/**
 * Bundles together x, y, width, and height into a single object that can
 * be passed around. Also includes convenient methods that are commonly used
 * in graphical projects.
 */
public class Rectangle {
    public final int x;
    public final int y;
    public final int width;
    public final int height;

    /**
     * Constructs a new {@link Rectangle} object.
     */
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    /**
     * Checks if two rectangles r1 and r2 overlap.
     */
    public static boolean checkOverlap(Rectangle r1, Rectangle r2) {
        boolean overlapInX = (r1.x < r2.x + r2.width) && (r1.x + r1.width > r2.x);
        boolean overlapInY = (r1.y < r2.y + r2.height) && (r1.y + r1.height > r2.y);

        return overlapInX && overlapInY;
    }

    /**
     * Fixes up rectangles with negative width or height.
     */
    public static Rectangle normalize(Rectangle r) {
        return new Rectangle(
            Math.min(r.x, r.x + r.width),
            Math.min(r.y, r.y + r.height),
            Math.abs(r.width),
            Math.abs(r.height)
        );
    }
}