package com.empresa.oscar.exportando.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.empresa.oscar.exportando.LossVerificationActivity;
import com.empresa.oscar.exportando.R;
import com.empresa.oscar.exportando.object.Loss;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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

        final Loss ls = items.get(position);
        viewhold.orden_texto.setText(ls.getOrder());
        viewhold.locacion_texto.setText(ls.getLocation());
        viewhold.cantidad_texto.setText(Integer.toString(ls.getAmount()));
        viewhold.fecha_texto.setText(ls.getDate());
        viewhold.boton_atender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject lossObject=new JSONObject();
                try {
                    lossObject.put("loss_id", ls.getLossId());
                    lossObject.put("loss_order_id",ls.getOrderId());
                    lossObject.put("loss_order_token",ls.getOrder());
                    lossObject.put("loss_amount",ls.getAmount());
                    lossObject.put("loss_location_id",ls.getLocationId());
                    lossObject.put("loss_delivery_id",ls.getDelivery());
                    lossObject.put("loss_location_name",ls.getLocation());
                    lossObject.put("loss_user_id",ls.getUserId());
                    lossObject.put("loss_user_nickname",ls.getUser());
                    lossObject.put("loss_type_id",ls.getTypeId());
                    lossObject.put("loss_order_token",ls.getOrder());
                    lossObject.put("loss_type_name",ls.getType());
                    lossObject.put("loss_status",ls.getStatus());
                    lossObject.put("loss_date",ls.getDate());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent Storage_activity = new Intent(activity, LossVerificationActivity.class);
                Storage_activity.putExtra("loss",lossObject.toString());
                activity.startActivity(Storage_activity);
                activity.finish();
            }
        });

        return(vi);
    }

}
