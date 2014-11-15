package com.empresa.oscar.exportando.object;

/**
 * Created by UsuarioRasa on 05/11/2014.
 */
public class Loss {
    public String locacion_name;
    public int locacion_id;
    public String tipo_name;
    public int tipo_id;
    public String usuario_name;
    public int usuario_id;
    public int cantidad;
    public String order_token;
    public int order_id;
    public int merma_id;
    public boolean status;
    public String fecha;
    public int delivery_id;
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

