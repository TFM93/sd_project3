package pt.ua.sd.RopeGame.interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
import pt.ua.sd.RopeGame.info.Bundle;
import pt.ua.sd.RopeGame.info.VectorTimestamp;

/**
 * Interface that defines the bench
 */
public interface BenchInterface extends Remote{

    /**
     * Coach sleeps while the trial was not called or the mach is not started yet and then
     * calls the contestants to play
     * @return bundle with a vector timestamp
     * @throws RemoteException
     */
    Bundle callContestants(int team_id,int[] selected_contestants, int n_players, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Last coach wakes up referee and sleeps until all the contestants have followed their coache's advice
     * @return bundle with a vector timestamp
     * @throws RemoteException
     */
    Bundle informReferee(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Follow coach advice and sleep until referee wakes them up on start trial. Only the contestants that
     * are playing the trial are waken up. The other ones gain one strength point.
     * @return bundle with a vector timestamp and true if player is playing and false if he's going to sit down
     * @throws RemoteException
     */
    Bundle followCoachAdvice(int contestant_id,int strength, int team_id, int n_players, int n_players_pushing, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Contestants sleep until trial is started
     * @return bundle with a vector timestamp
     * @throws RemoteException
     *
     */
    Bundle getReady(int n_players_pushing, VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Referee calls the trial
     * @return bundle with a vector timestamp
     * @throws RemoteException
     */
    Bundle callTrial(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Referee waits for coaches to inform the referee and then starts trial and wakes up the contestants in bench
     * @return bundle with a vector timestamp
     * @throws RemoteException
     */
    Bundle startTrial(VectorTimestamp vectorTimestamp) throws RemoteException;

    /**
     * Declares the winner of the match
     * @param games1 number of games won by team 1
     * @param games2 number of games won by team 2
     * @return bundle with a vector timestamp and winner team id
     * @throws RemoteException
     */
    Bundle declareMatchWinner(int games1, int games2, VectorTimestamp vectorTimestamp) throws RemoteException;

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
