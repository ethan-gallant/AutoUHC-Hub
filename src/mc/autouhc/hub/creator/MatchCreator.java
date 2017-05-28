package mc.autouhc.hub.creator;

import java.util.ArrayList;
import java.util.UUID;

public class MatchCreator {

    public enum BorderSize {
        ONE_K(1000, "oneThousand"), 
        FIFTEEN_K(1500, "fifteenHundred"), 
        TWO_K(2000, "twoThousand");

        private final int size;
        private final String msgKey;

        private BorderSize(int borderSize, String key) {
            this.size = borderSize;
            this.msgKey = key;
        }

        public int getSize() {
            return this.size;
        }

        /**
         * The key of the message inside of the "messages" {@link org.bukkit.configuration.ConfigurationSection}
         * @return String the message key
         */

        public String getMessageKey() {
            return this.msgKey;
        }
    }

    public enum MatchDuration {
        FORTY_MINUTES(40, "fortyMins"), 
        FIFTY_MINUTES(50, "fiftyMins"), 
        SIXTY_MINUTES(60, "sixtyMins");

        private final int minutes;
        private final String msgKey;

        private MatchDuration(int matchDuration, String key) {
            this.minutes = matchDuration;
            this.msgKey = key;
        }

        public int getMinutes() {
            return this.minutes;
        }

        /**
         * The key of the message inside of the "messages" {@link org.bukkit.configuration.ConfigurationSection}
         * @return String the message key
         */

        public String getMessageKey() {
            return this.msgKey;
        }
    }

    private final UUID creator;
    private BorderSize borderSize;
    private MatchDuration matchDuration;
    private boolean whitelisted;
    private int slots;

    private boolean teamMode;
    private ArrayList<ScenarioType> scenarios;

    // Settings relating to Teams
    private int teamSize;
    private boolean friendlyFire;
    private boolean randomTeams;

    public MatchCreator(UUID creator) {
        this.creator = creator;
        this.borderSize = BorderSize.ONE_K;
        this.matchDuration = MatchDuration.FORTY_MINUTES;
        this.whitelisted = false;
        this.slots = 0;

        this.teamMode = false;
        this.scenarios = new ArrayList<ScenarioType>();

        // Initializing team variables...
        this.teamSize = 0;
        this.friendlyFire = false;
        this.randomTeams = true;
    }

    public void setBorderSize(BorderSize size) {
        this.borderSize = size;
    }

    public BorderSize getBorderSize() {
        return this.borderSize;
    }

    public void setMatchDuration(MatchDuration duration) {
        this.matchDuration = duration;
    }

    public MatchDuration getMatchDuration() {
        return this.matchDuration;
    }

    public void setWhitelisted(boolean flag) {
        this.whitelisted = flag;
    }

    public boolean isWhitelisted() {
        return this.whitelisted;
    }

    public void setSlots(int slots) {
        this.slots = slots;
    }

    public int getSlots() {
        return this.slots;
    }

    public void setTeamMode(boolean flag) {
        this.teamMode = flag;
    }

    public boolean isTeamMode() {
        return this.teamMode;
    }

    public void addScenario(ScenarioType type) {
        this.scenarios.add(type);
    }

    public boolean hasScenario(ScenarioType type) {
        return this.scenarios.contains(type);
    }

    public void removeScenario(ScenarioType type) {
        this.scenarios.remove(type);
    }

    public ArrayList<ScenarioType> getScenarios() {
        return this.scenarios;
    }

    public void setTeamSize(int size) {
        this.teamSize = size;
    }

    public int getTeamSize() {
        return this.teamSize;
    }

    public void setFriendlyFire(boolean flag) {
        this.friendlyFire = flag;
    }

    public boolean allowsFriendlyFire() {
        return this.friendlyFire;
    }

    public void setRandomTeams(boolean flag) {
        this.randomTeams = flag;
    }

    public boolean allowsRandomTeams() {
        return this.randomTeams;
    }

    public UUID getCreator() {
        return this.creator;
    }
}
