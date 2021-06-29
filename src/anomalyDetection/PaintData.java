package anomalyDetection;

import java.util.List;

public class PaintData
{
    public final List<Point> points;
    public final String color;
    public final boolean isLine;

    public PaintData(List<Point> points, String color, boolean isLine)
    {
        this.points = points;
        this.color = color;
        this.isLine = isLine;
    }
}
