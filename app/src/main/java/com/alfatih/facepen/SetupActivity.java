package com.alfatih.facepen;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private CircleImageView circleImageView;
    private Toolbar toolbar;
    private Uri mainImageUri;
    private EditText setupName;
    private Button setupButton;
    private Boolean isChanged = false;

    private String user_id;
    private StorageReference storageReference;
    private FirebaseAuth auth;
    private FirebaseFirestore firebaseFirestore;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        circleImageView = (CircleImageView) findViewById(R.id.profileImageView);
        setupName = (EditText) findViewById(R.id.setup_editText);
        setupButton = (Button) findViewById(R.id.setup_btn);
        progressBar = (ProgressBar) findViewById(R.id.setupProgressBar);
        //Firebasee inisializing
        auth = FirebaseAuth.getInstance();
        user_id = auth.getCurrentUser().getUid();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        toolbar = (Toolbar) findViewById(R.id.toolbarSetup);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Facepen");
        //Circle Image button
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(SetupActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Permission Denied! ",
                                Toast.LENGTH_SHORT).show();
                        ActivityCompat.requestPermissions(SetupActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        startCropImage();
                    }
                } else {
                    startCropImage(); }
            }
        });
        //To getting setup of the currentUser
        progressBar.setVisibility(View.VISIBLE);
        setupButton.setEnabled(false);
        firebaseFirestore.collection("users").document(user_id)
                .get().addOnCompleteListener(
                new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().exists()) {
                                String nameSetRetrive = task.getResult().getString("name");
                                String image = task.getResult().getString("image");
                                //to hold image offline
                                mainImageUri = Uri.parse(image);
                                setupName.setText(nameSetRetrive);
                                //to clear blank
                                /*RequestOptions placeHolderRequst = new RequestOptions();
                                placeHolderRequst.placeholder(R.drawable.dfp);
                                Glide.with(SetupActivity.this)

                                        .load(image).into(circleImageView);*/
                                Picasso.get()
                                        .load(image)
                                        .resize(50, 50)
                                        .placeholder(R.drawable.dfp)
                                        .error(R.drawable.dfp)
                                        .into(circleImageView);
                            }
                        } else {
                            String error = task.getException().getMessage();
                            Toast.makeText(SetupActivity.this, "FilestoreError Retrive: " + error,
                                    Toast.LENGTH_LONG).show();
                        }
                        progressBar.setVisibility(View.INVISIBLE);
                        setupButton.setEnabled(true);
                    }
                });
        setupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                final String setup_name = setupName.getText().toString();
                if (!TextUtils.isEmpty(setup_name) && mainImageUri != null) {
                    if (isChanged) {
                        //declared in the private
                        user_id = auth.getCurrentUser().getUid();
                        StorageReference image_path = storageReference.child("profile image")
                                .child(user_id + ".jpg");
                        image_path.putFile(mainImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                                if (task.isSuccessful()) {
                                    //to hold image offline
                                    storeFirestor(task, setup_name);
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(SetupActivity.this,
                                            "FilestoreError: " + error,
                                            Toast.LENGTH_LONG).show(); }
                            }
                        });
                    } else {
                        storeFirestor(null, setup_name);
                    }
                } else {
                    String error = "Please enter your name";
                    Toast.makeText(SetupActivity.this, error,
                            Toast.LENGTH_LONG).show();
                }
                progressBar.setVisibility(View.VISIBLE);
            }
        });
    }
    //to hold image offline

    private void storeFirestor(@NonNull Task<UploadTask.TaskSnapshot> task, String setup_name) {

        Uri download_uri;
        if (task != null) {
            download_uri = task.getResult().getDownloadUrl();

        } else {
            download_uri = mainImageUri;
        }
        //user_id = task.getResult().toString();
        Toast.makeText(SetupActivity.this, "Uploaded Successfully !! ",
                Toast.LENGTH_LONG).show();

        Map<String, String> userMap = new HashMap<>();

        userMap.put("name", setup_name);
        userMap.put("image", download_uri.toString());
        firebaseFirestore.collection("users")
                .document(user_id)
                .set(userMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if (task.isSuccessful()) {

                            Toast.makeText(SetupActivity.this, "Successful",
                                    Toast.LENGTH_LONG).show();
                            startActivity(new Intent(SetupActivity.this, MainActivity.class));

                            finish();
                        }

                    }
                });
    }

    private void startCropImage() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(SetupActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = result.getUri();
                circleImageView.setImageURI(mainImageUri);

                isChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
    }

}
