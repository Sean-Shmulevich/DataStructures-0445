import java.util.*;
public class LinkedList<T>
{
  private Node firstNode;
	private int  numberOfEntries;

  public LinkedList()
  {
    firstNode = null;
		numberOfEntries = 0;
  }

   public void clear()
   {
     firstNode = null;
 		 numberOfEntries = 0;
   }

   public void add(T item)
   {
     Node newNode = new Node(item);
     if(numberOfEntries <= 0)
     {
       firstNode = newNode;
     }
     else
     {
       newNode.next = firstNode;
       firstNode = newNode;
     }
     numberOfEntries++;
   }

   public void add(T item, int position)
   {
     //need to make sure index does not go out of bounds and that
     //position is not greater then the logical size
     //position is greater then 0
     //if pos is 0 then it is a special case
     Node newNode = new Node(item);
     Node curr = firstNode;

     if(numberOfEntries == 0)
     {
       firstNode = newNode;
     }
     else if(position == numberOfEntries+1)
     {
       Node last = getNodeAt(position);
       last.next = newNode;
     }
     else if(position < 0 || position > numberOfEntries)
     {
       throw new IndexOutOfBoundsException("Illegal position given to add operation.");
     }
     else
     {
       for(int i = 0; i < position-1; i++)
       {
         curr = curr.next;
       }
       Node nodeBefore = curr;
       Node nodeAfter = nodeBefore.next;

       nodeBefore.next = newNode;
       newNode.next = nodeAfter;
     }
     numberOfEntries++;
   }
   public T remove(int position)
   {
     T result = null;

     assert !isEmpty();

     if(numberOfEntries == position && !(numberOfEntries == 0))
     {
       Node before = getNodeAt(position-1);
       Node remove = before.next;
       result = remove.data;

       remove = null;
     }
     else if (numberOfEntries == 0)
     {
       result = null;
     }
     else if (position == 0)
     {
       result = firstNode.data;
       firstNode = firstNode.next;
     }
     else
     {
       Node before = getNodeAt(position-1);
       Node removed = before.next;
       result = removed.data;
       Node after = removed.next;
       before.next = after;
     }
     numberOfEntries--;
     return result;
   }
   public boolean isEmpty()
   {
     return (numberOfEntries == 0);
   }
   public Node getNodeAt(int pos)
   {
     //return null if pos > numberOfEntries
     Node curr = firstNode;
     for(int i = 0; i < pos; i++)
     {
       curr = curr.next;
     }
     return curr;
   }

   public T remove()
   {
     T data;
     if(numberOfEntries == 0)
     {
       data = null;
     }
     else
     {
       Node curr = firstNode;
       data = curr.data;
       firstNode = firstNode.next;
       numberOfEntries--;
     }
     return data;
   }

   public String toString()
   {
     String s = "";
     Node curr = firstNode;

     while(curr != null)
     {
       s += (curr.data+" ");
       curr = curr.next;
     }
     return s;
   }

  private class Node
	{
      private T data; // Entry in list
      private Node next; // Link to next node

      private Node(T dataPortion)
      {
         data = dataPortion;
         next = null;
      } // end constructor

      private Node(T dataPortion, Node nextNode)
      {
         data = dataPortion;
         next = nextNode;
      } // end constructor

      private T getData()
      {
         return data;
      } // end getData

      private void setData(T newData)
      {
         data = newData;
      } // end setData

      private Node getNextNode()
      {
         return next;
      } // end getNextNode

      private void setNextNode(Node nextNode)
      {
         next = nextNode;
      } // end setNextNode
	} // end Node
  public T replace(int givenPosition, T newEntry)
  {
    T oldData = null;
    if(givenPosition < numberOfEntries && numberOfEntries >= 0)
    {
      Node givenNode = getNodeAt(givenPosition);
      oldData = givenNode.data;
      givenNode.data = newEntry;
    }
    else
      throw new IndexOutOfBoundsException("Illegal position given to replace operation.");
    return oldData;
  }
  //physical size
  public int physicalSize()
  {
    return Integer.MAX_VALUE;
  }
  //logical size
  public int size()
  {
    return numberOfEntries;
  }
  public static LinkedList<Integer> randomArr(int size,int high)
  {
    Random r = new Random();
    LinkedList<Integer> l = new LinkedList<Integer>();

    for(int i = 0; i < size; i++)
    {
      l.add(r.nextInt(high));
    }
    return l;
  }
  public static void main(String args[])
  {
    LinkedList<Integer> list = randomArr(99569, 500);

    System.out.println(list.toString());
    System.out.println(list.size());
    Integer i = list.remove(list.size()-1);
    /*
    while(i != null)
    {
      System.out.println(i);
      i = list.remove(list.size()-1);
    }
    //System.out.println(list.getNodeAt(3).data);

*/
  }
}
