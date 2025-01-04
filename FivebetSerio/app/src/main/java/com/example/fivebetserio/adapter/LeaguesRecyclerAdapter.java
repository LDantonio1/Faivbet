package com.example.fivebetserio.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fivebetserio.R;
import com.example.fivebetserio.database.LeaguesRoomDatabase;
import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.ui.MainActivity;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LeaguesRecyclerAdapter extends RecyclerView.Adapter<LeaguesRecyclerAdapter.ViewHolder> {

    public static final String TAG = MainActivity.class.getName(); //una sorta di ID per identificare questa classe

    private int layout;
    private List<League> leaguesList;
    private Context context;

    public void setLeaguesList(List<League> leaguesList) {
        this.leaguesList = leaguesList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final RecyclerView recyclerViewMatches;
        private final CheckBox favoriteCheckbox;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textViewTitle = view.findViewById(R.id.Title);
            recyclerViewMatches = view.findViewById(R.id.recyclerViewMatches);
            favoriteCheckbox = view.findViewById(R.id.checkBoxFavorite);
        }

        public TextView getTextViewTitle() {
            return textViewTitle;
        }
        public RecyclerView getRecyclerViewMatches() {return recyclerViewMatches;}
        public CheckBox getFavoriteCheckbox() {return favoriteCheckbox;}
    }

    public LeaguesRecyclerAdapter(int layout, List<League> leagueList, Context context) {
        this.layout = layout;
        this.leaguesList = leagueList;
        this.context= context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (position < 0 || position >= leaguesList.size()) {
            Log.e(TAG, "Posizione non valida: " + position + ", Dimensione lista: " + leaguesList.size());
            return; // Evita di accedere a indici non validi
        }

        if (position >= leaguesList.size()) {
            Log.e(TAG, "Posizione non valida: " + position + ", Dimensione lista: " + leaguesList.size());
            return; // Evita di accedere a indici non validi
        }
        League currentLeague = leaguesList.get(position);
        viewHolder.getTextViewTitle().setText(currentLeague.getTitle());

        // Ottieni il database
        LeaguesRoomDatabase db = LeaguesRoomDatabase.getInstance(viewHolder.getTextViewTitle().getContext());

        // Usa un Executor per eseguire la query sul background thread
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            // Recupera i match per la lega corrente in background
            LiveData<List<Match>> matchesForLeague = db.matchDao().getMatchesByLeague(currentLeague.getKey());

            new Handler(Looper.getMainLooper()).post(() -> {
                // Osserva i cambiamenti nella lista dei match nel thread principale
                matchesForLeague.observe((LifecycleOwner) viewHolder.getTextViewTitle().getContext(), new Observer<List<Match>>() {
                    @Override
                    public void onChanged(List<Match> matches) {
                        // Popola il RecyclerView con i nuovi match
                        LinearLayoutManager layoutManager = new LinearLayoutManager(
                                viewHolder.recyclerViewMatches.getContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                        );
                        viewHolder.recyclerViewMatches.setLayoutManager(layoutManager);

                        // Passa la lista dei match all'adapter
                        MatchesRecyclerAdapter matchAdapter = new MatchesRecyclerAdapter(R.layout.item_match, matches);
                        viewHolder.recyclerViewMatches.setAdapter(matchAdapter);
                    }
                });
            });
        });

        viewHolder.getFavoriteCheckbox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                /*League currentLeague = leaguesList.get(viewHolder.getAdapterPosition());
                currentLeague.setLiked(b);

                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(() -> {
                    LeaguesRoomDatabase.getInstance(viewHolder.getTextViewTitle().getContext())
                            .leagueDao()
                            .updateLeague(currentLeague);

                    // Aggiorna il RecyclerView nel thread principale
                    new Handler(Looper.getMainLooper()).post(() -> {
                        notifyItemChanged(viewHolder.getAdapterPosition());
                    });
                });*/
            }
        });

    }


    @Override
    public int getItemCount() {
        return leaguesList.size();
    }

}

