package de.luk.ml.annotation.annotations;

import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 3/9/17.
 */
public class ElanEAFGenerator {
  private List<Annotation> annotations;
  private Optional<File> mediaFile;

  public ElanEAFGenerator(List<Annotation> annotations) {
    this.annotations = annotations;
  }

  public ElanEAFGenerator setMediaFile(File mediaFile) {
    this.mediaFile = Optional.of(mediaFile);
    return this;
  }


  public void writeToFile(File output) throws Exception {

    XMLOutputFactory factory = XMLOutputFactory.newFactory();
    XMLStreamWriter writer = factory.createXMLStreamWriter(
        new OutputStreamWriter(new FileOutputStream(output), StandardCharsets.UTF_8));

    writer.writeStartDocument("UTF-8", "1.0");

    writer.writeStartElement("ANNOTATION_DOCUMENT");

    writer.writeAttribute("xmlns:xsi", "http://www.w3.org/2001/XMLSchema-instance");
    writer.writeAttribute("xsi:noNamespaceSchemaLocation", "http://www.mpi.nl/tools/elan/EAFv2.8.xsd");
    writer.writeAttribute("DATE", ZonedDateTime.now().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME));
    writer.writeAttribute("FORMAT", "2.8");
    writer.writeAttribute("VERSION", "2.8");

    writeHeader(writer);
    writeTimeOrder(writer);
    writeTier(writer);

    writer.writeEmptyElement("LINGUISTIC_TYPE");
    writer.writeAttribute("GRAPHIC_REFERENCES", "false");
    writer.writeAttribute("LINGUISTIC_TYPE_ID", "default-lt");
    writer.writeAttribute("TIME_ALIGNABLE", "true");

    writer.writeEmptyElement("LINGUISTIC_TYPE");
    writer.writeAttribute("GRAPHIC_REFERENCES", "false");
    writer.writeAttribute("LINGUISTIC_TYPE_ID", "default");
    writer.writeAttribute("TIME_ALIGNABLE", "true");

    writer.writeEmptyElement("LOCALE");
    writer.writeAttribute("COUNTRY_CODE", "EN");
    writer.writeAttribute("LANGUAGE_CODE", "us");
    writer.writeAttribute("TIME_ALIGNABLE", "true");

    writer.writeEmptyElement("CONSTRAINT");
    writer.writeAttribute("DESCRIPTION", "Time subdivision of parent annotation's time interval, no time gaps allowed within this interval");
    writer.writeAttribute("STEREOTYPE", "Time_Subdivision");

    writer.writeEmptyElement("CONSTRAINT");
    writer.writeAttribute("DESCRIPTION", "Symbolic subdivision of a parent annotation. Annotations refering to the same parent are ordered");
    writer.writeAttribute("STEREOTYPE", "Symbolic_Subdivision");

    writer.writeEmptyElement("CONSTRAINT");
    writer.writeAttribute("DESCRIPTION", "1-1 association with a parent annotation");
    writer.writeAttribute("STEREOTYPE", "Symbolic_Association");

    writer.writeEmptyElement("CONSTRAINT");
    writer.writeAttribute("DESCRIPTION", "Time alignable annotations within the parent annotation's time interval, gaps are allowed");
    writer.writeAttribute("STEREOTYPE", "Included_In");

    writer.writeEndElement();
    writer.writeEndDocument();
    writer.flush();
    writer.close();
  }

  private void writeTier(XMLStreamWriter writer) throws Exception {
    writer.writeStartElement("TIER");
    writer.writeAttribute("DEFAULT_LOCALE", "us");
    writer.writeAttribute("LINGUISTIC_TYPE_REF", "default");
    writer.writeAttribute("TIER_ID", "\"all_tiers\"");
    int timeId = 1;
    int annotationId = 1;
    for (Annotation annotation : annotations) {
      writer.writeStartElement("ANNOTATION");

      writer.writeStartElement("ALIGNABLE_ANNOTATION");
      writer.writeAttribute("ANNOTATION_ID", "a" + (annotationId++));
      writer.writeAttribute("TIME_SLOT_REF1", "ts" + (timeId++));
      writer.writeAttribute("TIME_SLOT_REF2", "ts" + (timeId++));

      writer.writeStartElement("ANNOTATION_VALUE");

      writer.writeCharacters(annotation.getName());

      writer.writeEndElement();

      writer.writeEndElement();

      writer.writeEndElement();
    }

    writer.writeEndElement();
  }

  private void writeTimeOrder(XMLStreamWriter writer) throws Exception {
    writer.writeStartElement("TIME_ORDER");

    int index = 1;
    for (Annotation annotation : annotations) {
      writer.writeEmptyElement("TIME_SLOT");
      writer.writeAttribute("TIME_SLOT_ID", "ts" + index++);
      writer.writeAttribute("TIME_VALUE", Long.toString(annotation.getStart()));

      writer.writeEmptyElement("TIME_SLOT");
      writer.writeAttribute("TIME_SLOT_ID", "ts" + index++);
      writer.writeAttribute("TIME_VALUE", Long.toString(annotation.getEnd()));
    }

    writer.writeEndElement();
  }

  private void writeHeader(XMLStreamWriter writer) throws Exception {
    writer.writeStartElement("HEADER");
    writer.writeAttribute("TIME_UNITS", "milliseconds");

    if (mediaFile.isPresent()) {
      writer.writeEmptyElement("MEDIA_DESCRIPTOR");
      writer.writeAttribute("MEDIA_URL", "file:///" + mediaFile.get().getAbsolutePath());
      writer.writeAttribute("MIME_TYPE", "video/mp4");
      writer.writeAttribute("RELATIVE_MEDIA_URL", "./" + mediaFile.get().getName());
    }
    writer.writeStartElement("PROPERTY");
    writer.writeAttribute("NAME", "lastUsedAnnotationId");
    writer.writeCharacters(Integer.toString(this.annotations.size()));
    writer.writeEndElement();

    writer.writeEndElement();
  }
}
