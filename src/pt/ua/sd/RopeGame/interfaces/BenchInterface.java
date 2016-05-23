package pt.ua.sd.RopeGame.interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
import pt.ua.sd.RopeGame.info.Bundle;
import pt.ua.sd.RopeGame.info.VectorTimestamp;


public interface BenchInterface extends Remote{

    Bundle callContestants(int team_id,int[] selected_contestants, int n_players, VectorTimestamp vectorTimestamp) throws RemoteException;

    Bundle informReferee(VectorTimestamp vectorTimestamp) throws RemoteException;

    Bundle followCoachAdvice(int contestant_id,int strength, int team_id, int n_players, int n_players_pushing, VectorTimestamp vectorTimestamp) throws RemoteException;

    Bundle getReady(int n_players_pushing, VectorTimestamp vectorTimestamp) throws RemoteException;

    Bundle callTrial(VectorTimestamp vectorTimestamp) throws RemoteException;

    Bundle startTrial(VectorTimestamp vectorTimestamp) throws RemoteException;

    Bundle declareMatchWinner(int games1, int games2, VectorTimestamp vectorTimestamp) throws RemoteException;

    void terminate() throws RemoteException;

    boolean isClosed() throws RemoteException;

}
