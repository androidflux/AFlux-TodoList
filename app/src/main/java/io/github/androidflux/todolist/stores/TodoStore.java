package io.github.androidflux.todolist.stores;

import android.text.TextUtils;
import com.squareup.otto.Subscribe;
import io.github.androidflux.todolist.actions.ArrayMapAction;
import io.github.androidflux.todolist.constants.TodoConstants;
import io.github.androidflux.todolist.model.Todo;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dylanjiang on 15/12/30.
 */
public class TodoStore extends Store<ArrayMapAction> {

  private static TodoStore instance;
  private final List<Todo> todos;
  private Todo lastDeleted;

  protected TodoStore() {
    todos = new ArrayList<>();
  }

  public static TodoStore getInstance() {
    if (instance == null) {
      instance = new TodoStore();
    }
    return instance;
  }

  public List<Todo> getTodos() {
    return todos;
  }

  public boolean canUndo() {
    return lastDeleted != null;
  }

  @Subscribe
  @Override public void onAction(ArrayMapAction action) {
    String message = "";
    long id = 0;
    if (containsKey(action, TodoConstants.KEY_MESSAGE)) {
      message = action
          .getDatas().get(TodoConstants.KEY_MESSAGE).
          toString().trim();
    }
    if (containsKey(action, TodoConstants.KEY_ID)) {
      id = (long) action.getDatas().get(TodoConstants.KEY_ID);
    }

    switch (action.getType()) {
      case TodoConstants.TODO_CREATE:
        if (!TextUtils.isEmpty(message)) {
          create(message);
          emitStoreChange();
        }
        break;
      case TodoConstants.TODO_TOGGLE_COMPLETE_ALL:
        updateCompleteAll();
        emitStoreChange();
        break;
      case TodoConstants.TODO_UNDO_COMPLETE:
        updateComplete(id, false);
        emitStoreChange();
        break;
      case TodoConstants.TODO_COMPLETE:
        updateComplete(id, true);
        emitStoreChange();
        break;

      case TodoConstants.TODO_UPDATE_TEXT:
        updateText(id, message);
        emitStoreChange();
        break;
      case TodoConstants.TODO_DESTROY:
        destroy(id);
        emitStoreChange();
        break;
      case TodoConstants.TODO_DESTROY_COMPLETED:
        destroyCompleted();
        emitStoreChange();
        break;

      default:
        // no op
    }
  }

  private boolean containsKey(ArrayMapAction action, String key) {
    if (null == action.getDatas()) {
      return false;
    }else {
      return action.getDatas().containsKey(key);
    }
  }

  private void updateText(long id, String message) {
    Todo todo = getById(id);
    if (todo != null) {
      todo.message = message;
    }
  }

  private void destroyCompleted() {
    Iterator<Todo> iter = todos.iterator();
    while (iter.hasNext()) {
      Todo todo = iter.next();
      if (todo.completed) {
        iter.remove();
      }
    }
  }

  private void updateCompleteAll() {
    if (areAllComplete()) {
      updateAllComplete(false);
    } else {
      updateAllComplete(true);
    }
  }

  private boolean areAllComplete() {
    for (Todo todo : todos) {
      if (!todo.completed) {
        return false;
      }
    }
    return true;
  }

  private void updateAllComplete(boolean complete) {
    for (Todo todo : todos) {
      todo.completed = complete;
    }
  }

  private void updateComplete(long id, boolean complete) {
    Todo todo = getById(id);
    if (todo != null) {
      todo.completed = complete;
    }
  }

  private void create(String text) {
    long id = System.currentTimeMillis();
    Todo todo = new Todo(id, text, false);
    addElement(todo);
  }

  private void destroy(long id) {
    Iterator<Todo> iter = todos.iterator();
    while (iter.hasNext()) {
      Todo todo = iter.next();
      if (todo.id == id) {
        lastDeleted = todo.clone();
        iter.remove();
        break;
      }
    }
  }

  private Todo getById(long id) {
    Iterator<Todo> iter = todos.iterator();
    while (iter.hasNext()) {
      Todo todo = iter.next();
      if (todo.id == id) {
        return todo;
      }
    }
    return null;
  }

  private void addElement(Todo clone) {
    todos.add(clone);
  }

  @Override public StoreChangeEvent changeEvent() {
    return new StoreChangeEvent();
  }
}
