package friends;


import java.io.*;
import java.util.*;

class Term {
    /**
     * name of term.
     */
    public String name;
   
    /**
     * school of term.
     */
    public String school;
   
 

   public Person[] build(String[] people, ArrayList<String> friends){
	   Person[] zoo = new Person[people.length];
	   for(int i=0; i<people.length; i++){		// go through names and make them people
		   String raw = people[i];
		   String school=null;
		   String name;
		   
		   name = raw.substring(0, raw.indexOf("|"));	//get name until '|'
		   System.out.println("name: "+name);
		   if(raw.charAt(raw.indexOf("|")+1)=='y'){				//attends school?
			   school=raw.substring(raw.lastIndexOf('|'), raw.length()-1);	//get the schoolname
			   System.out.println("school: " + school);
		   }
		  
		   
		   Person body = new Person(name, school, null);	//create a new person
		   zoo[i]= body;							//put him in the zoo
	   }
	   
	   for(int j=0; j<friends.size(); j++){		//go through the friends
		   String raw = friends.get(j);
		   String first = raw.substring(0, raw.indexOf('|'));		//the first friend name
		   String second = raw.substring(raw.indexOf('|'));			//the second friend name

		   for(int k=0; k<zoo.length; k++){		//searching array --- there is probably a better way
			   Person firstPerson = new Person(null, null, null);
			   Person secondPerson = new Person(null, null, null);
			   if (zoo[k].name.equalsIgnoreCase(first)){		//the names match
				   firstPerson = zoo[k];
			   }
			   if(zoo[k].name.equalsIgnoreCase(second)){
				   secondPerson = zoo[k];
			   }
			   if(firstPerson.next != null){		//attach the second friend to the first without losing old friend chain
				   Person temp = firstPerson.next;
				   firstPerson.next = secondPerson;
				   secondPerson.next = temp;
			   }else firstPerson.next = secondPerson;
			   if(secondPerson.next != null){		//attach the first friend to the second without losing old friend chain
				   Person temp = secondPerson.next;
				   secondPerson.next = firstPerson;
				   firstPerson.next = temp;
			   }else secondPerson.next = firstPerson;
		   }
				  
	   }
	   return zoo;		//temporary return
   }

    public Person subgraph(String school) {
        /** COMPLETE THIS METHOD **/
        return null;
    }
   
    
    public void shortest(String lonely, String desired) {
        /** COMPLETE THIS METHOD **/
        
    }
   
    
    public void cliques(String school) {
        /** COMPLETE THIS METHOD **/
       
    }
   
    public void connectors() {
        /** COMPLETE THIS METHOD **/
       
    }
  
   
}
