package de.luk.ml.annotation.gui.views.main;

import de.luk.ml.annotation.gui.views.common.ViewController;
import javafx.fxml.FXML;

import javax.annotation.PostConstruct;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class MainView extends ViewController {

  @PostConstruct
  public void postConstruct(){

  }

  @FXML
  public void reviewAnnotations(){
    this.root.showReviewAnnotationView();
  }

  @FXML
  public void createAnnotation(){
    this.root.showConfigAnnotationCreation();
  }

  public String getTitle() {
    return "Main";
  }

}
