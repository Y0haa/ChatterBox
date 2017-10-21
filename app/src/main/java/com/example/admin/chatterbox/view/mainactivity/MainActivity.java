package com.example.admin.chatterbox.view.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.view.creategroupactivity.CreateGroupActivity;
import com.example.admin.chatterbox.view.joingroup.JoinGroup;
import com.example.admin.chatterbox.view.loginactivity.profileuser.ProfileFragment;

public class MainActivity extends AppCompatActivity {

    Button btnCreate, btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.setContentView(R.layout.activity_main);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void GroupButton(View view) {

        switch (view.getId()){

            case R.id.btnCreate:

                Intent createIntent = new Intent(this, CreateGroupActivity.class);
                startActivity(createIntent);

                break;
            case R.id.btnJoin:

                Intent joinIntent = new Intent(this, JoinGroup.class);
                startActivity(joinIntent);


        }
    }

    public void ProfileButton(View view) {
        FragmentManager fm = getSupportFragmentManager();
        ProfileFragment alertdFragment = new ProfileFragment ();
        alertdFragment.show(fm, "Profile");
    }
}
