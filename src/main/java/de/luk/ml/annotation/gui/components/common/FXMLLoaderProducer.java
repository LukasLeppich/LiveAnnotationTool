package de.luk.ml.annotation.gui.components.common;

import javafx.fxml.FXMLLoader;

import javax.enterprise.inject.Instance;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class FXMLLoaderProducer {
  @Inject
  Instance<Object> instance;

  @Produces
  public FXMLLoader createLoader() {
    return new FXMLLoader(null, null, null, (param) -> {
      return instance.select(param).get();
    }
        , StandardCharsets.UTF_8
    );
  }
}
