package backend;

import java.util.HashMap;
import java.util.stream.Stream;

public class Runway {

    private int tora;
    private int toda;
    private int asda;
    private int lda;
    private int clearway;
    private int stopway;
    private String name;
    private Plane plane = null;
    private String[] headings = {"01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31","32","33","34","35","36"};
    private HashMap<String,Integer> updated = null;

    //runway can contain 1 obstacle;
    private Obstacle obstacle = null;
    //variables that are used if there's an obstacle
    private int resa = 240;
    private int als = 50;
    private int tocs = 50;
    private int stripEnd = 60;
    private int displacedThreshold;

    //instantiating a singular runway
    //toda is defined as the tora + clearway
    //asda is defined as tora + stopway
    Runway(int tora, int clearway, int stopway, String heading, int displacedThreshold){
        setTORA(tora);
        setClearway(clearway);
        setStopway(stopway);
        setDisplacedThreshold(displacedThreshold);
        this.toda = tora + clearway;
        this.asda = tora + stopway;
        this.lda = tora - displacedThreshold;
        if(!Stream.of(headings).anyMatch(x -> x.equals(heading))){ throw new IllegalArgumentException("heading must be between 01 and 36!");}
        this.name = heading;
    }

    //second constructor if there is more than one runway (parallel runways)
    Runway(int tora, int clearway, int stopway, String heading, String pos, int displacedThreshold) {
        setTORA(tora);
        setClearway(clearway);
        setStopway(stopway);
        setDisplacedThreshold(displacedThreshold);
        this.toda = tora + clearway;
        this.asda = tora + stopway;
        this.lda = tora - displacedThreshold;
        if(!Stream.of(headings).anyMatch(x -> x.equals(heading))){ throw new IllegalArgumentException("heading must be between 01 and 36!");}
        if(!(pos.equals("R")) && !(pos.equals("L"))  && !(pos.equals("C"))){ throw new IllegalArgumentException("position must be R,L or C!");}
        this.name = heading+pos;
    }

    //setter for TORA, redefines the toda,asda and lda (only used if users want to adjust TORA)
    void setTORA(int tora){
        if(tora < 0){ throw new IllegalArgumentException("TORA cannot be negative!");}
        this.tora = tora;
        this.toda = tora + clearway;
        this.asda = tora + stopway;
        this.lda = tora - displacedThreshold;
    }

    //setter for clearway, redefines the TODA (only used if users want to adjust clearway)
    void setClearway(int clearway){
        if(clearway < 0){ throw new IllegalArgumentException("Clearway cannot be negative!");}
        if (clearway > this.tora) { throw new IllegalArgumentException("Clearway cannot be greater than the TORA"); }
        this.clearway = clearway;
        this.toda = tora + clearway;
    }

    //setter for stopway, redefines the ASDA (only used if users want to adjust stopway)
    void setStopway(int stopway){
        if(stopway < 0){ throw new IllegalArgumentException("Stopway cannot be negative!");}
        if (stopway > this.tora) { throw new IllegalArgumentException("Stopway cannot be greater than the TORA"); }
        this.stopway = stopway;
        this.asda = tora + stopway;
    }

    void setResa(int resa){
        if(resa < 0){ throw new IllegalArgumentException("RESA cannot be negative!");}
        this.resa = resa;
    }

    void setAls(int als){
        if(als < 0){ throw new IllegalArgumentException("ALS cannot be negative!");}
        this.als = als;
    }

    void setTocs(int tocs){
        if(tocs < 0){ throw new IllegalArgumentException("TOCS cannot be negative!");}
        this.tocs = tocs;
    }

    void setDisplacedThreshold(int displacedThreshold){
        if(displacedThreshold < 0){ throw new IllegalArgumentException("Displaced Threshold cannot be negative!");}
        if(displacedThreshold > tora){ throw new IllegalArgumentException("Displaced threshold cannot be greater than tora!");}
        this.displacedThreshold = displacedThreshold;
        this.lda = tora - displacedThreshold;
    }

