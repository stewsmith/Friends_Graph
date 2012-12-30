/*Written by Stewart Smith and Jake Skelci for CS 112 Fall 2012*/

package friends;

import java.io.*;
import java.util.*;
import java.util.Queue;

public class Friends {
  static BufferedReader br1, br2;
	static Scanner stdin = new Scanner(System.in);

	public static final int STUDENTS = 1;
	public static final int SHORT = 2;
	public static final int CLIQUE = 3;
	public static final int CONNECT = 4;
	public static final int QUIT = 5;

	public static int getChoice()
			throws IOException {
		System.out.println();
		System.out.println(STUDENTS + ". Students at school");
		System.out.println(SHORT + ". Shortest intro chain");
		System.out.println(CLIQUE + ". Cliques at school");
		System.out.println(CONNECT + ". Connectors");
		System.out.println(QUIT + ". QUIT");
		System.out.print("\tEnter choice # => ");
		return (Integer.parseInt(br1.readLine()));
	}

	public static void main(String[] args) throws IOException {
		br1 = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter the name of the friendship file => ");
		br2 = new BufferedReader(new FileReader(br1.readLine()));

		int count = Integer.parseInt(br2.readLine())+1;	//number of people
		String[] personBuk = new String[count-1];	

		for(int i=0; i<count-1; i++){			//put the people lines in an array
			String line= br2.readLine();
			personBuk[i]= line;
		}

		ArrayList<String> friendsBuk = new ArrayList<String>();
		String line = br2.readLine();
		friendsBuk.add(line);

		while(line != null){	//put the friends line in an array
			line = br2.readLine();
			friendsBuk.add(line);
		}

		Hashtable<String, Integer> hash = new Hashtable<String, Integer>();
		Person[] zoo = new Person[count];
		zoo = build(personBuk, friendsBuk, hash);	

		int choice = getChoice();
		while (choice != QUIT) {
			if (choice < 1 || choice > QUIT) {
				System.out.println("\tIncorrect choice " + choice);
			} else {
				switch (choice) {
				case STUDENTS: 
					System.out.print("Enter the name of the school => ");
					String school = stdin.nextLine();
					subgraph(school, zoo, true); 
					break;
				case SHORT: 
					System.out.print("Enter the name of the starting person => ");
					String start = stdin.nextLine();
					System.out.print("Enter the name of the target person => ");
					String target = stdin.nextLine();
					shortest(start, target, zoo, hash); 
					break;
				case CLIQUE: 
					System.out.print("Enter the name of the school => ");
					String sc = stdin.nextLine();
					cliques(sc, zoo); 
					break;	
				case CONNECT: 
					connectors(zoo, count);
					break;    
				default: break;
				}
			}
			choice = getChoice();
			for(int i=0; i<zoo.length;i++){			//reset EVERYTHING
				zoo[i].visited=false;
				zoo[i].back = -1;
				zoo[i].dfs = -1;
				zoo[i].schoolIndex = -1;
			}
		}
	}		//end main method

	public static Person[] build(String[] people, ArrayList<String> friends, Hashtable<String, Integer> hash){
		Person[] zoo = new Person[people.length];
		for(int i=0; i<people.length; i++){		// go through names and make them people
			String raw = people[i];
			String school=null;
			String name;

			name = raw.substring(0, raw.indexOf("|"));	//get name until '|'

			if(raw.charAt(raw.indexOf("|")+1)=='y'){				//attends school?
				school=raw.substring(raw.lastIndexOf('|')+1, raw.length());	//get the schoolname
			}
			Person body = new Person(name, school, null, false, i,-1,-1,-1);	//create a new person
			zoo[i]= body;							//put him in the zoo
			hash.put(body.name.toLowerCase(), i);					//
		}

		for(int j=0; j<friends.size()-1; j++){		//go through the friends
			String raw = friends.get(j);
			String first = raw.substring(0, raw.indexOf('|'));		//the first friend name
			String second = raw.substring(raw.indexOf('|')+1);			//the second friend name
			Friendex firstDex = new Friendex(0,null);
			Friendex secDex = new Friendex(0, null);
			boolean firstMatched = false;
			boolean secMatched =false;

			for(int k=0; k<zoo.length; k++){				//look for first and second name

				if (zoo[k].name.equalsIgnoreCase(first)){	//firstname found
					firstDex.friendNum=k;					//friendNum = where the match is
					firstMatched=true;
				}
				if (zoo[k].name.equalsIgnoreCase(second)){
					secDex.friendNum=k;
					secMatched=true;
				}	
				if(firstMatched && secMatched){
					Friendex temp1 = zoo[firstDex.friendNum].front;		//keep the chain that is already attached
					Friendex temp2 = zoo[secDex.friendNum].front;

					zoo[firstDex.friendNum].front = secDex;				//update fronts
					zoo[secDex.friendNum].front = firstDex;

					firstDex.next = temp2;								//attach old chains
					secDex.next = temp1;
					break;
				}
			}
		}
		return zoo;		
	}				//end build


