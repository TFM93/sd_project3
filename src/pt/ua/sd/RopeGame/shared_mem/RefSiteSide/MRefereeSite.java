package pt.ua.sd.RopeGame.shared_mem.RefSiteSide;

import pt.ua.sd.RopeGame.enums.WonType;
import pt.ua.sd.RopeGame.info.Bundle;
import pt.ua.sd.RopeGame.info.VectorTimestamp;
import pt.ua.sd.RopeGame.interfaces.IRefereeSiteCoach;
import pt.ua.sd.RopeGame.interfaces.IRefereeSiteContestant;
import pt.ua.sd.RopeGame.interfaces.IRefereeSiteReferee;
import pt.ua.sd.RopeGame.interfaces.RefereeSiteInterface;
import pt.ua.sd.RopeGame.structures.GameStat;

import java.rmi.RemoteException;

/**
 * Refereee Site shared memory<br>
 *<b><center><font size=6>Refereee Site shared memory</font></center></b><br>
 *     <font size=4>This class represents the monitor/shared memory of the referee site.</font>
 *
 *
 */
public class MRefereeSite implements RefereeSiteInterface{


    /**
     *
     * @return  the number of games played
     */
    public Bundle getN_games_played(VectorTimestamp vectorTimestamp) throws RemoteException {
        updVectorTimestamp(vectorTimestamp);//update vector

        return  new Bundle(localvectorTimestamp, n_games_played);
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

        return new Bundle(vectorTimestamp);
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
            return new Bundle(vectorTimestamp,new GameStat((n_games_played<n_games),knock_out, WonType.KNOCKOUT));
        }
        else if(knock_out == 2){

            return new Bundle(vectorTimestamp,new GameStat((n_games_played<n_games),knock_out, WonType.KNOCKOUT));
        }
        else if(score_T1>score_T2)
        {

            return new Bundle(vectorTimestamp,new GameStat((n_games_played<n_games),1,WonType.POINTS));
        }
        else if(score_T1<score_T2){

            return new Bundle(vectorTimestamp,new GameStat((n_games_played<n_games),2,WonType.POINTS));
        }

        return new Bundle(vectorTimestamp, new GameStat((n_games_played<n_games),0,WonType.DRAW));

    }

    @Override
    public void terminate() throws RemoteException {
        //Todo - implement
    }

    public boolean isClosed() {
        return false;
        //Todo - implement
    }
}
