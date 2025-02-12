package com.example.fivebetserio.adapter;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fivebetserio.R;
import com.example.fivebetserio.database.LeagueDao;
import com.example.fivebetserio.database.MatchDao;
import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.service.ServiceLocator;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LeaguesRecyclerAdapter extends RecyclerView.Adapter<LeaguesRecyclerAdapter.ViewHolder> {

    private int layout;
    private List<League> leaguesList;
    private Context context;
    private LeagueDao leagueDAO;
    private Executor executor;
    private Handler uiHandler;

    public LeaguesRecyclerAdapter(int layout, List<League> leagueList, Context context, Application application) {
        this.layout = layout;
        this.leaguesList = leagueList;
        this.context = context;
        this.leagueDAO = ServiceLocator.getInstance().getLeaguesDB(application).leagueDao();
        this.executor = Executors.newSingleThreadExecutor();
        this.uiHandler = new Handler(Looper.getMainLooper());
    }

    public void setLeaguesList(List<League> leaguesList) {
        this.leaguesList = leaguesList;
        notifyDataSetChanged();
    }

    public void updateMatches() {
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (position < 0 || position >= leaguesList.size()) {
            return;
        }

        League currentLeague = leaguesList.get(position);
        viewHolder.textViewTitle.setText(currentLeague.getTitle());

        MatchesRecyclerAdapter matchAdapter = (MatchesRecyclerAdapter) viewHolder.recyclerViewMatches.getAdapter();
        if (matchAdapter == null) {
            matchAdapter = new MatchesRecyclerAdapter(R.layout.item_match, new ArrayList<>());
            viewHolder.recyclerViewMatches.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            viewHolder.recyclerViewMatches.setAdapter(matchAdapter);
        }

        loadMatchesForLeague(currentLeague, matchAdapter);

        viewHolder.favoriteCheckbox.setChecked(currentLeague.isLiked());
        viewHolder.favoriteCheckbox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            League currentLeagueChecked = leaguesList.get(viewHolder.getAdapterPosition());
            currentLeagueChecked.setLiked(isChecked);

            executor.execute(() -> {
                leagueDAO.updateLeague(currentLeagueChecked);
                uiHandler.post(() -> notifyItemChanged(viewHolder.getAdapterPosition()));
            });
        });
    }

    @Override
    public int getItemCount() {
        return leaguesList.size();
    }

    private void loadMatchesForLeague(League league, MatchesRecyclerAdapter adapter) {
        executor.execute(() -> {
            MatchDao matchDAO = ServiceLocator.getInstance().getLeaguesDB((Application) context.getApplicationContext()).matchDao();
            List<Match> matchesForLeague = matchDAO.getMatchesByLeague(league.getUid());

            uiHandler.post(() -> {
                adapter.setMatchesList(matchesForLeague);
                updateMatches(); // 🔥 Aggiorna la UI dopo il caricamento dei match
            });
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final RecyclerView recyclerViewMatches;
        private final CheckBox favoriteCheckbox;

        public ViewHolder(View view) {
            super(view);
            textViewTitle = view.findViewById(R.id.Title);
            recyclerViewMatches = view.findViewById(R.id.recyclerViewMatches);
            favoriteCheckbox = view.findViewById(R.id.checkBoxFavorite);
        }
    }
}
