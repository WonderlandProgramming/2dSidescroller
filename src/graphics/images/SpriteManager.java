package graphics.images;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class SpriteManager {

	private HashMap<String, SpriteSheet> spriteSheets;

	public SpriteManager() {
		spriteSheets = new HashMap<>();
	}

	public void addSpriteSheet(String uniqueID, String path) {
		try {
			spriteSheets.put(uniqueID, new SpriteSheet(path));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void removeSpriteSheet(int uniqueID) {
		spriteSheets.remove(uniqueID);
	}

	public BufferedImage getImage(String spriteSheet, String imageName) {
		if (spriteSheets.containsKey(spriteSheet)) {
			return spriteSheets.get(spriteSheet).getImageset().get(imageName);
		}
		return null;
	}

	public HashMap<String, SpriteSheet> getSpriteSheets() {
		return spriteSheets;
	}
}
