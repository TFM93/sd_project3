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
     * @param vectorTimestamp timestamp clock
     * @return bundle with a vector timestamp
     * @throws RemoteException RMI exception
     */
    Bundle announceNewGame(VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * The number of played games is increased and the game winner is decided
     * @param vectorTimestamp timestamp clock
     * @param score_T1 team 1 score
     * @param score_T2 team 2 score
     * @param knock_out knock out flag yes or no if won or not by knockout
     * @param n_games number of games
     * @return bundle with a vector timestamp and GameStat data with the info of the winner
     * @throws RemoteException RMI exception
     */
    Bundle declareGameWinner(int score_T1, int score_T2, int knock_out, int n_games, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     *
     * @return bundle with a vector timestamp and the number of games played
     * @param vectorTimestamp timestamp clock
     * @throws RemoteException RMI exception
     */
    Bundle getN_games_played(VectorTimestamp vectorTimestamp)throws RemoteException;

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
