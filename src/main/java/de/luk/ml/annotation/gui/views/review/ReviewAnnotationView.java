package de.luk.ml.annotation.gui.views.review;

import de.luk.ml.annotation.annotations.AnnotationFile;
import de.luk.ml.annotation.annotations.AnnotationFileLoader;
import de.luk.ml.annotation.gui.components.fileselection.SelectAnnotationFile;
import de.luk.ml.annotation.gui.components.review.AnnotationStats;
import de.luk.ml.annotation.gui.components.review.CreateEAF;
import de.luk.ml.annotation.gui.components.review.UpdateStartTime;
import de.luk.ml.annotation.gui.views.common.ViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/8/17.
 */
public class ReviewAnnotationView extends ViewController {
  private AnnotationFile annotationFile;

  @Inject
  private SelectAnnotationFile selectAnnotationFile;

  @Inject
  private AnnotationStats stats;

  @Inject
  private UpdateStartTime updateStartTime;

  @Inject
  private AnnotationFileLoader fileLoader;

  @Inject
  private CreateEAF createEAF;

  @FXML
  private VBox vbxPanel;

  @PostConstruct
  private void init() {
    setTop();
  }

  public void setFileAndLoad(File file) {
    this.selectAnnotationFile.setFilePath(file);
    this.loadFile(null);
  }

  private void setTop() {
    HBox top = new HBox();
    top.setSpacing(3);
    top.setAlignment(Pos.CENTER);
    top.setPadding(new Insets(0, 6, 6, 6));
    Button load = new Button("Load");
    load.setOnAction(this::loadFile);
    top.getChildren().add(selectAnnotationFile);
    top.getChildren().add(load);
    HBox.setHgrow(selectAnnotationFile, Priority.ALWAYS);
    HBox.setHgrow(load, Priority.NEVER);
    this.setTop(top);
  }

  @Override
  public void attach() {
    super.attach();
    if (Objects.nonNull(this.annotationFile) && this.annotationFile.getFile().exists()) {
      this.selectAnnotationFile.setFilePath(this.annotationFile.getFile().getAbsoluteFile());
      this.loadAnnotationFile();
    } else {
      this.annotationFile = null;
      this.clearContent();
    }
  }

  private void loadFile(ActionEvent action) {
    this.loadAnnotationFile();
  }

  private void loadAnnotationFile() {
    if (Objects.nonNull(this.annotationFile)) {
      this.annotationFile.removeChangeListener(this::onChange);
    }
    File inputFile = new File(this.selectAnnotationFile.getFilePath());
    if (!inputFile.exists()) {
      this.root.showError("Annotation file not found!");
      return;
    }
    if (!inputFile.isFile()) {
      this.root.showError("Please select a file!");
      return;
    }
    Optional<AnnotationFile> annotations = this.root.executeAndShowException(() -> this.fileLoader.loadAnnotationFile(inputFile), "Error while loading annotation file.");
    if (!annotations.isPresent()) {
      return;
    }
    this.annotationFile = annotations.get();
    this.annotationFile.addChangeListener(this::onChange);
    this.setContent();
  }

  private void onChange(AnnotationFile annotationFile) {
    this.setContent();
  }

  private void clearContent() {
    this.vbxPanel.getChildren().clear();
  }

  private void setContent() {
    if (this.vbxPanel.getChildren().isEmpty()) {
      this.vbxPanel.getChildren().add(this.stats);
      this.vbxPanel.getChildren().add(this.updateStartTime);
      this.vbxPanel.getChildren().add(this.createEAF);
    }
    this.stats.setAnnotations(this.annotationFile.getAnnotations());
    this.updateStartTime.setAnnotationFile(this.annotationFile);
    this.createEAF.setAnnotationFile((this.annotationFile));
  }

  public String getTitle() {
    return "Review";
  }
}
