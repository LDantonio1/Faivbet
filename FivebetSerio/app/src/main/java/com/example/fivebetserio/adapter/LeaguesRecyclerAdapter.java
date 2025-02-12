package com.example.fivebetserio.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fivebetserio.R;
import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.ui.home.viewModel.league.LeagueViewModel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Adapter per la RecyclerView che gestisce l'elenco delle leghe e i relativi match.
 * Ogni elemento della lista mostra il nome della lega, un'icona di preferenza
 * e una RecyclerView annidata con i match corrispondenti.
 */

public class LeaguesRecyclerAdapter extends RecyclerView.Adapter<LeaguesRecyclerAdapter.ViewHolder> {

    private int layout;
    private List<League> leaguesList;
    private List<Match> allMatchesList;
    private Context context;
    private LeagueViewModel leagueViewModel;
    private Executor executor;
    private Handler uiHandler;


    public LeaguesRecyclerAdapter(int layout, List<League> leaguesList, List<Match> allMatchesList, Context context, LeagueViewModel leagueViewModel) {
        this.layout = layout;
        this.leaguesList = leaguesList;
        this.allMatchesList = allMatchesList;
        this.context = context;
        this.executor = Executors.newSingleThreadExecutor();
        this.uiHandler = new Handler(Looper.getMainLooper());
        this.leagueViewModel = leagueViewModel;
    }

    public void setLeaguesList(List<League> leaguesList) {
        this.leaguesList = leaguesList;
        notifyDataSetChanged();
    }

    public void setAllMatchesList(List<Match> allMatchesList) {
        this.allMatchesList = allMatchesList;
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

        // Filtra i match per la lega corrente
        List<Match> filteredMatches = filterMatchesForLeague(currentLeague.getUid());

        // Aggiorna i dati nell'adapter esistente
        viewHolder.matchAdapter.setMatchesList(filteredMatches);

        viewHolder.favoriteCheckbox.setOnCheckedChangeListener(null); // Prevent callback hell
        viewHolder.favoriteCheckbox.setChecked(currentLeague.isLiked());
        viewHolder.favoriteCheckbox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            int adapterPosition = viewHolder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                League currentLeagueChecked = leaguesList.get(adapterPosition);
                currentLeagueChecked.setLiked(isChecked);

                executor.execute(() -> {
                    leagueViewModel.updateLeague(currentLeagueChecked);
                    uiHandler.post(() -> notifyItemChanged(adapterPosition));
                });
            }
        });
    }

    private List<Match> filterMatchesForLeague(Long leagueId) {
        List<Match> filteredMatches = new ArrayList<>();
        for (Match match : allMatchesList) {
            if (match.getLeagueUid() == leagueId) {
                filteredMatches.add(match);
                //Log.d("LeaguesRecyclerAdapter", "Match aggiunto al filtro per la lega " + leagueId + ": " + match.getHome_team() + " vs " + match.getAway_team());
            }
        }
        //Log.d("LeaguesRecyclerAdapter", "Trovati " + filteredMatches.size() + " match per la lega " + leagueId);
        return filteredMatches;
    }

    @Override
    public int getItemCount() {
        return leaguesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final RecyclerView recyclerViewMatches;
        private final CheckBox favoriteCheckbox;
        public MatchesRecyclerAdapter matchAdapter;

        public ViewHolder(View view) {
            super(view);
            textViewTitle = view.findViewById(R.id.Title);
            recyclerViewMatches = view.findViewById(R.id.recyclerViewMatches);
            favoriteCheckbox = view.findViewById(R.id.checkBoxFavorite);

            // Inizializza LayoutManager e Adapter
            LinearLayoutManager layoutManager = new LinearLayoutManager(view.getContext(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewMatches.setLayoutManager(layoutManager);
            matchAdapter = new MatchesRecyclerAdapter(R.layout.item_match, new ArrayList<>());
            recyclerViewMatches.setAdapter(matchAdapter);
        }
    }
}