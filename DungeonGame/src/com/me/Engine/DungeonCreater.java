package com.me.Engine;


import java.util.ArrayList;

import com.me.Data.AssetLoader;
import com.me.GameObjects.Artifact;
import com.me.GameObjects.GoalRoom;
import com.me.GameObjects.Health;
import com.me.GameObjects.Room;
import com.me.GameObjects.RoomCheck;

//Types for walls: 0 = open; 1 = wall; 2 = goal; 3 = start;

public class DungeonCreater {
	//The testing method for which type it is might not ever return true got to check taht out

	private Room[][] rooms;
	private int[][]goalCoords;
	
	private int width, height;
	private int size;
	private int startX,startY;
	
	private AssetLoader assLoader;
	
	private ArrayList<RoomCheck> openCoordinates;
	private ArrayList<Artifact> aList;
	private ArrayList<Health> hList;
	
	public DungeonCreater(int w, int h, int s, AssetLoader a){
		if(w%s != 0){
			w += s - w%s;
		}
		if(h%s != 0){
			h += s - h%s;
		}
		width = w;
		height = h;
		size = s;
		
		//width/size +1 adds a barrier around so player can't get out of the map.
		rooms = new Room[width/size+1][height/size+1];
		
		assLoader= a;
	}
	
	//Creates a straight line for testing
	public void createTest(){
		
		ArrayList<GoalRoom> goalRooms = new ArrayList<GoalRoom>();
		rooms = new Room[width/size][height/size];
		goalRooms.add((GoalRoom) (rooms[2][1] = new GoalRoom(2, size, 2* size, 2* size,assLoader)));
		goalRooms.add((GoalRoom) (rooms[4][10] = new GoalRoom(2, size, 4* size, 10* size,assLoader)));
		for (int i = 0; i < 1; i++) {
			startX = (int) (Math.random() * (rooms.length - 2)+1);
			startY = (int) (Math.random() *(rooms[0].length-2)+1);
			rooms[startX][startY] = new Room(3, size, startX * size, startY * size,assLoader);
		}
//		for(int i = 2; i < 10; i++){
//			rooms[2][i] = new Room(0, size, 2 * size, i * size);
//		}
		connectGoalRooms(goalRooms);
		for(int x = 0; x < rooms.length; x++){
			for(int y = 0; y < rooms[0].length; y++){
				if(rooms[x][y] == null)
					rooms[x][y] = new Room(1,size,x*size,y*size,assLoader);
			}
		}
	}
	
	public ArrayList<GoalRoom> createDungeon2(int g){
		if(g > width/size * height/size)
			g = width/size * height/size;
		int goal = g;
		System.out.println("Goals:"+g);

		ArrayList<GoalRoom> goalRooms = new ArrayList<GoalRoom>();
		aList = new ArrayList<Artifact>();
		hList = new ArrayList<Health>();
		rooms = new Room[width/size][height/size];
		
		
		goalCoords = new int[g][2];
		
		for (int i = 0; i < g; i++) {
			goalCoords[i][0] = (int) (Math.random() * (rooms.length - 2)+1);
			goalCoords[i][1]= (int) (Math.random() * (rooms[0].length-2)+1);
			goalRooms.add((GoalRoom) (rooms[goalCoords[i][0]][goalCoords[i][1]] = new GoalRoom(2, size, goalCoords[i][0] * size, goalCoords[i][1] * size,assLoader)));
			aList.add(goalRooms.get(i).getArti());
			if(goalRooms.get(i).isHealth())
				hList.add(goalRooms.get(i).getHealth());
//			goalRooms.add( new Room(2, size, goalCoords[i][0] * size, goalCoords[i][1] * size));
		}

		// places the startingPositions
		for (int i = 0; i < 1; i++) {
			startX = (int) (Math.random() * (rooms.length - 2)+1);
			startY = (int) (Math.random() *(rooms[0].length-2)+1);
			rooms[startX][startY] = new Room(3, size, startX * size, startY * size,assLoader);
			if(rooms[startX][startY].isHealth())
				hList.add(rooms[startX][startY].getHealth());
		}
		
		connectGoalRooms(goalRooms);
		for(int x = 0; x < rooms.length; x++){
			for(int y = 0; y < rooms[0].length; y++){
				if(rooms[x][y] == null)
					rooms[x][y] = new Room(1,size,x*size,y*size,assLoader);
			}
		}
		return goalRooms;
	}
	
