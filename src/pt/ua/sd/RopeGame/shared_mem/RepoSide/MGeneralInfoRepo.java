package pt.ua.sd.RopeGame.shared_mem.RepoSide;

import pt.ua.sd.RopeGame.enums.CoachState;
import pt.ua.sd.RopeGame.enums.ContestantState;
import pt.ua.sd.RopeGame.enums.RefState;
import pt.ua.sd.RopeGame.enums.WonType;
import pt.ua.sd.RopeGame.info.Bundle;
import pt.ua.sd.RopeGame.info.VectorTimestamp;
import pt.ua.sd.RopeGame.interfaces.IRepoCoach;
import pt.ua.sd.RopeGame.interfaces.IRepoContestant;
import pt.ua.sd.RopeGame.interfaces.IRepoReferee;
import pt.ua.sd.RopeGame.interfaces.RepoInterface;

import java.io.*;
import java.nio.file.*;
import java.rmi.RemoteException;
import java.util.Arrays;

/**
 * Logging repository<br>
 *<b><center><font size=6>Logging repository</font></center></b><br>
 *     <font size=4>This class represents the shared memory for the logging.
 *     The logging file is saved in the root of the project.
 *     Info saved on RopeGame.log
 */
public class MGeneralInfoRepo implements RepoInterface{

    /**
     * Internal Data
     */
    private int referee_trial_number = 0;
    private static int PS_center = Integer.MAX_VALUE;//position of the center of rope

    private enum refStates{//abreviation of referee states
        SOM,SOG,TSR,WTC,EOM,EOG,NON
    }

    private enum  coachStates{//abreviation of coach states
        WRC,AST,WTR,NON
    }

    private enum contestantStates{//abreviation of contestant states
        SAB,SIP,DYB,NON
    }

    private static refStates referee_state;//referee state
    private static coachStates[] coach_state;//each coach state is saved here
    private static contestantStates[] team1_state;//each team 1 contestant state is saved here
    private static contestantStates[] team2_state;//each team 2 contestant state is saved here
    private static int[] team1_strength;//strenght of each contestant
    private static int[] team2_strength;//strenght of each contestant
    private static int game_nr;//number of the game
    private static File OUTPUT_FILE;//represents the log file
    private String TO_WRITE;//info that needs to be saved to file
    private static Writer output=null;

    private static int[] contestants_team1;
    private static int[] contestants_team2;


    private int players_pushing;


    /**
     * Constructor
     * @param players_team number of players per team
     * @param players_pushing number of players pushing the rope
     * @param n_trials number of trials
     * @param n_games number of games
     * @param knockDif maximum diference to the rope center to reach a knockout
     */
   public MGeneralInfoRepo(int players_team, int players_pushing, int n_trials, int n_games, int knockDif) {
        this.players_pushing = players_pushing;


        if(contestants_team1 == null){
            contestants_team1 = new int[players_pushing];
            Arrays.fill(contestants_team1, -1);
        }

        if(contestants_team2 == null){
            contestants_team2 = new int[players_pushing];
            Arrays.fill(contestants_team2, -1);
        }

        String LOG_LOCATION = "RopeGame.log";
        TO_WRITE = "";
        TO_WRITE="";//nothing needs to be written now
        OUTPUT_FILE = new File(LOG_LOCATION);
        if(OUTPUT_FILE.exists())//check if the file exists
        {
            deleteFile();//and delete it
        }
        game_nr = 0;//reset game nr
        referee_state = refStates.NON;
        coach_state = new coachStates[2];
        for (int i=0;i<coach_state.length;i++) {
            coach_state[i]= coachStates.NON;
        }

        team1_state = new contestantStates[players_team];
        team2_state = new contestantStates[players_team];
        for (int i=0;i<team1_state.length;i++) {
            team1_state[i]= contestantStates.NON;
            team2_state[i]= contestantStates.NON;
        }

        team1_strength = new int[players_team];
        team2_strength = new int[players_team];
        for (int i=0;i<team1_strength.length;i++) {
            team1_strength[i] = 0;
            team2_strength[i] = 0;
        }

        AddheaderInternal(true);//add initial header
    }

    /**
     * Function responsible to add the header to the log
     * @param first if true prints to file only the initial header, if false also prints the game number
     */
    public synchronized Bundle Addheader(boolean first, VectorTimestamp vectorTimestamp)throws RemoteException
    {
        String temp;//temporary string

        if(first) {
            temp="                               Game of the Rope - Description of the internal state" +
                    "\n\n" +
                    "Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5     Trial    \n" +
                    "Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS\n";
            //System.out.printf(temp);
            TO_WRITE += temp;
        }
        else {
            // game_nr +=1;
            temp = "Game " + game_nr +
                    " \nRef Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5     Trial    \n" +
                    "Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS\n";
            //System.out.printf(temp);
            TO_WRITE += temp;
        }
        return new Bundle(vectorTimestamp);

    }

