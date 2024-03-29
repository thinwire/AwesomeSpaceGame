package spacegame;
import java.awt.Font;
import java.awt.event.KeyEvent;

import engine.core.Application;
import engine.graphics.Image;
import engine.graphics.Sprite;
import engine.graphics.Text;
import spacegame.game.BulletSystem;
import spacegame.game.EnemySystem;
import spacegame.game.ExplosionSystem;
import spacegame.game.PlayerShip;
import spacegame.game.Starfield;

public class SpaceGame extends Application {

	private Font scoreFont;
	private Text scoreDisplay;

	private Starfield starfield;

	private BulletSystem playerBullets;
	private PlayerShip player;

	private EnemySystem enemies;
	private ExplosionSystem explosions;

	private int score = 0;

	SpaceGame() {
		super(800, 800);
		screen.setTitle("Awesome space game");

		// Load up our score font
		Text.loadFont("gfx/arcade_i.ttf");
		scoreFont = Text.createFont("Arcade Interlaced", 0, 24);

		// Add a background sprite
		Sprite bg = new Sprite(new Image("gfx/space.png"));
		bg.setPosition(400, 400);
		addDrawable(bg);

		// Dynamic starfield
		starfield = new Starfield(96);
		addDrawable(starfield);

		// Create an explosion system here
		explosions = new ExplosionSystem();

		// The bullets the player can shoot
		playerBullets = new BulletSystem("gfx/laser.png");
		addDrawable(playerBullets);

		// Enemies
		enemies = new EnemySystem("gfx/enemy.png", playerBullets, explosions);
		addDrawable(enemies);

		// The player itself
		player = new PlayerShip(playerBullets);
		addDrawable(player);

		// Add explosions on top of everything
		addDrawable(explosions);

		// Score display
		scoreDisplay = new Text(scoreFont);
		scoreDisplay.setPosition(25, 45);
		scoreDisplay.setText("Score: 0");
		addDrawable(scoreDisplay);
		
		// Set up keyboard controls
		// You can modify these to whatever you want
		input.clearBindings();

		input.bind("LEFT",  KeyEvent.VK_LEFT);
		input.bind("RIGHT", KeyEvent.VK_RIGHT);
		input.bind("UP",    KeyEvent.VK_UP);
		input.bind("DOWN",  KeyEvent.VK_DOWN);
		input.bind("FIRE",  KeyEvent.VK_SPACE);
		input.bind("EXIT",  KeyEvent.VK_ESCAPE);
	}

	@Override
	public void update(double delta) {

		// Update starfield
		starfield.update(delta);

		// Update enemies
		enemies.update(delta);

		// Update player (movement, shooting, etc)
		player.update(input, delta);

		// Update player bullets to move them up the screen
		playerBullets.update(delta);

		// Update explosions
		explosions.update(delta);

		// Update score
		int newscore = enemies.getKillCount() * 125;
		if (score != newscore) {
			score = newscore;
			scoreDisplay.setText("Score: " + score);
		}

		// See if we want to quit
		if(input.isPressed("EXIT")) {
		    System.out.println("Exit pressed");
		    exit();
		}
		
	}

	//////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////
	//////////////////////////////////////////////////////////////////////////

	// Main entry point - we create an instance of our game here and start it
	public static void main(String[] args) {
		System.out.println("Game init");

		new SpaceGame().run(); // This will run until the game wants to exit

		System.out.println("Game shutdown");

		// Force exit of the application
		System.exit(0);
	}

}
