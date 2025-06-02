package org.example.multithreading;

import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.geometry.Insets;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Objects;


public class Controller {

    @FXML private ImageView originalView;
    @FXML private ImageView processedView;
    @FXML private ComboBox<String> operationBox;
    @FXML private Button saveBtn;
    @FXML private Button scaleBtn;
    @FXML private Button rotateLeftBtn;
    @FXML private Button rotateRightBtn;
    @FXML private ImageView logoView;

    private Image originalImage;
    private Image processedImage;

    @FXML
    public void initialize() {
        operationBox.getItems().addAll("Negative", "Thresholding", "Contouring");
        setControlState(false);

        try {
            Image logo = new Image(Objects.requireNonNull(ApplicationWindow.class.getResourceAsStream("pwrlogo.jpg")));
            logoView.setImage(logo);
        } catch (Exception e) {
            System.out.println("Error loading logo: " + e.getMessage());
        }
    }

    private void setControlState(boolean state) {
        saveBtn.setDisable(!state);
        scaleBtn.setDisable(!state);
        rotateLeftBtn.setDisable(!state);
        rotateRightBtn.setDisable(!state);
    }

    @FXML
    private void loadImage() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Load JPG Image");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JPG Files", "*.jpg"));
        File file = chooser.showOpenDialog(null);

        if (file != null && file.getName().toLowerCase().endsWith(".jpg")) {
            originalImage = new Image(file.toURI().toString());
            processedImage = null;
            originalView.setImage(originalImage);
            processedView.setImage(null);
            setControlState(true);
            showInfo("Image loaded successfully!");
        } else {
            showInfo("Invalid file format. Please select a .jpg file.");
        }
    }

    @FXML
    private void applyOperation() {
        String op = operationBox.getValue();
        if (op == null) {
            showInfo("Please select an operation.");
            return;
        }

        Image input = processedImage != null ? processedImage : originalImage;

        switch (op) {
            case "Negative" -> {
                processedImage = ImageProcessor.generateNegative(input);
                processedView.setImage(processedImage);
                showInfo("Negative generated.");
            }
            case "Thresholding" -> showThresholdDialog();
            case "Contouring" -> {
                processedImage = ImageProcessor.applyEdgeDetection(input);
                processedView.setImage(processedImage);
                showInfo("Edge detection applied.");
            }
        }
    }

    @FXML
    private void saveImage() {
        if (processedImage == null) {
            showInfo("No image to save.");
            return;
        }

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Save Image");
        dialog.setHeaderText("Enter a file name (3–100 characters):");
        dialog.setContentText("File name:");

        dialog.showAndWait().ifPresent(name -> {
            if (name.length() < 3 || name.length() > 100) {
                showInfo("File name must be between 3 and 100 characters.");
                return;
            }

            File outputDir = new File(System.getProperty("user.home") + "/Pictures");
            if (!outputDir.exists() && !outputDir.mkdirs()) {
                showInfo("Could not create output directory.");
                return;
            }

            File outputFile = new File(outputDir, name + ".jpg");
            if (outputFile.exists()) {
                showInfo("File already exists. Choose another name.");
                return;
            }

            try {
                BufferedImage raw = SwingFXUtils.fromFXImage(processedImage, null);
                BufferedImage rgb = new BufferedImage(raw.getWidth(), raw.getHeight(), BufferedImage.TYPE_INT_RGB);
                rgb.createGraphics().drawImage(raw, 0, 0, java.awt.Color.WHITE, null);

                if (ImageIO.write(rgb, "jpg", outputFile)) {
                    showInfo("Image saved to: " + outputFile.getAbsolutePath());
                } else {
                    showInfo("Image save failed.");
                }
            } catch (IOException e) {
                showInfo("Save error: " + e.getMessage());
            }
        });
    }

    @FXML
    private void rotateLeft() {
        Image input = processedImage != null ? processedImage : originalImage;
        processedImage = ImageProcessor.rotate(input, false);
        processedView.setImage(processedImage);
        showInfo("Rotated left.");
    }

    @FXML
    private void rotateRight() {
        Image input = processedImage != null ? processedImage : originalImage;
        processedImage = ImageProcessor.rotate(input, true);
        processedView.setImage(processedImage);
        showInfo("Rotated right.");
    }

    @FXML
    private void scaleImage() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Scale Image");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField widthField = new TextField();
        widthField.setPromptText("Width (max 3000)");

        TextField heightField = new TextField();
        heightField.setPromptText("Height (max 3000)");

        Button resetBtn = new Button("Reset to original");

        grid.add(new Label("Width:"), 0, 0);
        grid.add(widthField, 1, 0);
        grid.add(new Label("Height:"), 0, 1);
        grid.add(heightField, 1, 1);
        grid.add(resetBtn, 0, 2, 2, 1);

        resetBtn.setOnAction(a -> {
            processedImage = originalImage;
            processedView.setImage(originalImage);
            dialog.close();
        });

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(button -> {
            if (button == ButtonType.OK) {
                try {
                    int width = Integer.parseInt(widthField.getText());
                    int height = Integer.parseInt(heightField.getText());

                    if (width <= 0 || width > 3000 || height <= 0 || height > 3000) {
                        showInfo("Dimensions must be between 1 and 3000.");
                        return;
                    }

                    Image input = processedImage != null ? processedImage : originalImage;
                    processedImage = ImageProcessor.scaleImage(input, width, height);
                    processedView.setImage(processedImage);
                    showInfo("Image scaled.");
                } catch (NumberFormatException e) {
                    showInfo("Please enter valid integers.");
                }
            }
        });
    }

    private void showThresholdDialog() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Thresholding");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField thresholdField = new TextField();
        thresholdField.setPromptText("Value (0-255)");

        grid.add(new Label("Threshold:"), 0, 0);
        grid.add(thresholdField, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        dialog.showAndWait().ifPresent(button -> {
            if (button == ButtonType.OK) {
                try {
                    int value = Integer.parseInt(thresholdField.getText());
                    if (value < 0 || value > 255) {
                        showInfo("Threshold must be in range 0–255.");
                        return;
                    }

                    Image input = processedImage != null ? processedImage : originalImage;
                    processedImage = ImageProcessor.applyThreshold(input, value);
                    processedView.setImage(processedImage);
                    showInfo("Thresholding completed.");
                } catch (NumberFormatException e) {
                    showInfo("Enter a valid integer.");
                }
            }
        });
    }

    private void showInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
