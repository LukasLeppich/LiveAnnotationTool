package de.luk.ml.annotation.gui.views.create;

import de.luk.ml.annotation.annotations.AnnotationList;
import de.luk.ml.annotation.gui.views.common.ViewController;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class CreateAnnotationsView extends ViewController {

  @Inject
  private AnnotationList annotationList;

  private File outputFile;

  @PostConstruct
  private void init() {

  }

  public CreateAnnotationsView setOutputFile(File file) {
    this.outputFile = file;
    return this;
  }
}
