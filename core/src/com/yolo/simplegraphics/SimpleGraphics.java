package com.yolo.simplegraphics;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;



import java.util.ArrayList;
import java.util.List;

public class SimpleGraphics extends ApplicationAdapter {
	//Map Related
	public static JsonReader jsonReader;
	public static List<JsonValue> serverJsonValues;
	public static List<JsonValue> nodeJsonValues;
	public static List<JsonValue> edgeJsonValues;
	public static List<JsonValue> contextJsonValues;
	public static List<String> errorsList;
	public static List<String> logList;

	public static List<Node> everyNode;
	public static List<Edge> everyEdge;
	public static List<Agent> everyAgent;
	public static FileHandle  logFile;

	Texture blueThiefTexture;
	Texture blueCopTexture;
	Texture redThiefTexture;
	Texture redCopTexture;



	public static Sprite blueThiefSprite;
	public static Sprite blueCopSprite;
	public static Sprite redThiefSprite;
	public static Sprite redCopSprite;

	//Game Related
	public static SpriteBatch batch;
	public static Viewport viewport;
	public static OrthographicCamera camera;
	public static ShapeRenderer shape;

	EventHandler eventHandler;
	myGestureListener gestureListener;

	public static int currentTurn=0;
	public static int maxTurnNumber=0;

	public static final float winWidth = 500;
	public static final float winHeight = winWidth/2.2f;
	public static int winCurrentWidth=0;
	public static int winCurrentHeight=0;

	public static Vector2 mouseVector2=new Vector2(0,0);
	public static boolean mouseHeld=false;


	@Override
	public void create () {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		viewport = new StretchViewport(winWidth,winHeight,camera);

		blueCopTexture = new Texture(Gdx.files.internal("BlueCop.png"));
		blueThiefTexture = new Texture(Gdx.files.internal("BlueThief.png"));
		redCopTexture = new Texture(Gdx.files.internal("RedCop.png"));
		redThiefTexture = new Texture(Gdx.files.internal("RedThief.png"));

		blueCopSprite = new Sprite(blueCopTexture);
		blueThiefSprite = new Sprite(blueThiefTexture);
		redCopSprite = new Sprite(redCopTexture);
		redThiefSprite = new Sprite(redThiefTexture);




		logList = new ArrayList<>();
		logFile = Gdx.files.local("log.txt");
		logFile.writeString("LOG FILE STARTED\n",false);

		shape = new ShapeRenderer();
		jsonReader = new JsonReader();
		eventHandler = new EventHandler();
		gestureListener = new myGestureListener();


		readServer("server.log");
		deserializeServer();
		fillGameEvents();

		logList.add("Nodes Coordination: "+Node.getCoordination());

		InputMultiplexer inputMultiplexer=new InputMultiplexer(eventHandler,new GestureDetector(gestureListener));
		Gdx.input.setInputProcessor(inputMultiplexer);

	}


	public void renderShapes()
	{
//		shape.begin(ShapeRenderer.ShapeType.Line);
//		shape.setColor(Color.BLACK);
//		shape.line(0,winHeight/2,0,-winHeight/2);
//		shape.line(winWidth/2,0,-winWidth/2,0);
//		shape.end();
		Edge.drawAll(everyEdge);
		Node.drawAll(everyNode);
	}

	public void renderTextures()
	{
		Agent.drawAll(everyAgent);
	}


	@Override
	public void render () {
		ScreenUtils.clear(Color.SKY);
		batch.begin();
		renderShapes();
		batch.end();

		batch.begin();
		renderTextures();
		batch.end();
	}
	
	@Override
	public void dispose ()
	{
		writeLogs();
		batch.dispose();
	}

	@Override
	public void resize(int width, int height) {
		winCurrentHeight=height;
		winCurrentWidth=width;
		viewport.update(width,height);
		batch.setProjectionMatrix(camera.combined);
		shape.setProjectionMatrix(camera.combined);



	}

