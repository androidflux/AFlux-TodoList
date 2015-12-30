package io.github.androidflux.todolist.actions;

import io.github.androidflux.todolist.constants.TodoConstants;
import io.github.androidflux.todolist.dispatcher.Dispatcher;
import io.github.androidflux.todolist.model.Todo;

/**
 * Created by dylanjiang on 15/12/29.
 */
public class TodoActionCreator implements ActionCreator<ArrayMapAction> {
  private static TodoActionCreator instance;
  private Dispatcher dispatcher;

  private TodoActionCreator(Dispatcher dispatcher) {
    this.dispatcher = dispatcher;
  }

  public static TodoActionCreator getInstance(Dispatcher dispatcher) {
    if (null == instance) {
      instance = new TodoActionCreator(dispatcher);
    }
    return instance;
  }

  public void create(String text) {
    dispatcher.dispatch(
        createAction(
            TodoConstants.TODO_CREATE,
            TodoConstants.KEY_MESSAGE, text
        ));
  }

  public void updateText(long id, String text) {
    dispatcher.dispatch(
        createAction(
            TodoConstants.TODO_UPDATE_TEXT,
            TodoConstants.KEY_ID, id,
            TodoConstants.KEY_MESSAGE, text
        ));
  }

  public void toggleComplete(Todo todo) {
    String type = todo.completed ?
        TodoConstants.TODO_UNDO_COMPLETE :
        TodoConstants.TODO_COMPLETE;
    dispatcher.dispatch(
        createAction(type,
            TodoConstants.KEY_ID, todo.id
        ));
  }

  public void toggleCompleteAll() {
    dispatcher.dispatch(createAction(TodoConstants.TODO_TOGGLE_COMPLETE_ALL));
  }

  public void destroy(long id) {
    dispatcher.dispatch(createAction(TodoConstants.TODO_DESTROY,
        TodoConstants.KEY_ID, id));
  }

  public void destroyCompleted() {
    dispatcher.dispatch(createAction(TodoConstants.TODO_DESTROY_COMPLETED));
  }

  @Override public ArrayMapAction createAction(String type, Object... params) {
    ArrayMapAction action = new ArrayMapAction();
    action.init(type, params);
    return action;
  }
}
