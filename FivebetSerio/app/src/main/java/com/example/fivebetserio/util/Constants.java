package com.example.fivebetserio.util;

import java.util.Arrays;
import java.util.List;

public class Constants {
    // queste costanti indicano i nomi dei file nei quali sono salvati i risultati delle getAPI
    public static final String LEAGUES_FILE = "leagues.json";
    public static final String MATCHES_FILE = "matches.json";

    //in questo array specifico (in modo statico) quali campionati mostrare
    //un potenziale sviluppo futuro è poter mostrare tutti i camoionati o far scegliere all'utente una lista di camoionati da vadere
    //lista completa: ("Primera División - Argentina", "A-League", "Belgium First Div", "Championship", "EFL Cup", "League 1", "League 2", "EPL", "FA Cup", "FIFA World Cup Winner", "Ligue 1 - France", "Ligue 2 - France", "Bundesliga - Germany", "Bundesliga 2 - Germany", "3. Liga - Germany", "Super League - Greece", "Serie A - Italy", "Serie B - Italy", "League of Ireland", "Liga MX", "Dutch Eredivisie", "Ekstraklasa - Poland", "Primeira Liga - Portugal", "La Liga - Spain", "La Liga 2 - Spain", "Premiership - Scotland", "Swiss Superleague", "Turkey Super League", "UEFA Europa Conference League", "UEFA Europa League")
    public static final List<String> LEAGUES = Arrays.asList("Bundesliga - Germany", "Serie A - Italy");


    public static final String UNEXPECTED_ERROR = "unexpected_error";
    public static final String INVALID_USER_ERROR = "invalidUserError";
    public static final String INVALID_CREDENTIALS_ERROR = "invalidCredentials";
    public static final String USER_COLLISION_ERROR = "userCollisionError";
    public static final String WEAK_PASSWORD_ERROR = "passwordIsWeak";

    public static final String FIREBASE_REALTIME_DATABASE = "https://faivbet-6776c-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String FIREBASE_USERS_COLLECTION = "users";
}
