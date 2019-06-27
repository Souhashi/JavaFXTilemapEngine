package souhashi.tilemap.alpha;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;

import javafx.geometry.Point2D;
import javafx.scene.shape.Rectangle;

//https://gamedevelopment.tutsplus.com/tutorials/how-to-use-bsp-trees-to-generate-game-maps--gamedev-12268
public class BSPGen {
	public static final int minsize = 3;
	public static final int maxleafsize = 5;
	ArrayList<GameArea> partitions;
	

	// generate a 2D array of nxn or nxm -> Game Area
	public class GameArea {

		int x;
		int y;
		int height;
		int width;
		Random ran;
		GameArea left;
		GameArea right;
		Room room;

		public GameArea(int xc, int yc, int w, int h) {
			this.x = xc;
			this.y = yc;
			this.height = h;
			this.width = w;
			ran = new Random();
			left = null;
			right = null;
			room= new Room (1,1,1,1);
		}

		public int anyRandomIntRange(Random random, int low, int high) {
			int randomInt = random.nextInt(high + 1 - low) + low;
			return randomInt;
		}

		public boolean split() {
			int max = 0;

			if (left != null || right != null) {
				return false;
			}
			boolean splith = ran.nextDouble() > 0.5;

			if (width > height && width / height >= 1.25) {
				splith = false;
			} else if (height > width && height / width >= 1.25) {
				splith = true;
			}
			// System.out.println(max);
			if (splith == true) {
				max = height - minsize;
				System.out.println(max);
				if (max <= minsize) {
					return false;
				}
			} else {
				max = width - minsize;
				// System.out.println(max);
				if (max <= minsize) {
					return false;
				}
			}
			int split = anyRandomIntRange(ran, minsize, max);

			if (splith == true) {
				left = new GameArea(x, y, width, split);
				right = new GameArea(x, y + split, width, height - split);
			} else {
				left = new GameArea(x, y, split, height);
				right = new GameArea(x + split, y, width - split, height);
			}
			return true;
		}

		public void createRooms() {

			if (left != null || right != null) {
				if (left != null) {
					left.createRooms();
				}
				if (right != null) {
					right.createRooms();
				}
				
			} else {

				Point2D roomsize;
				Point2D roompos;
				//roomsize = new Point2D(anyRandomIntRange(ran, 0, width), anyRandomIntRange(ran, 0, height));
				roompos = new Point2D(anyRandomIntRange(ran, 0, (int) (width - 1)),
						anyRandomIntRange(ran, 0, (int) (height - 1)));
				room = new Room((int)(x + roompos.getX()), (int)(y + roompos.getY()), 1, 1);
			}
		}
		
		
		
		

	}

	public void generate(int x, int y, int w, int h) {
		partitions = new ArrayList<GameArea>();
	GameArea root = new GameArea(x, y, w, h);
		partitions.add(root);
		boolean didsplit = true;
		while (didsplit) {
			didsplit = false;
			for (ListIterator<GameArea> it = partitions.listIterator(); it.hasNext();) {
				GameArea a = it.next();
				if (a.left == null && a.right == null) {
					if (a.width > maxleafsize || a.height > maxleafsize) {
						if (a.split()) {
							it.add(a.left);
							it.add(a.right);
							didsplit = true;
						}
					}
				}
			}
		}
		root.createRooms();
		System.out.println("Generation successful: " + partitions.size());
		for (GameArea b : partitions) {
			System.out.println(b.x + ", " + b.y + ", " + b.width + ", " + b.height);
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
