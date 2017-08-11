package com.example.deepakrattan.firebaserealtimedatabasedemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by deepak.rattan on 8/9/2017.
 */

public class ArtistAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Artist> artistArrayList;
    private LayoutInflater inflater;

    public ArtistAdapter(Context context, ArrayList<Artist> artistArrayList) {
        this.context = context;
        this.artistArrayList = artistArrayList;
    }

    @Override
    public int getCount() {
        return artistArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return artistArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.single_row_artist, viewGroup, false);

        TextView txtName = (TextView) view.findViewById(R.id.txtName);
        TextView txtGenre = (TextView) view.findViewById(R.id.txtGenre);

        Artist artist = artistArrayList.get(i);
        String name = artist.getName();
        String genre = artist.getGenere();

        txtName.setText(name);
        txtGenre.setText(genre);


        return view;
    }
}
