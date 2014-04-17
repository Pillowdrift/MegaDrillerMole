package com.pillowdrift.drillergame;


import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.pillowdrift.drillergame.crossinterfaces.MDMAdsServiceApi;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.pillowdrift.drillergame.crossinterfaces.MDMDataAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.MDMFacebookAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.MDMTwitterAccessServiceApi;
import com.pillowdrift.drillergame.crossinterfaces.WebBrowserServiceApi;
import com.pillowdrift.drillergame.data.DataCache;
import com.pillowdrift.drillergame.framework.ResourceManager;
import com.pillowdrift.drillergame.framework.Scene;
import com.pillowdrift.drillergame.framework.SceneManager;
import com.pillowdrift.drillergame.framework.Utils;
import com.pillowdrift.drillergame.scenes.AboutScene;
import com.pillowdrift.drillergame.scenes.DebugScene;
import com.pillowdrift.drillergame.scenes.GameScene;
import com.pillowdrift.drillergame.scenes.HighRetryScene;
import com.pillowdrift.drillergame.scenes.MenuScene;
import com.pillowdrift.drillergame.scenes.OptionsScene;
import com.pillowdrift.drillergame.scenes.PauseScene;
import com.pillowdrift.drillergame.scenes.QuitScene;
import com.pillowdrift.drillergame.scenes.RecordsScene;
import com.pillowdrift.drillergame.scenes.RetryScene;
import com.pillowdrift.drillergame.scenes.ShopScene;
import com.pillowdrift.drillergame.scenes.SplashScene;
import com.pillowdrift.drillergame.scenes.TutorialScene;

//Main game class for Mega Driller Mole by Pillowdrift Ltd.
//This implements the libGDX interface 'ApplicationListener',
//becoming an application capable of being run on Android.
public final class DrillerGame01 implements ApplicationListener
{
	//DEFINES
	private static final boolean DEBUG = false;
	
	//DATA
	//SceneManager
	private SceneManager _sceneManager;
	//Scenes
	private GameScene _gameScene;
	private MenuScene _menuScene;
	private RecordsScene _recordsScene;
	private AboutScene _aboutScene;
	private RetryScene _retryScene;
	private HighRetryScene _highRetryScene;
	private QuitScene _quitScene;
	private PauseScene _pauseScene;
	private OptionsScene _optionsScene;
	private TutorialScene _tutorialScene;
	private ShopScene _shopScene;
	
	//Services
	WebBrowserServiceApi _webService;
	MDMDataAccessServiceApi _dataService;
	MDMFacebookAccessServiceApi _facebookService;
	MDMTwitterAccessServiceApi _twitterService;
	MDMAdsServiceApi _adsService;
	
	//ACCESS
	public WebBrowserServiceApi getWebBrowserService()
	{
		return _webService;
	}
	public MDMDataAccessServiceApi getDataService()
	{
		return _dataService;
	}
	public MDMFacebookAccessServiceApi getFacebookService()
	{
		return _facebookService;
	}
	public MDMTwitterAccessServiceApi getTwitterService()
	{
		return _twitterService;
	}
	public MDMAdsServiceApi getAdsService()
	{
		return _adsService;
	}

	//CONSTRUCTION
	public DrillerGame01(WebBrowserServiceApi webService, 
						 MDMDataAccessServiceApi dataService,
						 MDMFacebookAccessServiceApi facebookService,
						 MDMTwitterAccessServiceApi twitterService,
						 MDMAdsServiceApi adsService)
	{
		//Store a reference to the services provided
		_webService = webService;
		_dataService = dataService;
		_facebookService = facebookService;
		_twitterService = twitterService;
		_adsService = adsService;
	}

