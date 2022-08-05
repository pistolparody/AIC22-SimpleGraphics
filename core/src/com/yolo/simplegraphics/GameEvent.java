package com.yolo.simplegraphics;

import java.util.ArrayList;
import java.util.List;

import static com.yolo.simplegraphics.SimpleGraphics.agentsAreMoving;
import static com.yolo.simplegraphics.SimpleGraphics.everyAgent;

// ------ GAME EVENTS ------ \\
// STATUS_CHANGE
    // timeStamp
    // toStatus
    // fromStatus
// GAME_RESULT_CHANGED
    // timeStamp
    // game result
// TURN_CHANGE
    // timeStamp
    // toTurnNumber
    // isVisible
    // toTurn
// ------ AGENT EVENTS ------ \\
// AGENT_MOVEMENT
    // timeStamp
    // agentId
    // balance
    // price
    // toNodeId
    // fromNodeId
// AGENT_BALANCE_CHARGED
    // timeStamp
    // agentId
    // balance
    // wage
// POLICE_CAUGHT_THIEVES
    // timeStamp
    // thiefId
    // nodeId
public class GameEvent
{

    public static class STATUS_CHANGE
    {
        final String timeStamp;
        final String fromStatus;
        final String toStatus;

        public static final String text = "STATUS_CHANGE";
        public static List<STATUS_CHANGE> statusChangeList;

        STATUS_CHANGE(String timeStamp,String fromStatus,String toStatus)
        {
            this.timeStamp=timeStamp;
            this.fromStatus=fromStatus;
            this.toStatus=toStatus;
        }


        public String toString(){return getString(this);}
        public static String getString(STATUS_CHANGE status_change)
        {
            return "<fromStatus:"+ status_change.fromStatus+"> <toStatus:"+ status_change.toStatus+"> <timeStamp:"+ status_change.timeStamp+">";
        }

    }

    public static class GAME_RESULT_CHANGED
    {

    }

    public static class TURN_CHANGE
    {
        final String timeStamp;
        final int toTurnNumber;
        final boolean isVisible;

        final Agent.TYPE toTurn;

        public final static String text = "TURN_CHANGE";
        public static List<TURN_CHANGE> turnChangeList;

        TURN_CHANGE
                (
                        String timeStamp,
                        int toTurnNumber,
                        boolean isVisible,

                        Agent.TYPE toTurn
                )
        {
            this.timeStamp = timeStamp;
            this.toTurnNumber = toTurnNumber;
            this.isVisible = isVisible;
            this.toTurn = toTurn;

        }

        public static TURN_CHANGE findTurnByTurnNumber(int toTurnNumber)
        {
            for (int i=0;i!=TURN_CHANGE.turnChangeList.size();i++)
            {
                if (turnChangeList.get(i).toTurnNumber==toTurnNumber)
                {
                    return turnChangeList.get(i);
                }
            }

            throw new Error("TurnChangeNotFoundException: Could not find turn change by turn number : "+toTurnNumber);
        }

        public String toString(){return getString(this);}
        public static String getString(TURN_CHANGE turn_change)
        {
            return "< TURN_CHANGE: <toTurnNumber:"+turn_change.toTurnNumber+"> <toTurn:"
                    +turn_change.toTurn.turnText+"> <isVisible:"+turn_change.isVisible+
                            "> <timeStamp:"+ turn_change.timeStamp+"> >";
        }

        public static boolean updateMap(int turnNumber)
        {

            SimpleGraphics.isHoovering=false;
            agentsAreMoving = true;

            System.out.println("Current turnNumber : "+turnNumber);

            if ((turnNumber==0)||(turnNumber==1))
            {
                Agent.requestMovementAll(turnNumber);
//                Agent.updateAgents(turnNumber);
            }

            for (int i=0;i!=turnChangeList.size();i++)
            {

             if (turnChangeList.get(i).toTurnNumber==turnNumber)
             {
                 Agent.requestMovementAll(turnNumber);
//                 Agent.updateAgents(turnNumber);
                 return true;
              }
            }
            return false;
        }

    }

    public static class AGENT_MOVEMENT
    {
        final String timeStamp;
        final int agentId;
        final double balance;
        final double price;
        final int fromNodeId;
        final int toNodeId;
        final int turnNumber;

        public static List<AGENT_MOVEMENT> agentMovementList;

        public static final String text = "AGENT_MOVEMENT";
        AGENT_MOVEMENT
                (
                        String timeStamp,
                        int agentId,
                        int fromNodeId,
                        int toNodeId,
                        double balance,
                        double price,
                        int turnNumber
                )
        {
            this.timeStamp=timeStamp;
            this.agentId=agentId;
            this.fromNodeId=fromNodeId;
            this.toNodeId=toNodeId;
            this.balance=balance;
            this.price=price;
            this.turnNumber=turnNumber;
        }


        public String toString(){return getString(this);}
        public static String getString(AGENT_MOVEMENT agent_movement)
        {
            return "< AGENT_MOVEMENT: <agentId:"+agent_movement.agentId+"> <fromNodeId:"
                    +agent_movement.fromNodeId+"> <toNodeId:"+agent_movement.toNodeId+
                    "> <balance: "+agent_movement.balance+"> <price:"+agent_movement.price+
                    "> <timeStamp:"+ agent_movement.timeStamp+"> <turnNumber:"+ agent_movement.turnNumber+"> >";
        }
    }

    public static class AGENT_BALANCE_CHARGED
    {

    }

    public static class POLICE_CAUGHT_THIEVES
    {
        final String timeStamp;
        final int thiefId;
        final int nodeId;
        final int turnNumber;

        final public static String text = "POLICES_CAUGHT_THIEVES";
        public static ArrayList<POLICE_CAUGHT_THIEVES> policeCaughtThievesArrayList;

        POLICE_CAUGHT_THIEVES(String timeStamp,int thiefId,int nodeId,int turnNumber)
        {
            this.timeStamp = timeStamp;
            this.thiefId = thiefId;
            this.nodeId = nodeId;
            this.turnNumber = turnNumber;

            for (int i=0;i!=everyAgent.size();i++)
            {
                if (everyAgent.get(i).id==this.thiefId)
                {
                    everyAgent.get(i).setDeadAfter(this.turnNumber);
                    System.out.println("found some stuff");
                }
            }



        }


        public String toString(){return getString(this);}
        public static String getString(POLICE_CAUGHT_THIEVES policeCaughtThieves)
        {
            return "< POLICE_CAUGHT_THIEVES: <thiefId:"+policeCaughtThieves.thiefId+"> <nodeId:"
                    +policeCaughtThieves.nodeId+
                    "> <timeStamp:"+ policeCaughtThieves.timeStamp+">";
        }


    }








}
