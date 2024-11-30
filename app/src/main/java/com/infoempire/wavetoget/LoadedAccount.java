package com.infoempire.wavetoget;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoadedAccount extends AppCompatActivity {
    private Button back;
    private Button storename;
    Boolean loading;
    public static Button lastClicked;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loaded_account);
//        home = findViewById(R.id.homebutton);

        Info frag = new Info();
        getSupportFragmentManager().beginTransaction().replace(R.id.infopage, frag).commit();
        Balance balanceFrag = new Balance();
        getSupportFragmentManager().beginTransaction().replace(R.id.page, balanceFrag).commit();
        StoreMainMenu menufrag = new StoreMainMenu();
        getSupportFragmentManager().beginTransaction().replace(R.id.storemenu, menufrag).commit();

//        home.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Balance balanceFrag = new Balance();
//                getSupportFragmentManager().beginTransaction().replace(R.id.page, balanceFrag).commit();
//                SetColor(home);
//            }
//        });
//        redeem = findViewById(R.id.redeembutton);
//        redeem.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
////                redeem frag = new redeem();
////                getSupportFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
////                SetColor(redeem);
//            }
//        });
//        rewards = findViewById(R.id.rewardsbutton);
//        rewards.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                rewards frag = new rewards();
//                getSupportFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
//                SetColor(rewards);
//            }
//        });
//        more = findViewById(R.id.notesbtn);
//        more.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
////                more frag = new more();
//                notes frag = new notes();
//                getSupportFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
//                SetColor(more);
////                getParentFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
//            }
//        });
        back = findViewById(R.id.searchbutton);

        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                LoadNextPage();
            }
        });

//        activity = findViewById(R.id.activity);
//
//        activity.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                CustomerActivityPage frag = new CustomerActivityPage();
//                getSupportFragmentManager().beginTransaction().replace(R.id.page, frag).commit();
//            }
//        });

        storename = findViewById(R.id.storename);
        storename.setText(StoreAccount.name);
        storename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadSettings();
            }
        });

//        SetColor(home);

    }
    public void LoadNextPage(){
        Intent intent = new Intent(this, StorePage.class);
        finish();
        startActivity(intent);
    }
    void LoadSettings()
    {
        Intent intent = new Intent(this, StoreSettings.class);
        startActivity(intent);
    }

//    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//    void SetColor(Button but)
//    {
//        if(but != lastClicked)
//        {
//            but.getBackground().setTint(Color.parseColor("#808080"));
//            if(lastClicked != null)
//                lastClicked.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#4275B3")));
//            lastClicked = but;
//        }
//    }
}