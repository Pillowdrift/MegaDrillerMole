package com.pillowdrift.drillergame.entities;

import java.util.Iterator;
import java.util.LinkedList;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.framework.Entity;
import com.pillowdrift.drillergame.framework.GameEntity;
import com.pillowdrift.drillergame.other.Vertex;
import com.pillowdrift.drillergame.scenes.GameScene;

/**
 * Draws a trail behind the player.
 * @author bombpersons
 *
 */
public class DigTrail extends GameEntity {
	
	// Vars
	Mesh _mesh;
	LinkedList<Vertex> _vertices;
	float[] _floatBuffer = new float[MAX_SIZE * 4 * 2];
	short[] _indices = new short[MAX_SIZE * 2];
	
	//Width of the trail
	private static final float TRAIL_WIDTH = 17.0f;	
	
	// The maximum size of the trail.
	private static final int MAX_SIZE = 1000;
	
	// The frequency of putting down vertices.
	private static final float FREQUENCY = 20f;
	private static final float MAX_DIST = 100;
	
	// The cached rotation of the player.
	float _playerRotation = 9999;
	
	// Whether or not we have just entered the sky
	boolean _switchedSurface = false;
	
	// The texture
	float _textureWidth = 1;
	float _textureHeight = 1;
	float _oneOverTextureWidth = 1;
	float _oneOverTextureHeight = 1;
	float _cameraWorldX = 0;
	
	// The entity
	GameEntity _entity;
	private float _acumilaedRotation = 0;
	
	
	private Vector2 _lastPlacedSegmentPos = new Vector2();
	
	/**
	 * Create a new trail.
	 * @param parent
	 */
	public DigTrail(GameScene parent, GameEntity entity)
	{
		super(parent);
		
		_entity = entity;
		
		// Create a our mesh
		_mesh = new Mesh(false, MAX_SIZE * 2, MAX_SIZE * 2, 
						 new VertexAttribute(Usage.Position, 2, "a_position"),
						 new VertexAttribute(Usage.TextureCoordinates, 2, "a_uv"));
		
		// Create the queue
		_vertices = new LinkedList<Vertex>();
		
		// Setup the indices.
		for (short i = 0; i < MAX_SIZE * 2; ++i)
		{
			_indices[i] = i;
		}
		_mesh.setIndices(_indices);
		
		// Set our depth
		_depth = 20;
		
		// Set Texture Wrap
		Texture tex = _parentGameScene.getResourceManager().getTexture("tunnel01");
		tex.setWrap(TextureWrap.Repeat, TextureWrap.ClampToEdge);
		_textureWidth = tex.getWidth();
		_textureHeight = tex.getHeight();
		_oneOverTextureWidth = 1.0f / _textureWidth;
		_oneOverTextureHeight = 1.0f / _textureHeight;
	}
	
	public void putVertices()
	{
		// Pop the back of the list of if the list is to big.
		if (_vertices.size() >= MAX_SIZE)
		{
			_vertices.removeFirst();
			_vertices.removeFirst();
		}
		
		// Get the player
		Player player = ((Player)_parent.getEntityFirst("Player"));
		if (player == null)
			return;
		Vector2 pos = player.getPosV2();
		Vector2 vel = player.getVelocity();			
		Vector2 perpendicular = new Vector2(-vel.y, vel.x).nor();
		
		
		// VertexONE
		float xOne = pos.x + perpendicular.x * TRAIL_WIDTH;
		float yOne = pos.y + perpendicular.y * TRAIL_WIDTH;
		
		// VertexTWO
		float xTwo = pos.x - perpendicular.x * TRAIL_WIDTH;
		float yTwo = pos.y - perpendicular.y * TRAIL_WIDTH;
		
		float uOne = (xOne - _cameraWorldX) * _oneOverTextureWidth;
		float vOne = (yOne - 400.0f) * -1 * _oneOverTextureHeight;
		
		float uTwo = (xTwo - _cameraWorldX) * _oneOverTextureWidth;
		float vTwo = (yTwo - 400.0f) * -1 * _oneOverTextureHeight;
		
		// System.out.println("uOne: " + uOne + ", vOne: " + vOne);	
		
		// Add coordinates.
		Vertex vertexOne = new Vertex(xOne, yOne, uOne, vOne);
		Vertex vertexTwo = new Vertex(xTwo, yTwo, uTwo, vTwo);
		
		_vertices.add(vertexTwo);
		_vertices.add(vertexOne);

	}
	
