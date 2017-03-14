package de.luk.ml.annotation.utils;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 3/14/17.
 */
public class AnnotationFileUtils {
  public static final String FILE_NAME_FORMAT = "'entityRecording'-yyyy-MM-dd-HH-mm-ss";
  public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(FILE_NAME_FORMAT);

  public static LocalDateTime getDateTime(File file) {
    return getDateTime(file.getName());
  }

  public static LocalDateTime getDateTime(String name) {
    return LocalDateTime.parse(FilenameUtils.getBaseName(name), DATE_TIME_FORMATTER);
  }

  public static String getCurrentFileName(String extension) {
    return getFileName(LocalDateTime.now(), extension);
  }

  public static String getFileName(LocalDateTime time, String extension) {
    return time.format(DATE_TIME_FORMATTER) + "." + extension;
  }

  public static Optional<File> findNearestFile(File reference, String extension) {
    File parent = reference.getParentFile();
    List<String> names = Arrays.stream(parent.listFiles()).map(File::getName)
        .filter(name -> FilenameUtils.isExtension(name, extension)).collect(Collectors.toList());
    if (names.isEmpty()) {
      return Optional.empty();
    } else if (names.size() == 1) {
      return Optional.of(new File(parent, names.get(0)));
    } else {
      LocalDateTime referenceTime = getDateTime(reference);
      LocalDateTime bestFit = names.stream()
          .map(AnnotationFileUtils::getDateTime)
          .sorted(AnnotationFileUtils.compareDiffToReferenceTime(referenceTime))
          .findFirst().get();
      return Optional.of(new File(parent, getFileName(bestFit, extension)));
    }
  }

  private static Comparator<LocalDateTime> compareDiffToReferenceTime(LocalDateTime reference) {
    return (LocalDateTime a, LocalDateTime b) ->
        (int) (Math.abs(Duration.between(a, reference).getSeconds()) - Math.abs(Duration.between(b, reference).getSeconds()));

  }
}
