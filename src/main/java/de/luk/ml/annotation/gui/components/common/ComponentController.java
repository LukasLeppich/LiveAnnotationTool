package de.luk.ml.annotation.gui.components.common;


import de.luk.ml.annotation.gui.views.common.ViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.io.IOException;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public abstract class ComponentController extends BorderPane {
  protected ViewController view;

  public void setView(ViewController view) {
    this.view = view;
  }


  @Inject
  private FXMLLoader fxmlLoader;

  @PostConstruct
  private void loadFXMLAndSetStyle() {
    try {
      fxmlLoader.setLocation(this.getClass().getResource(this.getClass().getSimpleName() + ".fxml"));
      fxmlLoader.setController(this);
      fxmlLoader.setRoot(this);
      fxmlLoader.load();
      this.getStylesheets().add(ComponentController.class.getResource("/styles/MainStyle.css").toExternalForm());
    } catch (IOException exc) {
      exc.printStackTrace();
    }
  }

}
