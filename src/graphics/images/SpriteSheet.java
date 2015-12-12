package graphics.images;

import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import parser.file.FreeFormatFileReader;

/**
 * Creates a Spritesheet from the JsonFile (List) of TexturePacker.
 * 
 * @author Lukas Peer
 * @since 11.12.2015
 * @version 0.1_u
 */
class SpriteSheet {
	/**
	 * The HashMap of all {@link BufferedImage} that are created when creating a
	 * new Instance of {@link SpriteSheet}.
	 */
	private HashMap<String, BufferedImage> imageset;

	/**
	 * The main Constructor that will create a {@link SpriteSheet} from a
	 * TexturePacker JsonFile (List). It will automaticly crop and rotate all
	 * images in the Sheet.
	 * 
	 * @param jsonFilePath
	 *            The Path to the JsonFile from TexturePacker as Json List
	 *            format.
	 * @throws FileNotFoundException
	 *             If either the Json or the Image File in the Json is not
	 *             found.
	 */
	public SpriteSheet(String jsonFilePath) throws FileNotFoundException {
		imageset = new HashMap<>();
		File f = new File("res/" + jsonFilePath);
		if (f.exists()) {
			String jsonString = FreeFormatFileReader.readAllLines(jsonFilePath);
			try {
				JsonParser p = new JsonParser();
				JsonObject element = p.parse(jsonString).getAsJsonObject();
				JsonObject meta = element.get("meta").getAsJsonObject();
				BufferedImage spritesheet = ImageIO
						.read(new File(f.getParent() + "/" + meta.get("image").getAsString()));
				JsonArray tiles = element.get("frames").getAsJsonArray();
				tiles.forEach(tile -> {
					createFromJson(tile.getAsJsonObject(), spritesheet);
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			throw new FileNotFoundException();
		}
	}

	/**
	 * Crops and rotates the part of the Spritesheet.
	 * 
	 * @param spritesheet
	 *            The Spritesheet
	 * @param x
	 *            Coordinate of the Tile
	 * @param y
	 *            Coordinate of the Tile
	 * @param w
	 *            Width of the Tile
	 * @param h
	 *            Height of the Tile
	 * @param rotated
	 *            If the Image is rotated
	 * @param pivot_x
	 *            The point of Rotation in x Direction
	 * @param pivot_y
	 *            The point of Rotation in y Direction
	 * @return The Tile as {@link BufferedImage}
	 */
	private BufferedImage cropAndRotate(BufferedImage spritesheet, int x, int y, int w, int h, boolean rotated,
			float pivot_x, float pivot_y) {
		if (rotated) {
			BufferedImage cropped = spritesheet.getSubimage(x, y, h, w);

			int width = cropped.getWidth();
			int height = cropped.getHeight();
			BufferedImage biFlip = new BufferedImage(height, width, cropped.getType());
			for (int i = 0; i < width; i++)
				for (int j = 0; j < height; j++)
					biFlip.setRGB(height - 1 - j, width - 1 - i, cropped.getRGB(i, j));

			AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
			tx = AffineTransform.getScaleInstance(-1, 1);
			tx.translate(-biFlip.getWidth(null), 0);
			AffineTransformOp  op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
			biFlip = op.filter(biFlip, null);

			return biFlip;

		} else {
			return spritesheet.getSubimage(x, y, w, h);
		}
	}

	/**
	 * Reads all the inputs from the Json Object. And stores it into the the
	 * HashMap.
	 * 
	 * @param object
	 *            The read Object from the File.
	 * @param spriteSheet
	 *            The SpriteSheet.
	 */
	private void createFromJson(JsonObject object, BufferedImage spriteSheet) {
		String filename = object.get("filename").getAsString();

		JsonObject frame = object.get("frame").getAsJsonObject();
		int x = frame.get("x").getAsInt();
		int y = frame.get("y").getAsInt();
		int w = frame.get("w").getAsInt();
		int h = frame.get("h").getAsInt();

		boolean rotated = object.get("rotated").getAsBoolean();

		JsonObject pivot = object.get("pivot").getAsJsonObject();
		float pivot_x = pivot.get("x").getAsFloat();
		float pivot_y = pivot.get("y").getAsFloat();

		BufferedImage croppedImage = cropAndRotate(spriteSheet, x, y, w, h, rotated, pivot_x, pivot_y);
		if (croppedImage != null)
			imageset.put(filename, croppedImage);
	}

	public HashMap<String, BufferedImage> getImageset() {
		return imageset;
	}
}
