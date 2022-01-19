package com.ray3k.demonstration;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;
import com.ray3k.stripe.FreeTypeSkin;
import com.ray3k.stripe.ResizeWidget;
import com.ray3k.stripe.ResizeWidget.ResizeWidgetStyle;
import com.ray3k.stripe.ViewportWidget;

/**
 * This class demonstrates the differences between the various viewports available in libGDX. It is intended to be displayed inside of the
 */
public class Core extends ApplicationAdapter {
	private Stage stage;
	private Skin skin;
	private SpriteBatch spriteBatch;
	private final ArrayMap<String, Viewport> viewports = new ArrayMap<>();
	private final Array<TextureRegion> regions = new Array<>();
	private ViewportWidget viewportWidget;
	public static final float DEFAULT_WIDTH = 400f;
	public static final float DEFAULT_HEIGHT = 400f;

	@Override
	public void create() {
		viewports.put("Stretch", new StretchViewport(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		viewports.put("Fit", new FitViewport(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		viewports.put("Fill", new FillViewport(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		viewports.put("Screen", new ScreenViewport());
		viewports.put("Extend", new ExtendViewport(DEFAULT_WIDTH, DEFAULT_HEIGHT));
		for (Viewport viewport : viewports.values()) {
			viewport.getCamera().position.set(DEFAULT_WIDTH / 2, DEFAULT_HEIGHT / 2, 1f);
		}

		skin = createSkin();
		regions.add(skin.getRegion("sample-image1"));
		regions.add(skin.getRegion("sample-image2"));
		regions.add(skin.getRegion("sample-image3"));

		spriteBatch = new SpriteBatch();
		stage = new Stage(new ScreenViewport(), spriteBatch);
		Gdx.input.setInputProcessor(stage);

		//The ViewportWidget allows a viewport to be manipulated from Scene2D
		viewportWidget = new ViewportWidget(viewports.firstValue());

		//The ResizeWidget allows a widget to be resized and repositioned dynamically.
		ResizeWidget resizeWidget = new ResizeWidget(viewportWidget, skin);
		resizeWidget.getStack().setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		resizeWidget.setFillParent(true);
		stage.addActor(resizeWidget);

		SelectBox<String> selectBox = new SelectBox<String>(skin) {
			@Override
			public void act(float delta) {
				super.act(delta);
				//Keep the selectBox attached to the position of the viewportWidget
				Actor actor = resizeWidget.getStack();
				setPosition(actor.getX() + actor.getWidth() - getWidth() - 14, actor.getY() + actor.getHeight() - 6);
				viewportWidget.viewport.apply();
			}
		};
		selectBox.setItems(viewports.keys().toArray());
		selectBox.setAlignment(Align.center);
		changeListener(selectBox, () -> {
			viewportWidget.viewport = viewports.get(selectBox.getSelected());
			viewportWidget.viewport.apply();
			viewportWidget.layout();
		});
		stage.addActor(selectBox);

		Table root = new Table();
		root.setFillParent(true);
		root.align(Align.top);
		stage.addActor(root);

		Label label = new Label("Resize the viewport by dragging the handles.\nGreen background is the visible area outside the specified world.", skin);
		label.setAlignment(Align.center);
		root.add(label).expandX();
	}

	@Override
	public void render() {
		stage.act();

		ScreenUtils.clear(Color.BLACK);

		//draw demonstration viewport
		viewportWidget.viewport.apply();
		spriteBatch.setProjectionMatrix(viewportWidget.viewport.getCamera().combined);
		spriteBatch.begin();

		Drawable drawable = skin.getDrawable("pattern-10");
		drawable.draw(spriteBatch, viewportWidget.viewport.getCamera().position.x  - Gdx.graphics.getWidth() / 2f, viewportWidget.viewport.getCamera().position.y - Gdx.graphics.getHeight() / 2f, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		TextureRegion region = regions.get(0);
		spriteBatch.draw(region, 0, 0);
		spriteBatch.end();

		//draw ui viewport
		stage.getViewport().apply();
		stage.draw();
	}

	@Override
	public void resize(int width, int height) {
		stage.getViewport().update(width, height, true);
	}

	@Override
	public void dispose() {
		stage.dispose();
		skin.dispose();
	}

	private Skin createSkin() {
		Skin skin = new FreeTypeSkin(Gdx.files.internal("skin.json"));

		ResizeWidgetStyle style = new ResizeWidgetStyle();
		style.handle = skin.getDrawable("resizer-major");
		style.handleOver = skin.getDrawable("resizer-major-over");
		style.handlePressed = skin.getDrawable("resizer-major-pressed");
		style.minorHandle = skin.getDrawable("resizer-minor");
		style.minorHandleOver = skin.getDrawable("resizer-minor-over");
		style.minorHandlePressed = skin.getDrawable("resizer-minor-pressed");
		style.background = skin.getDrawable("resizer-background-10");
		skin.add("default", style);

		return skin;
	}

	public static void changeListener(Actor actor, Runnable runnable) {
		actor.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				runnable.run();
			}
		});
	}
}