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
     * @return bundle with a vector timestamp and selected_contestant_for_next_trial
     * @throws RemoteException
     */
    Bundle reviewNotes(int[] selected_contestants, int n_players, int n_players_pushing, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * Contestants wait until they are all in position to pull the rope and, only then, will they start pulling
     * @return bundle with a vector timestamp
     * @throws RemoteException
     */
    Bundle pullTheRope(int team_id, int strenght, int contestant_id, int n_players_pushing, int n_players, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * Contestants are done pulling the rope
     * @return bundle with a vector timestamp
     * @throws RemoteException
     */
    Bundle iAmDone(int n_players_pushing, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * Have contestants wait for the trial decision to change their state into SEAT_AT_THE_BENCH
     * @return bundle with a vector timestamp
     * @throws RemoteException
     */
    Bundle seatDown(int n_players_pushing, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * The referee waits until the contestants are done pulling the rope and the asserts the trial winner
     * @return bundle with a vector timestamp and trial_stats
     * @throws RemoteException
     */
    Bundle assertTrialDecision(int n_players_pushing, int knockDif, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * Signals Shop to terminate.
     * @throws RemoteException
     */
    void terminate() throws RemoteException;

    /**
     * Indicates if Shop is closed.
     * @return true if it is closed; false otherwise
     * @throws RemoteException
     */
    boolean isClosed() throws RemoteException;


}
