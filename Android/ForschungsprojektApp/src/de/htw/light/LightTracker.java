package de.htw.light;

public class LightTracker {

	public static double getEstimatedLightValueWindowForDayTime(int daytime) {
		if (daytime <= 2 || daytime >= 22) {
			return 0;
		}
		
		return (-0.29133 * Math.pow(daytime,3) + (0.155548 * Math.pow(daytime, 2)) + 150.277*daytime -427.133);
	}
	
	public static double getEstimatedLightValueWallForDayTime(int daytime) {
		return 0;
	}
}
