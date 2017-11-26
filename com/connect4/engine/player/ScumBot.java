package com.connect4.engine.player;

import java.util.concurrent.ThreadLocalRandom;
import com.connect4.engine.Alliance;
import com.connect4.engine.board.Board;
import com.connect4.engine.board.BoardUtils;

/**
 * Created by RT on 11/22/2017.
 */
public class ScumBot extends Player {
    public ScumBot(String name, Alliance alliance) {
        super(name, alliance);
    }

    @Override
    public int takeTurn(Board Connect4Board, Player opponent, int turnCallBack) {
        while(true) {
            int randomNum = ThreadLocalRandom.current().nextInt(0, BoardUtils.WIDTH_OF_BOARD - 1);
            if(!Connect4Board.getGameBoard().get(randomNum).isColumnFull()){
                return randomNum;
            }
        }
    }
}
