package com.yolo.simplegraphics;




import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

import static com.yolo.simplegraphics.SimpleGraphics.*;

public class Node
{
    private final int id;
    private final float x;
    private final float y;
    private int connectedNodesCount=0;

    Node(int p_nodeId,float p_x,float p_y)
    {
        id=p_nodeId;
        x=p_x;
        y=p_y;
    }

    public int getId() {return id;}
    public float getX() {return x;}
    public float getY() {return y;}

    public void fillConnectedNodes(List<Edge> edgeList)
    {
        for (int i=0;i!=edgeList.size();i++)
        {
            if (edgeList.get(i).includesNode(id))
            {
                connectedNodesCount++;
            }
        }
    }

    public Vector2 getProjectedVector2()
    {
        Rectangle rect = Node.getCoordinationRectangle();
        float scaleX = winWidth/rect.width*0.9f;
        float scaleY = winHeight/rect.height*0.9f;
        return  new Vector2(x*scaleX,y*scaleY);
    }

    public void draw()
    {

        Vector2 location = getProjectedVector2();
        shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.setColor(Color.BLACK);
        shape.circle(location.x, location.y, 1.3f,20);
        shape.end();
    }

    public String toString() {return getString(this);}

    // STATICS

    private static float minX=0;
    private static float minY=0;
    private static float maxX=0;
    private static float maxY=0;

    public static Rectangle getCoordination() {return new Rectangle(minX,minY,maxX,maxY);}
    public static Rectangle getCoordinationRectangle(){return new Rectangle(0,0,maxX-minX,maxY-minY);}

    public static void fillCoordination(List<Node> p_everyNode)
    {
        for (int i=0;i!=p_everyNode.size();i++)
        {
            if (p_everyNode.get(i).getX()<Node.minX) {minX = p_everyNode.get(i).getX();}
            if (p_everyNode.get(i).getX()>Node.maxX) {maxX = p_everyNode.get(i).getX();}
            if (p_everyNode.get(i).getY()<Node.minY) {minY = p_everyNode.get(i).getY();}
            if (p_everyNode.get(i).getY()>Node.maxY) {maxY = p_everyNode.get(i).getY();}
        }
    }

    public static void fillEveryCNC()
    {
        for (int i=0;i!=everyNode.size();i++)
        {
            everyNode.get(i).fillConnectedNodes(everyEdge);
        }
    }

    public int getConnectedNodesCount() {
        return connectedNodesCount;
    }

    public static Node findNodeById(int p_nodeId)
    {
        Payam += "<"+p_nodeId+">";
        for (int i=0;i!=everyNode.size();i++)
        {
            if (everyNode.get(i).getId()==p_nodeId)
            {
                return everyNode.get(i);
            }
        }

        System.out.println("Warning, Could not find node "+p_nodeId+" in "+Payam);
//        errorsList.add("Warning, Could not find node "+p_nodeId+" in "+Payam);
        return everyNode.get(0);
//        writeLogs();
//        throw new Error("Could not find the requested node " + p_nodeId + " " + Payam);
    }

    public static String getString(Node p_node)
    {
        return "id: "+p_node.getId()+" x:"+p_node.getX()+" y: "+p_node.getY();
    }

    public static void drawAll(List<Node> p_nodeList)
    {
        for (int i=0;i!=p_nodeList.size();i++)
        {
            p_nodeList.get(i).draw();
        }

    }

    public static Vector2 getProjectedVector2(Vector2 vector2)
    {
        Rectangle rect = Node.getCoordinationRectangle();
        float scaleX = winWidth/rect.width*0.9f;
        float scaleY = winHeight/rect.height*0.9f;
        return  new Vector2(vector2.x*scaleX,vector2.y*scaleY);
    }

}
