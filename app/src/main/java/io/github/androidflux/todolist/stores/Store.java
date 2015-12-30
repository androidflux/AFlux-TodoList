package io.github.androidflux.todolist.stores;

import com.squareup.otto.Bus;
import io.github.androidflux.todolist.actions.Action;

/**
 * Created by dylanjiang on 15/12/29.
 */
public abstract class Store<T extends Action> {
  private static final Bus bus = new Bus();

  protected Store() {
  }

  public void register(final Object view) {
    this.bus.register(view);
  }

  public void unregister(final Object view) {
    this.bus.unregister(view);
  }

  void emitStoreChange() {
    this.bus.post(changeEvent());
  }

  public abstract StoreChangeEvent changeEvent();

  public abstract void onAction(T action);

  public class StoreChangeEvent {
  }
}
