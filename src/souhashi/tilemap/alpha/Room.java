package souhashi.tilemap.alpha;

import javafx.geometry.Point2D;

public class Room {
	
	int gx, gy, gx1, gy1, gw,gh;
	int px,py, pw,ph;
	Point2D center;
	BSPGen gen;
	public Room(int x, int y, int w, int h) {
		gx = x;
		gy = y;
		gw = w;
		gh = h;
		gx1 = x + w;
		gy1 = y + h;
		px = x * Vis.tilewidth;
		py = y * Vis.tileheight;
		pw = w * Vis.tilewidth;
		ph = h * Vis.tileheight;
		gen = new BSPGen();
		center = new Point2D((gx+gx1) /2, (gy + gy1) /2);
	}
	
	public boolean intersects(Room room) {
		
		return (gx <= room.gx1 && gx1 >= room.gx && gy <= room.gy1 && room.gy1 >= room.gy);
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
