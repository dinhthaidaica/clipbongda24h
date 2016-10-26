package nansolution.android.app.clipbongda24h.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by phandinhthai on 10/14/16.
 */

public class Category {

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
