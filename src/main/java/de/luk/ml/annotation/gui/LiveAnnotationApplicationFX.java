package de.luk.ml.annotation.gui;

import de.luk.ml.annotation.gui.views.RootView;
import javafx.application.Application;
import javafx.stage.Stage;
import org.jboss.weld.environment.se.Weld;

import java.io.IOException;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class LiveAnnotationApplicationFX extends Application {

  private Weld weld;

  @Override
  public void start(Stage primaryStage) throws IOException {
    weld.initialize().instance().select( RootView.class ).get().start( primaryStage, getParameters() );
  }

  @Override
  public void init() throws Exception {
    weld = new Weld();
  }

  @Override
  public void stop() throws Exception {
    weld.shutdown();
  }

}
