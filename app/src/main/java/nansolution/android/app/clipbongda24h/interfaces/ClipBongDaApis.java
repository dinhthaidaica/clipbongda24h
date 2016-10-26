package nansolution.android.app.clipbongda24h.interfaces;

import nansolution.android.app.clipbongda24h.models.Match;
import nansolution.android.app.clipbongda24h.utils.network.ListMatchesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by phandinhthai on 10/10/16.
 */

public interface ClipBongDaApis {

    @GET("home")
    Call<ListMatchesResponse> getDataForHomePage(@Query("page") int page, @Query("limit") int limit);

    @GET("category")
    Call<ListMatchesResponse> getMatchesByCategory(@Query("id") String name, @Query("page") int page, @Query("limit") int limit);

    @GET("search")
    Call<ListMatchesResponse> searchByKeyWord(@Query("keyword") String keyword, @Query("category_name") String categoryName);

    @GET("match")
    Call<Match> getMatchDetails(@Query("id") String id);
}
