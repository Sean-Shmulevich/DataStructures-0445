import java.util.*;

public class SimBank
{
  int numTellers;
  boolean bankType;
  double simLength;
  double pplPerHour;
  double aveTransLen;
  int maxCustomers;
  long seed;

  int completions = 0;
  int arrivals = 0;
  int numWaited = 0;

  PriorityQueue<SimEvent> eventPQ = new PriorityQueue<SimEvent>();
  ArrayList<Customer> completedCustomers = new ArrayList<Customer>();
  ArrayList<Customer> notServed = new ArrayList<Customer>();

  // Create bank objects passing following parameters to constructor (in order):
  //    number of tellers (int)
  //    whether bank uses a single queue (true) or multiple queues (false)
  //    number of hours to run simulation (double)
  //    arrival rate of customers (double, in arrivals per hour)
  //    ave transaction length (double, in minutes)
  //    max customers allowed to wait (int, not counting those being served)
  //    seed for initilializing RandDist object (see RandDist.java)
  public SimBank(int tellers, boolean queueType, double hours, double perHour,  double transLength, int max, long sed)
  {
    numTellers = tellers;
    bankType = queueType;
    simLength = hours;
    pplPerHour = perHour;
    aveTransLen = transLength;
    maxCustomers = max;
    seed = sed;
  }

  ArrayList<LinkedList<Customer>> lines;

