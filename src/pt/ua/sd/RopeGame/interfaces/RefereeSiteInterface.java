package pt.ua.sd.RopeGame.interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
import pt.ua.sd.RopeGame.info.Bundle;
import pt.ua.sd.RopeGame.info.VectorTimestamp;
import pt.ua.sd.RopeGame.structures.GameStat;

/**
 * Interface that defines the referee site
 */
public interface RefereeSiteInterface extends Remote {

    /**
     * The referee announce a new game
     * @return bundle with a vector timestamp
     * @throws RemoteException
     */
    Bundle announceNewGame(VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * The number of played games is increased and the game winner is decided
     * @return bundle with a vector timestamp and GameStat data with the info of the winner
     * @throws RemoteException
     */
    Bundle declareGameWinner(int score_T1, int score_T2, int knock_out, int n_games, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     *
     * @return bundle with a vector timestamp and the number of games played
     * @throws RemoteException
     */
    Bundle getN_games_played(VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * Signals Shop to terminate.
     * @return bundle with a vector timestamp
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
