# libGDX Viewports

So… VIEWPORTS! The bane of the libGDX beginner. Viewports are a critical component to controlling how players peer into the world you’ve created for them. And yet some people manage to make games without them. How do they do that? And more importantly, why? Viewports will dramatically improve the visual way your game is consumed by players and they are very easy to understand after grasping a few key concepts.
Most people make the mistake of jumping straight into projections and world units and all that jazz. No, let’s just look at a game without a viewport.

![Untitled_1 4 1](https://user-images.githubusercontent.com/60154347/170790238-c7e826f1-ee58-420e-b230-d4e3fe1101a4.png)

See, it doesn’t look like there’s anything wrong here. But, what if you resize the window.

![Untitled_1 4 2](https://user-images.githubusercontent.com/60154347/170790265-f85f62e2-73c8-4364-84b3-25b42275a44b.png)

Ahh, yes. That, indeed, looks like crap. And what if we want our game to work on various devices with different aspect ratios? We want everyone to have a similar experience and we certainly don’t want anyone to have an unfair advantage. We need to have some way of controlling that.

## Interactive Sample

View the interactive HTML5 sample here: https://raeleus.github.io/viewports-sample-project/

This demonstrates the various Viewports and their behaviors.

## Demonstration Assets

Place the following assets in your project's assets folder if you want to follow along: [Viewports Demo Assets.zip](https://github.com/raeleus/viewports-sample-project/files/7964323/Viewports.Demo.Assets.zip)

## FitViewport

Let’s apply a FitViewport.

![Untitled_1 6 1](https://user-images.githubusercontent.com/60154347/170790280-4ad0f84d-9550-4b76-a159-88e53c498324.png)

```java
public class Core extends ApplicationAdapter {
    private TextureRegion textureRegion;
    private SpriteBatch spriteBatch;
    private FitViewport fitViewport;
    
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        Texture texture = new Texture(Gdx.files.internal("texture.png"));
        textureRegion = new TextureRegion(texture);
        fitViewport = new FitViewport(800, 800);
    }
    
    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        fitViewport.apply();
        spriteBatch.setProjectionMatrix(fitViewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(textureRegion, 0, 0);
        spriteBatch.end();
    }
    
    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height);
    }
    
    @Override
    public void dispose() {
        spriteBatch.dispose();
        textureRegion.getTexture().dispose();
    }
}
```

You have to provide a world width and height to create one. Think of these values as the size of your game window under perfect conditions for now. 800 by 800 is a fine example. Let’s do our resize test again. We’re always going to have the same aspect ratio. This “world” we defined will always be visible. The trouble is that we get these black borders on the side.

![Untitled_1 6 2](https://user-images.githubusercontent.com/60154347/170790293-7cd1988d-21ad-4d61-a594-0e9222f9c880.png)

It’s like trying to watch an old movie on a modern wide-screen TV. Monitors are no longer square.

## FillViewport

Choosing a viewport is all about compromise. FitViewport’s concession is adding black borders. FillViewport’s is cutting off part of the view to fill the entire screen.

![Untitled_1 7 1](https://user-images.githubusercontent.com/60154347/170790315-d15f9c28-13dc-4bfa-8c0f-85ef11d85df4.png)

```java
public class Core extends ApplicationAdapter {
    private TextureRegion textureRegion;
    private SpriteBatch spriteBatch;
    private FillViewport fillViewport;
    
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        Texture texture = new Texture(Gdx.files.internal("texture.png"));
        textureRegion = new TextureRegion(texture);
        fillViewport = new FillViewport(800, 800);
    }
    
    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        fillViewport.apply();
        spriteBatch.setProjectionMatrix(fillViewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(textureRegion, 0, 0);
        spriteBatch.end();
    }
    
    @Override
    public void resize(int width, int height) {
        fillViewport.update(width, height);
    }
    
    @Override
    public void dispose() {
        spriteBatch.dispose();
        textureRegion.getTexture().dispose();
    }
}
```

It maintains the same aspect ratio, but some of the world is going to be hidden.

## ExtendViewport

Neither one of these are completely ideal. That’s why a lot of people go with ExtendViewport for their games.

![Untitled_1 8 1](https://user-images.githubusercontent.com/60154347/170790328-ed7da677-668a-4f50-b4ea-e6a7eaa64dfa.png)

```java
public class Core extends ApplicationAdapter {
    private TextureRegion textureRegion;
    private SpriteBatch spriteBatch;
    private ExtendViewport extendViewport;
    
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        Texture texture = new Texture(Gdx.files.internal("texture-long.png"));
        textureRegion = new TextureRegion(texture);
        extendViewport = new ExtendViewport(800, 800);
        extendViewport.getCamera().position.set(800, 400, 0);
    }
    
    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        extendViewport.apply();
        spriteBatch.setProjectionMatrix(extendViewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(textureRegion, 0, 0);
        spriteBatch.end();
    }
    
    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height);
    }
    
    @Override
    public void dispose() {
        spriteBatch.dispose();
        textureRegion.getTexture().dispose();
    }
}
```

It keeps the aspect ratio like a FitViewport, but to avoid the black borders it extends the visible world to fill in the empty space. So, you’re not going to have full control of the view dimensions, but this is the best option for explorable worlds.

## ScreenViewport

The truth is you have to pick the best viewport for the circumstances of your game. And you don’t have to stick with one viewport either. You can have a different viewport for every screen. For example, your game screen could have an ExtendViewport and your menu could have a ScreenViewport.

![Untitled_1 9 1](https://user-images.githubusercontent.com/60154347/170790346-605a4b54-578c-4583-8baa-1382b55d18ff.png)

```java
public class Core extends ApplicationAdapter {
    private SpriteBatch spriteBatch;
    private ScreenViewport screenViewport;
    private Skin skin;
    private Stage stage;
    private Dialog dialog;
    
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        skin = new Skin(Gdx.files.internal("ui/Particle Park UI.json"));
        screenViewport = new ScreenViewport();
        stage = new Stage(screenViewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);
    
        dialog = new Dialog("Game Menu", skin);
        dialog.getTitleLabel().setAlignment(Align.center);
        Table table = dialog.getContentTable();
        table.pad(10);
        
        table.row();
        table.defaults().width(150);
        TextButton textButton = new TextButton("Play", skin);
        table.add(textButton);
        
        table.row();
        textButton = new TextButton("Options", skin);
        table.add(textButton);
        
        table.row();
        textButton = new TextButton("Quit", skin);
        table.add(textButton);
        dialog.show(stage);
    }
    
    @Override
    public void render() {
        ScreenUtils.clear(Color.DARK_GRAY);
        screenViewport.apply();
        stage.act();
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        screenViewport.update(width, height, true);
        dialog.setPosition(Math.round((stage.getWidth() - dialog.getWidth()) / 2), Math.round((stage.getHeight() - dialog.getHeight()) / 2));
    }
    
    @Override
    public void dispose() {
        spriteBatch.dispose();
        stage.dispose();
        skin.dispose();
    }
}
```

ScreenViewport is ideal for UI because it will always be pixel for pixel. No black borders and no stretching. If your window is 540x1080, your “world” would be 540x1080. With Scene2D’s ability to dynamically resize widgets with nine patches and the like, there’s really no need for any other viewport in this context. This is really the only way to avoid nasty blurring of fonts and edges. The exception to this is pixel art games, but those should be using advanced setups with framebuffers.

## Overlaying Multiple Viewports

How about a game screen with a user interface overlayed on the top? The best way to achieve this is by having two viewports at the same time.

![Untitled_1 10 1](https://user-images.githubusercontent.com/60154347/170790622-9d02984c-a2a1-422e-9b7a-ed3c29154d81.png)

```java
package com.ray3k.demonstration;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Core extends ApplicationAdapter {
    private SpriteBatch spriteBatch;
    private ExtendViewport extendViewport;
    private TextureRegion textureRegion;
    private ScreenViewport screenViewport;
    private Skin skin;
    private Stage stage;
    
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        Texture texture = new Texture(Gdx.files.internal("texture-long.png"));
        textureRegion = new TextureRegion(texture);
        extendViewport = new ExtendViewport(800, 800);
        extendViewport.getCamera().position.set(800, 400, 0);
        
        skin = new Skin(Gdx.files.internal("ui/Particle Park UI.json"));
        screenViewport = new ScreenViewport();
        stage = new Stage(screenViewport, spriteBatch);
        Gdx.input.setInputProcessor(stage);
    
        Table root = new Table();
        root.setFillParent(true);
        root.pad(10);
        stage.addActor(root);
        
        Table table = new Table();
        root.add(table).growX();
        
        table.defaults().space(5);
        Label label = new Label("Player Health:", skin);
        table.add(label);
        
        ProgressBar progressBar = new ProgressBar(0, 100, 1, false, skin);
        progressBar.setValue(50);
        table.add(progressBar).expandX().left();
        
        TextButton textButton = new TextButton("Menu", skin);
        table.add(textButton);
        
        root.row();
        table = new Table();
        table.setBackground(skin.getDrawable("highlight"));
        root.add(table).growX().expandY().bottom();
        
        ButtonGroup<CheckBox> buttonGroup = new ButtonGroup<>();
        table.defaults().space(20);
        CheckBox checkBox = new CheckBox("Option1", skin, "radio");
        table.add(checkBox);
        buttonGroup.add(checkBox);
    
        checkBox = new CheckBox("Option2", skin, "radio");
        table.add(checkBox);
        buttonGroup.add(checkBox);
    
        checkBox = new CheckBox("Option3", skin, "radio");
        table.add(checkBox);
        buttonGroup.add(checkBox);
    
        checkBox = new CheckBox("Option4", skin, "radio");
        table.add(checkBox);
        buttonGroup.add(checkBox);
    
        checkBox = new CheckBox("Option5", skin, "radio");
        table.add(checkBox);
        buttonGroup.add(checkBox);
    }
    
    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        extendViewport.apply();
        spriteBatch.setProjectionMatrix(extendViewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(textureRegion, 0, 0);
        spriteBatch.end();
        
        screenViewport.apply();
        stage.act();
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height);
        
        screenViewport.update(width, height, true);
    }
    
    @Override
    public void dispose() {
        spriteBatch.dispose();
        textureRegion.getTexture().dispose();
        stage.dispose();
        skin.dispose();
    }
}
```

You can, in fact, use as many viewports as you want simultaneously. You just have to make sure to apply each viewport before you start drawing things for that layer. So, I apply the ExtendViewport to draw my game world, then I apply my ScreenViewport to draw the Scene2D stage on top.

## World Units

Hey. Yeah. I’m talking to you. We’ve been avoiding the conversation about world units so far. Let’s talk. I like pixels. My monitor resolution is measured in pixels. My game graphics are made out of pixels. Coming from using the AWT Canvas from way back, pixels make sense to me. But your game world is not made up of pixels. A lot of you use libraries like Box2D to handle your physics and collision detection. Box2D uses meters. For scale, your player character could be just 1 meter tall.

![Untitled_1 17 1](https://user-images.githubusercontent.com/60154347/170790641-087df215-b5ca-47b6-81e4-a3b8e4e5a6b9.png)

This works because measurements are nice and small. You get more floating-point error when you increase the scale of your numbers. Box2D would simply implode if you tried to feed it pixel values. I have to admit, I use pixel units for my jam games and I always regret it when I’m defining gravity in tens of thousands of pixels per frame. Not good. Even if you don’t use Box2D, using meters or any sort of game unit that you define is also a lot simpler to work with and conceptualize.

Now some people come up with some crazy formulas to convert from pixel to meter. That is not necessary. You define your viewports in your world units. So, my example of a Viewport before: 800x800 pixels. For simplicity, I’ll just say 1 meter is 100 pixels, but you come up with your own scale that matches the elements of your game. This could be the size of your player or your tiles if you’re making a tile-based game. We’ll now create our new Viewport as 8x8 units. Then we’ll position the elements of our game in game units and the speed the player moves in units per frame.  

![Untitled_1 8 1](https://user-images.githubusercontent.com/60154347/170790328-ed7da677-668a-4f50-b4ea-e6a7eaa64dfa.png)

```java
public class Core extends ApplicationAdapter {
    private TextureRegion textureRegion;
    private SpriteBatch spriteBatch;
    private ExtendViewport extendViewport;
    
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        Texture texture = new Texture(Gdx.files.internal("texture-long.png"));
        textureRegion = new TextureRegion(texture);
        extendViewport = new ExtendViewport(8, 8);
        extendViewport.getCamera().position.set(8, 4, 0);
    }
    
    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        extendViewport.apply();
        spriteBatch.setProjectionMatrix(extendViewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(textureRegion, 0, 0, 16, 8);
        spriteBatch.end();
    }
    
    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height);
    }
    
    @Override
    public void dispose() {
        spriteBatch.dispose();
        textureRegion.getTexture().dispose();
    }
}
```

We functionally have the same game, but the units are a lot saner now.

## Projecting and Unprojecting

There are some things that cannot be measured in game units, however. Input for one thing is measured in pixels relative to the window. That’s fine because we can “unproject” the screen coordinates. There are two methods available to us: unproject transforms from screen coordinates to world units. 

```java
Vector2 vector2 = new Vector2(x, y);
gameViewport.unproject(vector2);
```

Project transforms from world units to screen coordinates.

```java
Vector2 vector2 = new Vector2(x, y);
gameViewport.project(vector2);
```

An analogy you can use for this is a movie projector. Your game lives in the projector on those tiny film frames. It is “projected” onto the big screen, or the game window in our case, for your audience to see. And when you have a point on the screen that needs to go back into the game, it needs to be “unprojected”.

## Camera Position

Yes, every viewport has a camera that you can use for project/unproject, but that camera is capable of so much more! This is what you manipulate during gameplay to change the position of the camera.

![Untitled_1 26 1](https://user-images.githubusercontent.com/60154347/170790832-d25a30c4-bc38-4bcd-ab2e-121b98386d46.png)

```java
public class Core extends ApplicationAdapter {
    private TextureRegion levelRegion;
    private TextureRegion playerRegion;
    private SpriteBatch spriteBatch;
    private FitViewport gameViewport;
    private float playerX;
    private float playerY;
    private static final float MOVE_SPEED = 150f;
    
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        Texture texture = new Texture(Gdx.files.internal("level.png"));
        levelRegion = new TextureRegion(texture);
        texture = new Texture(Gdx.files.internal("player.png"));
        playerRegion = new TextureRegion(texture);
        
        gameViewport = new FitViewport(800, 800);
    }
    
    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        
        if (Gdx.input.isKeyPressed(Keys.A)) {
            playerX -= MOVE_SPEED * delta;
        } else if (Gdx.input.isKeyPressed(Keys.D)) {
            playerX += MOVE_SPEED * delta;
        }
        
        if (Gdx.input.isKeyPressed(Keys.S)) {
            playerY -= MOVE_SPEED * delta;
        } else if (Gdx.input.isKeyPressed(Keys.W)) {
            playerY += MOVE_SPEED * delta;
        }
        
        gameViewport.getCamera().position.set(playerX, playerY, 0);
        
        ScreenUtils.clear(Color.BLACK);
        gameViewport.apply();
        spriteBatch.setProjectionMatrix(gameViewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(levelRegion, 0, 0);
        spriteBatch.draw(playerRegion, playerX, playerY);
        spriteBatch.end();
    }
    
    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
    }
    
    @Override
    public void dispose() {
        spriteBatch.dispose();
        levelRegion.getTexture().dispose();
    }
}
```

Now the view follows the player character. You can modify the movement to be a little smoother to be less jarring, but this all depends on the type of game you want to make.

## Screen Shake Effect

You can even use the camera to add a screen shake effect.

```java
package com.ray3k.demonstration;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;

public class Core extends ApplicationAdapter {
    private TextureRegion textureRegion;
    private SpriteBatch spriteBatch;
    private ExtendViewport extendViewport;
    private ScreenShake screenShake;
    
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        Texture texture = new Texture(Gdx.files.internal("zebra.png"));
        textureRegion = new TextureRegion(texture);
        extendViewport = new ExtendViewport(800, 800);
        extendViewport.getCamera().position.set(400, 400, 0);
        screenShake = new ScreenShake();
        screenShake.shake(20f, 1, 20, 20f, true);
    }
    
    @Override
    public void render() {
        ScreenUtils.clear(Color.WHITE);
        extendViewport.apply();
        spriteBatch.setProjectionMatrix(extendViewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(textureRegion, 0, 0);
        spriteBatch.end();
        screenShake.update(Gdx.graphics.getDeltaTime(), extendViewport.getCamera(), Gdx.graphics.getWidth(),
                Gdx.graphics.getHeight());
    }
    
    @Override
    public void resize(int width, int height) {
        extendViewport.update(width, height);
    }
    
    @Override
    public void dispose() {
        spriteBatch.dispose();
        textureRegion.getTexture().dispose();
    }
}
```

ScreenShake class by Peanut Panda:
```java
public class ScreenShake {
    float[] samples;
    Random rand = new Random();
    float internalTimer = 0;
    float shakeDuration = 0;
    
    int duration = 5; // In seconds, make longer if you want more variation
    int frequency = 35; // hertz
    float amplitude = 20; // how much you want to shake
    boolean falloff = true; // if the shake should decay as it expires
    
    int sampleCount;
    
    public ScreenShake() {
        sampleCount = duration * frequency;
        samples = new float[sampleCount];
        for (int i = 0; i < sampleCount; i++) {
            samples[i] = rand.nextFloat() * 2f - 1f;
        }
    }
    
    public void update(float dt, Camera camera, float screenWidth, float screenHeight) {
        internalTimer += dt;
        if (internalTimer > duration) internalTimer -= duration;
        if (shakeDuration > 0) {
            shakeDuration -= dt;
            float shakeTime = (internalTimer * frequency);
            int first = (int) shakeTime;
            int second = (first + 1) % sampleCount;
            int third = (first + 2) % sampleCount;
            float deltaT = shakeTime - (int) shakeTime;
            float deltaX = samples[first] * deltaT + samples[second] * (1f - deltaT);
            float deltaY = samples[second] * deltaT + samples[third] * (1f - deltaT);
            
            camera.position.x = screenWidth / 2 + deltaX * amplitude * (falloff ? Math.min(shakeDuration, 1f) : 1f);
            camera.position.y = screenHeight / 2 + deltaY * amplitude * (falloff ? Math.min(shakeDuration, 1f) : 1f);
            camera.update();
        }
    }
    
    /**
     * @param d  duration of the shake in seconds
     * @param d2 in seconds, make longer if you want more variation
     * @param f  hertz
     * @param a  how much you want to shake
     * @param fo if the shake should decay as it expires
     */
    public void shake(float d, int d2, int f, float a, boolean fo) {
        shakeDuration = d;
        duration = d2;
        frequency = f;
        amplitude = a;
        falloff = fo;
        
        
    }
}
```

## Camera Zoom

Another trick is to change the zoom of the camera. You can give the player direct control of the zoom so they can set it to a comfortable level.

![Untitled_1 28 1](https://user-images.githubusercontent.com/60154347/170790900-c7ffc468-c3c4-43a5-9e16-a839a349e14d.png)

```java
((OrthographicCamera) gameViewport.getCamera()).zoom = .5f;
```

![Untitled_1 28 2](https://user-images.githubusercontent.com/60154347/170790916-9a1075ab-f992-41f1-8f67-6fc723fd17bd.png)

```java
((OrthographicCamera) gameViewport.getCamera()).zoom = 2f;
```

Or you can have the gameplay define the zoom. In VTOL, I used a dynamic zoom based on the player’s speed so they can see obstacles ahead of them.

![Untitled_1 29 1](https://user-images.githubusercontent.com/60154347/170790931-397d88ee-37f7-452d-b69d-eaee27921cab.png)

![Untitled_1 29 2](https://user-images.githubusercontent.com/60154347/170790933-d7bf0f36-73b4-4a6e-afe2-b449ff42e1c1.png)

With control of the zoom and position, you can make your game more cinematic. You can add points of interest or wrest control from the player during the story breaks.

## Minimaps

You’ve got the basics of viewports down. Now let’s get crazy. You can use viewports for more than just your main view. You can use it for minimaps as well as a split screen. How this is done is by setting the position and size of an additional viewport and overlaying it on top of your main view for the minimap.

![Untitled_1 30 1](https://user-images.githubusercontent.com/60154347/170790978-f1014c29-9a9a-43fe-80c2-c6823999fe15.png)

```java
public class Core extends ApplicationAdapter {
    private TextureRegion levelRegion;
    private TextureRegion playerRegion;
    private TextureRegion minimapRegion;
    private TextureRegion playerMinimapRegion;
    private SpriteBatch spriteBatch;
    private ExtendViewport gameViewport;
    private FitViewport mapViewport;
    private float playerX;
    private float playerY;
    private static final float MOVE_SPEED = 150f;
    
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        Texture texture = new Texture(Gdx.files.internal("level.png"));
        levelRegion = new TextureRegion(texture);
        texture = new Texture(Gdx.files.internal("player.png"));
        playerRegion = new TextureRegion(texture);
        texture = new Texture(Gdx.files.internal("minimap.png"));
        minimapRegion = new TextureRegion(texture);
        texture = new Texture(Gdx.files.internal("player-minimap.png"));
        playerMinimapRegion = new TextureRegion(texture);
        
        gameViewport = new ExtendViewport(800, 800);
        gameViewport.getCamera().position.set(400, 400, 0);
        
        mapViewport = new FitViewport(800, 800);
        mapViewport.getCamera().position.set(400, 400, 0);
        mapViewport.setScreenBounds(Gdx.graphics.getWidth() - 120, 20, 100, 100);
    }
    
    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            playerX -= MOVE_SPEED * delta;
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            playerX += MOVE_SPEED * delta;
        }
    
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            playerY -= MOVE_SPEED * delta;
        } else if (Gdx.input.isKeyPressed(Keys.UP)) {
            playerY += MOVE_SPEED * delta;
        }
        
        ScreenUtils.clear(Color.BLACK);
        gameViewport.apply();
        spriteBatch.setProjectionMatrix(gameViewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(levelRegion, 0, 0);
        spriteBatch.draw(playerRegion, playerX, playerY);
        spriteBatch.end();
        
        mapViewport.apply();
        spriteBatch.setProjectionMatrix(mapViewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(minimapRegion, 0, 0);
        spriteBatch.draw(playerMinimapRegion, playerX, playerY);
        spriteBatch.end();
    }
    
    @Override
    public void resize(int width, int height) {
        gameViewport.update(width, height);
        mapViewport.setScreenBounds(width - 120, 20, 100, 100);
    }
    
    @Override
    public void dispose() {
        spriteBatch.dispose();
        levelRegion.getTexture().dispose();
    }
}
```

## Split Screen

And for the splitscreen, position and resize the two viewports side by side.

![Untitled_1 31 1](https://user-images.githubusercontent.com/60154347/170790957-fa7d73b7-07d4-428e-90af-58b989cbe475.png)

```java
public class Core extends ApplicationAdapter {
    private TextureRegion levelRegion;
    private TextureRegion playerRegion;
    private SpriteBatch spriteBatch;
    private CustomViewport player1Viewport;
    private CustomViewport player2Viewport;
    private float player1X;
    private float player1Y;
    private float player2X;
    private float player2Y;
    private static final float MOVE_SPEED = 150f;
    
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        Texture texture = new Texture(Gdx.files.internal("level.png"));
        levelRegion = new TextureRegion(texture);
        texture = new Texture(Gdx.files.internal("player.png"));
        playerRegion = new TextureRegion(texture);
        
        player1Viewport = new CustomViewport(true, 800, 800);
        ((OrthographicCamera)player1Viewport.getCamera()).zoom = .5f;
        player2Viewport = new CustomViewport(false, 800, 800);
        ((OrthographicCamera)player2Viewport.getCamera()).zoom = .5f;
    }
    
    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        
        if (Gdx.input.isKeyPressed(Keys.A)) {
            player1X -= MOVE_SPEED * delta;
        } else if (Gdx.input.isKeyPressed(Keys.D)) {
            player1X += MOVE_SPEED * delta;
        }
    
        if (Gdx.input.isKeyPressed(Keys.S)) {
            player1Y -= MOVE_SPEED * delta;
        } else if (Gdx.input.isKeyPressed(Keys.W)) {
            player1Y += MOVE_SPEED * delta;
        }
        
        player1Viewport.getCamera().position.set(player1X, player1Y, 0);
    
        if (Gdx.input.isKeyPressed(Keys.LEFT)) {
            player2X -= MOVE_SPEED * delta;
        } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
            player2X += MOVE_SPEED * delta;
        }
    
        if (Gdx.input.isKeyPressed(Keys.DOWN)) {
            player2Y -= MOVE_SPEED * delta;
        } else if (Gdx.input.isKeyPressed(Keys.UP)) {
            player2Y += MOVE_SPEED * delta;
        }
    
        player2Viewport.getCamera().position.set(player2X, player2Y, 0);
        
        ScreenUtils.clear(Color.BLACK);
        player1Viewport.apply();
        spriteBatch.setProjectionMatrix(player1Viewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(levelRegion, 0, 0);
        spriteBatch.draw(playerRegion, player1X, player1Y);
        spriteBatch.draw(playerRegion, player2X, player2Y);
        spriteBatch.end();
    
        player2Viewport.apply();
        spriteBatch.setProjectionMatrix(player2Viewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(levelRegion, 0, 0);
        spriteBatch.draw(playerRegion, player1X, player1Y);
        spriteBatch.draw(playerRegion, player2X, player2Y);
        spriteBatch.end();
    }
    
    @Override
    public void resize(int width, int height) {
        player1Viewport.update(width, height);
        player2Viewport.update(width, height);
    }
    
    @Override
    public void dispose() {
        spriteBatch.dispose();
        levelRegion.getTexture().dispose();
    }
    
    private static class CustomViewport extends Viewport {
        private boolean leftSide = true;
        
        public CustomViewport(boolean leftSide, float worldWidth, float worldHeight) {
            this(leftSide, worldWidth, worldHeight, new OrthographicCamera());
        }
    
        public CustomViewport (boolean leftSide, float worldWidth, float worldHeight, Camera camera) {
            this.leftSide = leftSide;
            setWorldSize(worldWidth, worldHeight);
            setCamera(camera);
        }
    
        public void update (int screenWidth, int screenHeight, boolean centerCamera) {
            Vector2 scaled = Scaling.fit.apply(getWorldWidth(), getWorldHeight(), screenWidth / 2f, screenHeight);
            int viewportWidth = Math.round(scaled.x);
            int viewportHeight = Math.round(scaled.y);
            
            setScreenBounds(leftSide ? 0 : viewportWidth, (screenHeight - viewportHeight) / 2, viewportWidth, viewportHeight);
        
            apply(centerCamera);
        }
    }
}
```

It’s that simple.

I think the gold standard for split screens is Lego Batman. And we can see here our resident genius Groxar made an excellent implementation in libGDX.

![Untitled_1 33 1](https://user-images.githubusercontent.com/60154347/170791077-287edb7a-f102-470e-ad7c-96c8a0993496.png)

This technique is much more involved and actually leans more on camera manipulation and masks. Make sure to watch our [video on masks](https://youtu.be/qDKmcNFFFng) and see [Groxar's code example](https://github.com/Raxorg/Alejandria/blob/master/core/src/main/java/com/epicness/alejandria/showcase/modules/viewports/AdvancedSplitScreen.java).

## Filling the "Black Border"

Remember that FitViewport had black borders? If you must use that viewport, they don’t necessarily have to be “black”. You can overlay it on top of another viewport with a pattern.

![Untitled_1 34 1](https://user-images.githubusercontent.com/60154347/170791117-a7cebbff-a708-44c3-abf4-079806fdb7ea.png)

```java
public class Core extends ApplicationAdapter {
    private TextureRegion textureRegion;
    private SpriteBatch spriteBatch;
    private FillViewport fillViewport;
    private FitViewport fitViewport;
    private TiledDrawable tiledDrawable;
    
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        
        Texture texture = new Texture(Gdx.files.internal("texture.png"));
        textureRegion = new TextureRegion(texture);
        
        texture = new Texture(Gdx.files.internal("pattern.png"));
        tiledDrawable = new TiledDrawable(new TextureRegion(texture));
        
        fillViewport = new FillViewport(800, 800);
        fitViewport = new FitViewport(800, 800);
    }
    
    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        
        fillViewport.apply();
        spriteBatch.setProjectionMatrix(fillViewport.getCamera().combined);
        spriteBatch.begin();
        tiledDrawable.draw(spriteBatch, 0, 0, 800, 800);
        spriteBatch.end();
        
        fitViewport.apply();
        spriteBatch.setProjectionMatrix(fitViewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(textureRegion, 0, 0);
        spriteBatch.end();
    }
    
    @Override
    public void resize(int width, int height) {
        fillViewport.update(width, height, true);
        fitViewport.update(width, height, true);
    }
    
    @Override
    public void dispose() {
        spriteBatch.dispose();
        textureRegion.getTexture().dispose();
    }
}
```

Or better yet, fill it with relevant player statistics.

![Untitled_1 35 1](https://user-images.githubusercontent.com/60154347/170791139-0016441b-78e1-4f29-be55-ba0c5a852c47.png)

```java
public class Core extends ApplicationAdapter {
    private TextureRegion textureRegion;
    private TextureRegion soldierRegion;
    private TextureRegion healthRegion;
    private TextureRegion ammoRegion;
    private SpriteBatch spriteBatch;
    private ScreenViewport leftViewport;
    private ScreenViewport rightViewport;
    private FitViewport fitViewport;
    private Skin skin;
    private Stage leftStage;
    private Stage rightStage;
    
    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        
        Texture texture = new Texture(Gdx.files.internal("texture.png"));
        textureRegion = new TextureRegion(texture);
        
        texture = new Texture(Gdx.files.internal("infantry.png"));
        soldierRegion = new TextureRegion(texture);
    
        texture = new Texture(Gdx.files.internal("health.png"));
        healthRegion = new TextureRegion(texture);
    
        texture = new Texture(Gdx.files.internal("ammo.png"));
        ammoRegion = new TextureRegion(texture);
        
        skin = new Skin(Gdx.files.internal("ui/Particle Park UI.json"));
        
        leftViewport = new ScreenViewport();
        leftStage = new Stage(leftViewport);
        
        rightViewport = new ScreenViewport();
        rightStage = new Stage(rightViewport);
        
        fitViewport = new FitViewport(800, 800);
    
        Table root = new Table();
        root.setFillParent(true);
        root.pad(10);
        leftStage.addActor(root);
    
        root.defaults().space(10);
        TextureRegionDrawable drawable = new TextureRegionDrawable(soldierRegion);
        Image image = new Image(drawable);
        image.setScaling(Scaling.fit);
        root.add(image);
        
        root.row();
        Label label = new Label("INFANTRY", skin);
        root.add(label);
        
        root.row();
        drawable = new TextureRegionDrawable(healthRegion);
        image = new Image(drawable);
        image.setScaling(Scaling.fit);
        root.add(image);
    
        root = new Table();
        root.setFillParent(true);
        root.pad(10);
        rightStage.addActor(root);
    
        root.defaults().space(10);
        drawable = new TextureRegionDrawable(ammoRegion);
        image = new Image(drawable);
        image.setScaling(Scaling.fit);
        root.add(image);
    
        root.row();
        label = new Label("x23", skin);
        root.add(label);
        
        root.row();
        label = new Label("Primary Objective: Proceed to main living space and erradicate all rodents", skin);
        label.setWrap(true);
        root.add(label).growX();
    }
    
    @Override
    public void render() {
        ScreenUtils.clear(Color.DARK_GRAY);
        
        leftStage.act();
        rightStage.act();
        
        leftViewport.apply();
        leftStage.draw();
        
        rightViewport.apply();
        rightStage.draw();
        
        fitViewport.apply();
        spriteBatch.setProjectionMatrix(fitViewport.getCamera().combined);
        spriteBatch.begin();
        spriteBatch.draw(textureRegion, 0, 0);
        spriteBatch.end();
    }
    
    @Override
    public void resize(int width, int height) {
        fitViewport.update(width, height, true);
        leftViewport.update(fitViewport.getLeftGutterWidth(), height, true);
        leftViewport.setScreenPosition(0, 0);
        rightViewport.update(fitViewport.getRightGutterWidth(), height, true);
        rightViewport.setScreenPosition(fitViewport.getRightGutterX(), 0);
    }
    
    @Override
    public void dispose() {
        spriteBatch.dispose();
        textureRegion.getTexture().dispose();
        ammoRegion.getTexture().dispose();
        soldierRegion.getTexture().dispose();
        healthRegion.getTexture().dispose();
        skin.dispose();
    }
}
```

Make the gutter part of the game.

## Conclusion

That’s viewports in a nutshell. Like I said, there isn’t much to them once you get the basics down. Good luck on your aspect ratio and black bar adventures!

Please see the following pages for further information:  
https://libgdx.com/wiki/graphics/viewports  
https://libgdx.com/wiki/graphics/2d/orthographic-camera  
https://raeleus.github.io/viewports-sample-project/

A very special thanks to Groxar, Lyze, and Peanut Panda for their contributions to this guide.
