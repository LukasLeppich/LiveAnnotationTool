package de.luk.ml.annotation.gui.views.create;

import de.luk.ml.annotation.gui.components.annotations.TriggerAnnotationComponent;
import de.luk.ml.annotation.gui.views.common.ViewController;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class RecordAnnotationsView extends ViewController {

  @Inject
  private TriggerAnnotationComponent triggerAnnotationComponent;

  @PostConstruct
  private void init() {
    this.setCenter(this.triggerAnnotationComponent);
  }

  public void setOutputFile(File outputFile) {
    this.triggerAnnotationComponent.setOutputFile(outputFile);
  }

}
