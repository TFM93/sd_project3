package pt.ua.sd.RopeGame.shared_mem.PlaygroundSide;


import pt.ua.sd.RopeGame.enums.WonType;
import pt.ua.sd.RopeGame.info.Bundle;
import pt.ua.sd.RopeGame.info.VectorTimestamp;
import pt.ua.sd.RopeGame.interfaces.PlaygroundInterface;
import pt.ua.sd.RopeGame.structures.TrialStat;

import java.rmi.RemoteException;
import java.util.Arrays;

import static java.lang.Thread.sleep;

/**
 * Playground - It is where contestants do the actual rope pulling. It is also where the referee
 * comes to see where the rope center is and assert who won the trial and where the coaches review their
 * notes to decide which contestants are playing in the next trial.
 */

public class MPlayground implements PlaygroundInterface {

    //private int n_contestant_pulls_team1[] = {0,0,0,0,0};
    //private int n_contestant_pulls_team2[] = {0,0,0,0,0};
    private int n_contestant_pulls_team1[];
    private int n_contestant_pulls_team2[];
    private int ready_to_push;
    private boolean push_at_all_force = false;
    private int finished_pushing;
    private static int center_rope = 0;
    private int n_trials_on_game = 0;
    private int n_contestants_done_awake = 0;
    private int n_coaches_reviewed_notes = 0;
    private boolean trial_decided_coach = false;
    private boolean trial_decided_contestants = false;
    private boolean contestants_are_done = false;
    private int n_contestants_done = 0;
    /**
     * local vectorial clock
     * @serialField localVectorTimestamp
     */
    private VectorTimestamp localVectorTimestamp;


    public MPlayground(int nEntities){

        localVectorTimestamp = new VectorTimestamp(nEntities-2,nEntities);
        this.ready_to_push=0;
        this.push_at_all_force=false;
    }

