package de.luk.ml.annotation.gui.views.common;


import de.luk.ml.annotation.gui.components.common.ComponentController;
import de.luk.ml.annotation.gui.views.RootView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public abstract class ViewController extends ComponentController {
  private static Logger logger = LoggerFactory.getLogger(ViewController.class);
  public RootView root;
  private boolean loading = false;

  abstract public String getTitle();

  @Inject
  protected Instance<LoadingView> loadingView;

  public void setRootView(RootView root) {
    this.root = root;
    Arrays.stream(getClass().getDeclaredFields())
        .forEach(this::hasSetViewMethod);
  }

  private void hasSetViewMethod(Field field) {
    try {
      Method setView = field.getType().getMethod("setView", ViewController.class);
      field.setAccessible(true);
      setView.invoke(field.get(this), this);
    } catch (Throwable e) {
    }
  }

  public void detach() {
    Arrays.stream(getClass().getDeclaredFields())
        .forEach(this::hasDetachMethod);
  }

  private void hasDetachMethod(Field field) {
    try {
      Method detach = field.getType().getMethod("detach");
      field.setAccessible(true);
      detach.invoke(field.get(this));
    } catch (Throwable e) {
    }
  }

  public void attach() {
    Arrays.stream(getClass().getDeclaredFields())
        .forEach(this::hasAttachMethod);
  }

  private void hasAttachMethod(Field field) {
    try {
      Method attach = field.getType().getMethod("attach");
      field.setAccessible(true);
      attach.invoke(field.get(this));
    } catch (Throwable e) {
    }
  }

  public void setLoading(boolean loading) {
    this.loading = loading;
    if (this.loading) {
      root.setView(loadingView.get());
    } else {
      root.setView(this);
    }
  }

}
