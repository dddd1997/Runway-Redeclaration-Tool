package backend;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObstacleTest {

    @Test
    public void testNewObstacle(){
        System.out.print("Creating obstacle plane of height 12");
        Obstacle obstacle = new Obstacle(12,"plane");
        assertEquals(12,obstacle.getHeight());
        assertEquals("plane",obstacle.getName());
    }
}
