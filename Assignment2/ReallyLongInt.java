// CS 0445 Spring 2020
// This is a partial implementation of the ReallyLongInt class.  You need to
// complete the implementations of the remaining methods.  Also, for this class
// to work, you must complete the implementation of the LinkedListPlus class.
// See additional comments below.

public class ReallyLongInt 	extends LinkedListPlus<Integer> implements Comparable<ReallyLongInt>
{
	private ReallyLongInt()
	{
		super();
	}

	// Data is stored with the LEAST significant digit first in the list.  This is
	// done by adding all digits at the front of the list, which reverses the order
	// of the original string.  Note that because the list is doubly-linked and
	// circular, we could have just as easily put the most significant digit first.
	// You will find that for some operations you will want to access the number
	// from least significant to most significant, while in others you will want it
	// the other way around.  A doubly-linked list makes this access fairly
	// straightforward in either direction.
	public ReallyLongInt(String s)
		{
			super();
			char c;
			int digit = -1;
			// Iterate through the String, getting each character and converting it into
			// an int.  Then make an Integer and add at the front of the list.  Note that
			// the add() method (from A2LList) does not need to traverse the list since
			// it is adding in position 1.  Note also the the author's linked list
			// uses index 1 for the front of the list.
			for (int i = 0; i < s.length(); i++)
			{
				c = s.charAt(i);
				if (('0' <= c) && (c <= '9'))
				{
					digit = c - '0';
					// Do not add leading 0s
					if (!(digit == 0 && this.getLength() == 0))
						this.add(1, Integer.valueOf(digit));
						//System.out.println(Integer.valueOf(digit));
				}
				else throw new NumberFormatException("Illegal digit " + c);
			}
			// If number is all 0s, add a single 0 to represent it
			if (digit == 0 && this.getLength() == 0)
				this.add(1, Integer.valueOf(digit));
		}

		public ReallyLongInt(ReallyLongInt rightOp)
		{
			super(rightOp);
		}

	// Method to put digits of number into a String.  Note that toString()
	// has already been written for LinkedListPlus, but you need to
	// override it to show the numbers in the way they should appear.
	public String toString()
	{
		boolean first = true;
		boolean once = true;
		StringBuilder b = new StringBuilder();
		//StringBuilder c = new StringBuilder();
		ReallyLongInt temp = new ReallyLongInt(this);
		temp.reverse();
		Node curr = temp.firstNode;

		int i = 0;
		while (i < this.getLength())
		{
			//System.out.print(curr.data);

			if(curr.data == 0)
			{
				 if(!first)
				 {
					b.append(curr.data.toString());
				 }
			}
			else if(curr.data != 0)
			{
				first = false;
				b.append(curr.data.toString());
				first = false;
			}

			//b.append(" ");
			curr = curr.next;
			i++;
		}
		if(b.toString().equals(""))
		{
			b.append(Integer.valueOf(0));
		}
		return b.toString();
	}

	// See notes in the Assignment sheet for the methods below.  Be sure to
	// handle the (many) special cases.  Some of these are demonstrated in the
	// RLITest.java program.

	// Return new ReallyLongInt which is sum of current and argument
	public ReallyLongInt add(ReallyLongInt rightOp)
	{
		Node curr = this.firstNode;
		//Node first = this.firstNode;
		Node rightCurr = rightOp.firstNode;
		//Node rightFirst = rightOp.firstNode;
		Integer remainder = Integer.valueOf(0);
		ReallyLongInt sum = new ReallyLongInt();
		int n = 0, biggestSize, diff;


		if(this.numberOfEntries > rightOp.getLength())
		{
			biggestSize = this.numberOfEntries;
			diff = biggestSize-rightOp.getLength();
			for(int i = 0; i < diff; i++)
			{
				rightOp.add(0);
			}
		}
		else if(this.numberOfEntries < rightOp.getLength())
		{
			biggestSize = rightOp.getLength();
			diff =  rightOp.getLength() - this.numberOfEntries;
			for(int i = 0; i < diff; i++)
			{
				this.add(0);
			}
		}
		else
		{
			biggestSize = rightOp.getLength();
			diff = 0;
		}
		while(n < biggestSize)
		{
			Integer right = rightCurr.data;
			Integer thisInt = curr.data;
			Integer result = Integer.valueOf(right+thisInt+remainder);


			Integer tens = Integer.valueOf(result/10);
			Integer ones = Integer.valueOf(result % 10);



			sum.add(ones);
			remainder = tens;

			if(this.firstNode.prev == curr && rightOp.firstNode.prev == rightCurr)
			{
				if(result >= 10)
				{
					//sum.add(ones);
					sum.add(tens);
				}
			}
			curr = curr.next;
			rightCurr = rightCurr.next;
			n++;
		}
		//sum.reverse();
		return sum;
	}

