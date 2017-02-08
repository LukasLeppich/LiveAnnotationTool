package de.luk.ml.annotation.annotations;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

  public Annotation() {
    this("");
  }

  public Annotation(String name) {
    this.name = new SimpleStringProperty(name);
    this.index = new SimpleIntegerProperty(0);
  }

  public void remove() {
    this.removeCallbacks.forEach(cb -> cb.accept(this));
    this.removeCallbacks.clear();
  }

  public Annotation onRemove(Consumer<Annotation> callback) {
    this.removeCallbacks.add(callback);
    return this;
  }

  @Override
  public String toString() {
    return Integer.toString(this.index.get() + 1) + ": " + this.getName();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Annotation)) return false;
    Annotation that = (Annotation) o;
    return Objects.equals(getName(), that.getName()) &&
        Objects.equals(getIndex(), that.getIndex());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getName(), getIndex());
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

}
