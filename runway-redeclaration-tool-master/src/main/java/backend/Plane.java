package backend;

import java.util.HashMap;

public class Plane {
    private int blastProtection;
    private String id;
    private String breakdown = "";

    public Plane(String id, int blastProtection) {
        this.id = id;
        this.blastProtection = blastProtection;
    }

    /*
     *  Recalculates the TORA, ASDA, TODA, LDA for an aircraft taking off/landing towards the obstacle
     *  Returns the new values in a HashMap object rather than updating them in the Runway object
     */
    public HashMap<String, Integer> towardsObstacle(Runway runway) {
        HashMap<String, Integer> newValues = new HashMap<String, Integer>();
        int displacedThreshold = runway.getDisplacedThreshold();
        int newTORA;
        int newASDA;
        int newTODA;
        int newLDA;
        int dft = runway.getObstacle().getDistanceFromThreshold();
        boolean flag;
        String msg;
        boolean useALS;
        // Used in TORA calculation
        if (runway.getObstacle().getDistanceFromThreshold() + runway.getDisplacedThreshold() > runway.getTora()/2) {
            flag = true;
        } else {
            dft = (runway.getTora() - runway.getObstacle().getDistanceFromThreshold()); //- BP? - removed but not sure
            flag = false;
        }
        if ( (runway.getObstacle().getHeight() * 50) > runway.getResa()) {
            useALS = true;
        } else { useALS = false; }

        // Calculates the newTORA
        if (useALS) {
            newTORA = dft - (runway.getObstacle().getHeight() * 50) - displacedThreshold - runway.getStripEnd();
        } else {
            newTORA = dft - runway.getResa() - displacedThreshold - runway.getStripEnd();
        }
        // Validate TORA
        if (newTORA < 0) { throw new IllegalArgumentException("Obstacle is too tall to calculate valid angle of descent, no valid TORA");}

        // Sets the new ASDA and TODA
        newASDA = newTORA;
        newTODA = newTORA;
        int tempDisplacedThreshold;
        // Sets the new LDA
        if (flag) { displacedThreshold = 0;}
        newLDA = dft - runway.getResa() - runway.getStripEnd() - displacedThreshold;
        // Validate LDA
        if (newLDA < 0) { throw new IllegalArgumentException("Obstacle is too tall to calculate valid angle of descent, no valid LDA");}

        if (flag) {
            msg = "(TORA) " + Integer.toString(newTORA) + " = (Distance from Threshold) " + Integer.toString(dft) + " - (Strip End) " +
                    Integer.toString(runway.getStripEnd()) + " - (Displaced Threshold) " + Integer.toString(displacedThreshold);

        } else {
            msg = "(TORA-R) " + Integer.toString(newTORA) + " = (TORA-O) " + Integer.toString(runway.getTora()) +
                    " - (Distance from Threshold) " + Integer.toString(runway.getObstacle().getDistanceFromThreshold()) +
                    " - (Displaced Threshold) " + Integer.toString(displacedThreshold) + " - (Strip End) " +
                    Integer.toString(runway.getStripEnd());
        }
        if (useALS) {
            msg = msg + " - (TOCS) " + Integer.toString(runway.getObstacle().getHeight() * 50);
        } else {
            msg = msg + " - (RESA) " + Integer.toString(runway.getResa());
        }

        addToBreakdown(msg);
        addToBreakdown("\n\n(ASDA) " + Integer.toString(newASDA) + " = (TORA-R) " + Integer.toString(newTORA));
        addToBreakdown("\n\n(TODA) " + Integer.toString(newTODA) + " = (TORA-R) " + Integer.toString(newTORA));

        if (flag) {
            addToBreakdown("\n\n(LDA) " + Integer.toString(newLDA) + " = (Distance from Threshold) " + Integer.toString(dft)
            + " - (RESA) " + Integer.toString(runway.getResa()) + " - (Strip End) " + Integer.toString(runway.getStripEnd()));
        } else {
            addToBreakdown("\n\n(LDA) " + Integer.toString(newLDA) + " = (TORA-O) " + Integer.toString(runway.getTora()) +
                    " - (Distance from Threshold) " + Integer.toString(runway.getObstacle().getDistanceFromThreshold()) +
                    " - (RESA) " + Integer.toString(runway.getResa()) + " - (Strip End) " + Integer.toString(runway.getStripEnd()) +
                    " - (Displaced Threshold) " + Integer.toString(displacedThreshold));
        }

        // Place the values in the HashMap
        newValues.put("TORA", newTORA);
        newValues.put("ASDA", newASDA);
        newValues.put("TODA", newTODA);
        newValues.put("LDA", newLDA);

        return newValues;
    }

