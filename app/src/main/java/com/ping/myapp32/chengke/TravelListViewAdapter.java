package com.ping.myapp32.chengke;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ping.myapp32.R;

import java.util.ArrayList;

/**
 * Created by 2-1Ping on 2016/9/6.
 */
public class TravelListViewAdapter extends BaseAdapter {
    private final ArrayList<String> textArrayList;
    private final LayoutInflater layoutInflater;

    public TravelListViewAdapter(final Context context)
    {
        this.layoutInflater = LayoutInflater.from(context);
        this.textArrayList = new ArrayList<>();
    }

    @Override
    public int getCount()
    {
        return textArrayList.size();
    }

    @Override
    public String getItem(int position)
    {
        return textArrayList.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;

        if (convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.row_view1, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(textArrayList.get(position));

        return convertView;
    }

    public void updateList(ArrayList<String> stringArrayList)
    {
        this.textArrayList.clear();
        this.textArrayList.addAll(stringArrayList);
        this.notifyDataSetChanged();
    }

    private class ViewHolder
    {
        TextView textView;

        public ViewHolder(View v)
        {
            textView = (TextView) v.findViewById(R.id.text);
        }
    }
}
