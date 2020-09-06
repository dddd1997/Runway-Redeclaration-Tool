package frontend;

import backend.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Vector;

public class AppController {
    @FXML
    private TabPane tabPane;

    // Status Tab
    // XML section.
    @FXML
    private TextField loadXMLFileTextField;
    @FXML
    private Button loadXMLButton;
    @FXML
    private TextField exportXMLFileTextField;

    // Initial Runway Parameters section.
    @FXML
    private Label initialToraLabel;
    @FXML
    private Label initialTodaLabel;
    @FXML
    private Label initialLdaLabel;
    @FXML
    private Label initialAsdaLabel;

    // Updated Runway Parameters section.
    @FXML
    private Label updatedToraLabel;
    @FXML
    private Label updatedTodaLabel;
    @FXML
    private Label updatedLdaLabel;
    @FXML
    private Label updatedAsdaLabel;

    // Status tab bottom section (Breakdown & Log)
    @FXML
    private TextArea calculationsTextArea;
    @FXML
    private TextArea actionLogTextArea;


    // Actions Tab
    // Set Runway Parameters section.
    @FXML
    private ChoiceBox<Runway> predefinedRunwayChoiceBox;
    @FXML
    private TextField setTORATextField;
    @FXML
    private TextField setClearwayTextField;
    @FXML
    private TextField setStopwayTextField;
    @FXML
    private TextField setDisplacedThresholdTextField;

    // Obstacle section.
    @FXML
    private ChoiceBox<Obstacle> predefinedObstacleChoiceBox;
    @FXML
    private TextField obstacleNameTextField;
    @FXML
    private TextField obstacleHeightTextField;
    @FXML
    private TextField distanceFromThresholdTextField;
    @FXML
    private TextField distanceFromCentrelineTextField;

    // Plane section.
    @FXML
    private ChoiceBox<Plane> predefinedPlaneChoiceBox;
    @FXML
    private TextField planeIDTextField;
    @FXML
    private TextField planeBlastProtectionTextField;

    // Redeclare Parameters section.
    @FXML
    private ChoiceBox actionChoiceBox;
    @FXML
    private CheckBox goingLeftBox;

    
    // Top View Tab
    @FXML
    private ImageView obstacleImage;
    @FXML
    private ImageView topDownRunway;
    @FXML
    private ImageView topDownBackground;
    @FXML
    private StackPane topDownStackPane;
    @FXML
    private Rectangle displacedThreshold;
    @FXML
    private Rectangle topViewStopwayStart;
    @FXML
    private Rectangle topViewStopwayEnd;
    @FXML
    private Rectangle topViewClearway;

    // Headings for top view
    @FXML
    private Label headingPosL;
    @FXML
    private Label headingPosR;
    @FXML
    private Label headingNumL;
    @FXML
    private Label headingNumR;

    @FXML

    private Button plusRotateTopView;

    @FXML
    private Button minusRotateTopView;

    @FXML
    private Label rotLabel;

    @FXML
    private Label rotationValueTopView;

    
    // Side View Tab
    @FXML
    private StackPane sideViewStackPane;
    @FXML
    private ImageView sideViewRunwayImage;


    // This will be used for simplicity for the moment as we don't have support for multiple runways yet.
    private Runway selectedRunway = Calculator.getRunways().get(0);
    private Obstacle currentObstacle;

    private XMLParser xmlParser = new XMLParser();
    private TextWriter textWriter = new TextWriter();

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    
    @FXML
    void initialize() {
        initialiseTopView();
        // Set the values for the initial runway parameters text fields.
        updateInitialRunwayParameterTextFields();

        // TODO: Populate the predefined runways choice box.

        // Set the values for the text fields in the "Runway Parameters" section to display the initial runway parameters.
        setTORATextField.setText(String.valueOf(Calculator.getTORA(selectedRunway)));
        setClearwayTextField.setText(String.valueOf(Calculator.getClearway(selectedRunway)));
        setStopwayTextField.setText(String.valueOf(Calculator.getStopway(selectedRunway)));
        setDisplacedThresholdTextField.setText(String.valueOf(Calculator.getDisplacedThreshold(selectedRunway)));

        // Populate the predefined obstacles and predefined planes choice boxes.
        predefinedObstacleChoiceBox.setItems(FXCollections.observableList(Calculator.getObstacles()));
        predefinedPlaneChoiceBox.setItems(FXCollections.observableList(Calculator.getPlanes()));

        // Populate the action choice box.
        List<String> actionStringList = new Vector<>();
        actionStringList.add("Takeoff");
        actionStringList.add("Landing");
        actionChoiceBox.setItems(FXCollections.observableList(actionStringList));

        // Give keyboard focus to the "Load XML" button.
        Platform.runLater(()->loadXMLButton.requestFocus());

        Calculator.setNotificationLog( "" + LocalTime.now().format(formatter) + "\tSystem Started");
        actionLogTextArea.setText(Calculator.getNotificationLog());
    }

    void updateNotificationLog(String s) {
        Calculator.addNotificationItem(s);
        actionLogTextArea.setText(Calculator.getNotificationLog());
    }

    // STATUS TAB METHODS
    @FXML
    // On pushing the "Load XML" button, show a file chooser, load the selected XML file and update the
    // "Load XML path:" textfield with the selected path.
    void onLoadXMLFile(){
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("XML Files", "*.xml"),
                    new ExtensionFilter("All Files", "*.*"));

            Window currentWindow = Stage.getWindows().stream().filter(Window::isFocused).findFirst().orElse(null);
            File selectedFile = fileChooser.showOpenDialog(currentWindow);

