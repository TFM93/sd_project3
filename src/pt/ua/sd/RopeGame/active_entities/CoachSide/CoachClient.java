package pt.ua.sd.RopeGame.active_entities.CoachSide;
import pt.ua.sd.RopeGame.interfaces.BenchInterface;
import pt.ua.sd.RopeGame.interfaces.PlaygroundInterface;
import pt.ua.sd.RopeGame.interfaces.RefereeSiteInterface;
import pt.ua.sd.RopeGame.interfaces.RepoInterface;
import pt.ua.sd.RopeGame.shared_mem.RepoSide.RepoServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class CoachClient {

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: <registry host> <registry port> "
                    + "<number of players> <number of players pushing> <number of trials> <number of games> <knockout differential> <number of entities>");
            System.exit(0);
        }

        /* get location of the generic registry service */
        String rmiRegHostName = args[0];
        int rmiRegPortNumb = Integer.parseInt(args[1]);
        int n_players = Integer.parseInt(args[2]);
        int n_players_pushing = Integer.parseInt(args[3]);
        int n_trials = Integer.parseInt(args[4]);
        int n_games = Integer.parseInt(args[5]);
        int knockdif = Integer.parseInt(args[6]);
        int nEntities = Integer.parseInt(args[7]);

        /*  fetch remote object  */
        String nameEntryBench = "Bench";
        String nameEntryPlayground = "Playground";
        String nameEntryRefereeSite = "RefereeSite";
        String nameEntryRepository = "Repository";
        BenchInterface bench = null;
        PlaygroundInterface playground = null;
        RefereeSiteInterface refSite = null;
        RepoInterface repo = null;
        Registry registry = null;

        /*  get the registry  */
        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("RMI registry create exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        /*  get the bench  */
        try {
            bench = (BenchInterface) registry.lookup(nameEntryBench);
        } catch (RemoteException e) {
            System.out.println("ContestantClient look up exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("ContestantClient not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        /*  get the playground  */
        try {
            playground = (PlaygroundInterface) registry.lookup(nameEntryPlayground);
        } catch (RemoteException e) {
            System.out.println("ContestantClient look up exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("ContestantClient not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }


        /*  get the ref site  */
        try {
            refSite = (RefereeSiteInterface) registry.lookup(nameEntryRefereeSite);
        } catch (RemoteException e) {
            System.out.println("ContestantClient look up exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("ContestantClient not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }


        /*  get the repo  */
        try {
            repo = (RepoInterface) registry.lookup(nameEntryRepository);
        } catch (RemoteException e) {
            System.out.println("ContestantClient look up exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("ContestantClient not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }


        /*  get nContestants from configuration  */
        int nContestants = 3;


        /*  instantiate the contestants  */
        /*  ARRANJAR VECTOR TIMESTAMP ID!!!!!!!!!!!!  */
        Coach coach1 = new Coach(0, 1, playground,
                refSite,
                bench,
                repo,
                n_players,
                n_players_pushing,
                n_trials,
                n_games,
                knockdif,
                2,
                nEntities);  // Customers array
        Coach coach2 = new Coach(1, 2, playground,
                refSite,
                bench,
                repo,
                n_players,
                n_players_pushing,
                n_trials,
                n_games,
                knockdif,
                3,
                nEntities);  // Customers array


        /*  start simulation  */
        coach1.start();
        coach2.start();

        // Wait for end of the simulation
            try {
                coach1.join();
                coach2.join();
                bench.terminate();
                playground.terminate();
                refSite.terminate();
                repo.terminate();
            } catch (InterruptedException e) {
            } catch (RuntimeException e) {
            } catch (RemoteException e) {
                System.out.println("Error running contestant: " + e.getMessage());
                e.printStackTrace();
            }

            System.out.println("Coach " + coach1.getTeam_id() + " terminated!");

    }
}
