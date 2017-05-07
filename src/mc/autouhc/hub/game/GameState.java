package mc.autouhc.hub.game;

public enum GameState {
    WAITING(0), STARTING(1), PREPARING(2), GRACE_PERIOD(3), PVP(4), DEATHMATCH(5), FINISHED(6);

    private final int stateIndex;

    private GameState(int index) {
        this.stateIndex = index;
    }

    public static GameState fromIndex(int index) {
        for(GameState state : GameState.values()) {
            if(state.toIndex() == index) {
                return state;
            }
        }

        return null;
    }

    public int toIndex() {
        return this.stateIndex;
    }
}
