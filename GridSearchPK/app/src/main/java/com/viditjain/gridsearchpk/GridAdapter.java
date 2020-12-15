package com.viditjain.gridsearchpk;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import java.util.ArrayList;

public class GridAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<String> lettersArrayList;

    public GridAdapter(Context context,ArrayList<String> lettersArrayList)
    {
        this.context=context;
        this.lettersArrayList=lettersArrayList;
    }

    @Override
    public int getCount()
    {
        return lettersArrayList.size();
    }

    @Override
    public Object getItem(int position)
    {
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        Button button=new Button(context);
        button.setAllCaps(false);
        button.setTextSize(20);
        button.setText(lettersArrayList.get(position));
        button.setBackgroundColor(context.getResources().getColor(R.color.purple));
        button.setTextColor(context.getResources().getColor(R.color.login_form_details));
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RStringActivity.rStringTextView.setText(RStringActivity.rStringTextView.getText()+lettersArrayList.get(position));
            }
        });
        return button;
    }
}
