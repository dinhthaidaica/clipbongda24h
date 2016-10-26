package nansolution.android.app.clipbongda24h.utils.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import nansolution.android.app.clipbongda24h.models.Category;
import nansolution.android.app.clipbongda24h.models.Match;

/**
 * Created by phandinhthai on 10/10/16.
 */

public class ListMatchesResponse extends ResponseBase {

    @SerializedName("categories")
    List<Category> categories;

    @SerializedName("matches")
    List<Match> data;

    @SerializedName("total")
    int total;

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Match> getData() {
        return data;
    }

    public void setData(List<Match> data) {
        this.data = data;
    }
}
