package pl.edu.agh.msc.GenericGeneticAlgorithm;

import java.util.List;

public class Portfolio {

	private int size;
	private List<Double> portfolio;
	
	public Portfolio(int size){
		this.size = size;
	}
	
	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
}
