package com.polito.gruppo4.pixweb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;
import android.widget.ImageButton;
import android.view.View.OnClickListener;
import android.content.Intent;

public class MainActivity extends AppCompatActivity implements OnClickListener{

    private ImageButton infolButton, drawButton, gameButton;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        infolButton = (ImageButton)findViewById(R.id.info);
        drawButton= (ImageButton)findViewById(R.id.web);
        gameButton= (ImageButton)findViewById(R.id.game);

        infolButton.setOnClickListener(this);
        drawButton.setOnClickListener(this);
        gameButton.setOnClickListener(this);

    }
    public void onClick(View view) {

        if(view.getId()==R.id.info){

            startActivity(new Intent(getApplicationContext(), TutorialActivity.class));
        }

        else if(view.getId()==R.id.web){

            startActivity(new Intent(getApplicationContext(), DrawingActivity.class));
        }
        else if(view.getId()==R.id.game){

            startActivity(new Intent(getApplicationContext(), GameActivity.class));
        }
    }
}
