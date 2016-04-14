package edu.asu.stratego.gui;

import edu.asu.stratego.game.Game;
import edu.asu.stratego.gui.board.BoardTurnIndicator;
import edu.asu.stratego.gui.board.setup.SetupPanel;
import edu.asu.stratego.media.ImageConstants;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * Wrapper class for a JavaFX scene. Contains a scene UI and its associated
 * event handlers for playing a game of Stratego.
 */
public class BoardScene {

    private static final double UNIT = ClientStage.getUnit();
    private static final int SIDE = ClientStage.getSide();

    private static StackPane root = null;
    private static GridPane setupPanel = null;

    private static ImageView border = null;
    private static Rectangle background = null;

    Scene scene;

    /**
     * Creates a new instance of BoardScene.
     */
    public BoardScene() {
                
        /* ================ Board Design ================
         * 
         * The scene is divided into a 12 x 12 grid.
         * Each unit represents a 1 x 1 area.
         * 
         * The scene should be about roughly 85% of the 
         * square of the height of the player's screen 
         * resolution.
         * 
         *          = = = = = = = = = = = =
         *          = + + + + + + + + + + =
         *          = + + + + + + + + + + =
         *          = + + + + + + + + + + =
         *          = + + + + + + + + + + =
         *          = + + + + + + + + + + =
         *          = + + + + + + + + + + =
         *          = + + + + + + + + + + =
         *          = + + + + + + + + + + =
         *          = + + + + + + + + + + =
         *          = + + + + + + + + + + =
         *          = = = = = = = = = = = =
         * 
         * Each '=' indicates part of the board border.
         * Each '+' indicates an individual board square.
         * 
         * Part of the border image is semi-transparent so
         * that the scene background color can come through 
         * to indicate which player's turn it is.
         */

        // TODO Sound test here
        //PlaySound.playMusic("cornfield", 1);

        // Resize the board.
        final int size = 10;
        for (int row = 0; row < size; ++row) {
            for (int col = 0; col < size; ++col) {
                Game.getBoard().getSquare(row, col).getPiecePane().getPiece().setFitHeight(UNIT);
                Game.getBoard().getSquare(row, col).getPiecePane().getPiece().setFitWidth(UNIT);
                Game.getBoard().getSquare(row, col).getEventPane().getHover().setFitHeight(UNIT);
                Game.getBoard().getSquare(row, col).getEventPane().getHover().setFitWidth(UNIT);
            }
        }

        // Set the background color (turn indicator).
        new BoardTurnIndicator();


        // Create the setup panel.
        new SetupPanel();
        StackPane.setMargin(getSetupPanel(), new Insets(UNIT, 0, 0, 0));
        StackPane.setAlignment(getSetupPanel(), Pos.TOP_CENTER);

        // Show Board GUI.
        Game.getBoard().getPiecePane().setAlignment(Pos.CENTER);
        Game.getBoard().getEventPane().setAlignment(Pos.CENTER);

        StackPane rootPane = getRootPane();
        rootPane.setMaxSize(SIDE, SIDE);

        scene = new Scene(rootPane, SIDE, SIDE);
    }

    private static ImageView getBorder() {
        if (border == null)
            border = new ImageView(ImageConstants.BORDER);
        StackPane.setAlignment(border, Pos.CENTER);
        border.setFitHeight(SIDE);
        border.setFitWidth(SIDE);
        return border;
    }

    private static Rectangle getBackground() {
        if (background == null)
            background = BoardTurnIndicator.getTurnIndicator();
        return background;
    }

    public static StackPane getRootPane() {
        if (root == null)
            root = new StackPane(getBackground(), Game.getBoard().getPiecePane(), Game.getBoard().getEventPane(),
                                 getSetupPanel(), getBorder());
        return root;
    }

    public static GridPane getSetupPanel() {
        if (setupPanel == null)
            setupPanel = SetupPanel.getSetupPanel();
        return setupPanel;
    }
}