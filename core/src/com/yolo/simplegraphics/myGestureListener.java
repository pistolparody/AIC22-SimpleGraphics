package com.yolo.simplegraphics;

import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;

import static com.badlogic.gdx.scenes.scene2d.InputEvent.Type.keyTyped;
import static com.yolo.simplegraphics.SimpleGraphics.*;
import static com.yolo.simplegraphics.SimpleGraphics.camera;

public class myGestureListener implements GestureDetector.GestureListener
{
    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        return false;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {


        float scaleX = winWidth / winCurrentWidth;
        float scaleY = winHeight / winCurrentHeight;
        Vector2 rel = new Vector2((int)(-deltaX * scaleX * camera.zoom), (int)(deltaY * scaleY * camera.zoom));
        rel.x*=0.6f;
        rel.y*=0.6f;

        Vector2 newPos = new Vector2(rel.x+camera.position.x,rel.y+camera.position.y);

        if ((newPos.x<-winWidth/2)||(newPos.x>winWidth/2)||(newPos.y<-winHeight/2)||(newPos.y>winHeight/2)) {return false;}


        camera.translate(rel.x,rel.y);
        viewport.update(winCurrentWidth,winCurrentHeight);
            batch.setProjectionMatrix(camera.combined);
            shape.setProjectionMatrix(camera.combined);

        return true;
    }

    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
