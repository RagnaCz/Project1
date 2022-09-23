package Project1;

import java.util.*;
import java.io.*;
////////////////////////////////////////////////////////////////////////////////
//Exception handeling
////////////////////////////////////////////////////////////////////////////////
class MissingInputException extends Exception {
    private int length;
    public MissingInputException(int i) {
        this.length = i;
    }
    public void data(){
        System.out.println(": "+(7-length) + " columns missing");
    }
}
class OverInputException extends Exception {
    private int length;
    public OverInputException(int i) {
        this.length = i;
    }  
    public void data(){
        System.out.println(": "+(length-7) + " columns upperbounding");
    }
}

class InvalidInputException extends Exception{
    private int num;
    public InvalidInputException(int i) {
        this.num = i;
    }  
    public void data(){
        System.out.println(": For input: "+num);
    }
}
////////////////////////////////////////////////////////////////////////////////
//Menu Class
////////////////////////////////////////////////////////////////////////////////
class Menu implements Comparable<Menu>{
    private String name;
    private int price;
    private int totalDishes = 0;
    private boolean cheapestmenu = false;
    
    //Constructor
    public Menu(String name,int price){
        this.name = name;
        this.price = price;
    }    

    //Comparable
    @Override
    public int compareTo(Menu other) {
        if (this.price < other.price) return 1;
        else return -1;
    }
    
    //get method
    public int getPrice()                 {return price;}
    public String getName()               {return name;}
    public int getTotaldishes()           {return totalDishes;}
    public boolean getIndexCheapestMenu() {return cheapestmenu;}
    
    //set method
    public void setCheapIndex()           {cheapestmenu = true;}
    public void addTotaldishes(int dish)  {this.totalDishes+=dish;}
    
    //Print data method
    public void getdata(){
        System.out.print(String.format("%-24s",name) + " Price = " + String.format("%3d",price) + "   total delivery = " + totalDishes + "\n");
    }
}
////////////////////////////////////////////////////////////////////////////////
//Order Class
////////////////////////////////////////////////////////////////////////////////
class Order{
    private int ID;
    private int orderbill = 0;
    private int finalbill = 0;
    private int [] orderlist = {0,0,0,0,0};
    private int freeCheapestMenuqty = 0;
    int index_cheap = 0;
    
    //Process method from requirement
    public void process(int price,int index,ArrayList<Menu> AL1,ArrayList<Customer> AL3){
        orderbill += orderlist[index]*price;
        if(index == 4){
            for(int i = 0 ; i < 5 ; i++){ if(AL1.get(i).getIndexCheapestMenu()){index_cheap = i;break;}} 
            freeCheapestMenuqty =  + (orderbill/1000);
            for(int i = 0 ; i < AL3.size() ; i++){
                if(AL3.get(i).havebillID(ID)){
                    AL3.get(i).updatePoints(orderbill);
                    AL3.get(i).setPointhistory(AL3.get(i).getpoints());
                    if(AL3.get(i).getpoints()>=500){
                        AL3.get(i).usePoints();
                        AL3.get(i).setPointhistory(AL3.get(i).getpoints());
                        finalbill = ((orderbill*95)/100) + 40;
                    }else{
                        AL3.get(i).setPointhistory(AL3.get(i).getpoints());
                        finalbill = orderbill + 40;
                    }
                }
            }
            AL1.get(index_cheap).addTotaldishes(freeCheapestMenuqty);
        } 
    }
    
    //Constructor
    public Order(int id,int [] order){
        this.ID = id;
        orderlist = order;
    }
    
    //Print data method
    public void showBill(ArrayList<Menu> AL1,ArrayList<Customer> AL3){
        String name = "";
        int index = 0;
        for(int i = 0 ; i < AL3.size() ; i++){
            if(AL3.get(i).havebillID(ID)){
                name = AL3.get(i).getname();
                index = i;
            }
        }
        System.out.print("Order " + String.format("%2d,",ID) + name +  " >> ");
        for(int i = 0 ; i < 5 ; i++){
            System.out.print(AL1.get(i).getName() + String.format(" (%2d)    ",orderlist[i]));
        }
        System.out.println("\n                  order bill = " + String.format("%,6d",orderbill) + "     current points = " + String.format("%,5d",AL3.get(index).getPointhistory(AL3.get(index).getpointindex())) + "    free " + AL1.get(index_cheap).getName() + " = " + freeCheapestMenuqty);
        System.out.println("                  final bill = " + String.format("%,6d",finalbill) + "     current points = " + String.format("%,5d",AL3.get(index).getPointhistory(AL3.get(index).getpointindex())) + "\n");
    }
}
////////////////////////////////////////////////////////////////////////////////
//Customer Class
////////////////////////////////////////////////////////////////////////////////
class Customer implements Comparable<Customer>{
    private String name;
    private int points = 0;
    private double pointlong = 0;
    private int index_point = 0;
    
