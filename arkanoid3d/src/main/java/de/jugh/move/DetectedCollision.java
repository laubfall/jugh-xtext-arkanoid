package de.jugh.move;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import javafx.scene.Node;

/**
 * Holds information about a collision.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class DetectedCollision
{
	private Node collider;
	
	private List<Node> colliders;

	private Supplier<Optional<DetectedCollision>> collisionDetectedBy;
	
	public DetectedCollision(Node collider, List<Node> colliders, Supplier<Optional<DetectedCollision>> collisionDetectedBy) {
		super();
		this.collider = collider;
		this.colliders = colliders;
		this.collisionDetectedBy = collisionDetectedBy;
	}

	public Node getCollider()
	{
		return collider;
	}

	public List<Node> getColliders()
	{
		return colliders;
	}

	public Supplier<Optional<DetectedCollision>> getCollisionDetectedBy()
	{
		return collisionDetectedBy;
	}
}
