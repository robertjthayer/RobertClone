package com.connect4.engine.board;

public class BoardUtils {

    public static final int WINNING_SEQUENCE_LENGTH = 4;
    public static final int HEIGHT_OF_BOARD = 6;
    public static final int WIDTH_OF_BOARD = 7;

    private BoardUtils() {
        throw new RuntimeException("You cannot instantiate BoardUtils");
    }

    public static boolean isValidBoardCoordinate(final int column, final int row){
        return column >= 0 && column < WIDTH_OF_BOARD && row >= 0 && row < HEIGHT_OF_BOARD;
    }
}