    /**
     * Have contestants wait for the trial decision to change their state into SEAT_AT_THE_BENCH
     */
    public synchronized Bundle seatDown(int n_players_pushing, VectorTimestamp vectorTimestamp) throws RemoteException
    {
        updVectorTimestamp(vectorTimestamp);//update vector

        while (!this.trial_decided_contestants){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.n_contestants_done_awake += 1;
        if(this.n_contestants_done_awake >= n_players_pushing * 2){
            /*  reset conditions for next trial  */
            this.n_contestants_done_awake = 0;
            this.trial_decided_contestants = false;
        }
        return new Bundle(localVectorTimestamp.clone());
    }


    /**
     * Contestants wait until they are all in position to pull the rope and, only then, will they start pulling
     */
    public synchronized Bundle pullTheRope(int team_id, int strenght, int contestant_id, int n_players_pushing, int n_players, VectorTimestamp vectorTimestamp) throws RemoteException {
        updVectorTimestamp(vectorTimestamp);//update vector
        this.ready_to_push += 1;

        if(n_contestant_pulls_team1 == null){
            n_contestant_pulls_team1 = new int[n_players];
            Arrays.fill(n_contestant_pulls_team1, 0);
        }

        if(n_contestant_pulls_team2 == null){
            n_contestant_pulls_team2 = new int[n_players];
            Arrays.fill(n_contestant_pulls_team2, 0);
        }

        /*  sleep only if the 6 players have not yet arrived and the push flag is not true  */
        /*  the flag is only set to false by the last player to finish pushing  */
        if (this.ready_to_push >= n_players_pushing * 2 && !this.push_at_all_force){
            this.ready_to_push = 0;
            this.push_at_all_force = true;
            center_rope=0;//reset center of rope
            notifyAll();
        }
        while (!this.push_at_all_force){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if(team_id == 1){
            center_rope -= strenght;//subtract value for push to the left
            try {
                Thread.sleep((long)(Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*  reset push number  */
            this.n_contestant_pulls_team1[contestant_id] = 0;
            /*  the last player to finish pushing in the trial, resets the push_at_all_force flag  */
            this.finished_pushing += 1;
            if(this.finished_pushing >= n_players_pushing * 2){
                this.finished_pushing = 0;
                this.push_at_all_force = false;
            }
        }
        else if (team_id == 2){
            center_rope += strenght;//positive value for push to the right
            try {
                Thread.sleep((long)(Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            /*  reset push number  */
            this.n_contestant_pulls_team2[contestant_id] = 0;
            /*  the last player to finish pushing in the trial, resets the push_at_all_force flag  */
            this.finished_pushing += 1;
            if(this.finished_pushing >= n_players_pushing * 2){
                this.finished_pushing = 0;
                this.push_at_all_force = false;
            }

        }
        return new Bundle(localVectorTimestamp.clone());

    }


    /**
     * The referee waits until the contestants are done pulling the rope and the asserts the trial winner
     * @return trial_stats
     */
    public synchronized Bundle assertTrialDecision(int n_players_pushing, int knockDif, VectorTimestamp vectorTimestamp) throws RemoteException {


        updVectorTimestamp(vectorTimestamp);//update vector

        boolean decision = false;
        WonType decision_type = WonType.NONE;
        int winner = -1;

        /*  wait for contestants to get be done pulling the rope  */
        while (!this.contestants_are_done){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.contestants_are_done = false;

        /*  increment the trials counter  */
        this.n_trials_on_game += 1;

        if(center_rope == 0)
        {
            decision_type = WonType.DRAW;//its a draw
            winner = 0;//none winner

        }
        else if(center_rope> knockDif || center_rope<-knockDif)
        {
            decision_type = WonType.KNOCKOUT;
        }
        else {
            decision_type = WonType.POINTS;
        }

        if(center_rope>0)
        winner=2;
        else if(center_rope<0)
        winner=1;

        /*  flag to tell that there was a trial decision  */
        this.trial_decided_contestants = true;
        this.trial_decided_coach = true;

        /*  wake up contestants in iAmDone and coaches in informReferee  */
        notifyAll();

        /*  return has_next_trial  */
        if(this.n_trials_on_game >= n_players_pushing * 2){
            /*  set number of trials to 0 for next game  */
            this.n_trials_on_game = 0;
            decision = false;
        }
        else{
            decision=true;
        }

        return new Bundle(localVectorTimestamp.clone(),new TrialStat(decision,winner, decision_type.ordinal(), center_rope));
    }

    /**
     * Coaches wait for the trial to be decided and then proceed to chose the contestants for the next trial
     *
     * @param selected_contestants selected contestants in current trial
     * @return selected_contestant_for_next_trial
     */
    public synchronized Bundle reviewNotes(int[] selected_contestants, int n_players, int n_players_pushing, VectorTimestamp vectorTimestamp) throws RemoteException {
        updVectorTimestamp(vectorTimestamp);//update vector

        while (!this.trial_decided_coach){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        /*  substitutions implemented  */
        /*  only one player is substituted from each team at each trial  */
        for (int i = 0; i < n_players_pushing; i++){
            if(selected_contestants[i] == 0){
                selected_contestants[i] = n_players - 1;
            }
            else{
                selected_contestants[i] -= 1;
            }
        }

        if(this.n_coaches_reviewed_notes >= 2){
            this.n_coaches_reviewed_notes = 0;
            this.trial_decided_coach = false;
        }

        return new Bundle(localVectorTimestamp.clone(), selected_contestants);
    }


    /**
     * Contestants are done pulling the rope
     */
    public synchronized Bundle iAmDone(int n_players_pushing, VectorTimestamp vectorTimestamp) throws RemoteException{
        updVectorTimestamp(vectorTimestamp);//update vector

        this.n_contestants_done += 1;

        /*  last contestant done wakes up referee  */
        if(this.n_contestants_done >= n_players_pushing * 2) {
            this.n_contestants_done = 0;
            this.contestants_are_done = true;
            notifyAll();
        }
        return new Bundle(localVectorTimestamp.clone());

    }

    @Override
    public void terminate() throws RemoteException {
        //Todo - implement
    }

    public boolean isClosed() throws RemoteException{
        return false;
        //Todo - implement
    }
    private synchronized void updVectorTimestamp(VectorTimestamp receivedVector) throws RemoteException{
        localVectorTimestamp.incrementVectorTimestamp();
        localVectorTimestamp.updateVectorTimestamp(receivedVector);//update vector

    }

}

