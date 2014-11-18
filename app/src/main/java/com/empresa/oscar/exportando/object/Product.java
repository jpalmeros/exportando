package com.empresa.oscar.exportando.object;

/**
 * Created by lord on 05/11/2014.
 */
public class Product {
    public int codeId;
    public String codeValueSerial;
    public int productId;
    public int productTypeId;
    public String productName;
    public int productAmount;
    public Product(int codeId,int productId, int productTypeId, int productAmount, String productName,String codeValueSerial){
        this.codeId=codeId;
        this.productId=productId;
        this.productAmount=productAmount;
        this.productTypeId=productTypeId;
        this.productName=productName;
        this.codeValueSerial=codeValueSerial;
    }

    public int getCodeId(){return codeId;}
    public int getProductId(){return productId;}
    public int getProductTypeId(){return  productTypeId;}
    public String getProductName(){return productName;}
    public int getProductAmount(){return  productAmount;}
    public String getCodeValueSerial(){
        return codeValueSerial;
    }

}
