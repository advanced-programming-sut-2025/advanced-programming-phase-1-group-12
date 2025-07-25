package org.example.models;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class CrowAnimation {
    public float x;
    public float y;
    float duration;
    public TextureRegion currentFrame;
    Animation<TextureRegion> crowAnimation;
    boolean isFinished = false;

    public CrowAnimation(float x, float y, Animation<TextureRegion> crowAnimation) {
        this.x = x;
        this.y = y;
        this.duration = 5f;  // Crow animation duration in seconds
        this.crowAnimation = crowAnimation;
        this.currentFrame = crowAnimation.getKeyFrame(0);
    }

    public void update(float delta) {
        duration -= delta;
        if (duration <= 0) {
            isFinished = true;
        } else {
            currentFrame = crowAnimation.getKeyFrame(5f - duration); // Update crow animation frame
        }
    }

    public boolean isFinished() {
        return isFinished;
    }
}
