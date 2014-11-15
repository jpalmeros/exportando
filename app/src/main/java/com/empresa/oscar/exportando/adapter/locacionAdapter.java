package com.empresa.oscar.exportando.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.empresa.oscar.exportando.R;
import com.empresa.oscar.exportando.object.Locacion;

import java.util.ArrayList;

/**
 * Created by lord on 14/11/2014.
 */
public class locacionAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Locacion> items;

    public locacionAdapter(Activity activity, ArrayList<Locacion> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return  items.get(position).getIndiceLocacion();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi=convertView;
        ViewHolderLocacion viewhold;

        if(vi == null|| !(vi.getTag() instanceof ViewHolderLocacion)) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.item_locacion, null);
            viewhold=new ViewHolderLocacion();
            viewhold.locacion_nombre=(TextView) vi.findViewById(R.id.item_locacion);
        }
        else{
            viewhold = (ViewHolderLocacion)vi.getTag();
        }
        Locacion loc = items.get(position);
        viewhold.locacion_nombre.setText(loc.getNombreLocacion());
        return(vi);
    }
}
