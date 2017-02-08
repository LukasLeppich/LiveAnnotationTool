package de.luk.ml.annotation.gui.components.config;

import de.luk.ml.annotation.annotations.Annotation;
import de.luk.ml.annotation.gui.components.common.ComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
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
  private TextField txfName;

  @FXML
  private void removeAnnotation() {
    if (!Objects.isNull(this.annotation)) {
      this.annotation.remove();
      this.unsetAnnotation();
    }
  }

  public void setAnnotation(Annotation annotation) {
    this.unsetAnnotation();
    this.annotation = annotation;
    this.txfName.textProperty().bindBidirectional(annotation.name);
  }

  public void unsetAnnotation() {
    if (!Objects.isNull(this.annotation)) {
      this.txfName.textProperty().unbindBidirectional(annotation.name);
      this.annotation = null;
    }
  }
}
