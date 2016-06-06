package pt.ua.sd.RopeGame.shared_mem.BenchSide;


import java.rmi.registry.Registry;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import pt.ua.sd.RopeGame.interfaces.Register;
import pt.ua.sd.RopeGame.interfaces.BenchInterface;

public class BenchServer {

    /**
     * Main class
     * @param args arguments
     */
    public static void main(String[] args) {

        if (args.length != 4) {
            System.out.println("Usage: <registry host name> <registry port number> <listening port> <number of players per team>");
            System.exit(0);
        }

        /* get location of the generic registry service */
        String rmiRegHostName = args[0];
        int rmiRegPortNumb = Integer.parseInt(args[1]);
        int listeningPort = Integer.parseInt(args[2]);//porta a escuta do server
        int nPlayers = Integer.parseInt(args[3]);
        int nEntities = 3 + (2*nPlayers) + 3;//number of players plus both coaches and an referee. the last sum is referred to the shared memories

        // Step 1 - Get Repository remote object

        /* look for the remote object by name in the remote host registry */
        Registry registry = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("RMI registry create exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        // Step 2 - Register Shop remote object

        /* create and install the security manager */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        System.out.println("Security manager was created and installed!");

        /* instantiate a remote object that runs mobile code and generate a stub for it */
        MContestantsBench bench = new MContestantsBench(nEntities);
        BenchInterface benchStub = null;
        //int listeningPort = 22214;                            /* it should be set accordingly in each case */

        try {
            benchStub = (BenchInterface) UnicastRemoteObject.exportObject(bench, listeningPort);
        } catch (RemoteException e) {
            System.out.println("bench stub generation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Stub was generated!");

        /* register it with the general registry service */
        String nameEntryBase = "RegisterHandler";
        String nameEntryObject = "Bench";
        registry = null;
        Register reg = null;

        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("RMI registry creation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("RMI registry was created!");

        try {
            reg = (Register) registry.lookup(nameEntryBase);
        } catch (RemoteException e) {
            System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (NotBoundException e) {
            System.out.println("RegisterRemoteObject not bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        try {
            reg.bind(nameEntryObject, benchStub);
        } catch (RemoteException e) {
            System.out.println("Shop registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("Shop already bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Shop object was registered!");

        // Wait for shop to terminate
        while (!bench.isClosed()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {}
        }

        System.out.println("Shop terminated!");
        System.exit(0);
    }

}
