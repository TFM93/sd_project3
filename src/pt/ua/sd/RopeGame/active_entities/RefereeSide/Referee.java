package pt.ua.sd.RopeGame.active_entities.RefereeSide;


import pt.ua.sd.RopeGame.enums.RefState;
import pt.ua.sd.RopeGame.enums.WonType;
import pt.ua.sd.RopeGame.info.Bundle;
import pt.ua.sd.RopeGame.info.VectorTimestamp;
import pt.ua.sd.RopeGame.interfaces.*;
import pt.ua.sd.RopeGame.structures.GameStat;
import pt.ua.sd.RopeGame.structures.TrialStat;

import java.rmi.RemoteException;
import java.util.Arrays;


/**
 * Referee thread<br>
 *     This class represents the thread of the referee, his life cycle ends when
 *     the internal flag MATCH_ENDED takes the positive notation.
 *     Notes:
 *     - the access to the shared memories is limited by the interfaces present in the interfaces package.
 *     - the default state is START_OF_THE_MATCH
 *
 * @author Ivo Silva (<a href="mailto:ivosilva@ua.pt">ivosilva@ua.pt</a>)
 * @author Tiago Magalhaes (<a href="mailto:tiagoferreiramagalhaes@ua.pt">tiagoferreiramagalhaes@ua.pt</a>)
 */
public class Referee extends Thread {
    /**
     * Internal Data
     */
    private BenchInterface contestants_bench;//represents the bench shared memory
    private RefereeSiteInterface referee_site;//represents the referee site shared memory
    private PlaygroundInterface playground;//represents the playground shared memory
    private RepoInterface repo;//represents the general info repository of shared memory
    private int n_players_pushing;//number of players in each team pushing at any given trial, defined in rg.config
    private int n_games;//number of games, defined in rg.config
    private int knockDif;//number of knockout difference needed to win, defined in rg.config
    private final VectorTimestamp vectorTimestamp;




    /**
     * Constructor
     * @param playground playground shared memory instancy
     * @param referee_site referee site shared memory instancy
     * @param contestants_bench contestants bench shared memory instancy
     * @param repo general info repository shared memory instancy
     * @param n_players_pushing number of players pushing the rope
     * @param n_games number of games
     * @param knockDif knockout difference
     * @param vectorTimestampId timestamp id
     * @param nEntities number of entities
     */
    public Referee(PlaygroundInterface playground,
                   RefereeSiteInterface referee_site,
                   BenchInterface contestants_bench,
                   RepoInterface repo,
                   int n_players_pushing,
                   int n_games, int knockDif, int vectorTimestampId, int nEntities){
        this.playground = playground;
        this.referee_site = referee_site;
        this.contestants_bench = contestants_bench;
        this.repo = repo;
        this.n_players_pushing = n_players_pushing;
        this.n_games = n_games;
        this.knockDif = knockDif;
        this.vectorTimestamp = new VectorTimestamp(vectorTimestampId, nEntities);
    }


