package de.luk.ml.annotation.annotations;

import com.google.common.base.Charsets;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/8/17.
 */
public class AnnotationFileLoader {
  private static Logger logger = LoggerFactory.getLogger(AnnotationFileLoader.class);
  private Pattern doublePattern = Pattern.compile("^\\d*(\\.?\\d+)$");

  public AnnotationFile loadAnnotationFile(File inputFile) throws Exception {
    List<String> lines = FileUtils.readLines(inputFile, Charsets.UTF_8);
    logger.debug("Loaded " + lines.size() + " lines from [" + inputFile.getName() + "]");
    List<Annotation> annotations = lines.stream().skip(1)
        .map(line -> line.split(";"))
        .map(this::createAnnotation)
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    logger.debug("Loaded " + annotations.size() + " annotations");
    return new AnnotationFile(this, inputFile, annotations, loadStarted(inputFile));
  }

  public void saveAnnotationFile(AnnotationFile file) throws Exception {
    List<String> lines = file.getAnnotations().stream()
        .map(annotation -> String.format("%.3f;%.3f;\"" + annotation.getName().replaceAll("\"", "") + "\"",
            annotation.getStart() / 1000.0,
            annotation.getEnd() / 1000.0))
        .collect(Collectors.toList());
    lines.add(0, "\"#starttime\";\"#endtime\";\"all_tiers\"");
    FileUtils.writeLines(file.inputFile, Charsets.UTF_8.name(), lines, "\n");
    FileUtils.writeStringToFile(new File(file.inputFile.getParentFile(), file.inputFile.getName() + ".started"), Long.toString(file.getStart()), "utf-8", false);
  }

  private long loadStarted(File inputFile) throws Exception {
    File startFile = new File(inputFile.getParent(), inputFile.getName() + ".started");
    if (!startFile.exists()) {
      logger.error("Start file not found");
      return -1;
    }
    List<String> lines = FileUtils.readLines(startFile, Charsets.UTF_8);
    if (lines.isEmpty()) {
      logger.error("Start file was empty!");
      return -1;
    }
    return lines.stream()
        .filter(line -> line.matches("^\\d+$"))
        .mapToLong(line -> Long.parseLong(line))
        .findFirst()
        .orElseGet(() -> -1);
  }

  private Annotation createAnnotation(String[] csvLine) {
    if (csvLine.length < 3) {
      logger.error("CSV line with less than 3 elements: " + Arrays.stream(csvLine).collect(Collectors.joining(";")));
      return null;
    }
    String start = csvLine[0];
    String end = csvLine[1];
    String name = csvLine[2];
    if (!doublePattern.matcher(start).matches()) {
      logger.error("Unknown start format, expected decimal but found: " + start);
      return null;
    }
    if (!doublePattern.matcher(start).matches()) {
      logger.error("Unknown end format, expected decimal but found: " + end);
      return null;
    }
    return new Annotation(name).setStart(toLong(start)).setEnd(toLong(end));
  }

  private Long toLong(String time) {
    double doubleValue = Double.parseDouble(time);
    return Math.round(doubleValue * 1000.0);
  }

}
