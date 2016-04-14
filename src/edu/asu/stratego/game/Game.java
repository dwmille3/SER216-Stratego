package edu.asu.stratego.game;

import edu.asu.stratego.game.board.ClientBoard;

/**
 * Contains information about the Stratego game, which is shared between the
 * JavaFX GUI and the ClientGameManager.
 *
 * @see edu.asu.stratego.gui.ClientStage
 * @see edu.asu.stratego.game.ClientGameManager
 */
public class Game {
    private static volatile Player player = null;
    private static volatile Player opponent = null;

    private static volatile Move move = null;
    private static volatile MoveStatus moveStatus = null;

    private static volatile GameStatus status = null;
    private static volatile PieceColor turn = null;
    private static volatile ClientBoard board = null;

    /**
     * Initializes data fields for a new game.
     */
    public Game() {
    }

    /**
     * @return Player object containing information about the player.
     */
    public static Player getPlayer() {
        if (Game.player == null)
            player = new Player();
        return player;
    }

    /**
     * @param player Player object containing information about the player.
     */
    public static void setPlayer(Player player) {
        Game.player = player;
    }

    /**
     * @return Player object containing information about the opponent.
     */
    public static Player getOpponent() {
        if (opponent == null)
            opponent = new Player();
        return opponent;
    }

    /**
     * @param opponent Player object containing information about the opponent.
     */
    public static void setOpponent(Player opponent) {
        Game.opponent = opponent;
    }

    /**
     * @return value the status of the game.
     */
    public static GameStatus getStatus() {
        if (status == null)
            status = GameStatus.SETTING_UP;
        return status;
    }

    /**
     * @param status the status of the game
     */
    public static void setStatus(GameStatus status) {
        Game.status = status;
    }

    /**
     * @return value the color of the current player's turn
     */
    public static PieceColor getTurn() {
        if (turn == null)
            turn = PieceColor.RED;
        return turn;
    }

    /**
     * @param turn the color of the current player's turn
     */
    public static void setTurn(PieceColor turn) {
        Game.turn = turn;
    }

    /**
     * @return the game board.
     */
    public static ClientBoard getBoard() {
        if (board == null)
            board = new ClientBoard();
        return board;
    }

    /**
     * @param board the game board
     */
    public static void setBoard(ClientBoard board) {
        Game.board = board;
    }

    public static Move getMove() {
        if (move == null)
            move = new Move();
        return move;
    }

    public static void setMove(Move move) {
        Game.move = move;
    }

    public static MoveStatus getMoveStatus() {
        if (moveStatus == null)
            moveStatus = MoveStatus.OPP_TURN;
        return moveStatus;
    }

    public static void setMoveStatus(MoveStatus moveStatus) {
        Game.moveStatus = moveStatus;
    }
}