    //ArrayList for Customer
    private ArrayList<Integer> billID = new ArrayList<>();
    private ArrayList<Integer> point_history = new ArrayList<>();
    
    //Constructor
    public Customer(String name, int index){
        this.name = name;
        billID.add(index);
    }
    
    //Comparable
    @Override
    public int compareTo(Customer other) {
        if (this.points < other.points)       return 1;
        else return -1;
    }
    
    //get method
    public boolean havebillID(int ID)      {return billID.contains(ID);}
    public String getname()                {return name;}
    public int getsizepointhistory()       {return point_history.size();}
    public int getPointhistory(int index)  {return point_history.get(index);}
    public int getpointindex()             {index_point++;return index_point-1;}
    public int getpoints()                 {return points;}
        
    //set method 
    public void updatePoints(int points){
        pointlong = Double.valueOf(points)/10 ;
        if(pointlong > points/10)
            this.points = this.points + points/10 + 1;
        else{
           this.points = this.points + points/10; 
        }
    }
    public void updatebill(int index)      {billID.add(index);}
    public void usePoints()                {points -=500;}
    public void setPointhistory(int point) {point_history.add(point);} 
    
    //Print data method
    public void getdata(){
        System.out.println(name + " points = " + String.format("%5d",points));
    }
} 
////////////////////////////////////////////////////////////////////////////////
//Class that store location of file, open file and Exception handling
////////////////////////////////////////////////////////////////////////////////
class MyInputReader<T>{
    private String path, fileName, fileName2;
    boolean opensuccess = false;
    Scanner sc;
    
    //Constructor
    public MyInputReader(String p, String fn, String fn2)                
    {   
        path = p;
        fileName = fn;
        fileName2 = fn2;
        sc = new Scanner(System.in);
    }
    
    //get data from openfile method, have line and arraylist in param for menu only
    public void processLine(String line, ArrayList<Menu> AL)
    {
        try
        {
            String[] buf = line.split(",");
            for(int i = 0 ; i < buf.length ; i++){
                buf[i] = buf[i].trim();
            }
            
            AL.add(new Menu(buf[0],Integer.parseInt(buf[1])));
            int cheapIndex = 0;
            int temp_price = 0;
            if(AL.size() == 5){
                for (int i = 0 ; i < AL.size() ; i++) {
                    if(i==0) {temp_price = AL.get(i).getPrice();cheapIndex = 0;}
                    else if(AL.get(i).getPrice()<temp_price){
                            cheapIndex = i;
                            temp_price = AL.get(i).getPrice();
                    }
                }
                AL.get(cheapIndex).setCheapIndex();
            }
        }
        catch(RuntimeException e)  //runtime exception
        {   
            System.out.println(e); 
            System.out.println(line+"\n");
        }
    }
    //get data from openfile method, have line and arraylist in param for order and customer
    public void processLine2(String line, ArrayList<Menu> AL1 ,ArrayList<Order> AL2, ArrayList<Customer> AL3) throws Exception
    {
        try
        {
            String[] buf = line.split(",");
            int buf_quantity[] = {0,0,0,0,0};
            boolean hasError = false;
            boolean error = false;
            Exception Error = new Exception();
            for(int i = 0 ; i < buf.length ; i++){
                buf[i] = buf[i].trim();
            }
            for(int i = 2 ; i < 8 ; i++ ){
                try{  
                    if(i>=2 && i < buf.length)buf_quantity[i-2] = Integer.parseInt(buf[i]);             
                    if(buf.length < 7 && i == 7 ){
                        Error = new MissingInputException(buf.length);
                        error = true;
                    }else if(buf.length > 7 && i == 7){
                        Error = new OverInputException(buf.length); 
                        error = true;
                    }else if(i>=2 && i < buf.length){
                       if(Integer.parseInt(buf[i]) < 0){
                        Error = new InvalidInputException(Integer.parseInt(buf[i]));
                        error = true;}
                    }
                if(error){error=false;throw Error;}
                }catch(RuntimeException e){
                    System.out.println(e);
                    buf[i] = "0";
                    buf_quantity[i-2] = Integer.parseInt(buf[i]); 
                    hasError = true;
                    error = false;
                }catch(MissingInputException e){
                    System.out.print(e);
                    e.data();
                    String[] temp = new String[7];
                    System.arraycopy(buf, 0, temp, 0, buf.length);
                    for(int k = 0 ; k < 7-buf.length ;k++){
                        temp[buf.length+k] = "0";
                    }
                    buf = temp;
                    hasError = true;
                    error = false;
                }catch(OverInputException e){
                    System.out.print(e);
                    e.data();
                    String[] temp = new String[7];
                    System.arraycopy(buf, 0, temp, 0, 7);
                    buf = temp;
                    hasError = true;
                    error = false;
                }catch(InvalidInputException e){
                    System.out.print(e);
                    e.data();
                    buf[i] = "0";
                    buf_quantity[i-2] = Integer.parseInt(buf[i]); 
                    hasError = true;
                    error = false;
                }finally{
                    if(i<buf.length)AL1.get(i-2).addTotaldishes(buf_quantity[i-2]);
                }
            }
            
            if(hasError){
                System.out.print("Original [" + line + "]  -->  Correction  [" + buf[0] +", "+ buf[1]);
                for(int i=0; i<5; i++) System.out.print(",  "+ buf_quantity[i]);
                System.out.print("]\n\n");
            }
            boolean check_empty = true;
            int index = 0;
            if(AL3.isEmpty()==false){
                for(int i = 0 ; i < AL3.size() ; i++){
                    if(AL3.get(i).getname().equalsIgnoreCase(buf[1])==true){
                        check_empty = false;
                        index = i;
                    } 
                }
            }
            if(check_empty){
                AL3.add(new Customer(buf[1],Integer.parseInt(buf[0])));
            }else{
                AL3.get(index).updatebill(Integer.parseInt(buf[0]));
            }
            AL2.add(new Order(Integer.parseInt(buf[0]),buf_quantity));
            for(int i = 0 ; i < 5 ; i++ ) AL2.get(Integer.parseInt(buf[0])-1).process(AL1.get(i).getPrice(), i , AL1 , AL3);
        }
        catch(RuntimeException e)  //runtime exception
        {   
            System.out.println(e); 
            System.out.println(line+"\n");
        }

    }
     
