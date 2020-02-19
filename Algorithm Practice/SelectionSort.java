import java.util.*;

public class SelectionSort
{
  public static void main(String args[])
  {
    int amtNums;
    int maxNum;
    int[] arr;
    System.out.print(9/10);
    Scanner s = new Scanner(System.in);
    System.out.print("Enter the amount of numbers you want to sort: ");
    amtNums = s.nextInt();
    System.out.print("Enter the max number: ");
    maxNum = s.nextInt();
    s.close();

    arr = randomArr(amtNums, maxNum);

    System.out.println("\n\n\t\t\t\tUnsorted array\n"+Arrays.toString(arr));

    int[] arrSorted = sort(arr);
    System.out.println("\n\n\t\t\t\tSorted array\n"+Arrays.toString(arrSorted));

  }
  public static int[] randomArr(int size,int high)
  {
    Random r = new Random();
    int[] array = new int[size];

    for(int i = 0; i < size; i++)
    {
      array[i] = r.nextInt(high);
    }
    return array;
  }
  public static int[] sort(int[] unsorted)
  {
    //int[] sortedArr = new int[unsorted.length];
    int minIndex = 0;
    int min = 0;
    int times = 0;
    for(int i = 0; i < unsorted.length-1; i++)
    {
      minIndex = i;
      for(int j = i+1; j < unsorted.length; j++)
      {
        if(unsorted[j]<unsorted[minIndex])
        {
          minIndex = j;
        }
        times++;
      }
      times++;
      min = unsorted[minIndex];
      unsorted[minIndex] = unsorted[i];
      unsorted[i] = min;
    }
    System.out.println("Times: "+times);
    return unsorted;
  }
}
