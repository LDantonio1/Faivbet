package com.example.fivebetserio.adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.R;


import java.util.List;

public class MatchesRecyclerAdapter extends RecyclerView.Adapter<MatchesRecyclerAdapter.ViewHolder> {

    private int layout;
    private List<Match> matchesList;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewHomeTeam;
        private final TextView textViewAwayTeam;
        private final TextView textViewDate;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textViewHomeTeam = view.findViewById(R.id.homeTeam);
            textViewAwayTeam = view.findViewById(R.id.awayTeam);
            textViewDate = view.findViewById(R.id.date);
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

    }

    public MatchesRecyclerAdapter(int layout, List<Match> matchesList) {
        this.layout = layout;
        this.matchesList = matchesList;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextViewHomeTeam().setText(matchesList.get(position).getHome_team());
        viewHolder.getTextViewAwayTeam().setText(matchesList.get(position).getAway_team());
        viewHolder.getTextViewDate().setText(matchesList.get(position).getCommence_time());
    }


    @Override
    public int getItemCount() {
        return matchesList.size();
    }
}

