package io.github.androidflux.todolist.dispatcher;

import com.squareup.otto.Bus;
import io.github.androidflux.todolist.actions.Action;
import io.github.androidflux.todolist.stores.Store;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dylanjiang on 15/12/29.
 */
public class Dispatcher {

  private static Dispatcher instance;
  private final List<Store> stores = new ArrayList<>();

  public static Dispatcher getInstance() {
    if (instance == null) {
      instance = new Dispatcher();
    }
    return instance;
  }

  Dispatcher() {
  }

  public void register(final Store store) {
    if (!stores.contains(store)) {
      stores.add(store);
    }
  }

  public void unregister(final Store store) {
    stores.remove(store);
  }

  public void dispatch(Action action) {
    post(action);
  }

  private void post(final Action action) {
    for (Store store : stores) {
      store.onAction(action);
    }
  }
}
