package pt.ua.sd.RopeGame.shared_mem.BenchSide;

import pt.ua.sd.RopeGame.info.VectorTimestamp;
import pt.ua.sd.RopeGame.interfaces.BenchInterface;
import pt.ua.sd.RopeGame.info.Bundle;


import java.rmi.RemoteException;
import java.util.Arrays;


/**
 * Contestant Bench shared memory<br>
 *<b><center><font size=6>Contestant Bench shared memory</font></center></b><br>
 *     <font size=4>This class represents the monitor/shared memory of the contestant bench.</font>
 *
 *
 */
public class MContestantsBench implements BenchInterface {
    //Todo- update vector
    /**
     * Internal Data
     */
    private boolean trial_called = false;//flag that identifies if the trial was called

    private int n_coaches_called_contestants = 0;//number of players that already called the contestants, max 2
    private boolean contestants_called = false;//flag that identifies if the contestants were already called

    private int advice_followed = 0;//number of players that already follow the advice

    private boolean trial_started = false;//flag true if trial has started yet

    private int n_coaches_informed_referee = 0;//when a coach inform the referee this var is increased in 1 unit
    private boolean coaches_informed = false;//flag if the coaches were or not informed yet

    private int n_contestants_called = 0;//number of contestants called on callcontestants

    private int n_coaches_trial_decided = 0;//nr of coaches that were already informed that trial was decided

    /*new selection of the playing team*/
    //private boolean new_team1_selected[] = {true, true, true, true, true};
    //private boolean new_team2_selected[] = {true, true, true, true, true};
    private boolean new_team1_selected[];
    private boolean new_team2_selected[];

    /* arrays of selected contestants to play the trial*/
    //private static int team1_selected_contestants[] = {0, 1, 2};
    //private static int team2_selected_contestants[] = {0, 1, 2};
    private static int team1_selected_contestants[];
    private static int team2_selected_contestants[];

    private int n_ready_contestants_started;//nr of contestants that started trial and are ready
    private boolean followed_coach_advice;//flag for follow coach advice, true when the advce is followed
    /*team strenght*/
    //private int[] team1_strength = {0, 0, 0, 0, 0};
    //private int[] team2_strength = {0, 0, 0, 0, 0};
    private int[] team1_strength;
    private int[] team2_strength;

    private boolean match_ended = false;//flag for match ended, true if ended

    /*  array to know if the players are playing  */
    private boolean playing1[];
    private boolean playing2[];
    /**
     * Vector timestamp
     * @serialField localVectorTimestamp
     */
    private final VectorTimestamp localVectorTimestamp;

    public MContestantsBench(int nEntities){
        localVectorTimestamp = new VectorTimestamp(nEntities-1,nEntities);
    }

    /**
     * Referee calls the trial
     */
    public synchronized Bundle callTrial(VectorTimestamp vectorTimestamp) throws RemoteException
    {
        updVectorTimestamp(vectorTimestamp);//update vector

        /*  wake up coaches in reviewNotes  */
        this.trial_called = true;


        notifyAll();
        return new Bundle(localVectorTimestamp.clone());

    }



