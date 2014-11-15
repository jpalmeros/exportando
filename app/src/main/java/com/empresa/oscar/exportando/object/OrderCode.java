package com.empresa.oscar.exportando.object;

/**
 * Created by lord on 05/11/2014.
 */
public class OrderCode {
    public int codeId;
    public String codeSerial;
    public int productId;
    public int productType;
    public String productName;
    public int amount;
    public int codeOrderId;
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
