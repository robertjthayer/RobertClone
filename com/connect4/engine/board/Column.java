package com.connect4.engine.board;

import com.connect4.engine.board.Space.EmptySpace;
import com.google.common.collect.ImmutableList;

import java.util.LinkedList;
import java.util.List;

public class Column {

    private final int columnNumber;
    private final ImmutableList<Space> piecesInColumn;
    private Space nextAvailableSpace;

    public Column(final int columnNumber, Board board){

        this.columnNumber = columnNumber;
        List<Space> pieces = new LinkedList<>();
        for(int i = 0; i < BoardUtils.HEIGHT_OF_BOARD; i++){
            pieces.add(new EmptySpace(columnNumber, i));
        }
        piecesInColumn = ImmutableList.copyOf(pieces);
        this.nextAvailableSpace = piecesInColumn.get(0);
    }

    private Column(final int columnNumber, List<Space> spaces, Board board){
        this.columnNumber = columnNumber;
        this.piecesInColumn = ImmutableList.copyOf(spaces);
        int rowCount = 0;
        for (Space emptySpaces : spaces){
            if(!emptySpaces.isTileOccupied()){
                this.nextAvailableSpace = emptySpaces;
                return;
            }
            else{
                rowCount++;
            }
            if(rowCount == BoardUtils.HEIGHT_OF_BOARD){
                nextAvailableSpace = null;
                return;
            }
        }
    }

    public ImmutableList<Space>getPiecesinColumn(){
        return piecesInColumn;
    }


    public Space getSpaceFromColumn(final int row){
            return piecesInColumn.get(row);
    }

    public boolean isSpaceOccupied(final int row){
        return piecesInColumn.get(row).isTileOccupied();
    }

    public Space getNextAvailableSpace(){
        return nextAvailableSpace;
    }

    public boolean isColumnFull(){
        return piecesInColumn.get((BoardUtils.HEIGHT_OF_BOARD)-1).isTileOccupied();
    }

    public static Column listToColumn(int columnNumber, List<Space> spaces, Board board){
        return new Column(columnNumber,spaces,board);
    }
}
