//memoized solution
import java.util.*;
public class Recur
{
  public static void main(String[] args)
  {
    System.out.println(recur(8));
  }
  public static int recur(int n)
  {
    int result;
    int[] arr = new int[n+1];
    if(arr[n] != 0)
    {
      System.out.print("here");
      return arr[n];
    }
    if(n == 1 || n == 2)
    {
      result = 1;
    }
    else
    {
      result = recur(n-1)+recur(n-2);

    }

    System.out.println(Arrays.toString(arr));
    arr[n] = result;
    return result;
  }
}
