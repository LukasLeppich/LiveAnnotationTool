package de.luk.ml.annotation.gui.components.review;

import de.luk.ml.annotation.annotations.AnnotationFile;
import de.luk.ml.annotation.annotations.ElanEAFGenerator;
import de.luk.ml.annotation.gui.components.common.ComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 3/9/17.
 */
public class CreateEAF extends ComponentController {
  private AnnotationFile annotationFile;
  @Inject
  private Instance<StatItem> itemInstaces;

  @FXML
  private TextField txfMediaFile;

  @FXML
  private TextField txfOutputFile;

  @FXML
  private Label lblOutput;


  @PostConstruct
  public void init() {
  }

  @FXML
  public void createEAFFile() {
    lblOutput.setText("");
    ElanEAFGenerator generator = new ElanEAFGenerator(this.annotationFile.getAnnotations());
    generator.setMediaFile(new File(this.txfMediaFile.getText()));
    try {
      File outputFile = new File(this.txfOutputFile.getText());
      generator.writeToFile(outputFile);
      lblOutput.setText("Written " + outputFile.length() + " bytes");
    } catch (Exception e) {
      e.printStackTrace();
      lblOutput.setText(e.getClass().getSimpleName());
    }
  }

  @FXML
  public void selectMediaFile() {
    FileChooser fc = new FileChooser();
    if(!this.txfMediaFile.getText().isEmpty()){
      fc.setInitialFileName(this.txfMediaFile.getText());
      fc.setInitialDirectory(Paths.get(this.txfMediaFile.getText()).getParent().toFile());
    } else {
      fc.setInitialDirectory(this.annotationFile.getFile().getParentFile());
    }
    fc.setTitle("Select media file");
    File response = fc.showOpenDialog(this.view.root.stage);
    if(Objects.nonNull(response)){
      this.txfMediaFile.setText(response.getAbsolutePath());
    }
  }

  @FXML
  public void selectOutputFile() {
    FileChooser fc = new FileChooser();
    if(!this.txfOutputFile.getText().isEmpty()){
      fc.setInitialFileName(this.txfOutputFile.getText());
      fc.setInitialDirectory(Paths.get(this.txfOutputFile.getText()).getParent().toFile());
    } else {
      fc.setInitialDirectory(this.annotationFile.getFile().getParentFile());
    }
    fc.setTitle("Select output file");
    File response = fc.showSaveDialog(this.view.root.stage);
    if(Objects.nonNull(response)){
      this.txfOutputFile.setText(response.getAbsolutePath());
    }
  }

  public void setAnnotationFile(AnnotationFile annotations) {
    this.annotationFile = annotations;
    String baseFileName = annotationFile.getFile().getName().replaceFirst("\\.[^.]+$", ".");
    this.txfOutputFile.setText(
        Paths.get(annotations.getFile().getParentFile().getAbsolutePath(),
            baseFileName + "eaf").toString());
    this.txfMediaFile.setText(
        Paths.get(annotations.getFile().getParentFile().getAbsolutePath(),
            baseFileName + "mp4").toString());

  }

}
