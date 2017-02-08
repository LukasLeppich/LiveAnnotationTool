package de.luk.ml.annotation.gui.components.config;

import de.luk.ml.annotation.annotations.Annotation;
import javafx.scene.control.ListCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.Objects;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class AnnotationListCell extends ListCell<Annotation> {
  private static Logger logger = LoggerFactory.getLogger(AnnotationListCell.class);
  private AnnotationItemComponent item;

  @Inject
  private Instance<AnnotationItemComponent> itemInstance;

  @Override
  protected void updateItem(Annotation annotation, boolean empty) {
    super.updateItem(annotation, empty);
    if (empty) {
      this.setGraphic(null);
      if (!Objects.isNull(this.item)) {
        this.item.unsetAnnotation();
      }
    } else {
      if (Objects.isNull(this.item)) {
        this.item = itemInstance.get();
      }
      this.item.setAnnotation(annotation);
      this.setGraphic(this.item);
    }
  }
}
