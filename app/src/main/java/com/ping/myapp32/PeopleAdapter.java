package com.ping.myapp32;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by 2-1Ping on 2016/9/5.
 */
public class PeopleAdapter extends ArrayAdapter<NamePeople> {
    private int resourceId;
    public PeopleAdapter(Context context,int textViewResourceId,List<NamePeople> objects){
            super(context,textViewResourceId,objects);
        resourceId=textViewResourceId;
    }
    @Override
    public View getView(int position,View convertView,ViewGroup parent){
        NamePeople namePeople=getItem(position);
        View view;
        ViewHolder viewHolder;
        if (convertView==null){
            view= LayoutInflater.from(getContext()).inflate(resourceId,null);
            viewHolder=new ViewHolder();
            viewHolder.PeopleImage=(ImageView)view.findViewById(R.id.add);
            viewHolder.PeopleName=(TextView)view.findViewById(R.id.viewspot);
            view.setTag(viewHolder);

        }else{
            view = convertView;
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.PeopleImage.setImageResource(namePeople.getId());
        viewHolder.PeopleName.setText(namePeople.getName());
        return view;
    }
    class ViewHolder{
        ImageView PeopleImage;
        TextView PeopleName;
    }
}
