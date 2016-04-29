package edu.asu.stratego.gui;

import edu.asu.stratego.game.ClientSocket;
import edu.asu.stratego.game.Game;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;

import java.io.IOException;

/**
 * Wrapper class for a JavaFX scene. Contains a scene UI and its associated
 * event handlers for retrieving network connection information from the player
 * and connecting to the network.
 */
public class ConnectionScene {

    private static final Object playerLogin = new Object();
    private static final int WIDTH = 1150;
    private static final int HEIGHT = 300;
    static Label statusLabel = new Label();
    private static String serverIP, nickname;
    Scene scene;
    private Button submitFields = new Button("Enter Battlefield");
    private TextField nicknameField = new TextField();
    private TextField serverIPField = new TextField();
    private Label instructions = new Label("Stratego Instructions\n\n"
	    		+ "Stratego is a game in which you need to "
	    		+ "capture the flag of your opponent while defending your own flag. \n"
	    		+ "To capture the flag you use your army of 40 pieces. "
	    		+ "Pieces have a rank and represent individual officers \nand "
	    		+ "soldiers in an army. In addition to those ranked pieces you "
	    		+ "can use bombs to protect your flag.\n\n" 
	    		+ "Pieces move 1 square per turn, horizontally or vertically. "
	    		+ "Only the scout can move over multiple empty squares per turn.\n "
	    		+ "Pieces cannot jump over another piece. "
	    		+ "If a piece is moved onto a square occupied by an opposing piece,\n "
	    		+ "their identities are revealed. The weaker piece is removed from the board, "
	    		+ "and the stronger piece is moved into the place formerly\n "
	    		+ "occupied by the weaker piece. If the engaging pieces are "
	    		+ "of equal rank, they are both removed. Pieces may not move\n "
	    		+ "onto a square already occupied by another piece without "
	    		+ "attacking. Exception to the rule of the higher rank winning is the spy.\n"
	    		+ "When the spy attacks the marshal, the spy defeats the higher ranked marshal. "
	    		+ "However, when the marshal attacks the spy, the spy loses.\n "
	    		+ "Bombs lose when they are defused by a miner.\n\n"
	    		+ "The bombs and the flag cannot be moved. A bomb defeats every piece "
	    		+ "that tries to attack it, except the miner. The flag loses from every\n "
	    		+ "other piece. When you capture the flag of your opponent you win the game.\n\n"
	    		+ "The Stratego board consists of 10 x 10 squares. Within the board there "
	    		+ "are two obstacles of 2 x 2 squares each. Pieces are not allowed to move there.");

    /**
     * Creates a new instance of ConnectionScene.
     */
    ConnectionScene() {
        // Create UI.
        GridPane gridPane = new GridPane();
        gridPane.add(new Label("Nickname: "), 0, 0);
        gridPane.add(new Label("Server IP: "), 0, 1);
        gridPane.add(nicknameField, 1, 0);
        gridPane.add(serverIPField, 1, 1);
        gridPane.add(submitFields, 1, 3);
        gridPane.add(instructions, 0, 3);

        BorderPane borderPane = new BorderPane();
        BorderPane.setMargin(statusLabel, new Insets(0, 0, 10, 0));
        BorderPane.setAlignment(statusLabel, Pos.CENTER);
        borderPane.setBottom(statusLabel);
        borderPane.setCenter(gridPane);

        // UI Properties.
        GridPane.setHalignment(submitFields, HPos.RIGHT);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(5);
        gridPane.setVgap(5);

        // Event Handler.
        submitFields.setOnAction(e -> Platform.runLater(new ProcessFields()));
        

        scene = new Scene(borderPane, WIDTH, HEIGHT);
    }

    /**
     * A Runnable task for establishing a connection to a Stratego server.
     * The task will continue running until a successful connection has
     * been made. The connection attempt loop is structured like so:
     * <p>
     * <ol><li>
     * Wait for the player to invoke button event in the ConnectionScene.
     * </li><li>
     * Attempt to connect to a Stratego server using the information retrieved
     * from the UI and wake up the button event thread.
     * </li><li>
     * If connection succeeds, signal the isConnected condition to indicate to
     * other threads a successful connection attempt and then terminate the
     * task. Otherwise, output error message to GUI, and go to #1.
     * </li></ol>
     *
     * @see edu.asu.stratego.gui.ConnectionScene.ProcessFields
     */
    public static class ConnectToServer implements Runnable {
        @Override
        public void run() {

            while (ClientSocket.getInstance() == null) {
                synchronized (playerLogin) {
                    try {
                        // Wait for submitFields button event.
                        playerLogin.wait();

                        // Attempt connection to server.
                        ClientSocket.connect(serverIP, 4212);
                    } catch (IOException | InterruptedException e) {
                        Platform.runLater(() -> {
                            statusLabel.setText("Cannot connect to the Server");
                        });
                    } finally {
                        // Wake up button event thread.
                        playerLogin.notify();
                    }
                }
            }
        }
    }

    /**
     * Event handler task for submitFields button events. Notifies the
     * connectToServer thread that connection information has been received
     * from the user.
     * <p>
     * <p>
     * The method call to wait() will cause the event to hang until it is woken
     * up by another thread signaling that a connection attempt has been made.
     * Until the thread running this task is notified, the form fields will
     * be disabled preventing the user from firing another event.
     * </p>
     *
     * @see edu.asu.stratego.gui.ConnectionScene.ConnectToServer
     */
    private class ProcessFields implements Runnable {
        @Override
        public void run() {
            Platform.runLater(() -> {
                statusLabel.setText("Connecting to the server...");
            });

            nickname = nicknameField.getText();
            serverIP = serverIPField.getText();

            // Default values.
            if (nickname.equals(""))
                nickname = "Player";
            if (serverIP.equals(""))
                serverIP = "localhost";

            Game.getPlayer().setNickname(nickname);

            nicknameField.setEditable(false);
            serverIPField.setEditable(false);
            submitFields.setDisable(true);

            synchronized (playerLogin) {
                try {
                    playerLogin.notify();  // Signal submitFields button event.
                    playerLogin.wait();    // Wait for connection attempt.
                } catch (InterruptedException e) {
                    // TODO Handle this exception somehow...
                    e.printStackTrace();
                }
            }

            nicknameField.setEditable(true);
            serverIPField.setEditable(true);
            submitFields.setDisable(false);
        }
    }
}
