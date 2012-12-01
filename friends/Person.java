package friends;

public class Person{
	public String name;
	public String school;
	public Friendex front;
	public boolean visited;
	public int zooIndex;
	
	public Person(String name, String school, Friendex front, boolean visited, int zooIndex){	//ArrayList<Person> friendList
		this.name = name;
		this.school = school;
		this.front= front;
		this.visited = visited;
		this.zooIndex = zooIndex;
	}
}

