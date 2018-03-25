package com.bizvisionsoft.bruiengine.util;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;

public class BruiColors {

	public enum BruiColor {

		Blue_Grey_100(0xcf, 0xd8, 0xdc),
		
		Blue_Grey_300(0x90, 0xa4, 0xae),

		Blue_Grey_500(0x60, 0x7d, 0x8b),
		
		Blue_Grey_600(0x54, 0x6e, 0x7a),

		Blue_Grey_700(0x45, 0x5a, 0x64),

		Blue_Grey_800(0x37, 0x47, 0x4f),

		Blue_Grey_900(0x26, 0x32, 0x38),
		
		Indigo_500(0x3f,0x51,0xb5),
		
		Indigo_900(0x1a,0x23,0x73),
		
		Indigo_700(0x30,0x3f,0x9f),
		
		Teal_500(0x00,0x96,0x88),
		
		Teal_900(0x00,0x4d,0x40),
		
		Teal_700(0x00,0x79,0x6b),
		
		Grey_1000(0,0,0),
		
		Grey_900(0x21,0x21,0x21),
		
		Grey_600(0x75,0x75,0x75),
		
		Grey_200(0xee,0xee,0xee),
		
		Grey_50(0xfa,0xfa,0xfa),
		
		;

		private RGB rgb;

		private BruiColor(int r, int g, int b) {
			rgb = new RGB(r, g, b);
		}

		@Override
		public String toString() {
			return getHtmlColor(rgb);
		}
		
		public RGB getRgb() {
			return rgb;
		}

	}

	public static Color getColor(String name) {
		Color color = JFaceResources.getColorRegistry().get(name);
		if (color == null) {
			BruiColor bruiColor = BruiColor.valueOf(name);
			color = new Color(null, bruiColor.rgb);
			JFaceResources.getColorRegistry().put(name, bruiColor.rgb);
		}
		return color;
	}

	public static Color getColor(BruiColor bruiColor) {
		Color color = JFaceResources.getColorRegistry().get(bruiColor.name());
		if (color == null) {
			color = new Color(null, bruiColor.rgb);
			JFaceResources.getColorRegistry().put(bruiColor.name(), bruiColor.rgb);
		}
		return color;
	}
	
	public static String getHtmlColor(RGB rgb) {
		return "#" + hex(rgb.red) + hex(rgb.green) + hex(rgb.blue);
	}

	private static String hex(int color) {
		String i = Integer.toHexString(color);
		if (i.length() == 1) {
			return "0" + i;
		} else {
			return i;
		}
	}

}
