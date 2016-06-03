package pt.ua.sd.RopeGame.structures;


import java.io.Serializable;


public class TrialStat implements Serializable{

    /**
     * Serialization key
     * @serialField serialVersionUID
     */
    private static final long serialVersionUID = 200416L;

    /**
     *  The {@link Boolean } instance tells if has(true) or not(false) one next trial in the current game
     */
    private boolean has_next_trial;

    /**
     *  represents the team id (1 or 2)
     */
    private int team;
    /**
     *  The  int instance represents the type of the result in the trial
     */
    private int wonType;
    /**
     *  tells the center of the rope in the trial
     */
    private  int center_rope;


    /**
     * constructor
     * @param has_next_trial check if theres another trial coming
     * @param team check team id
     * @param wonType check the trial WIN/LOSS status and type
     * @param center_rope check the position of the rope
     */
    public TrialStat(boolean has_next_trial, int team, int wonType, int center_rope)
    {
        this.has_next_trial = has_next_trial;
        this.team = team;
        this.wonType = wonType;
        this.center_rope = center_rope;

    }

    /**
     * @return the {@link Boolean } instance that tells if has(true) or not(false) one next trial in the current game
     */
    public boolean isHas_next_trial() {
        return has_next_trial;
    }

    /**
     * @return the center of the rope
     */
    public int getCenter_rope() {
        return center_rope;
    }

    /**
     * @return the team id
     */
    public int getTeam() {
        return team;
    }

    /**
     * @return the instance int that represents the type of the result in the trial
     */
    public int getWonType() {
        return wonType;
    }

}
