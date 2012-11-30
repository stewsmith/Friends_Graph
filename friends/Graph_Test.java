package friends;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;


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

	public static void subgraph()
			throws IOException {
		System.out.print("Enter the name of the school => ");
		String sc = stdin.nextLine();

	}

	public static void shortest()
			throws IOException {
		System.out.print("Enter the name of the lonely boy => ");
		String lone = stdin.nextLine();

	}

	public static void cliques()
			throws IOException {
		System.out.println("Enter the name of the school => ");
		String x = stdin.nextLine();

	}

	public static void connectors(){


	}

	public static void main(String[] args) throws IOException {
		br1 = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter the name of the friendship file => ");
		br2 = new BufferedReader(new FileReader(br1.readLine()));

		int count = Integer.parseInt(br2.readLine())+1;	//number of people
		String[] personBuk = new String[count];	

		for(int i=0; i<count; i++){			//put the people lines in an array
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

		Person[] graph = new Person[count];

		graph = build(personBuk, friendsBuk);		//FUCK HOW DOES THIS SYNTAX WORK?!?!

		System.out.println(graph);


		int choice = getChoice();
		while (choice != QUIT) {
			if (choice < 1 || choice > QUIT) {
				System.out.println("\tIncorrect choice " + choice);
			} else {
				switch (choice) {
				case STUDENTS: subgraph(); break;
				case SHORT: shortest(); break;
				case CLIQUE: cliques(); break;
				case CONNECT: connectors(); break;    //revise
				default: break;
				}
			}
			choice = getChoice();
		}

	}

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
			Person body = new Person(name, school, null);	//create a new person
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
		return zoo;		//temporary return
	}
}