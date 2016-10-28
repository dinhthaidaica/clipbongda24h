package nansolution.android.app.clipbongda24h;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.ArrayList;
import java.util.List;

import nansolution.android.app.clipbongda24h.models.League;

/**
 * Created by phandinhthai on 10/14/16.
 */

public class AppUtils {

    public static boolean isOffline(Context ctx) {
        NetworkInfo info = ((ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (info == null || !info.isConnected() || info.isRoaming()) {
            return true;
        }
        return false;
    }

    public static List<League> getLeagueData() {
        List<League> list = new ArrayList<>();
        list.add(new League(null, "Home - All matches"));
        list.add(new League("EN", "Premier League"));
        list.add(new League("SP", "La Liga"));
        list.add(new League("C1", "Champion League"));
        list.add(new League("IT", "Serie A"));
        list.add(new League("GER", "Bundesliga"));
        list.add(new League("PR", "League One"));
        list.add(new League("WC", "World Cup"));
        list.add(new League("OTHER", "Other Leagues"));

        return list;
    }
}
