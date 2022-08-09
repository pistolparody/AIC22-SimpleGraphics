package com.yolo.simplegraphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;
import java.security.Key;

import static com.yolo.simplegraphics.SimpleGraphics.*;

public class EventHandler implements InputProcessor
{
    @Override
    public boolean keyDown(int keycode) {
        if (keycode== Input.Keys.SHIFT_LEFT)
        {
            heldKey=true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode==Input.Keys.SHIFT_LEFT)
        {
            heldKey=false;
        }

        if (keycode== Input.Keys.ESCAPE)
        {
            Gdx.app.exit();
            return true;
        }

        if (agentsAreMoving){
            System.out.println("Can't take commands while the agents are moving!");
            return false;
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
        else if(keycode==Input.Keys.NUMPAD_1)
        {
            moveSpeed=0.01f;
            System.out.println("Agents movement speed is now 1X");
        }
        else if(keycode==Input.Keys.NUMPAD_2)
        {
            moveSpeed=0.02f;
            System.out.println("Agents movement speed is now 2X");
        }
        else if(keycode==Input.Keys.NUMPAD_3)
        {
            moveSpeed=0.04f;
            System.out.println("Agents movement speed is now 4X");
        }
        else if(keycode==Input.Keys.NUMPAD_4)
        {
            moveSpeed=0.08f;
            System.out.println("Agents movement speed is now 8X");
        }
        else if(keycode==Input.Keys.NUMPAD_5)
        {
            moveSpeed=0.16f;
            System.out.println("Agents movement speed is now 16X");
        }
        else if(keycode== Input.Keys.UP)
        {
            moveSpeed+=0.005f;
            System.out.println("Agents movement speed is now "+(moveSpeed*100f)+"X");

        }
        else if(keycode==Input.Keys.DOWN)
        {
            if (moveSpeed-0.005>=0){
                moveSpeed-=0.005f;
            }

            System.out.println("Agents movement speed is now "+(moveSpeed*100f)+"X");
        }

        textRect.update(getGameState());


        return false;
    }

    @Override
    public boolean keyTyped(char character) {

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button==Input.Buttons.LEFT){mouseHeld=true;}

        if (agentsAreMoving){
            System.out.println("Can't take commands while the agents are moving!");
            return false;
        }

        if (!heldKey)
        {
            if (button==Input.Buttons.FORWARD)
            {
                if (currentTurn+1<=maxTurnNumber){
                    currentTurn++;
                    GameEvent.TURN_CHANGE.updateMap(currentTurn);
                }
                textRect.update(getGameState());

            }
            else if(button==Input.Buttons.BACK)
            {
                if (currentTurn-1>=0){
                    currentTurn--;
                    GameEvent.TURN_CHANGE.updateMap(currentTurn);
                }
                textRect.update(getGameState());
            }
        }
        else
        {
            if (button==Input.Buttons.FORWARD)
            {
                moveSpeed+=(moveSpeed*0.1f+0.005f);
                System.out.println("Agents movement speed is now "+(moveSpeed*100f)+"X");
                textRect.update(getGameState());

            }
            else if(button==Input.Buttons.BACK)
            {
                if (moveSpeed-(moveSpeed*0.1f+0.005f)>=0){
                    moveSpeed-=(moveSpeed*0.01f+0.005f);
                }

                System.out.println("Agents movement speed is now "+(moveSpeed*100f)+"X");
                textRect.update(getGameState());

            }
        }


        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (button==Input.Buttons.LEFT){mouseHeld=false;}
        mouseVector2.x=screenX; mouseVector2.y=screenY;
        uiMouseVector2 = new Vector2(mouseVector2);

        viewport.unproject(mouseVector2);
        uiViewport.unproject(uiMouseVector2);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        mouseVector2.x=screenX; mouseVector2.y=screenY;
        uiMouseVector2 = new Vector2(mouseVector2);

        viewport.unproject(mouseVector2);
        uiViewport.unproject(uiMouseVector2);

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
