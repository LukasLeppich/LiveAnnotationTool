package de.luk.ml.annotation.recorder;

import de.luk.ml.annotation.annotations.Annotation;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/8/17.
 */
public class AnnotationRecorder {
  private static Logger logger = LoggerFactory.getLogger(AnnotationRecorder.class);
  private BufferedWriter output;

  private Annotation currentActive = null;
  private long currentActiveStart;

  private long startTime;

  public void startRecording(File outputFile) throws IOException {
    this.output = new BufferedWriter(new FileWriter(outputFile));
    this.output.write("\"#starttime\";\"#endtime\";\"all_tiers\"");
    this.startTime = System.currentTimeMillis();
    FileUtils.writeStringToFile(new File(outputFile.getParentFile(), outputFile.getName() + ".started"), Long.toString(this.startTime), "utf-8", false);
  }

  public void stopRecording() throws IOException {
    this.output.close();
  }

  public void startAnnotation(Annotation annotation) {
    this.currentActive = annotation;
    this.currentActiveStart = System.currentTimeMillis();
  }

  public void stopAnnotation(Annotation annotation) throws IOException {
    if (this.currentActiveStart == 0 || this.currentActive == null) {
      // TODO: Error handling.
      return;
    }
    double started = (currentActiveStart - startTime) / 1000.0;
    double ended = (System.currentTimeMillis() - startTime) / 1000.0;
    output.write(String.format(Locale.US, "\n%.3f;%.3f;\"" + annotation.getName() + "\"", started, ended));
    output.flush();
    this.currentActiveStart = 0;
    this.currentActive = null;
  }


}
