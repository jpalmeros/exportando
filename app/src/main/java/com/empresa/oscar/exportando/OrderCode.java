package com.empresa.oscar.exportando;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by lord on 05/11/2014.
 */
public class OrderCode {
    int codeId;
    String codeSerial;
    int productId;
    int productType;
    String productName;
    int amount;
    int codeOrderId;
    OrderCode(int codeOrderId,int codeId,String codeSerial,int productId,int productType,String productName,int amount){
        this.codeOrderId=codeOrderId;
        this. codeId=codeId;
        this.codeSerial=codeSerial;
        this.productName=productName;
        this.productId=productId;
        this.productType=productType;
        this.amount=amount;
    }
}
class ViewHolderOrderCode {
    TextView codeSerial_texto;
    TextView producto_texto;
    TextView cantida_texto;
    Button boton_borrar;
}
class orderCodeAdapter extends BaseAdapter
{
    protected Activity activity;
    protected ArrayList<OrderCode> items;

    public orderCodeAdapter(Activity activity, ArrayList<OrderCode> items) {
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
        return items.get(position).codeOrderId;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi=convertView;
        ViewHolderOrderCode viewhold;

        if(vi == null|| !(vi.getTag() instanceof ViewHolderLocacion)) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.loss_list_item, null);

            viewhold=new ViewHolderOrderCode();
            viewhold.codeSerial_texto=(TextView) vi.findViewById(R.id.serial);
            viewhold.producto_texto=(TextView) vi.findViewById(R.id.producto);
            viewhold.cantida_texto=(TextView) vi.findViewById(R.id.cantidad);
            viewhold.boton_borrar=(Button)vi.findViewById(R.id.borrar);
        }
        else{
            viewhold = (ViewHolderOrderCode)vi.getTag();
        }

        OrderCode oc = items.get(position);
        viewhold.codeSerial_texto.setText(oc.codeSerial);
        viewhold.producto_texto.setText(oc.productName);
        viewhold.cantida_texto.setText(oc.amount);
        viewhold.boton_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final OrderCode actualOrderCode=items.get(position);

                new AlertDialog.Builder(activity)
                        .setTitle(R.string.RecepcionCompra)
                        .setMessage(R.string.ConfirmaRecepcionCompra)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String exito="";
                                // continue with delete


                                Log.e("Confirma", "Borrar Order Code");

                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                                Log.e("Cancela", "Borrar Order Code" );
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();

            }
        });

        return(vi);
    }

}