package io.github.androidflux.todolist.components;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.squareup.otto.Subscribe;
import io.github.androidflux.todolist.R;
import io.github.androidflux.todolist.actions.TodoActionCreator;
import io.github.androidflux.todolist.components.adapter.TodoAdapter;
import io.github.androidflux.todolist.dispatcher.Dispatcher;
import io.github.androidflux.todolist.stores.Store;
import io.github.androidflux.todolist.stores.TodoStore;

public class MainActivity extends AppCompatActivity {

  private RecyclerView dataList;
  private AppCompatEditText editor;
  private AppCompatButton add;
  private AppCompatButton completeAll;
  private AppCompatButton destroyComplete;

  private Dispatcher dispatcher;
  private TodoActionCreator actionCreator;
  private TodoStore store;

  private TodoAdapter todoAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    initDependencies();
    setupView();
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    dispatcher.unregister(store);
  }

  private void initDependencies() {
    store = TodoStore.getInstance();
    store.register(this);

    dispatcher = Dispatcher.getInstance();
    dispatcher.register(store);

    actionCreator = TodoActionCreator.getInstance(dispatcher);
  }

  private void setupView() {
    dataList = (RecyclerView) findViewById(R.id.rv);
    add = (AppCompatButton) findViewById(R.id.add_todo);
    completeAll = (AppCompatButton) findViewById(R.id.complete_all);
    destroyComplete = (AppCompatButton) findViewById(R.id.destroy_complete);
    editor = (AppCompatEditText) findViewById(R.id.editor);

    add.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        actionCreator.create(editor.getText().toString());
      }
    });
    completeAll.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        actionCreator.toggleCompleteAll();
      }
    });
    destroyComplete.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        actionCreator.destroyCompleted();
      }
    });

    dataList.setLayoutManager(new LinearLayoutManager(this));
    todoAdapter = new TodoAdapter(actionCreator, R.layout.item_todo);
    dataList.setAdapter(todoAdapter);
  }

  private void reader(TodoStore store) {
    todoAdapter.setDatas(store.getTodos());
    todoAdapter.notifyDataSetChanged();
  }

  @Subscribe public void onStoreChange(Store.StoreChangeEvent event) {
    if (event != null) {
      reader(store);
    }
  }
}
