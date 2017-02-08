package de.luk.ml.annotation.gui.components.review;

import de.luk.ml.annotation.annotations.Annotation;
import de.luk.ml.annotation.gui.components.common.ComponentController;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/8/17.
 */
public class AnnotationStats extends ComponentController {
  private List<StatItem> items;

  @Inject
  private Instance<StatItem> itemInstaces;

  @FXML
  private VBox vbxContent;

  @PostConstruct
  public void init() {
    this.items = IntStream.range(0, 2)
        .mapToObj(i -> this.itemInstaces.get())
        .collect(Collectors.toList());
    vbxContent.getChildren().addAll(this.items);
  }

  public void setAnnotations(List<Annotation> annotations) {
    this.items.get(0).setStat("Annotations: ", Integer.toString(annotations.size()));
  }

}
