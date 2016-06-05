package pt.ua.sd.RopeGame.interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;

import pt.ua.sd.RopeGame.enums.CoachState;
import pt.ua.sd.RopeGame.enums.ContestantState;
import pt.ua.sd.RopeGame.enums.RefState;
import pt.ua.sd.RopeGame.enums.WonType;
import pt.ua.sd.RopeGame.info.Bundle;
import pt.ua.sd.RopeGame.info.VectorTimestamp;


/**
 * Interface that defines the repository
 */
public interface RepoInterface extends Remote{

    /**
     * Logs the referee changes and prints to the file
     * @param state state of the referee
     * @param trial_number number of current trial
     * @throws RemoteException
     */
    void refereeLog(RefState state, int trial_number, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * Function responsible to add the header to the log
     * @param first if true prints to file only the initial header, if false also prints the game number
     * @throws RemoteException
     */
    void Addheader(boolean first, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * imprime resultado do jogo ou da partida, ou de ambos
     * @param team_id id of the current team
     * @param wonType knock out, draw or points
     * @param nr_trials number of played trials
     * @throws RemoteException
     */
    void setResult(int team_id, WonType wonType, int nr_trials, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * Logs the match result
     * @param winner team winner
     * @param score1 score of team 1
     * @param score2 score of team 2
     * @throws RemoteException
     */
    void printMatchResult(int winner, int score1, int score2, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * just increase the number of played games
     * @throws RemoteException
     */
    void updGame_nr(VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * updates the center of the rope locally
     * @param center new rope center
     * @throws RemoteException
     */
    void updtRopeCenter(int center, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     * Updates the referee info and prints the new state in log file
     * @param team_id id of current team
     * @param state state of the referee in current time
     * @throws RemoteException
     */
    void coachLog(int team_id, CoachState state, VectorTimestamp vectorTimestamp)throws RemoteException;

    /**
     *Logs the contestant states in the current moment
     * @param id contestant id
     * @param team_id team id
     * @param strength contestant strengt
     * @param state contestant state
     * @throws RemoteException
     */
    void contestantLog(int id, int team_id, int strength, ContestantState state, VectorTimestamp vectorTimestamp)throws RemoteException;

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
