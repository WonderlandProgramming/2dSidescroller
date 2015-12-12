package graphics.statemachine;

import java.awt.image.BufferedImage;

public class AnimationStep {
	private BufferedImage image;
	private float length;
	
	public AnimationStep(BufferedImage image, float length) {
		this.image = image;
		this.length = length;
	}
	
	public BufferedImage getImage() {
		return image;
	}
	public void setImage(BufferedImage image) {
		this.image = image;
	}
	public float getLength() {
		return length;
	}
	public void setLength(float length) {
		this.length = length;
	}
}
