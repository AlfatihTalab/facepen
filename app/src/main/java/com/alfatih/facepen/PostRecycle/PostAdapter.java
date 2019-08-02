package com.alfatih.facepen.PostRecycle;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.alfatih.facepen.Fragments.HomeFragment;
import com.alfatih.facepen.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostAdapter extends ArrayAdapter<Post> {
    public PostAdapter(@NonNull HomeFragment context, @NonNull List<Post> post) {
        super(context, 0, post);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view== null){
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.post_list, parent, false
            );
        }

        Post currentPost = getItem(position);

        TextView name = (TextView) view.findViewById(R.id.name_list_post_id);
        name.setText(currentPost.getmName());

        TextView post = (TextView) view.findViewById(R.id.post_text_id);
        name.setText(currentPost.getmPostText());

        CircleImageView mProfilImage = (CircleImageView) view.findViewById(R.id.profilPostImage_id);
        mProfilImage.setImageURI(currentPost.getmImageUri());




        return view;
    }
}
