package com.zappos.intern;
import org.paukov.combinatorics.Factory;
import org.paukov.combinatorics.Generator;
import org.paukov.combinatorics.ICombinatoricsVector;

public class Zappos {
	private final static int total=147;
	private final static int k=3;
	
	private ICombinatoricsVector<Integer> closest=null;
	private int close=0;
	private int delta=total;
	
//	public void Zappos(){
//		
//	}
	
	public static void main(String args[]){
		Integer[] arr=new Integer[] { 1,2,50,40,59,69,20,79,440,29,102,440 };
		Zappos z=new Zappos();
		z.findClosest(arr);
	}
	
	public void findClosest(Integer[] arr){
		ICombinatoricsVector<Integer> initialVector = Factory.createVector(arr);
		int cnt=0;
	    Generator<Integer> gen = Factory.createSimpleCombinationGenerator(initialVector, k);
	    for (ICombinatoricsVector<Integer> combination : gen) {
	    	//cnt++;
	    	int sum=0;
	    	for(int i=0;i<k;i++){
	    		sum=sum+combination.getValue(i);
	    	}
	    	int d=Math.abs(total-sum);
	    	if(d<delta){
	    		delta=d;
	    		//cnt++;
	    		close=sum;
	    		System.out.println(combination+"--"+sum+"--");
	    	}
	    }
	    System.out.println(close);
	}
	
	   
}
