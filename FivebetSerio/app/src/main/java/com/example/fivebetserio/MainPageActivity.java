package com.example.fivebetserio.ui;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.fivebetserio.adapter.LeagueAdapter;
import com.example.fivebetserio.R;
import com.example.fivebetserio.model.League;
import com.example.fivebetserio.model.Match;
import android.util.Log;
import java.util.List;
public class MainPageActivity extends AppCompatActivity {
    private LinearLayout linearLayoutContainer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);
        Log.d("MainPageActivity", "onCreate chiamato");
        linearLayoutContainer = findViewById(R.id.linearLayoutContainer);
        // Simulazione di chiamata API
        List<League> leagues = fetchLeaguesFromAPI();
        for (League league : leagues) {
            addLeagueSection(league);
        }
    }
    private List<League> fetchLeaguesFromAPI() {
        // Log per verificare se la funzione viene chiamata
        Log.d("MainPageActivity", "Chiamata a fetchLeaguesFromAPI()");
        // Simula una chiamata API o usala realmente
        List<League> leagues = LeagueAdapter.getLeagues();  // Dati di esempio
        // Log per verificare cosa restituisce la funzione
        Log.d("MainPageActivity", "Leagues recuperate: " + (leagues != null ? leagues.size() : 0));
        return leagues;
    }
    private void addLeagueSection(League league) {
        View leagueSection = LayoutInflater.from(this).inflate(R.layout.league_scroll_view, linearLayoutContainer, false);
        TextView leagueTitle = leagueSection.findViewById(R.id.leagueTitle);
        leagueTitle.setText(league.getName());
        LinearLayout leagueMatchesContainer = leagueSection.findViewById(R.id.leagueMatchesContainer);
        for (Match match : league.getMatches()) {
            View matchCard = LayoutInflater.from(this).inflate(R.layout.game_card, leagueMatchesContainer, false);
            // Popola il matchCard con i dati
            TextView team1 = matchCard.findViewById(R.id.team1Name);
            TextView team2 = matchCard.findViewById(R.id.team2Name);
            team1.setText(match.getTeam1());
            Log.d("nometeam", "team name: " + match.getTeam1());
            team2.setText(match.getTeam2());
            leagueMatchesContainer.addView(matchCard);
        }
        linearLayoutContainer.addView(leagueSection);
    }
}