    /**
     * Function responsible to add the header to the log
     * @param first if true prints to file only the initial header, if false also prints the game number
     */
    public synchronized void AddheaderInternal(boolean first)
    {
        String temp;//temporary string

        if(first) {
            temp="                               Game of the Rope - Description of the internal state" +
                    "\n\n" +
                    "Ref Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5     Trial    \n" +
                    "Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS\n";
            //System.out.printf(temp);
            TO_WRITE += temp;
        }
        else {
            // game_nr +=1;
            temp = "Game " + game_nr +
                    " \nRef Coa 1 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5 Coa 2 Cont 1 Cont 2 Cont 3 Cont 4 Cont 5     Trial    \n" +
                    "Sta  Stat Sta SG Sta SG Sta SG Sta SG Sta SG  Stat Sta SG Sta SG Sta SG Sta SG Sta SG 3 2 1 . 1 2 3 NB PS\n";
            //System.out.printf(temp);
            TO_WRITE += temp;
        }

    }

    /**
     * Updates the referee info and prints the new state in log file
     * @param team_id id of current team
     * @param state state of the referee in current time
     */
    @Override
    public synchronized Bundle coachLog(int team_id, CoachState state, VectorTimestamp vectorTimestamp)throws RemoteException {
        switch (state){
            case WAIT_FOR_REFEREE_COMMAND:
                coach_state[team_id - 1] = coachStates.WRC;
                break;
            case ASSEMBLE_TEAM:
                coach_state[team_id - 1] = coachStates.AST;
                break;
            case WATCH_TRIAL:
                coach_state[team_id - 1] = coachStates.WTR;
                break;
        }

        printStates();
        return new Bundle(vectorTimestamp);

    }

    /**
     * just increase the number of played games
     */
    public synchronized Bundle updGame_nr( VectorTimestamp vectorTimestamp)throws RemoteException
    {
        game_nr +=1;
        return new Bundle(vectorTimestamp);

    }



    /**
     * imprime resultado do jogo ou da partida, ou de ambos
     * @param team_id id of the current team
     * @param wonType knock out, draw or points
     * @param nr_trials number of played trials
     */
    public synchronized Bundle setResult(int team_id, WonType wonType, int nr_trials, VectorTimestamp vectorTimestamp)throws RemoteException
    {
        String temp="";

        if(wonType == WonType.KNOCKOUT)
        {
            //gameWinnerLog(team_id);
            temp = "Game "+ game_nr+" was won by team "+team_id +" by knock out in "+ nr_trials +" trials.\n";
        }
        else if(wonType == WonType.DRAW)
        {
            //gameWinnerLog(3);
            temp = "Game "+game_nr+" was a draw.\n";
        }
        else if(wonType == WonType.POINTS)
        {
            //gameWinnerLog(team_id);
            temp = "Game "+game_nr+" was won by team "+team_id+" by points.\n";

        }

        TO_WRITE += temp;// buffers the added info to be writen in future
        writeToFile();//write the TO_WRITE buffer to file
        return new Bundle(vectorTimestamp);

    }

    /**
     * Logs the match result
     * @param winner team winner
     * @param score1 score of team 1
     * @param score2 score of team 2
     */
    public synchronized Bundle printMatchResult(int winner,int score1, int score2, VectorTimestamp vectorTimestamp)throws RemoteException
    {
        String temp;
        if(score1 != score2)
        {
            temp = "Match was won by team"+ winner+" ("+score1+"-"+score2+").\n";
        }
        else
        {
            temp="Match was draw.\n";
        }
        TO_WRITE += temp;//buffers the info
        writeToFile();//writes the text present in buffer
        return new Bundle(vectorTimestamp);

    }

    /**
     * Logs the referee changes and prints to the file
     * @param state state of the referee
     * @param trial_number number of current trial
     */
    @Override
    public synchronized Bundle refereeLog(RefState state, int trial_number, VectorTimestamp vectorTimestamp)throws RemoteException {
        // START_OF_THE_MATCH, START_OF_A_GAME, TEAMS_READY, WAIT_FOR_TRIAL_CONCLUSION, END_OF_A_GAME, END_OF_A_MATCH
        switch (state){
            case START_OF_THE_MATCH:
                referee_state = refStates.SOM;
                this.referee_trial_number = 0;
                break;
            case START_OF_A_GAME:
                referee_state = refStates.SOG;
                this.referee_trial_number = trial_number;
                break;
            case TEAMS_READY:
                referee_state = refStates.TSR;
                this.referee_trial_number = trial_number;
                break;
            case WAIT_FOR_TRIAL_CONCLUSION:
                referee_state = refStates.WTC;
                this.referee_trial_number = trial_number;
                break;
            case END_OF_A_GAME:
                referee_state = refStates.EOG;
                this.referee_trial_number = trial_number;
                break;
            case END_OF_A_MATCH:
                referee_state = refStates.EOM;
                this.referee_trial_number = trial_number;
                break;
        }

        printStates();//buffers the states
        writeToFile();//writes the buffer to file
        return new Bundle(vectorTimestamp);

    }

