package com.filesynch.rmi;

import com.filesynch.logger.Logger;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class ClientGui extends UnicastRemoteObject implements ClientGuiInt {
    private ClientRmiInt clientRmiInt;

    public ClientGui(ClientRmiInt clientRmiInt) throws RemoteException {
        super();
        this.clientRmiInt = clientRmiInt;
    }

    @Override
    public void log(String stringToLog) throws RemoteException {
        Logger.log(stringToLog);
    }

    @Override
    public void logYellow(String stringToLog) throws RemoteException {
        Logger.logYellow(stringToLog);
    }

    @Override
    public void logBlue(String stringToLog) throws RemoteException {
        Logger.logBlue(stringToLog);
    }

    @Override
    public void logGreen(String stringToLog) throws RemoteException {
        Logger.logGreen(stringToLog);
    }

    @Override
    public void logRed(String stringToLog) throws RemoteException {
        Logger.logRed(stringToLog);
    }
}
