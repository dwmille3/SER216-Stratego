package edu.asu.stratego.tests;

import edu.asu.stratego.game.ClientSocket;
import edu.asu.stratego.game.ServerGameManager;
import org.junit.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.Assert.*;

public class ClientGameManagerTest {

    private void startPseudoClient() throws Exception {
        new Thread(() -> {

            try {
                Thread.sleep(200);
                ClientSocket.connect("localhost", 4212);
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private boolean startPseudoServer() throws Exception {
        try {
            ServerSocket listener = new ServerSocket(4212);
            startPseudoClient();
            startPseudoClient();
            Socket playerOne = listener.accept();
            Socket playerTwo = listener.accept();
            new ServerGameManager(playerOne, playerTwo, 0);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Test
    public void testConnectToServer() throws Exception {
        assertEquals(startPseudoServer(), true);
    }

}