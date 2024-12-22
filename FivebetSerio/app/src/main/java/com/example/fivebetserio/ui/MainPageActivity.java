package com.example.fivebetserio.ui;

//1 ora e 8
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fivebetserio.R;
import com.example.fivebetserio.adapter.LeaguesRecyclerAdapter;
import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.LeaguesAPIResponse;
import com.example.fivebetserio.util.Constants;
import com.example.fivebetserio.util.JSONParserUtils;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;

public class MainPageActivity extends AppCompatActivity {

    public static final String TAG = MainPageActivity.class.getName();

    public MainPageActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //seleziono il layout
        setContentView(R.layout.activity_main_page);

        ImageButton logoutButton = findViewById(R.id.logout_button);

        logoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            FirebaseAuth.getInstance().signOut();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerViewLeagues);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        JSONParserUtils jsonParserUtils = new JSONParserUtils(getAssets());

        try {
            //prendo i file dall'api( in realtà per ora sono statici, ho salvato nella cartella assets i risultati dell api ma enso che nella prossima lezione spiegi come fare una get)
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