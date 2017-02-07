package de.luk.ml.annotation.persistence;

import de.luk.ml.annotation.annotations.Annotation;
import de.luk.ml.annotation.annotations.AnnotationList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.Objects;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
@Singleton
public class AnnotationTypeRepo {
  private static Logger logger = LoggerFactory.getLogger(AnnotationTypeRepo.class);
  private File file;
  private AnnotationList list;

  @PostConstruct
  private void postConstruct(){

  }

  @Produces
  public AnnotationList produceAnnotationlist(){
    if(Objects.isNull(list)){
      this.loadList();
    }
    return list;
  }

  private void loadList(){
    this.list = new AnnotationList();
    // TODO: Load annotation list from file
  }




  public static void main(String[] args){
    test();
  }

  public static void test() {
    try {
      AnnotationList list = new AnnotationList();
      list.addAnnotation(new Annotation("hallo").setIndex(10));
      list.addAnnotation(new Annotation("hallo 2").setIndex(12));
      list.addAnnotation(new Annotation("hallo 4").setIndex(14));
      JAXBContext context = JAXBContext.newInstance(AnnotationList.class);
      Marshaller m = context.createMarshaller();
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      m.marshal(list, System.out);
    } catch (Throwable e) {
      e.printStackTrace();
    }
  }
}