	public static ArrayList<Person> subgraph(String school, Person[] zoo, boolean printSub){

		ArrayList<Person> schoolZoo = new ArrayList<>();
		int schoolZoodex = 0;
		for(int i=0; i< zoo.length; i++){			//go through all people in zoo----linear	
			if(zoo[i].school != null && zoo[i].school.equalsIgnoreCase(school)){		//if their school matches
				zoo[i].schoolIndex=schoolZoodex;
				
				Person tempPer = new Person(zoo[i].name, zoo[i].school, zoo[i].front.copyList(), false, i,schoolZoodex,-1,-1);	//copy zoo person
				schoolZoo.add(tempPer);				//add the person to the arrayList
				schoolZoodex++;
			}
		}
		for(int k=0; k<schoolZoo.size(); k++){		//go through schoolZoo
			Person student =  schoolZoo.get(k);		//person in the schoolZoo
			Friendex ptr = student.front;
			Friendex prev = null;
			while(ptr != null){		//go through the attached chain
				if(zoo[ptr.friendNum].schoolIndex <0){
					if(prev==null){			//first person doesn't attend target school
						student.front = ptr.next;
					}else{
						prev.next = ptr.next;		//cut chains to non-attending person
					}
				}else{
					ptr.friendNum = zoo[ptr.friendNum].schoolIndex;		
					prev=ptr; 		//student attends--- don't modify
				}
				ptr=ptr.next;
			}
		}
		if(printSub){
			printSub(schoolZoo);
		}
		return schoolZoo;
	}						//end subgraph

	private static void printSub(ArrayList<Person> schoolZoo){
		//boolean[] visited = new boolean[(schoolZoo.size())];
		for(int i=0; i<schoolZoo.size(); i++){		//print out names and school--- i.e. "nick|y|rutgers"
			Person curr = schoolZoo.get(i);
			curr.visited = false;
			String name = curr.name;
			String school =curr.school;
			if(school != null){
				System.out.println(name + "|y|" + school);
			}else{
				System.out.println(name + "|n");
			}
		}	
		//print out relationships--- i.e. "kaitlin|nick"
		for(int k=0; k<schoolZoo.size(); k++){			//run through vertical array
			Person test = schoolZoo.get(k);
			String first = test.name;
			Friendex ptr = schoolZoo.get(k).front;
			while(ptr!=null){							//run through horizontal LL
				if(!(test.visited && schoolZoo.get(ptr.friendNum).visited)){		//if one isn't marked, print them out
					String second = schoolZoo.get(ptr.friendNum).name;
					System.out.println(first+ "|" + second);
					schoolZoo.get(ptr.friendNum).visited = true;
					test.visited =true;		
				}
				ptr=ptr.next;
			}
		}
	}		//end printSub

	public static void shortest(String start, String target, Person[] zoo, Hashtable<String, Integer> hash)throws IOException {
		Person perStart = null;
		Stack<Person> printStack = new Stack<Person>();
		Queue<Integer> newQ = new LinkedList<Integer>();
		boolean complete = false;		//SS

		int i = hash.get(start.toLowerCase());		// finds the start person in zoo
		perStart = zoo[i];
		perStart.zooIndex = i;
		newQ.add(i);			 // add the start person to queue for BFS

		while (!newQ.isEmpty()){          //BFS queue build
			if(complete) break;		//SS
			zoo[newQ.peek()].visited = true;
			Person parent = zoo[newQ.poll()]; //dequeue front
			Friendex friendPtr = parent.front;
			while (friendPtr!=null){              //move horizontally through friends
				Person temp = zoo[friendPtr.friendNum];  //friend currently looking at
				if (!temp.visited){ //unvisited friends
					temp.zooIndex = friendPtr.friendNum;
					temp.back = parent.zooIndex;  //tells where the person came from
				}
				else{		//if friend has already been visited
					friendPtr = friendPtr.next;
					continue;
				} 
				if (target.equalsIgnoreCase(temp.name)){ // target is found
					Person parentPtr = temp;
					while (parentPtr.zooIndex != perStart.zooIndex){ //moves back and populates print stack
						printStack.push(parentPtr);
						parentPtr=zoo[parentPtr.back];
					}
					printStack.push(perStart);	//  SS----adds last starting person to printStack
					complete = true;	//SS
					break;	//  SS--- target found so break out of loop
				}else {newQ.add(friendPtr.friendNum); //if temp is not the target put on BFS queue
				friendPtr=friendPtr.next;			
				}	
			}
		}
		String answer = "";
		if(printStack.isEmpty()){	
			System.out.println("No possible path");
		}
		else{
			while (!printStack.isEmpty()){
				String name = printStack.pop().name;
				answer += name + "--";
			}
			answer = answer.substring(0,answer.length()-2);
			System.out.println(answer);
		}
	}		//end shortest


