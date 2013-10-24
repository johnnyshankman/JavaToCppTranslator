package translator;

public class Test 
{
	
	public static void main(String [] asd)
	{
		String s = "broski";
		Object o = s;
		System.out.println(o);
		char c = 'c';
		int x = (int) c;
		System.out.println(x);
		String string = new String("braugh");
		System.out.println(string);
		Br0 bro = new Br0(5);
		bro.setG(9);
		System.out.println(bro.getG());
		System.out.println(bro.toString());
	}	
}

class Br0
{	
	public Br0(int g) { 
		this.g = g; 
	}
	
	public String toString() { 
		return "Br0 value is " + g; 
	}
	
	private int g;

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = g;
	} 
}
