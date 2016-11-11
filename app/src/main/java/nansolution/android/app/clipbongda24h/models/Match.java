package nansolution.android.app.clipbongda24h.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by phandinhthai on 10/10/16.
 */

public class Match {

    @SerializedName("created_at")
    Float created;

    @SerializedName("id")
    String id;

    @SerializedName("name")
    String name;

    @SerializedName("description")
    String description;

    @SerializedName("image")
    String image;

    @SerializedName("video_urls")
    List<String> urls;

    @SerializedName("published_at")
    String published;

    @SerializedName("result")
    String matchResult;

    public String getMatchResult() {
        return matchResult;
    }

    public Float getCreated() {
        return created;
    }

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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getUrl() {
        return urls;
    }
}
