package backend;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class XMLParserTest {
    @Test
    public void CheckExampleXML() {
        System.out.println("Checking Example XML file");
        XMLParser xmlParser = new XMLParser();
        File f = new File("src/main/resources/backend/ExampleXML.xml");
        try {
            xmlParser.readXML(f);
        } catch (IOException e) {
            System.out.println("Error");
            System.out.println(e.getMessage());
        }
    }
}
