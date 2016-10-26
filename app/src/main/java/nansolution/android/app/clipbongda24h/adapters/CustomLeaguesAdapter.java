package nansolution.android.app.clipbongda24h.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import nansolution.android.app.clipbongda24h.fragments.LeagueFragment;
import nansolution.android.app.clipbongda24h.models.League;

/**
 * Created by phandinhthai on 10/15/16.
 */

public class CustomLeaguesAdapter extends FragmentStatePagerAdapter {

    List<LeagueFragment> data;
    List<League> leagues;


    public CustomLeaguesAdapter(FragmentManager fm, List<League> leagueData) {
        super(fm);
        leagues = leagueData;
        buildFragmentsData();
    }

    private void buildFragmentsData() {
        data = new ArrayList<>();
        for (int i = 0; i < leagues.size(); i++) {
            LeagueFragment fragment = LeagueFragment.getNewInstance(leagues.get(i).getLeagueId());
            data.add(fragment);
        }
    }

    public List<LeagueFragment> getData() {
        return data;
    }

    public void setData(List<LeagueFragment> data) {
        this.data = data;
    }

    public List<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(List<League> leagues) {
        this.leagues = leagues;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = data.get(position);
        return f;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}
