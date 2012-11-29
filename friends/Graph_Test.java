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
        graph = build(personBuk, friendsBuk);
       
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
}