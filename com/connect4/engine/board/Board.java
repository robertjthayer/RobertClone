package com.connect4.engine.board;

import com.connect4.engine.Alliance;
import com.connect4.engine.board.Space.OccupiedSpace;
import com.connect4.engine.pieces.Piece;
import com.connect4.engine.player.Player;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public final class Board {

    private ImmutableList<Column> gameBoard;
    private final Collection<Piece> whitePieces;
    private final Collection<Piece> blackPieces;
    public static int movesMade;
    public int newPieceRow;
    public int newPieceColumn;
    private static final Board EMPTY_BOARD = createStandardBoardImpl();

    private Board(){
        List<Column> emptyColumns = new LinkedList<>();
        for (int i = 0; i < BoardUtils.WIDTH_OF_BOARD; i++) {
            emptyColumns.add(new Column(i,this));
        }
        this.gameBoard = ImmutableList.copyOf(emptyColumns);
        this.whitePieces = new HashSet<>();
        this.blackPieces = new HashSet<>();
        this.movesMade = 0;
    }

    public Board(Board board) {
        this.gameBoard = board.gameBoard;
        this.whitePieces = board.getWhitePieces();
        this.blackPieces = board.getBlackPieces();
        this.newPieceColumn = board.newPieceColumn;
        this.newPieceRow = board.newPieceRow;
        this.movesMade = whitePieces.size() + blackPieces.size();
    }

    public Collection<Piece> getBlackPieces() {
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces() {
        return this.whitePieces;
    }

    public void addToPieceCollection(Piece piece) {
        if(piece.getPieceAlliance() == Alliance.WHITE){
            whitePieces.add(piece);
        }
        else if(piece.getPieceAlliance() == Alliance.BLACK) {
            blackPieces.add(piece);
        }
        else{
            throw new RuntimeException("No Alliance");
        }
    }

    public ImmutableList<Column> getGameBoard() {
        return this.gameBoard;
    }

    public static Board createStandardBoard() {
        return EMPTY_BOARD;
    }

    private static Board createStandardBoardImpl() {
        return new Board();
    }

    public int setPiece(final int col, Alliance alliance) {
        Column currentColumn = this.gameBoard.get(col);

        List<Space> copyOfColumn = new LinkedList<>();
        int i = 0;
        for (; i < BoardUtils.HEIGHT_OF_BOARD; i++) {
            if (currentColumn.isSpaceOccupied(i)) {
                copyOfColumn.add(currentColumn.getPiecesinColumn().get(i));
            } else {
                break;
            }
        }
        copyOfColumn.add(new OccupiedSpace(col, i, alliance));
        addToPieceCollection(new Piece(col, i, alliance));
        int rowOfNewPiece = i;
        i++;

        for (; i < BoardUtils.HEIGHT_OF_BOARD; i++) {
            copyOfColumn.add(currentColumn.getPiecesinColumn().get(i));
        }
        List<Column> newGameBoard =  new LinkedList<>();

        for (int j = 0; j < BoardUtils.WIDTH_OF_BOARD; j++) {
            if (j != col) {
                newGameBoard.add(gameBoard.get(j));
            } else {
                newGameBoard.add(Column.listToColumn(col, copyOfColumn, this));
            }
        }
        this.gameBoard = ImmutableList.copyOf(newGameBoard);
        this.newPieceRow = rowOfNewPiece;
        this.newPieceColumn = col;
        this.movesMade++;
        return rowOfNewPiece;
    }

    public Alliance getPieceAllianceAtCoords(int column, int row){
        if(gameBoard.get(column).getPiecesinColumn().get(row).getPieceAlliance() == null){
            return Alliance.UNASSIGNED;
        }
        return gameBoard.get(column).getPiecesinColumn().get(row).getPieceAlliance();
    }

    public Piece getPieceAtCoords(int column, int row){
        return gameBoard.get(column).getPiecesinColumn().get(row).getPieceInSpace();
    }

    public boolean isGameOver(final int newPieceColumn,final int newPieceRow){
        Alliance currentAlliance = getPieceAllianceAtCoords(newPieceColumn, newPieceRow);

        //Check for win by vertical sequence
        if(newPieceRow >= BoardUtils.WINNING_SEQUENCE_LENGTH - 1) {
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
        return false;
    }
}