package com.empresa.oscar.exportando;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by UsuarioRasa on 25/10/2014.
 */
public class Purchase {
    private String proveedor;
    private String producto;
    private String fecha;
    private int cantidad;
    private int id_compra;
    Purchase(String proveedor,String producto,String fecha,int cantidad,int id_compra){
        this.producto=producto;
        this.proveedor=proveedor;
        this.cantidad=cantidad;
        this.id_compra=id_compra;
        this.fecha=fecha;

    }
    public String getProveedor(){
        return proveedor;
    }
    public String getProducto(){
        return producto;
    }public int getCantidad(){
        return cantidad;
    }
    public String getFecha(){
        return fecha;
    }
    public  int getId_compra(){
        return id_compra;
    }

}
class ViewHolderPurchase {
    TextView proveedor_texto;
    TextView producto_texto;
    TextView cantidad_texto;
    TextView fecha_texto;
}
class purchaseAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Purchase> items;

    public purchaseAdapter(Activity activity, ArrayList<Purchase> items) {
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
        return  items.get(position).getId_compra();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi=convertView;
        ViewHolderPurchase viewhold;

        if(vi == null|| !(vi.getTag() instanceof ViewHolderLocacion)) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.purchase_list_item, null);
            viewhold=new ViewHolderPurchase();
            viewhold.proveedor_texto=(TextView) vi.findViewById(R.id.proveedor);
            viewhold.producto_texto=(TextView) vi.findViewById(R.id.producto);
            viewhold.cantidad_texto=(TextView) vi.findViewById(R.id.cantidad);
            viewhold.fecha_texto=(TextView) vi.findViewById(R.id.fecha);
        }
        else{
            viewhold = (ViewHolderPurchase)vi.getTag();
        }
        Purchase pr = items.get(position);
        viewhold.proveedor_texto.setText(pr.getProveedor());
        viewhold.producto_texto.setText(pr.getProducto());
        viewhold.cantidad_texto.setText(Integer.toString(pr.getCantidad()));
        viewhold.fecha_texto.setText(pr.getFecha());
        return(vi);
    }
}
