package pt.ua.sd.RopeGame.shared_mem.RefSiteSide;

import pt.ua.sd.RopeGame.enums.WonType;
import pt.ua.sd.RopeGame.info.Bundle;
import pt.ua.sd.RopeGame.info.VectorTimestamp;
import pt.ua.sd.RopeGame.interfaces.RefereeSiteInterface;
import pt.ua.sd.RopeGame.structures.GameStat;

import java.rmi.RemoteException;

/**
 * Refereee Site shared memory<br>
 *
 *    This class represents the monitor/shared memory of the referee site.
 *
 * @author Ivo Silva (<a href="mailto:ivosilva@ua.pt">ivosilva@ua.pt</a>)
 * @author Tiago Magalhaes (<a href="mailto:tiagoferreiramagalhaes@ua.pt">tiagoferreiramagalhaes@ua.pt</a>)
 *
 */
public class MRefereeSite implements RefereeSiteInterface{
    /**
     * local vector timestamp
     */
    private final VectorTimestamp localVectorTimestamp;

    public MRefereeSite(int nEntities){
        localVectorTimestamp = new VectorTimestamp(nEntities-3,nEntities);
    }

    /**
     *
     * @return  the number of games played
     */
    public Bundle getN_games_played(VectorTimestamp vectorTimestamp) throws RemoteException {
        updVectorTimestamp(vectorTimestamp);//update vector

        return  new Bundle(localVectorTimestamp.clone(), n_games_played);
    }

    /**
     * Internal Data
     */
    //private static int n_games=3;
    private static int n_games_played=0;


    /**
     * The referee announce a new game
     */
    public synchronized Bundle announceNewGame(VectorTimestamp vectorTimestamp) throws RemoteException {
        updVectorTimestamp(vectorTimestamp);//update vector
        return new Bundle(localVectorTimestamp.clone());
    }

    /**
     * The number of played games is increased and the game winner is decided
     * @return GameStat data with the info of the winner
     */
    public synchronized Bundle declareGameWinner(int score_T1, int score_T2, int knock_out, int n_games,VectorTimestamp vectorTimestamp)throws RemoteException {
        updVectorTimestamp(vectorTimestamp);//update vector

        n_games_played +=1;//increase number of games played

        if(knock_out== 1 )
        {
            return new Bundle(localVectorTimestamp.clone(),new GameStat((n_games_played<n_games),knock_out, WonType.KNOCKOUT.ordinal()));
        }
        else if(knock_out == 2){

            return new Bundle(localVectorTimestamp.clone(),new GameStat((n_games_played<n_games),knock_out, WonType.KNOCKOUT.ordinal()));
        }
        else if(score_T1>score_T2)
        {

            return new Bundle(localVectorTimestamp.clone(),new GameStat((n_games_played<n_games),1,WonType.POINTS.ordinal()));
        }
        else if(score_T1<score_T2){

            return new Bundle(localVectorTimestamp.clone(),new GameStat((n_games_played<n_games),2,WonType.POINTS.ordinal()));
        }

        return new Bundle(localVectorTimestamp.clone(), new GameStat((n_games_played<n_games),0,WonType.DRAW.ordinal()));

    }

    @Override
    public void terminate() throws RemoteException {
        //Todo - implement
    }

    public boolean isClosed() throws RemoteException{
        return false;
        //Todo - implement
    }

    private synchronized void updVectorTimestamp(VectorTimestamp receivedVector) throws RemoteException{
        localVectorTimestamp.incrementVectorTimestamp();
        localVectorTimestamp.updateVectorTimestamp(receivedVector);//update vector

    }
}

