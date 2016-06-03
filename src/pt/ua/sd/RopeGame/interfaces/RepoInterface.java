package pt.ua.sd.RopeGame.interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;

import pt.ua.sd.RopeGame.enums.CoachState;
import pt.ua.sd.RopeGame.enums.ContestantState;
import pt.ua.sd.RopeGame.enums.RefState;
import pt.ua.sd.RopeGame.enums.WonType;
import pt.ua.sd.RopeGame.info.Bundle;
import pt.ua.sd.RopeGame.info.VectorTimestamp;


public interface RepoInterface extends Remote{

    void refereeLog(RefState state, int trial_number, VectorTimestamp vectorTimestamp)throws RemoteException;
    void Addheader(boolean first, VectorTimestamp vectorTimestamp)throws RemoteException;
    void setResult(int team_id, WonType wonType, int nr_trials, VectorTimestamp vectorTimestamp)throws RemoteException;
    void printMatchResult(int winner, int score1, int score2, VectorTimestamp vectorTimestamp)throws RemoteException;
    void updGame_nr(VectorTimestamp vectorTimestamp)throws RemoteException;
    void updtRopeCenter(int center, VectorTimestamp vectorTimestamp)throws RemoteException;
    void coachLog(int team_id, CoachState state, VectorTimestamp vectorTimestamp)throws RemoteException;
    void contestantLog(int id, int team_id, int strength, ContestantState state, VectorTimestamp vectorTimestamp)throws RemoteException;
    void terminate() throws RemoteException;

    boolean isClosed() throws RemoteException;

}
