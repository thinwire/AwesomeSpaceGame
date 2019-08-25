package engine;

import java.io.File;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class Image {
	private BufferedImage data;
	
	public Image(String file) {
		try {
			data = ImageIO.read(new File(file));
		} catch(Exception e) {
			System.err.println("Error reading image " + file);
			System.err.println(e.getMessage());
			System.exit(1);
		}
	}
	
	public BufferedImage getData() {
		return data;
	}
	
	public int getWidth() {
		return data.getWidth();
	}
	
	public int getHeight() {
		return data.getHeight();
	}
}
