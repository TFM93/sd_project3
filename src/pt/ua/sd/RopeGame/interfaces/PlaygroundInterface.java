package pt.ua.sd.RopeGame.interfaces;
import java.rmi.Remote;
import java.rmi.RemoteException;
import pt.ua.sd.RopeGame.info.Bundle;
import pt.ua.sd.RopeGame.info.VectorTimestamp;
import pt.ua.sd.RopeGame.structures.TrialStat;


public interface PlaygroundInterface extends Remote {

    Bundle reviewNotes(int[] selected_contestants, int n_players, int n_players_pushing, VectorTimestamp vectorTimestamp)throws RemoteException;

    Bundle pullTheRope(int team_id, int strenght, int contestant_id, int n_players_pushing, int n_players, VectorTimestamp vectorTimestamp)throws RemoteException;

    Bundle iAmDone(int n_players_pushing, VectorTimestamp vectorTimestamp)throws RemoteException;

    Bundle seatDown(int n_players_pushing, VectorTimestamp vectorTimestamp)throws RemoteException;

    Bundle assertTrialDecision(int n_players_pushing, int knockDif, VectorTimestamp vectorTimestamp)throws RemoteException;

    void terminate() throws RemoteException;

    boolean isClosed();


}
