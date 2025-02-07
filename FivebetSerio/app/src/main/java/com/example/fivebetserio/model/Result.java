package com.example.fivebetserio.model;

public abstract class Result {
    private Result() {}

    public boolean isSuccess() {
        return !(this instanceof Error);
    }

    /**
     * Class that represents a successful action during the interaction
     * with a Web Service or a local database.
     */
    public static final class LeagueSuccess extends Result {
        private final LeaguesAPIResponse leaguesAPIResponse;
        public LeagueSuccess(LeaguesAPIResponse leaguesAPIResponse) {
            this.leaguesAPIResponse = leaguesAPIResponse;
        }
        public LeaguesAPIResponse getData() {
            return leaguesAPIResponse;
        }
    }
    /**
     * Class that represents an error occurred during the interaction
     * with a Web Service or a local database.
     */
    public static final class Error extends Result {
        private final String message;
        public Error(String message) {
            this.message = message;
        }
        public String getMessage() {
            return message;
        }
    }
}
