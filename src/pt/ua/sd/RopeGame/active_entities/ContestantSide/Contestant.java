package pt.ua.sd.RopeGame.active_entities.ContestantSide;


import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import pt.ua.sd.RopeGame.enums.ContestantState;
import pt.ua.sd.RopeGame.info.Bundle;
import pt.ua.sd.RopeGame.info.VectorTimestamp;
import pt.ua.sd.RopeGame.interfaces.*;

import java.rmi.RemoteException;

/**
 * Contestant thread<br>
 *     This class represents the thread of the contestant, his life cycle ends when
 *     the internal flag match_not_over takes the false notation.
 *     Notes:
 *     - the access to the shared memories is limited by the interfaces present in the interfaces package.
 *     - the default state is SEAT_AT_THE_BENCH
 *
 * @author Ivo Silva (<a href="mailto:ivosilva@ua.pt">ivosilva@ua.pt</a>)
 * @author Tiago Magalhaes (<a href="mailto:tiagoferreiramagalhaes@ua.pt">tiagoferreiramagalhaes@ua.pt</a>)
 */
public class Contestant extends Thread {

    /**
     * Internal Data
     */
    private int id;//represents the id of the coach
    private int team_id;//represents the id of the team
    private int strength;//represents the strenght of the current player
    private BenchInterface contestants_bench;//represents the bench shared memory
    private PlaygroundInterface playground;//represents the playground shared memory
    private RepoInterface repo;//represents the general info repository of shared memory
    private int n_players;//number of players in each team, defined in rg.config
    private int n_players_pushing;//number of players in each team pushing at any given trial, defined in rg.config
    private final VectorTimestamp vectorTimestamp;


    /**
     * Constructor
     * @param id current contestant id
     * @param team_id current team id
     * @param strength strengt of current player
     * @param playground playground shared memory instancy
     * @param contestants_bench contestants bench shared memory instancy
     * @param repo general info repository shared memory instancy
     * @param n_players number of players per team
     * @param n_players_pushing number of players pushing the rope
     * @param vectorTimestampId timestamp id
     * @param nEntities number of entities
     */
    public Contestant(int id, int team_id, int strength,
                      PlaygroundInterface playground,
                      BenchInterface contestants_bench,
                      RepoInterface repo,
                      int n_players, int n_players_pushing,
                       int vectorTimestampId, int nEntities){
        this.id = id;
        this.team_id = team_id;
        this.strength = strength;
        this.playground = playground;
        this.contestants_bench = contestants_bench;
        this.repo = repo;
        this.n_players = n_players;
        this.n_players_pushing = n_players_pushing;
        this.vectorTimestamp = new VectorTimestamp(vectorTimestampId, nEntities);
    }

    /**
     * Thread life cycle
     */
    public void run() {

        ContestantState state = ContestantState.START;//initial state
        boolean[] unpack = new boolean[2];//for receive return from follow coach advice
        unpack[0]=false;//def
        unpack[1]=false;//def
        boolean match_not_over = true;

        Bundle bundle;

        while (match_not_over){//this value can change when contestant is in the begining of his cycle(SAB) by followCoachAdvice()
            switch (state){

                case SEAT_AT_THE_BENCH:
                    try {
                        repo.updtRopeCenter(Integer.MAX_VALUE, getVectorTimestamp());//update central info repository the MAX_VALUE hides the log value
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        bundle = contestants_bench.followCoachAdvice(this.id,this.strength,this.team_id, this.n_players, this.n_players_pushing, getVectorTimestamp());
                        vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                        unpack = (boolean[]) bundle.getValue();
                        match_not_over = unpack[0];
                        if(unpack[1])
                        {
                            incrementStrength();
                        }
                        if(!match_not_over){
                            break;
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    state = ContestantState.STAND_IN_POSITION;//change state
                    try {
                        repo.contestantLog(this.id, this.team_id, this.strength, state, getVectorTimestamp());//update central info repository
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case STAND_IN_POSITION:
                    try {
                        bundle = contestants_bench.getReady(n_players_pushing, getVectorTimestamp());
                        vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    state = ContestantState.DO_YOUR_BEST;//change state
                    try {
                        repo.contestantLog(this.id, this.team_id, this.strength, state, getVectorTimestamp());//update central info repository
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case DO_YOUR_BEST:
                    try {
                        bundle = playground.pullTheRope(this.team_id, this.strength, this.id, n_players_pushing, n_players, getVectorTimestamp());
                        vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());

                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        repo.contestantLog(this.id, this.team_id, this.strength, state, getVectorTimestamp());//update central info repository
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        bundle = playground.iAmDone(n_players_pushing, getVectorTimestamp());
                        vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    decrementStrength();//depois de am done decrementar a forca
                    try {
                        bundle = playground.seatDown(n_players_pushing, getVectorTimestamp());
                        vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    state = ContestantState.START;//change state
                    break;
                default:
                    state = ContestantState.SEAT_AT_THE_BENCH;//change state
                    try {
                        repo.contestantLog(this.id, this.team_id, this.strength, state, getVectorTimestamp());//update central info repository
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        System.out.println("Contestant " + this.id + " finished execution");

    }

    /**
     *
     * @return the {@link Integer} repesentation of the contestant id
     */
    public int getContestantId() {
        return id;
    }
    /**
     *
     * @return the {@link Integer} repesentation of the team id
     */
    public int getTeam_id() {
        return team_id;
    }

    /**
     *
     * @return the {@link Integer} repesentation of the contestant strenght
     */
    public int getStrength() {
        return this.strength;
    }

    /**
     *Decrements one unit of strenght on current contestant
     */
    public void decrementStrength() {
        if (this.strength > 0){
            this.strength--;
        }
    }
    /**
     *
     *Increments one unit of strenght on current contestant
     */
    public void incrementStrength() {
        this.strength++;
    }


    /**
     * Gets an incremented Contestant timestamp vector.
     * @return vector timestamp already incremented
     */
    private VectorTimestamp getVectorTimestamp() {

        /* Increment vector */
        vectorTimestamp.incrementVectorTimestamp();

        /* Return vector timestamp */
        return vectorTimestamp.clone();
    }
}
