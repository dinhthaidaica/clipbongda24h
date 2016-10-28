package nansolution.android.app.clipbongda24h;

import android.content.Context;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import nansolution.android.app.clipbongda24h.fragments.AllLeaguesFragment;
import nansolution.android.app.clipbongda24h.fragments.MatchDetailsFragment;
import nansolution.android.app.clipbongda24h.interfaces.ClipBongDaApis;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements RewardedVideoAdListener, AppBarLayout.OnOffsetChangedListener {

    final String BASE_URL = "http://123.30.132.164:7070/";
    Retrofit retrofit;
    ClipBongDaApis apiService;
    Toolbar toolbar;
    AllLeaguesFragment mainFragment;

    private boolean isWaitingForClose;

    private RewardedVideoAd mAd;

    private TextView txtTitle;

    private ImageView ivSearch;
    private FloatingSearchView floatingSearchView;
    private AppBarLayout mAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        txtTitle = (TextView) findViewById(R.id.txt_title);
        ivSearch = (ImageView) findViewById(R.id.iv_search);
        floatingSearchView = (FloatingSearchView) findViewById(R.id.floating_search_view);
        floatingSearchView.setVisibility(View.GONE);

        mAppBar = (AppBarLayout) findViewById(R.id.appbar);

        mAppBar.addOnOffsetChangedListener(this);

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                ivSearch.setVisibility(View.GONE);
                floatingSearchView.setVisibility(View.VISIBLE);
                floatingSearchView.requestFocus();
                showInputMethod(floatingSearchView);

            }
        });

        floatingSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {
            @Override
            public void onSearchTextChanged(String oldQuery, String newQuery) {

            }
        });

        setTextTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);

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
        mAd = MobileAds.getRewardedVideoAdInstance(this);
        mAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
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

    private void loadRewardedVideoAd() {
        mAd.loadAd(getString(R.string.ads_id), new AdRequest.Builder().addTestDevice("05FA71BABA44AE8A697393E95A652665").build());
    }

    public void openMatchFragment(String matchId) {
        MatchDetailsFragment fragment = MatchDetailsFragment.newInstance(matchId);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragmentContainer, fragment);
        ft.hide(mainFragment);
        ft.addToBackStack(matchId);
        ft.commit();
        requestLandscapeMode();
    }

    public void backToMainScreen() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.show(mainFragment);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    public void requestFullScreen(boolean isFullScreen) {
        if (isFullScreen) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            // hide toolbar
            if (getSupportActionBar() != null && getSupportActionBar().isShowing()) {
                getSupportActionBar().hide();
            }
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            // show toolbar
            if (getSupportActionBar() != null && !getSupportActionBar().isShowing()) {
                getSupportActionBar().show();
            }
        }
    }

    public void requestPortraitMode() {
        requestFullScreen(false);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void requestLandscapeMode() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    public ClipBongDaApis getApiService() {
        return apiService;
    }

    @Override
    public void onRewardedVideoAdLoaded() {
        printMessage("onRewardedVideoAdLoaded");
    }

    @Override
    public void onRewardedVideoAdOpened() {
        printMessage("onRewardedVideoAdOpened");
    }

    @Override
    public void onRewardedVideoStarted() {
        printMessage("onRewardedVideoStarted");
    }

    @Override
    public void onRewardedVideoAdClosed() {
        printMessage("onRewardedVideoAdClosed");
    }

    @Override
    public void onRewarded(RewardItem rewardItem) {
        printMessage("onRewarded! currency: ");
    }

    @Override
    public void onRewardedVideoAdLeftApplication() {
        printMessage("onRewardedVideoAdLeftApplication");

    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int i) {
        printMessage("onRewardedVideoAdFailedToLoad ");
    }

    private void printMessage(String message) {
        Log.d("THAIPD ADS", message);
    }

    public void showAds() {
        mAd.show();
    }

    public boolean isAdLoaded() {
        return mAd.isLoaded();
    }

    @Override
    public void onBackPressed() {
        if (mainFragment.isHidden()) {
            backToMainScreen();
            requestPortraitMode();
            super.onBackPressed();
        } else {
            if (floatingSearchView.getVisibility() == View.VISIBLE) {
                floatingSearchView.setVisibility(View.GONE);
            } else if (isWaitingForClose) {
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

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        Log.d("THAIPD", "onOffsetChanged " + verticalOffset);
        toolbar.setTranslationY(verticalOffset);
    }
}
