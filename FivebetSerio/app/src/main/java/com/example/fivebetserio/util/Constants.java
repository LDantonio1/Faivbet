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

    public static final int FRESH_TIMEOUT = 1000 * 60; // 1 minute in milliseconds

    public static final String API_BASE_URL = "https://api.the-odds-api.com/v4/";
    public static final String TOP_HEADLINES_ENDPOINT = "sports/";
    public static final String EUROPE = "eu";
    public static final String H2H = "h2h";
    public static final String UNIBET = "unibet_eu";
    public static final String DECIMAL = "decimal";

    public static final String TOP_HEADLINES_PAGE_SIZE_PARAMETER = "pageSize";
    public static final int TOP_HEADLINES_PAGE_SIZE_VALUE = 100;

    public static final String REMOVED_ARTICLE_TITLE = "[Removed]";

    public static final String SHARED_PREFERENCES_FILENAME = "com.example.Fivebet.preferences";
    public static final String SHARED_PREFERENCES_COUNTRY_OF_INTEREST = "country_of_interest";
    public static final String SHARED_PREFERENCES_CATEGORIES_OF_INTEREST = "categories_of_interest";
    public static final String SHARED_PREFERENECES_LAST_UPDATE = "last_update";

    public static final String RETROFIT_ERROR = "retrofit_error";
    public static final String API_KEY_ERROR = "api_key_error";
    public static final String UNEXPECTED_ERROR = "unexpected_error";
    public static final String INVALID_USER_ERROR = "invalidUserError";
    public static final String INVALID_CREDENTIALS_ERROR = "invalidCredentials";
    public static final String USER_COLLISION_ERROR = "userCollisionError";
    public static final String WEAK_PASSWORD_ERROR = "passwordIsWeak";


    public static final String FIREBASE_REALTIME_DATABASE = "https://faivbet-6776c-default-rtdb.europe-west1.firebasedatabase.app/";
    public static final String FIREBASE_USERS_COLLECTION = "users";




    public static final int DATABASE_VERSION = 2;
    public static final String SAVED_LEAGUES_DATABASE = "app_database";
}
