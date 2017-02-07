package de.luk.ml.annotation.gui.components.annotations.creation;

import de.luk.ml.annotation.annotations.Annotation;
import de.luk.ml.annotation.gui.components.common.ComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.util.converter.NumberStringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class AnnotationItemComponent extends ComponentController {
  private static Logger logger = LoggerFactory.getLogger(AnnotationItemComponent.class);
  private Annotation annotation;

  @FXML
  private Label lblIndex;

  @FXML
  private TextField txfName;

  @FXML
  private void removeAnnotation() {
    if (!Objects.isNull(this.annotation)) {
      this.annotation.remove();
    }
  }

  public void setAnnotation(Annotation annotation) {
    this.annotation = annotation;
    this.txfName.textProperty().bindBidirectional(annotation.name);
    this.lblIndex.textProperty().bindBidirectional(annotation.index, new NumberStringConverter());
  }

  public void unsetAnnotation() {
    this.txfName.textProperty().unbindBidirectional(annotation.name);
    this.lblIndex.textProperty().unbindBidirectional(annotation.index);
    this.annotation = null;
  }
}