    /**
     *Logs the contestant states in the current moment
     * @param id contestant id
     * @param team_id team id
     * @param strength contestant strengt
     * @param state contestant state
     */
    @Override
    public synchronized Bundle contestantLog(int id, int team_id, int strength, ContestantState state, VectorTimestamp vectorTimestamp)throws RemoteException {
        //    SEAT_AT_THE_BENCH, STAND_IN_POSITION, DO_YOUR_BEST, START
        switch (state){
            case SEAT_AT_THE_BENCH:
                if(team_id == 1){
                    team1_state[id] = contestantStates.SAB;
                    team1_strength[id] = strength;

                    for (int i = 0; i < players_pushing; i++){
                        if (contestants_team1[i] == id){
                            contestants_team1[i] = -1;
                        }
                    }
                }else if (team_id == 2){
                    team2_state[id] = contestantStates.SAB;
                    team2_strength[id] = strength;

                    for (int i = 0; i < players_pushing; i++){
                        if (contestants_team2[i] == id){
                            contestants_team2[i] = -1;
                        }
                    }
                }

                break;
            case STAND_IN_POSITION:
                if(team_id == 1){
                    team1_state[id] = contestantStates.SIP;
                    team1_strength[id] = strength;

                    for (int i = 0; i < players_pushing; i++){
                        if (contestants_team1[i] == -1){
                            contestants_team1[i] = id;
                            break;
                        }
                    }



                }else if (team_id == 2){
                    team2_state[id] = contestantStates.SIP;
                    team2_strength[id] = strength;

                    for (int i = 0; i < players_pushing; i++){
                        if (contestants_team2[i] == -1){
                            contestants_team2[i] = id;
                            break;
                        }
                    }
                }
                break;
            case DO_YOUR_BEST:
                if(team_id == 1){
                    team1_state[id] = contestantStates.DYB;
                    team1_strength[id] = strength;


                }else if (team_id == 2){
                    team2_state[id] = contestantStates.DYB;
                    team2_strength[id] = strength;


                }
                break;
        }

        printStates();//buffers the changes
        writeToFile();//writes the changes
        return new Bundle(vectorTimestamp);
    }

    /**
     * buffers the states in current moment for each entity
     */
    private synchronized void printStates(){

        TO_WRITE += String.format("%s   %s %s %02d %s %02d %s %02d %s %02d %s %02d   %s %s %02d %s %02d %s %02d %s %02d %s %02d %s %s %s . %s %s %s %s %s\n",
                referee_state,
                coach_state[0],
                team1_state[0],
                team1_strength[0],
                team1_state[1],
                team1_strength[1],
                team1_state[2],
                team1_strength[2],
                team1_state[3],
                team1_strength[3],
                team1_state[4],
                team1_strength[4],
                coach_state[1],
                team2_state[0],
                team2_strength[0],
                team2_state[1],
                team2_strength[1],
                team2_state[2],
                team2_strength[2],
                team2_state[3],
                team2_strength[3],
                team2_state[4],
                team2_strength[4],
                (contestants_team1[2] != -1) ? String.format("%01d", contestants_team1[2]+1) : "-",
                (contestants_team1[1] != -1) ? String.format("%01d", contestants_team1[1]+1) : "-",
                (contestants_team1[0] != -1) ? String.format("%01d", contestants_team1[0]+1) : "-",
                (contestants_team2[0] != -1) ? String.format("%01d", contestants_team2[0]+1) : "-",
                (contestants_team2[1] != -1) ? String.format("%01d", contestants_team2[1]+1) : "-",
                (contestants_team2[2] != -1) ? String.format("%01d", contestants_team2[2]+1) : "-",
                (referee_trial_number != 0) ? String.format("%02d", referee_trial_number) : "--",
                (PS_center != Integer.MAX_VALUE) ? String.format("%02d", PS_center) : "--");

    }


    /**
     * updates the center of the rope locally
     * @param new_val new rope center
     */
    public synchronized Bundle updtRopeCenter(int new_val, VectorTimestamp vectorTimestamp)throws RemoteException
    {
        PS_center = new_val;
        return new Bundle(vectorTimestamp);

    }

    /**
     * writes the text present in TO_WRITE buffer to file
     */
    private synchronized void writeToFile(){
        //use buffering

        try {
            output = new BufferedWriter(new FileWriter(OUTPUT_FILE,true));
            //FileWriter always assumes default encoding is OK!
            output.write(TO_WRITE);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (output != null) try {
                output.close();
                TO_WRITE="";
            } catch (IOException ignore) {}
        }
    }

    /**
     * deletes the RopeGame.log file
     */
    private void deleteFile()
    {
        try {
            Files.delete(OUTPUT_FILE.toPath());
        } catch (NoSuchFileException x) {
            System.err.format("%s: no such" + " file or directory%n", OUTPUT_FILE.toPath());
        } catch (DirectoryNotEmptyException x) {
            System.err.format("%s not empty%n", OUTPUT_FILE.toPath());
        } catch (IOException x) {
            // File permission problems are caught here.
            System.err.println(x.toString());
        }
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