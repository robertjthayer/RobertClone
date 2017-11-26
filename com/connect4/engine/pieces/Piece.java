package com.connect4.engine.pieces;

import com.connect4.engine.Alliance;
import com.connect4.engine.board.Board;
import com.connect4.engine.board.Move;

import java.util.Collection;

public class Piece {

    protected final int pieceColumn;
    protected final int pieceRow;
    protected final Alliance pieceAlliance;

    public Piece(final int pieceColumn, final int pieceRow, final Alliance pieceAlliance){
        this.pieceColumn = pieceColumn;
        this.pieceRow = pieceRow;
        this.pieceAlliance = pieceAlliance;
    }

    public Alliance getPieceAlliance(){
        return this.pieceAlliance;
    }
}
