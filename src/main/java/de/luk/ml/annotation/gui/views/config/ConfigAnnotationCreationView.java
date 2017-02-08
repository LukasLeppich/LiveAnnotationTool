package de.luk.ml.annotation.gui.views.config;

import de.luk.ml.annotation.annotations.AnnotationList;
import de.luk.ml.annotation.gui.components.config.AnnotationListComponent;
import de.luk.ml.annotation.gui.components.fileselection.SelectOutputFileComponent;
import de.luk.ml.annotation.gui.views.common.ViewController;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class ConfigAnnotationCreationView extends ViewController {

  @FXML
  private Button btnStartAnnotation;

  @Inject
  private AnnotationListComponent annotationListComponent;

  @Inject
  private SelectOutputFileComponent outputFileComponent;

  @Inject
  private AnnotationList annotationList;

  @PostConstruct
  public void init() {
    this.setCenter(this.annotationListComponent);
    this.setTop(this.outputFileComponent);
    this.btnStartAnnotation.disableProperty().bind(
        Bindings.or(
            this.outputFileComponent.filePath.isEmpty(),
            this.annotationList.isEmpty
        ));
  }

  public String getTitle() {
    return "Config recording";
  }

  @FXML
  private void startAnnotation() {
    this.root.showRecordAnnotation(new File(this.outputFileComponent.filePath.get()));
  }

}
