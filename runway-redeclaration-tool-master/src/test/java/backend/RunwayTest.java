package backend;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

public class RunwayTest {

    //checks if inputting tora redefines toda and asda correctly
    @Test
    public void checkTORAInput(){
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        myRunway.printInformation();
        myRunway.setTORA(2000);
        assertEquals(2000, myRunway.getTora());
        assertEquals(2000+100,myRunway.getToda());
        assertEquals(2000+50,myRunway.getAsda());
        System.out.println("\nNew TORA is " + myRunway.getTora()+"\nTODA is TORA:"+myRunway.getTora()+" + Clearway:"+myRunway.getClearway()+ " = "+myRunway.getToda() + "\nASDA is TORA:"+myRunway.getTora() + " + Stopway:"+myRunway.getStopway()+" = "+myRunway.getAsda());
        myRunway.printInformation();
    }

    //tests if inputting a new clearway redefines TODA correctly
    @Test
    public void checkClearwayInput(){
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        myRunway.printInformation();
        myRunway.setClearway(250);
        assertEquals(250,myRunway.getClearway());
        assertEquals(1250,myRunway.getToda());
        System.out.println("\nNew clearway is "+myRunway.getClearway());
        System.out.println(("TODA is TORA:"+myRunway.getTora()+" + Clearway:"+myRunway.getClearway()+" = "+myRunway.getToda()));
        myRunway.printInformation();
    }

    //tests if inputting a new Stopway redefines ASDA correctly
    @Test
    public void checkStopwayInput(){
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        myRunway.printInformation();
        myRunway.setStopway(300);
        assertEquals(300,myRunway.getStopway());
        assertEquals(1300,myRunway.getAsda());
        System.out.println("\nNew stopway is "+myRunway.getStopway());
        System.out.println(("ASDA is TORA:"+myRunway.getTora()+" + Stopway:"+myRunway.getStopway()+" = "+myRunway.getAsda()));
        myRunway.printInformation();
    }

    //tests if function printInformation() outputs the information correctly
    @Test
    public void testOutput(){
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        myRunway.setObstacle(new Obstacle(12,"plane"),50,100);
        assertEquals("--Runway 09--"+
                "\nTORA is 1000" +
                "\nclearway is 100"+
                "\nstopway is 50"+
                "\nTODA is 1100"+
                "\nASDA is 1050"+
                "\nLDA is 880"+
                "\nALS is 50"+
                "\nTOCS is 50"+
                "\nRESA is 240"+
                "\nLanding threshold is displaced by 120m"+
                "\nThere is a plane on the runway of height 12m, 50m from the threshold, 100m from the centreline"
                ,myRunway.printInformation()
                );
    }

    //tests if obstacle is added correctly to the runway
    @Test
    public void testAddObject(){
        Obstacle obstacle = new Obstacle(12,"foo");
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        System.out.println("Adding obstacle "+obstacle.getName());
        myRunway.setObstacle(obstacle,100,50);
        System.out.println();
        myRunway.printInformation();
        assertEquals(obstacle,myRunway.getObstacle());
    }

