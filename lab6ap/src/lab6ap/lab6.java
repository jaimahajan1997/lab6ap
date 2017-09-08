package lab6ap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
//Sources-https://stackoverflow.com/questions/15189949/making-a-generic-comparator-class,http://www.geeksforgeeks.org/binary-search-tree-set-1-search-and-insertion/
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

//JAI MAHAJAN
//2016154


class type{
	String type;
	int x;
	int y;
	String notCoordinate;
	type(String s){
		type=s;
	}
}
class knight{
	int x=0;
	int y=0;
	String name;
	Stack<type> stack ;
	knight(String nam,int xcor,int ycor){
		name=nam;
		x=xcor;
		y=ycor;
		stack=new Stack<type>() ;
	}
	void add(type a){
		stack.push(a);
	}

}
public  class lab6 {
	static class cmp implements Comparator<knight>{
		@Override
		public int compare(knight a,knight b){
			String n1=a.name;
			String n2=b.name;
			int res=n1.compareTo(n2);
			//System.out.println(n1+n2+res);
			return res;
		}
	}


public static <T> void main(String args[]) throws FileNotFoundException, UnsupportedEncodingException  {
	Scanner s=new Scanner(System.in);
	
	int n=s.nextInt();//no of knightss
	int x=s.nextInt();//total iterations
	int q_x=s.nextInt();//queen coordinate
	int q_y=s.nextInt();
	s.close();
	Comparator<knight> comparator = new cmp();
	PriorityQueue<knight> knights=new PriorityQueue<>(n,comparator);
	HashMap<Integer, HashMap<Integer, knight>> outerMap = new HashMap<Integer, HashMap<Integer, knight>>();
	PrintWriter w = new PrintWriter("./src/lab6ap/" + 1 + ".txt", "UTF-8");
	for (int t = 1; t<= n; t++) {
	File file = new File("C:/Users/Preeti/Desktop/JAVA/lab6ap/src/"+ t + ".txt");
//System.out.println(t);
    try {

        Scanner sc = new Scanner(file);
        String name=sc.next();
        int k_x=sc.nextInt();
        int k_y=sc.nextInt();
        
        int m=sc.nextInt();
        knight k_obj=new knight(name,k_x,k_y);
        try{
        	HashMap<Integer,knight> inner=outerMap.get(k_x);
        	try{
        		knight kn=inner.get(k_y);
        		
        	}
        	catch(Exception e){
            outerMap.get(k_x).put(k_y, k_obj);
        	}
        }
        catch(Exception e){
        	HashMap<Integer,knight> inner=new HashMap<Integer,knight>();
    		inner.put(k_y, k_obj);
    		outerMap.put(k_x, inner);
        }
        for (int i=0;i<m;i++){
        	String Type=sc.next();
        	type typ=new type(Type);
        	if (Type.equals("Coordinate")){
        		typ.x=sc.nextInt();
        		typ.y=sc.nextInt();
        		
        	}
        	else{
        		typ.notCoordinate=sc.next();
        	}
        	
        	k_obj.add(typ);
        }
        knights.add(k_obj);
    	
    	
        sc.close();
    } 
    catch (Exception e) {
        e.printStackTrace();
    }
    
	}
	System.out.println("Initial queue");
	for(knight k:knights){
		System.out.println(k.name);
	}
	//main loop for interations x
	for(int i=0;i<x;i++){
		boolean brake=false;
		knight remov=null;
		HashMap<knight,Boolean> rem=new HashMap<knight,Boolean>();
		for(knight k_obj:knights){
			try{
			if(rem.get(k_obj)){
				continue;
			}}
			catch(Exception e){
				;
			}
			boolean noexcep=false;
			w.println(String.format("<%d> <%s> <current_location (%d %d)>",i+1,k_obj.name,k_obj.x,k_obj.y));
			type pop=null;
			try{
			pop=k_obj.stack.pop();
			try{
				if(!pop.type.equals("Coordinate")){
					throw new NonCoordinateException(String.format("NonCoordinateException: Not a coordinate Exception  < %s >",pop.notCoordinate));
				}
				else{
					noexcep=true;
				}
				k_obj.x=pop.x;
				k_obj.y=pop.y;
				int k_x=k_obj.x;
				int k_y=k_obj.y;
				try{
		        	HashMap<Integer,knight> inner=outerMap.get(k_x);
		        	try{
		        		knight kn=inner.get(k_y);
		        		try{
		        		noexcep=false;	
		        		throw new OverlapException(String.format("OverlapException: Knights Overlap Exception  < %s >",kn.name));}
		        		catch(Exception e){
		        			remov=kn;
		        			rem.put(kn, true);
		        			outerMap.get(k_x).put(k_y, kn);
		        			if(e.getMessage()!=null){
							w.println(e.getMessage());}
		        		}
		        	}
		        	catch(Exception e){
		            outerMap.get(k_x).put(k_y, k_obj);
		        	}
		        }
		        catch(Exception e){
		        	HashMap<Integer,knight> inner=new HashMap<Integer,knight>();
		    		inner.put(k_y, k_obj);
		    		outerMap.put(k_x, inner);
		        }
				try{
				if(k_obj.x==q_x && k_obj.y==q_y){
					noexcep=false;	
					throw new QueenFoundException("QueenFoundException: Queen has been Found. Abort!");
					
					
				}}
				catch(Exception e){
					w.println(e.getMessage());
					brake=true;
					break;
				}
				}
				catch(NonCoordinateException e){
					w.println(e.getMessage());
				}
			
			}
			catch(Exception e){
				remov=k_obj;
    			rem.put(k_obj, true);
				outerMap.get(k_obj.x).put(null,null);
				try {
					noexcep=false;	
					throw new StackEmptyException(String.format("StackEmptyException: Stack Empty exception"));
				} catch (Exception e1) {
					w.println(e1.getMessage());
				}
				continue;
			}	
			if (noexcep){
				w.println(String.format("No exception <coordinates popped (%d %d)>",pop.x,pop.y));
			}
		}
		if (remov!=null){
			knights.remove(remov);
			remov=null;
		}
		if (brake){
			break;
		}
		
	}
	w.flush();
}


static class NonCoordinateException extends Exception {
	String msg;
	@Override
	public String getMessage(){
		return msg;
	}
	NonCoordinateException(String string)throws Exception{
		msg=string;
	}
}
static class QueenFoundException extends Exception {
	String msg;
	@Override
	public String getMessage(){
		return msg;
	}
	QueenFoundException(String string)throws Exception{
		msg=string;
	}
}
static class OverlapException extends Exception {
	String msg;
	@Override
	public String getMessage(){
		return msg;
	}
	OverlapException(String string)throws Exception{
		msg=string;
	}
}
static class StackEmptyException extends Exception {
	String msg;
	@Override
	public String getMessage(){
		return msg;
	}
	StackEmptyException(String string)throws Exception{
		msg=string;
	}
}
}
