package edu.asu.stratego.gui;

import javafx.stage.Stage;

import java.awt.*;

/**
 * The ConnectionStage class, which inherits from the JavaFX Stage class, is a
 * preset Stage for facilitating easy navigation between scenes in the Client
 * application.
 */
public class ClientStage extends Stage {

    private ConnectionScene connection;
    private WaitingScene waiting;
    private BoardScene board;

    /**
     * Creates a new instance of ClientStage.
     */
    public ClientStage() {
        // Calculate the BoardScene dimensions from screen resolution.
        setConnectionScene();
        this.setTitle("ASU Stratego");
        this.setResizable(false);
        this.show();
    }

    /**
     * @return the board scene side length (in pixels) divided by 12.
     */
    public static double getUnit() {
        return getSide() / 12.0;
    }

    /**
     * @return the side length of the board scene (in pixels)
     */
    public static int getSide() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return (int) (0.85 * screenSize.getHeight()) / 12 * 12;
    }

    /**
     * Switch to the Connection Scene.
     *
     * @see edu.asu.stratego.gui.ConnectionScene
     */
    public void setConnectionScene() {
        connection = new ConnectionScene();
        this.setScene(getConnection().scene);
    }

    /**
     * Switch to the Waiting Scene.
     *
     * @see edu.asu.stratego.gui.WaitingScene
     */
    public void setWaitingScene() {
        waiting = new WaitingScene();
        this.setScene(waiting.scene);
    }

    /**
     * Switch to the Board Scene.
     *
     * @see edu.asu.stratego.gui.BoardScene
     */
    public void setBoardScene() {
        board = new BoardScene();
        this.setScene(board.scene);
    }

    /**
     * Returns the ConnectionScene created in the ClientStage instance.
     *
     * @return ConnectionScene object
     */
    public ConnectionScene getConnection() {
        return connection;
    }
}