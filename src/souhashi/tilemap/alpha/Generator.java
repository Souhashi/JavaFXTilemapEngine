package souhashi.tilemap.alpha;

import java.util.ArrayList;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;
import javafx.embed.swing.SwingFXUtils;
import javax.imageio.ImageIO;

public class Generator {

	public ArrayList<Room> rooms;
	public ArrayList<Room> corridors;
	int min, max, maxrooms;
	Random random;
	
	public Generator(int min, int max, int maxr) {
		this.min = min;
		this.max = max;
		maxrooms = maxr;
		rooms = new ArrayList<Room>();
		corridors = new ArrayList<Room>();
		random = new Random();
	}
	
	public void generate() {
		for (int i=0; i< maxrooms; i++) {
			
			int w = min + random.nextInt(max - min+1);
			int h = min + random.nextInt(max - min+1);
			int x = random.nextInt(Vis.width -w-1)+1;
			int y = random.nextInt(Vis.height-h-1)+1;
			
			Room newRoom = new Room (x,y,w,h);
			boolean failed = false;
			for (Room r : rooms) {
				if (newRoom.intersects(r)) {
					failed = true;
					break;
				}
			}
			if (!failed) {
				rooms.add(newRoom);
			}
		}
		System.out.println(rooms.size());
	}
	
	public void generatecorridors() {
		int h,w,px,nx,py,ny;
		int wh = 2;
		for (int j =0; j< rooms.size(); j++) {
			if (j>=1) {
			px = (int) Math.round(rooms.get(j-1).center.getX());
			py = (int) Math.round(rooms.get(j-1).center.getY());
			nx = (int) Math.round(rooms.get(j).center.getX());
			ny = (int) Math.round(rooms.get(j).center.getY());
			
			w = nx - px;
			h = ny - py;
			if (w < 0 && h > 0) {
				corridors.add(new Room(nx, ny- Math.abs(h),wh , Math.abs(h)));
				corridors.add(new Room(nx, py, Math.abs(w), wh));
			}
			if (w < 0 && h < 0) {
				corridors.add(new Room(nx, py- Math.abs(h), wh, Math.abs(h)));
				corridors.add(new Room(nx, py, Math.abs(w), wh));
			}
			if (w > 0 && h > 0) {
				corridors.add(new Room(px, ny- Math.abs(h), wh, Math.abs(h)));
				corridors.add(new Room(px, ny, Math.abs(w), wh));
			}
			if (w > 0 && h < 0) {
				corridors.add(new Room(px, py- Math.abs(h), wh, Math.abs(h)));
				corridors.add(new Room(px, ny, Math.abs(w), wh));
			}
			}
			
				
		}
		System.out.println(corridors.size());
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Generator test = new Generator(6,20,5);
		test.generate();
		test.generatecorridors();
	}

}
