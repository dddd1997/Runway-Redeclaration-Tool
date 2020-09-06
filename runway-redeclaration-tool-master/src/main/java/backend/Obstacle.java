package backend;

public class Obstacle {
    private int height;
    private String name;
    private int distanceFromThreshold;
    private int distanceFromCentreline;

    public Obstacle(int height, String name){
        this.height = height;
        this.name = name;
    }

    void setDistanceFromThreshold(int distanceFromThreshold){
        this.distanceFromThreshold = distanceFromThreshold;
    }

    void setDistanceFromCentreline(int distanceFromCentreline){
        this.distanceFromCentreline = distanceFromCentreline;
    }

    int getHeight(){
        return this.height;
    }

    public String getName(){
        return this.name;
    }

    int getDistanceFromThreshold(){
        return this.distanceFromThreshold;
    }

    int getDistanceFromCentreline(){
        return this.distanceFromCentreline;
    }

    @Override
    public String toString() {
        return  "\"" + name + "\", h = " + height;
    }
}