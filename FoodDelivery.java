package Project1;

import java.util.*;
import java.io.*;

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


class Menu{
    private String name;
    private int price;
    private int totalDishes = 0;
    
    
    public Menu(String name,int price){
        this.name = name;
        this.price = price;
    }
    public int getPrice(){
         return price;
    }
    public int getTotaldishes(){
        return totalDishes;
    }
    public void getdata(){
        System.out.println("Name = " + name + " Price = " + price);
    }
}

class Order{
    private int ID;
    private int orderbill = 0;
    private int [] orderlist = {0,0,0,0,0};
    
    public void process(){
        for(int i = 0 ; i < 5 ; i++){
            
        }
    }
    
    public Order(int id,int [] order){
        this.ID = id;
        orderlist = order;
    }
    
    public void getdata(){
        System.out.println(ID);
        for(int i = 0 ; i < orderlist.length ; i++){
            System.out.print(orderlist[i]+ " ");
        }
        System.out.println();
    }
}

class Customer{
    private String name;
    private int points;
    
    private ArrayList<Integer> billID = new ArrayList<>();
    
    public Customer(String name, int index){
        this.name = name;
        billID.add(index);
    }
    
    public void updatePoints(int points){
       this.points = this.points + points;
    }
    
    public void updatebill(int index){
        billID.add(index);
    }
    
    public String getname(){
        return name;
    }
    
    public int getpoints(){
        return points;
    }
    public void getdata(){
        System.out.println("name : "+name + "  point : " + points);
        System.out.print("BillList : ");
        for(int i = 0 ; i < billID.size() ; i++){
            System.out.print(billID.get(i) + " ");
        }
        System.out.println();
    }
} 

//Class that store location of file, open file and Exception handling
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
    
    //get data from openfile method, have line and arraylist in param
    public void processLine(String line, ArrayList<T> AL)
    {
        try
        {
            String[] buf = line.split(",");
            for(int i = 0 ; i < buf.length ; i++){
                buf[i] = buf[i].trim();
            }
            
            AL.add((T) new Menu(buf[0],Integer.parseInt(buf[1])));
        }
        catch(RuntimeException e)  //runtime exception
        {   
            System.out.println(e); 
            System.out.println(line+"\n");
        }
    }
    public void processLine2(String line, ArrayList<T> AL2, ArrayList<Customer> AL3) throws Exception
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
            for(int i = 2 ; i < 7 ; i++ ){
                try{   
                    buf_quantity[i-2] = Integer.parseInt(buf[i]);                    
                    if(buf.length < 7 && i == buf.length - 1){
                        Error = new MissingInputException(buf.length);
                        error = true;
                    }else if(buf.length > 7 && i == 6){
                        Error = new OverInputException(buf.length); 
                        error = true;
                    }else if(Integer.parseInt(buf[i]) < 0){
                        Error = new InvalidInputException(Integer.parseInt(buf[i]));
                        error = true;
                    }
                if(error){error=false;throw Error;}
                }catch(RuntimeException e){
                    System.out.println(e);
                    buf[i] = "0";
                    hasError = true;
                    error = false;i--;
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
                    error = false;i--;
                }catch(OverInputException e){
                    System.out.print(e);
                    e.data();
                    String[] temp = new String[7];
                    System.arraycopy(buf, 0, temp, 0, 7);
                    buf = temp;
                    hasError = true;
                    error = false;i--;
                }catch(InvalidInputException e){
                    System.out.print(e);
                    e.data();
                    buf[i] = "0";
                    hasError = true;
                    error = false;i--;
                }
            }
            
            if(hasError){
                System.out.print("Original [" + line + "]  -->  Correction  [" + buf[0] +", "+ buf[1]);
                for(int i=0; i<5; i++) System.out.print(",  "+ buf_quantity[i]);
                System.out.print("]\n\n");
            }
            AL2.add((T) new Order(Integer.parseInt(buf[0]),buf_quantity));
            
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
        }
        catch(RuntimeException e)  //runtime exception
        {   
            System.out.println(e); 
            System.out.println(line+"\n");
        }

    }
     
     public void openfile(ArrayList<T> AL1,ArrayList<T> AL2, ArrayList<Customer> AL3) throws Exception{
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
                            processLine2(fileread.nextLine(),AL2,AL3);
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
        
        for(int i = 0 ; i < 5 ; i++){
            menu_list.get(i).getdata();
        }
        for(int i = 0 ; i < order_list.size() ; i++){
            order_list.get(i).getdata();
        }
        for(int i = 0 ; i < customer_list.size() ; i++){
            customer_list.get(i).getdata();
        }
    }
}