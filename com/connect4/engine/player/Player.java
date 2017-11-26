package com.connect4.engine.player;

import com.connect4.engine.Alliance;
import com.connect4.engine.board.Board;

/**
 * Created by RT on 11/22/2017.
 */
public abstract class Player {
    protected String name;
    protected Alliance alliance;

    public Player(String name, Alliance alliance){
        this.name = name;
        this.alliance = alliance;
    }

    public abstract int takeTurn(Board Connect4Board, Player opponent, int turnCallBack);

    public Alliance getAlliance(){
        return this.alliance;
    }

    public String getName() {
        return this.name;
    }
}
