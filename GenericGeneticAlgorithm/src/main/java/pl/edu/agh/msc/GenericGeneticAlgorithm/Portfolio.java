package pl.edu.agh.msc.GenericGeneticAlgorithm;

import java.util.ArrayList;

public class Portfolio {

	private static final double PORTFOLIO_START_VAL = 100.0;
	private int size;
	private ArrayList<Double> portfolio;
	private double value;
	
	public Portfolio(int size){
		this.size = size;
		portfolio = new ArrayList<Double>(size);
		
		for(int i = 0; i < size; i++){
			portfolio.add(0.0);
		}
		
		this.value = PORTFOLIO_START_VAL;
	}
	
	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public ArrayList<Double> getPortfolio() {
		return portfolio;
	}

	public void setPortfolio(ArrayList<Double> portfolio) {
		this.portfolio = portfolio;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
}
