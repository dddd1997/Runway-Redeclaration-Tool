package backend;

import javafx.scene.control.TextArea;

import java.io.*;

public class TextWriter {
    public static String writeToFile(Runway runway, String filePath) throws IOException {
        File target = new File(filePath);
        try (
            Writer writer =
                new BufferedWriter(
                    new OutputStreamWriter(
                        new FileOutputStream(target),"utf-8"
                    )
                )
        ) {
            writer.write(
                Calculator.getObstacleOfRunway(runway) +" is on the runway " +
                Calculator.getObstacleOfRunway(runway).getDistanceFromThreshold() + "m from the threshold and " +
                Calculator.getObstacleOfRunway(runway).getDistanceFromCentreline() + "m from the centre line " +
                "\noriginal TORA is "+Calculator.getTORA(runway) +
                "\noriginal TODA is "+Calculator.getTODA(runway) +
                "\noriginal LDA is "+Calculator.getLDA(runway) +
                "\noriginal ASDA is "+Calculator.getASDA(runway) +
                "\nupdated TORA is "+Calculator.getUpdatedTORA(runway) +
                "\nupdated TODA is "+Calculator.getUpdatedTODA(runway) +
                "\nupdated LDA is "+ Calculator.getUpdatedLDA(runway) +
                "\nupdated ASDA is "+Calculator.getUpdatedASDA(runway)
            );
        }
        return ("Configuration exported as .txt file");
    }
}
