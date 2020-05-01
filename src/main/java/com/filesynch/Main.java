package com.filesynch;

import com.bulenkov.darcula.DarculaLaf;
import com.filesynch.dto.ClientInfoDTO;
import com.filesynch.dto.ClientStatus;
import com.filesynch.dto.SettingsDTO;
import com.filesynch.gui.ConnectToServer;
import com.filesynch.gui.FileSynchronizationClient;
import com.filesynch.logger.Logger;
import com.filesynch.rmi.ClientGui;
import com.filesynch.rmi.ClientRmiInt;

import javax.swing.*;
import javax.swing.plaf.basic.BasicLookAndFeel;
import java.awt.event.WindowAdapter;
import java.rmi.Naming;
import java.rmi.RemoteException;

public class Main {
    public static JFrame connectToServerFrame;
    public static ConnectToServer connectToServer;
    public static JFrame clientFrame;
    public static FileSynchronizationClient fileSynchronizationClient;
    public static ClientGui clientGui;
    public static ClientRmiInt clientRmi;
    public static ClientInfoDTO clientInfoDTO;

    public static void main(String[] args) throws Exception {
        BasicLookAndFeel darcula = new DarculaLaf();
        UIManager.setLookAndFeel(darcula);
        try {
            clientRmi = (ClientRmiInt) Naming.lookup("rmi://localhost:36790/gui");
            clientGui = new ClientGui(clientRmi);
            clientInfoDTO = clientRmi.connectGuiToClient(clientGui);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        System.setProperty("java.awt.headless", "false");

        ClientStatus currentClientStatus = ClientStatus.NEW;
        try {
            currentClientStatus = clientRmi.getClientStatus();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        if (currentClientStatus == ClientStatus.NEW) {
            connectToServerFrame = new JFrame("Connect To Server");
            connectToServer = new ConnectToServer();
            connectToServerFrame.setContentPane(connectToServer.getJPanelMain());
            connectToServerFrame.pack();
            connectToServerFrame.setLocationRelativeTo(null);
            connectToServerFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            connectToServerFrame.setVisible(true);

            if (clientInfoDTO != null) {
                connectToServer.getJTextFieldName().setText(clientInfoDTO.getName());
            }
        } else {
            fileSynchronizationClient = new FileSynchronizationClient();
            Logger.logArea = fileSynchronizationClient.getTextPane1();

            clientFrame = new JFrame("File Synchronization Client");
            clientFrame.setContentPane(fileSynchronizationClient.getJPanelClient());
            clientFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
            clientFrame.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    if (JOptionPane.showConfirmDialog(clientFrame,
                            "Are you sure you want to close this window?", "Close Window?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                        try {
                            //client.logoutFromServer();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    }
                }
            });
            clientFrame.pack();
            clientFrame.setLocationRelativeTo(null);
            clientFrame.setVisible(true);
        }
    }

    public static void connectToServer(String ip, String port, ClientInfoDTO clientInfoDTO) {
        fileSynchronizationClient = new FileSynchronizationClient();
        Logger.logArea = fileSynchronizationClient.getTextPane1();

        try {
            clientRmi.connectToServer(ip, port, clientInfoDTO);
        } catch (RemoteException e) {
            e.printStackTrace();
        }


        connectToServerFrame.setVisible(false);
        clientFrame = new JFrame("File Synchronization Client");
        clientFrame.setContentPane(fileSynchronizationClient.getJPanelClient());
        clientFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        clientFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                if (JOptionPane.showConfirmDialog(clientFrame,
                        "Are you sure you want to close this window?", "Close Window?",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION) {
                    try {
                        //client.logoutFromServer();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                }
            }
        });
        clientFrame.pack();
        clientFrame.setLocationRelativeTo(null);
        clientFrame.setVisible(true);
    }

    public static void sendMessage(String message) {
        try {
            clientRmi.sendMessage(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void sendFile(String file) {
        try {
            clientRmi.sendFile(file);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void sendAllFiles() {
        try {
            clientRmi.sendAllFiles();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static SettingsDTO getSettings() {
        try {
            return clientRmi.getSettings();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return new SettingsDTO();
    }

    public static void setSettings(SettingsDTO settings) {
        try {
            clientRmi.setSettings(settings);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void logout() {
        try {
            clientRmi.logout();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