  public void runSimulation()
  {

    SimEvent curr;
    double curr_time = 0.0;

    Customer cust;
    int customerId = 1;


    boolean full;

    double arr_rate_min = pplPerHour/60;
		double serve_rate = 1/aveTransLen;
    double runMin = simLength*60;
    double stop_time = curr_time + runMin;

    lines = getLines();
    ArrayList<Teller> t = new ArrayList<Teller>();

    RandDist R = new RandDist(seed);

    //creates firest arrival event
    double next_arr_min = R.exponential(arr_rate_min);
		ArrivalEvent next_arrival = new ArrivalEvent(next_arr_min);
		eventPQ.offer(next_arrival);
    //System.out.println("YO: "+next_arrival.get_e_time());

    //initializes tellers
    for(int i = 0; i < numTellers; i++)
    {
      t.add(new Teller(i));
    }

    while(eventPQ.size() > 0)
    {
      ///output is iterating through customers
      //customers are in the order of when they leave the bank
      //not when they are generated
      curr = eventPQ.poll();
      curr_time = curr.get_e_time();

      //System.out.println(curr_time);

      if (curr instanceof ArrivalEvent)
			{
        cust = new Customer(customerId, curr_time);
        customerId++;
        //set arrival time to time of current event
        cust.setArrivalT(curr_time);
        //generate and set service time
        double serve_time = R.exponential(serve_rate);
        cust.setServiceT(serve_time);

        //put them into a line or teller

        //first check for an avalable teller

        //else (no available teller)
        //go into line with least ppl


        arrivals++;

        //should be able to take ppl off of the line and put them back;
        //for all available tellers

        //Function checks if there is a free teller
        //if there is a free teller then the function will return
        //the index of that teller
        int freeTeller = freeTellerIndex(t);
        //System.out.println(freeTeller);
        //there is an available teller
        //if free teller does not equal -1


        //this means that the customer came  into the bank and there was a free teller right away
        if(freeTeller != -1)
        {
          //set start time
          cust.setStartT(curr_time);
          //add customer to teller
          t.get(freeTeller).addCust(cust);
          //set start transaction time
          cust.setStartT(curr_time);
          //assign customer to teller index
          cust.setTeller(freeTeller);
          //generate ending time for transaction
          double finish_time = curr_time + cust.getServiceT();
          //assign end time to customer
          cust.setEndT(finish_time);
          //generate and add new completion loc event
          CompletionLocEvent next_complete = new CompletionLocEvent(finish_time,freeTeller);
          eventPQ.offer(next_complete);
        }
        //add customer to line
        else
        {
          //returns true if all lines are full
          //puts customer into shortest line if all lines are full and then Returns false
          full = putInLine(cust);
          if(full)
          {
            //adds to list of not served customers
            notServed.add(cust);
          }
          else
          {

            numWaited++;
          }
          //System.out.println(full);
        }

        //considers the time of the next arrival
        next_arr_min = curr_time + R.exponential(arr_rate_min);
        //if generated arriaval time next_arr_min is less then stop time
        //then a new arrival event is generated
				if (next_arr_min <= stop_time)
				{
          //created new arrival event and adds to PriorityQueue
					next_arrival = new ArrivalEvent(next_arr_min);
					eventPQ.offer(next_arrival);
				}
      }
      //if the next event is a completion event
      else if(curr instanceof CompletionLocEvent)
			{
        int tellerIndex;
        Teller doneTeller;
        Customer completedCust;
				CompletionLocEvent thisComp = (CompletionLocEvent)curr;


        completions++;


        //variable for the location of the teller where the completionLocEvent happened
        tellerIndex = thisComp.getLoc();
        //teller with the completed transaction
        doneTeller = t.get(tellerIndex);
        //remove cust changes status of teller and returns customer with the completed transaction
        completedCust = doneTeller.removeCust();

        //add the current customer to the list of the customers that have left
        completedCustomers.add(completedCust);

        if(isLine(tellerIndex))//there is a line behind the current teller
        {
          Customer newCust;
          //if the bank is a single queue implementation
          //take next customer from the only line in the customer list
          //newCust or the next customer of the line will have their completion processed
          if(bankType)
          {
            newCust = lines.get(0).removeFirst();

            doneTeller.addCust(newCust);
          }
          else
          {
            //System.out.println(lines.get(tellerIndex));
            newCust = lines.get(tellerIndex).removeFirst();
            //System.out.println();
            doneTeller.addCust(newCust);
          }
          //processes customer completion event and adds to pq
          newCust.setStartT(curr_time);
          newCust.setTeller(tellerIndex);
          double finish_time = curr_time + newCust.getServiceT();
          newCust.setEndT(finish_time);
          CompletionLocEvent next_complete = new CompletionLocEvent(finish_time,tellerIndex);
          eventPQ.offer(next_complete);
        }
			}
    }
  }
  //checks of there is a line
  private boolean isLine(int index)
  {
    //for bank with one line
    if(lines.size() == 1)
    {
      if(lines.get(0).size() > 0)
      {
        return true;
      }
    }
    //bank with multiple lines
    else
    {
      if(lines.get(index).size() > 0)
      {
        return true;
      }
    }
    return false;
  }
  //cheks if there is a free teller and returns index of that teller
  //if no free teller return -1
  private int freeTellerIndex(ArrayList<Teller> t)
  {
    for(int k = 0; k < t.size(); k++)
    {
      if(!(t.get(k).isBusy()))
      {
        return k;
      }
    }
    return -1;
  }
  //returns the number of customers waiting in the line
  private int numWaiting(ArrayList<LinkedList<Customer>> lines)
  {
    int sum = 0;
    for(int i = 0; i < lines.size();i++)
    {
      sum += lines.get(i).size();
    }
    return sum;
  }
  //puts customer into shortest line
  private boolean putInLine(Customer cust)
  {
    int min = Integer.MAX_VALUE;
    int minIndex = 0;
    int sumLength = 0;

    //finds shortest length
    for(int i = 0; i < lines.size(); i++)
    {
      sumLength += lines.get(i).size();
      if(lines.get(i).size() < min)
      {
        min = lines.get(i).size();
      }
    }
    //finds first index of shortest length
    for(int i = 0; i < lines.size(); i++)
    {
      //THIS IS THE REASON WHY my results are different from ramirez for the
      //multi queue implementation
      //he did this differently somehow
      if (lines.get(i).size() == min)
      {
        //count number of lines with size equaling min
        minIndex = i;
        //breaks after first index of minIndex is found
        break;
      }
    }
    //System.out.println("Min index or index of shortest line: "+minIndex+" Size of shortest Line: "+lines.get(minIndex).size()+ " sumLength: "+sumLength+" Max Customers: "+maxCustomers);
    //lines are full
    if(sumLength >= maxCustomers)
    {
      return true;
    }
    else
    {
      //puts customer into line
      cust.setQueue(minIndex);
      lines.get(minIndex).add(cust);
      return false;
    }
  }
  public void showResults()
  {
    int served = (arrivals - notServed.size());
    //System.out.println("Arrivals: "+arrivals+"\tNot Served: "+notServed.size()+"\tservedCust: "+served);
    System.out.println("Customer  Arrival    Service  Queue  Teller  Time Serv  Time Cust  Time Serv  Time Spent");
    System.out.println("   Id      Time       Time     Loc    Loc     Begins      Waits      Ends       in Sys  ");
    System.out.println("--------  -------   --------  -----  ------  ---------  ---------  ---------  ----------");
    //System.out.printf("\t\tAdding next ArrivalEvent for time: %6.2f \n", next_arr_min);
    for(Customer c : completedCustomers)
    {
      //System.out.println("\t\t"+(c.getId())+"   |  "+ round(c.getArrivalT())+"   |   "+round(c.getServiceT())+"  |  "+c.getQueue()+"  |  "+c.getTeller()+"    |   "+round(c.getStartT())+"   |   "+round(c.getWaitT())+"    |   "+round(c.getEndT())+"   |   "+round(c.getInSystem()));
      System.out.printf("   %d\t %6.2f\t   %6.2f\t%d\t%d     %6.2f\t  %6.2f    %6.2f \t%6.2f \n", c.getId(), c.getArrivalT(),c.getServiceT(),c.getQueue(),c.getTeller(), c.getStartT(), c.getWaitT(), c.getEndT(), c.getInSystem());
    }
    if(notServed.size()>0)
    {
      System.out.println("\n\n\n");
      System.out.println("Customers who did not stay:\n");
      System.out.println("Customer  Arrival    Service");
      System.out.println("   Id      Time       Time  ");
      System.out.println("--------  -------    -------");
    }
    else
    {
      System.out.println("\n\n\n");
      System.out.println("All customers served");
    }
    for(Customer c : notServed)
    {
      //System.out.println(" | "+c.getId()+"  |  "+round(c.getArrivalT())+"   |  "+round(c.getServiceT())+"  ");
      System.out.printf("   %d\t  %6.2f    %6.2f \n", c.getId(), c.getArrivalT(), c.getServiceT());
    }
    System.out.println("\nNumber of Tellers: "+ numTellers);
    System.out.println("Number of Queues: "+ lines.size());
    System.out.println("Max number allowed to wait: "+ maxCustomers);
    System.out.println("Customer arrival rate (per hr): "+pplPerHour);
    System.out.println("Customer service time (ave min): "+ aveTransLen);
    System.out.println("Number of customers arrived: "+ arrivals);
    System.out.println("Number of customers served: "+  served);
    System.out.println("Num. Turned Away: "+ notServed.size());
    System.out.println("Num. who waited: "+ (numWaited));
    System.out.println("Average wait: "+ aveWait());
    System.out.println("Max wait: "+ maxWait());
    System.out.println("Std. Dev. Wait: "+ round(stdDev()));
    System.out.println("Ave. Service: "+aveService());
    System.out.println("Ave. Waiter Wait: "+aveWaitWaited());
    System.out.println("Ave. in System: "+aveSystem());
  }
  //average time in system
  public double aveSystem()
  {
    double timeSum = 0;
    for(Customer c : completedCustomers)
    {
      timeSum += c.getInSystem();
    }
    return round(timeSum/completedCustomers.size());
  }
  //ave time for service
  private double aveService()
  {
    double timeSum = 0;
    for(Customer c : completedCustomers)
    {
      timeSum += c.getServiceT();
    }
    return round(timeSum/completedCustomers.size());
  }
  //standard deviation
  private double stdDev()
  {
    double dev = 0;
    double aveWait = aveWait();
    int numCust = completedCustomers.size();

    //list of the (averageWait-currCustomerWait)^squared
    ArrayList<Double> diff =  new ArrayList<Double>();
    double sum = 0;
    for(Customer c: completedCustomers)
    {
      //adds value to diff
      diff.add(square(Double.valueOf(c.getWaitT()-aveWait)));
    }
    //creates sum of values in diff
    for(Double d : diff)
    {
      sum += d.doubleValue();
    }
    //sum of diff / amt of customers
    dev = sum/completedCustomers.size();
    //qurt of this number
    dev = Math.sqrt(dev);
    //return standard dev
    return dev;
  }
  //helper function for standard dev
  private Double square(Double d)
  {
    //convert Double to double
    double doub = d.doubleValue();
    //square double
    double squared = Math.pow(doub, 2);
    //return Double of double^2
    return Double.valueOf(squared);
  }
  //calculate average wait time for all customers
  private double aveWait()
  {
    double timeSum = 0;
    for(Customer c : completedCustomers)
    {
      timeSum += c.getWaitT();
    }
    return round(timeSum/completedCustomers.size());
  }
  //calculate average wait time for all customers who needed to wait in line
  private double aveWaitWaited()
  {
    double timeSum = 0;
    for(Customer c : completedCustomers)
    {
      timeSum += c.getWaitT();
    }
    return round(timeSum/numWaited);
  }
  //find max wait time
  private double maxWait()
  {
    double max = 0;
    for(Customer c : completedCustomers)
    {
      if(c.getWaitT() > max)
      {
        max = c.getWaitT();
      }
    }
    return round(max);
  }
  //round double and return double
  private double round(double num)
  {
    return (double)(Math.round(num * 100d) / 100d);
  }
  //generate the array list of linked list of customers
  private ArrayList<LinkedList<Customer>> getLines()
  {
    ArrayList<LinkedList<Customer>> a = new ArrayList<LinkedList<Customer>>();
    if(!bankType)
    {
      //System.out.println(numTellers);
      for (int i = 0; i < numTellers; i++)
      {
        a.add(new LinkedList<Customer>());
        //System.out.println(a.toString());
      }
    }
    else
    {
      //System.out.println("HI");
      a.add(new LinkedList<Customer>());
    }
    return a;
  }
  //Function for testing purposes
  public static void main(String args[])
  {
    SimBank sqbank = new SimBank(6, true, 8, 30, 8, 10, 111);
    SimBank mqbank = new SimBank(6, false, 8, 30, 8, 10, 111);
    sqbank.runSimulation();
		sqbank.showResults();
		System.out.println();
		mqbank.runSimulation();
		mqbank.showResults();
    //sqbank.testList();
  }
}
