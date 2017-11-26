package com;

import com.connect4.engine.Alliance;
import com.connect4.engine.player.Bootsy;
import com.connect4.engine.player.Player;
import com.connect4.engine.player.ScumBot;

public class main {
    public static void main(String[] args){
        System.out.println("Run sequence initialized");
        Player p1 = new ScumBot("Scumbot wins", Alliance.WHITE);
        Player p2 = new Bootsy("Bootsy wins", Alliance.BLACK);
        Game game = new Game(p1,p2, 10_000);
        System.out.println(game.simulateGame().getName());
    }
}
