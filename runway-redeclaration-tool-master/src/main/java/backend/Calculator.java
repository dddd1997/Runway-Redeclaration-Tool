package backend;

import javafx.scene.text.Text;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;

// This class holds the program's default values for obstacles, runways and planes
// This class provides convenient methods for the frontend to call.
public class Calculator {
    private static Airport airport;
    private static String breakdown;
    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static String notificationLog = "";


    static {
        airport = new Airport();
    }


    // Getters for the class members.

    public static List<Obstacle> getObstacles() {
        return airport.getObstacles();
    }
    public static List<Runway> getRunways() {
        return airport.getRunways();
    }
    public static List<Plane> getPlanes() {
        return airport.getPlanes();
    }

    //setters for the class members
    public static String writeToFile(Runway runway, String filePath) throws IOException {
        return TextWriter.writeToFile(runway, filePath);
    }
    public static void setObstacles(List<Obstacle> obstacles) {
        airport.setObstacles(obstacles);
    }

    public static void setPlanes(List<Plane> planes) {
        airport.setPlanes(planes);
    }

    public static void setRunways(List<Runway> runways) {
        airport.setRunways(runways);
    }


    // Get obstacle, runway or plane names.

    public static List<String> getObstacleNames() {
        List<String> list = new LinkedList<>();
        for (Obstacle o : airport.getObstacles()) {
            list.add(o.getName());
        }
        return list;
    }
    public static List<String> getRunwayNames() {
        List<String> list = new LinkedList<>();
        for (Runway r : airport.getRunways()) {
            list.add(r.getName());
        }
        return list;
    }
    public static List<String> getPlaneIDs() {
        List<String> list = new LinkedList<>();
        for (Plane p : airport.getPlanes()) {
            list.add(p.getID());
        }
        return list;
    }

    public static String getObstacleName(Obstacle obstacle) {
        return obstacle.getName();
    }
    public static int getObstacleHeight(Obstacle obstacle) {
        return obstacle.getHeight();
    }
    public static String getRunwayName(Runway runway) {
        return runway.getName();
    }
    public static void setRunwayName(Runway runway, String name){
        runway.setName(name);
    }

    public static String getPlaneID(Plane plane) {
        return plane.getID();
    }


    // Add new obstacles, runways and planes.

    public static List<Obstacle> addObstacle(Obstacle obstacle) {
        airport.addObstacle(obstacle);
        return airport.getObstacles();
    }

    public static List<Plane> addPlane(Plane plane) {
        airport.addPlane(plane);
        return airport.getPlanes();
    }

    // Get parameters of a given runway.

    // Gets original TORA of a runway.
    public static int getTORA(Runway runway) {
        return runway.getTora();
    }
    // Gets original TODA of a runway.
    public static int getTODA(Runway runway) {
        return runway.getToda();
    }
    // Gets original ASDA of a runway.
    public static int getASDA(Runway runway) {
        return runway.getAsda();
    }
    // Gets original LDA of a runway.
    public static int getLDA(Runway runway) {
        return runway.getLda();
    }

    public static int getClearway(Runway runway){return runway.getClearway();}

    public static int getStopway(Runway runway){return runway.getStopway();}

    public static int getALS(Runway runway){return runway.getAls();}

    public static int getRESA(Runway runway) {
        return runway.getResa();
    }

    public static int getTOCS(Runway runway) {
        return runway.getTocs();
    }

    public static int getDisplacedThreshold(Runway runway) { return runway.getDisplacedThreshold();}

    // Gets updated TORA of a runway from calculations.
    public static int getUpdatedTORA(Runway runway) throws NullPointerException{
        return runway.getUpdated().get("TORA");
    }
    // Gets updated TODA of a runway from calculations.
    public static int getUpdatedTODA(Runway runway) throws NullPointerException{
        return runway.getUpdated().get("TODA");
    }
    // Gets updated ASDA of a runway from calculations.
    public static int getUpdatedASDA(Runway runway) throws NullPointerException{
        return runway.getUpdated().get("ASDA");
    }
    // Gets updated LDA of a runway from calculations.
    public static int getUpdatedLDA(Runway runway) throws NullPointerException{
        return runway.getUpdated().get("LDA");
    }

    public static int getPlaneBlastProtection(Plane plane){
        return plane.getBlastProtection();
    }


    // Set parameters for a given runway.

    public static void setDisplacedThreshold(int dt, Runway runway) throws IllegalArgumentException {
        runway.setDisplacedThreshold(dt);
    }

    public static void setTora(int tora, Runway runway) throws IllegalArgumentException {
        runway.setTORA(tora);
    }

