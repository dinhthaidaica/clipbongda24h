package nansolution.android.app.clipbongda24h.utils.network;

/**
 * Created by phandinhthai on 10/10/16.
 */

public class ServerConnector {

    private static ServerConnector mInstance;

    final String BASE_URL = "http://polls.apiblueprint.org/";

    private ServerConnector() {

    }

    public static ServerConnector getInstance() {
        if (mInstance == null) {
            mInstance = new ServerConnector();
        }

        return mInstance;
    }

}