    //tests if obstacle is removed correctly from the runway
    @Test
    public void testRemoveObject(){
        Obstacle obstacle = new Obstacle(12,"foo");
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        myRunway.setObstacle(obstacle,100,50);
        System.out.println("Current obstacle is "+myRunway.getObstacle().getName());
        myRunway.removeObstacle();
        System.out.println("obstacle removed");
        try{
            if(!myRunway.getObstacle().equals(null)){
                System.out.println("Current obstacle is "+myRunway.getObstacle().getName());
            }
        } catch (Exception e) {
        }
        System.out.println();
        myRunway.printInformation();
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> myRunway.getObstacle());
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("No obstacle on runway"));
    }

    //tests if single runway prints name correctly
    @Test
    public void testSingleRunway(){
        System.out.println("Creating runway 09");
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        myRunway.printInformation();
        assertEquals("09",myRunway.getName());
    }

    //tests if parallel runway prints name correctly
    @Test
    public void testParallelRunway(){
        System.out.println("creating runway 09R");
        Runway myRunway = new Runway(1000, 100, 50,"09","R",120);
        myRunway.printInformation();
        assertEquals("09R",myRunway.getName());
    }

    //tests if displaced threshold is set correctly
    @Test
    public void testDisplaceThreshold(){
        System.out.println("creating runway with displaced threshold of 120");
        Runway myRunway = new Runway(1000, 100, 50,"09","R",120);
        myRunway.printInformation();
        assertEquals(120,myRunway.getDisplacedThreshold());
    }

    //tests if obstacle has correct distance from threshold and centre line when added to a runway
    @Test
    public void testObstacleDistances(){
        System.out.println("creating obstacle 10m from threshold and 15m from centre line");
        Obstacle obstacle = new Obstacle(12,"foo");
        Runway myRunway = new Runway(1000, 100, 50,"09","R",120);
        myRunway.setObstacle(obstacle,10,15);
        myRunway.printInformation();
        assertEquals(10,myRunway.getObstacle().getDistanceFromThreshold());
        assertEquals(15,myRunway.getObstacle().getDistanceFromCentreline());
    }

    //tests if resa is changed correctly
    @Test
    public void testResaChange(){
        System.out.println("Creating runway with resa of 240m");
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        myRunway.printInformation();
        System.out.println("\nSetting resa to 350m");
        System.out.println();
        myRunway.setResa(350);
        myRunway.printInformation();
        assertEquals(350,myRunway.getResa());
    }

    //exception testing for setting tora
    @Test
    public void testNegativeTora(){
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        myRunway.printInformation();
        System.out.println("\nSetting resa to -200");
        System.out.println();
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> myRunway.setTORA(-200));
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("TORA cannot be negative!"));
    }

    //exception testing for setting clearway
    @Test
    public void testNegativeClearway(){
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        myRunway.printInformation();
        System.out.println("\nSetting clearway to -200");
        System.out.println();
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> myRunway.setClearway(-200));
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Clearway cannot be negative!"));
    }

    //exception testing for setting stopway
    @Test
    public void testNegativeStopway(){
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        myRunway.printInformation();
        System.out.println("\nSetting stopway to -200");
        System.out.println();
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> myRunway.setStopway(-200));
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Stopway cannot be negative!"));
    }

    //exception testing for setting resa
    @Test
    public void testNegativeResa(){
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        myRunway.printInformation();
        System.out.println("\nSetting RESA to -200");
        System.out.println();
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> myRunway.setResa(-200));
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("RESA cannot be negative!"));
    }

    //exception testing for setting als
    @Test
    public void testNegativeAls(){
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        myRunway.printInformation();
        System.out.println("\nSetting ALS to -200");
        System.out.println();
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> myRunway.setAls(-200));
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("ALS cannot be negative!"));
    }

    //exception testing for setting tocs
    @Test
    public void testNegativeTocs(){
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        myRunway.printInformation();
        System.out.println("\nSetting TOCS to -200");
        System.out.println();
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> myRunway.setTocs(-200));
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("TOCS cannot be negative!"));
    }

    //exception testing for setting displaced threshold
    @Test
    public void testNegativeDisplacedThreshold(){
        Runway myRunway = new Runway(1000, 100, 50,"09",120);
        myRunway.printInformation();
        System.out.println("\nSetting displaced threshold to -200");
        System.out.println();
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> myRunway.setDisplacedThreshold(-200));
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Displaced Threshold cannot be negative!"));
    }

    //exception testing for instantiating a singular runway
    @Test
    public void testSingularRunwayException(){
        AtomicReference<Runway> myRunway = null;
        System.out.println("creating runway with heading 37");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> myRunway.set(new Runway(1000, 100, 50, "37", 120)));
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("heading must be between 01 and 36!"));
        System.out.println("creating runway with heading 00");
        IllegalArgumentException thrown1 = assertThrows(IllegalArgumentException.class, () -> myRunway.set(new Runway(1000, 100, 50,  "00", 120)));
        System.out.println(thrown1.getMessage());
        assertTrue(thrown1.getMessage().contains("heading must be between 01 and 36!"));
    }

    //exception testing for instantiating a parallel runway
    @Test
    public void testParallelRunwayException(){
        AtomicReference<Runway> myRunway = null;
        System.out.println("creating runway with position F");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> myRunway.set(new Runway(1000, 100, 50,  "09","F", 120)));
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("position must be R,L or C!"));
    }

    //tests if displaced threshold sets lda correctly
    @Test
    public void testCorrectLda(){
        System.out.println("setting displaced threshold to 100, tora to 1000");
        Runway myRunway = new Runway(1000, 100, 50,"09",100);
        System.out.println("Setting displaced threshold to 150");
        myRunway.setDisplacedThreshold(150);
        myRunway.printInformation();
        assertEquals(850,myRunway.getLda());
    }

    //tests that displaced threshold can't be larger than tora
    @Test
    public void testDisplacedThresholdLength(){
        System.out.println("Setting tora to 1000, setting Displaced threshold to 2000");
        AtomicReference<Runway> myRunway = null;
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> myRunway.set(new Runway(1000, 100, 200, "09", 2000)));
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Displaced threshold cannot be greater than tora!"));
    }

    //tests that exception is thrown if no obstacle is on runway
    @Test
    public void checkObstacleException(){
        Runway runway = new Runway(2000, 100, 200, "09", 100);
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> runway.getObstacle());
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("No obstacle on runway"));
    }

    //tests that exception is thrown if no plane is on runway
    @Test
    public void checkPlaneException(){
        Runway runway = new Runway(2000, 100, 200, "09", 100);
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> runway.getPlane());
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("No plane on runway"));
    }

    //tests that exception is thrown if trying to get calculations when they haven't been done
    @Test
    public void checkUpdatedCalculationsException(){
        Runway runway = new Runway(2000, 100, 1000, "09", 100);
        NullPointerException thrown = assertThrows(NullPointerException.class, () -> runway.getUpdated());
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Recalculations haven't been done!"));
    }

    //tests partitions for displaced threshold against tora
    @Test
    public void testToraDisplacedThreshold(){
        System.out.println("Setting Tora to 1000");
        Runway runway = new Runway(1000, 100, 210, "09", 100);
        System.out.println("Setting displaced threshold to 1001");
        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> runway.setDisplacedThreshold(1001));
        System.out.println(thrown.getMessage());
        assertTrue(thrown.getMessage().contains("Displaced threshold cannot be greater than tora!"));
        System.out.println("Setting displaced threshold to -1");
        IllegalArgumentException thrown1 = assertThrows(IllegalArgumentException.class, () -> runway.setDisplacedThreshold(-1));
        System.out.println(thrown1.getMessage());
        assertTrue(thrown1.getMessage().contains("Displaced Threshold cannot be negative!"));
        System.out.println("Setting displaced threshold to 500");
        runway.setDisplacedThreshold(500);
        assertEquals(500,runway.getLda());
    }

}
