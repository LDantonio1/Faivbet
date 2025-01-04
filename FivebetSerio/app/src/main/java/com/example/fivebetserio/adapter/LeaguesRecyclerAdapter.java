package com.example.fivebetserio.adapter;


import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fivebetserio.model.League;
import com.example.fivebetserio.R;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.model.MatchesAPIResponse;
import com.example.fivebetserio.util.Constants;
import com.example.fivebetserio.util.JSONParserUtils;


import java.io.IOException;
import java.util.List;

public class LeaguesRecyclerAdapter extends RecyclerView.Adapter<LeaguesRecyclerAdapter.ViewHolder> {

    private int layout;
    private List<League> leaguesList;
    private List<Match> matchesList;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textViewTitle;
        private final RecyclerView recyclerViewMatches;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textViewTitle = view.findViewById(R.id.Title);
            recyclerViewMatches = view.findViewById(R.id.recyclerViewMatches);
        }

        public TextView getTextViewTitle() {
            return textViewTitle;
        }
        public RecyclerView getRecyclerViewMatches() {return recyclerViewMatches;}
    }

    public LeaguesRecyclerAdapter(int layout, List<League> leagueList, Context context) {
        this.layout = layout;
        this.leaguesList = leagueList;
        this.context= context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(layout, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextViewTitle().setText(leaguesList.get(position).getTitle());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                viewHolder.recyclerViewMatches.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
        );

        JSONParserUtils jsonParserUtils = new JSONParserUtils(context.getAssets());
        List<Match> matchList = null;

        try {
            //mi "prendo" i dati delle partite dall'api
            MatchesAPIResponse matchResponse = jsonParserUtils.parseMatchesJSONFileWithGSon(Constants.MATCHES_FILE);
            matchList = matchResponse.getMatches();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //popolo la recyclerView
        viewHolder.recyclerViewMatches.setLayoutManager(layoutManager);
        MatchesRecyclerAdapter matchAdapter = new MatchesRecyclerAdapter(R.layout.item_match, matchList);
        viewHolder.recyclerViewMatches.setAdapter(matchAdapter);
    }


    @Override
    public int getItemCount() {
        return leaguesList.size();
    }
}

