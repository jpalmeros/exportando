package com.empresa.oscar.exportando;

/**
 * Created by lord on 05/11/2014.
 */
public class Product {
    private int productId;
    private int productTypeId;
    private String productName;
    private int productAmount;
    Product(int productId,int productTypeId,int productAmount,String productName){
        this.productId=productId;
        this.productAmount=productAmount;
        this.productTypeId=productTypeId;
        this.productName=productName;
    }

    public int getProductId(){return productId;}
    public int getProductTypeId(){return  productTypeId;}
    public String getProductName(){return productName;}
    public int getProductAmount(){return  productAmount;}

}
