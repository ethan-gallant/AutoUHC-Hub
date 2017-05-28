package mc.autouhc.hub.game;

public enum GameType {
    FREE_FOR_ALL(0), TEAMS(1);

    private final int index;
    private String typeIdentifier;

    private GameType(int typeIndex) {
        this.index = typeIndex;
        this.typeIdentifier = "";
    }

    /**
     * Sets the type identifier
     * @param String id
     */

    public void setTypeID(String id) {
        this.typeIdentifier = id;
    }

    /**
     * Fetches the type identifier.
     * @return String representing type or empty string if not specified.
     */

    public String getTypeID() {
        return this.typeIdentifier;
    }

    /**
     * Fetches the index of a GameType.
     * @return int
     */

    public int toIndex() {
        return this.index;
    }

    public static GameType getByIndex(int index) {
        for(GameType type : GameType.values()) {
            if(type.toIndex() == index) {
                return type;
            }
        }

        return null;
    }
}