    //allows the obstacle to be removed from the runway
    void removeObstacle(){
        this.obstacle = null;
    }

    //allows an obstacle to be placed on the runway
    void setObstacle(Obstacle obstacle, int distanceFromThreshold, int distanceFromCentreLine) throws IllegalArgumentException {
        this.obstacle = obstacle;
        if ((distanceFromThreshold + this.getDisplacedThreshold() >= 0) && (distanceFromThreshold + this.getDisplacedThreshold() <= this.getTora())) {
            this.obstacle.setDistanceFromThreshold(distanceFromThreshold);
        } else {
            throw new IllegalArgumentException("Obstacle must be placed on the runway");
        }
        if (distanceFromCentreLine <= 150 && distanceFromCentreLine >= -150) {
            this.obstacle.setDistanceFromCentreline(distanceFromCentreLine);
        } else {
            throw new IllegalArgumentException("Obstacle must be placed withing runway bounds");
        }
    }

    //function to allow users to get all the information about the runway
    String printInformation(){
        String text = "";
        text += "--Runway "+getName()+"--";
        text += "\nTORA is "+getTora();
        text += "\nclearway is "+getClearway();
        text += "\nstopway is "+getStopway();
        text += "\nTODA is "+getToda();
        text += "\nASDA is "+getAsda();
        text += "\nLDA is "+getLda();
        text += "\nALS is "+getAls();
        text += "\nTOCS is "+getTocs();
        text += "\nRESA is "+getResa();
        text += "\nLanding threshold is displaced by "+getDisplacedThreshold()+"m";
        try{
            text += "\nThere is a "+obstacle.getName()+" on the runway of height " +obstacle.getHeight() + "m, " + obstacle.getDistanceFromThreshold() + "m from the threshold, "+obstacle.getDistanceFromCentreline()+"m from the centreline";
        } catch (Exception e) {
            text += "\nThere is no obstacle";
        }
        System.out.println(text);
        return text;
    }

    void setPlane(Plane plane){
        this.plane = plane;
    }

    void setUpdated(HashMap<String, Integer> updated) {
        this.updated = updated;
    }

    int getAsda(){
        return this.asda;
    }

    int getToda(){
        return this.toda;
    }

    int getStopway(){
        return this.stopway;
    }

    int getClearway(){
        return this.clearway;
    }

    int getTora(){
        return this.tora;
    }

    int getLda(){return this.lda; }

    Obstacle getObstacle(){
        if(obstacle == null){ throw new NullPointerException("No obstacle on runway");}
        return this.obstacle;
    }

    int getResa() { return this.resa;}

    int getAls() { return this.als;}

    int getTocs(){ return this.tocs;}

    String getName(){ return this.name;}

    void setName(String name){
        String pos = name.substring(2);
        String heading = name.substring(0,2);

        if(!Stream.of(headings).anyMatch(x -> x.equals(heading))){ throw new IllegalArgumentException("heading must be between 01 and 36!");}
        if(!(pos.equals("R")) && !(pos.equals("L"))  && !(pos.equals("C"))){ throw new IllegalArgumentException("position must be R,L or C!");}
        this.name = heading+pos;
    }

    @Override
    public String toString() {
        String baseName = "Runway: Heading = "+ getName() + ", TORA = " + getTora()+ " , Clearway = "+ getClearway() +
                ", Stopway = " +getStopway() +", Displaced Threshold = " +getDisplacedThreshold();
        if (plane != null){
            baseName += "\n Attached Plane: " +getPlane().toString();
        }
        if (obstacle != null){
            baseName += "\n Attached Obstacle: " +getObstacle().toString();
        }
        return baseName;
    }

    int getDisplacedThreshold(){ return this.displacedThreshold;}

    int getStripEnd(){ return this.stripEnd;}

    Plane getPlane(){
        if(plane == null){ throw new NullPointerException("No plane on runway");}
        return this.plane;
    }

    HashMap<String,Integer> getUpdated(){
        if(updated == null){ throw new NullPointerException("Recalculations haven't been done!");}
        return updated;
    }
}
