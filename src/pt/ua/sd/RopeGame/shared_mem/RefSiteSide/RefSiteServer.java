package pt.ua.sd.RopeGame.shared_mem.RefSiteSide;


import pt.ua.sd.RopeGame.interfaces.RefereeSiteInterface;
import pt.ua.sd.RopeGame.interfaces.Register;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RefSiteServer {
    public static void main(String[] args){
        if (args.length != 4) {
            System.out.println("Usage: <registry host name> <registry port number> <listening port> <number of players>");
            System.exit(0);
        }



        /* get location of the generic registry service */
        String rmiRegHostName = args[0];
        int rmiRegPortNumb = Integer.parseInt(args[1]);
        int listeningPort = Integer.parseInt(args[2]);//porta a escuta do server
        int nPlayers = Integer.parseInt(args[3]);
        int nEntities = nPlayers + 6;

        /* look for the remote object by name in the remote host registry */
        Registry registry = null;


        try {
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumb);
        } catch (RemoteException e) {
            System.out.println("RMI registry create exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
       /* create and install the security manager */
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        System.out.println("Security manager was created and installed!");

        /* instantiate a remote object that runs mobile code and generate a stub for it */
        MRefereeSite refsite = new MRefereeSite(nEntities);
        RefereeSiteInterface refsiteStub = null;
        //int listeningPort = 22214;                            /* it should be set accordingly in each case */

        try {
            refsiteStub = (RefereeSiteInterface) UnicastRemoteObject.exportObject(refsite, listeningPort);
        } catch (RemoteException e) {
            System.out.println("Referee Site stub generation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Stub was generated!");

        /* register it with the general registry service */
        String nameEntryBase = "RegisterHandler";
        String nameEntryObject = "RefereeSite";
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
            reg.bind(nameEntryObject, refsiteStub);
        } catch (RemoteException e) {
            System.out.println("Referee Site registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("Referee Site already bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Referee Site object was registered!");

        // Wait for shop to terminate
        while (!refsite.isClosed()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                break;
            }
        }

        System.out.println("RefereeSite terminated!");
        System.exit(0);
    }

}