    /**
     * Thread life cycle
     */
    public void run() {

        Bundle bundle;

        int trial_number = 1;//to know which trial is it
        int score_T1= 0;//score of trials for team 1 in the current game
        int score_T2 = 0;//score of trials for team 2 in the current game
        int knock_out=-1;//indicates who knocked out the other team, the int represents the team id. -1 is invalid
        int gamesWon_T1=0;//score of games for team 1
        int gamesWon_T2=0;//score of games for team 2

        RefState state = RefState.START_OF_THE_MATCH;
        Boolean has_next_trial;
        Boolean MATCH_ENDED = false;//flag for end the life cycle
        try {
            repo.refereeLog(state, trial_number, getVectorTimestamp());//update refereee state in central info repository
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        while (!MATCH_ENDED) {//this value can change when the referee reaches the state END_OF_A_MATCH
            switch (state) {
                case START_OF_THE_MATCH:
                    try {
                        bundle = this.referee_site.announceNewGame(getVectorTimestamp());
                        System.out.println("Received: " + Arrays.toString(bundle.getVectorTimestamp().getVectorTimestampArray()));
                        vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                        System.out.println("UPDATED: " + Arrays.toString(vectorTimestamp.getVectorTimestampArray()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        repo.updGame_nr(getVectorTimestamp());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    state = RefState.START_OF_A_GAME;
                    try {
                        repo.Addheader(false, getVectorTimestamp());//update central info repository adding the header with game nr
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        repo.refereeLog(state, trial_number, getVectorTimestamp());//update central info repository
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case START_OF_A_GAME:
                    /*  At the start of a game, the trial number is always 0  */
                    try {
                        this.repo.updtRopeCenter(Integer.MAX_VALUE, getVectorTimestamp());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    trial_number = 1;
                    score_T1=0;
                    score_T2=0;
                    knock_out=-1;
                    try {
                        bundle = this.contestants_bench.callTrial(getVectorTimestamp());
                        System.out.println("Received: " + Arrays.toString(bundle.getVectorTimestamp().getVectorTimestampArray()));
                        vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                        System.out.println("UPDATED: " + Arrays.toString(vectorTimestamp.getVectorTimestampArray()));
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    state = RefState.TEAMS_READY;
                    try {
                        repo.refereeLog(state, trial_number, getVectorTimestamp());//update central info repository
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case TEAMS_READY:
                    try {
                        bundle = this.contestants_bench.startTrial(getVectorTimestamp());
                        System.out.println("Received: " + Arrays.toString(bundle.getVectorTimestamp().getVectorTimestampArray()));
                        vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        this.repo.updtRopeCenter(0, getVectorTimestamp());//update rope center in central info repository
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    state = RefState.WAIT_FOR_TRIAL_CONCLUSION;
                    try {
                        repo.refereeLog(state, trial_number, getVectorTimestamp());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                case WAIT_FOR_TRIAL_CONCLUSION:
                    TrialStat unpack;
                    WonType wt;//for trial result
                    WonType gr;//for game result
                    GameStat game_result=null;
                    try {
                        bundle = this.playground.assertTrialDecision(n_players_pushing, knockDif, getVectorTimestamp());
                        System.out.println("Received: " + Arrays.toString(bundle.getVectorTimestamp().getVectorTimestampArray()));

                        vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                        System.out.println("UPDATED: " + Arrays.toString(vectorTimestamp.getVectorTimestampArray()));
                        unpack = (TrialStat) bundle.getValue();
                        has_next_trial = unpack.isHas_next_trial();
                        wt = WonType.values()[unpack.getWonType()];
                        repo.updtRopeCenter(unpack.getCenter_rope(), getVectorTimestamp());//update rope center in central info repository
                        switch (wt){
                            case DRAW://if in trial was declared a draw increase both team scores
                                score_T1 +=1;
                                score_T2 +=1;
                                break;
                            case KNOCKOUT:

                                has_next_trial = false;//if one team is knocked out the game ends
                                if(unpack.getTeam()==1)
                                {
                                    knock_out=1;//team 2 was knocked out
                                }
                                else
                                {
                                    knock_out=2;//team 1 was knocked out
                                }
                                break;
                            case POINTS:// trial victory by points
                                if(unpack.getTeam()==1) {//if team 1 wins the trial
                                    score_T1 += 1;
                                }else{//if team 2 wins the trial
                                    score_T2 +=1;
                                }
                                break;
                        }
                        /*  if the trial decision says that there is a next trial, the referee has to call it  */
                        if (has_next_trial) {
                            this.repo.updtRopeCenter(Integer.MAX_VALUE, getVectorTimestamp());//MAX_VALUE hides/resets the rope center in the log
                            bundle = this.contestants_bench.callTrial(getVectorTimestamp());//new trial
                            System.out.println("Received: " + Arrays.toString(bundle.getVectorTimestamp().getVectorTimestampArray()));
                            vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                            System.out.println("UPDATED: " + Arrays.toString(vectorTimestamp.getVectorTimestampArray()));
                            /*  when new trial is called, increment trial number  */
                            trial_number += 1;//increase nr of trials
                            state = RefState.TEAMS_READY;//change state
                        }
                    /*  if not, the referee needs to declare a game winner  */
                        else{

                            bundle=this.referee_site.declareGameWinner(score_T1, score_T2, knock_out, n_games, getVectorTimestamp());

                            System.out.println("Received: " + Arrays.toString(bundle.getVectorTimestamp().getVectorTimestampArray()));
                            vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                            System.out.println("UPDATED: " + Arrays.toString(vectorTimestamp.getVectorTimestampArray()));
                            game_result = (GameStat) bundle.getValue();
                            if(game_result.getWinnerTeam() == 1)
                            {
                                gamesWon_T1 +=1;//increase nr of games won by team 1
                            }
                            else if(game_result.getWinnerTeam()==2)
                            {
                                gamesWon_T2 +=1;//increase nr of games won by team 2
                            }
                        /*update the central info repository with the result of the game */

                            state = RefState.END_OF_A_GAME;

                        }
                        repo.refereeLog(state, trial_number, getVectorTimestamp());//update the referee state in central info repository
                        if(state == RefState.END_OF_A_GAME && game_result != null){
                            gr = WonType.values()[game_result.getWonType()];
                        this.repo.setResult(game_result.getWinnerTeam(),gr,trial_number, getVectorTimestamp());}
                        break;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    try {
                        repo.refereeLog(state, trial_number, getVectorTimestamp());//update the referee state in central info repo
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                case END_OF_A_GAME:

                    int n_games_referee;
                    try {
                        bundle = this.referee_site.getN_games_played(getVectorTimestamp());
                        System.out.println("Received: " + Arrays.toString(bundle.getVectorTimestamp().getVectorTimestampArray()));
                        vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                        System.out.println("UPDATED: " + Arrays.toString(vectorTimestamp.getVectorTimestampArray()));
                        n_games_referee = (int) bundle.getValue();
                        if(n_games > n_games_referee){//if less than 3 games played
                            bundle = this.referee_site.announceNewGame(getVectorTimestamp());//new game announced
                            System.out.println("Received: " + Arrays.toString(bundle.getVectorTimestamp().getVectorTimestampArray()));
                            vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                            System.out.println("UPDATED: " + Arrays.toString(vectorTimestamp.getVectorTimestampArray()));
                            repo.updGame_nr(getVectorTimestamp());//updte the nr of games in central info repo
                            state = RefState.START_OF_A_GAME;
                            repo.refereeLog(state, trial_number, getVectorTimestamp());//update the referee state in central info repo
                            repo.Addheader(false, getVectorTimestamp());//add header with the nr of games in central info repo
                            trial_number = 0;//reset nr of trials played
                            break;
                        }
                        state = RefState.END_OF_A_MATCH;
                        bundle = this.contestants_bench.declareMatchWinner(gamesWon_T1,gamesWon_T2, getVectorTimestamp());//declaring the match winner
                        System.out.println("Received: " + Arrays.toString(bundle.getVectorTimestamp().getVectorTimestampArray()));
                        vectorTimestamp.setVectorTimestamp(bundle.getVectorTimestamp());
                        System.out.println("UPDATED: " + Arrays.toString(vectorTimestamp.getVectorTimestampArray()));
                        int match_winner = (int) bundle.getValue();
                        repo.refereeLog(state, trial_number, getVectorTimestamp());//update the referee state in central info repo
                        repo.printMatchResult(match_winner,gamesWon_T1,gamesWon_T2, getVectorTimestamp());//update the centrla info repo with the winner of the match
                        break;
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }

                case END_OF_A_MATCH:
                    MATCH_ENDED=true;
                    break;
                default:
                    state = RefState.START_OF_THE_MATCH;//default referee state
                    try {
                        repo.refereeLog(state, trial_number, getVectorTimestamp());
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;

            }
        }


        System.out.println("Referee finished execution");

    }

    /**
     * Gets an incremented Referee timestamp vector.
     * @return vector timestamp incremented
     */
    private VectorTimestamp getVectorTimestamp() {

        /* Increment vector */
        vectorTimestamp.incrementVectorTimestamp();

        /* Return vector timestamp */
        return vectorTimestamp.clone();
    }
}
