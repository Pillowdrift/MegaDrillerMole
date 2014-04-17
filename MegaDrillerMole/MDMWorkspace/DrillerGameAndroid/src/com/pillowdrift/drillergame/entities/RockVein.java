package com.pillowdrift.drillergame.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.pillowdrift.drillergame.emitters.PlayerMiningEmitter;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.other.SimplexNoise;
import com.pillowdrift.drillergame.other.SpawnableManager;
import com.pillowdrift.drillergame.scenes.GameScene;

public class RockVein extends ManagableSpawnable {
	
	static final float SEGMENT_SIZE = 20.0f;
	static final int SEGMENT_MIN_COUNT = 10;
	static final int SEGMENT_MAX_COUNT = 30;
	
	private static final float WIDTH_STEP = 0.04f;	
	private static final float WIDTH_SCALE = 0.3f;
	private static final float HEIGHT_FREQ = 0.016f;
	private static final float HEIGHT_SCALE = 0.4f / 2;	
	private static final float NOISE_FREQ = 0.1f;
	private static final float NOISE_MIN = 0.5f;
	private static final float NOISE_SCALE = 0.2f;
	
	private static final float MINIMUM_GAP = 0.3f;
	private static final float MINE_RATE = 50; // How many gems you get per second.
	private static final float GEMS_PER_SEGMENT = 10;
	
	// The emitter for being mined.
	PlayerMiningEmitter _emitter;
	
	// The amount of gems inside this rock.
	float _gemCount;
	
	// The maximum size of the trail.
	int _segmentCount;
	
	// Vars
	Mesh _mesh;
	float[] _floatBuffer;
	short[] _indices;
	
	// The texture
	float _oneOverTextureWidth;
	float _oneOverTextureHeight;
	
	float _startpos;

	public RockVein(GameScene parentGameScene, SpawnableManager manager) {
		super(parentGameScene, manager);
		
		_depth = 21;
		
		// Create the emitter
		_emitter = new PlayerMiningEmitter(parentGameScene.getParticlePool01(), parentGameScene);
	}
	
	public void init() {
		// Set Texture Wrap
		Texture tex = _parentGameScene.getResourceManager().getTexture("tunnel01");
		tex.setWrap(TextureWrap.Repeat, TextureWrap.ClampToEdge);
		_oneOverTextureWidth = 1.0f / (float)tex.getWidth();
		_oneOverTextureHeight = 1.0f / (float)tex.getHeight();	
		
		//Generate Segments into _floatBuffer
		generateSegments();
		
		// Create a our mesh
		if (_mesh != null) _mesh.dispose();
		_mesh = new Mesh(false, _segmentCount * 2, _segmentCount * 2, 
						 new VertexAttribute(Usage.Position, 2, "a_position"),
						 new VertexAttribute(Usage.TextureCoordinates, 2, "a_uv"));
		
		// Setup the indices.
		for (short i = 0; i < _segmentCount * 2; ++i) {
			_indices[i] = i;
		}
		
		_mesh.setIndices(_indices);
		_mesh.setVertices(_floatBuffer);		
		
		// Calculate how many gems there are.
		_gemCount = GEMS_PER_SEGMENT * _segmentCount;
	}
	
	@Override
	public void activate() {
		super.activate();
		
		// Initialize the vein
		init();
	}
	
