package graphics.statemachine;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import graphics.images.SpriteManager;
import parser.file.FreeFormatFileReader;

public class StateMachine {

	private HashMap<String, Animation> animations;
	private String activeAnimation;

	public StateMachine(SpriteManager spriteManager, String path, Uniform... uniforms) {
		animations = new HashMap<>();

		File f = new File("res/" + path);
		if (f.exists()) {
			String fullFile = FreeFormatFileReader.readAllLines(path);
			for (Uniform uniform : uniforms) {
				fullFile = fullFile.replace("@" + uniform.getUniform(), uniform.getValue());
			}

			JsonParser parser = new JsonParser();
			JsonObject obj = parser.parse(fullFile).getAsJsonObject();

			JsonObject animation = obj.get("animations").getAsJsonObject();
			for (Entry<String, JsonElement> element : animation.entrySet()) {
				String name = element.getKey();
				JsonObject anim = element.getValue().getAsJsonObject();

				String order = anim.get("order").getAsString();
				String[] stringOrders = order.split("-");
				int[] orders = new int[stringOrders.length];

				for (int i = 0; i < stringOrders.length; i++) {
					orders[i] = Integer.parseInt(stringOrders[i]);
				}

				JsonArray steps = anim.get("steps").getAsJsonArray();

				List<AnimationStep> animSteps = new ArrayList<>();
				for (int i = 0; i < steps.size(); i++) {
					JsonObject step = steps.get(i).getAsJsonObject();

					float steplength = step.get("steplength").getAsFloat();
					String[] spritePath = step.get("image").getAsString().split(Pattern.quote("."));
					BufferedImage image = spriteManager.getImage(spritePath[0], spritePath[1]);

					animSteps.add(new AnimationStep(image, steplength));

					if (image == null) {
						System.out.println("Image not found " + spritePath[0] + " : " + spritePath[1]);
					}
				}
				AnimationStep[] array = animSteps.toArray(new AnimationStep[animSteps.size()]);
				animations.put(name, new Animation(orders, array));
			}
		}
	}

	public void update() {
		if (activeAnimation != null)
			animations.get(activeAnimation).update();
	}

	public void setActiveAnimation(String newActive) {
		if (animations.containsKey(newActive)) {
			activeAnimation = newActive;
		}
	}

	public void render(Graphics g, int x, int y) {
		if (animations.containsKey(activeAnimation)) {
			g.drawImage(animations.get(activeAnimation).getCurrentImage(), x, y, null);
		}
	}
}
