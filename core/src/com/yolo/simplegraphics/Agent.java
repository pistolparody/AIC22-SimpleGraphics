package com.yolo.simplegraphics;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.sun.org.apache.xerces.internal.xs.ItemPSVI;

import java.util.ArrayList;
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
        POLICE("POLICE","POLICE_TURN"),
        THIEF("THIEF","THIEF_TURN");

        public final String text;
        public final String turnText;
        TYPE(String text, String turnText){this.text=text;this.turnText=turnText;}
    }

    public static class STATE
    {
        public final int nodeId;
        public final double balance;
        public final int turnNumber;
        public final boolean isVisible;
        public final boolean isDead;

        STATE(int nodeId,double balance,int turnNumber,boolean isVisible,boolean isDead)
        {
            this.nodeId=nodeId; this.balance=balance;
            this.turnNumber=turnNumber; this.isVisible=isVisible;
            this.isDead=isDead;
        }


        public String toString(){return getString(this);}

        public static String getString(STATE state)
        {
            return "<nodeId:"+ state.nodeId+"> <turnNumber:"+state.turnNumber+"> <balance"+ state.balance+">";
        }


    }


    private double balance;
    private int nodeId;
    private boolean isDead;
    private static boolean isVisible;


    public final int id;
    public final TEAM team;
    public final TYPE type;


    
    public List<STATE> agentStateList;

    Agent(int id,TEAM team,TYPE type,int nodeId,double balance)
    {
        this.balance = balance;
        this.nodeId = nodeId;
        this.id = id;
        this.team = team;
        this.type = type;
        isVisible = true;
        isDead = false;
    }


    public void fillAgentMovementList(List<GameEvent.AGENT_MOVEMENT> everyAgentMovement)
    {
        agentStateList = new ArrayList<>();

        agentStateList.add(new STATE(nodeId,balance,
                0,false,false));
        if (type==TYPE.POLICE){
            agentStateList.add(new STATE(nodeId,balance,
                    1,true,false));
        }

        boolean isVisible0=false;
        for (int i=0;i!=everyAgentMovement.size();i++)
        {
            if (everyAgentMovement.get(i).agentId==id)
            {
                try{
                    isVisible0=GameEvent.TURN_CHANGE.findTurnByTurnNumber(everyAgentMovement.get(i).turnNumber).isVisible;
                }catch (Error e){
                    errorsList.add(e.toString());
                }
                agentStateList.add(new STATE(everyAgentMovement.get(i).toNodeId,everyAgentMovement.get(i).balance,
                                                everyAgentMovement.get(i).turnNumber,
                        isVisible0,false));
                agentStateList.add(new STATE(everyAgentMovement.get(i).toNodeId,everyAgentMovement.get(i).balance,
                        everyAgentMovement.get(i).turnNumber+1,
                        isVisible0,false));
            }
        }
    }

    public boolean update(int turnNumber)
    {
        for (int i=0;i!=agentStateList.size();i++)
        {
            if (agentStateList.get(i).turnNumber==turnNumber)
            {
                nodeId = agentStateList.get(i).nodeId;
                balance = agentStateList.get(i).balance;
                if (type==TYPE.THIEF){
                    isVisible = agentStateList.get(i).isVisible;
                }

                isDead = agentStateList.get(i).isDead;
                return true;
            }
        }

        System.out.println("MovementNotFound: movement number "+turnNumber+" was not found for agent "+id+" in team "+team.text+ " and type "+type);
//        throw new Error("MovementNotFound: movement number "+turnNumber+" was not found for agent "+id+" in team "+team.text+ "and type "+type);
        return false;
    }


    public boolean draw()
    {


        Vector2 point = Node.findNodeById(nodeId).getProjectedVector2();
        Sprite tempSprite;
        float posX=point.x;




        if (type==TYPE.THIEF) {
            if (team==TEAM.FIRST) {tempSprite=redThiefSprite;}
            else {tempSprite=blueThiefSprite;}
        }
        else {
            if (team==TEAM.FIRST) {tempSprite=redCopSprite;}
            else {tempSprite=blueCopSprite;}
        }

        float idealHeight = winHeight*0.1f*camera.zoom;
        float currentHeight = tempSprite.getHeight();

        float newWidthScale = idealHeight / currentHeight;

        if (team==TEAM.FIRST)
        {
         posX-=tempSprite.getBoundingRectangle().width*0.125f;
        }
        else
        {
            posX+=tempSprite.getBoundingRectangle().width*0.125f;
        }


//        if ((type==TYPE.THIEF)&&(!isVisible))
//        {
        if (this.isDead&&(type==TYPE.THIEF)){return false;}
        if (!this.isVisible&&(type==TYPE.THIEF))
        {
            tempSprite.setAlpha(0.5f);
        }
        else
        {
            tempSprite.setAlpha(1);
        }
//        }


        tempSprite.setScale(newWidthScale);

        tempSprite.setCenter(posX,point.y+tempSprite.getBoundingRectangle().height/2);

        tempSprite.draw(batch);

        return true;
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

    public static void updateAgents(int turnNumber)
    {
        for (int i=0;i!=everyAgent.size();i++)
        {
            everyAgent.get(i).update(turnNumber);
        }
    }
}