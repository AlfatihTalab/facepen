package com.alfatih.facepen.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alfatih.facepen.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {


    View v1;
    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v1 = inflater.inflate(R.layout. fragment_notification,container,false);
        return v1;
    }

}
