package de.luk.ml.annotation.gui.components.review;

import de.luk.ml.annotation.annotations.AnnotationFile;
import de.luk.ml.annotation.gui.components.common.ComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 3/14/17.
 */
public class RenameFiles extends ComponentController {
  private static Logger logger = LoggerFactory.getLogger(RenameFiles.class);
  private AnnotationFile annotationFile;
  private File parentFile;

  @FXML
  private TextField txfNameReference;

  @FXML
  private ListView<String> lstFiles;


  public void setAnnotationFile(AnnotationFile file) {
    this.annotationFile = file;
    this.txfNameReference.setText(this.annotationFile.getFile().getAbsolutePath());
    this.loadFiles();
  }

  @PostConstruct
  public void init() {
    this.lstFiles.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
  }

  @FXML
  private void selectFile() {
    FileChooser chooser = new FileChooser();
    chooser.setInitialDirectory(this.annotationFile.getFile().getParentFile());
    File file = chooser.showOpenDialog(view.root.stage);
    if (Objects.nonNull(file)) {
      this.txfNameReference.setText(file.getAbsolutePath());
    }
  }

  @FXML
  private void loadFiles() {
    this.parentFile = new File(this.txfNameReference.getText()).getParentFile();
    if (this.parentFile.exists()) {
      this.lstFiles.getItems().setAll(Arrays.stream(this.parentFile.listFiles()).map(File::getName).collect(Collectors.toList()));
    }
  }

  @FXML
  private void renameFiles() {
    String baseName = FilenameUtils.getBaseName(this.txfNameReference.getText());
    for (String file : this.lstFiles.getSelectionModel().getSelectedItems()) {
      String extension = FilenameUtils.getExtension(file);
      if (file.endsWith("csv.started")) {
        extension = "csv.started";
      }
      Path oldFile = Paths.get(this.parentFile.getAbsolutePath(), file);
      Path newFile = Paths.get(this.parentFile.getAbsolutePath(), baseName + "." + extension);
      if (Files.exists(newFile)) {
        logger.warn("File " + newFile + " already exists");
      } else {
        try {
          Files.move(oldFile, newFile);
          logger.info("Rename " + oldFile + " to " + newFile);
        } catch (IOException e) {
          e.printStackTrace();
          logger.error("Error while renaming file: " + e.getMessage());
        }
      }
    }
    this.loadFiles();
  }
}
