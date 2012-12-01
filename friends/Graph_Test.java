package friends;

import java.io.*;
import java.util.*;
import java.lang.*;



public class Graph_Test {
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
	

	

	public static void cliques()
			throws IOException {
		System.out.println("Enter the name of the school => ");
		String school = stdin.nextLine();

	}

	public static void connectors(){


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

		Person[] zoo = new Person[count];
		zoo = build(personBuk, friendsBuk);		//FUCK HOW DOES THIS SYNTAX WORK?!?!
		
		int choice = getChoice();
		while (choice != QUIT) {
			if (choice < 1 || choice > QUIT) {
				System.out.println("\tIncorrect choice " + choice);
			} else {
				switch (choice) {
				case STUDENTS: 
					System.out.print("Enter the name of the school => ");
					String school = stdin.nextLine();
					subgraph(school, zoo); break;
				case SHORT: 
					System.out.print("Enter the name of the starting person => ");
					String start = stdin.nextLine();
					System.out.print("Enter the name of the starting person => ");
					String target = stdin.nextLine();
					shortest(start, target, zoo); break;
				case CLIQUE: cliques(); break;
				case CONNECT: connectors(); break;    //revise
				default: break;
				}
			}
			choice = getChoice();
		}
	}
	//FIX NEEDED--- school is case insensitive
	public static Person[] build(String[] people, ArrayList<String> friends){
		Person[] zoo = new Person[people.length];
		for(int i=0; i<people.length; i++){		// go through names and make them people
			String raw = people[i];
			String school=null;
			String name;

			name = raw.substring(0, raw.indexOf("|"));	//get name until '|'

			if(raw.charAt(raw.indexOf("|")+1)=='y'){				//attends school?
				school=raw.substring(raw.lastIndexOf('|')+1, raw.length());	//get the schoolname
			}
			Person body = new Person(name, school, null, false, -1);	//create a new person
			zoo[i]= body;							//put him in the zoo
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
	}
	public static ArrayList<Person> subgraph(String school, Person[] zoo){
		ArrayList<Person> schoolZoo = new ArrayList<>();
		for(int i=0; i< zoo.length; i++){			//go through all people in zoo----linear
			if(zoo[i].school != null && zoo[i].school.equals(school)){		//if their school matches
				Person tempPer = new Person(zoo[i].name, zoo[i].school, zoo[i].front, false, i);	//copy zoo person
				schoolZoo.add(tempPer);				//add the person to the arrayList
			}
		}
		for(int k=0; k<schoolZoo.size(); k++){		//go through schoolZoo
			Person student =  schoolZoo.get(k);		//person in the schoolZoo
			Friendex ptr = student.front;
			Friendex prev = null;
			while(ptr != null){		//go through the attached chain
				if(zoo[ptr.friendNum].school == null || !zoo[ptr.friendNum].school.equals(school)){
					if(prev==null){			//first person doesn't attend target school
						student.front = ptr.next;
					}else{
						prev.next = ptr.next;		//cut chains to non-attending person
					}
				}else{
					prev=ptr; 		//student attends--- don't modify
				}
				ptr=ptr.next;
			}
		}
		printSub(schoolZoo, zoo);
		return schoolZoo;
	}
	
	public static void printSub(ArrayList<Person> schoolZoo, Person[] zoo){
		for(int i=0; i<schoolZoo.size(); i++){		//print out names and school--- i.e. "nick|y|rutgers"
			String name = schoolZoo.get(i).name;
			String school =schoolZoo.get(i).school;
			if(school != null){
				System.out.println(name + "|y|" + school);
			}else{
				System.out.println(name + "|n");
			}
		}
		//print out relationships--- i.e. "kaitlin|nick"
		for(int k=0; k<schoolZoo.size(); k++){			//run through vertical array
			String first = schoolZoo.get(k).name;
			Person test = zoo[schoolZoo.get(k).zooIndex];
			test.visited =true;		//refers back to zoo from schoolZoo index
			Friendex ptr = schoolZoo.get(k).front;
			while(ptr!=null){							//run through horizontal LL
				if(!(zoo[schoolZoo.get(k).zooIndex].visited && zoo[ptr.friendNum].visited)){		//if one isn't marked, print them out
					String second = zoo[ptr.friendNum].name;
					System.out.println(first+ "|" + second);
					zoo[ptr.friendNum].visited = true;
				}
				ptr=ptr.next;
			}
		}
	}
	
	public static void shortest(String start, String target, Person[] zoo)throws IOException {
		Person perStart = null;
		int startDex = -1;
		for(int i=0; i<zoo.length;i++){
			if(zoo[i].name.equals(start)){
				perStart = zoo[i];
				startDex = i;
			}
			Queue<Friendex> newQ = new LinkedList<Friendex>();
			while (!newQ.isEmpty()){
			

		}
		}
	}
}	//end GraphTest class