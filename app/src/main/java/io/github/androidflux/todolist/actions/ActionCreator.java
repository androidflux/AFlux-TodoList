package io.github.androidflux.todolist.actions;

/**
 * Created by dylanjiang on 15/12/30.
 */
public interface ActionCreator<T extends Action> {
  T createAction(String type, Object... params);
}
