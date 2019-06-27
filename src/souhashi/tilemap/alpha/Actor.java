package souhashi.tilemap.alpha;

public class Actor extends Tile{

	int px, py, width, height, centrex, centrey,vel;
	final int left = 1;
	final int right = 2;
	final int up = 3;
	final int down = 4;
	final int stop = 0;
	public Actor(int id1, int xc, int yc, int px, int py, int width, int height) {
		super(id1, xc, yc);
		// TODO Auto-generated constructor stub
		this.px = px;
		this.py = py;
		this.width = width;
		this.height = height;
		centrex = px + (width/2);
		centrey = py+(height/2);
		vel = 10;
	}
	
	public void MoveActor(int state) {
		switch (state) {
		case left:
			px = px - vel;
			break;
		case right:
			px = px + vel;
			break;
		case up:
			py = py - vel;
			break;
		case down:
			py = py + vel;
			break;
		case stop:
			vel = 0;
			break;
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
