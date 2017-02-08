package de.luk.ml.annotation.utils;

import javafx.stage.FileChooser;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/8/17.
 */
public class AnnotationFileFilter {

  public static FileChooser.ExtensionFilter getCSVFilter() {
    return new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
  }

  public static FileChooser.ExtensionFilter getAnyFilter() {
    return new FileChooser.ExtensionFilter("Any file", "*");
  }


}
