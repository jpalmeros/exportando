package com.empresa.oscar.exportando.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.empresa.oscar.exportando.R;
import com.empresa.oscar.exportando.object.Loss;

import java.util.ArrayList;

/**
 * Created by lord on 14/11/2014.
 */
public class lossAdapter extends BaseAdapter
{
    protected Activity activity;
    protected ArrayList<Loss> items;

    public lossAdapter(Activity activity, ArrayList<Loss> items) {
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
        return  items.get(position).getLossId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi=convertView;
        ViewHolderLoss viewhold;

        if(vi == null|| !(vi.getTag() instanceof ViewHolderLocacion)) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.loss_list_item, null);

            viewhold=new ViewHolderLoss();
            viewhold.orden_texto=(TextView) vi.findViewById(R.id.order);
            viewhold.cantidad_texto=(TextView) vi.findViewById(R.id.unidades);
            viewhold.locacion_texto=(TextView) vi.findViewById(R.id.locacion);
            viewhold.fecha_texto=(TextView) vi.findViewById(R.id.fecha);
            viewhold.boton_atender=(Button)vi.findViewById(R.id.atender);
        }
        else{
            viewhold = (ViewHolderLoss)vi.getTag();
        }

        Loss ls = items.get(position);
        viewhold.orden_texto.setText(ls.getOrder());
        viewhold.locacion_texto.setText(ls.getLocation());
        viewhold.cantidad_texto.setText(Integer.toString(ls.getAmount()));
        viewhold.fecha_texto.setText(ls.getDate());
        viewhold.boton_atender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



            }
        });

        return(vi);
    }

}
