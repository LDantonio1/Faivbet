package com.example.fivebetserio.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.fivebetserio.R;
import com.example.fivebetserio.model.Match;
import java.util.ArrayList;
import java.util.List;

public class MatchesRecyclerAdapter extends RecyclerView.Adapter<MatchesRecyclerAdapter.ViewHolder> {

    private int layout;
    private List<Match> matchesList;

    public MatchesRecyclerAdapter(int layout, List<Match> matchesList) {
        this.layout = layout;
        this.matchesList = new ArrayList<>(matchesList);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewHomeTeam, textViewAwayTeam, textViewDate, textViewUno, textViewX, textViewDue;

        public ViewHolder(View view) {
            super(view);
            textViewHomeTeam = view.findViewById(R.id.homeTeam);
            textViewAwayTeam = view.findViewById(R.id.awayTeam);
            textViewDate = view.findViewById(R.id.date);
            textViewUno = view.findViewById(R.id.quota1);
            textViewX = view.findViewById(R.id.quota2);
            textViewDue = view.findViewById(R.id.quota3);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        Match match = matchesList.get(position);
        viewHolder.textViewHomeTeam.setText(match.getHome_team());
        viewHolder.textViewAwayTeam.setText(match.getAway_team());
        viewHolder.textViewDate.setText(match.getCommence_time());

        if (match.getBookmakers() != null && !match.getBookmakers().isEmpty()) {
            viewHolder.textViewUno.setText("1\n" + match.getBookmakers().get(0).getMarkets().get(0).getOutcomes().get(1).getPrice());
            viewHolder.textViewX.setText("X\n" + match.getBookmakers().get(0).getMarkets().get(0).getOutcomes().get(2).getPrice());
            viewHolder.textViewDue.setText("2\n" + match.getBookmakers().get(0).getMarkets().get(0).getOutcomes().get(0).getPrice());
        } else {
            viewHolder.textViewUno.setText("1\n**");
            viewHolder.textViewX.setText("X\n**");
            viewHolder.textViewDue.setText("2\n**");
        }
    }

    @Override
    public int getItemCount() {
        return matchesList.size();
    }

    // Metodo per aggiornare i dati senza ricreare l'adapter
    public void setMatchesList(List<Match> newMatches) {
        this.matchesList.clear();
        this.matchesList.addAll(newMatches);
        notifyDataSetChanged();
    }
}