	@Override
	public void update(float dt)
	{
		super.update(dt);
		
		// Move the players world position aruond.
		_cameraWorldX += dt * _parentGameScene.ALL_VELOCITY_X;
		_lastPlacedSegmentPos.sub(dt * _parentGameScene.ALL_VELOCITY_X, 0);
		
		// Move the trail along.
		Iterator<Vertex> iter = _vertices.iterator();
		while (iter.hasNext()) iter.next()._x += _parentGameScene.ALL_VELOCITY_X*dt;		
		
		// If the last item is of screen.
		if (_vertices.size() > 0)
		{
			if (_vertices.getFirst()._x < -100.0f)
			{
				_vertices.removeFirst();
				_vertices.removeFirst();
			}
		}
		
		Player player = ((Player)_parent.getEntityFirst("Player"));
		if (player == null || (!player.isAlive() && !player.isBlownUp()) || player.getRemoveFlag())
			return;
		
		// If the entity is onscreen
		
		// Get the rotation of the player.
		_acumilaedRotation += Math.abs(_playerRotation - _entity.getVelocity().angle());	
		
		// If there are no vertices..
		if (_vertices.size() == 0)
		{
			putVertices();
			_lastPlacedSegmentPos.set(player.getPosV2());
		}
		
		//Distance between the last placed segment and the player position
		Vector2 segmentGap = _lastPlacedSegmentPos.cpy().sub(player.getPosV2());
		boolean b = segmentGap.len() > MAX_DIST;
		if (_acumilaedRotation > FREQUENCY || b)
		{
			putVertices();
			_lastPlacedSegmentPos.set(player.getPosV2());
			_acumilaedRotation = 0;

		}		
		// Move the last vertices we put down to be here.
		else if (_vertices.size() != 0)
		{
			_vertices.removeLast();
			_vertices.removeLast();
		}
		putVertices();		
		
		// Update the rotation of the player.
		_playerRotation = _entity.getVelocity().angle();
	}
	
	@Override
	public void onGameReset()
	{
		super.onGameReset();
		
		// Reset the vertices.
		
		remove();
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch)
	{ 
		// Flush the sprite batch.. We need to do this since we need to
		// draw without spritebatch at the moment.
		spriteBatch.flush();
		
		if (_vertices.size() > 4)
		{
			// Construct the buffer.
			Iterator<Vertex> iter = _vertices.iterator();
			int i = 0;
			while (iter.hasNext()) {
				Vertex vertex = iter.next();
				
				_floatBuffer[i] = vertex._x;
				_floatBuffer[i+1] = vertex._y;
				_floatBuffer[i+2] = vertex._u;
				_floatBuffer[i+3] = vertex._v;
				i += 4;
			}
			
			// Enable the depth buffer so that the transparency doesn't screw up.
			Gdx.gl.glDepthMask(true);
			Gdx.gl.glEnable(GL10.GL_DEPTH_TEST);
			Gdx.gl.glDepthFunc(GL10.GL_LESS);
			Gdx.gl.glDisable(GL10.GL_CULL_FACE);
			
			if (i > 0)
			{
				_parentGameScene.getResourceManager().getTexture("tunnel01").bind();
				_mesh.setVertices(_floatBuffer, 0, i);
				_mesh.render(GL10.GL_TRIANGLE_STRIP, 0, _vertices.size());
			}
			
			Gdx.gl.glDisable(GL10.GL_DEPTH_TEST);
		}
	}	
}