	// Return new ReallyLongInt which is difference of current and argument
	public ReallyLongInt subtract(ReallyLongInt rightOp)
	{
		Node curr = this.firstNode;
		//Node first = this.firstNode;
		Node rightCurr = rightOp.firstNode;
		//Node rightFirst = rightOp.firstNode;
		Integer remainder = Integer.valueOf(0);
		ReallyLongInt sum = new ReallyLongInt();

		int n = 0, biggestSize = this.numberOfEntries, diff;

		if(this.compareTo(rightOp) == -1)
		{
			throw new ArithmeticException("Result cannot be negative");
		}
		if(this.numberOfEntries != rightOp.getLength())
		{
			diff = this.numberOfEntries-rightOp.getLength();
			for(int i = 0; i < diff; i++)
			{
				rightOp.add(0);
			}
			//System.out.println((rightOp.toString()));
		}
		//System.out.println()
		while(n < biggestSize)
		{
			Integer right = rightCurr.data;
			Integer thisInt = curr.data;
			int val = (thisInt.intValue() - right.intValue()-remainder.intValue());
			//System.out.println(val);
			Integer result = Integer.valueOf(val);


			//Integer tens = Integer.valueOf(result/10);
			Integer ones = Integer.valueOf(Math.abs(val));

			if((val) < 0)
			{
				result = Integer.valueOf(10 - ones.intValue());
				remainder = Integer.valueOf(1);
			}

			sum.add(result);



			/*
			if(this.firstNode.prev == curr && rightOp.firstNode.prev == rightCurr)
			{
				if(result >= 10)
				{
					//sum.add(ones);
					sum.add(tens);
				}
			}
			*/
			curr = curr.next;
			rightCurr = rightCurr.next;
			n++;
		}
		//sum.reverse();
		return sum;
	}

	// Return -1 if current ReallyLongInt is less than rOp
	// Return 0 if current ReallyLongInt is equal to rOp
	// Return 1 if current ReallyLongInt is greater than rOp
	public int compareTo(ReallyLongInt rOp)
	{
		if(this.numberOfEntries > rOp.numberOfEntries)
		{
			return 1;
		}
		else if(this.numberOfEntries < rOp.numberOfEntries)
		{
			return -1;
		}
		else
		{
			//System.out.println("this: "+this.toString()+" right: "+ rOp.toString());
			Node curr = firstNode.prev;
			Node rightCurr = rOp.firstNode.prev;
			for(int i = 0; i < numberOfEntries; i++)
			{
				if(curr.data > rightCurr.data)
				{
					return 1;
				}
				else if(curr.data < rightCurr.data)
				{
					return -1;
				}
				else
				{
					curr = curr.prev;
					rightCurr = rightCurr.prev;
				}
			}
			return 0;
		}
	}

	// Is current ReallyLongInt equal to rightOp?
	public boolean equals(Object rightOp)
	{
		int comp = this.compareTo((ReallyLongInt)rightOp);
		if(comp == 0)
		{
			return true;
		}
		else
			return false;
	}

	// Mult. current ReallyLongInt by 10^num
	public void multTenToThe(int num)
	{
		int n = 0;
		//Node first = firstNode;
		Node last = firstNode.prev;
		Node first = firstNode;
		Node newNode;
		while(n < num)
		{
			add(1,0);
			n++;
		}
	}

	// Divide current ReallyLongInt by 10^num
	public void divTenToThe(int num)
	{
		int n = 0;
		//System.out.println(this);
		if(num > numberOfEntries)
		{
			clear();
			this.add(0);
			return;
		}
		leftShift(num);


	}

}
