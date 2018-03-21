package de.jugh.move;

/**
 * Basic interface for movements of any kind.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public interface IMovement
{
	void startMovement();

	void updateMovement();

	void changeMovement(double percentage);
	
	void applyMovement();
	
	boolean isStarted();

}