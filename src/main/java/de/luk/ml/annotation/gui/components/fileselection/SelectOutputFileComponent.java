package de.luk.ml.annotation.gui.components.fileselection;

import de.luk.ml.annotation.gui.components.common.ComponentController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class SelectOutputFileComponent extends ComponentController {

  @FXML
  private TextField txfFilePath;

  @FXML
  private void openFileChooser() {
    FileChooser fc = new FileChooser();
    fc.setTitle("Select annotation output file");
    File outputFile = fc.showSaveDialog(this.view.root.stage);
    if(outputFile != null){
      txfFilePath.setText(outputFile.getAbsolutePath());
    }
  }

  public String getFilePath() {
    return txfFilePath.getText();
  }

}
