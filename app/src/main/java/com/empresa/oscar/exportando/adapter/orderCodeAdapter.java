package com.empresa.oscar.exportando.adapter;

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

import com.empresa.oscar.exportando.R;
import com.empresa.oscar.exportando.object.OrderCode;

import java.util.ArrayList;

/**
 * Created by lord on 14/11/2014.
 */
public class orderCodeAdapter extends BaseAdapter
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
            vi = inflater.inflate(R.layout.order_code_list_item, null);

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
        viewhold.cantida_texto.setText(Integer.toString(oc.amount));
        viewhold.boton_borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final OrderCode actualOrderCode=items.get(position);

                new AlertDialog.Builder(activity)
                        .setTitle(R.string.BorrarProducto)
                        .setMessage(R.string.ConfirmaBorrarProducto)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                String exito="";
                                items.remove(position);
                                notifyDataSetChanged();
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
