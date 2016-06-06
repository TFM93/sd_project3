package pt.ua.sd.RopeGame.active_entities.CoachSide;

import pt.ua.sd.RopeGame.enums.CoachState;
import pt.ua.sd.RopeGame.info.Bundle;
import pt.ua.sd.RopeGame.info.VectorTimestamp;
import pt.ua.sd.RopeGame.interfaces.*;

import java.rmi.RemoteException;

/**
 * Coach thread<br>
 *     This class represents the thread of the coach, her life cycle ends when
 *     the internal flag match_not_over takes the false notation.
 *     Notes:
 *     - the access to the shared memories is limited by the interfaces present in the interfaces package.
 *     - the default selected team to play is the first 3 contestants (id: 0,1 and 2).
 *     - the default state is WAIT_FOR_REFEREE_COMMAND
 *
 * @author Ivo Silva (<a href="mailto:ivosilva@ua.pt">ivosilva@ua.pt</a>)
 * @author Tiago Magalhaes (<a href="mailto:tiagoferreiramagalhaes@ua.pt">tiagoferreiramagalhaes@ua.pt</a>)
 */

public class Coach extends Thread {

    /**
     * Internal Data
     */
    private int id;//represents the id of the coach
    private int team_id;//represents the id of the team
    private int team_selected_contestants[];//each coach has 3 selected contestants to play
    private BenchInterface contestants_bench;//represents the bench shared memory
    private PlaygroundInterface playground;//represents the playground shared memory
    private RepoInterface repo;//represents the general info repository of shared memory
    private int n_players;//number of players in each team, defined in rg.config
    private int n_players_pushing;//number of players in each team pushing at any given trial, defined in rg.config
    private final VectorTimestamp vectorTimestamp;


    /**
     * Constructor
     * @param id current coach id
     * @param team_id current team id
     * @param playground playground shared memory instancy
     * @param contestants_bench contestants bench shared memory instancy
     * @param repo general info repository shared memory instancy
     * @param n_players number of players per team
     * @param n_players_pushing number of players pushing the rope
     * @param vectorTimestampId coach local timestamp id
     * @param nEntities number of total entities
     */
    public Coach(int id, int team_id, PlaygroundInterface playground,
                 BenchInterface contestants_bench, RepoInterface repo, int n_players, int n_players_pushing,
                  int vectorTimestampId, int nEntities) {
        this.id = id;
        this.team_id = team_id;
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

        Bundle bundle;

        CoachState state = CoachState.WAIT_FOR_REFEREE_COMMAND;//initial state
        try {
            repo.coachLog(this.team_id, state, getVectorTimestamp());//update repo
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        boolean match_not_over = true;


        while (match_not_over){//this value can change when coach is in the begining of his cycle(WRC) by callContestants
            switch (state){
                case WAIT_FOR_REFEREE_COMMAND:
                    if (team_selected_contestants == null){
                        team_selected_contestants = new int[n_players_pushing];
                        for(int i = 0; i < n_players_pushing; i++){
                            team_selected_contestants[i] = i;
                        }
                    }
                    try {
                        bundle = this.contestants_bench.callContestants(this.team_id,this.team_selected_contestants, n_players, getVectorTimestamp());
                        vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                        match_not_over = (boolean) bundle.getValue();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                    state = CoachState.ASSEMBLE_TEAM;//change state
                    try {
                        repo.coachLog(this.team_id, state, getVectorTimestamp());//update central info repository
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case ASSEMBLE_TEAM:
                    try {
                        bundle = this.contestants_bench.informReferee(getVectorTimestamp());
                        vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    state = CoachState.WATCH_TRIAL;
                    try {
                        repo.coachLog(this.team_id, state, getVectorTimestamp());//update central info repository
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case WATCH_TRIAL:
                    try {
                        bundle = this.playground.reviewNotes(this.team_selected_contestants, n_players, n_players_pushing, getVectorTimestamp());
                        vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                        this.team_selected_contestants = (int[]) bundle.getValue();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    state = CoachState.WAIT_FOR_REFEREE_COMMAND;
                    try {
                        repo.coachLog(this.team_id, state, getVectorTimestamp());//update central info repository
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    state= CoachState.WAIT_FOR_REFEREE_COMMAND;//default state
                    try {
                        repo.coachLog(this.team_id, state, getVectorTimestamp());//update central info repository
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }

        System.out.println("Coach " + this.id + " finished execution");
    }

    /**
     *
     * @return the {@link Integer} repesentation of the coach id
     */
    public int getCoachId() {
        return id;
    }

    /**
     *
     * @return the {@link Integer} representation of the team id
     */
    public int getTeam_id() {
        return team_id;
    }

    /**
     *
     * @return the {@link Integer}[] representation of the contestants
     */
    public int[] getSelectedContestants(){
        return this.team_selected_contestants;
    }

    /**
     * Gets an incremented Coach timestamp vector
     * @return vector timestamp already incremented
     */
    private VectorTimestamp getVectorTimestamp() {

        /* Increment vector */
        vectorTimestamp.incrementVectorTimestamp();

        /* Return vector timestamp */
        return vectorTimestamp.clone();
    }

}