    /**
     * Coach sleeps while the trial was not called or the mach is not started yet and then
     * calls the contestants to play
     */
    public synchronized Bundle callContestants(int team_id,int[] selected_contestants, int n_players, VectorTimestamp vectorTimestamp) throws RemoteException
    {
        updVectorTimestamp(vectorTimestamp);//update vector

        /*  if arrays are not initilialized  */
        if (new_team1_selected == null){
            new_team1_selected = new boolean[n_players];
            Arrays.fill(new_team1_selected, true);
        }
        if (new_team2_selected == null){
            new_team2_selected = new boolean[n_players];
            Arrays.fill(new_team1_selected, true);

        }

        /*  to know when to increment contestants strength  */
        for (int i = 0; i < n_players; i++){
            if(team_id == 1){
                this.new_team1_selected[i] = true;
            }else if(team_id == 2){
                this.new_team2_selected[i] = true;
            }
        }

        while(!this.trial_called || this.match_ended){

            if(this.match_ended){
                return new Bundle(localVectorTimestamp.clone(),false);
            }

            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.n_coaches_called_contestants += 1;

        if(this.n_coaches_called_contestants >= 2){
            /*  wake up the contestants in bench  */
            notifyAll();
            this.n_coaches_called_contestants = 0;
            this.contestants_called = true;
            this.trial_called = false;
        }

        if(team_id == 1){
            team1_selected_contestants = selected_contestants;
        }else if(team_id == 2){
            team2_selected_contestants = selected_contestants;
        }

        return new Bundle(localVectorTimestamp.clone(),true);

    }

    /**
     * Follow coach advice and sleep until referee wakes them up on start trial. Only the contestants that
     * are playing the trial are waken up. The other ones gain one strength point.
     * @return  true if player is playing and false if he's going to sit down
     */
    public synchronized Bundle followCoachAdvice(int contestant_id,int strength, int team_id, int n_players, int n_players_pushing, VectorTimestamp vectorTimestamp) throws RemoteException
    {

        updVectorTimestamp(vectorTimestamp);//update vector

        boolean[] ret =new boolean[2];
        ret[1]=false;//not increment by default
        ret[0]=false;//return false by default

        /*  if arrays are not initilialized  */
        if (new_team1_selected == null){
            new_team1_selected = new boolean[n_players];
            Arrays.fill(new_team1_selected, true);
        }
        if (new_team2_selected == null){
            new_team2_selected = new boolean[n_players];
            Arrays.fill(new_team1_selected, true);
        }


        if (team1_selected_contestants == null){
            team1_selected_contestants = new int[n_players_pushing];
            for(int i = 0; i < n_players_pushing; i++){
                team1_selected_contestants[i] = i;
            }
        }

        if (team2_selected_contestants == null){
            team2_selected_contestants = new int[n_players_pushing];
            for(int i = 0; i < n_players_pushing; i++){
                team2_selected_contestants[i] = i;
            }
        }


        if(team_id == 1){

            if (team1_strength == null){
                team1_strength = new int[n_players];
                Arrays.fill(team1_strength, 0);
            }

            this.team1_strength[contestant_id] = strength;
        }
        else if(team_id == 2){

            if (team2_strength == null){
                team2_strength = new int[n_players];
                Arrays.fill(team2_strength, 0);
            }

            this.team2_strength[contestant_id] = strength;
        }

        if(team_id == 1){

            /*  if array is not created, create it  */
            if(playing1 == null){
                playing1 = new boolean[n_players];
                Arrays.fill(playing1, false);
            }


            /*  check if player is going to play the next trial  */
            playing1[contestant_id] = false;
            for (int j = 0; j < n_players_pushing; j++){
                if (team1_selected_contestants[j] == contestant_id){
                    /*  know that contestant is playing  */
                    playing1[contestant_id] = true;
                    break;
                }
            }

            while ( !this.contestants_called || !playing1[contestant_id] || this.match_ended ){


                if(this.match_ended){
                    ret[0]=false;//return false
                    return new Bundle(localVectorTimestamp.clone(),ret);
                }

                if( new_team1_selected[contestant_id] ){
                    new_team1_selected[contestant_id] = false;

                    if(!playing1[contestant_id]){
                        ret[1] = true; //increment strength
                    }

                }


                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                /*  check if player is going to play the next trial  */
                playing1[contestant_id] = false;
                for (int j = 0; j < n_players_pushing; j++){
                    if (team1_selected_contestants[j] == contestant_id){
                    /*  know that contestant is playing  */
                        playing1[contestant_id] = true;
                        break;
                    }
                }

            }

        }else if(team_id == 2){


            /*  if array is not created, create it  */
            if(playing2 == null){
                playing2 = new boolean[n_players];
                Arrays.fill(playing2, false);
            }


            /*  check if player is going to play the next trial  */
            playing2[contestant_id] = false;
            for (int j = 0; j < n_players_pushing; j++){
                if (team2_selected_contestants[j] == contestant_id){
                    /*  know that contestant is playing  */
                    playing2[contestant_id] = true;
                    break;
                }
            }


            while ( !this.contestants_called || !playing2[contestant_id] || this.match_ended ){


                if(this.match_ended){
                    ret[1] = false;//not increment by default
                    return new Bundle(localVectorTimestamp.clone(),ret);
                }

                if( new_team2_selected[contestant_id] ){
                    new_team2_selected[contestant_id] = false;

                    if(!playing2[contestant_id]){
                        ret[1] = true;
                    }

                }

                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                /*  check if player is going to play the next trial  */
                playing2[contestant_id] = false;
                for (int j = 0; j < n_players_pushing; j++){
                    if (team2_selected_contestants[j] == contestant_id){
                    /*  know that contestant is playing  */
                        playing2[contestant_id] = true;
                        break;
                    }
                }
            }
        }



        if(team_id == 1){//if team 1
            if (this.new_team1_selected[contestant_id]) {//if the current contestant is selected
                this.new_team1_selected[contestant_id] = false;
            }
        }
        else if(team_id == 2){
            if (this.new_team2_selected[contestant_id]) {
                this.new_team2_selected[contestant_id] = false;
            }
        }

        this.n_contestants_called += 1;
        if (this.n_contestants_called >= n_players_pushing * 2){
            this.contestants_called = false; // the last contestant to get here, resets the seatDown flag
            this.n_contestants_called = 0;
        }


        /*  when all the players have followed the advice, wake up coach  */
        this.advice_followed += 1;
        if(this.advice_followed >= n_players_pushing * 2){
            this.followed_coach_advice = true;
            this.advice_followed = 0;
            notifyAll();
        }
        ret[0]=true;
        return new Bundle(localVectorTimestamp.clone(),ret);
    }


    /**
     * Last coach wakes up referee and sleeps until all the contestants have followed their coache's advice
     */
    public synchronized Bundle informReferee(VectorTimestamp vectorTimestamp) throws RemoteException {

        updVectorTimestamp(vectorTimestamp);//update vector

        this.n_coaches_informed_referee += 1;

        /*  wake up referee when both coaches have informed them  */
        if(this.n_coaches_informed_referee >= 2){
            this.n_coaches_informed_referee = 0;
            this.coaches_informed = true;
            notifyAll();
        }

        /*  wait for trial decision  */
        while (!this.followed_coach_advice){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.n_coaches_trial_decided += 1;
        if (this.n_coaches_trial_decided >= 2) {
            this.n_coaches_trial_decided = 0;
            this.followed_coach_advice = false;
        }

        return new Bundle(localVectorTimestamp.clone());
    }


    /**
     * Referee waits for coaches to inform the referee and then starts trial and wakes up the contestants in bench
    */
    public synchronized Bundle startTrial(VectorTimestamp vectorTimestamp) throws RemoteException
    {
        updVectorTimestamp(vectorTimestamp);//update vector
        /*  wait for coaches to inform referee  */
        while (!this.coaches_informed){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.coaches_informed = false;
        this.trial_started = true;
        notifyAll();
        return new Bundle(localVectorTimestamp.clone());
    }


    /**
     * Contestants sleep until trial is started
     *
     */
    public synchronized Bundle getReady(int n_players_pushing, VectorTimestamp vectorTimestamp) throws RemoteException
    {
        updVectorTimestamp(vectorTimestamp);//update vector
        /*  wait for every contestant to be ready  */
        /*  the last contestant to get ready wakes up everyone else  */
        while (!this.trial_started){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.n_ready_contestants_started += 1;
        if(this.n_ready_contestants_started >= n_players_pushing * 2){
            /*  restore contestants value for next trial  */
            this.n_ready_contestants_started = 0;
            this.trial_started = false;
        }

        return new Bundle(localVectorTimestamp.clone());
    }


    /**
     * Declares the winner of the match
     * @param games1 number of games won by team 1
     * @param games2 number of games won by team 2
     * @return winner team id
     */
    public synchronized Bundle declareMatchWinner(int games1, int games2, VectorTimestamp vectorTimestamp) throws RemoteException{


        updVectorTimestamp(vectorTimestamp);//update vector

        this.match_ended = true;
        notifyAll();
        if (games1 > games2) {
            return new Bundle(localVectorTimestamp.clone(),1);
        } else if(games2 > games1){
            return new Bundle(localVectorTimestamp.clone(),2);
        }
        return new Bundle(localVectorTimestamp.clone(),0);

    }

    @Override
    public void terminate() throws RemoteException {
        //Todo - implement
    }

    public boolean isClosed() {
        return false;
        //Todo - implement
    }

    private synchronized void updVectorTimestamp(VectorTimestamp receivedVector) throws RemoteException{
        localVectorTimestamp.incrementVectorTimestamp();
        localVectorTimestamp.updateVectorTimestamp(receivedVector);//update vector

    }
}
