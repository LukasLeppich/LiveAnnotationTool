package de.luk.ml.annotation.gui.components.recording;

import de.luk.ml.annotation.annotations.Annotation;
import de.luk.ml.annotation.annotations.AnnotationList;
import de.luk.ml.annotation.gui.components.common.ComponentController;
import de.luk.ml.annotation.gui.views.common.ViewController;
import de.luk.ml.annotation.recorder.AnnotationRecorder;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class TriggerAnnotationComponent extends ComponentController {
  private static Logger logger = LoggerFactory.getLogger(TriggerAnnotationComponent.class);
  private boolean isRunning;

  private static Paint NOT_STARTED = Color.web("#E0E0E0");
  private static Paint ACTIVE = Color.web("#76FF03");
  private static Paint INACTIVE = Color.web("#F1F8E9");

  private Annotation selectedAnnoation;
  private File outputFile;

  @FXML
  private ListView<Annotation> lstAnnotations;

  @FXML
  private Label lblActiveAnnotation;

  @FXML
  private BorderPane crcParent;
  @FXML
  private Circle crcIsActive;

  @FXML
  private Text txtHelp;

  @Inject
  private AnnotationList annotationList;

  @Inject
  private AnnotationRecorder recorder;


  @PostConstruct
  private void init() {
    this.lblActiveAnnotation.setText("Press ENTER key to start");
    this.lstAnnotations.setItems(this.annotationList.observable);
    this.crcIsActive.setFill(NOT_STARTED);
    this.crcIsActive.radiusProperty().bind(
        Bindings.min(
            crcParent.widthProperty().divide(2.5).subtract(12),
            crcParent.heightProperty().divide(2.5).subtract(12)
        ));
    this.setHelpText();
  }

  private void setHelpText() {
    this.txtHelp.setText("ENTER: Start recording\n" +
        "ESC: Stop recording\n" +
        "1-0: Select annotation (0 is 10)");
  }

  public void setOutputFile(File output) {
    this.outputFile = output;
  }

  @Override
  public void setView(ViewController view) {
    super.setView(view);
    this.view.root.scene.addEventFilter(MouseEvent.MOUSE_PRESSED, this::onMouseDown);
    this.view.root.scene.addEventFilter(MouseEvent.MOUSE_RELEASED, this::onMouseUp);
    this.view.root.scene.addEventFilter(KeyEvent.KEY_PRESSED, this::onKeyPressed);
  }

  public void start() {
    try {
      this.recorder.startRecording(this.outputFile);
    } catch (Throwable e) {
      String message = "Error while starting recorder.";
      logger.error(message, e);
      this.view.root.showException(message, e);
      return;
    }
    this.isRunning = true;
    this.selectedAnnoation = this.annotationList.observable.get(0);
    this.crcIsActive.setFill(INACTIVE);
    this.setLabelTextToCurrentAnnotation();
  }

  public void stop() {
    if (this.isRunning) {
      try {
        this.recorder.stopRecording();
      } catch (Throwable e) {
        String message = "Error while closing record file.";
        logger.error(message, e);
        this.view.root.showException(message, e);
      }
    }
    this.isRunning = false;
    this.crcIsActive.setFill(NOT_STARTED);
    this.lblActiveAnnotation.setText("Recording stopped.");
    this.view.root.scene.removeEventFilter(MouseEvent.MOUSE_PRESSED, this::onMouseDown);
    this.view.root.scene.removeEventFilter(MouseEvent.MOUSE_RELEASED, this::onMouseUp);
  }

  private void onKeyPressed(KeyEvent event) {
    KeyCode pressedKey = event.getCode();
    if (pressedKey == KeyCode.ESCAPE) {
      if (this.isRunning) {
        this.stop();
      }
    } else if (pressedKey.isDigitKey()) {
      int value = Integer.parseInt(pressedKey.getName()) - 1;
      if (value < 0) {
        value = 9;
      }
      if (value < this.annotationList.observable.size()) {
        this.selectedAnnoation = this.annotationList.observable.get(value);
        this.setLabelTextToCurrentAnnotation();
      }
    } else if (pressedKey == KeyCode.ENTER) {
      this.start();
    }
  }

  private void setLabelTextToCurrentAnnotation() {
    this.lblActiveAnnotation.setText("Current annotation " + this.selectedAnnoation.getName());
  }

  private void onMouseDown(MouseEvent event) {
    if (!this.isRunning) {
      return;
    }
    event.consume();
    this.recorder.startAnnotation(this.selectedAnnoation);
    this.crcIsActive.setFill(ACTIVE);

  }

  private void onMouseUp(MouseEvent event) {
    if (!this.isRunning) {
      return;
    }
    event.consume();
    try {
      this.recorder.stopAnnotation(this.selectedAnnoation);
    } catch (Throwable e) {
      this.stop();
      String message = "Error while writing annotation interval to file.";
      logger.error(message, e);
      this.view.root.showException(message, e);
    }
    this.crcIsActive.setFill(INACTIVE);
  }
}
