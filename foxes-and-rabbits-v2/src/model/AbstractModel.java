package model;

import java.util.ArrayList;
import java.util.List;


import view.AbstractView;


public abstract class AbstractModel {

	private List<AbstractView> view;
	
	public AbstractModel(){
		view = new ArrayList<AbstractView>();
	}
	
	public void addView(AbstractView view){
		this.view.add(view);	
	}

	
	 //ublic void showStatusAll(){
     //   for(AbstractView v: view) v.showStatus();
 //
	//

	public void showStatusAll() {
		 for(AbstractView v: view) v.showStatus();
		
	}

//	public void showStatusAll() {
	//	for(int i=0; i<view.size();i++){
	//		((AbstractView) view.get(i)).showStatus();
	//		}
		//}

	 /**
     * Method to reset all subclasses.
     */
    public abstract void reset();
	
	}



