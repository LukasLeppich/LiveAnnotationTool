package de.luk.ml.annotation.gui.views.common;


import de.luk.ml.annotation.gui.components.common.ComponentController;
import de.luk.ml.annotation.gui.views.RootView;

import javax.enterprise.inject.Instance;
import javax.inject.Inject;

/**
 * Created by Lukas Leppich (lukas.leppich@gmail.com) on 2/7/17.
 */
public class ViewController extends ComponentController {
  public RootView root;
  protected boolean loading = false;

  @Inject
  protected Instance<LoadingView> loadingView;

  public void setRootView(RootView root) {
    this.root = root;
  }

  public void detach(){

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
