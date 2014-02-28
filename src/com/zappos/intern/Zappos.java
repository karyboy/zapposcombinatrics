package com.zappos.intern;

import java.io.IOException;
import java.util.ArrayList;

import static us.monoid.web.Resty.*;
import us.monoid.json.JSONArray;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;


public class Zappos {
	private final static double total=580.00;
	private final static int k=4;
	
	private ArrayList<JSONObject> closest=null;
	private double close=0;
	private double delta=total;
	private Resty r;
	private String apikey="52ddafbe3ee659bad97fcce7c53592916a6bfd73";

	public Zappos(){
		r=new Resty();
	}
	
	public static void main(String args[]){
		Zappos z=new Zappos();
		z.getProducts("http://api.zappos.com/Search?term=boots");
		System.out.println(">>"+z.getClosest().toString());
	}
	
	public void getProducts(String url){
		try {
			JSONResource j = r.json(url+"&key="+apikey);
			this.iterateOnJSON(j.object());
		} catch (IOException e) {
			System.out.println("Cant Reach the API >> "+e.toString());
			// TODO Auto-generated catch block
			//e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void testCloseness(double sum,ArrayList<JSONObject> arr){
		double d=Math.abs(total-sum);
    	if(d<delta){
    		delta=d;
    		closest=arr;
    		close=sum;
    		System.out.println(arr.toString()+"--"+sum+"--");
    	}
	}
	
	public void iterateOnJSON(JSONObject json){
		if(json.has("results")){
			ArrayList<JSONObject> arr=new ArrayList<JSONObject>();
				try {
					JSONArray jsona=json.getJSONArray("results");
					for(int i=0;i<jsona.length();i++){
						JSONObject product = jsona.getJSONObject(i);
						arr.add(product);
					}
					//System.out.println(arr.toString());
					this.printCombination(arr, arr.size(), k);
				} catch (JSONException e) {
					System.out.println("results not found");
				}
		}
	}

	public void combinationUtil(ArrayList<JSONObject> arr, ArrayList<JSONObject> data, int start, int end, int index, int r)
	{
	    // Current combination is ready to be printed, print it
	    if (index == r)
	    {
	    	double sum=0;
	        for (int j=0; j<r; j++){
	        	try {
	        		String price=data.get(j).getString("price");
	        		price=price.substring(1,price.length());
	        		sum=sum+Double.parseDouble(price);
					//System.out.print(price+"|");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        }	
	        testCloseness(sum,data);
	        //System.out.print(sum);
	        //System.out.println();
	        return;
	    }
	 
	    
	    for (int i=start; i<=end && end-i+1 >= r-index; i++)
	    {	//System.out.println("--"+index);
	    	if(data.size()<r)
	    		data.add(index,arr.get(i)) ;
	    	else
	    		data.set(index, arr.get(i));
	        combinationUtil(arr, data, i+1, end, index+1, r);
	    }
	}
	
	void printCombination(ArrayList<JSONObject> arr, int n, int r)
	{
		ArrayList<JSONObject> data=new ArrayList<JSONObject>(3);
	    combinationUtil(arr, data, 0, n-1, 0, r);
	}
	
	public ArrayList<JSONObject> getClosest(){
		return closest;
	}
	   
}
