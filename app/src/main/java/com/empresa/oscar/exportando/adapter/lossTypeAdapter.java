package com.empresa.oscar.exportando.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.empresa.oscar.exportando.R;
import com.empresa.oscar.exportando.object.LossType;
import java.util.ArrayList;

public class lossTypeAdapter extends BaseAdapter
{
    protected Activity activity;
    protected ArrayList<LossType> items;

    public lossTypeAdapter(Activity activity, ArrayList<LossType> items) {
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
        return  items.get(position).getLossTypeId();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View vi=convertView;
        ViewHolderLossType viewhold;

        if(vi == null|| !(vi.getTag() instanceof ViewHolderLocacion)) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.imet_loss_type, null);

            viewhold=new ViewHolderLossType();
            viewhold.loss_texto=(TextView) vi.findViewById(R.id.item_loss_type);
        }
        else{
            viewhold = (ViewHolderLossType)vi.getTag();
        }

        LossType ls = items.get(position);
        viewhold.loss_texto.setText(ls.getLossTypeName());

        return(vi);
    }

}
