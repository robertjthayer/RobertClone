package com;

import com.connect4.engine.board.Board;
import com.connect4.engine.board.BoardUtils;
import com.connect4.engine.player.Player;

/**
 * Created by RT on 11/23/2017.
 */
public class Game {

    private final Player player1;
    private final Player player2;
    private final int turnLength;
    private int turnNum;
    private Board gameBoard;
    private Player playerWithMove;

    public Game(final Player player1, final Player player2, final int turnLength){
        this.player1 = player1;
        this.player2 = player2;
        this.turnLength = turnLength;
        this.turnNum = 0;
        gameBoard = Board.createStandardBoard();
        playerWithMove = this.player1;
    }

    public Player simulateGame(){
        int moveCounter = 0;

        while (true){
            Player currentPlayer = playerWithMove;
            Player waitingPlayer = null;

            if (currentPlayer == player1){
                waitingPlayer = player2;
            }
            else if(currentPlayer == player2){
                waitingPlayer = player1;
            }
            else{
                throw new RuntimeException("Current player did not set properly");
            }
            final int playersChosenColumn = currentPlayer.takeTurn(gameBoard, waitingPlayer, 10);
            int newPieceRow = gameBoard.setPiece(playersChosenColumn, playerWithMove.getAlliance());
            moveCounter++;

            // Check endgame conditions
            if(moveCounter >= BoardUtils.WIDTH_OF_BOARD * BoardUtils.HEIGHT_OF_BOARD){
                throw new RuntimeException("Board is full. No winner found");
            }
            turnNum++;
            printBoard();
            if(gameBoard.isGameOver(playersChosenColumn, newPieceRow)){
                return currentPlayer;
            }
            playerWithMove = waitingPlayer;
        }
    }
    private void printBoard(){
        for(int i = (BoardUtils.HEIGHT_OF_BOARD - 1); i >= 0; i--) {
            for (int j = 0; j < BoardUtils.WIDTH_OF_BOARD; j++) {
                    switch (gameBoard.getPieceAllianceAtCoords(j,i)){
                        case WHITE:
                            System.out.print(" O ");
                            continue;
                        case BLACK:
                            System.out.print(" X ");
                            continue;
                        case UNASSIGNED:
                                System.out.print(" - ");
                    }
            }
            System.out.println();
        }
        System.out.println("Position after " + turnNum + " moves");
        System.out.println();
    }
}
