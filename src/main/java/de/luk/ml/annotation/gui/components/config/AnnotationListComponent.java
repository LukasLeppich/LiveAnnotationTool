package de.luk.ml.annotation.gui.components.config;

import de.luk.ml.annotation.annotations.Annotation;
import de.luk.ml.annotation.annotations.AnnotationList;
import de.luk.ml.annotation.gui.components.common.ComponentController;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;


/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class AnnotationListComponent extends ComponentController {
  private static Logger logger = LoggerFactory.getLogger(AnnotationListComponent.class);
  @FXML
  private ListView<Annotation> lstAnnotations;
  @FXML
  private TextField txfName;
  @FXML
  private Button btnAdd;

  @Inject
  private AnnotationList annotations;

  @Inject
  private Instance<AnnotationListCell> listCells;

  @FXML
  private void addAnnotation() {
    if(!txfName.getText().isEmpty()){
      this.annotations.addAnnotation(new Annotation(txfName.getText()));
      this.txfName.clear();
      this.txfName.requestFocus();
    }
  }

  @PostConstruct
  private void init() {
    this.btnAdd.disableProperty().bind(Bindings.isEmpty(this.txfName.textProperty()));
    this.lstAnnotations.setItems(this.annotations.observable);
    this.lstAnnotations.setCellFactory(lstView -> listCells.get());
  }

}