     public void openfile(ArrayList<Menu> AL1,ArrayList<Order> AL2, ArrayList<Customer> AL3) throws Exception{
         opensuccess = false;
         String fn = fileName;
         for( int k = 0 ; k < 2 ; k++ ){
         while(!opensuccess){
             try(
                     Scanner fileread = new Scanner(new File(path + fn));
                ){
                    if(k == 0)System.out.println("Read menus from file " + path + fn);
                    else System.out.println("Read orders from file " + path + fn);
                    System.out.println();
                    opensuccess = true;
                    if(k==0){
                        while(fileread.hasNext()){
                            processLine(fileread.nextLine(),AL1);
                        }
                    }else{
                        while(fileread.hasNext()){
                            processLine2(fileread.nextLine(),AL1,AL2,AL3);
                        }
                    }
             }catch(FileNotFoundException e){
                System.out.println(e);
                switch(k){
                    case 0 -> {
                        System.out.println("Enter file name for menus = ");
                        fileName = sc.nextLine();
                        fn = fileName;
                     }
                    case 1 -> {
                        System.out.println("Enter file name for orders = ");
                        fileName2 = sc.nextLine();
                        fn = fileName2;
                     }
                }
            }
        }
         fn = fileName2;
         opensuccess = false;
        }

    }
}
////////////////////////////////////////////////////////////////////////////////
//FoodDelivery for store main method
////////////////////////////////////////////////////////////////////////////////
public class FoodDelivery{
    public static void main(String [] args) throws Exception{
        //Directory
        String path    = "src/main/java/Project1/";
	String [] filename = {"menus.txt","orders.txt","orders_errors.txt"};
        
        ArrayList<Menu> menu_list = new ArrayList<>(); //AL1
        ArrayList<Order> order_list = new ArrayList<>();//AL2
        ArrayList<Customer> customer_list = new ArrayList<>();//AL3
        
        MyInputReader inp = new MyInputReader(path,filename[0],filename[2]); 
        
        inp.openfile(menu_list,order_list,customer_list);
        
        System.out.println("\n=== Order processing ===");
        for(int i = 0 ; i < order_list.size() ; i++){
            order_list.get(i).showBill(menu_list,customer_list);
        }
        
        System.out.println("\n=== Customer summary ===");
        Collections.sort(customer_list);
        for(int i = 0 ; i < customer_list.size() ; i++){
            customer_list.get(i).getdata();
        }
        
        System.out.println("\n=== Menu summary ===");
        Collections.sort(menu_list);
        for(int i = 0 ; i < menu_list.size() ; i++){
            menu_list.get(i).getdata();
        }
        
    }
}