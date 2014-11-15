package com.empresa.oscar.exportando.object;

/**
 * Created by UsuarioRasa on 25/10/2014.
 */
public class Purchase {
    public String proveedor;
    public String producto;
    public String fecha;
    public int cantidad;
    public int id_compra;
    public Purchase(String proveedor, String producto, String fecha, int cantidad, int id_compra){
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