	private void connectGoalRooms(ArrayList goals){
		ArrayList<GoalRoom> unconnected = goals;
		System.out.println("1st goal: "+(((Room) goals.get(0)).getX())+", "+ ((Room) goals.get(0)).getY());
		GoalRoom closest;
		// while (!done) {
		//Connects goal Rooms
		for (int c = 0; c < unconnected.size(); c++) {

			

			GoalRoom startingPoint = unconnected.get(c); // THe room that is searching for closest
			closest = unconnected.get(c); // cloestRoom to startingPoint
			
			double minDist = 100000;
			// Checks for closest unlinked room
			for (int i = 0; i < unconnected.size(); i++) {
				if( i != c && unconnected.get(i).getConnected()){
					GoalRoom lg = unconnected.get(i); // The room that is being looked at
					double dist = Math.sqrt(Math.pow(lg.getX() - startingPoint.getX(), 2)+ Math.pow(lg.getY() - startingPoint.getY(), 2));
					if (dist < minDist) {
						minDist = dist;
						closest = lg;
						System.out.println("2st goal: "+(((Room) goals.get(i)).getX())+", "+ ((Room) goals.get(i)).getY());
					}
				}
			}

			int dir = (startingPoint.getX() > closest.getX() ? -1 : 1);
			for (int x = startingPoint.getArrayX(); x * dir <= closest.getArrayX() * dir; x += dir) {
				if (rooms[x][startingPoint.getArrayY()] == null){
					rooms[x][startingPoint.getArrayY()] = new Room(0, size, x * size,startingPoint.getArrayY() * size,assLoader);
					if(rooms[x][startingPoint.getArrayY()].isHealth())
						hList.add(rooms[x][startingPoint.getArrayY()].getHealth());
				}
			}
			dir = (startingPoint.getY() > closest.getY() ? -1 : 1);
			for (int y = startingPoint.getArrayY(); y * dir <= closest.getArrayY() * dir; y += dir) {
				if (rooms[closest.getArrayX()][y] == null ){
					rooms[closest.getArrayX()][y] = new Room(0, size, closest.getArrayX() * size, y * size,assLoader);
					if(rooms[closest.getArrayX()][y].isHealth())
						hList.add(rooms[closest.getArrayX()][y].getHealth());
				}
			}
			startingPoint.setConnected(true);
		}
			
		//Connects starting room
		Room startingPoint = rooms[startX][startY]; // THe room that is searching
		closest = unconnected.get((int)(Math.random()*goals.size())); //Chooses a random goal
		int dir = (startingPoint.getX() > closest.getX() ? -1 : 1);
		for (int x = startingPoint.getArrayX(); x * dir <= closest.getArrayX() * dir; x += dir) {
			if (rooms[x][startingPoint.getArrayY()] == null){
				rooms[x][startingPoint.getArrayY()] = new Room(0, size, x * size,startingPoint.getArrayY() * size,assLoader);
				if(rooms[x][startingPoint.getArrayY()].isHealth())
					hList.add(rooms[x][startingPoint.getArrayY()].getHealth());
			}
		}
		dir = (startingPoint.getY() > closest.getY() ? -1 : 1);
		for (int y = startingPoint.getArrayY(); y * dir <= closest.getArrayY() * dir; y += dir) {
			if (rooms[closest.getArrayX()][y] == null ){
				rooms[closest.getArrayX()][y] = new Room(0, size, closest.getArrayX() * size, y * size,assLoader);
				if(rooms[closest.getArrayX()][y].isHealth())
					hList.add(rooms[closest.getArrayX()][y].getHealth());
			}
		}
	}
	
	public boolean doesBoardWork(){
		openCoordinates = new ArrayList();
		int x = startX,y = startY;
		openCoordinates.add(new RoomCheck(x,y,0));
//		System.out.println("StartX: "+x+" StartY: "+y);
		boolean work = true;
//		end:
//		while(work){
//			int direction = 0;
//			switch(direction){
//			case 0:
//				x-=1;
//				if(checkRoom(x,y)){
//					if (rooms[x][y].getType() == 2){
//						work = true;
//						break end;
//					}
//					else if (rooms[x][y].getType() == 0 || rooms[x][y].getType() == 3){
//						break;
//					}
//				}
//			}
//		}
		if(checkingSurroundings(false,x,y,0,0) == 0){
			System.out.println("Fails");
			return false;
		}
		System.out.println("Works");
		return work;
	}
	
