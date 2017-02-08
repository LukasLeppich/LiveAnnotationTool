package de.luk.ml.annotation.gui.components.review;

import de.luk.ml.annotation.gui.components.common.ComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/8/17.
 */
public class StatItem extends ComponentController {

  @FXML
  private TextField txfCount;

  @FXML
  private Label lblLabel;


  public StatItem setStat(String name, String value) {
    this.lblLabel.setText(name);
    this.txfCount.setText(value);
    return this;
  }

}
