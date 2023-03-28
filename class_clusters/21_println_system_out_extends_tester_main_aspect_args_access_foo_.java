package lara;
class G 
{
	public static void main(String[] args) 
	{
		F f1=new F();
		f1.test1();
		System.out.println("done");
	}
}

--------------------

class Z11 
{
	enum B1
	{
	CON1(10),CON2(30),CON4(50);
	int i;
	B1(int i)
		{
			this.i=i;
		}
	}
	public static void main(String[] args) 
	{
		B1 b1=B1.CON4;
		System.out.println(b1);
		System.out.println(B1.CON4.i);
		B1 b2=B1.CON2;
		System.out.println(b2);
		System.out.println(b2.i);
	}
}

--------------------

class P
{
 static void test()
{
System.out.println("test-P");	
}
}
class R extends P
{
static void test()
{
System.out.println("test-R");	
}
}
public class Manager6
{
public static void main(String args[])
	{
	
	
	R.test();
	
	}
}
--------------------

