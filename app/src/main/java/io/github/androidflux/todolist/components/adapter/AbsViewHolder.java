package io.github.androidflux.todolist.components.adapter;

import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by DylanJiang on 2015/12/30.
 */
public class AbsViewHolder extends RecyclerView.ViewHolder {

  private SparseArray<View> views;
  private View itemView;
  private View.OnClickListener onClickListener;
  private View.OnLongClickListener onLongClickListener;

  public AbsViewHolder(View itemView) {
    super(itemView);
    this.itemView = itemView;
    views = new SparseArray<>();
  }

  public <T extends View> T getView(int id) {
    View view;
    if (null != views.get(id)) {
      view = views.get(id);
    } else {
      view = itemView.findViewById(id);
      views.put(id, view);
    }
    return (T) view;
  }

  public AbsViewHolder setText(int id, CharSequence charSequence) {
    View view;
    TextView textView;
    if (charSequence == null) {
      return this;
    }

    if (null != (view = getView(id))) {
      textView = (TextView) view;
      textView.setText(charSequence);
    }
    return this;
  }

  public AbsViewHolder setCheck(int id, boolean checked) {
    View view;
    CheckBox checkBox;
    if (null != (view = getView(id))) {
      checkBox = (CheckBox) view;
      checkBox.setChecked(checked);
    }

    return this;
  }

  public void setClickable(boolean clickable){
    itemView.setClickable(clickable);
  }

  public void setOnLongClickListener(View.OnLongClickListener onLongClickListener) {
    this.onLongClickListener = onLongClickListener;
    if (onLongClickListener != null) {
      itemView.setOnLongClickListener(this.onLongClickListener);
    }
  }

  public void setOnClickListener(View.OnClickListener onClickListener) {
    this.onClickListener = onClickListener;
    if (onClickListener != null) {
      itemView.setOnClickListener(this.onClickListener);
    }
  }

}
