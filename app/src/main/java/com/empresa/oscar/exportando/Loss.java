package com.empresa.oscar.exportando;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by UsuarioRasa on 05/11/2014.
 */
public class Loss {
    private String locacion_name;
    private int locacion_id;
    private String tipo_name;
    private int tipo_id;
    private String usuario_name;
    private int usuario_id;
    private int cantidad;
    private String order_token;
    private int order_id;
    private int merma_id;
    private boolean status;
    private String fecha;
    private int delivery_id;
    Loss(int order_id,String orden_token,int locacion_id,String locacion_name,
         int tipo_id, String tipo_name, int usuario_id, String usuario_name, int cantidad,
         int merma_id,boolean status,String fecha,int delivery_id){
        this.order_id=order_id;
        this.order_token=orden_token;
        this.locacion_id=locacion_id;
        this.locacion_name=locacion_name;
        this.tipo_id=tipo_id;
        this.tipo_name=tipo_name;
        this.cantidad=cantidad;
        this.merma_id=merma_id;
        this.usuario_id=usuario_id;
        this.usuario_name=usuario_name;
        this.status=status;
        this.fecha=fecha;
        this.delivery_id=delivery_id;
    }

    public int getDelivery(){return delivery_id;}
    public String getDate(){
        return fecha;
    }
    public int getOrderId(){return order_id;}
    public String getOrder(){
        return order_token;
    }
    public int getLocationId(){
        return locacion_id;
    }
    public String getLocation(){
        return locacion_name;
    }
    public int getAmount(){
        return cantidad;
    }
    public int getTypeId(){return tipo_id;}
    public String getType(){
        return tipo_name;
    }
    public int getLossId(){
        return merma_id;
    }
    public  int getUserId(){return usuario_id;}
    public String getUser(){
        return usuario_name;
    }
    public  boolean getStatus(){
        return status;
    }


}
class ViewHolderLoss {
    TextView orden_texto;
    TextView cantidad_texto;
    TextView locacion_texto;
    TextView fecha_texto;
    TextView tipo_texto;
    Button boton_atender;
}

class lossAdapter extends BaseAdapter
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
            viewhold.orden_texto=(TextView) vi.findViewById(R.id.orden);
            viewhold.cantidad_texto=(TextView) vi.findViewById(R.id.cantidad);
            viewhold.locacion_texto=(TextView) vi.findViewById(R.id.locacion);
            viewhold.fecha_texto=(TextView) vi.findViewById(R.id.fecha);
            viewhold.tipo_texto=(TextView) vi.findViewById(R.id.tipo);
            viewhold.boton_atender=(Button)vi.findViewById(R.id.atender);
        }
        else{
            viewhold = (ViewHolderLoss)vi.getTag();
        }

        Loss ls = items.get(position);
        viewhold.orden_texto.setText(ls.getOrder());
        viewhold.tipo_texto.setText(ls.getType());
        viewhold.locacion_texto.setText(ls.getLocation());
        viewhold.cantidad_texto.setText(Integer.toString(ls.getAmount()));
        viewhold.fecha_texto.setText(ls.getDate());
        viewhold.boton_atender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final Loss actualloss=items.get(position);

                new AlertDialog.Builder(activity)
                        .setTitle(R.string.RecepcionCompra)
                        .setMessage(R.string.ConfirmaRecepcionCompra)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String exito="";
                                // continue with delete


                                Log.e("Atendiendo", "Merma " + Integer.toString(actualloss.getLossId()));

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                Log.e("Cancela", "merma " + Integer.toString(actualloss.getLossId()));
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        return(vi);
    }

}

