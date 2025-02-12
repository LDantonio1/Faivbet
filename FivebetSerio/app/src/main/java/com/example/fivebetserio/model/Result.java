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

    public static final class MatchSuccess extends Result {
        private final MatchesAPIResponse matchesAPIResponse;
        public MatchSuccess(MatchesAPIResponse matchesAPIResponse) {
            this.matchesAPIResponse = matchesAPIResponse;
        }
        public MatchesAPIResponse getData() {
            return matchesAPIResponse;
        }
    }


    public static final class UserSuccess extends Result {
        private final User user;
        public UserSuccess(User user) {
            this.user = user;
        }
        public User getData() {
            return user;
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
