package de.luk.ml.annotation.gui.views.review;

import de.luk.ml.annotation.annotations.AnnotationFile;
import de.luk.ml.annotation.annotations.AnnotationFileLoader;
import de.luk.ml.annotation.gui.components.fileselection.SelectAnnotationFile;
import de.luk.ml.annotation.gui.components.review.AnnotationStats;
import de.luk.ml.annotation.gui.components.review.UpdateStartTime;
import de.luk.ml.annotation.gui.views.common.ViewController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.TilePane;

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

  @FXML
  private TilePane tilePanel;

  @PostConstruct
  private void init() {
    setTop();
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

  private void loadFile(ActionEvent action) {
    if (Objects.nonNull(this.annotationFile)) {
      this.annotationFile.removeChangeListener(this::onChange);
    }
    File inputFile = new File(this.selectAnnotationFile.filePath.get());
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

  private void setContent() {
    if (this.tilePanel.getChildren().isEmpty()) {
      this.tilePanel.getChildren().add(this.stats);
      this.tilePanel.getChildren().add(this.updateStartTime);
    }
    this.stats.setAnnotations(this.annotationFile.getAnnotations());
    this.updateStartTime.setAnnotationFile(this.annotationFile);
  }

  public String getTitle() {
    return "Review";
  }
}