	private boolean checkRoom(int x,int y, int prevX, int prevY){
//		System.out.println("Check X: "+x+" Y: "+y);
		if((x < rooms.length && x >= 0) && (y < rooms[0].length && y >= 0)){
			if(openCoordinates.size()>0)
			for(int i = 0; i < openCoordinates.size(); i ++){
				if(x == openCoordinates.get(i).getX() && y == openCoordinates.get(i).getY()){
//					System.out.println("That was Checked");
					return false;
				}
			}
			if(x == prevX && y == prevY){
//				System.out.println("Same");
				return false;
			}
			else
				if( rooms[x][y].getType() == 1 || rooms[x][y].getType() == 3)
					return false;
				else 
					return true;
		}
		else
			return false;

	}
	
	private int checkingSurroundings(boolean goback, int x, int y, int prevX, int prevY) {
		System.out.println("X: "+x+" Y: "+y);
//		for(int i = 0; i < openCoordinates.size(); i ++){
//			System.out.println("X "+ openCoordinates.get(i).getX()+" Y "+openCoordinates.get(i).getY());
//		}
		int dir = 0;
		//0 = left, 1 = up, 2 = right, 3 = right
		
		while(true){
			int tempX = x, tempY = y;
			switch(dir){
			case 0:
				tempX-=1;
				if(checkRoom(tempX,tempY,prevX,prevY)){
					if(rooms[tempX][tempY].getType() == 2)
						return 2;
					openCoordinates.add(new RoomCheck(tempX,tempY,dir));
					return checkingSurroundings(false,tempX,tempY,x,y);
				}
				else{
					dir +=1;
				}
				break;
			case 1:
				tempY-=1;
				if(checkRoom(tempX,tempY,prevX,prevY)){
					if(rooms[tempX][tempY].getType() == 2)
						return 2;
					openCoordinates.add(new RoomCheck(tempX,tempY,dir));
					return checkingSurroundings(false,tempX,tempY,x,y);
				}
				else{
					dir +=1;
				}
				break;
			case 2:
				tempX+=1;
				if(checkRoom(tempX,tempY,prevX,prevY)){
					if(rooms[tempX][tempY].getType() == 2)
						return 2;
					openCoordinates.add(new RoomCheck(tempX,tempY,dir));
					return checkingSurroundings(false,tempX,tempY,x,y);
				}
				else{
					dir +=1;
				}
				break;
			case 3:
				tempY+=1;
				if(checkRoom(tempX,tempY,prevX,prevY)){
					if(rooms[tempX][tempY].getType() == 2)
						return 2;
					openCoordinates.add(new RoomCheck(tempX,tempY,dir));
					return checkingSurroundings(false,tempX,tempY,x,y);
				}
				else{
					dir +=1;
				}
				break;
				default:
					if(!goback){
						System.out.println("Go Back");
						return checkingSurroundings(true,openCoordinates.get(0).getX(),openCoordinates.get(0).getY(),x,y);
					}
					else{
						openCoordinates.remove(0);
						if(openCoordinates.size() == 0){
							return 0;
						}
						else{
							
							return checkingSurroundings(true, openCoordinates.get(0).getX(), openCoordinates.get(0).getY(), x, y);
						}
					}
			}
		}
	}
	
	//Gets specfic room
	public Room getRooms(int x, int y){
		return rooms[x][y];
	}
	//Gets room list
	public Room[][] getRooms(){
		return rooms;
	}
	
	//Checks to see if room exists
	public boolean isRoomValid(int x, int y){
		if(x >= rooms.length || x <= 0 || y>= rooms[0].length || y <= 0){
			return false;
		}
//		System.out.println("valid");
		return true;
	}
	
	//Gets adjacent Rooms
	public ArrayList<Room> getAdjacentRooms(int ax, int ay){
//		System.out.println("Int x: "+ax+" int y "+ay);
		ArrayList<Room> aRooms = new ArrayList<Room>();
		for(int i = -1; i < 2; i ++){
			for(int j = -1; j < 2; j++){
				if(isRoomValid(ax+i,ay+j)){
					aRooms.add(rooms[ax+i][ay+j]);
				}
			}
		}
//		System.out.println("Arooms: "+aRooms);
		return aRooms;
	}
	
	public int getStartX(){
		return startX;
	}
	
	public int getStartY(){
		return startY;
	}
	
	public ArrayList<Artifact> getArti(){
		return aList;
	}
	
	public ArrayList<Health> getHealth(){
		return hList;
	}
	
	//Why do i have to reverse the positions of the array
	//to print out the actual thing?
	//Maybe its how the array works.
	@Override
	public String toString(){
		String total = "";
		for(int i = 0;  i < rooms[0].length; i++){
			for(int j = 0; j < rooms.length; j++){
				total += (rooms[j][i].getType())+" ";
			}
			total += "\n";
		}
		return total;
	}
	
}
