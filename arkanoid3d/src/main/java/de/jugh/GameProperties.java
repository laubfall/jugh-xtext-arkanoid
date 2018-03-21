package de.jugh;

import java.io.IOException;
import java.util.Properties;
import java.util.function.Function;

public class GameProperties extends Properties
{
	/**
	 * The serial version uid. 
	 */
	private static final long serialVersionUID = -5862061687150328495L;
	
	/**
	 * Singleton instance.
	 */
	private static GameProperties instance = new GameProperties();
	
	private GameProperties() {
		try {
			load(getClass().getResourceAsStream("/ark.properties"));
		} catch (IOException e) {
			throw new ArkGameException("failed loading game properties", e);
		}
	}

	public static GameProperties get() {
		return instance;
	}
	
	public long getLongProperty(String key) {
		return convert(Long::valueOf, key);
	}
	
	public int getIntProperty(String key) {
		return convert(Integer::valueOf, key);
	}
	
	public double getDoubleProperty(String key) {
		return convert(Double::valueOf, key);
	}
	
	private <R> R convert(Function<String, R> converter, String key) {
		final String property = getProperty(key);
		try {
			return converter.apply(property);
		} catch (NumberFormatException e) {
			throw new ArkGameException("Failed to convert properties value to number. Prop: " + key, e);
		}
	}
}
