package de.luk.ml.annotation.gui.views;

import de.luk.ml.annotation.gui.components.common.ComponentController;
import de.luk.ml.annotation.gui.views.common.ViewController;
import de.luk.ml.annotation.gui.views.create.ConfigAnnotationCreationView;
import de.luk.ml.annotation.gui.views.create.CreateAnnotationsView;
import de.luk.ml.annotation.gui.views.main.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;
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
  private CreateAnnotationsView createAnnotationsView;

  @PostConstruct
  public void init() {
    scene = new Scene(this);
    scene.getStylesheets().clear();
    scene.getStylesheets().add(RootView.class.getResource("/styles/MainStyle.css").toExternalForm());
  }

  public void start(final Stage primaryStage, final Application.Parameters parameters) throws IOException {
    stage = primaryStage;
    stage.setScene(scene);
    stage.setWidth(800);
    stage.setHeight(600);
    stage.sizeToScene();
    stage.setTitle("Live Annotation");
    setView(this.mainView);
    stage.show();
  }

  public void showConfigAnnotationCreation() {
    this.setView(this.configAnnotationCreationView);
  }

  public void setView(ViewController view) {
    if (Objects.nonNull(currentView)) {
      currentView.detach();
    }
    currentView = view;
    currentView.setRootView(this);
    this.setCenter(currentView);
  }
}
