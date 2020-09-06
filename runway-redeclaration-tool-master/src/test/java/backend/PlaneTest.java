package backend;

import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class PlaneTest {
    // Scenario 1
    @Test
    public void CheckTowardsObstacleS1() {
        System.out.println("Scenario 1 Towards");
        Runway myRunway = new Runway(3884, 0, 0,  "27", "R", 306);
        Plane myPlane = new Plane("01", 300);
        Obstacle myObstacle = new Obstacle(12, "Boeing 747");

        myRunway.setObstacle(myObstacle, -50, 0);

        HashMap<String, Integer> map = myPlane.towardsObstacle(myRunway);
        assertEquals(2968, map.get("TORA"));
        assertEquals(2968, map.get("ASDA"));
        assertEquals(2968, map.get("TODA"));
        assertEquals(3328, map.get("LDA"));
        System.out.println("\nNew TORA is " + map.get("TORA")
                + "\nNew ASDA is " + map.get("ASDA")
                + "\nNew TODA is " + map.get("TODA")
                + "\nNew LDA is " + map.get("LDA"));
    }
    @Test
    public void CheckAwayFromObstacleS1() {
        System.out.println("Scenario 1 Away");
        Runway myRunway = new Runway(3902, 0, 0,  "09", "L", 306);
        Plane myPlane = new Plane("01", 300);
        Obstacle myObstacle = new Obstacle(12, "Boeing 747");

        myRunway.setObstacle(myObstacle, -50, 0);

        HashMap<String, Integer> map = myPlane.awayFromObstacle(myRunway);
        assertEquals(3346, map.get("TORA"));
        assertEquals(3346, map.get("ASDA"));
        assertEquals(3346, map.get("TODA"));
        assertEquals(2680, map.get("LDA"));
        System.out.println("\nNew TORA is " + map.get("TORA")
                + "\nNew ASDA is " + map.get("ASDA")
                + "\nNew TODA is " + map.get("TODA")
                + "\nNew LDA is " + map.get("LDA"));
    }

    // Scenario 2
    @Test
    public void CheckTowardsObstacleS2() {
        System.out.println("Scenario 2 Towards");
        Runway myRunway = new Runway(3660, 0, 0,  "09", "R", 307);
        Plane myPlane = new Plane("01", 300);
        Obstacle myObstacle = new Obstacle(25, "Boeing 737");

        myRunway.setObstacle(myObstacle, 2853, 20);

        HashMap<String, Integer> map = myPlane.towardsObstacle(myRunway);
        assertEquals(1236, map.get("TORA"));
        assertEquals(1236, map.get("ASDA"));
        assertEquals(1236, map.get("TODA"));
        assertEquals(2553, map.get("LDA"));
        System.out.println("\nNew TORA is " + map.get("TORA")
                + "\nNew ASDA is " + map.get("ASDA")
                + "\nNew TODA is " + map.get("TODA")
                + "\nNew LDA is " + map.get("LDA"));
    }
    @Test
    public void CheckAwayFromObstacleS2() {
        System.out.println("Scenario 2 Away");
        Runway myRunway = new Runway(3660, 0, 0,  "27", "L", 0);
        Plane myPlane = new Plane("01", 300);
        Obstacle myObstacle = new Obstacle(25, "Boeing 737");

        myRunway.setObstacle(myObstacle, 3160, 20);

        HashMap<String, Integer> map = myPlane.awayFromObstacle(myRunway);
        assertEquals(2860, map.get("TORA"));
        assertEquals(2860, map.get("ASDA"));
        assertEquals(2860, map.get("TODA"));
        assertEquals(1850, map.get("LDA"));
        System.out.println("\nNew TORA is " + map.get("TORA")
                + "\nNew ASDA is " + map.get("ASDA")
                + "\nNew TODA is " + map.get("TODA")
                + "\nNew LDA is " + map.get("LDA"));
    }

    // Scenario 3
    @Test
    public void CheckTowardsObstacleS3() {
        System.out.println("Scenario 3 Towards");
        Runway myRunway = new Runway(3660, 0, 0,  "27", "L", 307);
        Plane myPlane = new Plane("01", 300);
        Obstacle myObstacle = new Obstacle(15, "Boeing 767");

        myRunway.setObstacle(myObstacle, 150, 60);

        HashMap<String, Integer> map = myPlane.towardsObstacle(myRunway);
        assertEquals(2393, map.get("TORA"));
        assertEquals(2393, map.get("ASDA"));
        assertEquals(2393, map.get("TODA"));
        assertEquals(2903, map.get("LDA"));
        System.out.println("\nNew TORA is " + map.get("TORA")
                + "\nNew ASDA is " + map.get("ASDA")
                + "\nNew TODA is " + map.get("TODA")
                + "\nNew LDA is " + map.get("LDA"));
    }
    @Test
    public void CheckAwayFromObstacleS3() {
        System.out.println("Scenario 3 Away");
        Runway myRunway = new Runway(3660, 0, 0,  "09", "R", 307);
        Plane myPlane = new Plane("01", 300);
        Obstacle myObstacle = new Obstacle(15, "Boeing 767");

        myRunway.setObstacle(myObstacle, 150, 60);

        HashMap<String, Integer> map = myPlane.awayFromObstacle(myRunway);
        assertEquals(2903, map.get("TORA"));
        assertEquals(2903, map.get("ASDA"));
        assertEquals(2903, map.get("TODA"));
        assertEquals(2086, map.get("LDA"));
        System.out.println("\nNew TORA is " + map.get("TORA")
                + "\nNew ASDA is " + map.get("ASDA")
                + "\nNew TODA is " + map.get("TODA")
                + "\nNew LDA is " + map.get("LDA"));
    }

    // Scenario 4
    @Test
    public void CheckTowardsObstacleS4() {
        System.out.println("Scenario 4 Towards");
        Runway myRunway = new Runway(3902, 0, 0,  "09", "L", 306);
        Plane myPlane = new Plane("01", 300);
        Obstacle myObstacle = new Obstacle(20, "de Havilland Dragon Rapide");

        myRunway.setObstacle(myObstacle, 3546, 20);

        HashMap<String, Integer> map = myPlane.towardsObstacle(myRunway);
        assertEquals(2180, map.get("TORA"));
        assertEquals(2180, map.get("ASDA"));
        assertEquals(2180, map.get("TODA"));
        assertEquals(3246, map.get("LDA"));
        System.out.println("\nNew TORA is " + map.get("TORA")
                + "\nNew ASDA is " + map.get("ASDA")
                + "\nNew TODA is " + map.get("TODA")
                + "\nNew LDA is " + map.get("LDA"));
    }
    @Test
    public void CheckAwayFromObstacleS4() {
        System.out.println("Scenario 4 Away");
        Runway myRunway = new Runway(3884, 0, 0,  "27", "R", 0);
        Plane myPlane = new Plane("01", 300);
        Obstacle myObstacle = new Obstacle(20, "de Havilland Dragon Rapide");

        myRunway.setObstacle(myObstacle, 3834, 20);

        HashMap<String, Integer> map = myPlane.awayFromObstacle(myRunway);
        assertEquals(3534, map.get("TORA"));
        assertEquals(3534, map.get("ASDA"));
        assertEquals(3534, map.get("TODA"));
        assertEquals(2774, map.get("LDA"));
        System.out.println("\nNew TORA is " + map.get("TORA")
                + "\nNew ASDA is " + map.get("ASDA")
                + "\nNew TODA is " + map.get("TODA")
                + "\nNew LDA is " + map.get("LDA"));
    }

    @Test
    public void CheckNegativeBlastProtection() {
        Plane myPlane = new Plane("01", 300);
        System.out.println("Creating a Plane object with blast protection = -20\n");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> myPlane.setBlastProtection(-20));
        System.out.println("Exception: \"" + thrown.getMessage() + "\" thrown");
        assertTrue(thrown.getMessage().contains("Negative blast protection value passed"));
    }

    @Test
    public void CheckGivenValuesAway() {
        Runway myRunway = new Runway(3250, 150, 100,  "27", 0);
        Plane myPlane = new Plane("01", 300);
        Obstacle myObstacle = new Obstacle(15, "de Havilland Dragon Rapide");

        myRunway.setObstacle(myObstacle, 2750, 20);

        HashMap<String, Integer> map = myPlane.awayFromObstacle(myRunway);
        assertEquals(2450, map.get("TORA"));
        assertEquals(2550, map.get("ASDA"));
        assertEquals(2600, map.get("TODA"));
        assertEquals(1940, map.get("LDA"));
        System.out.println("\nNew TORA is " + map.get("TORA")
                + "\nNew ASDA is " + map.get("ASDA")
                + "\nNew TODA is " + map.get("TODA")
                + "\nNew LDA is " + map.get("LDA"));
    }

    @Test
    public void CheckGivenValuesTowards() {
        Runway myRunway = new Runway(3250, 150, 100,  "09", 205);
        Plane myPlane = new Plane("01", 300);
        Obstacle myObstacle = new Obstacle(15, "de Havilland Dragon Rapide");

        myRunway.setObstacle(myObstacle, 2750, 20);

        HashMap<String, Integer> map = myPlane.towardsObstacle(myRunway);
        assertEquals(1735, map.get("TORA"));
        assertEquals(1735, map.get("ASDA"));
        assertEquals(1735, map.get("TODA"));
        assertEquals(2450, map.get("LDA"));
        System.out.println("\nNew TORA is " + map.get("TORA")
                + "\nNew ASDA is " + map.get("ASDA")
                + "\nNew TODA is " + map.get("TODA")
                + "\nNew LDA is " + map.get("LDA"));
    }
}
