package backend;

import frontend.RedeclarationApp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        if (args.length != 0) {
            Runway myRunway = new Runway(1000, 100, 50,"09",120);
            Obstacle obstacle = new Obstacle(12,"plane");
            Plane plane = new Plane("f",100);
            myRunway.setPlane(plane);
            myRunway.setObstacle(obstacle,10,15);
            Calculator.towardsObstacle(myRunway);

            String defaultFilePath = System.getProperty("user.home") + "/Documents/redeclaration-app-text-file.txt";
            Calculator.writeToFile(myRunway, defaultFilePath);
        }
        else {
            RedeclarationApp.main(args);
        }
    }
}
