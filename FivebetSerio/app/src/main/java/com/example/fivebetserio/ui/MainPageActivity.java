package com.example.fivebetserio.ui;


import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fivebetserio.R;
import com.example.fivebetserio.adapter.LeaguesRecyclerAdapter;
import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.LeaguesAPIResponse;
import com.example.fivebetserio.model.Match;
import com.example.fivebetserio.model.MatchesAPIResponse;
import com.example.fivebetserio.util.Constants;
import com.example.fivebetserio.util.JSONParserUtils;

import java.io.IOException;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    public static final String TAG = MainPageActivity.class.getName();


    public MainPageActivity() {
        // Required empty public constructor
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewLeagues);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        JSONParserUtils jsonParserUtils = new JSONParserUtils(getAssets());

        try {
            LeaguesAPIResponse response = jsonParserUtils.parseLeaguesJSONFileWithGSon(Constants.LEAGUES_FILE);
            List<League> leagueList = response.getLeagues();

            LeaguesRecyclerAdapter adapter =
                    new LeaguesRecyclerAdapter(R.layout.item_league, leagueList, this);
            recyclerView.setAdapter(adapter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}