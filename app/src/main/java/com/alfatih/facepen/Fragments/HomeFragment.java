package com.alfatih.facepen.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alfatih.facepen.PostRecycle.Post;
import com.alfatih.facepen.PostRecycle.PostAdapter;
import com.alfatih.facepen.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    View v1;


    public static HomeFragment newInstance() {
        return new HomeFragment();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v1 = inflater.inflate(R.layout.fragment_home,container,false);
        return v1;
            //ListView listView = (ListView) findViewById(R.id.homeListView);
            //ArrayList<Post> posts = new ArrayList<>();


            //PostAdapter adapter = new PostAdapter(this,posts);
            //listView.setAdapter(adapter);
    }



}
