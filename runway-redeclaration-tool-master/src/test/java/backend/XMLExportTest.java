package backend;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.fail;

public class XMLExportTest {
    @Test
    public void CheckExportXML() {
        System.out.println("Checking XML exporting");
        XMLParser xmlParser = new XMLParser();
        String path = "src/main/resources/backend";
        File f = new File("src/main/resources/backend/ExampleXML.xml");
        try {
            xmlParser.readXML(f);
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
        try {
            xmlParser.outputXML(path);
        } catch (Exception e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
            fail("should not have reached exception");
        }
    }

}
