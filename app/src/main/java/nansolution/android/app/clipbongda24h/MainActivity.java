package nansolution.android.app.clipbongda24h;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.vungle.publisher.EventListener;
import com.vungle.publisher.VunglePub;

import nansolution.android.app.clipbongda24h.fragments.AllLeaguesFragment;
import nansolution.android.app.clipbongda24h.fragments.LeagueFragment;
import nansolution.android.app.clipbongda24h.fragments.MatchDetailsFragment;
import nansolution.android.app.clipbongda24h.interfaces.ClipBongDaApis;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity /*implements AppBarLayout.OnOffsetChangedListener */{

    final String BASE_URL = "http://123.30.132.164:7070/";
    Retrofit retrofit;
    ClipBongDaApis apiService;
    Toolbar toolbar;
    AllLeaguesFragment mainFragment;

    LeagueFragment.CustomAdsListener adsListener;
    private boolean isAdAvailable = false;

    private boolean isWaitingForClose;

    final VunglePub vunglePub = VunglePub.getInstance();
    final String VungleApplicationID = "580a4c1407267fb375000036";

    private TextView txtTitle;

//    private ImageView ivSearch;
//    private FloatingSearchView floatingSearchView;
//    private AppBarLayout mAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        toolbar.setVisibility(VISIBLE);
        /*ivSearch = (ImageView) findViewById(R.id.iv_search);
        floatingSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);*/
//        floatingSearchView.setVisibility(GONE);

        /*mAppBar = (AppBarLayout) findViewById(R.id.appbar);

        mAppBar.addOnOffsetChangedListener(this);*/

        /*ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivSearch.setVisibility(View.GONE);
                floatingSearchView.setVisibility(View.VISIBLE);
                floatingSearchView.requestFocus();
                showInputMethod(floatingSearchView);

            }
        });*/

        /*floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {

            }
        });*/

        setSupportActionBar(toolbar);
        setTextTitle(getString(R.string.app_name));
//        getSupportActionBar().setHideOnContentScrollEnabled(true);

        // build retrofit object
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ClipBongDaApis.class);
        mainFragment = new AllLeaguesFragment();

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentContainer, mainFragment);
        ft.commit();
        if (AppUtils.isOffline(this)) {
            Toast.makeText(this, "Please check your network and re-open app or pull to refresh", Toast.LENGTH_LONG).show();
        }

        // VungLe Ads
        vunglePub.init(this, VungleApplicationID);
        vunglePub.addEventListeners(new EventListener() {
            @Override
            public void onAdEnd(boolean b, boolean b1) {
                Log.v("THAIPD", " Ad Ends");
                if (adsListener != null) {
                    Log.i("THAIPD", " start Listener");
                    adsListener.onAdFinished();
                }
            }

            @Override
            public void onAdStart() {
                Log.d("THAIPD", " Ad Starts");
            }

            @Override
            public void onAdUnavailable(String s) {
                Log.v("THAIPD", " onAdUnavailable : " + s);
            }

            @Override
            public void onAdPlayableChanged(boolean b) {
                Log.v("THAIPD", " Ad Playable Changed " + b);
                isAdAvailable = b;
            }

            @Override
            public void onVideoView(boolean b, int i, int i1) {
                Log.i("THAIPD", " OnVideoView " + b + " " + i + "  " + i1);
            }
        });
        isWaitingForClose = false;
    }

    private void showInputMethod(final View view) {
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.showSoftInput(view, 0);
                }
            }
        }, 200);

    }

    public void setTextTitle(String title) {
        txtTitle.setText(title);
    }

    public void openMatchFragment(String matchId) {
            MatchDetailsFragment fragment = MatchDetailsFragment.newInstance(matchId);
            adsListener = fragment;
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.add(R.id.fragmentContainer, fragment);
            ft.hide(mainFragment);
            ft.addToBackStack(matchId);
            ft.commit();
            requestLandscapeMode();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showMainScreen() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(mainFragment);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public void requestFullScreen(boolean isFullScreen) {
        try {
            if (isFullScreen) {
                // hide toolbar
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

                if (getSupportActionBar() != null && getSupportActionBar().isShowing()) {
                    Log.d("THAIPD", " HIDE ACTION BAR");
                    getSupportActionBar().hide();

                }

            } else {
                Log.v("THAIPD", " REQUEST EXIT FULL SCREEN");
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                // show toolbar
                if (getSupportActionBar() != null && !getSupportActionBar().isShowing()) {
                    getSupportActionBar().show();
                    Log.d("THAIPD", " SHOW ACTION BAR");
                }
            }
        } catch (Exception e) {
            Log.e("THAIPD", e.getMessage());
        }
    }

    public void requestPortraitMode() {
        requestFullScreen(false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void requestLandscapeMode() {
        requestFullScreen(true);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public ClipBongDaApis getApiService() {
        return apiService;
    }

    public void showAds() {
        vunglePub.playAd();
    }

    public boolean isAdAvailable() {
        return isAdAvailable;
    }

    @Override
    public void onBackPressed() {
        if (mainFragment.isHidden()) {
            showMainScreen();
            requestPortraitMode();
            super.onBackPressed();
        } else {
            /*if (floatingSearchView.getVisibility() == View.VISIBLE) {
                floatingSearchView.setVisibility(GONE);
            } else*/ if (isWaitingForClose) {
                super.onBackPressed();
            } else {
                isWaitingForClose = true;
                Toast.makeText(this, getString(R.string.press_more_to_close), Toast.LENGTH_SHORT).show();
                new CountDownTimer(2000, 2000) {
                    @Override
                    public void onTick(long l) {
                    }

                    @Override
                    public void onFinish() {
                        isWaitingForClose = false;
                    }
                }.start();
            }
        }

    }

    /*@Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
//        toolbar.setTranslationY(verticalOffset);
    }*/

    @Override
    protected void onPause() {
        super.onPause();
        vunglePub.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        vunglePub.onResume();
    }

    public void printStatus() {
        Log.v("THAIPD", "ACTIONBAR IS " + getSupportActionBar().isShowing());
    }
}
