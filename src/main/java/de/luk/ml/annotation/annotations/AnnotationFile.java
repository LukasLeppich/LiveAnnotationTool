package de.luk.ml.annotation.annotations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/8/17.
 */
public class AnnotationFile {
  File inputFile;
  private AnnotationFileLoader loader;
  private List<Annotation> annotations;
  private long start;
  private List<Consumer<AnnotationFile>> changeListeners = new ArrayList<>();


  public AnnotationFile(AnnotationFileLoader loader, File inputFile, List<Annotation> annotations, long start) {
    this.loader = loader;
    this.inputFile = inputFile;
    this.annotations = annotations;
    this.start = start;
  }

  public void recalculateTimes(long recordingStart) throws Exception {
    long diff = this.start - recordingStart;
    if (diff == 0) {
      return;
    }
    // TODO: Rollback on error
    this.annotations.forEach(annotation ->
        annotation.setStart(annotation.getStart() + diff)
            .setEnd(annotation.getEnd() + diff));
    this.start = recordingStart;
    this.loader.saveAnnotationFile(this);
    this.changeListeners.forEach(cb -> cb.accept(this));
  }

  public void addChangeListener(Consumer<AnnotationFile> callback) {
    this.changeListeners.add(callback);
  }

  public void removeChangeListener(Consumer<AnnotationFile> callback) {
    this.changeListeners.remove(callback);
  }

  public List<Annotation> getAnnotations() {
    return annotations;
  }

  public long getStart() {
    return start;
  }
}
