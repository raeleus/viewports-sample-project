package com.ray3k.demonstration;

import com.badlogic.gdx.graphics.Camera;

import java.util.Random;

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
    
    public void update(float dt, Camera camera) {
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
            
//            camera.position.x = ScreenSize.WIDTH.getSize() / 2 + deltaX * amplitude * (falloff ? Math.min(shakeDuration,
//                    1f) : 1f);
//            camera.position.y = ScreenSize.HEIGHT.getSize() / 2 + deltaY * amplitude * (falloff ? Math.min(
//                    shakeDuration, 1f) : 1f);
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