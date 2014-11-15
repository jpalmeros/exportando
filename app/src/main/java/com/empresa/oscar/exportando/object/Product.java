package com.empresa.oscar.exportando.object;

/**
 * Created by lord on 05/11/2014.
 */
public class Product {
    public int productId;
    public int productTypeId;
    public String productName;
    public int productAmount;
    public Product(int productId, int productTypeId, int productAmount, String productName){
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
