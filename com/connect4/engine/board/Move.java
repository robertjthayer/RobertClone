package com.connect4.engine.board;

import com.connect4.engine.Alliance;
import com.connect4.engine.pieces.Piece;

/**
 * Created by RT on 12/26/2016.
 */
public abstract class Move {
    final Board board;
    final Alliance newPieceAlliance;
    final int destinationColumn;

    public Move(final Board board, Alliance newPieceAlliance, final int destinationColumn){
        this.board = board;
        this.newPieceAlliance = newPieceAlliance;
        this.destinationColumn = destinationColumn;
    }
}
