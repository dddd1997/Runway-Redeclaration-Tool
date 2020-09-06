package backend;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ObstaclePlacementTest {
    @Test
    void TooFar() {
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        Obstacle myObstacle = new Obstacle(5, "Obstacle");
       IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> myRunway.setObstacle(myObstacle,10000,50));
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Obstacle must be placed on the runway"));
    }

    @Test
    void BeforeRunway() {
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        Obstacle myObstacle = new Obstacle(5, "Obstacle");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> myRunway.setObstacle(myObstacle,-121,50));
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Obstacle must be placed on the runway"));
    }

    @Test
    void InDT() {
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        Obstacle myObstacle = new Obstacle(5, "Obstacle");
        assertDoesNotThrow(() -> myRunway.setObstacle(myObstacle, -70, 10));
        System.out.println("Obstacle placed with negative value but within displaced threshold");
    }

}