    /*
     *  Recalculates the TORA, ASDA, TODA, LDA for an aircraft taking off/landing away from the obstacle
     *  Returns the new values in a HashMap object rather than updating them in the Runway object
     */
    public HashMap<String, Integer> awayFromObstacle(Runway runway) {
        HashMap<String, Integer> newValues = new HashMap<String, Integer>();
        int newASDA;
        int newTODA;
        int newLDA;
        int newTORA = runway.getTora();
        int dft;
        boolean flag;
        String msg;
        String LDAmsg;
        String LDAsubmsg;
        // Used in TORA calculation
        if (runway.getObstacle().getDistanceFromThreshold() + runway.getDisplacedThreshold() < runway.getTora()/2) {
            dft = runway.getObstacle().getDistanceFromThreshold();
            flag = true;
        } else {
            dft = runway.getTora() - runway.getObstacle().getDistanceFromThreshold();
            flag = false;
        }
        newTORA -= dft + getBlastProtection() + runway.getDisplacedThreshold();;
        // Validate TORA
        if (newTORA < 0) { throw new IllegalArgumentException("Not enough space on the runway for take-off");}
        // Sets the new ASDA and TODA
        newASDA = newTORA + runway.getStopway();
        newTODA = newTORA + runway.getClearway();
        // Sets the new LDA
        if ( (runway.getObstacle().getHeight() * 50) > runway.getResa() ) {
            if ((runway.getObstacle().getHeight() * 50) + runway.getStripEnd() > this.blastProtection) {
                newLDA = runway.getLda() - (runway.getObstacle().getHeight() * 50)
                        - dft - runway.getStripEnd() - runway.getDisplacedThreshold();
                LDAsubmsg = " - (ALS) " + Integer.toString(runway.getObstacle().getHeight() * 50) + " - (Strip End) " + Integer.toString(runway.getStripEnd()) +
                        " - (Displaced Threshold) " + Integer.toString(runway.getDisplacedThreshold());
            } else {
                newLDA = runway.getLda() - dft - this.blastProtection - runway.getDisplacedThreshold();
                LDAsubmsg = " - (Blast Protection) " + Integer.toString(this.blastProtection) + " - (Displaced Threshold) " + Integer.toString(runway.getDisplacedThreshold());
            }
        } else {
            if (runway.getResa() + runway.getStripEnd() > this.blastProtection) {
                newLDA = runway.getLda() - (runway.getResa()) - dft - runway.getStripEnd();
                LDAsubmsg = " - (RESA) " + Integer.toString(runway.getResa()) + " - (Strip End) " + Integer.toString(runway.getStripEnd());;
            } else {
                newLDA = runway.getLda() - dft - this.blastProtection;
                LDAsubmsg = " - (Blast Protection) " + Integer.toString(this.blastProtection);
            }
        }

        // Validate LDA
        if (newLDA < 0) { throw new IllegalArgumentException("Obstacle is too tall to calculate valid angle of descent, no valid LDA given current obstacle");}

        if (flag) {
            msg = "(TORA-R) " + Integer.toString(newTORA) + " = (TORA-O) " + Integer.toString(runway.getTora()) +
                    " - (Distance from Threshold) " + Integer.toString(dft) + " - (Blast Protection) " +
                    Integer.toString(this.getBlastProtection()) + " - (Displaced Threshold) " + Integer.toString(runway.getDisplacedThreshold());
        } else{
            msg = "(TORA-R) " + Integer.toString(newTORA) + " = (Distance from Threshold) " + Integer.toString(dft) +
                    " - (RESA) " + Integer.toString(runway.getResa()) + " - (Strip End) " + Integer.toString(runway.getStripEnd());
        }

        addToBreakdown(msg);
        addToBreakdown("\n\n(ASDA) " + Integer.toString(newASDA) + " = (TORA-R) " + Integer.toString(newTORA) +
                " + (Stopway) " + Integer.toString(runway.getStopway()));
        addToBreakdown("\n\n(TODA) " + Integer.toString(newTODA) + " = (TORA-R) " + Integer.toString(newTODA) +
                " + (Clearway) " + Integer.toString(runway.getClearway()));
        if (flag) {
            LDAmsg = "\n\n(LDA-R) " + Integer.toString(newLDA) + " = (LDA-O) " + Integer.toString(runway.getLda()) +
                    " - (Distance from Threshold) " + Integer.toString(dft);
        } else {
            LDAmsg = "\n\n(LDA-R) " + Integer.toString(newLDA) + " = (LDA-O) " + Integer.toString(runway.getLda()) +
                    " - (TORA-O - Distance from Threshold) " + Integer.toString(dft);
        }

        LDAmsg = LDAmsg + LDAsubmsg;
        addToBreakdown(LDAmsg);

        // Place the values in the HashMap
        newValues.put("TORA", newTORA);
        newValues.put("ASDA", newASDA);
        newValues.put("TODA", newTODA);
        newValues.put("LDA", newLDA);

        return newValues;
    }

    public int getBlastProtection() { return this.blastProtection;}
    public String getID() { return this.id;}

    public void setBlastProtection(int blastProtection) throws IllegalArgumentException {
        if (blastProtection >= 0) {
            this.blastProtection = blastProtection;
        } else {
            throw new IllegalArgumentException("Negative blast protection value passed");
        }
    }

    @Override
    public String toString() {
        return  "\"" + getID() + "\", bp = " + getBlastProtection();
    }

    String getBreakdown() { return this.breakdown;}

    void resetBreakdown(){ this.breakdown = "";}

    void setBreakdown(String s) {
        this.breakdown = s;
    }

    void addToBreakdown(String s) {
        this.breakdown = this.breakdown + s;
    }
}
