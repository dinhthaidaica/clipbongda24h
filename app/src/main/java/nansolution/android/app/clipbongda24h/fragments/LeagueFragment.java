package nansolution.android.app.clipbongda24h.fragments;

import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import nansolution.android.app.clipbongda24h.MainActivity;
import nansolution.android.app.clipbongda24h.R;
import nansolution.android.app.clipbongda24h.interfaces.ClipBongDaApis;
import nansolution.android.app.clipbongda24h.models.Match;
import nansolution.android.app.clipbongda24h.utils.network.ListMatchesResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by phandinhthai on 10/13/16.
 */

public class LeagueFragment extends Fragment {


    private final int LIMIT = 10;
    private View view;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    ListMatchesAdapter adapter;
    private MainActivity mActivity;
    List<Match> matchData = new ArrayList<>();
    ProgressDialog dialog;
    private int currentPage;
    private int totalVideos;

    private String leagueId;
    private static final String LEAGUE_ID =  "LeagueId";

    public String getLeagueId() {
        return leagueId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            leagueId = getArguments().getString(LEAGUE_ID);
        }
        this.mActivity = (MainActivity) getActivity();
    }

    public static LeagueFragment getNewInstance(String leagueId) {
        LeagueFragment fragment = new LeagueFragment();
        Bundle bundle = new Bundle();
        bundle.putString(LEAGUE_ID, leagueId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_pulltorefresh_base, container, false);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        adapter = new ListMatchesAdapter();
        recyclerView.setAdapter(adapter);
        dialog = new ProgressDialog(getContext());
        dialog.setMessage(getString(R.string.data_loading));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(true);
                }
                matchData.clear();
                currentPage = 1;
                getMatchesDataFromServer(currentPage);
            }
        });
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));

        getMatchesDataFromServer(1);
        matchData.clear();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void loadMore() {
        if (totalVideos > matchData.size()) {
            getMatchesDataFromServer(currentPage + 1);
        } else {
            Toast.makeText(getContext(), "Data had ended", Toast.LENGTH_LONG).show();
        }
    }

    private void getMatchesDataFromServer(int page) {
        if (mActivity == null) {
            this.mActivity = (MainActivity) getActivity();
        }
        currentPage = page;
        ClipBongDaApis api = mActivity.getApiService();

        if (leagueId == null) {
            Call<ListMatchesResponse> call = api.getDataForHomePage(page, LIMIT);
            call.enqueue(new Callback<ListMatchesResponse>() {
                @Override
                public void onResponse(Call<ListMatchesResponse> call, Response<ListMatchesResponse> response) {
                    dialog.hide();
                    disableSwiping();
                    if (response.isSuccessful()) {
                        List<Match> data = response.body().getData();
                        matchData.addAll(data);
                        adapter.notifyDataSetChanged();
                        totalVideos = response.body().getTotal();
                    } else {
                        Toast.makeText(getContext(), "Error occur while getting data", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ListMatchesResponse> call, Throwable t) {
                    dialog.hide();
                    disableSwiping();
                    Toast.makeText(getContext(), "Fail to get data", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Call<ListMatchesResponse> call = api.getMatchesByCategory(leagueId, page, LIMIT);
            call.enqueue(new Callback<ListMatchesResponse>() {
                @Override
                public void onResponse(Call<ListMatchesResponse> call, Response<ListMatchesResponse> response) {
                    dialog.hide();
                    if (response.isSuccessful()) {
                        List<Match> data = response.body().getData();
                        matchData.addAll(data);
                        adapter.notifyDataSetChanged();
                        totalVideos = response.body().getTotal();
                    } else {
                        Toast.makeText(getContext(), "Error occur while getting data", Toast.LENGTH_SHORT).show();
                    }
                    disableSwiping();
                }

                @Override
                public void onFailure(Call<ListMatchesResponse> call, Throwable t) {
                    dialog.hide();
                    disableSwiping();
                    Toast.makeText(getContext(), "Fail to get data", Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public class ListMatchesAdapter extends RecyclerView.Adapter<MatchItemViewHolder> {

        final SimpleDateFormat sdf = new SimpleDateFormat(("EEEE, dd/MM/yyyy"));

        @Override
        public MatchItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_match_item, parent, false);
            return new MatchItemViewHolder(convertView);
        }

        @Override
        public void onBindViewHolder(MatchItemViewHolder holder, int position) {
            Match match = matchData.get(position);
            Picasso.with(getContext()).load(match.getImage())
                    .placeholder(R.drawable.videopholder)
                    .fit()
                    .centerCrop()
                    .into(holder.thumbImageView);
            String description = match.getName();
            if (match.getCreated() != null) {
                Date date = new Date((long)(match.getCreated() * 1000));
                description += " <br> (" + sdf.format(date) + ") </br>";
            }
            holder.titleTextView.setText(Html.fromHtml(description));

            if (position + 1 == matchData.size() && totalVideos > matchData.size()) {
                loadMore();
            }
        }

        @Override
        public int getItemCount() {
            return matchData.size();
        }
    }

    private void requestOpenMatch(String matchId) {
        mActivity.openMatchFragment(matchId);
    }

    private void disableSwiping() {
        if (swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        }
    }

    public class MatchItemViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbImageView;
        TextView titleTextView;

        public MatchItemViewHolder(View itemView) {
            super(itemView);
            thumbImageView = (ImageView) itemView.findViewById(R.id.imgThumbnail);
            titleTextView = (TextView) itemView.findViewById(R.id.title);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getAdapterPosition();
                    requestOpenMatch(matchData.get(pos).getId());
                }
            });
        }
    }

    public interface CustomAdsListener {
        void onAdFinished();
    }
}
