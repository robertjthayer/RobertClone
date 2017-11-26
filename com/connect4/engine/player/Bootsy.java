package com.connect4.engine.player;

import com.connect4.engine.Alliance;
import com.connect4.engine.board.Board;
import com.connect4.engine.board.BoardUtils;
import com.connect4.engine.board.Space;
import javafx.util.Pair;

import java.util.LinkedList;
import java.util.List;

public class Bootsy extends Player {
    private final int FIXED_MAX_DEPTH = 5;
    private static final int[][] INHERENT_VALUES = initializeInherentValues();
    private static final int POSITION_BASE_WEIGHT_FACTOR = 100;
    private static final double POSITION_CENTRAL_WEIGHT_FACTOR = 20;

    public Bootsy(String name, Alliance alliance) {
        super(name, alliance);
    }

    @Override
    public int takeTurn(Board Connect4Board, Player opponent, int turnCallBack) {
        if(Connect4Board.movesMade <= 3){
            return getMaxAvailableInherentPositionValue(Connect4Board);
        }
        return negaMax(Connect4Board, FIXED_MAX_DEPTH,0, alliance).getValue();
    }

    private Pair<Integer,Integer> negaMax(final Board board, final int maxDepth, final int currentDepth, Alliance colorToMove){
        //Base Case
        if(currentDepth == maxDepth || board.isGameOver(board.newPieceColumn, board.newPieceRow)){
            //printBoard(board,currentDepth);
            return new Pair<>(staticEvaluation(board), -1);
        }
        int bestMove = -1;
        int bestScore = Integer.MIN_VALUE;
        for(int i = 0; i < BoardUtils.WIDTH_OF_BOARD; i++){
            if(board.getGameBoard().get(i).isColumnFull()){
                continue;
            }
            Board newBoard = new Board(board);
            newBoard.setPiece(i,colorToMove);
            int recursedScore = 0;
            int currentMove = 0;
            Pair<Integer, Integer> recursedScoreAndCurrMove;
            recursedScoreAndCurrMove = negaMax(newBoard, FIXED_MAX_DEPTH, currentDepth + 1, switchAlliance(colorToMove));
            int currentScore = -(recursedScoreAndCurrMove.getKey());
            if(currentScore > bestScore){
                bestScore = currentScore;
                bestMove = i;
            }
        }
        return new Pair<>(bestScore,bestMove);
    }

    private int staticEvaluation(final Board board){
        int[] OpenSequences = new int[BoardUtils.WINNING_SEQUENCE_LENGTH];
        for(Space currentSpace : availableSpaces(board)){

            //Check for available vertical sequence
            for (int i = 1; i <= BoardUtils.WINNING_SEQUENCE_LENGTH; i++) {
                if (i == BoardUtils.WINNING_SEQUENCE_LENGTH){
                    return true;
                }
                if (currentAlliance == getPieceAllianceAtCoords(newPieceColumn, newPieceRow - i)){
                    continue;
                }
                else {
                    break;
                }
            }
            int inARow = 1; // counts new piece
            boolean leftContinue = true;
            boolean rightContinue = true;

            // Check for win by horizontal sequence
            for (int i = 1; i <= BoardUtils.WINNING_SEQUENCE_LENGTH; i++) {
                if((newPieceColumn - i) >= 0 &&
                        currentAlliance == getPieceAllianceAtCoords(newPieceColumn - i, newPieceRow) &&
                        leftContinue == true){
                    inARow++;
                }
                else{
                    leftContinue = false;
                }
                if((newPieceColumn + i) < BoardUtils.WIDTH_OF_BOARD &&
                        currentAlliance == getPieceAllianceAtCoords(newPieceColumn + i, newPieceRow) &&
                        rightContinue == true){
                    inARow++;
                }
                else{
                    rightContinue = false;
                }
                if(inARow >= BoardUtils.WINNING_SEQUENCE_LENGTH){
                    return true;
                }
                if(leftContinue == false && rightContinue == false){
                    break;
                }
            }
            inARow = 1;
            rightContinue = true;
            leftContinue = true;
            // Check for win by '/' diagonal sequence
            for (int i = 1; i <= BoardUtils.WINNING_SEQUENCE_LENGTH; i++) {
                if((newPieceColumn - i) >= 0 &&
                        (newPieceRow - i) >= 0 &&
                        currentAlliance == getPieceAllianceAtCoords(newPieceColumn - i, newPieceRow - i) &&
                        leftContinue == true){
                    inARow++;
                }
                else{
                    leftContinue = false;
                }
                if((newPieceColumn + i) < BoardUtils.WIDTH_OF_BOARD &&
                        (newPieceRow + i) < BoardUtils.HEIGHT_OF_BOARD &&
                        currentAlliance == getPieceAllianceAtCoords(newPieceColumn + i, newPieceRow + i) &&
                        rightContinue == true){
                    inARow++;
                }
                else{
                    rightContinue = false;
                }
                if(inARow >= BoardUtils.WINNING_SEQUENCE_LENGTH){
                    return true;
                }
                if(leftContinue == false && rightContinue == false){
                    break;
                }
            }
            inARow = 1;
            rightContinue = true;
            leftContinue = true;
            // Check for win by '\' diagonal sequence
            for (int i = 1; i <= BoardUtils.WINNING_SEQUENCE_LENGTH; i++) {
                if ((newPieceColumn - i) >= 0 &&
                        (newPieceRow + i) < BoardUtils.HEIGHT_OF_BOARD &&
                        currentAlliance == getPieceAllianceAtCoords(newPieceColumn - i, newPieceRow + i) &&
                        leftContinue == true) {
                    inARow++;
                } else {
                    leftContinue = false;
                }
                if ((newPieceColumn + i) < BoardUtils.WIDTH_OF_BOARD &&
                        (newPieceRow - i) >= 0 &&
                        currentAlliance == getPieceAllianceAtCoords(newPieceColumn + i, newPieceRow - i) &&
                        rightContinue == true) {
                    inARow++;
                } else {
                    rightContinue = false;
                }
                if (inARow >= BoardUtils.WINNING_SEQUENCE_LENGTH) {
                    return true;
                }
                if (leftContinue == false && rightContinue == false) {
                    break;
                }
            }
        }
        return 1;
    }

