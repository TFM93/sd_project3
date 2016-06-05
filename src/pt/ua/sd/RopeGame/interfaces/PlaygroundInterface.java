package pt.ua.sd.RopeGame.interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
import pt.ua.sd.RopeGame.info.Bundle;
import pt.ua.sd.RopeGame.info.VectorTimestamp;
import pt.ua.sd.RopeGame.structures.TrialStat;


/**
 * Interface that defines the playground
 */
public interface PlaygroundInterface extends Remote {

    /**
     * Coaches wait for the trial to be decided and then proceed to chose the contestants for the next trial
     *
     * @param selected_contestants selected contestants in current trial
     * @param vectorTimestamp timestamp clock
     * @param n_players number of players
     * @param n_players_pushing players pushing the rope
     * @return bundle with a vector timestamp and selected_contestant_for_next_trial
     * @throws RemoteException RMI exception
     */
    Bundle reviewNotes(int[] selected_contestants, int n_players, int n_players_pushing, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * Contestants wait until they are all in position to pull the rope and, only then, will they start pulling
     * @param vectorTimestamp timestamp clock
     * @param contestant_id id of the contestant
     * @param team_id identification number
     * @param strenght contestant strenght
     * @param n_players number of players
     * @param n_players_pushing players pushing the rope
     * @return bundle with a vector timestamp
     * @throws RemoteException  RMI exception
     */
    Bundle pullTheRope(int team_id, int strenght, int contestant_id, int n_players_pushing, int n_players, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * Contestants are done pulling the rope
     * @param vectorTimestamp timestamp clock
     * @param n_players_pushing number of players pushing the rope
     * @return bundle with a vector timestamp
     * @throws RemoteException RMI exception
     */
    Bundle iAmDone(int n_players_pushing, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * Have contestants wait for the trial decision to change their state into SEAT_AT_THE_BENCH
     * @param vectorTimestamp timestamp clock
     * @param n_players_pushing players pushing the rope
     * @return bundle with a vector timestamp
     * @throws RemoteException RMI exception
     */
    Bundle seatDown(int n_players_pushing, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * The referee waits until the contestants are done pulling the rope and the asserts the trial winner
     * @param n_players_pushing number of players pushing hte rope
     * @param knockDif max dif to knockout
     * @param vectorTimestamp timestamp clock
     * @return bundle with a vector timestamp and trial_stats
     * @throws RemoteException RMI exception
     */
    Bundle assertTrialDecision(int n_players_pushing, int knockDif, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * Signals Shop to terminate.
     * @throws RemoteException RMI exception
     */
    void terminate() throws RemoteException;

    /**
     * Indicates if Shop is closed.
     * @return true if it is closed; false otherwise
     * @throws RemoteException RMI exception
     */
    boolean isClosed() throws RemoteException;


}