            // Store the file in instance var and set path label
            if (selectedFile != null){
                String filePath = selectedFile.getAbsolutePath();
                loadXMLFileTextField.setText(filePath);
                File xmlFile = selectedFile;

                updateNotificationLog(xmlParser.readXML(xmlFile));

                // Populate the runway, obstacle and plane choice boxes with predefined values.
                predefinedRunwayChoiceBox.setItems(FXCollections.observableList(Calculator.getRunways()));
                predefinedObstacleChoiceBox.setItems(FXCollections.observableList(Calculator.getObstacles()));
                predefinedPlaneChoiceBox.setItems(FXCollections.observableList(Calculator.getPlanes()));
            }
        }
        catch (IOException e){
            showAlert("Invalid XML format!");
            updateNotificationLog("Failed to load XML file");
        }
    }
    @FXML
    // Opens a FileChooser which strictly saves XML files
    // On pushing the "Export XML" button, show a file chooser, export to the selected path as XML and update the
    // "Export XML path:" textfield with the selected path.
    void onExportXML() {
        FileChooser fileChooser = new FileChooser();

        Window currentWindow = Stage.getWindows().stream().filter(Window::isFocused).findFirst().orElse(null);
        File selectedFile = fileChooser.showSaveDialog(currentWindow);

        if (selectedFile != null){
            String absoluteFilePath = selectedFile.getAbsolutePath();
            updateNotificationLog(xmlParser.outputXML(absoluteFilePath));
            exportXMLFileTextField.setText(absoluteFilePath);
        }
    }
    @FXML
    // On pushing the "Export Text" button, show a file chooser and export to the selected path as a text file.
    void onExportText() {
        FileChooser fileChooser = new FileChooser();

        Window currentWindow = Stage.getWindows().stream().filter(Window::isFocused).findFirst().orElse(null);
        File selectedFile = fileChooser.showSaveDialog(currentWindow);

        if (selectedFile != null){
            try {
                updateNotificationLog(Calculator.writeToFile(selectedRunway, selectedFile.getAbsolutePath()));
            } catch (IOException e){
                showAlert("Failed to export text file.");
                updateNotificationLog("Failed to export text file");
            } catch (NullPointerException e) {
                showAlert("Cannot export with empty fields");
                updateNotificationLog("Failed to write to .txt file");
            }
        }
    }

    //Set the values on the GUI textfields for the initial runway parameters.
    private void updateInitialRunwayParameterTextFields() {
        initialLdaLabel.setText(Integer.toString(Calculator.getLDA(selectedRunway)));
        initialToraLabel.setText(Integer.toString(Calculator.getTORA(selectedRunway)));
        initialAsdaLabel.setText(Integer.toString(Calculator.getASDA(selectedRunway)));
        initialTodaLabel.setText(Integer.toString(Calculator.getTODA(selectedRunway)));
    }
    //Set the values on the GUI textfields for the updated runway parameters.
    private void updateUpdatedRunwayParameterTextFields() {
        updatedLdaLabel.setText(Integer.toString(Calculator.getUpdatedLDA(selectedRunway)));
        updatedToraLabel.setText(Integer.toString(Calculator.getUpdatedTORA(selectedRunway)));
        updatedAsdaLabel.setText(Integer.toString(Calculator.getUpdatedASDA(selectedRunway)));
        updatedTodaLabel.setText(Integer.toString(Calculator.getUpdatedTODA(selectedRunway)));
    }


    // ACTIONS TAB METHODS
    @FXML
    // On selecting a runway from the drop down populate the fields and fields of any attached objects
    void onPredefinedRunwaySelection() {
        setTORATextField.setText("");
        setClearwayTextField.setText("");
        setStopwayTextField.setText("");
        setDisplacedThresholdTextField.setText("");

        Runway pickedRunway = predefinedRunwayChoiceBox.getValue();

        if (pickedRunway != null){
            setTORATextField.setText(String.valueOf(Calculator.getTORA(pickedRunway)));
            setClearwayTextField.setText(String.valueOf(Calculator.getClearway(pickedRunway)));
            setStopwayTextField.setText(String.valueOf(Calculator.getStopway(pickedRunway)));
            setDisplacedThresholdTextField.setText(String.valueOf(Calculator.getClearway(pickedRunway)));

            String name = Calculator.getRunwayName(pickedRunway);
            Calculator.setRunwayName(selectedRunway, name);

            try {
                Obstacle selectedObstacle = Calculator.getObstacleOfRunway(pickedRunway);

                obstacleHeightTextField.setText(String.valueOf(Calculator.getObstacleHeight(selectedObstacle)));
                obstacleNameTextField.setText(Calculator.getObstacleName(selectedObstacle));
                distanceFromThresholdTextField.setText(String.valueOf(Calculator.getObstacleDistanceFromThreshold(selectedObstacle)));
                distanceFromCentrelineTextField.setText(String.valueOf(Calculator.getObstacleDistanceFromCentreline(selectedObstacle)));
            } catch (NullPointerException e){
            }

            try {
                Plane selectedPlane = Calculator.getPlaneOfRunway(pickedRunway);

                planeIDTextField.setText(Calculator.getPlaneID(selectedPlane));
                planeBlastProtectionTextField.setText(String.valueOf(Calculator.getPlaneBlastProtection(selectedPlane)));

            } catch (NullPointerException e){

            }
        }

    }

    @FXML
    // On selecting an obstacle from the dropdown:
    // Clear the distance from threshold and distance from centreline textfields.
    // Set the obstacle name and obstacle height textfields to the values of the selected obstacle.
    void onPredefinedObstacleSelection() {
        distanceFromThresholdTextField.setText("");
        distanceFromCentrelineTextField.setText("");

        Obstacle selectedObstacle = predefinedObstacleChoiceBox.getValue();

        if (selectedObstacle != null) {
            obstacleHeightTextField.setText(String.valueOf(Calculator.getObstacleHeight(selectedObstacle)));
            obstacleNameTextField.setText(Calculator.getObstacleName(selectedObstacle));
        }
    }
    @FXML
    // On selecting a plane from the dropdown populate the 'ID' and the 'Blast Protection' textfields.
    void onPredefinedPlaneSelection() {
        Plane selectedPlane = predefinedPlaneChoiceBox.getValue();

        if (selectedPlane == null) {
            planeIDTextField.setText("");
            planeBlastProtectionTextField.setText("");
        } else {
            planeIDTextField.setText(Calculator.getPlaneID(selectedPlane));
            planeBlastProtectionTextField.setText(String.valueOf(Calculator.getPlaneBlastProtection(selectedPlane)));
        }
    }

    @FXML
    // Update the model with the new values of the runway parameters and change the values of the initial runway
    // parameters text fields. Returns false if unsuccessful.
    boolean setRunwayParameters() {
        // Backup the current runway parameters
        int previousTORA = Calculator.getTORA(selectedRunway);
        int previousClearway = Calculator.getClearway(selectedRunway);
        int previousStopway = Calculator.getStopway(selectedRunway);
        int previousDisplacedThreshold = Calculator.getDisplacedThreshold(selectedRunway);

        // Read the textfields.
        String toraText, clearwayText, stopwayText, displacedThresholdText;
        toraText = setTORATextField.getText();
        clearwayText = setClearwayTextField.getText();
        stopwayText = setStopwayTextField.getText();
        displacedThresholdText = setDisplacedThresholdTextField.getText();

        // Check if all textfields were filled.
        if (toraText.trim().isBlank() || clearwayText.trim().isBlank() || stopwayText.trim().isBlank()
                || displacedThresholdText.trim().isBlank()) {
            showAlert("Please fill in all the fields for the Runway Parameters section.");
            return false;
        }

        // Parse the input for numbers.
        int tora, clearway, stopway, displacedThrehold;

        try {
            tora = Integer.parseInt(setTORATextField.getText());
        }
        catch (NumberFormatException exception) {
            showAlert("Invalid integer given for TORA.");
            return false;
        }

        try {
            clearway = Integer.parseInt(setClearwayTextField.getText());
        }
        catch (NumberFormatException exception) {
            showAlert("Invalid integer given for Clearway.");
            return false;
        }

        try {
            stopway = Integer.parseInt(setStopwayTextField.getText());
        }
        catch (NumberFormatException exception) {
            showAlert("Invalid integer given for Stopway.");
            return false;
        }

        try {
            displacedThrehold = Integer.parseInt(setDisplacedThresholdTextField.getText());
        }
        catch (NumberFormatException exception) {
            showAlert("Invalid integer given for displaced threshold.");
            return false;
        }

        // Set new distances for the runway.
        try {
            Calculator.setTora(tora, selectedRunway);
            Calculator.SetClearwayAndStopway(selectedRunway, clearway, stopway);
            Calculator.setDisplacedThreshold(displacedThrehold, selectedRunway);

            updateInitialRunwayParameterTextFields();
            return true;
        }
        // If there are any errors with the values, recover to the previously used values.
        catch (IllegalArgumentException exception) {
            /* TODO:
                Send a log message here saying "Reverting Runway Parameters change...".
                This will be required if for example 'Calculator.setClearway()' sends out a log message.
             */
            Calculator.setTora(previousTORA, selectedRunway);
            Calculator.setClearway(previousClearway, selectedRunway);
            Calculator.setStopway(previousStopway, selectedRunway);
            Calculator.setDisplacedThreshold(previousDisplacedThreshold, selectedRunway);

            showAlert(exception.getMessage());
            return false;
        }
    }
    @FXML
    // Read the values from the 'Obstacle' text fields and set a new obstacle of those values on the runway.
    // Returns false if unsuccessful.
    boolean addObstacle() {
        // Read the textfields.
        String obstacleNameText, obstacleHeightText, distanceFromThresholdText, distanceFromCentreLineText;
        obstacleNameText = obstacleNameTextField.getText();
        obstacleHeightText = obstacleHeightTextField.getText();
        distanceFromThresholdText = distanceFromThresholdTextField.getText();
        distanceFromCentreLineText = distanceFromCentrelineTextField.getText();

        // Check if all textfields were filled.
        if (obstacleNameText.trim().isBlank() || obstacleHeightText.trim().isBlank() ||
                distanceFromThresholdText.trim().isBlank() || distanceFromCentreLineText.trim().isBlank()) {
            showAlert("Please fill in all the fields for the Obstacle section.");
            return false;
        }

        String obstacleName = obstacleNameText;

        // Parse the input for numbers.
        int obstacleHeight, distanceFromThreshold, distanceFromCentreLine;
        try {
            obstacleHeight = Integer.parseInt(obstacleHeightTextField.getText());
        }
        catch (NumberFormatException exception) {
            showAlert("Invalid integer given for obstacle height.");
            return false;
        }
        try {
            distanceFromThreshold = Integer.parseInt(distanceFromThresholdTextField.getText());
        }
        catch (NumberFormatException exception) {
            showAlert("Invalid integer given for obstacle distance from threshold.");
            return false;
        }
        try {
            distanceFromCentreLine = Integer.parseInt(distanceFromCentrelineTextField.getText());
        }
        catch (NumberFormatException exception) {
            showAlert("Invalid integer given for obstacle distance from centre line.");
            return false;
        }

        // Save the current obstacle. This is constructed as new because the user might change the name and height
        // loaded in from a predefined obstacle.
        currentObstacle = new Obstacle(obstacleHeight, obstacleName);
        // Set the obstacle on the runway
        try {
            Calculator.addObstacleToRunway(currentObstacle, distanceFromThreshold, distanceFromCentreLine, selectedRunway);
        } catch (IllegalArgumentException e) {
            showAlert(e.getMessage());
            return false;
        }

        return true;
    }


    @FXML
    // Read the values from the 'Plane' text fields and set a new plane of those values on the runway.
    // Returns false if unsuccessful.
    boolean addPlane() {
        try {
            // Check if either text field is blank.
            if (planeIDTextField.getText().isBlank() || planeBlastProtectionTextField.getText().isBlank()) {
                showAlert("Please fill in all the fields for the Plane section.");
                return false;
            }
            else {
                // Read the textfields
                String planeID = planeIDTextField.getText();
                int planeBlastProtection = Integer.parseInt(planeBlastProtectionTextField.getText());

                Calculator.addPlaneToRunway(new Plane(planeID, planeBlastProtection), selectedRunway);
                return true;
            }
        }
        catch (NumberFormatException exception) {
            showAlert("Invalid integer entered for Blast Protection.");
            return false;
        }
        // No more exceptions to catch as neither addPlaneToRunway nor the Plane(String, int) constructor throw exceptions.
    }
    @FXML
    // On pushing the "Redeclare Parameters" button, update the model with the updated values and change the values of
    // the updated runway parameters text fields.
    void onRedeclareParameters() {
        if (setRunwayParameters() && addObstacle() && addPlane()) {
            // Should show an alert if no obstacle is on the runway.
            if (!Calculator.hasObstacle(selectedRunway)) {
                showAlert("No obstacles loaded.");
            }
            // Should show an alert if no plane is on the runway.
            else if (!Calculator.hasPlane(selectedRunway)) {
                showAlert("No plane on runway.");
            }
            // Should show an alert if no action is selected.
            else if (actionChoiceBox.getSelectionModel().isEmpty()) {
                showAlert("No action selected.");
            } else {
                String obsName = Calculator.getObstacleOfRunway(selectedRunway).getName();
                String planeName = Calculator.getPlaneOfRunway(selectedRunway).getID();
                updateNotificationLog("Plane: " + planeName + " added to the runway");
                updateNotificationLog("Obstacle: " + obsName + " added to the runway");
                String selectedAction = (String) actionChoiceBox.getSelectionModel().getSelectedItem();

                double objectDist = Calculator.getObstacleDistanceFromThreshold(selectedRunway);
                double tora = Calculator.getTORA(selectedRunway);

                // measured from the left
                boolean left = objectDist <= 0.5 * tora;
                boolean right = !left;
                boolean goingLeft = goingLeftBox.isSelected();
                boolean goingRight = !goingLeft;

                // Set the updated distances depending on if the plane is going towards or away from the obstacle.
                try {
                    if (left && goingLeft) {
                        Calculator.towardsObstacle(selectedRunway);
                    } else if (right && goingRight) {
                        Calculator.towardsObstacle(selectedRunway);
                    } else if (left && goingRight) {
                        Calculator.awayFromObstacle(selectedRunway);
                    } else if (right && goingLeft) {
                        Calculator.awayFromObstacle(selectedRunway);
                    }

                    // Update calculations breakdown
                    calculationsTextArea.setText(Calculator.getBreakdown(selectedRunway));

                    // Update Notifications log
                    actionLogTextArea.setText(Calculator.getNotificationLog());
                    // Update the updated runway parameters textfields.
                    updateUpdatedRunwayParameterTextFields();
                    refreshSideView();
                    refreshTopView();
                    tabPane.getSelectionModel().selectFirst();
                } catch (IllegalArgumentException | NullPointerException e) {
                    showAlert(e.getMessage());
                }
            }
        }
    }


    // TOP VIEW TAB METHODS
    @FXML
    private void rotateObjectPositive() {

        topDownStackPane.setRotate(topDownStackPane.getRotate() + 10);
        updateRotateValueTopDown(true);
    }

    @FXML
    private void rotateObjectNegative() {

        topDownStackPane.setRotate(topDownStackPane.getRotate() - 10);
        updateRotateValueTopDown(false);
    }
    @FXML
    public void updateRotateValueTopDown(boolean ispos) {
        int currentRot = (int)topDownStackPane.getRotate();
        if (currentRot == 360) {
            topDownStackPane.setRotate(0);
        }

        int displayval = (currentRot + 90 )/10;
        if (displayval > 36) {
            displayval = displayval - 36;
        }
        this.rotationValueTopView.setText(Integer.toString(displayval));

    }



    @FXML
    public void updateClearwayTopViewGoingRight() {
        int widthMeters = Calculator.getClearway(selectedRunway);

        int runwayLengthMeters = Calculator.getTORA(selectedRunway);
        // Length of the runway in pixels.

        double runwayLengthPixels = topDownRunway.getFitWidth();

        // pixels per meter

        double ratio = runwayLengthPixels / runwayLengthMeters;

        double pixels = ratio * widthMeters;

        //set the clearway pixel width.
        this.topViewClearway.setWidth(pixels);

        //translate the clearway half a runway and half its width.

        this.topViewClearway.setTranslateX(0);



        double clearwayDisplaceAmount = runwayLengthPixels/2 + pixels/2;

        this.topViewClearway.setTranslateX(clearwayDisplaceAmount);
        this.topViewClearway.setVisible(true);

    }
    @FXML
    public void updateClearwayTopViewGoingLeft() {
        int widthMeters = Calculator.getClearway(selectedRunway);

        int runwayLengthMeters = Calculator.getTORA(selectedRunway);
        // Length of the runway in pixels.

        double runwayLengthPixels = topDownRunway.getFitWidth();

        // pixels per meter

        double ratio = runwayLengthPixels / runwayLengthMeters;

        double pixels = ratio * widthMeters;

        //set the clearway pixel width.
        this.topViewClearway.setWidth(pixels);

        //translate the clearway half a runway and half its width.

        this.topViewClearway.setTranslateX(0);



        double clearwayDisplaceAmount = runwayLengthPixels/2 + pixels/2;

        //displace by amount negative of going right view, since we are going left.
        this.topViewClearway.setTranslateX(-clearwayDisplaceAmount);
        this.topViewClearway.setVisible(true);

    }
    //updates the width of the stopway in the top view, both front of runway and end.
    @FXML
    public void updateStopWayTopView() {
        int widthMeters = Calculator.getStopway(selectedRunway);

        int runwayLengthMeters = Calculator.getTORA(selectedRunway);
        // Length of the runway in pixels.

        double runwayLengthPixels = topDownRunway.getFitWidth();

        // pixels per meter

        double ratio = runwayLengthPixels / runwayLengthMeters;

        double pixels = ratio * widthMeters;

        //set the stop pixel width, for both ends.
        this.topViewStopwayStart.setWidth(pixels);
        this.topViewStopwayEnd.setWidth(pixels);

        //translate them appropriately.

        this.topViewStopwayEnd.setTranslateX(0);
        this.topViewStopwayStart.setTranslateX(0);
        this.topViewClearway.setTranslateX(0);


        double displaceAmountstart = -runwayLengthPixels/2 - pixels/2;
        double displaceAmountend = runwayLengthPixels/2 + pixels/2;

        this.topViewStopwayStart.setTranslateX(displaceAmountstart);
        this.topViewStopwayStart.setVisible(true);

        this.topViewStopwayEnd.setTranslateX(displaceAmountend);
        this.topViewStopwayEnd.setVisible(true);

    }
    //initialise top view.
    @FXML
    void initialiseTopView() {
        this.obstacleImage.setVisible(false);
        this.displacedThreshold.setVisible(false);
        this.topViewStopwayEnd.setVisible(false);
        this.topViewStopwayStart.setVisible(false);
        this.topViewClearway.setVisible(false);
    }
    @FXML
    void updateDisplacedThresholdDisplayGoingRight() {
        int widthMeters = Calculator.getDisplacedThreshold(selectedRunway);

        int runwayLengthMeters = Calculator.getTORA(selectedRunway);
        // Length of the runway in pixels.

        double runwayLengthPixels = topDownRunway.getFitWidth();

        // pixels per meter

        double ratio = runwayLengthPixels / runwayLengthMeters;

        double pixels = ratio * widthMeters;

        this.displacedThreshold.setWidth(pixels);


        this.displacedThreshold.setTranslateX(0);

        double displaceAmount = -runwayLengthPixels/2 + pixels/2;
        this.displacedThreshold.setTranslateX(displaceAmount);
        this.displacedThreshold.setVisible(true);

    }
    @FXML
    void updateDisplacedThresholdDisplayGoingLeft() {
        int widthMeters = Calculator.getDisplacedThreshold(selectedRunway);

        int runwayLengthMeters = Calculator.getTORA(selectedRunway);
        // Length of the runway in pixels.

        double runwayLengthPixels = topDownRunway.getFitWidth();

        // pixels per meter

        double ratio = runwayLengthPixels / runwayLengthMeters;

        double pixels = ratio * widthMeters;

        this.displacedThreshold.setWidth(pixels);


        this.displacedThreshold.setTranslateX(0);

        double displaceAmount = -runwayLengthPixels/2 + pixels/2;
        //negative displace amount because it is going left.
        this.displacedThreshold.setTranslateX(-displaceAmount);
        this.displacedThreshold.setVisible(true);

    }
    public void positionObstacleTopDown() {
        if (currentObstacle != null) {
            boolean goingLeft = goingLeftBox.isSelected();
            boolean goingRight = !goingLeft;
            obstacleImage.setTranslateX(0);

            int runwayLengthMeters = Calculator.getTORA(selectedRunway);
            // Length of the runway in pixels.

            double runwayLengthPixels = topDownRunway.getFitWidth();

            // pixels per meter

            double ratio = runwayLengthPixels / runwayLengthMeters;


            double yfromcentreInmetres = Calculator.getObstacleDistanceFromCentreline(currentObstacle);
            double metresfromthreshold = Calculator.getObstacleDistanceFromThreshold(currentObstacle);

            //total displaced metres
            double totalxdisplacedmetres = -runwayLengthMeters/2 + metresfromthreshold + Calculator.getDisplacedThreshold(selectedRunway);

            double totalypixels = topDownBackground.getFitHeight()/2  * yfromcentreInmetres/150;
            double totalxpixels = ratio * totalxdisplacedmetres;
            if (goingRight) {
                obstacleImage.setTranslateX(totalxpixels);
            }
            else {
                obstacleImage.setTranslateX(-totalxpixels);
            }
            obstacleImage.setTranslateY(-totalypixels);
            obstacleImage.setVisible(true);

        }
        else {
            return;
        }
    }
    double getDisplacedThresholdOffsetPixels() {
        int runwayLengthMeters = Calculator.getTORA(selectedRunway);
        // Length of the runway in pixels.

        double runwayLengthPixels = topDownRunway.getFitWidth();

        double offset = runwayLengthPixels / 2 + displacedThreshold.getTranslateX() + displacedThreshold.getWidth()/2;
        return offset;
    }
    private Arrow addArrowTopViewGoingRight(double metersX, double pixelsY, boolean doubleArrow, String text, boolean isLDA,boolean isDirection) {

        // Length of the runway in meters.
        int runwayLengthMeters = Calculator.getTORA(selectedRunway);
        // Length of the runway in pixels.

        double runwayLengthPixels = topDownRunway.getFitWidth();

        // pixels per meter

        double ratio = runwayLengthPixels / runwayLengthMeters;

        // Length of the arrow and displacement in pixels.
        double displaceLengthPixels;
        double arrowLengthPixels;

        //LDA must be shifted relative to displaced threshold.
        if (isLDA) {

            displaceLengthPixels = this.displacedThreshold.getWidth();
            arrowLengthPixels = metersX * ratio;

        }
        else if (isDirection) {
            arrowLengthPixels = metersX * ratio;
            displaceLengthPixels = this.topDownRunway.getFitWidth()/8;
        }

        else {
            displaceLengthPixels = 0;
            arrowLengthPixels = metersX * ratio;
        }


        Arrow arrow = new Arrow(0, 0, arrowLengthPixels, 0, doubleArrow);

        // Create label with desired text
        Label txt;
        if (!isDirection) {
            txt = new Label(String.format("%1$s %2$dm", text, Math.round(metersX)));
            txt.setBackground(new Background((new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))));
        }
        else {
            txt = new Label(String.format("%1$s", text));
            txt.setBackground(new Background((new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))));
        }

        // Move the arrow and text half a runway length to the left to start from the left of the runway
        // and add the displacement to this
        arrow.setTranslateX(displaceLengthPixels + (arrowLengthPixels - runwayLengthPixels) / 2);
        txt.setTranslateX(displaceLengthPixels + (arrowLengthPixels - runwayLengthPixels) / 2);

        // Move the arrow and text pixelsY pixels down.
        arrow.setTranslateY(pixelsY);
        txt.setTranslateY(pixelsY -12);

        // Add the arrow and text to the side view stack pane.
        topDownStackPane.getChildren().add(arrow);
        topDownStackPane.getChildren().add(txt);

        return arrow;

    }
    private Arrow addArrowTopViewGoingLeft(double metersX, double pixelsY, boolean doubleArrow, String text, boolean isLDA,boolean isDirection) {

        // Length of the runway in meters.
        int runwayLengthMeters = Calculator.getTORA(selectedRunway);
        // Length of the runway in pixels.

        double runwayLengthPixels = topDownRunway.getFitWidth();

        // pixels per meter

        double ratio = runwayLengthPixels / runwayLengthMeters;

        // Length of the arrow and displacement in pixels.
        double displaceLengthPixels;
        double arrowLengthPixels;

        if (isLDA) {
            //additional offset.
            displaceLengthPixels = this.displacedThreshold.getWidth();
            arrowLengthPixels = metersX * ratio;

        }
        else if (isDirection) {
            arrowLengthPixels = metersX * ratio;
            displaceLengthPixels = this.topDownRunway.getFitWidth()/8;
        }

        else {
            displaceLengthPixels = 0;
            arrowLengthPixels = metersX * ratio;
        }

        Arrow arrow = new Arrow(runwayLengthPixels, 0, runwayLengthPixels - arrowLengthPixels, 0, doubleArrow);

        Label txt;
        if (!isDirection) {
            txt = new Label(String.format("%1$s %2$dm", text, Math.round(metersX)));
            txt.setBackground(new Background((new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))));
        }
        else {
            txt = new Label(String.format("%1$s", text));
            txt.setBackground(new Background((new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))));
        }

        // Move the arrow and text half a runway length to the left to start from the left of the runway
        // and add the displacement to this

        //negative of displaced for going right.
        arrow.setTranslateX(-(displaceLengthPixels + (arrowLengthPixels - runwayLengthPixels) / 2));
        txt.setTranslateX(-(displaceLengthPixels + (arrowLengthPixels - runwayLengthPixels) / 2));

        // Move the arrow and text pixelsY pixels down.
        arrow.setTranslateY(pixelsY);
        txt.setTranslateY(pixelsY -12);

        // Add the arrow and text to the side view stack pane.
        topDownStackPane.getChildren().add(arrow);
        topDownStackPane.getChildren().add(txt);

        return arrow;

    }
    private void refreshTopView() {
        //update the displaced threshold on the display

        //boolean value determines flight direction.
        boolean goingLeft = goingLeftBox.isSelected();
        boolean goingRight = !goingLeft;
        double runwayLengthPixels = topDownBackground.getFitWidth();

        //position obstacle
        positionObstacleTopDown();
        //either update the components for going right or going left.
        if (goingRight) {
            updateDisplacedThresholdDisplayGoingRight();
            updateStopWayTopView();
            updateClearwayTopViewGoingRight();
        }

        else {
            updateDisplacedThresholdDisplayGoingLeft();
            updateStopWayTopView();
            updateClearwayTopViewGoingLeft();

        }

        ImageView runway = this.topDownRunway;
        ImageView backg = this.topDownBackground;
        ImageView obst = this.obstacleImage;
        Rectangle DT = this.displacedThreshold;
        Rectangle stpwystart = this.topViewStopwayStart;
        Rectangle clwy = this.topViewClearway;
        Rectangle stpwyend = this.topViewStopwayEnd;


        topDownStackPane.getChildren().clear();
        //add the components back on.
        topDownStackPane.getChildren().add(backg);
        topDownStackPane.getChildren().add(runway);
        topDownStackPane.getChildren().add(DT);
        topDownStackPane.getChildren().add(clwy);
        topDownStackPane.getChildren().add(stpwystart);
        topDownStackPane.getChildren().add(stpwyend);
        topDownStackPane.getChildren().add(headingNumL);
        topDownStackPane.getChildren().add(headingNumR);
        topDownStackPane.getChildren().add(headingPosL);
        topDownStackPane.getChildren().add(headingPosR);



        if (obst != null) {

            topDownStackPane.getChildren().add(obst);
        }

        //get the variables from model
        int TODA,ASDA,TORA,LDA;
        TODA = Calculator.getUpdatedTODA(selectedRunway);
        ASDA= Calculator.getUpdatedASDA(selectedRunway);
        TORA = Calculator.getUpdatedTORA(selectedRunway);
        LDA = Calculator.getUpdatedLDA(selectedRunway);


        // add arrows according to going right or going left.
        if (goingRight) {
            //direction arrow
            addArrowTopViewGoingRight(runwayLengthPixels / 2, runwayLengthPixels / 8, false, "Direction Of Flight", false,true);

            addArrowTopViewGoingRight(TODA, -200, true, "TODA", false,false);
            addArrowTopViewGoingRight(ASDA, -150, true, "ASDA", false,false);
            addArrowTopViewGoingRight(TORA, -100, true, "TORA", false,false);
            addArrowTopViewGoingRight(LDA, -50, true, "LDA", true,false);
        }
        else {
            addArrowTopViewGoingLeft(runwayLengthPixels / 2, runwayLengthPixels / 8, false,
                    "Direction Of Flight", false,true);
            addArrowTopViewGoingLeft(TODA, -200, true, "TODA", false,false);
            addArrowTopViewGoingLeft(ASDA, -150, true, "ASDA", false,false);
            addArrowTopViewGoingLeft(TORA, -100, true, "TORA", false,false);
            addArrowTopViewGoingLeft(LDA, -50, true, "LDA", true,false);
        }

        String runwayName = Calculator.getRunwayName(selectedRunway);
        int runwayNumber = Integer.valueOf(runwayName.substring(0,2));
        String runwayPosition = runwayName.substring(2);

        String otherRunwayPosition = runwayName.substring(2);
        if (runwayPosition.equals("R")){
            otherRunwayPosition = "L";
        } else {
            otherRunwayPosition = "R";
        }

        // Print runway numbers on screen
        if (runwayNumber <= 18) {
            if (runwayNumber <= 9) {
                headingNumL.setText("0" + String.valueOf(runwayNumber));
            } else {
                headingNumL.setText(String.valueOf(runwayNumber));
            }
            headingPosL.setText(String.valueOf(runwayPosition));
            headingNumR.setText(String.valueOf(runwayNumber + 18));
            headingPosR.setText(String.valueOf(otherRunwayPosition));
        }
        else {
            if (runwayNumber >= 28) {
                headingNumL.setText(String.valueOf(runwayNumber - 18));
                headingPosL.setText(String.valueOf(otherRunwayPosition));
            }
            else {
                headingNumL.setText("0" + String.valueOf(runwayNumber - 18));
                headingPosL.setText(String.valueOf(otherRunwayPosition));
            }
            headingNumR.setText(String.valueOf(runwayNumber));
            headingPosR.setText(String.valueOf(runwayPosition));
        }
    }


    // SIDE VIEW TAB METHODS
    private Arrow addArrowToSideViewLeft(int pixelsX, int displacePixelsX, double pixelsY, boolean doubleArrow, String text) {
        // Length of the runway in pixels.
        double runwayLengthPixels = sideViewRunwayImage.getFitWidth();

        // Length of the arrow and displacement in pixels.
        double arrowLengthPixels = pixelsX;
        double displaceLengthPixels = displacePixelsX;

        Arrow arrow = new Arrow(0, 0, arrowLengthPixels, 0, doubleArrow);

        // Create label with desired text
        Label txt = new Label(text);
        txt.setBackground(new Background((new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))));

        // Move the arrow and text half a runway length to the left to start from the left of the runway
        // and add the displacement to this
        arrow.setTranslateX(displaceLengthPixels + (arrowLengthPixels - runwayLengthPixels) / 2);
        txt.setTranslateX(displaceLengthPixels + (arrowLengthPixels - runwayLengthPixels) / 2);

        // Move the arrow and text pixelsY pixels down.
        arrow.setTranslateY(pixelsY);
        txt.setTranslateY(pixelsY + 15);

        // Add the arrow and text to the side view stack pane.
        sideViewStackPane.getChildren().add(arrow);
        sideViewStackPane.getChildren().add(txt);

        return arrow;
    }
    private Arrow addArrowToSideViewRight(int pixelsX, int displacePixelsX, int pixelsY, boolean doubleArrow, String text) {
        // Length of the runway in pixels.
        double runwayLengthPixels = sideViewRunwayImage.getFitWidth();

        // Length of the arrow and displacement in pixels.
        double arrowLengthPixels = pixelsX;
        double displaceLengthPixels = displacePixelsX;

        Arrow arrow = new Arrow(runwayLengthPixels, 0, runwayLengthPixels - arrowLengthPixels, 0, doubleArrow);

        // Create label with desired text
        Label txt = new Label(text);
        txt.setBackground(new Background((new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))));

        // Move the arrow and text half a runway length to the left to start from the left of the runway
        // and add the displacement to this.
        arrow.setTranslateX(-(displaceLengthPixels + (arrowLengthPixels - runwayLengthPixels) / 2));
        txt.setTranslateX(-(displaceLengthPixels + (arrowLengthPixels - runwayLengthPixels) / 2));

        // Move the arrow and text pixelsY pixels down.
        arrow.setTranslateY(pixelsY);
        txt.setTranslateY(pixelsY + 15);

        // Add the arrow and text to the side view stack pane.
        sideViewStackPane.getChildren().add(arrow);
        sideViewStackPane.getChildren().add(txt);

        return arrow;
    }
    private Arrow addArrowToSideViewByMetersLeft(double metersX, double displaceMetersX, double pixelsY, boolean doubleArrow, String text) {
        // Length of the runway in meters.
        int runwayLengthMeters = Calculator.getTORA(selectedRunway);
        // Length of the runway in pixels.
        double runwayLengthPixels = sideViewRunwayImage.getFitWidth();

        // Ratio of pixels:meters.
        double ratio = runwayLengthPixels / runwayLengthMeters;

        // Length of the arrow and displacement in pixels.
        double arrowLengthPixels = metersX * ratio;
        double displaceLengthPixels = displaceMetersX * ratio;

        Arrow arrow = new Arrow(0, 0, arrowLengthPixels, 0, doubleArrow);

        // Create label with desired text
        Label txt = new Label(String.format("%1$s %2$dm", text, Math.round(metersX)));
        txt.setBackground(new Background((new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))));

        // Move the arrow and text half a runway length to the left to start from the left of the runway
        // and add the displacement to this
        arrow.setTranslateX(displaceLengthPixels + (arrowLengthPixels - runwayLengthPixels) / 2);
        txt.setTranslateX(displaceLengthPixels + (arrowLengthPixels - runwayLengthPixels) / 2);

        // Move the arrow and text pixelsY pixels down.
        arrow.setTranslateY(pixelsY);
        txt.setTranslateY(pixelsY + 15);

        // Add the arrow and text to the side view stack pane.
        sideViewStackPane.getChildren().add(arrow);
        sideViewStackPane.getChildren().add(txt);

        return arrow;
    }
    private Arrow addArrowToSideViewByMetersRight(double metersX, double displaceMetersX, double pixelsY, boolean doubleArrow, String text) {
        // Length of the runway in meters.
        int runwayLengthMeters = Calculator.getTORA(selectedRunway);
        // Length of the runway in pixels.
        double runwayLengthPixels = sideViewRunwayImage.getFitWidth();

        // Ratio of pixels:meters.
        double ratio = runwayLengthPixels / runwayLengthMeters;

        // Length of the arrow and displacement in pixels.
        double arrowLengthPixels = metersX * ratio;
        double displaceLengthPixels = displaceMetersX * ratio;

        Arrow arrow = new Arrow(runwayLengthPixels, 0, runwayLengthPixels - arrowLengthPixels, 0, doubleArrow);

        // Create label with desired text
        Label txt = new Label(String.format("%1$s %2$dm", text, Math.round(metersX)));
        txt.setBackground(new Background((new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY))));

        // Move the arrow and text half a runway length to the left to start from the left of the runway
        // and add the displacement to this
        arrow.setTranslateX(-(displaceLengthPixels + (arrowLengthPixels - runwayLengthPixels) / 2));
        txt.setTranslateX(-(displaceLengthPixels + (arrowLengthPixels - runwayLengthPixels) / 2));

        // Move the arrow and text pixelsY pixels down.
        arrow.setTranslateY(pixelsY);
        txt.setTranslateY(pixelsY + 15);

        // Add the arrow and text to the side view stack pane.
        sideViewStackPane.getChildren().add(arrow);
        sideViewStackPane.getChildren().add(txt);

        return arrow;
    }
    private Arrow addAngledArrowToSideViewByMetersLeft(double metersX, double startY, double displaceMetersX, double pixelsY) {
        // Length of the runway in meters.
        int runwayLengthMeters = Calculator.getTORA(selectedRunway);
        // Length of the runway in pixels.
        double runwayLengthPixels = sideViewRunwayImage.getFitWidth();

        // Ratio of pixels:meters.
        double ratio = runwayLengthPixels / runwayLengthMeters;

        // Length of the arrow and displacement in pixels.
        double arrowLengthPixels = metersX * ratio;
        double displaceLengthPixels = displaceMetersX * ratio;

        Arrow arrow = new Arrow(0, 0, arrowLengthPixels, startY, 0);

        // Move the arrow and text half a runway length to the left to start from the left of the runway
        // and add the displacement to this
        arrow.setTranslateX(displaceLengthPixels + (arrowLengthPixels - runwayLengthPixels) / 2);

        // Move the arrow and text pixelsY pixels down.
        arrow.setTranslateY(pixelsY);

        // Add the arrow and text to the side view stack pane.
        sideViewStackPane.getChildren().add(arrow);

        return arrow;
    }
    private Arrow addAngledArrowToSideViewByMetersRight(double metersX, double endY, double displaceMetersX, double pixelsY) {
        // Length of the runway in meters.
        int runwayLengthMeters = Calculator.getTORA(selectedRunway);
        // Length of the runway in pixels.
        double runwayLengthPixels = sideViewRunwayImage.getFitWidth();

        // Ratio of pixels:meters.
        double ratio = runwayLengthPixels / runwayLengthMeters;

        // Length of the arrow and displacement in pixels.
        double arrowLengthPixels = metersX * ratio;
        double displaceLengthPixels = displaceMetersX * ratio;

        Arrow arrow = new Arrow(runwayLengthPixels, 0, runwayLengthPixels - arrowLengthPixels, endY, 0);

        // Move the arrow and text half a runway length to the left to start from the left of the runway
        // and add the displacement to this
        arrow.setTranslateX(-(displaceLengthPixels + (arrowLengthPixels - runwayLengthPixels) / 2));

        // Move the arrow and text pixelsY pixels down.
        arrow.setTranslateY(pixelsY);

        // Add the arrow and text to the side view stack pane.
        sideViewStackPane.getChildren().add(arrow);

        return arrow;
    }
    // pixelsY is the amount of pixels to move the shape upwards. This is from the center as we're centering everything
    // on a stackpane, so you'd usually give this half the height of your shape
    private Shape addShapeToSideViewByMetersLeft(Shape shape, double metersX, double pixelsY){
        // Length of the runway in meters.
        int runwayLengthMeters = Calculator.getTORA(selectedRunway);
        // Length of the runway in pixels.
        double runwayLengthPixels = sideViewRunwayImage.getFitWidth();

        // Ratio of pixels:meters.
        double ratio = runwayLengthPixels / (double) runwayLengthMeters;

        // Amount of pixels to move the shape to the right.
        double pixelsX = metersX * ratio;

        return addShapeToSideViewByPixelsLeft(shape, pixelsX, pixelsY);
    }
    private Shape addShapeToSideViewByPixelsLeft(Shape shape, double pixelsX, double pixelsY) {
        // Length of the runway in pixels.
        double runwayLengthPixels = sideViewRunwayImage.getFitWidth();
        // Move the shape pixelsX pixels right.
        shape.setTranslateX(pixelsX - runwayLengthPixels / 2.0);
        // Move the shape pixelsY pixels up.
        shape.setTranslateY(-pixelsY);

        // Add the shape to the side view stack pane.
        sideViewStackPane.getChildren().add(shape);

        return shape;
    }
    // Places a shape on the surface of the runway.
    private Shape addShapeOnSideViewRunwayByMetersLeft(Shape shape, double metersX){
        // Height of the runway in pixels.
        double runwayHeightPixels = sideViewRunwayImage.prefHeight(-1);
        // Height of shape in pixels.
        double shapeHeightPixels = shape.prefHeight(-1);

        return addShapeToSideViewByMetersLeft(shape, metersX, (runwayHeightPixels + shapeHeightPixels) / 2.0);
    }
    // Assumes image's x coordinate starts at the left side of runway and finishes at metersX.
    private ImageView addImageToSideViewByMetersLeft(ImageView imageView, double metersX, double pixelsY) {
        // Length of the runway in meters.
        int runwayLengthMeters = Calculator.getTORA(selectedRunway);
        // Length of the runway in pixels.
        double runwayLengthPixels = sideViewRunwayImage.getFitWidth();

        // Ratio of pixels:meters.
        double ratio = runwayLengthPixels / runwayLengthMeters;

        // Amount of pixels to move the image to the right.
        double pixelsX = metersX * ratio;

        // Move the image pixelsX pixels right.
        imageView.setTranslateX(pixelsX - runwayLengthPixels / 2);
        // Move the image pixelsY pixels up.
        imageView.setTranslateY(-pixelsY);

        // Add the image to the side view stack pane.
        sideViewStackPane.getChildren().add(imageView);

        return imageView;
    }
    // Add an image on the runway moved metersX meters to the right (counting from the left).
    private ImageView addImageToSideViewByMetersLeft(ImageView imageView, double metersX) {
        // Height of the runway in pixels.
        double runwayHeightPixels = sideViewRunwayImage.prefHeight(-1);
        // Height of image in pixels.
        double imageHeightPixels = imageView.prefHeight(-1);

        return addImageToSideViewByMetersLeft(imageView, metersX, (runwayHeightPixels + imageHeightPixels) / 2);
    }

    private void refreshSideView(){
        // Clear the existing view.
        sideViewStackPane.getChildren().clear();

        double runwayHeightPixels = sideViewRunwayImage.getFitHeight();
        // Length of the runway in pixels.
        int runwayLengthPixels = (int) sideViewRunwayImage.getFitWidth();
        // Length of the runway in meters.
        int runwayLengthMeters = Calculator.getTORA(selectedRunway);
        // Ratio of pixels:meters.
        double ratio = (double) runwayLengthPixels / (double) runwayLengthMeters;
        int stopway = Calculator.getStopway(selectedRunway);
        int clearway = Calculator.getClearway(selectedRunway);
        double stopwayLengthPixels = ratio * stopway * 2 + runwayLengthPixels;
        double clearwayLengthPixels = ratio * clearway * 2 + runwayLengthPixels;
        // Draw stopway
        Rectangle sideViewStopwayRectangle = new Rectangle(
                stopwayLengthPixels, runwayHeightPixels, Paint.valueOf("pink"));
        // Draw clearway
        Rectangle sideViewClearwayRectangle = new Rectangle(
                clearwayLengthPixels, runwayHeightPixels, Paint.valueOf("lightblue"));

        // Add the clearway.
        sideViewStackPane.getChildren().add(sideViewClearwayRectangle);
        // Add the stopway.
        sideViewStackPane.getChildren().add(sideViewStopwayRectangle);
        // Add the runway.
        sideViewStackPane.getChildren().add(sideViewRunwayImage);

        String runwayName = Calculator.getRunwayName(selectedRunway);
        int runwayNumber = Integer.valueOf(runwayName.substring(0,2));
        String runwayPosition = runwayName.substring(2);

        String otherRunwayPosition = runwayName.substring(2);
        if (runwayPosition.equals("R")){
            otherRunwayPosition = "L";
        } else {
            otherRunwayPosition = "R";
        }

        // Print runway numbers on screen
        if (runwayNumber <= 18) {
            addShapeToSideViewByMetersLeft(new Text("0" + String.valueOf(runwayNumber)), 0, 150);
            addShapeToSideViewByMetersLeft(new Text(String.valueOf(runwayPosition)), 0, 140);
            addShapeToSideViewByMetersLeft(new Text(String.valueOf(runwayNumber + 18)), Calculator.getTORA(selectedRunway), 150);
            addShapeToSideViewByMetersLeft(new Text(String.valueOf(otherRunwayPosition)), Calculator.getTORA(selectedRunway), 140);
        }
        else {
            if (runwayNumber >= 28) {
                addShapeToSideViewByMetersLeft(new Text(String.valueOf(runwayNumber - 18)), 0, 150);
                addShapeToSideViewByMetersLeft(new Text(String.valueOf(otherRunwayPosition)), 0, 140);
            }
            else {
                addShapeToSideViewByMetersLeft(new Text("0" + String.valueOf(runwayNumber - 18)), 0, 150);
                addShapeToSideViewByMetersLeft(new Text(String.valueOf(otherRunwayPosition)), 0, 140);
            }
            addShapeToSideViewByMetersLeft(new Text(String.valueOf(runwayNumber)), Calculator.getTORA(selectedRunway), 150);
            addShapeToSideViewByMetersLeft(new Text(String.valueOf(runwayPosition)), Calculator.getTORA(selectedRunway), 140);
        }


        // Get original redeclared parameters.
        int originalTORA = Calculator.getTORA(selectedRunway);

        // Get updated redeclared parameters.
        int TORA = Calculator.getUpdatedTORA(selectedRunway);
        int TODA = Calculator.getUpdatedTODA(selectedRunway);
        int ASDA = Calculator.getUpdatedASDA(selectedRunway);
        int LDA = Calculator.getUpdatedLDA(selectedRunway);

        // These measurements don't get updated.
        int ALS = Calculator.getALS(selectedRunway);
        int TOCS = Calculator.getTOCS(selectedRunway);
        int RESA = Calculator.getRESA(selectedRunway);
        int stripEnd = Calculator.getStripEnd(selectedRunway);
        int DT = Calculator.getDisplacedThreshold(selectedRunway);
        int DFT = Calculator.getObstacleDistanceFromThreshold(selectedRunway);
        // Distance of the obstacle from the start of the runway. Assumes that the threshold is always given from the
        // left end of the runway.
        int obstacleDistance = Calculator.getObstacleDistanceFromThreshold(selectedRunway) + Calculator.getDisplacedThreshold(selectedRunway);
        int obstacleHeight = Calculator.getObstacleHeight(currentObstacle);
        int blastProtection = Calculator.getPlaneBlastProtection(Calculator.getPlaneOfRunway(selectedRunway));

        String selectedAction = (String) actionChoiceBox.getSelectionModel().getSelectedItem();

        // Add the obstacle relative to the runway.
        addShapeOnSideViewRunwayByMetersLeft(new Rectangle(5,20), obstacleDistance);

        // Is the plane going left? This corresponds to whether the "Switch Direction" box is ticked.
        boolean goingLeft = goingLeftBox.isSelected();
        boolean goingRight = !goingLeft;

        // Flight direction arrow
        if (goingLeft) {
            addArrowToSideViewRight(runwayLengthPixels / 4, runwayLengthPixels / 8, -100,
                    false, "Direction of flight");
        }
        else {
            addArrowToSideViewLeft(runwayLengthPixels / 4, runwayLengthPixels / 8, -100,
                    false, "Direction of flight");
        }

        // Is the obstacle on the left-hand side?
        boolean left = (double) obstacleDistance <= 0.5 * (double) originalTORA;
        boolean right = !left;

        // Calculate the ALS and TOCS as they are not set right in the backend
        double realALS = obstacleHeight * 50;
        double realTOCS = obstacleHeight * 50;

        // Recurring max value of ALS or RESA
        double maxALSRESA = Math.max(realALS, RESA);

        // Draw arrows.
        // Landing away / over - going left
        if (right && goingLeft && selectedAction.contains("Landing")) {
            if (maxALSRESA > blastProtection) {
                if (realALS > RESA) {
                    // ALS arrow
                    addArrowToSideViewByMetersRight(realALS, originalTORA - obstacleDistance,
                            20, true, "H * 50");
                    // RESA arrow
                    addArrowToSideViewByMetersRight(RESA, originalTORA - obstacleDistance,
                            50, true, "RESA");
                    // Blast protection arrow
                    addArrowToSideViewByMetersRight(blastProtection, originalTORA - obstacleDistance,
                            80, true, "Blast protection");
                    // Strip end arrow
                    addArrowToSideViewByMetersRight(stripEnd, originalTORA - obstacleDistance + realALS,
                            110, true, "Strip end");
                    // LDA arrow
                    addArrowToSideViewByMetersRight(LDA, originalTORA - obstacleDistance + realALS + stripEnd,
                            140, true, "LDA");
                    // Angle from top of obstacle to start of LDA
                    addAngledArrowToSideViewByMetersRight(realALS + stripEnd, 20, originalTORA - obstacleDistance,
                            -17);
                }
                else {
                    // ALS arrow
                    addArrowToSideViewByMetersRight(realALS, originalTORA - obstacleDistance,
                            50, true, "H * 50");
                    // RESA arrow
                    addArrowToSideViewByMetersRight(RESA, originalTORA - obstacleDistance,
                            20, true, "RESA");
                    // Blast protection arrow
                    addArrowToSideViewByMetersRight(blastProtection, originalTORA - obstacleDistance,
                            80, true, "Blast protection");
                    // Strip end arrow
                    addArrowToSideViewByMetersRight(stripEnd, originalTORA - obstacleDistance + RESA,
                            110, true, "Strip end");
                    // LDA arrow
                    addArrowToSideViewByMetersRight(LDA, originalTORA - obstacleDistance + RESA + stripEnd,
                            140, true, "LDA");
                    // Angle from top of obstacle to start of LDA
                    addAngledArrowToSideViewByMetersRight(RESA + stripEnd, 20, originalTORA - obstacleDistance,
                            -17);
                }
            }
            else {
                // ALS arrow
                addArrowToSideViewByMetersRight(realALS, originalTORA - obstacleDistance,
                        50, true, "H * 50");
                // RESA arrow
                addArrowToSideViewByMetersRight(RESA, originalTORA - obstacleDistance,
                        80, true, "RESA");
                // Blast protection arrow
                addArrowToSideViewByMetersRight(blastProtection, originalTORA - obstacleDistance,
                        20, true, "Blast protection");
                // Strip end arrow
                addArrowToSideViewByMetersRight(stripEnd, originalTORA - obstacleDistance + maxALSRESA,
                        110, true, "Strip end");
                // LDA arrow
                addArrowToSideViewByMetersRight(LDA, originalTORA - obstacleDistance + blastProtection,
                        140, true, "LDA");
                // Angle from top of obstacle to start of LDA
                addAngledArrowToSideViewByMetersRight(blastProtection, 20, originalTORA - obstacleDistance,
                        -17);
            }
        }
        // Landing away / over - going right
        else if (left && goingRight && selectedAction.contains("Landing")) {
            // For landing decision making - ((ALS|RESA) + SE) | BP
            if (maxALSRESA > blastProtection) {
                if (realALS > RESA) {
                    // ALS arrow
                    addArrowToSideViewByMetersLeft(realALS, obstacleDistance,
                            20, true, "H * 50");
                    // RESA arrow
                    addArrowToSideViewByMetersLeft(RESA, obstacleDistance,
                            50, true, "RESA");
                    // Blast protection arrow
                    addArrowToSideViewByMetersLeft(blastProtection, obstacleDistance, 80, true,
                            "Blast protection");
                    // Strip end arrow
                    addArrowToSideViewByMetersLeft(stripEnd, obstacleDistance + realALS,
                            110, true, "Strip end");
                    // LDA arrow
                    addArrowToSideViewByMetersLeft(LDA, obstacleDistance + realALS + stripEnd,
                            140, true, "LDA");
                    // Angle from top of obstacle to start of LDA
                    addAngledArrowToSideViewByMetersLeft(realALS + stripEnd, 20, obstacleDistance,
                            -17);
                }
                else {
                    // ALS arrow
                    addArrowToSideViewByMetersLeft(realALS, obstacleDistance,
                            50, true, "H * 50");
                    // RESA arrow
                    addArrowToSideViewByMetersLeft(RESA, obstacleDistance,
                            20, true, "RESA");
                    // Blast protection arrow
                    addArrowToSideViewByMetersLeft(blastProtection, obstacleDistance, 80, true,
                            "Blast protection");
                    // Strip end arrow
                    addArrowToSideViewByMetersLeft(stripEnd, obstacleDistance + RESA,
                            110, true, "Strip end");
                    // LDA arrow
                    addArrowToSideViewByMetersLeft(LDA, obstacleDistance + RESA + stripEnd,
                            140, true, "LDA");
                    // Angle from top of obstacle to start of LDA
                    addAngledArrowToSideViewByMetersLeft(RESA + stripEnd, 20, obstacleDistance,
                            -17);
                }
            }
            else {
                // ALS arrow
                addArrowToSideViewByMetersLeft(realALS, obstacleDistance,
                        50, true, "H * 50");
                // RESA arrow
                addArrowToSideViewByMetersLeft(RESA, obstacleDistance,
                        80, true, "RESA");
                // Blast protection arrow
                addArrowToSideViewByMetersLeft(blastProtection, obstacleDistance, 20, true,
                        "Blast protection");
                // Strip end arrow
                addArrowToSideViewByMetersLeft(stripEnd, obstacleDistance + maxALSRESA,
                        110, true, "Strip end");
                // LDA arrow
                addArrowToSideViewByMetersLeft(LDA, obstacleDistance + blastProtection,
                        140, true, "LDA");
                // Angle from top of obstacle to start of LDA
                addAngledArrowToSideViewByMetersLeft(blastProtection, 20, obstacleDistance,
                        -17);
            }
        }

        // Landing towards - going left
        else if (left && goingLeft && selectedAction.contains("Landing")) {
            // LDA arrow
            addArrowToSideViewByMetersRight(LDA, 0, 20, true, "LDA");
            // Strip end arrow
            addArrowToSideViewByMetersRight(stripEnd, LDA, 50, true, "Strip end");
            // RESA arrow
            addArrowToSideViewByMetersRight(RESA, LDA + stripEnd, 80, true, "RESA");
        }
        // Landing towards - going right
        else if (right && goingRight && selectedAction.contains("Landing")) {
            // LDA arrow
            addArrowToSideViewByMetersLeft(LDA, DT, 20, true, "LDA");
            // Strip end arrow
            addArrowToSideViewByMetersLeft(stripEnd, LDA + DT, 50, true, "Strip end");
            // RESA arrow
            addArrowToSideViewByMetersLeft(RESA, LDA + stripEnd + DT, 80, true, "RESA");
        }

        // Taking off towards / over - going left
        else if (left && goingLeft && selectedAction.contains("Takeoff")) {
            if (realTOCS > RESA) {
                // TOCS arrow
                addArrowToSideViewByMetersLeft(realTOCS, obstacleDistance,20, true,
                        "H * 50");
                // RESA arrow
                addArrowToSideViewByMetersLeft(RESA, obstacleDistance,50, true,
                        "RESA");
                // Strip end arrow
                addArrowToSideViewByMetersLeft(stripEnd, obstacleDistance + realTOCS, 80,
                        true, "Strip end");
                // TORA-TODA-ASDA arrow
                addArrowToSideViewByMetersLeft(TORA, obstacleDistance + realTOCS + stripEnd, 110,
                        true, "TORA, TODA, ASDA");
                // Angle from top of obstacle to start of TORA
                addAngledArrowToSideViewByMetersLeft(realTOCS + stripEnd, 20, obstacleDistance,
                        -17);
            }
            else {
                // RESA arrow
                addArrowToSideViewByMetersLeft(RESA, obstacleDistance, 20, true, "RESA");
                // TOCS arrow
                addArrowToSideViewByMetersLeft(realTOCS, obstacleDistance,50, true,
                        "H * 50");
                // Strip end arrow
                addArrowToSideViewByMetersLeft(stripEnd, obstacleDistance + RESA, 80,
                        true, "Strip end");
                // TORA-TODA-ASDA arrow
                addArrowToSideViewByMetersLeft(TORA, obstacleDistance + RESA + stripEnd, 110,
                        true, "TORA, TODA, ASDA");
                // Angle from top of obstacle to start of TORA
                addAngledArrowToSideViewByMetersLeft(RESA + stripEnd, 20, obstacleDistance, -17);
            }
        }
        // Taking off towards / over - going right
        else if (right && goingRight && selectedAction.contains("Takeoff")) {
            if (realTOCS > RESA) {
                // TOCS arrow
                addArrowToSideViewByMetersRight(realALS, originalTORA - obstacleDistance,20, true,
                        "H * 50");
                // RESA arrow
                addArrowToSideViewByMetersRight(RESA, originalTORA - obstacleDistance,50, true,
                        "RESA");
                // Strip end arrow
                addArrowToSideViewByMetersRight(stripEnd, originalTORA - obstacleDistance + realTOCS, 80,
                        true, "Strip end");
                // TORA-TODA-ASDA arrow
                addArrowToSideViewByMetersRight(TORA, originalTORA - obstacleDistance + realTOCS + stripEnd, 110,
                        true, "TORA, TODA, ASDA");
                // Angle from top of obstacle to start of TORA
                addAngledArrowToSideViewByMetersRight(realTOCS + stripEnd, 20, originalTORA - obstacleDistance,
                        -17);
            }
            else {
                // RESA arrow
                addArrowToSideViewByMetersRight(RESA, originalTORA - obstacleDistance, 20, true, "RESA");
                // TOCS arrow
                addArrowToSideViewByMetersRight(realALS, originalTORA - obstacleDistance,50, true,
                        "H * 50");
                // Strip end arrow
                addArrowToSideViewByMetersRight(stripEnd, originalTORA - obstacleDistance + RESA, 80,
                        true, "Strip end");
                // TORA-TODA-ASDA arrow
                addArrowToSideViewByMetersRight(TORA, originalTORA - obstacleDistance + RESA + stripEnd, 110,
                        true, "TORA, TODA, ASDA");
                // Angle from top of obstacle to start of TORA
                addAngledArrowToSideViewByMetersRight(RESA + stripEnd, 20, originalTORA - obstacleDistance, -17);
            }
        }


        // Taking off away - going left
        else if (right && goingLeft && selectedAction.contains("Takeoff")) {
            // Blast protection arrow
            addArrowToSideViewByMetersRight(blastProtection, originalTORA - obstacleDistance,
                    20, true, "Blast protection");
            // TORA arrow
            addArrowToSideViewByMetersRight(TORA, originalTORA - obstacleDistance + blastProtection,
                    50, true, "TORA");
            // TODA arrow
            addArrowToSideViewByMetersRight(TODA, originalTORA - obstacleDistance + blastProtection,
                    80, true, "TODA");
            // ASDA arrow
            addArrowToSideViewByMetersRight(ASDA, originalTORA - obstacleDistance + blastProtection,
                    110, true, "ASDA");
        }
        // Taking off away - going right
        else if (left && goingRight && selectedAction.contains("Takeoff")) {
            // Blast protection arrow
            addArrowToSideViewByMetersLeft(blastProtection, obstacleDistance,
                    20, true, "Blast protection");
            // TORA arrow
            addArrowToSideViewByMetersLeft(TORA, obstacleDistance + blastProtection,
                    50, true, "TORA");
            // TODA arrow
            addArrowToSideViewByMetersLeft(TODA, obstacleDistance + blastProtection,
                    80, true, "TODA");
            // TORA arrow
            addArrowToSideViewByMetersLeft(ASDA, obstacleDistance + blastProtection,
                    110, true, "ASDA");
        }
    }


    // MISC HELPER METHODS
    // Constructs an image view from a file name.
    private ImageView getImageViewOfFileName(String fileName) {
        InputStream fileStream = this.getClass().getResourceAsStream(fileName);
        Image image = new Image(fileStream);
        return new ImageView(image);
    }

    // Show an error alert containing the input string.
    private void showAlert(String text) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setHeaderText(text);
        errorAlert.show();
        updateNotificationLog("ERROR: " + text);
    }
}
