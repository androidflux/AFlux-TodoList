package io.github.androidflux.todolist.actions;


/**
 * Created by dylanjiang on 15/12/29.
 */
public interface Action<P> {

  void init(String type, Object... datas);

  P getDatas();

  String getType();

}
