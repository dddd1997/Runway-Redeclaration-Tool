package backend;

import java.util.LinkedList;
import java.util.List;

public class Airport {
    private List<Obstacle> obstacles;
    private List<Runway> runways;
    private List<Plane> planes;

    Airport(){
        obstacles = new LinkedList<Obstacle>();
        runways = new LinkedList<Runway>();
        planes = new LinkedList<Plane>();
        setObstacles(obstacles);

        //TEMPORARY RUNWAY BEFORE XML IS IMPLEMENTED
        runways.add(new Runway(3000,200,100,"09",50));

        obstacles.add(new Obstacle(50, "Passenger Plane"));
        obstacles.add(new Obstacle(4, "Car"));
        obstacles.add(new Obstacle(1, "Box"));
        obstacles.add(new Obstacle(39, "Fire Engine"));
        obstacles.add(new Obstacle(20, "Private Jet"));

        planes.add(new Plane("Boeing 737",100));
        planes.add(new Plane("AirBus A320", 80));
    }

    List<Plane> getPlanes() {
        return planes;
    }

    List<Obstacle> getObstacles() {
        return obstacles;
    }

    List<Runway> getRunways() {
        return runways;
    }

    void setObstacles(List<Obstacle> obstacles1) {
        this.obstacles = obstacles1;
    }

    void setPlanes(List<Plane> planes) {
        this.planes = planes;
    }

    void setRunways(List<Runway> runways) {
        this.runways = runways;
    }

    void addObstacle(Obstacle obstacle){
        obstacles.add(obstacle);
    }

    void addPlane(Plane plane){
        planes.add(plane);
    }

}
