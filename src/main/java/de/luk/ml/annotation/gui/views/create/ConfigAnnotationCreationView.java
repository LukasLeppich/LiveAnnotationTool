package de.luk.ml.annotation.gui.views.create;

import de.luk.ml.annotation.gui.components.annotations.creation.AnnotationListComponent;
import de.luk.ml.annotation.gui.components.fileselection.SelectOutputFileComponent;
import de.luk.ml.annotation.gui.views.common.ViewController;
import javafx.fxml.FXML;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class ConfigAnnotationCreationView extends ViewController {

  @Inject
  private AnnotationListComponent annotationListComponent;

  @Inject
  private SelectOutputFileComponent outputFileComponent;

  @Inject
  private CreateAnnotationsView createAnnotationsView;

  @PostConstruct
  public void init() {
    this.annotationListComponent.setView(this);
    this.outputFileComponent.setView(this);
    this.setLeft(this.annotationListComponent);
    this.setTop(this.outputFileComponent);
  }

  @FXML
  private void startAnnotation() {
    this.root.setView(this.createAnnotationsView);
  }

}
