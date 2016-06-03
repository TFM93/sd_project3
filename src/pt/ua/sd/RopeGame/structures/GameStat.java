package pt.ua.sd.RopeGame.structures;



import java.io.Serializable;


public class GameStat implements Serializable{

    /**
     * Serialization key
     * @serialField serialVersionUID
     */
    private static final long serialVersionUID = 200416L;

    /**
     * check if theres another game coming
     */
    private boolean has_next_game;
    /**
     * team id
     */
    private int team;
    /**
     * {@link Integer} representation of the ordinal of {@link pt.ua.sd.RopeGame.enums.WonType} data structure
     */
    private int wonType;

    /**
     * Constructor
     * @param has_next_game to check if theres another game
     * @param team to check team id
     * @param wonType to check the Win/Loss status
     */
    public GameStat(boolean has_next_game, int team, int wonType)
    {
        this.has_next_game = has_next_game;
        this.team = team;
        this.wonType = wonType;

    }

    /**
     *
     * @return the {@link Boolean } instance that tells if has(true) or not(false) one next game in the current match
     */
    public boolean isHas_next_game() {
        return has_next_game;
    }

    /**
     *
     * @return the {@link Integer} team id
     */
    public int getWinnerTeam() {
        return team;
    }

    /**
     *
     * @return the {@link Integer} type of victory in the game
     */
    public int getWonType() {
        return wonType;
    }
}
