package com.thesaan.beyonduniverse.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.thesaan.beyonduniverse.R;
import com.thesaan.gameengine.android.activities.ProActivity;

/**
 * Created by Michael on 21.09.2015.
 */
public class StartScreenActivity extends ProActivity  implements View.OnClickListener{

    Button B_startGame,B_loadGame,B_options,B_credits,B_exit,B_devOptions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addViews();

    }

    private void addViews(){
        B_startGame = (Button)findViewById(R.id.B_new_game);
        B_loadGame = (Button)findViewById(R.id.B_load_game);
        B_options = (Button)findViewById(R.id.B_options);
        B_credits = (Button)findViewById(R.id.B_credits);
        B_exit = (Button)findViewById(R.id.B_exit);
        B_devOptions = (Button)findViewById(R.id.B_dev_options);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.B_new_game:
//TODO                normally run GameActivity, but for now just box_test MainActivity
                Intent newGame = new Intent(getApplicationContext(), com.thesaan.beyonduniverse.activities.GameActivity.class);
                startActivity(newGame);
                break;
            case R.id.B_load_game:
                Intent loadGame = new Intent(getApplicationContext(),GameLoadActivity.class);
                startActivity(loadGame);
                break;
            case R.id.B_options:
                Intent options = new Intent(getApplicationContext(),GameOptionsActivity.class);
                startActivity(options);
                break;
            case R.id.B_credits:
                Intent credits = new Intent(getApplicationContext(),GameCreditsActivity.class);
                startActivity(credits);
                break;
            case R.id.B_exit:
                finish();
                break;
            case R.id.B_dev_options:
                Intent dev = new Intent(getApplicationContext(),DeveloperActivity.class);
                startActivity(dev);

                break;
            default:
                shortToast("No Button detected in onClick!");
        }
    }
}
