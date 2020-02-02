package t3aarof.banat2020;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    DatabaseReference mDatabase,mDatabase2,mDatabase3,mDatabase4,mDatabase5;

    TextView Splash_Screen_Text;
    ImageView SplashScreenLogo;
    MyBounceInterpolator interpolator;

    boolean connectednetwork = false;

    SharedPreferences SplashScreenSharedPrefs;
    private Boolean Splash_Screen_ON = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FirebaseApp.initializeApp(this);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (Splash_Screen_ON == null) {
            SplashScreenSharedPrefs = this.getSharedPreferences("SplashScreen", Context.MODE_PRIVATE);
            Splash_Screen_ON = SplashScreenSharedPrefs.getBoolean("Splash_Screen_ON", true);
            if (Splash_Screen_ON) {

                /** Firebase Database Names */
                mDatabase = FirebaseDatabase.getInstance().getReference("SPLASH_SCREEN").child("SplashScreenAnim");
                mDatabase2 = FirebaseDatabase.getInstance().getReference("SPLASH_SCREEN").child("SplashScreenAnim").child("AnimState");
                mDatabase3 = FirebaseDatabase.getInstance().getReference("SPLASH_SCREEN").child("SplashScreenAnim").child("AnimType01");
                mDatabase4 = FirebaseDatabase.getInstance().getReference("SPLASH_SCREEN").child("SplashScreenAnim").child("AnimType02");
                mDatabase5 = FirebaseDatabase.getInstance().getReference("SPLASH_SCREEN").child("SplashScreenAnim").child("SplashLogo");

                /** Check Network State */
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                    connectednetwork = true;

                    SplashScreenLogo = (ImageView) findViewById(R.id.Splash_Screen_Logo);
                    Splash_Screen_Text = (TextView) findViewById(R.id.Splash_Screen_Text);

                    SPLASH_SCREEN_ON();

                } else {

                    connectednetwork = false;

                    OpenIntentNoNetworkActivity();
                }

            } else {

                SPLASH_SCREEN_OFF();
            }
        }
    }

    private void SPLASH_SCREEN_OFF() {

    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
    startActivity(intent);
    finish();
    }

    private void SPLASH_SCREEN_ON() {

        mDatabase3.child("BounceAnim").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);

                if (url.equals("on"))
                    Splash_Screen_Anim_01();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase4.child("FadeAnim").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);

                if (url.equals("on"))
                    Splash_Screen_Anim_02();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase2.child("NoAnim").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);

                if (url.equals("on"))
                    Splash_Screen_Anim_03();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase5.child("SplashScreenLogo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);

                Picasso.get()
                        .load(url)
                        .resize(512,512)
                        .into(SplashScreenLogo);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void Splash_Screen_Anim_03() {

        OpenIntentMainActivity();
    }

    private void Splash_Screen_Anim_02() {

        SplashScreenLogo.startAnimation(AnimationUtils.loadAnimation(getBaseContext(), R.anim.fadeanim));

        OpenIntentMainActivity();
    }

    private void Splash_Screen_Anim_01() {

        /** Splash Logo Animation*/
        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        SplashScreenLogo.startAnimation(myAnim);

        OpenIntentMainActivity();
    }

    private void OpenIntentMainActivity() {

        /** Splash Screen Wait Time */
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent homeIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(homeIntent);
                finish();
            }
        },2500); // 1000 Millis  =  "1.0 Seconds"
    }

    private void OpenIntentNoNetworkActivity() {

        Intent intent = new Intent(SplashActivity.this,NoNetWorkActivity.class);
        startActivity(intent);
        finish();
    }

    public void onBackPressed(){
        //empty
    }
}
