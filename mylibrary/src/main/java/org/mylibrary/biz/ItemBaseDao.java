package org.mylibrary.biz;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ws on 2015/11/6.
 */
public abstract class ItemBaseDao {
    /**创建view*/
    public abstract View getView(LayoutInflater inflater,int i, View view, ViewGroup viewGrou,Object data);
}
