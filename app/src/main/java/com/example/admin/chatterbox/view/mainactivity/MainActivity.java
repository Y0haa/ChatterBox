package com.example.admin.chatterbox.view.mainactivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;

import com.example.admin.chatterbox.R;
import com.example.admin.chatterbox.view.creategroup.CreateGroup;
import com.example.admin.chatterbox.view.joingroup.JoinGroup;

public class MainActivity extends AppCompatActivity {

    Button btnCreate, btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void GroupButton(View view) {

        switch (view.getId()){

            case R.id.btnCreate:

                Intent createIntent = new Intent(this, CreateGroup.class);
                startActivity(createIntent);

                break;
            case R.id.btnJoin:

                Intent joinIntent = new Intent(this, JoinGroup.class);
                startActivity(joinIntent);


        }
    }
}
