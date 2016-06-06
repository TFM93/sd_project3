package pt.ua.sd.RopeGame.active_entities.ContestantSide;


import pt.ua.sd.RopeGame.interfaces.BenchInterface;
import pt.ua.sd.RopeGame.interfaces.PlaygroundInterface;
import pt.ua.sd.RopeGame.interfaces.RefereeSiteInterface;
import pt.ua.sd.RopeGame.interfaces.RepoInterface;
import pt.ua.sd.RopeGame.shared_mem.RepoSide.RepoServer;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Random;

public class ContestantClient {

    public static void main(String[] args) {
        if (args.length != 7) {
            System.out.println("Usage: <registry host> <registry port> "
                    + "<number of players> <number of players pushing> <number of trials> <number of games> <knockout differential>");
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
        int nEntities = 6 + (2*n_players);

        System.out.println(n_players);
        System.out.println(n_players_pushing);

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


        Random rn = new Random();


        /*  instantiate the contestants  */
        Contestant[] contestants1 = new Contestant[n_players];  // Contestants array
        Contestant[] contestants2 = new Contestant[n_players];  // Contestants array

        /*  create the contestants  */
        for (int i = 0; i < n_players; i++) {
            contestants1[i] = new Contestant(i, 1, rn.nextInt(20 - 10 + 1) + 10,
                    playground,
                    bench,
                    repo,
                    n_players,
                    n_players_pushing,
                    i+3,
                    nEntities);
            contestants2[i] = new Contestant(i, 2, rn.nextInt(20 - 10 + 1) + 10,
                    playground,
                    bench,
                    repo,
                    n_players,
                    n_players_pushing,
                    i+3+n_players,
                    nEntities);
        }

        /*  start simulation  */
        for (int i = 0; i < n_players; i++) {
            contestants1[i].start();
            contestants2[i].start();
            System.out.println("Contestant  " + i + " started!");
        }

        // Wait for end of the simulation
        for (int i = 0; i < n_players; i++) {
            try {
                contestants1[i].join();
                contestants2[i].join();
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

            System.out.println("Contestant " + i + " terminated!");
        }

    }
}
