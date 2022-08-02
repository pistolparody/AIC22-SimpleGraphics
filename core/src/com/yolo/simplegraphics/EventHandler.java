package com.yolo.simplegraphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;

import static com.yolo.simplegraphics.SimpleGraphics.*;

public class EventHandler implements InputProcessor
{
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {


        if (keycode== Input.Keys.ESCAPE)
        {
            Gdx.app.exit();
            return true;
        }

        if (keycode==Input.Keys.RIGHT)
        {
            if (currentTurn+1<=maxTurnNumber){
                currentTurn++;
                GameEvent.TURN_CHANGE.updateMap(currentTurn);
            }

            }
        else if(keycode==Input.Keys.LEFT)
        {
            if (currentTurn-1>=0){
                currentTurn--;
                GameEvent.TURN_CHANGE.updateMap(currentTurn);
            }
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button==Input.Buttons.LEFT){mouseHeld=true;}

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button==Input.Buttons.LEFT){mouseHeld=false;}
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseVector2.x=screenX; mouseVector2.y=screenY;
        viewport.unproject(mouseVector2);
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {

        if (amountY>0)
        {
            if(camera.zoom+camera.zoom*0.1f<1.5){
                camera.zoom+=camera.zoom*0.1f;
            }
        }
        else if(amountY<0)
        {
            if(camera.zoom-camera.zoom*0.1f>0.25){
                camera.zoom-=camera.zoom*0.1f;
            }
        }

        viewport.apply();
        viewport.update(winCurrentWidth,winCurrentHeight);
        batch.setProjectionMatrix(camera.combined);
        shape.setProjectionMatrix(camera.combined);

        return false;
    }
}
