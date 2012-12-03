package friends;

public class Person{
	public String name;
	public String school;
	public Friendex front;
	public boolean visited;
	public int zooIndex;
	public int schoolIndex;
	public int back;
	public int dfs;
	
	public Person(String name, String school, Friendex front, boolean visited, 
			int zooIndex, int schoolIndex, int back, int dfs){	//ArrayList<Person> friendList
		this.name = name;
		this.school = school;
		this.front= front;
		this.visited = visited;
		this.zooIndex = zooIndex;
		this.schoolIndex = schoolIndex;
		this.back = back;
		this.dfs = dfs;
	}
}

