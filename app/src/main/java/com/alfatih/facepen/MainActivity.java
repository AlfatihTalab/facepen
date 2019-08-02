package com.alfatih.facepen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.alfatih.facepen.Fragments.FriendsFragment;
import com.alfatih.facepen.Fragments.HomeFragment;
import com.alfatih.facepen.Fragments.MessegeFragment;
import com.alfatih.facepen.Fragments.NotificationFragment;
import com.alfatih.facepen.PostRecycle.PostActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth auth;
    private Toolbar toolbar;
    private String titlename;
    private BottomNavigationView bottomNavigationView;
    private FirebaseFirestore firebaseFirestore;
    private String curent_user_id;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.BottomNavigation_id);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        titlename = "Facepen";




        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(titlename);
        // Custom title
        //@SuppressLint("ResourceType") TextView textCustomTitle = (TextView) findViewById(R.string.app_name);

        // Custom font
        //Typeface customFont = Typeface.createFromAsset(this.getAssets(), "fonts/Exo2-BoldItalic.ttf");

        // Set
        //textCustomTitle.setTypeface(customFont);

        firebaseFirestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
    }

    private void onLogout() {

        auth.signOut();
        setToLoginPage();
    }

    @Override
    protected void onStart() {

        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            curent_user_id = FirebaseAuth.getInstance().getUid();
            firebaseFirestore.collection("users").document(curent_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        if(!task.getResult().exists()){

                            startActivity(new Intent(MainActivity.this,SetupActivity.class));

                            finish();
                        }
                    }else {
                        String er = task.getException().getMessage();
                        Toast.makeText(MainActivity.this,
                                "Error: "+ er ,
                                Toast.LENGTH_SHORT).show();
                    }

                }
            });
            // User is signed in


        } else {
            // No user is signed in
            setToLoginPage();
        }
    }

    private void setToLoginPage() {


        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.logoutItem:
                onLogout();
                return true;
            case R.id.settingItem:
                startActivity(new Intent(MainActivity.this, SetupActivity.class));
                return true;
            case R.id.addPost_item:
                startActivity(new Intent(MainActivity.this, PostActivity.class));
                return true;


            default:
                return false;
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.homeItem_id:
                HomeFragment homeFragment = new HomeFragment();
                loadFragment(homeFragment);
                break;
            case R.id.addFrindItem_id:
                FriendsFragment friendsFragment = new FriendsFragment();
                loadFragment(friendsFragment);
                break;
            case R.id.puplicItem_id:
                NotificationFragment notificationFragment = new NotificationFragment();
                loadFragment(notificationFragment);
                break;
            case R.id.msgItem_id:
                MessegeFragment messegeFragment = new MessegeFragment();
                loadFragment(messegeFragment);

        }

        return true;

    }

    private void loadFragment(Fragment fragment) {

        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameLayoutMain, fragment)
                    .commit();
        }
    }
}