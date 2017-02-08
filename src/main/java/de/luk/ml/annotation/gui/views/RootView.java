package de.luk.ml.annotation.gui.views;

import de.luk.ml.annotation.gui.components.common.ComponentController;
import de.luk.ml.annotation.gui.views.common.ViewController;
import de.luk.ml.annotation.gui.views.config.ConfigAnnotationCreationView;
import de.luk.ml.annotation.gui.views.main.MainView;
import de.luk.ml.annotation.gui.views.recording.RecordAnnotationsView;
import de.luk.ml.annotation.gui.views.review.ReviewAnnotationView;
import de.luk.ml.annotation.utils.CallableWithException;
import de.luk.ml.annotation.utils.RunnableWithException;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Objects;
import java.util.Optional;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class RootView extends ComponentController {
  private static Logger logger = LoggerFactory.getLogger(RootView.class);
  public Scene scene;
  public Stage stage;
  public File workingDirectory = new File(System.getProperty("user.dir"));

  private ViewController currentView;

  @Inject
  private MainView mainView;

  @Inject
  private ConfigAnnotationCreationView configAnnotationCreationView;

  @Inject
  private RecordAnnotationsView recordAnnotationsView;

  @Inject
  private ReviewAnnotationView reviewAnnotationView;

  @FXML
  private HBox menu;

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
    this.askForWorkspace();
    setView(this.mainView);
    stage.show();
  }

  public void showConfigAnnotationCreation() {
    this.setView(this.configAnnotationCreationView);
  }

  public void showReviewAnnotationView() {
    this.setView(this.reviewAnnotationView);
  }

  public void showRecordAnnotation(File outputFile) {
    this.recordAnnotationsView.setOutputFile(outputFile);
    this.setView(this.recordAnnotationsView);
  }

  public void setView(ViewController view) {
    if (Objects.nonNull(currentView)) {
      currentView.detach();
    }
    this.updateMenu(view);
    currentView = view;
    currentView.attach();
    currentView.setRootView(this);
    this.setCenter(currentView);
  }

  private void updateMenu(ViewController newView) {
    if (Objects.isNull(currentView)) {
      return;
    }
    for (int i = 0; i < this.menu.getChildren().size(); i++) {
      if (((Button) this.menu.getChildren().get(i)).getText().equals(newView.getTitle())) {
        this.menu.getChildren().remove(i, this.menu.getChildren().size());
        return;
      }
    }
    this.menu.getChildren().add(createMenuItem(currentView));
  }

  private Button createMenuItem(ViewController view) {
    Button btn = new Button(view.getTitle());
    btn.getStyleClass().add("menu-button");
    btn.setOnAction(action -> this.setView(view));
    return btn;
  }

  public void showError(String message) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Error");
    alert.setContentText(message);
    alert.showAndWait();
  }

  public boolean executeAndShowException(RunnableWithException runnable, String errorMessage) {
    try {
      runnable.run();
      return true;
    } catch (Throwable e) {
      logger.error(errorMessage, e);
      showException(errorMessage, e);
    }
    return false;
  }

  public <T> Optional<T> executeAndShowException(CallableWithException<T> runnable, String errorMessage) {
    try {
      return Optional.ofNullable(runnable.call());
    } catch (Throwable e) {
      logger.error(errorMessage, e);
      showException(errorMessage, e);
    }
    return Optional.empty();
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

  private void askForWorkspace() {
    Dialog<File> dialog = new Dialog<>();
    dialog.setTitle("Select workspace");
    dialog.setHeaderText("Select workspace");
    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CLOSE);

    GridPane grid = new GridPane();
    grid.setHgap(10);
    grid.setVgap(10);
    grid.setPadding(new Insets(20, 150, 10, 10));

    TextField filePath = new TextField();
    filePath.setText(this.workingDirectory.getAbsolutePath());
    filePath.setPromptText("Path to workspace directory");
    GridPane.setHgrow(filePath, Priority.ALWAYS);

    Button button = new Button("...");
    button.setOnAction(action -> {
      DirectoryChooser dc = new DirectoryChooser();
      dc.setTitle("Choose workspace");
      dc.setInitialDirectory(new File(System.getProperty("user.dir")));
      File workingDirectory = dc.showDialog(this.stage);
      if (!Objects.isNull(workingDirectory)) {
        filePath.setText(workingDirectory.getAbsolutePath());
      }
    });
    grid.add(new Label("Workspace:"), 0, 0);
    grid.add(filePath, 1, 0);
    grid.add(button, 2, 0);

    dialog.getDialogPane().setContent(grid);
    dialog.setResultConverter(dialogButton -> {
      if (dialogButton == ButtonType.OK) {
        return new File(filePath.getText());
      }
      return null;
    });
    Optional<File> result = dialog.showAndWait();
    if (result.isPresent()) {
      this.workingDirectory = result.get();
    } else {
      System.exit(0);
    }
  }
}
