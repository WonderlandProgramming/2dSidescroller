package graphics.statemachine;

import java.awt.image.BufferedImage;

public class Animation {

	private long lastTime;
	private AnimationStep[] steps;
	private int[] order;
	private int index = 0;

	public Animation(int[] order, AnimationStep[] steps) {
		lastTime = System.nanoTime();
		this.order = order;
		this.steps = steps;
	}

	public void update() {
		long thisTime = System.nanoTime();
		float diffInSec = (float) ((thisTime - lastTime) / 1e+9);
		if (steps[order[index]].getLength() <= diffInSec) {
			index = (index + 1 >= order.length) ? 0 : index + 1;
			lastTime = thisTime;
		}
	}

	public AnimationStep getCurrentStep() {
		return steps[order[index]];
	}

	public BufferedImage getCurrentImage() {
		return steps[order[index]].getImage();
	}
}
