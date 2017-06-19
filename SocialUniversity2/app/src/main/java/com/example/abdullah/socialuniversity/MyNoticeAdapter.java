package com.example.abdullah.socialuniversity;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by abdullah on 19.05.2017.
 */

public class MyNoticeAdapter extends BaseAdapter {
    private static final String TAG = "myAdapter";
    LayoutInflater layoutInflater;
    List<Notices> noticesList;
    TextView noticeTitle;
    private ImageView noticeLogo;
    private String resimUrl;
    private final Context context;

    public MyNoticeAdapter(Context c){
        context = c;
    }
    public MyNoticeAdapter(Activity activity, List<Notices> notices, Context context ){
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        noticesList = notices;
        this.context = context;
    }

    @Override
    public int getCount() {
        return noticesList.size();
    }

    @Override
    public Object getItem(int position) {
        return noticesList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        View satirView;

        satirView = layoutInflater.inflate(R.layout.my_noticelist, null);

        noticeLogo = (ImageView) satirView.findViewById(R.id.noticeLogo);
        noticeTitle = (TextView) satirView.findViewById(R.id.noticeTitle);


        Notices notices = noticesList.get(position);

        resimUrl = notices.getNoticeLogo();
        noticeTitle.setText(notices.getNoticeTitle());

        Picasso.with(context).load(resimUrl).into(noticeLogo);

        return satirView;
    }
}
