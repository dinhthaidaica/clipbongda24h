package nansolution.android.app.clipbongda24h.utils.network;

import com.google.gson.annotations.SerializedName;

import nansolution.android.app.clipbongda24h.models.ErrorBase;

/**
 * Created by phandinhthai on 10/10/16.
 */

public class ResponseBase {

    @SerializedName("error")
    ErrorBase error;

    public ErrorBase getError() {
        return error;
    }

    public void setError(ErrorBase error) {
        this.error = error;
    }
}
