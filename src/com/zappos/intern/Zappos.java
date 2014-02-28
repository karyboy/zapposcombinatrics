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
	private double total;
	private int k;
	private ArrayList<JSONObject> closest=null;
	private double amount=0;
	private double delta;
	private Resty r;
	private String apikey="52ddafbe3ee659bad97fcce7c53592916a6bfd73";

	/*
	 * The constructor takes in the total amount entered by the customer and the number of products
	 */
	
	public Zappos(double total,int k){
		this.r=new Resty();
		this.total=total;
		this.k=k;
		this.delta=total;
	}
	
	public static void main(String args[]){
		Zappos z=new Zappos(150.94,1);
		z.getProducts("http://api.zappos.com/Search?term=boots&limit=100");
		System.out.println(">>"+z.getClosest().toString()+"--"+z.getClosestAmount());
		z.getProducts("http://api.zappos.com/Search?term=boots&limit=100&page=2");
		System.out.println(">>"+z.getClosest().toString()+"--"+z.getClosestAmount());
	}
	
	/*
	 * fetch the json of products from the url passed. The key is automatically appended to the url.
	 * This function can be called as many times, and if the there is a combination of products whose total value 
	 * is closer to the amount in any of the subsequent requests, the closest variable will be updated.  
	 */
	
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
	
	/*
	 * checks the closeness to the total amount
	 */
	
	public void testCloseness(double sum,ArrayList<JSONObject> arr){
		double d=Math.abs(total-sum);
    	if(d<delta){
    		delta=d;
    		closest=arr;
    		amount=sum;
    		//System.out.println(arr.toString()+"--"+sum+"--");
    	}
	}
	
	/*
	 * iterates on json received from the API and forms an ArrayList of JSONObject's
	 */
	
	public void iterateOnJSON(JSONObject json){
		if(json.has("results")){
			ArrayList<JSONObject> arr=new ArrayList<JSONObject>();
				try {
					JSONArray jsona=json.getJSONArray("results");
					//System.out.println(jsona.length());
					for(int i=0;i<jsona.length();i++){
						JSONObject product = jsona.getJSONObject(i);
						arr.add(product);
					}
					//System.out.println(arr.toString());
					this.startCombination(arr, arr.size(), k);
				} catch (JSONException e) {
					System.out.println("results not found");
				}
		}
	}

	/*
	 * form k-combinations of product JSONObjects
	 */
	
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
	
	/*
	 * boots recursive combining of JSONObjects
	 */
	
	void startCombination(ArrayList<JSONObject> arr, int n, int r)
	{
		ArrayList<JSONObject> data=new ArrayList<JSONObject>(3);
	    combinationUtil(arr, data, 0, n-1, 0, r);
	}
	
	/*
	 * gets the current closest k-combination of products.
	 */
	
	public ArrayList<JSONObject> getClosest(){
		return closest;
	}
	   
	/*
	 * gets the current closest amount value
	 */
	
	public double getClosestAmount(){
		return amount;
	}
}