    private static int[][] initializeInherentValues(){
        int[][] values = new int[BoardUtils.WIDTH_OF_BOARD][BoardUtils.HEIGHT_OF_BOARD];
        for (int currRow = 0; currRow < BoardUtils.HEIGHT_OF_BOARD; currRow++)
        {
            for(int currCol = 0; currCol < BoardUtils.WIDTH_OF_BOARD; currCol++){
                values[currCol][currRow] = quadraticPositionAnalysis(currCol, currRow);
                System.out.print(" " + values[currCol][currRow] + " ");
            }
            System.out.println();
        }
        return values;
    }

    private int getInherentPositionValue(int column, int row){
        return INHERENT_VALUES[column][row];
    }

    private int getMaxAvailableInherentPositionValue(Board board){
        int max = Integer.MIN_VALUE;
        int columnWithMax = -1;
        for(Space current : availableSpaces(board)){
            int curr = getInherentPositionValue(current.getColumn(),current.getRow());
            if(curr > max){
                max = curr;
                columnWithMax = current.getColumn();
            }
        }
        return columnWithMax;
    }

    private static int quadraticPositionAnalysis(int col, int row){
        double rowAnalysis =  (int)((-row * row)/((BoardUtils.HEIGHT_OF_BOARD-1) / POSITION_CENTRAL_WEIGHT_FACTOR) +
                (POSITION_CENTRAL_WEIGHT_FACTOR * row)) + POSITION_BASE_WEIGHT_FACTOR;
        double colAnalysis =((-col * col)/((BoardUtils.WIDTH_OF_BOARD-1) / POSITION_CENTRAL_WEIGHT_FACTOR) +
                (POSITION_CENTRAL_WEIGHT_FACTOR * col)) + POSITION_BASE_WEIGHT_FACTOR;
        return (int)((rowAnalysis * colAnalysis) / POSITION_BASE_WEIGHT_FACTOR);
    }

    private Alliance switchAlliance(Alliance current){
        return current == Alliance.WHITE ? Alliance.BLACK : Alliance.WHITE;
    }

    private void printBoard(Board gameBoard, int turnNum){
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
        System.out.println("Position analyzed after " + turnNum + " moves");
        System.out.println();
    }

    private List<Space> availableSpaces(Board board){
        List<Space> availableSpaces = new LinkedList<>();
        for(int i = 0; i < BoardUtils.WIDTH_OF_BOARD; i++){
            availableSpaces.add(board.getGameBoard().get(i).getNextAvailableSpace());
        }
        return availableSpaces;
    }
}
