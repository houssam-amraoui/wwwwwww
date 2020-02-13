package t3aarof.banat2020;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import t3aarof.banat2020.WebEngine.LiveWeb;
import t3aarof.banat2020.WebEngine.LiveWebClient;

import android.os.Environment;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.webkit.CookieManager;
import android.webkit.DownloadListener;
import android.webkit.GeolocationPermissions;
import android.webkit.PermissionRequest;
import android.webkit.SslErrorHandler;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URISyntaxException;
import java.util.Objects;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String Tempp="pp";

    DatabaseReference mDatabase,mDatabase2,mDatabase3,mDatabase4,mDatabase5,mDatabase6,mDatabase7;

    SharedPreferences SplashScreenSharedPrefs,CounterSharedPrefs;
    SharedPreferences.Editor sssp_editor,scsp_editor;

    boolean connectednetwork = false;
    private Boolean Dashboard_Rate_ON = true;

    private LiveWeb LiveWebView;
    private LiveWebClient LiveWebViewClient;

    private ValueCallback<Uri> mUploadMessage;
    public ValueCallback<Uri[]> uploadMessage;

    private final int WRITE_REQUEST = 100;
    private final int LOCATION_REQUEST= 101;
    public static final int REQUEST_SELECT_FILE = 102;
    private final static int FILECHOOSER_RESULTCODE = 103;
    private static final int RECORD_AUDIO_REQUEST = 104;
    private static final int CAMERA_REQUEST = 106;

    NavigationView navigationview;
    Menu menu;
    MenuItem nav_item01,nav_item02,nav_item03,nav_item04,nav_item05,nav_item06,nav_item07,nav_share,group_items,menu_items;

    SwipeRefreshLayout mySwipeRefreshLayout;
    ProgressBar MyProgressBar;
    View Progress_Lay;

    Toolbar Main_Tool_Bar;
    ImageButton ABM_Button_01,ABM_Button_02;
    TextView ABM_Text,DAS_Text,DAS_Text_2;
    ImageView NHM_Image,NHM_Image02;
    RelativeLayout Rel_Der,Rel_Der_2;
    Button DAS_Yes,DAS_No;
    View Lay_DER;
    CheckBox DAS_Check;

    int counter = 0;
    private int counter_rate = 1;
    private int counter_exit = 1;
    private int counter_ads = 1;

    /* CHANGE IT EVERY CLICKS */
    private int total_counter_ads = 10; // write ur clicks

    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    public static final String USER_AGENT = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);

        /** Firebase Database Name */
        mDatabase = FirebaseDatabase.getInstance().getReference("SPLASH_SCREEN");
        mDatabase2 = FirebaseDatabase.getInstance().getReference("MAIN_ACTIVITY");
        mDatabase3 = FirebaseDatabase.getInstance().getReference("MAIN_ACTIVITY").child("LoadPages");
        mDatabase4 = FirebaseDatabase.getInstance().getReference("MAIN_ACTIVITY").child("LoadPageTitles");
        mDatabase5 = FirebaseDatabase.getInstance().getReference("MAIN_ACTIVITY").child("LoadPageIcons");
        mDatabase6 = FirebaseDatabase.getInstance().getReference("MAIN_ACTIVITY").child("NavigationMenuState");
        mDatabase7 = FirebaseDatabase.getInstance().getReference("MAIN_ACTIVITY").child("LoadGoogleAdmob");


        LoadAdmobAd();
        LoadOneSignal();


        CounterSharedPrefs = this.getSharedPreferences("DashboardRate", Context.MODE_PRIVATE);

        ABM_Text = (TextView) findViewById(R.id.ABM_Text);
        DAS_Text = (TextView) findViewById(R.id.DAS_Text);
        DAS_Text_2 = (TextView) findViewById(R.id.DAS_Text_2);
        Main_Tool_Bar = (Toolbar) findViewById(R.id.Main_Tool_Bar);
        ABM_Button_01 = (ImageButton) findViewById(R.id.ABM_Button_01);
        ABM_Button_02 = (ImageButton) findViewById(R.id.ABM_Button_02);
        Rel_Der = (RelativeLayout)findViewById(R.id.Rel_Der);
        Rel_Der_2 = (RelativeLayout)findViewById(R.id.Rel_Der_2);
        DAS_Check = (CheckBox)findViewById(R.id.DAS_Check);
        DAS_Yes = (Button)findViewById(R.id.DAS_Yes);
        DAS_No = (Button)findViewById(R.id.DAS_No);
        Lay_DER = (View)findViewById(R.id.Lay_Der);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            DAS_Yes.setBackground(getDrawable(R.drawable.ripple_color_01));
            DAS_No.setBackground(getDrawable(R.drawable.ripple_color_01));

        } else {

            DAS_Yes.setBackgroundColor(getResources().getColor(R.color.main_background));
            DAS_No.setTextColor(getResources().getColor(R.color.main_background));
        }

        mySwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeContainer);
        MyProgressBar = (ProgressBar) findViewById(R.id.ProgressBar);
        Progress_Lay = (View) findViewById(R.id.Progress_Lay);

        LiveWebView = (LiveWeb)findViewById(R.id.LiveWebView);

        ABM_Button_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.openDrawer(GravityCompat.START);
            }
        });


        navigationview = findViewById(R.id.nav_view);
        navigationview.setCheckedItem(R.id.nav_item01);

        View headerView = navigationview.getHeaderView(0);
        NHM_Image = headerView.findViewById(R.id.NHM_Image);
        NHM_Image02 = headerView.findViewById(R.id.NHM_Image02);

        menu = navigationview.getMenu();

        nav_item01 = menu.findItem(R.id.nav_item01);
        nav_item02 = menu.findItem(R.id.nav_item02);
        nav_item03 = menu.findItem(R.id.nav_item03);
        nav_item04 = menu.findItem(R.id.nav_item04);
        nav_item05 = menu.findItem(R.id.nav_item05);
        nav_item06 = menu.findItem(R.id.nav_item06);
        nav_item07 = menu.findItem(R.id.nav_item07);
        nav_share = menu.findItem(R.id.nav_share);
        group_items = menu.findItem(R.id.group_items);
        menu_items = menu.findItem(R.id.menu_items);

        LoadItemTitles();
        LoadItemIcons();
        CheckNavMenuState();

        navigationview.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                menuItem.setChecked(false);
                Dashboard_Rate_State();
                ShowIntersAds();

                // Handle navigation view item clicks here.
                int id = menuItem.getItemId();

                switch (id){

                    case R.id.nav_item01:
                        LoadPageUrl01();
                        break;

                    case R.id.nav_item02:
                        LoadPageUrl02();
                        break;

                    case R.id.nav_item03:
                        LoadPageUrl03();
                        break;

                    case R.id.nav_item04:
                        LoadPageUrl04();
                        break;

                    case R.id.nav_item05:
                        LoadPageUrl05();
                        break;

                    case R.id.nav_item06:
                        LoadPageUrl06();
                        break;
                    case R.id.nav_item07:
                        LoadPageUrl07();
                        break;
                    case R.id.nav_share:
                        ShareApp();
                        break;
                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

        CheckNetworkState();
    }

    private void LoadOneSignal() {

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    private void LoadAdmobAd() {

        mAdView = new AdView(getBaseContext());
        mAdView.setAdSize(AdSize.BANNER);

        mInterstitialAd = new InterstitialAd(this);

        mDatabase7.child("AdmobAppId").child("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                MobileAds.initialize(MainActivity.this, string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase7.child("AdmobAdsState").child("BannerAdState").child("state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                if (string.equals("on")){

                    View adContainer = findViewById(R.id.adView);
                    adContainer.setVisibility(View.VISIBLE);
                } else {

                    View adContainer = findViewById(R.id.adView);
                    adContainer.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        LoadAdmobBannerAd();
        LoadAdmobIntersAd();
    }

    private void LoadAdmobIntersAd() {

        mDatabase7.child("AdmobIntersId").child("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                mInterstitialAd.setAdUnitId(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void LoadAdmobBannerAd() {

        View adContainer = findViewById(R.id.adView);

        mDatabase7.child("AdmobBannerId").child("id").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                mAdView.setAdUnitId(string);
                ((RelativeLayout)adContainer).addView(mAdView);
                AdRequest adRequest = new AdRequest.Builder().build();
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void ShowIntersAds() {

        if (counter_ads == total_counter_ads){

            mDatabase7.child("AdmobAdsState").child("IntersAdState").child("state").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String string = dataSnapshot.getValue(String.class);

                    if (string.equals("on")){

                        if (mInterstitialAd.isLoaded()){
                            mInterstitialAd.show();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });

            counter_ads = 0;
        } else {

            mInterstitialAd.loadAd(new AdRequest.Builder().build());

            counter_ads ++;
        }
    }

    private void CheckNavMenuState() {

        mDatabase6.child("NavMenu").child("state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                if (string.equals("on")){

                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

                    ABM_Button_01.setVisibility(View.VISIBLE);
                    ABM_Button_01.setEnabled(true);

                    CheckMenuItemState();

                } else {

                    DrawerLayout drawer = findViewById(R.id.drawer_layout);
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

                    ABM_Button_01.setVisibility(View.INVISIBLE);
                    ABM_Button_01.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase6.child("SharePageMenu").child("state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                if (string.equals("on")){

                    ABM_Button_02.setVisibility(View.VISIBLE);
                    ABM_Button_02.setEnabled(true);

                    CheckMenuItemState();

                } else {

                    ABM_Button_02.setVisibility(View.INVISIBLE);
                    ABM_Button_02.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void CheckMenuItemState() {

        mDatabase6.child("Menu02").child("state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                if (string.equals("on")){

                    nav_item02.setVisible(true);
                } else {

                    nav_item02.setVisible(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase6.child("Menu03").child("state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                if (string.equals("on")){

                    nav_item03.setVisible(true);
                } else {

                    nav_item03.setVisible(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase6.child("Menu04").child("state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                if (string.equals("on")){

                    nav_item04.setVisible(true);
                } else {

                    nav_item04.setVisible(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase6.child("Menu05").child("state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                if (string.equals("on")){

                    nav_item05.setVisible(true);
                } else {

                    nav_item05.setVisible(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase6.child("Menu06").child("state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                if (string.equals("on")){

                    nav_item06.setVisible(true);
                } else {

                    nav_item06.setVisible(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase6.child("Menu07").child("state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                if (string.equals("on")){

                    nav_item07.setVisible(true);
                } else {

                    nav_item07.setVisible(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase6.child("Menu08").child("state").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                if (string.equals("on")){

                    nav_share.setVisible(true);
                } else {

                    nav_share.setVisible(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void ShareApp() {

        mDatabase2.child("ShareAppText").child("text").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = string + " https://play.google.com/store/apps/details?id=" + (getPackageName());
                sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getPackageName());
                sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(sharingIntent, "Share via"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void SharePage(String string) {

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = string;
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getPackageName());
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    private void LoadItemIcons() {

        final Target mTarget = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {

                BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                nav_item01.setIcon(mBitmapDrawable);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {
            }
        };

        final Target mTarget2 = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {

                BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                nav_item02.setIcon(mBitmapDrawable);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {
            }
        };

        final Target mTarget3 = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {

                BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                nav_item03.setIcon(mBitmapDrawable);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {
            }
        };

        final Target mTarget4 = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {

                BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                nav_item04.setIcon(mBitmapDrawable);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {
            }
        };

        final Target mTarget5 = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {

                BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                nav_item05.setIcon(mBitmapDrawable);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {
            }
        };

        final Target mTarget6 = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {

                BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                nav_item06.setIcon(mBitmapDrawable);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {
            }
        };

        final Target mTarget7 = new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom loadedFrom) {

                BitmapDrawable mBitmapDrawable = new BitmapDrawable(getResources(), bitmap);
                nav_item07.setIcon(mBitmapDrawable);
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable drawable) {
            }
        };


        /** Database Get Icon URL */
        mDatabase5.child("LoadPageIcon01").child("icon").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                Picasso.get().load(string).into(mTarget);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase5.child("LoadPageIcon02").child("icon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                Picasso.get().load(string).into(mTarget2);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase5.child("LoadPageIcon03").child("icon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                Picasso.get().load(string).into(mTarget3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase5.child("LoadPageIcon04").child("icon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                Picasso.get().load(string).into(mTarget4);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase5.child("LoadPageIcon05").child("icon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                Picasso.get().load(string).into(mTarget5);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase5.child("LoadPageIcon06").child("icon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                Picasso.get().load(string).into(mTarget6);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase5.child("LoadPageIcon07").child("icon").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                Picasso.get().load(string).into(mTarget7);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void LoadItemTitles() {

        mDatabase4.child("LoadPageTitle01").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                nav_item01.setTitle(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase4.child("LoadPageTitle02").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                nav_item02.setTitle(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase4.child("LoadPageTitle03").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                nav_item03.setTitle(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase4.child("LoadPageTitle04").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                nav_item04.setTitle(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase4.child("LoadPageTitle05").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                nav_item05.setTitle(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase4.child("LoadPageTitle06").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                nav_item06.setTitle(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase4.child("LoadPageTitle07").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                nav_item07.setTitle(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase4.child("LoadPageTitle08").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                nav_share.setTitle(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase4.child("LoadPageTitle09").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                menu_items.setTitle(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase4.child("LoadPageTitle10").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                group_items.setTitle(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase2.child("LoadBGImage").child("LoadImage").child("image").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                Picasso.get()
                        .load(string)
                        .resize(600,300)
                        .into(NHM_Image02);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase.child("SplashScreenAnim").child("SplashLogo").child("SplashScreenLogo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);

                Picasso.get()
                        .load(string)
                        .into(NHM_Image);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void CheckNetworkState() {

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        /** Check Network State */
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            connectednetwork = true;

            CheckSplashScreenState();
            LoadPage();

        } else {

            connectednetwork = false;

            OpenIntentNoNetworkActivity();
        }
    }

    private void ProgressStart() {

        Progress_Lay.setVisibility(View.VISIBLE);

        ObjectAnimator anim = ObjectAnimator.ofInt(MyProgressBar, "progress", 0, 100);
        anim.setDuration(500);
        anim.setInterpolator(new DecelerateInterpolator());
        anim.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                ProgressGone();
            }
        },500);
    }

    private void ProgressGone() {

        Progress_Lay.setVisibility(View.GONE);
    }

    private void LoadPage() {

        LiveWebBrowserSettings();

        LoadPageUrl01();
    }

    private void LoadPageUrl01() {

        counter = 1;

        mDatabase3.child("LoadPageUrl01").child("url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                LiveWebView.loadUrl(url);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase4.child("LoadPageTitle01").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                ABM_Text.setText(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void LoadPageUrl02() {

        counter = 2;

        mDatabase3.child("LoadPageUrl02").child("url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                LiveWebView.loadUrl(url);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase4.child("LoadPageTitle02").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                ABM_Text.setText(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void LoadPageUrl03() {

        counter = 3;

        mDatabase3.child("LoadPageUrl03").child("url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                LiveWebView.loadUrl(url);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase4.child("LoadPageTitle03").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                ABM_Text.setText(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void LoadPageUrl04() {

        counter = 4;

        mDatabase3.child("LoadPageUrl04").child("url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                LiveWebView.loadUrl(url);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDatabase4.child("LoadPageTitle04").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                ABM_Text.setText(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void LoadPageUrl05() {

        counter = 5;

        mDatabase3.child("LoadPageUrl05").child("url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                LiveWebView.loadUrl(url);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDatabase4.child("LoadPageTitle05").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                ABM_Text.setText(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void LoadPageUrl06() {

        counter = 6;

        mDatabase3.child("LoadPageUrl06").child("url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                LiveWebView.loadUrl(url);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase4.child("LoadPageTitle06").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                ABM_Text.setText(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void LoadPageUrl07() {

        counter = 7;

        mDatabase3.child("LoadPageUrl07").child("url").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);
                LiveWebView.loadUrl(url);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mDatabase4.child("LoadPageTitle07").child("title").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String string = dataSnapshot.getValue(String.class);
                ABM_Text.setText(string);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void LiveWebBrowserSettings() {

        View nonVideoLayout = findViewById(R.id.nonVideoLayout);
        ViewGroup videoLayout = (ViewGroup) findViewById(R.id.videoLayout);

        LiveWebViewClient = new LiveWebClient(nonVideoLayout, videoLayout, LiveWebView) {

            @Override
            public void onPermissionRequest(final PermissionRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    request.grant(request.getResources());

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider location
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECORD_AUDIO}, RECORD_AUDIO_REQUEST);

                            return;
                        }
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider location
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);

                            return;
                        }
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider location
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);

                            return;
                        }
                    }
                }
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider location
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
                        return;
                    }
                }
            }

            /** For 3.0+ Devices (Start)
             onActivityResult attached before constructor */
            protected void openFileChooser(ValueCallback uploadMsg, String acceptType){
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            /** For Lollipop 5.0+ Devices */
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean onShowFileChooser(WebView mWebView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams)
            {
                if (uploadMessage != null) {
                    uploadMessage.onReceiveValue(null);
                    uploadMessage = null;
                }

                uploadMessage = filePathCallback;

                Intent intent = fileChooserParams.createIntent();
                try
                {
                    startActivityForResult(intent, REQUEST_SELECT_FILE);
                } catch (ActivityNotFoundException e)
                {
                    uploadMessage = null;
                    Toast.makeText(getApplicationContext(), "Cannot Open File Chooser", Toast.LENGTH_LONG).show();
                    return false;
                }
                return true;
            }

            /** For Android 4.1 only */
            protected void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture)
            {
                mUploadMessage = uploadMsg;
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "File Browser"), FILECHOOSER_RESULTCODE);
            }

            protected void openFileChooser(ValueCallback<Uri> uploadMsg)
            {
                mUploadMessage = uploadMsg;
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                startActivityForResult(Intent.createChooser(i, "File Chooser"), FILECHOOSER_RESULTCODE);
            }

            @Override
            public void onProgressChanged(WebView view,int newProgress){
                super.onProgressChanged(view,newProgress);
            }
        };

        LiveWebViewClient.setOnToggledFullscreen(new LiveWebClient.ToggledFullscreenCallback() {
            @Override
            public void toggledFullscreen(boolean fullscreen) {
                if (fullscreen) {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags |= WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

                    ShowFullScreenVideo();

                    getWindow().setAttributes(attrs);
                    if (Build.VERSION.SDK_INT >= 19) {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE);
                    }

                    MainActivity.this.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

                } else {
                    WindowManager.LayoutParams attrs = getWindow().getAttributes();
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    attrs.flags &= ~WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
                    getWindow().setAttributes(attrs);

                    ReturnFullScreenVideo();

                    if (Build.VERSION.SDK_INT >= 19) {
                        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }

                    MainActivity.this.setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
            }
        });
// lest do it with test ads
        LiveWebView .getSettings().setJavaScriptEnabled(true);
        LiveWebView .setFocusable(true);
        LiveWebView .setFocusableInTouchMode(true);
        LiveWebView .getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        LiveWebView .getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        LiveWebView .getSettings().setDomStorageEnabled(true);
        LiveWebView .getSettings().setDatabaseEnabled(true);
        LiveWebView .getSettings().setAppCacheEnabled(true);
        LiveWebView .setWebChromeClient(LiveWebViewClient);
        LiveWebView .setWebViewClient(new MyWebViewClient());
        LiveWebView .getSettings().setAllowFileAccess(true);
        LiveWebView .getSettings().setAllowContentAccess(true);
        LiveWebView .getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        LiveWebView .getSettings().setGeolocationDatabasePath(getFilesDir().getPath());
        LiveWebView .getSettings().setGeolocationEnabled(true);

        LiveWebView .getSettings().setUserAgentString(USER_AGENT);

        LiveWebView .getSettings().setUseWideViewPort(true);
        LiveWebView .getSettings().setBuiltInZoomControls(false);
        LiveWebView .setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        LiveWebView .setScrollbarFadingEnabled(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            LiveWebView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            LiveWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            LiveWebView.getSettings().setMixedContentMode( WebSettings.MIXED_CONTENT_ALWAYS_ALLOW );
        }

        LiveWebView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP){
                    if(!Tempp.equals(LiveWebView.getUrl().toString())){
                        Toast.makeText(MainActivity.this,  LiveWebView.getUrl(), Toast.LENGTH_SHORT).show();
                        ShowIntersAds();
                        Tempp = LiveWebView.getUrl().toString();
                    }
                }
                return false;
            }
        });

        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                                                      @Override
                                                      public void onRefresh() {
                                                          new Handler().postDelayed(new Runnable() {
                                                              @Override
                                                              public void run() {
                                                                  LiveWebView.reload();
                                                                  mySwipeRefreshLayout.setRefreshing(false);
                                                              }
                                                          },1000);
                                                      }
                                                  }
        );

        LiveWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent,String contentDisposition, String mimeType,long contentLength) {

                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(MainActivity.this), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider writing
                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_REQUEST);

                            return;
                        }
                    }

                    String string = String.valueOf((URLUtil.guessFileName(url, contentDisposition, mimeType)));

                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
                    request.setMimeType(mimeType);
                    String cookies = CookieManager.getInstance().getCookie(url);

                    request.addRequestHeader("cookie", cookies);
                    request.addRequestHeader("User-Agent", userAgent);
                    request.allowScanningByMediaScanner();
                    request.setDescription("Downloading...");
                    request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType));
                    request.allowScanningByMediaScanner();

                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS ,  string);

                    DownloadManager dm = (DownloadManager)MainActivity.this.getSystemService(DOWNLOAD_SERVICE);
                    dm.enqueue(request);

                    Toast.makeText(MainActivity.this, "Downloading file ...", Toast.LENGTH_LONG).show();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Toast.makeText(MainActivity.this, "Something went wrong. Please try again.", Toast.LENGTH_LONG).show();
                }
            }});
    }

    public void ShowFullScreenVideo() {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        Main_Tool_Bar.setVisibility(View.GONE);
    }

    public void ReturnFullScreenVideo() {

        CheckNavMenuState();

        Main_Tool_Bar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == REQUEST_SELECT_FILE) {
                if (uploadMessage == null)
                    return;
                uploadMessage.onReceiveValue(LiveWebClient.FileChooserParams.parseResult(resultCode, intent));
                uploadMessage = null;
            }
        } else if (requestCode == FILECHOOSER_RESULTCODE) {
            if (null == mUploadMessage)
                return;
            Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
            mUploadMessage.onReceiveValue(result);
            mUploadMessage = null;
        } else
            Toast.makeText(getApplicationContext(), "Failed To File", Toast.LENGTH_LONG).show();
    }

    private void CheckSplashScreenState() {

        /** Splash Shared Preferences Name */
        SplashScreenSharedPrefs = this.getSharedPreferences("SplashScreen", Context.MODE_PRIVATE);

        mDatabase.child("SplashScreenState").child("SplashScreen").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String url = dataSnapshot.getValue(String.class);

                if (url.equals("on")){

                    sssp_editor = SplashScreenSharedPrefs.edit();
                    sssp_editor.putBoolean("Splash_Screen_ON", true);
                    sssp_editor.apply();

                } else {

                    sssp_editor = SplashScreenSharedPrefs.edit();
                    sssp_editor.putBoolean("Splash_Screen_ON", false);
                    sssp_editor.apply();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    private void OpenIntentNoNetworkActivity() {

        Intent intent = new Intent(MainActivity.this,NoNetWorkActivity.class);
        startActivity(intent);
        finish();
    }

    private class MyWebViewClient extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
            builder.setMessage(R.string.notification_error_ssl_cert_invalid);
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
        }

        @TargetApi(Build.VERSION_CODES.M)
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                super.onReceivedHttpError(view, request, errorResponse);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if (url.startsWith("intent://")) {
                try {

                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

                    if (intent != null) {
                        view.stopLoading();

                        PackageManager packageManager = getPackageManager();
                        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        if (info != null) {
                            startActivity(intent);
                        } else {
                            String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                            view.loadUrl(fallbackUrl);
                        }
                        return true;
                    }
                } catch (URISyntaxException e) {

                    Toast.makeText( MainActivity.this,"Can't resolve intent.", Toast.LENGTH_SHORT).show();
                }
            }

            if(url.contains("whatsapp://")) {
                Uri IntentUri = Uri.parse(url);
                Intent wpIntent = new Intent(Intent.ACTION_VIEW, IntentUri);
                wpIntent.setPackage("com.whatsapp");

                if (wpIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(wpIntent);
                } else {
                    view.loadUrl(url);
                    Toast.makeText(MainActivity.this,"Can't resolve intent.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            if(url.contains("https://maps")) {
                Uri IntentUri = Uri.parse(url);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, IntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            if(url.contains("https://www.google.com/maps")) {
                Uri IntentUri = Uri.parse(url);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, IntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");

                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                } else {
                    view.loadUrl(url);
                }
                return true;
            }

            if (url.contains("tel:")) {
                Uri IntentUri = Uri.parse(url);
                Intent callIntent = new Intent(Intent.ACTION_DIAL, IntentUri);

                if (callIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(callIntent);
                    view.reload();
                } else {
                    view.loadUrl(url);
                    Toast.makeText(MainActivity.this, "Can't resolve intent.", Toast.LENGTH_SHORT).show();
                }
                return  true;
            }

            if(url.contains("mailto:")) {
                Uri IntentUri = Uri.parse(url);
                Intent mailIntent = new Intent(Intent.ACTION_SENDTO, IntentUri);

                if (mailIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mailIntent);
                } else {
                    view.loadUrl(url);
                    Toast.makeText(MainActivity.this,"Can't resolve intent.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            if (url.contains("play.google.com/store/apps/details?id")) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                Activity activity = (Activity) view.getContext();
                activity.startActivity(intent);
                return true;
            }

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, final String url, Bitmap favicon) {

            ProgressStart();
            isOnline();

            ABM_Button_02.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    SharePage(url);
                }
            });

            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            super.onLoadResource(view, url);

        }

        @Override
        public void onPageFinished(WebView view, String url) {

            if (counter == 1){
                view.clearHistory();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        counter = 10;
                    }
                },1000);
            }

            if (counter == 2){
                view.clearHistory();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        counter = 20;
                    }
                },1000);
            }

            if (counter == 3){
                view.clearHistory();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        counter = 30;
                    }
                },1000);
            }

            if (counter == 4){
                view.clearHistory();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        counter = 40;
                    }
                },1000);
            }

            if (counter == 5){
                view.clearHistory();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        counter = 50;
                    }
                },1000);
            }

            if (counter == 6){
                view.clearHistory();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        counter = 60;
                    }
                },1000);
            }

            if (counter == 7){
                view.clearHistory();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        counter = 70;
                    }
                },1000);
            }

            super.onPageFinished(view, url);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        if (requestCode == WRITE_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "User allowed write external storage.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "User denied write external storage permission.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == LOCATION_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "User allowed GPS.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "User denied GPS permission.", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == RECORD_AUDIO_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "User allowed Microphone.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "User denied Microphone permission.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (LiveWebView.canGoBack()){

                LiveWebView.goBack();
            } else {

                if (counter_exit == 1){

                    Dashboard_Exit();
                }
            }
        }
    }

    private void Dashboard_Exit() {

        Resources resources = getResources();
        String string = resources.getString(R.string.exit_text);
        DAS_Text.setText(string);

        counter_exit = 2;

        DAS_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        DAS_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dashboard_Gone();
                counter_exit = 1;
            }
        });

        Lay_DER.setVisibility(View.VISIBLE);
        Rel_Der_2.setVisibility(View.GONE);

        final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        Rel_Der.startAnimation(myAnim);
    }

    private void Dashboard_Gone() {

        Lay_DER.setVisibility(View.INVISIBLE);

        final Animation myAnim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.bounce);
        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
        myAnim.setInterpolator(interpolator);
        Rel_Der.startAnimation(myAnim);
    }

    private void Dashboard_Rate_State() {

        if(counter_rate == 6){

            counter_rate = 1;
            Dashboard_Rate();

        }else{

            counter_rate ++;
        }
    }

    private void Dashboard_Rate() {

        counter_exit = 1;

        Resources resources = getResources();
        String string = resources.getString(R.string.exit_rate);
        DAS_Text.setText(string);

        DAS_Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RateApp();
                Dashboard_Gone();

                scsp_editor = CounterSharedPrefs.edit();
                scsp_editor.putBoolean("Dashboard_Show_ON", false);
                scsp_editor.apply();
            }
        });

        DAS_No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dashboard_Gone();
                counter_rate = 1;

                if (DAS_Check.isChecked()){

                    scsp_editor = CounterSharedPrefs.edit();
                    scsp_editor.putBoolean("Dashboard_Show_ON", false);
                    scsp_editor.apply();
                } else {

                    scsp_editor = CounterSharedPrefs.edit();
                    scsp_editor.putBoolean("Dashboard_Show_ON", true);
                    scsp_editor.apply();
                }
            }
        });

        if (Dashboard_Rate_ON == true) {
            Dashboard_Rate_ON = CounterSharedPrefs.getBoolean("Dashboard_Show_ON", true);
            if (Dashboard_Rate_ON) {

                Lay_DER.setVisibility(View.VISIBLE);
                Rel_Der_2.setVisibility(View.VISIBLE);

                final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.bounce);
                MyBounceInterpolator interpolator = new MyBounceInterpolator(0.2, 20);
                myAnim.setInterpolator(interpolator);
                Rel_Der.startAnimation(myAnim);
            }
        }
    }

    private void RateApp() {

        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);

        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {

            return true;
        } else {

            OpenIntentNoNetworkActivity();

            return false;
        }
    }
}