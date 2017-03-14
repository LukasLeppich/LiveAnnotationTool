package de.luk.ml.annotation.gui.components.fileselection;

import de.luk.ml.annotation.gui.components.common.ComponentController;
import de.luk.ml.annotation.gui.views.common.ViewController;
import de.luk.ml.annotation.utils.AnnotationFileFilter;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.apache.commons.io.comparator.LastModifiedFileComparator;

import javax.annotation.PostConstruct;
import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/8/17.
 */
public class SelectAnnotationFile extends ComponentController {

  private Consumer<File> onFileSelected;

  @FXML
  private TextField txfFilePath;

  @FXML
  private void openFileChooser() {
    FileChooser fc = new FileChooser();
    fc.setTitle("Select an annotation file");
    File initializeDirectory = this.view.root.workingDirectory;
    if (!this.txfFilePath.getText().isEmpty()) {
      File selectedFile = new File(this.txfFilePath.getText());
      if (selectedFile.exists() || selectedFile.getParentFile().exists()) {
        initializeDirectory = selectedFile.getParentFile();
      }
    }
    fc.setInitialDirectory(initializeDirectory);
    fc.getExtensionFilters().add(AnnotationFileFilter.getCSVFilter());
    fc.getExtensionFilters().add(AnnotationFileFilter.getAnyFilter());
    File outputFile = fc.showOpenDialog(this.view.root.stage);
    if (outputFile != null) {
      txfFilePath.setText(outputFile.getAbsolutePath());
      if (Objects.nonNull(onFileSelected)) {
        onFileSelected.accept(outputFile);
      }
    }
  }

  @PostConstruct
  public void init() {
  }

  public void setFilePath(File file) {
    this.txfFilePath.setText(file.getAbsolutePath());
  }

  public String getFilePath() {
    return this.txfFilePath.getText();
  }

  public SelectAnnotationFile setOnFileSelected(Consumer<File> onFileSelected) {
    this.onFileSelected = onFileSelected;
    return this;
  }

  @Override
  public void setView(ViewController view) {
    super.setView(view);
    if (this.txfFilePath.getText().isEmpty()) {
      File root = this.view.root.workingDirectory;
      if (root.exists()) {
        Arrays.stream(Optional.ofNullable(root.listFiles()).orElseGet(() -> new File[0]))
            .filter(file -> file.getName().endsWith(".csv"))
            .sorted(LastModifiedFileComparator.LASTMODIFIED_COMPARATOR.reversed())
            .findFirst()
            .ifPresent(file -> this.txfFilePath.setText(file.getAbsolutePath()));
      }
    }
  }

}
