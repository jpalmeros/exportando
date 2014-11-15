package com.empresa.oscar.exportando.object;

/**
 * Created by lord on 01/10/2014.
 */
public class Locacion{
    public int indice_locacion;
    public String nombre_locacion;
    public Locacion(int indice_locacion,String nombre_locacion) {
        this.indice_locacion=indice_locacion;
        this.nombre_locacion=nombre_locacion;
    }
    public int getIndiceLocacion(){
        return indice_locacion;
    }
    public String getNombreLocacion(){
        return nombre_locacion;
    }
}
