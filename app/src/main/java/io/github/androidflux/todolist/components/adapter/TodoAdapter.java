package io.github.androidflux.todolist.components.adapter;

import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatEditText;
import android.text.ParcelableSpan;
import android.text.SpannableString;
import android.text.style.StrikethroughSpan;
import android.view.DragEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import io.github.androidflux.todolist.R;
import io.github.androidflux.todolist.actions.TodoActionCreator;
import io.github.androidflux.todolist.model.Todo;

/**
 * Created by dylanjiang on 15/12/30.
 */
public class TodoAdapter extends AbsAdapter<Todo, TodoActionCreator> {

  public TodoAdapter(@NonNull TodoActionCreator actionCreator, @NonNull int... layoutId) {
    super(actionCreator, layoutId);
  }

  @Override public int getItemViewType(int position) {
    return 0;
  }

  @Override public void onBindViewHolder(AbsViewHolder holder, int position) {
    final Todo todo = datas.get(position);

    SpannableString spanString = new SpannableString(todo.message);
    ParcelableSpan span = new StrikethroughSpan();
    if (todo.completed) {
      spanString.setSpan(span, 0, spanString.length(), 0);
    } else {
      spanString.removeSpan(span);
    }
    holder.setText(R.id.item_todo_msg, spanString);

    CheckBox completeChecker = holder.getView(R.id.item_todo_complete);
    holder.setClickable(!todo.completed);
    holder.setCheck(R.id.item_todo_complete, todo.completed);
    completeChecker.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        actionCreator.toggleComplete(todo);
      }
    });

    holder.setOnLongClickListener(new View.OnLongClickListener() {
      @Override public boolean onLongClick(View v) {
        actionCreator.destroy(todo.id);
        return true;
      }
    });

    final AppCompatEditText editor = new AppCompatEditText(context);
    holder.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        editor.setText(todo.message);
        AppCompatDialog dialog = new AlertDialog.Builder(context)
            .setTitle("Edit")
            .setView(editor)
            .setPositiveButton("Done", new DialogInterface.OnClickListener() {
              @Override public void onClick(DialogInterface dialog, int which) {
                actionCreator.updateText(todo.id, editor.getText().toString().intern());
              }
            })
            .setNegativeButton("Cancel", null)
            .show();

        dialog.show();
      }
    });
  }
}
