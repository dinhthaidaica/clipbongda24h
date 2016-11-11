package nansolution.android.app.clipbongda24h.models;

/**
 * Created by phandinhthai on 10/14/16.
 */

public class League {

    String leagueName;
    String leagueId;

    public League(String id, String name) {
        this.leagueName = name;
        this.leagueId = id;
    }

    public String getLeagueName() {
        return leagueName;
    }

    public String getLeagueId() {
        return leagueId;
    }

}
