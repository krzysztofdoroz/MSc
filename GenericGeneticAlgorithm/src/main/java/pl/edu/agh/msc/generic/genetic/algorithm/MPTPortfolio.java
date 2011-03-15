package pl.edu.agh.msc.generic.genetic.algorithm;


public class MPTPortfolio extends Portfolio {

	/**
	 * 
	 */
	private static final long serialVersionUID = 299711241408204220L;
	private double risk = -1.0;

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
