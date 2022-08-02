package com.yolo.simplegraphics;

import java.util.List;

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

            System.out.println("Current turnNumber : "+turnNumber);

            if ((turnNumber==0)||(turnNumber==1))
            {
                Agent.updateAgents(turnNumber);
            }

            for (int i=0;i!=turnChangeList.size();i++)
            {
             if (turnChangeList.get(i).toTurnNumber==turnNumber)
             {
                 Agent.updateAgents(turnNumber);
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

    }








}
