package com.yolo.simplegraphics;

import com.badlogic.gdx.math.Vector2;

import java.util.List;

import static com.yolo.simplegraphics.SimpleGraphics.*;

public class Agent
{

    enum TEAM
    {
        FIRST("FIRST"),
        SECOND("SECOND");

        public final String text;
        TEAM(String text){this.text=text;}
    }
    enum TYPE
    {
        POLICE("POLICE"),
        THIEF("THIEF");

        public final String text;
        TYPE(String text){this.text=text;}
    }


    private double balance;
    private int nodeId;

    public final int id;
    public final TEAM team;
    public final TYPE type;

    Agent(int id,TEAM team,TYPE type,int nodeId,double balance)
    {
        this.balance = balance;
        this.nodeId = nodeId;
        this.id = id;
        this.team = team;
        this.type = type;
    }




    public void draw()
    {
        Vector2 point = Node.findNodeById(nodeId).getProjectedVector2();

        if (type== TYPE.THIEF){
            thiefSprite.setCenter(point.x,point.y+thiefSprite.getBoundingRectangle().height/2);
            thiefSprite.setScale(0.03f);
            thiefSprite.draw(batch);
//            batch.draw(thiefSprite,point.x,point.y);
        }
        else
        {
            copSprite.setCenter(point.x,point.y+copSprite.getBoundingRectangle().height/2);
            copSprite.setScale(0.03f);
            copSprite.draw(batch);
//            batch.draw(copSprite,point.x,point.y);
        }
    }

    public String toString() {return getString(this);}
    public int getNodeId() {return nodeId;}
    public double getBalance() {return balance;}




    public static String getString(Agent agent)
    {
        return "<team:"+agent.team.text+"> <type:"+agent.type.text+"> <id:"+agent.id+"> <nodeId:"+agent.getNodeId()+"> <balance:"+agent.getBalance()+">";
    }

    public static void drawAll(List<Agent> p_agentList)
    {
        for (int i=0;i!=p_agentList.size();i++)
        {
            p_agentList.get(i).draw();
        }
    }
}
