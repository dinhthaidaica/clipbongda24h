package nansolution.android.app.clipbongda24h.fragments;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.hanks.htextview.HTextView;
import com.hanks.htextview.HTextViewType;

import java.util.List;

import nansolution.android.app.clipbongda24h.MainActivity;
import nansolution.android.app.clipbongda24h.R;
import nansolution.android.app.clipbongda24h.models.Match;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MatchDetailsFragment extends Fragment implements View.OnClickListener, LeagueFragment.CustomAdsListener {
    private static final String MATCH_ID = "MATCHID";
    private String matchId;
    MainActivity mActivity;
    VideoView videoView;
    ProgressDialog mProgressDialog;

    private final int FIRST_POSITION = 0;
    private int currentPosition = 0;

    private RelativeLayout resultLayout;
    private HTextView resultText;
    private ImageButton closeButton;
    private RelativeLayout replayButton;
    private TextView replayText;
    private TextView countDownText;

    private List<String> matchUrls;

    private String textResult = "";

    private Handler mHandler;

    private CountDownTimer timer = new CountDownTimer(5000, 500) {
        boolean isOn = true;
        int counter = 5;
        final int sdk = android.os.Build.VERSION.SDK_INT;
        @Override
        public void onFinish() {
            mActivity.onBackPressed();
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void onTick(long l) {
            try {
                if (isOn) {
                    Log.e("THAIPD", " Counter = " + counter);
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        replayButton.setBackgroundDrawable( getResources().getDrawable(R.drawable.bg_replay_btn) );
                    } else {
                        replayButton.setBackground( getResources().getDrawable(R.drawable.bg_replay_btn));
                    }
                    replayText.setTextColor(getResources().getColor(android.R.color.white));
                    countDownText.setTextColor(getResources().getColor(android.R.color.white));
                    counter --;
                    countDownText.setText("" + counter);
                } else {
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        replayButton.setBackgroundDrawable( getResources().getDrawable(R.drawable.bg_replay_btn) );
                    } else {
                        replayButton.setBackground( getResources().getDrawable(R.drawable.bg_replay_btn));
                    }
                    replayText.setTextColor(getResources().getColor(android.R.color.white));
                    countDownText.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            } catch (IllegalStateException e) {
                // dont care
            }
            isOn = !isOn;
        }
    };

    public MatchDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment MatchDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MatchDetailsFragment newInstance(String matchId) {
        MatchDetailsFragment fragment = new MatchDetailsFragment();
        Bundle args = new Bundle();
        args.putString(MATCH_ID, matchId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            matchId = getArguments().getString(MATCH_ID);
        }
        mHandler = new Handler();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_match_detail, container, false);
        videoView = (VideoView) view.findViewById(R.id.main_video);
        mActivity = (MainActivity) getActivity();
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setMessage(getString(R.string.video_loading));
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setCancelable(false);


        resultLayout = (RelativeLayout) view.findViewById(R.id.result_layout);
        resultText = (HTextView) view.findViewById(R.id.txt_result);
        closeButton = (ImageButton) view .findViewById(R.id.closed_button);
        replayButton = (RelativeLayout) view.findViewById(R.id.replay_layout);
        replayText = (TextView) view.findViewById(R.id.text_replay);
        countDownText = (TextView) view.findViewById(R.id.text_CountDown);
        hideResultLayout();

        closeButton.setOnClickListener(this);
        replayButton.setOnClickListener(this);

        mActivity = (MainActivity) getActivity();

        loadVideo();
        return view;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.closed_button:
                mActivity.onBackPressed();
                timer.cancel();
                break;
            case R.id.replay_layout:
                timer.cancel();
                hideResultLayout();
                if (matchUrls.size() > 1) {
                    playMatchVideos(matchUrls, FIRST_POSITION);
                } else {
                    videoView.seekTo(0);
                    videoView.start();
                }

                break;
        }
    }

    private void loadVideo() {
        if (mActivity == null) {
            mActivity = (MainActivity) getActivity();
        }
        Call<Match> call = mActivity.getApiService().getMatchDetails(matchId);
        call.enqueue(new Callback<Match>() {
            @Override
            public void onResponse(Call<Match> call, Response<Match> response) {
                if (response.isSuccessful()) {
                    if (response.body().getUrl() != null && response.body().getUrl().size() > 0) {
                        textResult = response.body().getMatchResult();
                        matchUrls = response.body().getUrl();
                        playMatchVideos(response.body().getUrl(), FIRST_POSITION);
                    } else {
                        Toast.makeText(getContext(), "Video is not existing, so sorry...", Toast.LENGTH_LONG).show();
                        getActivity().onBackPressed();
                    }
                } else {
                    Toast.makeText(getContext(), "Error occur while getting data", Toast.LENGTH_SHORT).show();
                    getActivity().onBackPressed();
                }
            }

            @Override
            public void onFailure(Call<Match> call, Throwable t) {
                getActivity().onBackPressed();
            }
        });
    }

    private void playMatchVideos(final List<String> urls, final int startPosition) {
        if (urls.size() <= startPosition) {
            return;
        }

        mProgressDialog.show();
        String VIDEO_URL = urls.get(startPosition);
        Uri vidUri = Uri.parse(VIDEO_URL);
        videoView.setVideoURI(vidUri);
        final MediaController vidControl = new MediaController(getContext());
        vidControl.setAnchorView(videoView);
        videoView.setMediaController(vidControl);
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                if (mProgressDialog.isShowing()) {
                    mProgressDialog.hide();
                }
                if (startPosition == FIRST_POSITION && mActivity.isAdAvailable()) {
                    mActivity.showAds();
                    videoView.pause();
                }
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                int size = urls.size();
                if (size - 1 > startPosition) {
                    playMatchVideos(urls, startPosition + 1);
                } else {
                    showResultLayout();
                    showMatchResult();
                    mActivity.printStatus();
                }
            }
        });
        videoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                mProgressDialog.hide();
                return false;
            }
        });
        videoView.start();
    }

    private void showMatchResult() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                resultText.setAnimateType(HTextViewType.LINE);
                resultText.animateText(textResult);
            }
        }, 200);
        timer.start();
    }

    private void showResultLayout() {
        resultLayout.bringToFront();
        resultLayout.setVisibility(View.VISIBLE);
        closeButton.setEnabled(true);
        replayButton.setEnabled(true);
        replayButton.setClickable(true);
        closeButton.setClickable(true);

    }

    private void hideResultLayout() {
        resultLayout.setVisibility(View.GONE);
        closeButton.setEnabled(false);
        replayButton.setEnabled(false);
        replayButton.setClickable(false);
        closeButton.setClickable(false);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        currentPosition = videoView.getCurrentPosition();
    }

    @Override
    public void onResume() {
        super.onResume();
        mActivity.requestFullScreen(true);
        if (currentPosition != 0) {
            videoView.seekTo(currentPosition);
            videoView.start();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onAdFinished() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (!videoView.isPlaying()) {
                    mActivity = (MainActivity) getActivity();
                    // hide toolbar
                    mActivity.requestFullScreen(true);
                    mHandler.postAtFrontOfQueue(new Runnable() {
                        @Override
                        public void run() {
                            videoView.seekTo(0);
                            videoView.start();
                        }
                    });
                }
            }
        });
    }
}
