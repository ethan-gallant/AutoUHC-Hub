package mc.autouhc.hub.creator;

public enum ScenarioType {
    CUTCLEAN("Cutclean", "cutclean"),
    TIMEBOMB("Timebomb", "timebomb"),
    SWITCHAROO("Switch-a-roo", "switcharoo"),
    DIAMONDLESS("Diamond-less", "diamondless"),
    TRIPLE_ORES("Triple Ores", "triple-ores"),
    TIMBER("Timber", "timber"),
    FIRELESS("Fireless", "fireless"),
    DOUBLE_HEALTH("Double Health", "double-health"),
    NO_FALL("No Fall", "no-fall"),
    INCREASING_SPEED("Increasing Speed", "increasing-speed"),
    ENCHANTED_DEATH("Enchanted Death", "enchanted-death"),
    ONE_HEAL("One Heal", "one-heal"),
    DOUBLE_OR_NOTHING("Double-or-Nothing", "double-or-nothing");

    private final String name;
    private final String configKey;

    private ScenarioType(String name, String configKey) {
        this.name = name;
        this.configKey = configKey;
    }

    public String getName() {
        return this.name;
    }

    public String getConfigKey() {
        return this.configKey;
    }

    /**
     * Searches for the ScenarioType by its config key.
     * @param String key
     * @return ScenarioType or null if not found.
     */

    public static ScenarioType getScenarioTypeByKey(String key) {
        for(ScenarioType type : ScenarioType.values()) {
            if(type.getConfigKey().equalsIgnoreCase(key)) {
                return type;
            }
        }

        return null;
    }
}
