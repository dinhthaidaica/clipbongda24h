package nansolution.android.app.clipbongda24h.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gigamole.navigationtabbar.ntb.NavigationTabBar;

import java.util.ArrayList;
import java.util.List;

import nansolution.android.app.clipbongda24h.AppUtils;
import nansolution.android.app.clipbongda24h.adapters.CustomLeaguesAdapter;
import nansolution.android.app.clipbongda24h.MainActivity;
import nansolution.android.app.clipbongda24h.R;
import nansolution.android.app.clipbongda24h.models.League;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllLeaguesFragment#newInstance} factory method to
 * This is main screen
 */
public class AllLeaguesFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private MainActivity mActivity;

    private List<League> dataLeagues;
    private List<Integer> resources = new ArrayList<>();
    private View view;
    private CustomLeaguesAdapter adapter;
    private ViewPager viewPager;
    private NavigationTabBar navigationTabBar;
    public AllLeaguesFragment() {
        // Required empty public constructor
        dataLeagues = AppUtils.getLeagueData();
        resources.add(R.drawable.icon_home);
        resources.add(R.drawable.icon_epl2);
        resources.add(R.drawable.icon_laliga);
        resources.add(R.drawable.icon_champions_leagues);
        resources.add(R.drawable.icon_serie_a);
        resources.add(R.drawable.icon_bundesliga);
        resources.add(R.drawable.icon_league1);
        resources.add(R.drawable.icon_worldcup);
        resources.add(R.drawable.icon_others);
    }

    public static AllLeaguesFragment newInstance(String param1, String param2) {
        AllLeaguesFragment fragment = new AllLeaguesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mActivity = (MainActivity) getActivity();
        view = inflater.inflate(R.layout.mainscreen, container, false);
        viewPager = (ViewPager) view.findViewById(R.id.vp_vertical_ntb);
        navigationTabBar = (NavigationTabBar) view.findViewById(R.id.ntb_vertical);
        if (adapter == null) {
            adapter = new CustomLeaguesAdapter(getChildFragmentManager(), dataLeagues);
        }
        initUI();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mActivity.getSupportActionBar() != null && !mActivity.getSupportActionBar().isShowing()) {
            mActivity.getSupportActionBar().show();
        }
    }

    private void initUI() {
        viewPager.setOffscreenPageLimit(dataLeagues.size());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                League league = adapter.getLeagues().get(position);
                String leagueName = league.getLeagueName();
                mActivity.setTextTitle(leagueName);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        final String[] colors = getResources().getStringArray(R.array.leagues_colors);

        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();

        for (int i = 0; i < dataLeagues.size(); i++) {
            models.add(
                    new NavigationTabBar.Model.Builder(
                            getResources().getDrawable(resources.get(i)),
                            Color.parseColor(colors[i]))
                            .build()
            );
        }
        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 0);
    }

}
