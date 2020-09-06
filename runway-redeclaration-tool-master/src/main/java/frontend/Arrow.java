package frontend;

import javafx.scene.paint.Color;
import javafx.scene.shape.*;

import java.util.function.Predicate;

/**
 * THIS CODE WAS NOT WRITTEN BY US, AUTHOR CREDITED BELOW
 * @author kn
 */
public class Arrow extends Path{
    private static final double defaultArrowHeadSize = 5.0;

    private double startingX;
    private double startingY;
    private double endingX;
    private double endingY;
    private double arrowSize;
    private boolean twoHead;

    public Arrow(double startX, double startY, double endX, double endY, double arrowHeadSize, Color color, boolean doubleHead){
        super();
        startingX = startX;
        startingY = startY;
        endingX = endX;
        endingY = endY;
        arrowSize = arrowHeadSize;
        twoHead = doubleHead;

        strokeProperty().bind(fillProperty());
        setFill(color);

        //Line
        getElements().add(new MoveTo(startX, startY));
        getElements().add(new LineTo(endX, endY));

        //ArrowHead
        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        //point1
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowHeadSize + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowHeadSize + endY;

        getElements().add(new LineTo(x1, y1));
        getElements().add(new LineTo(x2, y2));
        getElements().add(new LineTo(endX, endY));

        if (doubleHead){
            double angle1 = Math.atan2((startY - endY), (startX - endX)) - Math.PI / 2.0;
            double sin1 = Math.sin(angle1);
            double cos1 = Math.cos(angle1);
            //point3
            double x3 = (- 1.0 / 2.0 * cos1 + Math.sqrt(3) / 2 * sin1) * arrowHeadSize + startX;
            double y3 = (- 1.0 / 2.0 * sin1 - Math.sqrt(3) / 2 * cos1) * arrowHeadSize + startY;
            //point4
            double x4 = (1.0 / 2.0 * cos1 + Math.sqrt(3) / 2 * sin1) * arrowHeadSize + startX;
            double y4 = (1.0 / 2.0 * sin1 - Math.sqrt(3) / 2 * cos1) * arrowHeadSize + startY;

            // We need to go back to the start and then redraw these lines
            getElements().add(new LineTo(startX, startY));
            getElements().add(new LineTo(x3, y3));
            getElements().add(new LineTo(x4, y4));
            getElements().add(new LineTo(startX, startY));
        }
    }

    public Arrow(double startX, double startY, double endX, double endY){
        this(startX, startY, endX, endY, defaultArrowHeadSize);
    }

    public Arrow(double startX, double startY, double endX, double endY, double arrowHeadSize){
        this(startX, startY, endX, endY, arrowHeadSize, Color.BLACK, false);
    }

    public Arrow(double startX, double startY, double endX, double endY, Color color){
        this(startX, startY, endX, endY, defaultArrowHeadSize, color, false);
    }

    public Arrow(double startX, double startY, double endX, double endY, boolean doubleArrow){
        this(startX, startY, endX, endY, defaultArrowHeadSize, Color.BLACK, doubleArrow);
    }

    public void changeColour(Color color){
        getElements().clear();
        strokeProperty().bind(fillProperty());
        setFill(color);

        //Line
        getElements().add(new MoveTo(startingX, startingY));
        getElements().add(new LineTo(endingX, endingY));

        //ArrowHead
        double angle = Math.atan2((endingY - startingY), (endingX - startingX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        //point1
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowSize + endingX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowSize + endingY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowSize + endingX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowSize + endingY;

        getElements().add(new LineTo(x1, y1));
        getElements().add(new LineTo(x2, y2));
        getElements().add(new LineTo(endingX, endingY));

        if (twoHead) {
            double angle1 = Math.atan2((startingY - endingY), (startingX - endingX)) - Math.PI / 2.0;
            double sin1 = Math.sin(angle1);
            double cos1 = Math.cos(angle1);
            //point3
            double x3 = (-1.0 / 2.0 * cos1 + Math.sqrt(3) / 2 * sin1) * arrowSize + startingX;
            double y3 = (-1.0 / 2.0 * sin1 - Math.sqrt(3) / 2 * cos1) * arrowSize + startingY;
            //point4
            double x4 = (1.0 / 2.0 * cos1 + Math.sqrt(3) / 2 * sin1) * arrowSize + startingX;
            double y4 = (1.0 / 2.0 * sin1 - Math.sqrt(3) / 2 * cos1) * arrowSize + startingY;

            // We need to go back to the start and then redraw these lines
            getElements().add(new LineTo(startingX, startingY));
            getElements().add(new LineTo(x3, y3));
            getElements().add(new LineTo(x4, y4));
            getElements().add(new LineTo(startingX, startingY));
        }
    }

    // Change the coordinates of an already constructed arrow. Preserves arrow size and color.
    public void changeCoordinates(double startX, double startY, double endX, double endY) {
        // Remove all the previous elements
        getElements().removeIf(new Predicate<PathElement>() {
            @Override
            public boolean test(PathElement pathElement) {
                return true;
            }
        });

        // Recalculate and add everything as in the constructor.


        startingX = startX;
        startingY = startY;
        endingX = endX;
        endingY = endY;

        //Line
        getElements().add(new MoveTo(startX, startY));
        getElements().add(new LineTo(endX, endY));

        //ArrowHead
        double angle = Math.atan2((endY - startY), (endX - startX)) - Math.PI / 2.0;
        double sin = Math.sin(angle);
        double cos = Math.cos(angle);
        //point1
        double x1 = (- 1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowSize + endX;
        double y1 = (- 1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowSize + endY;
        //point2
        double x2 = (1.0 / 2.0 * cos + Math.sqrt(3) / 2 * sin) * arrowSize + endX;
        double y2 = (1.0 / 2.0 * sin - Math.sqrt(3) / 2 * cos) * arrowSize + endY;

        getElements().add(new LineTo(x1, y1));
        getElements().add(new LineTo(x2, y2));
        getElements().add(new LineTo(endX, endY));

        if (twoHead){
            double angle1 = Math.atan2((startY - endY), (startX - endX)) - Math.PI / 2.0;
            double sin1 = Math.sin(angle1);
            double cos1 = Math.cos(angle1);
            //point3
            double x3 = (- 1.0 / 2.0 * cos1 + Math.sqrt(3) / 2 * sin1) * arrowSize + startX;
            double y3 = (- 1.0 / 2.0 * sin1 - Math.sqrt(3) / 2 * cos1) * arrowSize + startY;
            //point4
            double x4 = (1.0 / 2.0 * cos1 + Math.sqrt(3) / 2 * sin1) * arrowSize + startX;
            double y4 = (1.0 / 2.0 * sin1 - Math.sqrt(3) / 2 * cos1) * arrowSize + startY;

            // We need to go back to the start and then redraw these lines
            getElements().add(new LineTo(startX, startY));
            getElements().add(new LineTo(x3, y3));
            getElements().add(new LineTo(x4, y4));
            getElements().add(new LineTo(startX, startY));
        }
    }
}