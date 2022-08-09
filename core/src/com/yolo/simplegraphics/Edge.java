package com.yolo.simplegraphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import static com.yolo.simplegraphics.SimpleGraphics.*;

public class Edge
{
    private final int type;

    private final int node1Id;
    private final int node2Id;

    Edge(int p_node1Id,int p_node2Id,int p_edgeType)
    {

        type = p_edgeType;

        node1Id = p_node1Id;
        node2Id = p_node2Id;
    }

    public void draw()
    {
        shape.begin(ShapeRenderer.ShapeType.Filled);
        if (type==0)
        {
            shape.setColor(Color.GRAY);
        }
        else if(type==1)
        {
            shape.setColor(Color.GOLDENROD);
        }
        else if(type==2)
        {
            shape.setColor(Color.FIREBRICK);
        }
        else{
            shape.setColor(Color.SKY);
        }

        Payam = " ";
        Payam += this.toString()+" ";
        Vector2 firstNodeVector2 = Node.findNodeById(node1Id).getProjectedVector2();
        Payam += this.toString();

        Vector2 secondNodeVector2 = Node.findNodeById(node2Id).getProjectedVector2();


        shape.line(firstNodeVector2,secondNodeVector2);

        shape.end();
    }


    public String toString() {return getString(this);}
    public int getNode1Id() {return node1Id;}
    public int getNode2Id(){return node2Id;}
    public int getType() {return type;}


    public boolean includesNode(int nodeId) {return (node1Id==nodeId)||(node2Id==nodeId);}

    public static String getString(Edge p_edge)
    {
        return "node1Id: "+p_edge.getNode1Id()+" node2Id: "+p_edge.getNode2Id()+" type: "+p_edge.type;
    }

    public static void drawAll(List<Edge> p_edgeList)
    {
        for (int i=0;i!=p_edgeList.size();i++)
        {
            p_edgeList.get(i).draw();
        }
    }

}
