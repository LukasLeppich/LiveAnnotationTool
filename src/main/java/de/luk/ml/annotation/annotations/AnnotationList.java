package de.luk.ml.annotation.annotations;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.enterprise.inject.Alternative;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
@XmlRootElement(name = "annotations")
@XmlAccessorType(XmlAccessType.NONE)
@Alternative
public class AnnotationList {

  public ObservableList<Annotation> observable = FXCollections.observableArrayList();

  public AnnotationList addAnnotation(Annotation annotation) {
    this.observable.add(annotation.onRemove(this::removeAnnotation)
        .setIndex(this.observable.size()));
    return this;
  }

  @XmlElement(name = "annotation")
  public List<Annotation> getAnnotations() {
    return this.observable.stream().collect(Collectors.toList());
  }

  public void setAnnotations(List<Annotation> annotations) {
    this.observable.setAll(annotations);
    this.observable.sort(Comparator.comparingInt(Annotation::getIndex));
  }

  private void removeAnnotation(Annotation annotation) {
    this.observable.remove(annotation);
  }

}
