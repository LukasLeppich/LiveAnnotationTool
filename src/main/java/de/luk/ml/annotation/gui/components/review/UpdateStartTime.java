package de.luk.ml.annotation.gui.components.review;

import de.luk.ml.annotation.annotations.AnnotationFile;
import de.luk.ml.annotation.gui.components.common.ComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.annotation.PostConstruct;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/8/17.
 */
public class UpdateStartTime extends ComponentController {
  private AnnotationFile annotationFile;

  @FXML
  private TextField txfAnnotationStarted;

  @FXML
  private TextField txfRecordingStarted;

  @FXML
  private Button btnUpdateTimes;

  @PostConstruct
  private void init() {
    this.btnUpdateTimes.disableProperty()
        .bind(txfRecordingStarted.textProperty().isEmpty());
  }

  public UpdateStartTime setAnnotationFile(AnnotationFile annotationFile) {
    this.annotationFile = annotationFile;
    this.txfAnnotationStarted.setText(Long.toString(this.annotationFile.getStart()));
    return this;
  }

  @FXML
  private void updateTimes() {
    if (!txfRecordingStarted.getText().matches("^\\d+$")) {
      this.view.root.showError("Recording started time should be an number timestamp");
    }
    long startTime = Long.parseLong(this.txfRecordingStarted.getText());
    this.view.root.executeAndShowException(() -> this.annotationFile.recalculateTimes(startTime), "Error while updating annotation file.");
  }

}
