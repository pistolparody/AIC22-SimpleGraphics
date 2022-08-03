package com.yolo.simplegraphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class TextRect {
    BitmapFont font;
    Rectangle rect;
    Color color;
    String text;

    Color[] debugColorArray;
    Vector2 center;
    GlyphLayout glyphLayout;
    int debugColorIndex;

    private float maxWidth=0;
    private float maxHeight=0;
    private float scale;

    TextRect(BitmapFont p_font, String p_text, Vector2 p_center, Color p_color){
        font = p_font;
        text = p_text;
        color = p_color;
        center = p_center;

        setDebugColors(Color.LIGHT_GRAY,Color.DARK_GRAY);

    }

    public void setDebugColors(Color color1,Color color2){

        debugColorArray = new Color[2];
        debugColorArray[0] = new Color(color1);
        debugColorArray[1] = new Color(color2);
    }

    public void setMaxWidth(float p_maxWidth){
        maxWidth=p_maxWidth;
    }

    public void setMaxHeight(float maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void init(){
        debugColorIndex=0;

        font.getData().setScale(1);
        font.setColor(color);
        glyphLayout = new GlyphLayout(font,text);

        if ((glyphLayout.width>maxWidth)||(glyphLayout.height>maxHeight)){
            debugColorIndex=1;

            scale = maxWidth/glyphLayout.width;
            if ((maxWidth==0))
            {
                scale = maxHeight/glyphLayout.height;
            }
            font.getData().setScale(scale);
            glyphLayout = new GlyphLayout(font,text);
            font.getData().setScale(1);
        }
        else{
            scale = 1;
        }


        rect = new Rectangle( center.x-glyphLayout.width/2 , center.y-glyphLayout.height/2 ,
                glyphLayout.width , glyphLayout.height);
    }

    public void recenter(Vector2 p_center){
        center= p_center;
        rect = new Rectangle( center.x-glyphLayout.width/2 , center.y-glyphLayout.height/2 ,
                glyphLayout.width , glyphLayout.height);
    }

    public void update(String p_text){
        text = p_text;
        init();
    }

    public void update(String p_text,Color p_color){
        text = p_text;
        color = p_color;
        init();
    }

    public void update(Color p_color){
        color = p_color;

    }


    public void render(SpriteBatch p_batch){


        font.getData().setScale(scale);
        font.draw(p_batch,glyphLayout, rect.x,rect.y+rect.height);
        font.getData().setScale(1);

    }

    public void renderDebug(ShapeRenderer p_shape){

        p_shape.begin(ShapeRenderer.ShapeType.Filled);
        p_shape.setColor(debugColorArray[debugColorIndex]);
        p_shape.rect(rect.x,rect.y,rect.width,rect.height);
        p_shape.end();
    }


    public static BitmapFont BitmapFontCopy (BitmapFont p_font) {
        // we use this only to change markdown at this point
        BitmapFont.BitmapFontData srcData = p_font.getData();
        BitmapFont.BitmapFontData dstData = new BitmapFont.BitmapFontData();
        // wtb copy constructor
        dstData.imagePaths = srcData.imagePaths;
        dstData.fontFile = srcData.fontFile;
        dstData.flipped = srcData.flipped;
        dstData.padTop = srcData.padTop;
        dstData.padRight = srcData.padRight;
        dstData.padBottom = srcData.padBottom;
        dstData.padLeft = srcData.padLeft;
        dstData.lineHeight = srcData.lineHeight;
        dstData.capHeight = srcData.capHeight;
        dstData.ascent = srcData.ascent;
        dstData.descent = srcData.descent;
        dstData.down = srcData.down;
        dstData.blankLineScale = srcData.blankLineScale;
        dstData.scaleX = srcData.scaleX;
        dstData.scaleY = srcData.scaleY;
        dstData.markupEnabled = srcData.markupEnabled;
        dstData.cursorX = srcData.cursorX;

        for (int i = 0, n = srcData.glyphs.length; i < n; i++) {
            dstData.glyphs[i] = srcData.glyphs[i];
        }
        dstData.missingGlyph = srcData.missingGlyph;
        dstData.spaceXadvance = srcData.spaceXadvance;
        dstData.xHeight = srcData.xHeight;

        dstData.breakChars = srcData.breakChars;
        dstData.xChars = srcData.xChars;
        dstData.capChars = srcData.capChars;

        return new BitmapFont(dstData, p_font.getRegions(), p_font.usesIntegerPositions());
    }
}
