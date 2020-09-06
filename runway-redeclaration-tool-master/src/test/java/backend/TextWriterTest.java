package backend;

import javafx.scene.text.Text;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TextWriterTest {

    @Test
    public void checkTextFileOutput() throws IOException {
        Plane plane = new Plane("plane",200);
        Runway runway = new Runway(1000,100,100,"09",100);
        Obstacle obstacle = new Obstacle(10,"plane");
        runway.setPlane(plane);
        runway.setObstacle(obstacle,100,100);
        Calculator.towardsObstacle(runway);
        TextWriter.writeToFile(runway, System.getProperty("user.home") + "/Documents/redeclaration-app-text-file.txt");
        System.out.println(System.getProperty("user.dir"));
        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader(System.getProperty("user.home")+"/Documents/redeclaration-app-text-file.txt"));
            assertEquals(Calculator.getObstacleOfRunway(runway) +" is on the runway " + Calculator.getObstacleOfRunway(runway).getDistanceFromThreshold() + "m from the threshold and " + Calculator.getObstacleOfRunway(runway).getDistanceFromCentreline() + "m from the centre line ",reader.readLine());
            assertEquals("original TORA is "+Calculator.getTORA(runway),reader.readLine());
            assertEquals("original TODA is "+Calculator.getTODA(runway),reader.readLine());
            assertEquals("original LDA is "+Calculator.getLDA(runway),reader.readLine());
            assertEquals("original ASDA is "+Calculator.getASDA(runway),reader.readLine());
            assertEquals("updated TORA is "+Calculator.getUpdatedTORA(runway),reader.readLine());
            assertEquals("updated TODA is "+Calculator.getUpdatedTODA(runway),reader.readLine());
            assertEquals("updated LDA is "+Calculator.getUpdatedLDA(runway),reader.readLine());
            assertEquals("updated ASDA is "+Calculator.getUpdatedASDA(runway),reader.readLine());
            reader.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Test
    public void checkNoCalculations() throws IOException {
        Runway runway = new Runway(1000,100,100,"10",100);
        Obstacle obstacle = new Obstacle(10,"plane");
        runway.setObstacle(obstacle,100,100);
        File file = new File(System.getProperty("user.home") + "/Documents/10.txt");

        try {
            TextWriter.writeToFile(runway, file.getAbsolutePath());
        } catch (Exception e) {

        }
        assertEquals(0,file.length());
    }
}
