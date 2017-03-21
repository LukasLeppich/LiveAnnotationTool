package de.luk.ml.annotation.gui.components.recording;

import de.luk.ml.annotation.annotations.Annotation;
import de.luk.ml.annotation.annotations.AnnotationList;
import de.luk.ml.annotation.gui.components.common.ComponentController;
import de.luk.ml.annotation.gui.views.common.ViewController;
import de.luk.ml.annotation.recorder.AnnotationRecorder;
import javafx.beans.binding.Bindings;
import javafx.event.Event;
import javafx.event.EventHandler;
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
import java.util.Objects;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class TriggerAnnotationComponent extends ComponentController {
  private static Logger logger = LoggerFactory.getLogger(TriggerAnnotationComponent.class);
  private boolean isRunning;

  private static Paint NOT_STARTED = Color.web("#505050");
  private static Paint ACTIVE = Color.web("#76FF03");
  private static Paint INACTIVE = Color.web("#F1F8E9");

  private Annotation selectedAnnoation;
  private int selectedAnnoationIndex = 0;
  private File outputFile;
  private String pressedKey;


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


  private EventHandler<KeyEvent> keyPressed;
  private EventHandler<KeyEvent> keyReleased;
  private EventHandler<MouseEvent> mouseDown;
  private EventHandler<MouseEvent> mouseUp;

  @PostConstruct
  private void init() {
    keyPressed = (event) -> {
      this.onKeyPressed(event);
    };
    keyReleased = (event) -> {
      this.onKeyReleased(event);
    };
    mouseDown = (event)->{
      this.onMouseDown(event);
    };
    mouseUp = (event)->{
      this.onMouseUp(event);
    };
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
        "1-0: Select annotation (0 is 10)\n" +
        "+|-: Select next|previous annotation\n" +
        "Press and hold any key to specify an annotated period of time.");
  }

  public void setOutputFile(File output) {
    this.outputFile = output;
  }

  @Override
  public void setView(ViewController view) {
    super.setView(view);
    logger.info("setView trigger component");
    this.view.root.scene.addEventFilter(KeyEvent.KEY_PRESSED, this.keyPressed);
    this.view.root.scene.addEventFilter(KeyEvent.KEY_RELEASED, this.keyReleased);
  }

  @Override
  public void detach() {
    logger.info("detach trigger component");
    this.view.root.scene.removeEventFilter(KeyEvent.KEY_PRESSED, this.keyPressed);
    this.view.root.scene.removeEventFilter(KeyEvent.KEY_RELEASED, this.keyReleased);
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
    this.selectedAnnoation = this.annotationList.observable.get(this.selectedAnnoationIndex);
    this.crcIsActive.setFill(INACTIVE);
    this.setLabelTextToCurrentAnnotation();
    this.view.root.scene.addEventFilter(MouseEvent.MOUSE_PRESSED, this.mouseDown);
    this.view.root.scene.addEventFilter(MouseEvent.MOUSE_RELEASED, this.mouseUp);
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
    this.view.root.showReviewAnnotationView(outputFile);
    this.view.root.scene.removeEventFilter(MouseEvent.MOUSE_PRESSED, this.mouseDown);
    this.view.root.scene.removeEventFilter(MouseEvent.MOUSE_RELEASED, this.mouseUp);
  }

  private void onKeyPressed(KeyEvent event) {
    KeyCode pressedKey = event.getCode();
    logger.info("Key pressed: ", pressedKey);
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
        this.selectedAnnoationIndex = value;
        this.selectedAnnoation = this.annotationList.observable.get(value);
        this.setLabelTextToCurrentAnnotation();
      }
    } else if (pressedKey == KeyCode.PLUS) {
      this.selectedAnnoationIndex = Math.min(this.selectedAnnoationIndex + 1,
          this.annotationList.observable.size() - 1);
      this.selectedAnnoation = this.annotationList.observable.get(this.selectedAnnoationIndex);
      this.setLabelTextToCurrentAnnotation();
    } else if (pressedKey == KeyCode.MINUS) {
      this.selectedAnnoationIndex = Math.max(this.selectedAnnoationIndex - 1, 0);
      this.selectedAnnoation = this.annotationList.observable.get(this.selectedAnnoationIndex);
      this.setLabelTextToCurrentAnnotation();
    } else if (pressedKey == KeyCode.ENTER) {
      if(!isRunning){
        this.start();
      }
    } else {
      if (Objects.isNull(this.pressedKey) || this.pressedKey.isEmpty()) {
        this.pressedKey = event.getCode().getName();
        startAnnotation(event);
      } else if (!this.pressedKey.equals(event.getCode().getName())) {
        this.pressedKey = event.getCode().getName();
      }
    }
  }

  private void onKeyReleased(KeyEvent event) {
    if (this.isRunning && Objects.nonNull(pressedKey) && !pressedKey.isEmpty() && pressedKey.equals(event.getCode().getName())) {
      stopAnnotation(event);
    }
  }

  private void setLabelTextToCurrentAnnotation() {
    this.lblActiveAnnotation.setText("Current annotation: \"" + this.selectedAnnoation.getName() + "\"");
  }

  private void onMouseDown(MouseEvent event) {
    startAnnotation(event);
  }

  private void onMouseUp(MouseEvent event) {
    stopAnnotation(event);
  }

  private void startAnnotation(Event event) {
    if (!this.isRunning) {
      return;
    }
    event.consume();
    this.recorder.startAnnotation(this.selectedAnnoation);
    this.crcIsActive.setFill(ACTIVE);
  }

  private void stopAnnotation(Event event) {
    if (!this.isRunning) {
      return;
    }
    event.consume();
    try {
      this.pressedKey = null;
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
