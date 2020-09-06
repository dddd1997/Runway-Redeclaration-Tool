package backend;

import javafx.scene.control.TextArea;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class XMLParser {
    public String readXML(File f) throws IOException {
        // Instantiate lists
        List<Runway> runways = new LinkedList<>();
        List<Obstacle> obstacles = new LinkedList<>();
        List<Plane> planes = new LinkedList<>();

        // Create a Document object containing the XML data
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document doc = null;
        try {
            builder = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        try {
            assert builder != null;
            doc = builder.parse(f);
        } catch (SAXException | NullPointerException e) {
            e.printStackTrace();
        }

        // Get a list of all Runway elements
        assert doc != null;
        NodeList nList = doc.getElementsByTagName("runway");
        // Array storing required attributes
        String[] attributes = {"tora", "clearway", "stopway", "name", "displacedThreshold"};
        // Loop through all runway elements
        for (int i = 0; i < nList.getLength(); i++) {
            Node nNode = nList.item(i);

            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                // Cast node to Element
                Element eElem = (Element) nNode;
                // Check for any missing/invalid XML tags
                for (String s : attributes) {
                    if (!eElem.hasAttribute(s)) {
                        throw new IOException("Invalid or Missing XML Tag");
                    }
                }
                // Create a new runway and add it to runways list
                if (eElem.hasAttribute("position")) {
                    runways.add(new Runway(Integer.parseInt(eElem.getAttribute("tora")),
                            Integer.parseInt(eElem.getAttribute("clearway")),
                            Integer.parseInt(eElem.getAttribute("stopway")),
                            eElem.getAttribute("name"),
                            eElem.getAttribute("position"),
                            Integer.parseInt(eElem.getAttribute("displacedThreshold"))));
                } else {
                    runways.add(new Runway(Integer.parseInt(eElem.getAttribute("tora")),
                            Integer.parseInt(eElem.getAttribute("clearway")),
                            Integer.parseInt(eElem.getAttribute("stopway")),
                            eElem.getAttribute("name"),
                            Integer.parseInt(eElem.getAttribute("displacedThreshold"))));
                }
                // Get obstacle and cast to Element
                try {
                    Element obstacle = (Element) ((Element) nNode).getElementsByTagName("obstacle").item(0);
                    // Get obstacle details
                    int height = Integer.parseInt(obstacle.getAttribute("height"));
                    String name = obstacle.getAttribute("name");
                    int dft = Integer.parseInt(obstacle.getAttribute("dft"));
                    int dfc = Integer.parseInt(obstacle.getAttribute("dfc"));
                    // Set the obstacle to the runway
                    runways.get(i).setObstacle(new Obstacle(height, name), dft, dfc);
                } catch (NullPointerException e) {
                    System.out.println("No obstacle found");
                }

                try {
                    // Get plane and cast to Element
                    Element plane = (Element) ((Element) nNode).getElementsByTagName("plane").item(0);
                    // Get plane details
                    String id = plane.getAttribute("id");
                    int blastProtection = Integer.parseInt(plane.getAttribute("blastProtection"));
                    // Set the plane to the runway
                    runways.get(i).setPlane(new Plane(id, blastProtection));
                } catch (NullPointerException e) {
                    System.out.println("No plane found");
                }
            }
        }
        // Get a list of all pre-defined obstacles
        NodeList nListO = doc.getElementsByTagName("preDefinedObstacle");
        // Loop through all pre-defined obstacles
        for (int i = 0; i < nListO.getLength(); i++) {
            Node nNode = nListO.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;
                obstacles.add(new Obstacle(Integer.parseInt(elem.getAttribute("height")), elem.getAttribute("name")));
            }
        }

        // Get a list of all pre-defined planes
        NodeList nListP = doc.getElementsByTagName("preDefinedPlane");
        // Loop through all pre-defined planes
        for (int i = 0; i < nListP.getLength(); i++) {
            Node nNode = nListP.item(i);
            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) nNode;
                planes.add(new Plane(elem.getAttribute("id"), Integer.parseInt(elem.getAttribute("blastProtection"))));
            }
        }

        Calculator.setObstacles(obstacles);
        Calculator.setPlanes(planes);
        Calculator.setRunways(runways);
        return("XML file Imported");
    }

    // Takes a full file path.
    public String outputXML(String path) {
        List<Runway> runways = Calculator.getRunways();
        List<Plane> planes = Calculator.getPlanes();
        List<Obstacle> obstacles = Calculator.getObstacles();
        // Create file if file doesn't exist
        File f = new File(path);
        try {
            if (f.createNewFile()) {
                System.out.println("New XML file created");
            } else {
                System.out.println("Existing XML save found");
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        // Build document
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            //Build document
            dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.newDocument();
            // Create root node: 'airport' and append to the doc
            Element rootElement = doc.createElement("airport");
            doc.appendChild(rootElement);
            // Create child node: 'runways' and append to root node ('airport')
            Element collectRunways = doc.createElement("runways");
            rootElement.appendChild(collectRunways);
            // Iterate through runways and append to node: 'runways'
            for (Runway r : runways) {
                // Create runway tag
                Element rEle = doc.createElement("runway");
                // Handle name attribute
                String name = r.getName();
                if (name.length() > 2) {
                    rEle.setAttribute("name", name.substring(0,2));
                    rEle.setAttribute("position", Character.toString(name.charAt(2)));
                } else {
                    rEle.setAttribute("name", name);
                }
                // Handle tora attribute
                rEle.setAttribute("tora", Integer.toString(r.getTora()));
                // Handle displaced Threshold
                rEle.setAttribute("displacedThreshold", Integer.toString(r.getDisplacedThreshold()));
                // Handle clearway
                rEle.setAttribute("clearway", Integer.toString(r.getClearway()));
                // Handle stopway
                rEle.setAttribute("stopway", Integer.toString(r.getStopway()));
                // Append the runway object within the 'runways' tags
                collectRunways.appendChild(rEle);
                // Add any obstacles on the runway to the runway
                try {
                    Obstacle o = r.getObstacle();
                    Element roEle = doc.createElement("obstacle");
                    roEle.setAttribute("name", o.getName());
                    roEle.setAttribute("height", Integer.toString(o.getHeight()));
                    roEle.setAttribute("dft", Integer.toString(o.getDistanceFromThreshold()));
                    roEle.setAttribute("dfc", Integer.toString(o.getDistanceFromCentreline()));

                    rEle.appendChild(roEle);
                } catch (NullPointerException e) {
                    System.out.println("No Obstacle on the runway");
                }
                // Add the Plane on the runway if it exists
                try {
                    Plane p = r.getPlane();
                    Element rpEle = doc.createElement("plane");
                    rpEle.setAttribute("id", p.getID());
                    rpEle.setAttribute("blastProtection", Integer.toString(p.getBlastProtection()));

                    rEle.appendChild(rpEle);
                } catch (NullPointerException e) {
                    System.out.println("No Plane on the runway");
                }
            }

            for (Obstacle o : obstacles) {
                Element oEle = doc.createElement("preDefinedObstacle");
                oEle.setAttribute("name", o.getName());
                oEle.setAttribute("height", Integer.toString(o.getHeight()));

                rootElement.appendChild(oEle);
            }

            for (Plane p : planes) {
                Element pEle = doc.createElement("preDefinedPane");
                pEle.setAttribute("id", p.getID());
                pEle.setAttribute("blastProtection", Integer.toString(p.getBlastProtection()));

                rootElement.appendChild(pEle);
            }
            // For outputting to the file
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            // Adds indenting into the outputted file
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult file = new StreamResult(f);
            // Write the data to the file
            transformer.transform(source, file);

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return ("XML file Exported");
    }
}
