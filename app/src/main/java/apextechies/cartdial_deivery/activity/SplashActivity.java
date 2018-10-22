package apextechies.cartdial_deivery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import apextechies.cartdial_deivery.R;
import apextechies.cartdial_deivery.common.ClsGeneral;
import apextechies.cartdial_deivery.common.ConstantValue;
import apextechies.cartdial_deivery.common.Utilz;


/**
 * Created by Shankar on 3/30/2018.
 */

public class SplashActivity extends AppCompatActivity {
    public static final int RC_SIGN_IN = 111;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
       // FirebaseApp.initializeApp(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);

        Thread timer= new Thread()
        {
            public void run()
            {
                try
                {
                    //Display for 3 seconds
                    sleep(3000);
                }
                catch (InterruptedException e)
                {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                finally
                {
                    if (ClsGeneral.getPreferences(SplashActivity.this,ConstantValue.USERID).equals("")){
                        startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                        finish();
                    }else {
                        startActivity(new Intent(SplashActivity.this, MainActivity.class).
                        putExtra("from", "splash"));
                        finish();
                    }
                }
            }
        };
        timer.start();
    }



}