	public static void cliques(String school, Person[] zoo){

		ArrayList<Person> schoolZoo = subgraph(school, zoo, false);	//creates subgraph with school
		ArrayList<ArrayList<Person>> answer = new ArrayList<>(); 		

		for(int i=0; i< schoolZoo.size(); i++){		//go through vertical array
			Person vert = schoolZoo.get(i);
			Queue<Person> newQ = new LinkedList<Person>();
			ArrayList<Person> newClique = new ArrayList<>();
			boolean addClique = false;
			if(!vert.visited){		//the person has not been visited so he must be part of a new clique
				addClique =true;
				newQ.add(vert);
				while(!newQ.isEmpty()){					//BFS queue
					Person justDQd = newQ.remove();
					justDQd.visited = true;				
					newClique.add(justDQd);
					Friendex ptr = justDQd.front;
					while(ptr != null){				//go through LL horizontally 
						if(!schoolZoo.get(ptr.friendNum).visited){
							newQ.add(schoolZoo.get(ptr.friendNum));
						}
						ptr = ptr.next;
					}
				}
			}
			if(addClique){
				answer.add(newClique);
			}
		}
		//Print out the cliques and members
		for(int p=0; p<answer.size(); p++){
			ArrayList <Person> clique = answer.get(p);
			System.out.println("Clique " + (p+1) +":");
			for(int k=0; k<clique.size(); k++){
				String name = clique.get(k).name;
				String sch = clique.get(k).school;
				System.out.println(name + "|" + "y" + "|" + sch);
			}
		}
	}		//end cliques


	public static void connectors(Person[] zoo, int size){
		int count=1;
		//ArrayList<Boolean> conn = new ArrayList<Boolean>();
		boolean[] conn = new boolean[zoo.length];

		for(int i=0; i<zoo.length; i++){		//move vertically looking for new cliques
			Person dfsStart = zoo[i];

			if(!dfsStart.visited){
				Stack<Person> dfsStack = new Stack<Person>();
				dfsStack.push(dfsStart);
				while(!dfsStack.isEmpty()){			//stack allows forward movement when push and backward when popped
					Person curr = dfsStack.peek();
					if(!curr.visited){
						curr.dfs = count;
						curr.back = count;
						count++;
						curr.visited = true;
					}
					Friendex ptr = curr.front;
					while(ptr != null){						//moves horizontally through friends
						if(!zoo[ptr.friendNum].visited){	//go to the friend that isn't already visited
							Person currFriend = zoo[ptr.friendNum];
							dfsStack.push(currFriend);		//moving forward in graph
							break;
						}else{						//neighbor is already visited
							Person base = curr;
							Person neigh = zoo[ptr.friendNum];
							base.back = Math.min(base.back, neigh.dfs);
						}
						ptr = ptr.next;
					}
					if(ptr == null){		//end of the dfs line---- start going backwards
						if(curr.dfs == dfsStart.dfs){
							break;				//needs revision----reached the start
						}																													
						Person neigh = dfsStack.pop();		// w = neigh
						Person base = dfsStack.peek();		//v = base
						if(base.dfs>neigh.back){			//	condition for changing base.back
							base.back = Math.min(base.back, neigh.back);
						}
						if(base.dfs <= neigh.back){		//CONNECTOR -- wrong: && base != dfsStart
							if(base != dfsStart){		//normal connector
								conn[base.zooIndex] = true;
							}else{		//connector is at the start
								Friendex stPtr = base.front;
								while(stPtr != null){		//check for unvisited friends
									if(!zoo[stPtr.friendNum].visited){		//if there is an unvisited friend
										conn[base.zooIndex] = true;			//then the start is a connector
										break;
									}
									stPtr = stPtr.next;
								}
							}

						}
					}
				}
			}
		}
		//give ans the all names of the connectors
		String ans = "";
		for(int j=0; j<conn.length; j++){
			if(conn[j]){
				ans = ans + zoo[j].name + ", ";
			}
		}
		ans = ans.substring(0, ans.length()-2);
		System.out.println(ans);
	}
}	//end GraphTest class
