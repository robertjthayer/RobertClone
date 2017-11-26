package com.connect4.engine.board;

import com.connect4.engine.Alliance;
import com.connect4.engine.pieces.Piece;
import com.google.common.collect.ImmutableMap;

import java.util.HashMap;
import java.util.Map;

public abstract class Space {

    protected final int row;
    protected final int column;

    private Space(final int column, final int row){
        this.row = row;
        this.column = column;
    }

    public int getColumn(){return column;}
    public int getRow(){return row;}
    public abstract boolean isTileOccupied();
    public abstract Piece getPieceInSpace();
    public abstract Alliance getPieceAlliance();

    public static final class EmptySpace extends Space {
        public EmptySpace(final int column, final int row){
            super(column, row);
        }

        @Override
        public boolean isTileOccupied(){
            return false;
        }

        @Override
        public Piece getPieceInSpace() {
            return null;
        }

        @Override
        public Alliance getPieceAlliance(){
            return null;
        }
    }
    public static final class OccupiedSpace extends Space {
        private final Piece pieceInSpace;

        public OccupiedSpace(int column, int row, Alliance pieceOnTile){
            super(column, row);
            this.pieceInSpace = new Piece(column, row, pieceOnTile);
        }

        @Override
        public Piece getPieceInSpace(){
            return this.pieceInSpace;
        }
        @Override
        public boolean isTileOccupied(){
            return true;
        }

        @Override
        public Alliance getPieceAlliance(){
            return this.pieceInSpace.getPieceAlliance();
        }
    }

}
