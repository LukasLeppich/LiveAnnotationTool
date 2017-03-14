package de.luk.ml.annotation.gui.components.fileselection;

import de.luk.ml.annotation.gui.components.common.ComponentController;
import de.luk.ml.annotation.gui.views.common.ViewController;
import de.luk.ml.annotation.utils.AnnotationFileFilter;
import de.luk.ml.annotation.utils.AnnotationFileUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class SelectOutputFileComponent extends ComponentController {

  public SimpleStringProperty filePath = new SimpleStringProperty();

  @FXML
  private TextField txfFilePath;

  @FXML
  private void openFileChooser() {
    FileChooser fc = new FileChooser();
    fc.setTitle("Select annotation output file");
    fc.getExtensionFilters().add(AnnotationFileFilter.getCSVFilter());
    fc.getExtensionFilters().add(AnnotationFileFilter.getAnyFilter());
    if (!txfFilePath.getText().isEmpty()) {
      fc.setInitialFileName(txfFilePath.getText());
      fc.setInitialDirectory(Paths.get(txfFilePath.getText()).getParent().toFile());
    } else {
      fc.setInitialDirectory(this.view.root.workingDirectory);
    }
    File outputFile = fc.showSaveDialog(this.view.root.stage);
    if (outputFile != null) {
      txfFilePath.setText(outputFile.getAbsolutePath());
    }
  }

  @FXML
  private void refreshName() {
    LocalDateTime currentTime = LocalDateTime.now();
    String filename = AnnotationFileUtils.getCurrentFileName("csv");
    File newParent = this.view.root.workingDirectory;
    if (!this.txfFilePath.getText().trim().isEmpty()) {
      File currentFile = new File(this.txfFilePath.getText());
      if (currentFile.exists() || currentFile.getParentFile().exists()) {
        newParent = currentFile.getParentFile();
      }
    }
    this.txfFilePath.setText(FilenameUtils.concat(newParent.getAbsolutePath(), filename));
  }

  @PostConstruct
  public void init() {
    this.filePath.bind(txfFilePath.textProperty());
  }

  @Override
  public void setView(ViewController view) {
    super.setView(view);
    this.refreshName();
  }

  @PreDestroy
  public void destroy() {
    this.filePath.unbind();
  }

}
