package de.luk.ml.annotation.annotations;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
@XmlRootElement
public class Annotation {
  @XmlTransient
  private List<Consumer<Annotation>> removeCallbacks = new ArrayList<>();
  @XmlTransient
  public SimpleStringProperty name;
  @XmlTransient
  public SimpleIntegerProperty index;
  @XmlTransient
  public SimpleBooleanProperty selected;
  @XmlTransient
  public SimpleBooleanProperty active;

  public Annotation() {
    this("");
  }

  public Annotation(String name) {
    this.name = new SimpleStringProperty(name);
    this.index = new SimpleIntegerProperty(0);
    this.selected = new SimpleBooleanProperty(false);
    this.active = new SimpleBooleanProperty(false);
  }

  public void remove() {
    this.removeCallbacks.forEach(cb -> cb.accept(this));
    this.removeCallbacks.clear();
  }

  public Annotation onRemove(Consumer<Annotation> callback) {
    this.removeCallbacks.add(callback);
    return this;
  }

  public String getName() {
    return name.get();
  }

  public Annotation setName(String name) {
    this.name.set(name);
    return this;
  }

  public int getIndex() {
    return index.get();
  }

  public Annotation setIndex(int index) {
    this.index.set(index);
    return this;
  }

  public boolean isSelected() {
    return selected.get();
  }

  public Annotation setSelected(boolean selected) {
    this.selected.set(selected);
    return this;
  }

  public boolean isActive() {
    return active.get();
  }

  public Annotation setActive(boolean active) {
    this.active.set(active);
    return this;
  }
}
