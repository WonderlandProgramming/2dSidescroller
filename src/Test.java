import java.awt.Canvas;
import java.awt.Graphics;
import java.io.IOException;

import javax.swing.JFrame;

import graphics.images.SpriteManager;
import graphics.statemachine.StateMachine;
import graphics.statemachine.Uniform;

public class Test {

	public static void main(String args[]) throws IOException {
		SpriteManager sm = new SpriteManager();
		sm.addSpriteSheet("player", "player.json");

		StateMachine machine = new StateMachine(sm, "animation.script",
				new Uniform("spritesheet", "player"));
		machine.setActiveAnimation("walk_up");

		Canvas c = new Canvas();
		c.setVisible(true);
		
		JFrame f = new JFrame("TEST");
		f.setSize(500, 500);
		f.add(c);
		f.setVisible(true);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		while (true){
			machine.update();
			Graphics g = c.getGraphics();
			g.clearRect(0, 0, 1000, 1000);
			machine.render(c.getGraphics(), 100, 100);
		}
	}

}
