package de.luk.ml.annotation.gui.components.fileselection;

import de.luk.ml.annotation.gui.components.common.ComponentController;
import de.luk.ml.annotation.gui.views.common.ViewController;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
    File outputFile = fc.showSaveDialog(this.view.root.stage);
    if (outputFile != null) {
      txfFilePath.setText(outputFile.getAbsolutePath());
    }
  }

  @PostConstruct
  public void init() {
    this.filePath.bind(txfFilePath.textProperty());
  }

  @Override
  public void setView(ViewController view) {
    super.setView(view);
    LocalDateTime currentTime = LocalDateTime.now();
    String filename = currentTime.format(DateTimeFormatter.ofPattern("yyyy_MM_dd__kk_mm__'annotations.csv'"));
    this.txfFilePath.setText(FilenameUtils.concat(this.view.root.workingDirectory.getAbsolutePath(), filename));
  }

  @PreDestroy
  public void destroy() {
    this.filePath.unbind();
  }

}
