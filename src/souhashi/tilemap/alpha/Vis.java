package souhashi.tilemap.alpha;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import souhashi.tilemap.alpha.BSPGen.GameArea;

public class Vis extends Application {

	public static final int tilewidth = 16;
	public static final int tileheight = 16;
	public static final int screenwidth = 960;
	public static final int screenheight = 640;
	public GraphicsContext gc;
	AnimationTimer timer;
	BorderPane border = new BorderPane();
	Group root = new Group();
	Canvas canvas = new Canvas(screenwidth, screenheight);
	Random r = new Random();
	Generator generator = new Generator(5, 10, 50);
	boolean north, south, east, west, idle, collided;
	int velocity = 5;
	public static final int width = screenwidth / tilewidth;
	public static final int height = screenheight / tileheight;
	private static final String heroimgloc = "http://icons.iconarchive.com/icons/raindropmemory/legendora/64/Hero-icon.png";
	Tile[][] map = new Tile[width][height];
	final Image image = new Image("tileset.png");
	private Image heroImage;
	private ImageView hero;

	public void populatemap() {
		for (int i = 0; i < width; i++) {
			for (int j = 0; j < height; j++) {
				map[i][j] = new Tile(r.nextInt(3), i, j);
			}

		}

	}

	public void drawArea() {
		gc.clearRect(0, 0, screenwidth, screenheight);
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 0, screenwidth, screenheight);
	}
	
	

	public Color setColor(int id) {

		switch (id) {
		case 0:
			return Color.BLACK;
		case 1:
			return Color.SANDYBROWN;
		case 3:
			return Color.AQUA;
		default:
			return Color.DARKSEAGREEN;
		}
	}
	
	public void setImage(int id, int x, int y) {
		switch(id) {
		case 0:
			canvas.getGraphicsContext2D().drawImage(image, 1*tilewidth, 2*tileheight, tilewidth, tileheight, x, y, tilewidth, tileheight);
			break;
		case 1:
			canvas.getGraphicsContext2D().drawImage(image, 6*tilewidth, 2*tileheight, tilewidth, tileheight, x, y, tilewidth, tileheight);
			break;
		case 3:
			canvas.getGraphicsContext2D().drawImage(image, 8*tilewidth, 7*tileheight, tilewidth, tileheight, x, y, tilewidth, tileheight);
			break;
		default:
			canvas.getGraphicsContext2D().drawImage(image, 6*tilewidth, 1*tileheight, tilewidth, tileheight, x, y, tilewidth, tileheight);
			break;
			
		}
	}

	public void generaterooms() {
		generator.generate();
		for (Room c : generator.rooms) {
			carveRoom(c.gx, c.gy, c.gw, c.gh,0);
			System.out.println(c.gx + ", " + c.gy + ", " + c.gw + ", " + c.gh);
		}
		generator.generatecorridors();
		for (Room f : generator.corridors) {
			carveRoom(f.gx, f.gy, f.gw, f.gh,0);
		}
		
		for  (Room a : generator.rooms) {
			a.gen.generate(a.gx, a.gy, a.gw, a.gh);
		}
	}

	public void carveRoom(int x, int y, int w, int h, int id) {
		for (int i = x; i < x + w; i++) {
			for (int j = y; j < y + h; j++) {
				map[i][j].id = id;
			}
		}
	}

	public void showTiles(int x, int y, int w, int h) {
		for (int i = x; i < x+w; i++) {
			for (int j = y; j < y+h; j++) {
				setImage(map[i][j].id,map[i][j].x,map[i][j].y);
				//map[i][j].tile.setFill(setColor(map[i][j].id));
				// System.out.println(map[i][j].id);
				//map[i][j].tile.setStroke(Color.BLACK);
				//[i][j].tile.setStrokeWidth(1);
				//root.getChildren().add(map[i][j].tile);
			}
		}

	}
	
	public Tile getTile(double x, double y) {
		Tile container = null;
		for (int i = 0; i<width; i++) {
			for (int j =0; j< height; j++) {
				if (map[i][j].contains(x, y) == true) container = map[i][j];
			}
		}
		return container;
	}
	
	public void showPartitions() {
		for (Room b : generator.rooms) {
		for (GameArea a : b.gen.partitions) {
			Rectangle temp = new Rectangle(a.x* tilewidth,a.y*tileheight,a.width*tilewidth,a.height*tileheight);
			temp.setStroke(Color.WHITE);
			temp.setStrokeWidth(1);
			root.getChildren().add(temp);
		}
		}
	}
	
	public void showRooms() {
		for (Room b : generator.rooms) {
			for (GameArea a : b.gen.partitions) {
				carveRoom(a.room.gx,a.room.gy, a.room.gw, a.room.gh,3);
			}
			}
	}
	
	

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		border = new BorderPane();
		root = new Group();
		root.getChildren().add(canvas);
		gc = canvas.getGraphicsContext2D();
		heroImage = new Image (heroimgloc);
		hero = new ImageView(heroImage);
		hero.setFitWidth(tilewidth);
		hero.setFitHeight(tileheight);
		//drawArea();
		populatemap();
		generaterooms();
		// carveRoom(1,3, 5, 5);
		showRooms();
		
		placeHero();
		root.getChildren().add(hero);
		//showPartitions();
		WritableImage wim = new WritableImage(screenwidth, screenheight);
		root.snapshot(null, wim);
		border.setCenter(root);
		Scene scene = new Scene(border, screenwidth * 1.2, screenheight * 1.2);
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				switch(event.getCode()) {
				case UP: north = true; break;
				case DOWN: south = true; break;
				case LEFT: west = true; break;
				case RIGHT: east = true; break;
				default: idle = true; break;
				}
			}
			
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {
				// TODO Auto-generated method stub
				switch(event.getCode()) {
				case UP: north = false; break;
				case DOWN: south = false; break;
				case LEFT: west = false; break;
				case RIGHT: east = false; break;
				default: idle = false; break;
				}
			}
			
		});
		stage.setScene(scene);
		stage.show();
		final long startNanoTime = System.nanoTime();
		timer = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				// TODO Auto-generated method stub
				drawArea();
				showTiles(0,0,width,height);
				//showRooms();
				int dx=0;
				int dy= 0;
				double t = (now - startNanoTime) / 1000000000.0; 
				
				if (north) {dy -=1; }
				if (south) {dy+= 1;}
				if (west) { dx -=1;}
				if (east) { dx +=1;}
				if (idle) {dx *=3; dy *= 3;}
				
				moveHeroBy(dx,dy);
			}
			
		};
		timer.start();
		File file = new File("GenTileMap8.png");

		try {
			ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
		} catch (Exception s) {
			System.out.println("We dun goofed...");
		}
	}
	
	private void moveHeroBy (int dx, int dy) {
		if (dx == 0 && dy == 0) return;
		final double cx = hero.getBoundsInLocal().getWidth() /2;
		final double cy = hero.getBoundsInLocal().getHeight()/2;
		double x = cx+ hero.getLayoutX() + dx;
		double y = cy + hero.getLayoutY() + dy;
		//if (getTile(x,y).id == 0) {
		moveHeroTo(x,y);
		//}
		
	}
	
	private void moveHeroTo(double x, double y) {
		collided = false;
		final double cx = hero.getBoundsInLocal().getWidth() /2;
		final double cy = hero.getBoundsInLocal().getHeight()/2;
		
		if (x - cx >= 0 && x+cx <= screenwidth && y-cy >= 0 && y+cy <= screenheight && getTile(x-cx,y-cy).id == 0&& getTile(x+cx,y+cy).id == 0  ) {
			hero.relocate(x-cx, y-cy);
		}
		
		
		//System.out.println(getTile(x-cx, y-cy));
		System.out.println(collided);
	}
	public void placeHero() {
		Room temp = generator.rooms.get(0);
		moveHeroTo(temp.px+(temp.pw/2), temp.py+(temp.ph/2));
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Application.launch(args);
	}

}
