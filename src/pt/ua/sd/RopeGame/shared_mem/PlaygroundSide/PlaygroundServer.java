package pt.ua.sd.RopeGame.shared_mem.PlaygroundSide;


import pt.ua.sd.RopeGame.interfaces.PlaygroundInterface;
import pt.ua.sd.RopeGame.interfaces.Register;

import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class PlaygroundServer {
    public static void main(String[] args){
        if (args.length != 3) {
            System.out.println("Usage: <registry host name> <registry port number> <listening port> ");
            System.exit(0);
        }


        /* get location of the generic registry service */
        String rmiRegHostName = args[0];
        int rmiRegPortNumb = Integer.parseInt(args[1]);
        int listeningPort = Integer.parseInt(args[2]);//porta a escuta do server

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
        MPlayground playground = new MPlayground();
        PlaygroundInterface playgStub = null;
        //int listeningPort = 22214;                            /* it should be set accordingly in each case */

        try {
            playgStub = (PlaygroundInterface) UnicastRemoteObject.exportObject(playground, listeningPort);
        } catch (RemoteException e) {
            System.out.println("Playground stub generation exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Stub was generated!");

        /* register it with the general registry service */
        String nameEntryBase = "RegisterHandler";
        String nameEntryObject = "Playground";
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
            reg.bind(nameEntryObject, playgStub);
        } catch (RemoteException e) {
            System.out.println("Playground registration exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (AlreadyBoundException e) {
            System.out.println("Playground already bound exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Playground object was registered!");

        // Wait for shop to terminate
        while (!playground.isClosed()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                break;
            }
        }

        System.out.println("Playground terminated!");
        System.exit(0);
    }
}