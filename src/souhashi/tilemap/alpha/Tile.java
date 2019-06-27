package souhashi.tilemap.alpha;

import javafx.scene.shape.Rectangle;

public class Tile {

	public int x,y,id;
	Rectangle tile;
	
	public Tile(int id1, int xc, int yc) {
		id = id1;
		x = xc * Vis.tilewidth;
		y = yc * Vis.tileheight;
		tile = new Rectangle(x,y,Vis.tilewidth,Vis.tileheight);
	}
	
	public boolean contains(double x, double y) {
		
		return (x >= this.x && y>= this.y && x <= this.x + Vis.tilewidth && y <= this.y +Vis.tileheight);
	}
	public static void main(String[] args) {
		Tile test = new Tile (0, 0,0);
		System.out.println(test.contains(3, 17));
	}
}
