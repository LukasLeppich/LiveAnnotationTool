package de.luk.ml.annotation.gui.views;

import de.luk.ml.annotation.gui.components.common.ComponentController;
import de.luk.ml.annotation.gui.views.common.ViewController;
import de.luk.ml.annotation.gui.views.create.ConfigAnnotationCreationView;
import de.luk.ml.annotation.gui.views.create.RecordAnnotationsView;
import de.luk.ml.annotation.gui.views.main.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class RootView extends ComponentController {
  public Scene scene;
  public Stage stage;

  private ViewController currentView;

  @Inject
  private MainView mainView;

  @Inject
  private ConfigAnnotationCreationView configAnnotationCreationView;

  @Inject
  private RecordAnnotationsView recordAnnotationsView;

  @PostConstruct
  public void init() {
    scene = new Scene(this);
    scene.getStylesheets().clear();
    scene.getStylesheets().add(RootView.class.getResource("/styles/MainStyle.css").toExternalForm());
  }

  public void start(final Stage primaryStage, final Application.Parameters parameters) throws IOException {
    stage = primaryStage;
    stage.setScene(scene);
    stage.setTitle("Live Annotation");
    setView(this.mainView);
    stage.show();
  }

  public void showConfigAnnotationCreation() {
    this.setView(this.configAnnotationCreationView);
  }

  public void showRecordAnnotation(File outputFile) {
    this.recordAnnotationsView.setOutputFile(outputFile);
    this.setView(this.recordAnnotationsView);
  }

  public void setView(ViewController view) {
    if (Objects.nonNull(currentView)) {
      currentView.detach();
    }
    currentView = view;
    currentView.setRootView(this);
    this.setCenter(currentView);
  }


  public void showException(String message, Throwable e) {
    // From: http://code.makery.ch/blog/javafx-dialogs-official/
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Exception Dialog");
    alert.setHeaderText(message);
    alert.setContentText(e.getLocalizedMessage());

    StringWriter sw = new StringWriter();
    PrintWriter pw = new PrintWriter(sw);
    e.printStackTrace(pw);
    String exceptionText = sw.toString();

    Label label = new Label("The exception stacktrace was:");

    TextArea textArea = new TextArea(exceptionText);
    textArea.setEditable(false);
    textArea.setWrapText(true);

    textArea.setMaxWidth(Double.MAX_VALUE);
    textArea.setMaxHeight(Double.MAX_VALUE);
    GridPane.setVgrow(textArea, Priority.ALWAYS);
    GridPane.setHgrow(textArea, Priority.ALWAYS);

    GridPane expContent = new GridPane();
    expContent.setMaxWidth(Double.MAX_VALUE);
    expContent.add(label, 0, 0);
    expContent.add(textArea, 0, 1);

    alert.getDialogPane().setExpandableContent(expContent);

    alert.showAndWait();
  }
}
