package com.pixelbypixel.bb.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.pixelbypixel.bb.Game;
import com.pixelbypixel.bb.handlers.GameButton;
import com.pixelbypixel.bb.handlers.GameStateManager;

public class Credits extends GameState {
	
	private World world;
	private GameButton backbutton;
	private BitmapFont font;
	private BitmapFont title;
	
	@SuppressWarnings("deprecation")
	public Credits(GameStateManager gsm) {
		
		super(gsm);
		
		world = new World(new Vector2(0, -9.8f * 5), true);
		
		Texture tex = Game.res.getTexture("arrowleft");
		backbutton = new GameButton(new TextureRegion(tex, 0, 0, 50, 50), 29, 135, cam);
		
		FreeTypeFontGenerator gen = new FreeTypeFontGenerator(Gdx.files.internal("fonts/ASCII.ttf"));
		font = gen.generateFont(12);
		title = gen.generateFont(20);
		
	}
	
	public void handleInput() {
		if(backbutton.isClicked()) gsm.setState(GameStateManager.MENU);
	}
	
	public void update(float dt) {
		
		handleInput();
		
		world.step(dt / 5, 8, 3);
		
		Menu.bg.update(dt);
		
		backbutton.update(dt);
		
	}
	
	public void render() {
		
		sb.setProjectionMatrix(cam.combined);
		
		Menu.bg.render(sb);
		
		backbutton.render(sb);
		
		sb.begin();
		font.setColor(Color.BLUE);
		font.draw(sb, "Levels 1-5", 120, 200);
		font.draw(sb, "Mike Sia", 127, 180);
		font.draw(sb, "Sprites", 130, 150);
		font.draw(sb, "Mike Sia", 126, 130);
		font.draw(sb, "Original source code", 80, 100);
		font.draw(sb, "Mike Sia", 125, 80);
		font.draw(sb, "Modified source code", 80, 60);
		font.draw(sb, "Deja Jackson", 115, 45);
		sb.end();
		
		sb.begin();
		title.setColor(Color.BLUE);
		title.draw(sb, "Credits", 113, 230);
		sb.end();
		
	}
	
	public void dispose() {
		
	}

}

--------------------

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gdp.thirtysecondsnippets;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 *
 * @author George
 */
public class StatsScreen implements Screen{
    Game tss;
    private Stage stage = new Stage(new StretchViewport(TSS.WIDTH, TSS.HEIGHT));
    private Table table = new Table();
    
    TextureAtlas atlas = new TextureAtlas(Gdx.files.internal("buttons.pack"));

    private Skin skin = new Skin(Gdx.files.internal("skin.json"), atlas);

    Preferences prefs = Gdx.app.getPreferences("30SSSettings");
    
    public StatsScreen(Game tss){
        this.tss = tss;
    }

    @Override
    public void show() {
        MusicDB db = new MusicDB();
        final User user = db.getUserByID(Installation.id());
        //User user = new User();
        //user.insertTestData();
        
        DecimalFormat df = new DecimalFormat();
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setGroupingSeparator(',');

        df.setDecimalFormatSymbols(dfs);
        
        Label statsLabel = new Label("Stats", skin.get("labelb", Label.LabelStyle.class));
       
        Label threaded  = new Label("Needles Threaded", skin.get("labelb", Label.LabelStyle.class));
        Label threaded_val  = new Label(df.format(user.getNeedles_thread()), skin.get("labelb", Label.LabelStyle.class));
        
        Label thread_cut  = new Label("Thread Cut", skin.get("labelb", Label.LabelStyle.class));
        Label thread_cut_val  = new Label(df.format((int) (user.getThread_cut()/2.0))+" m", skin.get("labelb", Label.LabelStyle.class));
        
        Label songs_played  = new Label("Songs Played", skin.get("labelb", Label.LabelStyle.class));
        Label songs_played_val  = new Label(df.format(user.getSong_played()), skin.get("labelb", Label.LabelStyle.class));
        
        Label beats  = new Label("Beats Felt ", skin.get("labelb", Label.LabelStyle.class));
        Label beats_val  = new Label(df.format(user.getBeats()), skin.get("labelb", Label.LabelStyle.class));
        
        Label total_needles  = new Label("Needles Seen", skin.get("labelb", Label.LabelStyle.class));
        Label total_needles_val  = new Label(df.format(user.getTotal_needles()), skin.get("labelb", Label.LabelStyle.class));
        
        Label thread_rate  = new Label("Thread Rate", skin.get("labelb", Label.LabelStyle.class));
        System.out.println((int)(((double)user.getNeedles_thread()/(double)user.getTotal_needles())));
        Label thread_rate_val  = new Label(Integer.toString((int)(((double)user.getNeedles_thread()/(double)user.getTotal_needles())*100))+"%", skin.get("labelb", Label.LabelStyle.class));
        
        TextButton back = new TextButton("<", skin.get("back", TextButton.TextButtonStyle.class));
        
        back.addListener(new ChangeListener(){
                @Override
                public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                    ThemeMenu game = new ThemeMenu(tss);
                    tss.setScreen(game);
                }
        });        
                
        table.add(statsLabel).top().padTop(2).colspan(2).height(Value.percentHeight(.15f, table));
        table.row();
        
        table.add(threaded).right();
        table.add(threaded_val).padLeft(35).left();
        table.row();
        
        table.add(thread_cut).right();
        table.add(thread_cut_val).padLeft(35).left();
        table.row();
        
        table.add(songs_played).right();
        table.add(songs_played_val).padLeft(35).left();
        table.row();
        
        table.add(beats).right();
        table.add(beats_val).padLeft(35).left();
        table.row();
        
        table.add(total_needles).right();
        table.add(total_needles_val).padLeft(35).left();
        table.row();
        
        table.add(thread_rate).right();
        table.add(thread_rate_val).padLeft(35).left();
        table.row();
        
        table.add(back).height(Value.percentHeight(.40f)).width(Value.percentHeight(.40f)).padTop(20).left().colspan(3);
        
        table.setBackground(skin.getDrawable("bg_blur"));
        table.setFillParent(true);
        //table.debug();
        
        stage.addActor(table);
        
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        atlas.dispose();
        skin.dispose();
        stage.dispose();        
    }
}

--------------------

package atm.se.project.pacman;

import atm.se.project.pacman.classes.MainMenu;
import atm.se.project.pacman.splash.Splash;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MyGdxGame extends Game {

	@Override
	public void create () {
		//batch = new SpriteBatch();
		//img = new Texture("pac.jpg");
		setScreen(new MainMenu());
	}

	@Override
	public void render () {
		//Gdx.gl.glClearColor(0, 0, 0, 1);
		//Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//batch.begin();
		//batch.draw(img, 250, 400);
		//batch.end();
		super.render();
	}

	@Override
	public void resize(int width, int height) {
			super.resize(width, height);
	}

	@Override
	public void pause() {
			super.pause();
	}

	@Override
	public void resume() {
			super.resume();
	}

	@Override
	public void dispose() {
			super.dispose();
	}
}

--------------------

