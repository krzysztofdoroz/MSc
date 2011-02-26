package pl.edu.agh.msc.MPTGeneticAlgorithm;

import pl.edu.agh.msc.generic.genetic.algorithm.Portfolio;

public class MPTPortfolio extends Portfolio {

	private double risk;

	public MPTPortfolio(int size) {
		super(size);
	}

	public double getRisk() {
		return risk;
	}

	public void setRisk(double risk) {
		this.risk = risk;
	}
	
}
