package pt.ua.sd.RopeGame.interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
import pt.ua.sd.RopeGame.info.Bundle;
import pt.ua.sd.RopeGame.info.VectorTimestamp;
import pt.ua.sd.RopeGame.structures.GameStat;


public interface RefereeSiteInterface extends Remote {

    Bundle announceNewGame(VectorTimestamp vectorTimestamp)throws RemoteException;

    Bundle declareGameWinner(int score_T1, int score_T2, int knock_out, int n_games, VectorTimestamp vectorTimestamp)throws RemoteException;

    Bundle getN_games_played(VectorTimestamp vectorTimestamp)throws RemoteException;

    void terminate() throws RemoteException;

    boolean isClosed() throws RemoteException;


}
