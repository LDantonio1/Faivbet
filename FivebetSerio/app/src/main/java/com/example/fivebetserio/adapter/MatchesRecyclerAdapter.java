package com.example.fivebetserio.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fivebetserio.database.LeaguesRoomDatabase;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.R;


import java.util.List;

public class MatchesRecyclerAdapter extends RecyclerView.Adapter<MatchesRecyclerAdapter.ViewHolder> {

    private int layout;
    private List<Match> matchesList;
    private String leagueKey;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewHomeTeam;
        private final TextView textViewAwayTeam;
        private final TextView textViewDate;
        private final TextView textViewUno;
        private final TextView textViewX;
        private final TextView textViewDue;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textViewHomeTeam = view.findViewById(R.id.homeTeam);
            textViewAwayTeam = view.findViewById(R.id.awayTeam);
            textViewDate = view.findViewById(R.id.date);
            textViewUno = view.findViewById(R.id.quota1);
            textViewX = view.findViewById(R.id.quota2);
            textViewDue = view.findViewById(R.id.quota3);
        }

        public TextView getTextViewHomeTeam() {
            return textViewHomeTeam;
        }
        public TextView getTextViewAwayTeam() {
            return textViewAwayTeam;
        }
        public TextView getTextViewDate() {
            return textViewDate;
        }

        public TextView getTextViewUno() {
            return textViewUno;
        }

        public TextView getTextViewX() {
            return textViewX;
        }

        public TextView getTextViewDue() {
            return textViewDue;
        }
    }

    public MatchesRecyclerAdapter(int layout, List<Match> matchesList) {
        this.layout = layout;
        this.matchesList = matchesList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Match match = matchesList.get(position);

        // Popola il layout con i dati del match
        viewHolder.getTextViewHomeTeam().setText(match.getHome_team());
        viewHolder.getTextViewAwayTeam().setText(match.getAway_team());
        viewHolder.getTextViewDate().setText(match.getCommence_time());
        // Controllo se la lista dei bookmakers è vuota
        if (match.getBookmakers() != null && !match.getBookmakers().isEmpty()) {
            // Se la lista non è vuota, accedi ai dati
            viewHolder.getTextViewUno().setText("1\n" + match.getBookmakers().get(0).getMarkets().get(0).getOutcomes().get(1).getPrice());
            viewHolder.getTextViewX().setText("X\n" + match.getBookmakers().get(0).getMarkets().get(0).getOutcomes().get(2).getPrice());
            viewHolder.getTextViewDue().setText("2\n" + match.getBookmakers().get(0).getMarkets().get(0).getOutcomes().get(0).getPrice());
        } else {
            // Se la lista è vuota, mostra "**" al posto del prezzo
            viewHolder.getTextViewUno().setText("1\n**");
            viewHolder.getTextViewX().setText("X\n**");
            viewHolder.getTextViewDue().setText("2\n**");
        }
    }


    @Override
    public int getItemCount() {
        return matchesList.size();
    }
}

