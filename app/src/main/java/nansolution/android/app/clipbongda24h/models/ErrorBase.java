package nansolution.android.app.clipbongda24h.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by phandinhthai on 10/10/16.
 */

public class ErrorBase {

    @SerializedName("message")
    String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
