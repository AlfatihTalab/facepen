package com.alfatih.facepen.PostRecycle;

import android.net.Uri;

public class Post {
    private String mName, mPostText;
    private Uri mImageUri;

    public Post(String mName, String mPostText, Uri mImageUri) {
        this.mName = mName;
        this.mPostText = mPostText;
        this.mImageUri = mImageUri;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmPostText() {
        return mPostText;
    }

    public void setmPostText(String mPostText) {
        this.mPostText = mPostText;
    }

    public Uri getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(Uri mImageUri) {
        this.mImageUri = mImageUri;
    }
}