	public void readServer(String p_filePath)
	{

		FileHandle src = Gdx.files.local(p_filePath);
		serverJsonValues = new ArrayList<>();
		char[] charArray = src.readString().toCharArray();
		List<String> stringList=new ArrayList<>();
		errorsList = new ArrayList<>();

		String tempString="";
		int charCounter=0;
		for (int i=0;i!=charArray.length;i++)
		// Here we remove the first and last Double Quotation from the lines to convert it to a valid Json format
		{
			if ((charCounter!=0)){
				if ((i!=charArray.length-1)&&(charArray[i+1]!='\n'))
				{
					tempString+=charArray[i];
				}
			}
			charCounter++;
			if (charArray[i]=='\n')
			{
				stringList.add(tempString);
				tempString="";
				charCounter=0;
			}
		}

		JsonValue tempValue;

		int success=0;
		int fail=0;
		for (int i=0;i!=stringList.size();i++){

			try {
				tempValue = jsonReader.parse(stringList.get(i));
				serverJsonValues.add(tempValue);
				success++;
			}catch (SerializationException e){
				errorsList.add(stringList.get(i));
				fail++;
			}
		}
		logList.add("json parsing result: success: "+success+" fail: "+fail);
	}

	public void deserializeServer() {
		nodeJsonValues = new ArrayList<>();
		edgeJsonValues = new ArrayList<>();
		contextJsonValues = new ArrayList<>();

		JsonValue iterJV;
		JsonValue tempJV;
		for (int i = 0; i != serverJsonValues.size(); i++)
		{
			iterJV = serverJsonValues.get(i);

			tempJV = iterJV.get("nodes");
			if (tempJV!=null)
			{
				nodeJsonValues.add(tempJV);
				tempJV = iterJV.get("edges");
				edgeJsonValues.add(tempJV);
				continue;
			}
			tempJV = iterJV;
			if (tempJV!=null)
			{
				contextJsonValues.add(tempJV);
			}
		}

		if (nodeJsonValues.size()==0) {throw new Error("ServerJsonParseException: Could not find nodes!");}
		if (edgeJsonValues.size()==0) {throw new Error("ServerJsonParseException: Could not find edges!");}
		if (contextJsonValues.size()==0) {throw new Error("ServerJsonParseException: Could not find context!");}

		everyNode = new ArrayList<>();
		everyEdge = new ArrayList<>();
		everyAgent = new ArrayList<>();
		GameEvent.TURN_CHANGE.turnChangeList = new ArrayList<>();
		GameEvent.AGENT_MOVEMENT.agentMovementList = new ArrayList<>();
		GameEvent.STATUS_CHANGE.statusChangeList = new ArrayList<>();

		Node tempNode;
		for (int i=0;i!=nodeJsonValues.get(0).size;i++)
		{
			tempJV = nodeJsonValues.get(0).get(i);

			tempNode = new Node(tempJV.getInt("id"),tempJV.get("position").getFloat("x"),tempJV.get("position").getFloat("z"));

			everyNode.add(tempNode);

		}

		Node.fillCoordination(everyNode);

		Edge tempEdge;
		for (int i=0;i!=edgeJsonValues.get(0).size;i++)
		{
			tempJV = edgeJsonValues.get(0).get(i);

			tempEdge = new Edge(tempJV.getInt("node1Id"),tempJV.getInt("node2Id"),tempJV.getInt("edgeType"));

			everyEdge.add(tempEdge);

		}


		JsonValue context;
		Agent.TYPE type;

		Agent.TEAM team;
		GameEvent.TURN_CHANGE turnChange;
		GameEvent.AGENT_MOVEMENT agentMovement;
		GameEvent.STATUS_CHANGE statusChange;

		int id;
		int nodeId;
		double balance;
		Agent agent;
		int turnCounter=0;


		for (int i=0;i!=contextJsonValues.size();i++)
		{
			try{
				if (contextJsonValues.get(i).has("type"))
				{

					context = contextJsonValues.get(i).get("context");
					if (contextJsonValues.get(i).getString("type").equals(GameEvent.TURN_CHANGE.text))
					{
						if (context.getString("toTurn").equals(Agent.TYPE.POLICE.turnText))
						{
							type = Agent.TYPE.POLICE;

						}
						else{
							type = Agent.TYPE.THIEF;

						}
						turnChange = new GameEvent.TURN_CHANGE
								( contextJsonValues.get(i).getString("timeStamp"),
										context.getInt("toTurnNumber"),
										context.getBoolean("isVisible"),

										type
								);
						turnCounter++;
						logList.add("\n\t"+turnChange.toString()+" turnCounter:"+turnCounter+"\n");
						GameEvent.TURN_CHANGE.turnChangeList.add(turnChange);
					}
					else if(contextJsonValues.get(i).getString("type").equals(GameEvent.AGENT_MOVEMENT.text))
					{


						agentMovement = new GameEvent.AGENT_MOVEMENT
								(
										contextJsonValues.get(i).getString("timeStamp"),
										context.getInt("agentId"),
										context.getInt("fromNodeId"),
										context.getInt("toNodeId"),
										context.getDouble("balance"),
										context.getDouble("price"),
										turnCounter
								);


						logList.add("\t"+agentMovement.toString()+" turnCounter:"+turnCounter);
						GameEvent.AGENT_MOVEMENT.agentMovementList.add(agentMovement);

					}
					else if(contextJsonValues.get(i).getString("type").equals(GameEvent.STATUS_CHANGE.text))
					{
						statusChange = new GameEvent.STATUS_CHANGE(contextJsonValues.get(i).getString("timeStamp"),
										context.getString("fromStatus"),
										context.getString("toStatus"));

						if (context.getString("toStatus").equals("ONGOING")){turnCounter++;}

						logList.add("\n\t"+statusChange.toString()+" turnCounter:"+turnCounter+"\n");
						GameEvent.STATUS_CHANGE.statusChangeList.add(statusChange);

					}
				}


				context = contextJsonValues.get(i).get("context");
				if (context.has("agentId"))
				{
					if (contextJsonValues.get(i).getString("type").equals("READINESS_DECLARATION"))
					{
						if (context.getString("type").equals(Agent.TYPE.POLICE.text))
						{
							type = Agent.TYPE.POLICE;
						}
						else{
							type = Agent.TYPE.THIEF;
						}

						if (context.getString("team").equals(Agent.TEAM.FIRST.text))
						{
							team = Agent.TEAM.FIRST;
						}
						else{
							team = Agent.TEAM.SECOND;
						}

						id = context.getInt("agentId");
						nodeId = context.getInt("nodeId");
						balance = context.getDouble("balance");

						agent = new Agent(id,team,type,nodeId,balance);
						logList.add(agent.toString()+" turnCounter:"+turnCounter);
						everyAgent.add(agent);
					}
				}
				else
				{

				}
			}catch (NullPointerException e)
			{
				errorsList.add(e.getMessage());
			}
		}
		maxTurnNumber=turnCounter;
	}

	public void fillGameEvents()
	{

		for (int i=0;i!=everyAgent.size();i++)
		{
			everyAgent.get(i).fillAgentMovementList(GameEvent.AGENT_MOVEMENT.agentMovementList);
			logList.add("\n\n\tAgent movements List : "+
					everyAgent.get(i).toString()+
					"\n\n");
			for (int c=0;c!=everyAgent.get(i).agentStateList.size();c++)
			{
				logList.add(everyAgent.get(i).agentStateList.get(c).toString());
			}
		}
	}

	public void writeLogs()
	{
		logFile.writeString("\nLOGS:\n",true);
		for (int i=0;i!=logList.size();i++)
		{
			logFile.writeString("\t"+logList.get(i)+"\n",true);
		}

		logFile.writeString("\nERRORS:\n",true);
		for (int i=0;i!=errorsList.size();i++)
		{
			logFile.writeString("\t"+errorsList.get(i)+"\n",true);
		}

	}



}
