package backend;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CalculatorTest {
    private Plane plane;
    private Runway runway;
    private Obstacle obstacle;

    @BeforeEach
    private void setUp() {
        plane = new Plane("plane", 200);
        runway = new Runway(1000, 100, 100, "09", 100);
        obstacle = new Obstacle(10, "plane");
        runway.setPlane(plane);
        runway.setObstacle(obstacle, 100, 100);
        Calculator.towardsObstacle(runway);
    }

    @Test
    public void testSetPlanes(){
        LinkedList<Plane> test = new LinkedList<Plane>();
        test.add(plane);
        Calculator.setPlanes(test);
        assertEquals(test,(LinkedList<Plane>) Calculator.getPlanes());
    }

    @Test
    public void testSetObstacles(){
        LinkedList<Obstacle> test = new LinkedList<Obstacle>();
        test.add(obstacle);
        Calculator.setObstacles(test);
        assertEquals(test,(LinkedList<Obstacle>) Calculator.getObstacles());
    }

    @Test
    public void testSetRunways(){
        LinkedList<Runway> test = new LinkedList<Runway>();
        test.add(runway);
        Calculator.setRunways(test);
        assertEquals(test,(LinkedList<Runway>) Calculator.getRunways());
    }

    @Test
    public void testGetPlaneIDs(){
        LinkedList<Plane> test = new LinkedList<Plane>();
        test.add(plane);
        Calculator.setPlanes(test);
        List<String> list = new LinkedList<>();
        for (Plane p : test) {
            list.add(p.getID());
        }
        assertEquals(list,(LinkedList<String>) Calculator.getPlaneIDs());
    }

    @Test
    public void testGetObstacleNames(){
        LinkedList<Obstacle> test = new LinkedList<Obstacle>();
        test.add(obstacle);
        Calculator.setObstacles(test);
        List<String> list = new LinkedList<>();
        for (Obstacle o : test) {
            list.add(o.getName());
        }
        assertEquals(list,(LinkedList<String>) Calculator.getObstacleNames());
    }

    @Test
    public void testGetRunwayNames(){
        LinkedList<Runway> test = new LinkedList<Runway>();
        test.add(runway);
        Calculator.setRunways(test);
        List<String> list = new LinkedList<>();
        for (Runway r : test) {
            list.add(r.getName());
        }
        assertEquals(list,(LinkedList<String>) Calculator.getRunwayNames());
    }

    @Test
    public void testGetObstacleName(){
        LinkedList<Obstacle> test = new LinkedList<Obstacle>();
        test.add(obstacle);
        Calculator.setObstacles(test);
        assertEquals("plane", Calculator.getObstacleName(obstacle));
    }

    @Test
    public void testGetRunwayName(){
        LinkedList<Runway> test = new LinkedList<Runway>();
        test.add(runway);
        Calculator.setRunways(test);
        assertEquals("09", Calculator.getRunwayName(runway));
    }

    @Test
    public void testGetPlaneID(){
        LinkedList<Plane> test = new LinkedList<Plane>();
        test.add(plane);
        Calculator.setPlanes(test);
        assertEquals("plane", Calculator.getPlaneID(plane));
    }

    @Test
    public void testAddObstacle(){
        LinkedList<Obstacle> test = new LinkedList<Obstacle>();
        Obstacle testObstacle = new Obstacle(20,"lll");
        test.add(obstacle);
        Calculator.setObstacles(test);
        Calculator.addObstacle(testObstacle);
        assertEquals(testObstacle, Calculator.getObstacles().get(Calculator.getObstacles().size()-1));
    }

    @Test
    public void testAddPlane(){
        LinkedList<Plane> test = new LinkedList<Plane>();
        test.add(plane);
        Plane plane = new Plane("Plane",100);
        Calculator.setPlanes(test);
        Calculator.addPlane(plane);
        assertEquals(plane, Calculator.getPlanes().get(1));
    }

    @Test void testGetTora(){
        assertEquals(1000,Calculator.getTORA(runway));
    }

    @Test void testGetToda(){
        assertEquals(1100,Calculator.getTODA(runway));
    }

    @Test void testGetAsda(){
        assertEquals(1100,Calculator.getASDA(runway));
    }

    @Test void testGetLda(){
        assertEquals(900,Calculator.getLDA(runway));
    }

    @Test
    public void testGetUpdatedTora(){
        assertEquals(240,Calculator.getUpdatedTORA(runway));
    }

    @Test
    public void testGetUpdatedAsda(){
        assertEquals(240,Calculator.getUpdatedASDA(runway));
    }

    @Test
    public void testGetUpdatedToda(){
        assertEquals(240,Calculator.getUpdatedTODA(runway));
    }

    @Test
    public void testGetUpdatedLda(){
        assertEquals(500,Calculator.getUpdatedLDA(runway));
    }

    @Test
    public void testGetPlaneBlastProtection(){
        assertEquals(200,Calculator.getPlaneBlastProtection(plane));
    }

    @Test
    public void testSetDisplacedThreshold(){
        Calculator.setDisplacedThreshold(100,runway);
        assertEquals(100,Calculator.getDisplacedThreshold(runway));
    }

    @Test
    public void testSetClearway(){
        Calculator.setTora(1000,runway);
        Calculator.setClearway(50,runway);
        assertEquals(1050,Calculator.getTODA(runway));
    }

    @Test
    public void testSetTora(){
        Calculator.setTora(2000,runway);
        assertEquals(2000,Calculator.getTORA(runway));
    }

    @Test
    public void testSetStopway(){
        Calculator.setStopway(50,runway);
        assertEquals(1050, Calculator.getASDA(runway));
    }

    @Test
    public void testSetResa(){
        Calculator.setResa(69,runway);
        assertEquals(69,Calculator.getRESA(runway));
    }

    @Test
    public void testSetAls(){
        Calculator.setAls(69,runway);
        assertEquals(69,Calculator.getALS(runway));
    }

    @Test
    public void testSetTocs(){
        Calculator.setTocs(69,runway);
        assertEquals(69,Calculator.getTOCS(runway));
    }

    @Test
    public void testSetObstacleDistanceFromThreshold(){
        Calculator.setObstacleDistanceFromThreshold(obstacle,400);
        assertEquals(400,Calculator.getObstacleDistanceFromThreshold(obstacle));
        assertEquals(400,Calculator.getObstacleDistanceFromThreshold(runway));
    }

    @Test
    public void testSetObstacleDistanceFromCentreline(){
        Calculator.setObstacleDistanceFromCentreline(obstacle,69);
        assertEquals(69,Calculator.getObstacleDistanceFromCentreline(runway));
        assertEquals(69,Calculator.getObstacleDistanceFromCentreline(obstacle));
    }

    @Test
    public void testGetObstacleHeight(){
        assertEquals(10,Calculator.getObstacleHeight(obstacle));
    }

    @Test
    public void testGetObstacle(){
        assertEquals(obstacle,Calculator.getObstacleOfRunway(runway));
    }

    @Test
    public void testGetPlane(){
        assertEquals(plane,Calculator.getPlaneOfRunway(runway));
    }
}