    public static void setClearway(int clearway, Runway runway) throws IllegalArgumentException {
        runway.setClearway(clearway);
    }

    public static void setStopway(int stopway, Runway runway)throws IllegalArgumentException {
        runway.setStopway(stopway);
    }

    public static void setResa(int resa, Runway runway)throws IllegalArgumentException {
        runway.setResa(resa);
    }

    public static void setAls(int als, Runway runway)throws IllegalArgumentException {
        runway.setAls(als);
    }

    public static void setTocs(int tocs, Runway runway)throws IllegalArgumentException {
        runway.setTocs(tocs);
    }

    public static void setObstacleDistanceFromThreshold(Obstacle obstacle, int distanceFromThreshold){
        obstacle.setDistanceFromThreshold(distanceFromThreshold);
    }

    public static void setObstacleDistanceFromCentreline(Obstacle obstacle, int distanceFromCentreline){
        obstacle.setDistanceFromCentreline(distanceFromCentreline);
    }

    // Redeclare runway parameters.

    // Used to calculate distances if plane is landing / taking off towards obstacle.
    // Throws exception if no plane is set for the runway.
    public static void towardsObstacle(Runway runway) throws NullPointerException, IllegalArgumentException {
        runway.setUpdated(runway.getPlane().towardsObstacle(runway));
        breakdown = runway.getPlane().getBreakdown();
        addNotificationItem("Runway Redeclared: Values calculated travelling towards the obstacle");
    }

    // Used to calculate distances if plane is landing / taking off away from obstacle.
    // Throws exception if no plane is set for the runway.
    public static void awayFromObstacle(Runway runway) throws NullPointerException, IllegalArgumentException {
        runway.setUpdated(runway.getPlane().awayFromObstacle(runway));
        breakdown = runway.getPlane().getBreakdown();
        addNotificationItem("Runway Redeclared: Values calculated travelling away from the obstacle");
    }


    // Other getters and adders.

    // Throws exception if obstacle doesn't exist on runway.
    public static int getObstacleDistanceFromThreshold(Runway runway) throws NullPointerException {
        return runway.getObstacle().getDistanceFromThreshold();
    }
    // Throws exception if obstacle doesn't exist on runway.
    public static int getObstacleDistanceFromCentreline(Runway runway) throws NullPointerException {
        return runway.getObstacle().getDistanceFromCentreline();
    }

    // Throws exception if obstacle doesn't exist on runway.
    public static int getObstacleDistanceFromThreshold(Obstacle obstacle) throws NullPointerException {
        return obstacle.getDistanceFromThreshold();
    }
    // Throws exception if obstacle doesn't exist on runway.
    public static int getObstacleDistanceFromCentreline(Obstacle obstacle) throws NullPointerException {
        return obstacle.getDistanceFromCentreline();
    }

    // Throws NullPointerException if no obstacle is on runway.
    public static Obstacle getObstacleOfRunway(Runway runway) throws NullPointerException {
        return runway.getObstacle();
    }
    public static void addObstacleToRunway(Obstacle obstacle, int distanceFromThreshold, int distanceFromCentreline, Runway runway) {
        runway.setObstacle(obstacle,distanceFromThreshold,distanceFromCentreline);
    }

    // Throws NullPointerException if plane is not on runway.
    public static Plane getPlaneOfRunway(Runway runway) throws NullPointerException{
        return runway.getPlane();
    }
    public static void addPlaneToRunway(Plane plane, Runway runway){
        runway.setPlane(plane);
    }

    // Returns true if the given runway has an obstacle.
    public static boolean hasObstacle(Runway runway) {
        try {
            runway.getObstacle();
            return true;
        }
        catch (NullPointerException exception) {
            return false;
        }
    }
    // Returns true if the given runway has a plane.
    public static boolean hasPlane(Runway runway) {
        try {
            runway.getPlane();
            return true;
        }
        catch (NullPointerException exception) {
            return false;
        }
    }

    public static int getStripEnd(Runway runway) {
        return runway.getStripEnd();
    }

    public static String getBreakdown(Runway runway) { return runway.getPlane().getBreakdown();}

    public static void SetClearwayAndStopway(Runway runway, int clearway, int stopway) throws IllegalArgumentException {
        if (clearway < stopway) {
            throw new IllegalArgumentException("Clearway must be greater than or equal to the stopway");
        } else {
            runway.setClearway(clearway);
            runway.setStopway(stopway);
        }
    }

    public static String getNotificationLog() { return notificationLog;}

    public static void addNotificationItem(String item) {
        notificationLog = notificationLog + "\n" + LocalTime.now().format(formatter) + "\t" + item;
    }

    public static void setNotificationLog(String s) { notificationLog = s;}
}
