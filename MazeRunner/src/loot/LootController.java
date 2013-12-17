package loot;

import java.util.ArrayList;
import java.util.Iterator;

import mazerunner.Maze;
import mazerunner.Player;
import mazerunner.VisibleObject;

public class LootController implements VisibleObject {
	
	// private Player player;
	
	private ArrayList<Loot> lootList;
	// private Loot currentLoot;
	
	
	/**
	 * Create a new lootcontroller with an associated player
	 * @param player
	 */
	public LootController(Player player){
		// this.player = player;
		
		lootList = new ArrayList<Loot>();
		lootList.add(new Food(2 * Maze.SQUARE_SIZE + Maze.SQUARE_SIZE / 2,Maze.SQUARE_SIZE / 2,4 * Maze.SQUARE_SIZE + Maze.SQUARE_SIZE / 2,50,true,null));
		lootList.add(new Food(8 * Maze.SQUARE_SIZE + Maze.SQUARE_SIZE / 2,Maze.SQUARE_SIZE / 2,4 * Maze.SQUARE_SIZE + Maze.SQUARE_SIZE / 2,50,true,null));
	}
	
	/**
	 * The loot display function
	 */
	public void display(){
		for(Iterator<Loot> it = lootList.iterator();it.hasNext();)
			it.next().display();
	}

	/**
	 * update
	 */
/*	public void update() {
		Iterator<Loot> lootIterator = lootList.listIterator();
		
		while(lootIterator.hasNext()){
			currentLoot = lootIterator.next();
			
			if(currentLoot.near(player.getLocationX(),player.getLocationY(),player.getLocationZ())){
				if(currentLoot instanceof Food){
					player.addHP(50);
					lootIterator.remove();}}}
	}
*/	
	/**
	 * get the loot list
	 */
	public ArrayList<Loot> getList(){
		return lootList;
	}
}