	private void generateSegments() {
		
		_startpos = _parentGameScene.getGlobalXOffset() - _parentGameScene.getTargetWidth();
		
		_segmentCount = MathUtils.random(SEGMENT_MIN_COUNT, SEGMENT_MAX_COUNT);
		
		_floatBuffer = new float[_segmentCount * 4 * 2];
		_indices = new short[_segmentCount * 2];		
		
		float r1 = MathUtils.random(100.0f);
		float r2 = MathUtils.random(100.0f);
		float r3 = MathUtils.random(100.0f);
		float heightOffset = MathUtils.random(MINIMUM_GAP, 1 - MINIMUM_GAP);	
		
		int j = 0;
		for(int i = 0; i < _segmentCount; i++)
		{			
			float pdist = Math.min(1, i * WIDTH_STEP);			
			pdist = Math.min(pdist, (_segmentCount - i) * WIDTH_STEP);
			
			float width = smoothestep(pdist) * WIDTH_SCALE;			
			float height = simplexGenreate(i, r1, HEIGHT_FREQ, HEIGHT_SCALE * pdist) + heightOffset;
			
			float top = simplexGenreate(i, r2, NOISE_FREQ, NOISE_SCALE) + NOISE_MIN;			
			float bottom = simplexGenreate(i, r3, NOISE_FREQ, -NOISE_SCALE) - NOISE_MIN;		
			
			float topPos = top * width + height;
			float bottomPos = bottom * width + height;
			
			float xPos = 0 + i * SEGMENT_SIZE;
			
			float yTop = Utils.lerp(topPos, GameScene.WORLD_BEDROCK_HEIGHT, GameScene.WORLD_SURFACE_HEIGHT);
			float yBottom = Utils.lerp(bottomPos, GameScene.WORLD_BEDROCK_HEIGHT, GameScene.WORLD_SURFACE_HEIGHT);
			
			_floatBuffer[j] = xPos;			//x			
			_floatBuffer[j+1] = yTop;		//y
			_floatBuffer[j+2] = xPos / 200.0f;		//u
			_floatBuffer[j+3] = yTop / 200.0f;	//v		
			
			
			_floatBuffer[j+4] = xPos;		//x
			_floatBuffer[j+5] = yBottom;	//y
			_floatBuffer[j+6] = xPos / 200.0f;	//u
			_floatBuffer[j+7] = yBottom / 200.0f;	//v		
			
			j += 8;			
		}			
	}
	
	float smoothestep(float x)
	{
        return x*x*(3 - 2*x);
	}
	

	private float simplexGenreate(float i, float rand, float frequency, float scale) {
		return (float)SimplexNoise.noise(i * frequency + rand, 0) * scale;
	}
	
	public boolean checkCollision(Vector2 point)
	{
		float temp = (_parentGameScene.getGlobalXOffset() - _startpos);
		float x = point.x - temp;
		
		x /= SEGMENT_SIZE;		
		
		int i = (int)x * 8; // Multiply by floats per segment	
		
		if(i < 0)
			return false;
		
		if(i+5 >= _floatBuffer.length)
			return false;
			
		return  (_floatBuffer[i+1] > point.y && _floatBuffer[i+5] < point.y);
	}		

	@Override
	public void update(float dt) {
		// TODO Auto-generated method stub
		super.update(dt);
		
		// Make the rock vein disappear after it goes of the screen
		if ((_parentGameScene.getGlobalXOffset() - _startpos) + (SEGMENT_SIZE * _segmentCount) < 0) {
			remove();
		}
		
		// Do collision with the player.
		Player player = (Player)_parent.getEntityFirst("Player");
		if (player != null) {
			if (checkCollision(player.getPosV2())) {
				player.setVelocity(player.getVelocity().cpy().sub(player.getVelocity().cpy().mul(1 - 0.4f)));
				
				// Give the player points
				if (_gemCount > 0) {
					player.givePointsWithoutCombo((int)((MINE_RATE * dt) + 0.5f));
					_gemCount -= MINE_RATE * dt;
					
					// Emit the mining emitter.
					_emitter.update(dt, player.getPosX(), player.getPosY());
					_emitter.emit();
				}
			}
		}
	}
	
	@Override
	public void draw(SpriteBatch spriteBatch) {
		// Flush the sprite batch.. We need to do this since we need to
		// draw without spritebatch at the moment.	
		
		
		spriteBatch.flush();
		
		Gdx.gl.glDisable(GL10.GL_CULL_FACE);
		
		_parentGameScene.getResourceManager().getTexture("stone01").bind();
		
		Gdx.graphics.getGL10().glLoadMatrixf(new Matrix4().setToTranslation(_parentGameScene.getGlobalXOffset() - _startpos, 0, 0).val, 0);		
		_mesh.render(GL10.GL_TRIANGLE_STRIP);
		
		spriteBatch.setTransformMatrix(spriteBatch.getTransformMatrix());
		
		//Gdx.gl.glEnable(GL10.GL_TEXTURE_2D);
				
	}
}
