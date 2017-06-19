package com.example.abdullah.socialuniversity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by abdullah on 14.04.2017.
 */

public class MyAdapter extends BaseAdapter {

    private static final String TAG = "myAdapter";
    LayoutInflater layoutInflater;
    List<Universities> universitiesList;
    TextView universityName, universityWeb;
    private ImageView universityLogo;
    private String resimUrl;
    private final Context context;

    public MyAdapter(Context c){
        context = c;
    }
    public MyAdapter(Activity activity, List<Universities> yarsimacis, Context context ){
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        universitiesList = yarsimacis;
        this.context = context;
    }

    @Override
    public int getCount() {
        return universitiesList.size();
    }

    @Override
    public Object getItem(int position) {
        return universitiesList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View satirView;

        satirView = layoutInflater.inflate(R.layout.my_list, null);

        universityName = (TextView) satirView.findViewById(R.id.universityName);
        universityWeb = (TextView) satirView.findViewById(R.id.universityWeb);
        universityLogo = (ImageView) satirView.findViewById(R.id.universityLogo);


        Universities universities = universitiesList.get(position);

        resimUrl = universities.getUniversityLogo();
        universityName.setText(universities.getUniversityName());
        universityWeb.setText(universities.getUniversityWeb());

        Picasso.with(context).load(resimUrl).into(universityLogo);

        return satirView;
    }
}
