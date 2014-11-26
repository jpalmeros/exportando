package com.empresa.oscar.exportando.object;

public class LossType {
    public int lossTypeId;
    public String lossTypeName;
    public LossType(int lossTypeId,String lossTypeName){
        this.lossTypeId=lossTypeId;
        this.lossTypeName=lossTypeName;
    }
    public int getLossTypeId() {
        return lossTypeId;
    }

    public String getLossTypeName() {
        return lossTypeName;
    }
}