	//FUNCTION
	@Override
	public void create()
	{
		//Initialize random number generation
		Utils.randInit();
		
		//Add our scene manager
		_sceneManager = new SceneManager(this);
		_sceneManager.setClearColour(new Color(81.0f/255.0f, 182.0f/255.0f, 234.0f/255.0f, 1.0f));
		
		//Open our databases
		_dataService.begin();
		
		//Tell the data cache which service to use
		DataCache.setDataService(_dataService);
		//Update the scores initially
		DataCache.updateScores();
		//Update the config initially
		DataCache.updateConfig();
		
		//Load our resources
		ResourceManager resMan = _sceneManager.getResourceManager();
		resMan.loadAtlas("atlas01", "data/graphics/pack");
		resMan.loadFont("OSP-DIN", "data/fonts/OSP-DIN-OUTLINED.fnt", resMan.getAtlasRegion("atlas01", "OSP-DIN-OUTLINED"));
		resMan.loadTexture("tunnel01", "data/graphics/tunnel01.png");
		resMan.loadTexture("stone01", "data/graphics/stone.png");
		resMan.getTexture("stone01").setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
		
		// Load sounds.
		resMan.loadSound("dig", "data/sounds/dig.wav");
		resMan.loadSound("air", "data/sounds/air.wav");
		resMan.loadSound("explosion", "data/sounds/explosion.wav");
		resMan.loadSound("unload", "data/sounds/unload.wav");
		resMan.loadSound("wormexplosion", "data/sounds/wormexplode.wav");
		resMan.loadSound("demoncatentry", "data/sounds/demoncatentry.wav");
		resMan.loadSound("gem", "data/sounds/gem.wav");
		resMan.loadSound("fireball", "data/sounds/fireball.wav");
		resMan.loadSound("button", "data/sounds/button.wav");
		resMan.loadSound("bedrock", "data/sounds/bedrock.wav");
		resMan.loadSound("whirlwind", "data/sounds/whirlwind.wav");
		resMan.loadSound("boost", "data/sounds/boost.wav");
		resMan.loadSound("dirtpedo", "data/sounds/dirtpedo.mp3");
		resMan.loadSound("pop", "data/sounds/pop.mp3");
		
		//Add our scenes
		_gameScene = new GameScene(_sceneManager);
		_sceneManager.addScene("GameScene", _gameScene);
		_menuScene = new MenuScene(_sceneManager);
		_sceneManager.addScene("MenuScene", _menuScene);
		_recordsScene = new RecordsScene(_sceneManager);
		_sceneManager.addScene("RecordsScene", _recordsScene);
		_aboutScene = new AboutScene(_sceneManager);
		_sceneManager.addScene("AboutScene", _aboutScene);
		_retryScene = new RetryScene(_sceneManager);
		_sceneManager.addScene("RetryScene", _retryScene);
		_highRetryScene = new HighRetryScene(_sceneManager);
		_sceneManager.addScene("HighRetryScene", _highRetryScene);
		_quitScene = new QuitScene(_sceneManager);
		_sceneManager.addScene("QuitScene", _quitScene);
		_pauseScene = new PauseScene(_sceneManager);
		_sceneManager.addScene("PauseScene", _pauseScene);
		_shopScene = new ShopScene(_sceneManager);
		_sceneManager.addScene("ShopScene", _shopScene);
		if (getDataService().getConfigDataAccessor() != null) {
			_optionsScene = new OptionsScene(_sceneManager);
			_sceneManager.addScene("OptionsScene", _optionsScene);
		}
		_tutorialScene = new TutorialScene(_sceneManager);
		_sceneManager.addScene("TutorialScene", _tutorialScene);
		if (DEBUG) {
			_sceneManager.addScene("DebugScene", new DebugScene(_sceneManager));
		}
		
		// Add the splash screen
		Scene splash = new SplashScene(_sceneManager);
		_sceneManager.addScene("SplashScene", splash);
		splash.activate();
		
		//Set their initial states
		_gameScene.activate();
		_gameScene.setActive(false);
		_menuScene.activate();
		_menuScene.setActive(false);
		
		Gdx.graphics.setVSync(true);
	}

	@Override
	public void resize(int width, int height)
	{
		//Do not respond
	}

	@Override
	public void render()
	{
		//Get delta time since last frame
		//This is passed to our scenes for time-based processing
		float dt = Gdx.graphics.getDeltaTime();
		if (dt > 0.1f) {
			dt = 0.1f;
		}
		
		//Update scenes
		_sceneManager.update(dt);
		
		//Draw scenes
		_sceneManager.draw();
	}

	@Override
	public void pause()
	{
		_sceneManager.pause();
		_dataService.pause();
	}

	@Override
	public void resume()
	{
		_sceneManager.resume();
		_dataService.resume();
		_facebookService.extendFacebookToken();
	}

	@Override
	public void dispose()
	{
		_sceneManager.release();
		_dataService.dispose();
	}

}
