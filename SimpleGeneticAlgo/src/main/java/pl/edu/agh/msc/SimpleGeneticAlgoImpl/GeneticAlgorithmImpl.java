package pl.edu.agh.msc.SimpleGeneticAlgoImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pl.edu.agh.msc.GenericGeneticAlgorithm.IGeneticAlgorithm;
import pl.edu.agh.msc.GenericGeneticAlgorithm.Portfolio;

public class GeneticAlgorithmImpl implements IGeneticAlgorithm {

	private int numberOfStocks;
	private List<Portfolio> population;
	private int populationSize;
	
	private int[][] data = new int[][]{{100,101,103,104},{100,99,100,99}};
	
	public GeneticAlgorithmImpl(int portfolioSize, int populationSize){
		this.numberOfStocks = portfolioSize;
		this.population = new LinkedList<Portfolio>();
		this.populationSize = populationSize;
		
		for(int i = 0; i < this.populationSize; i++){
			population.add(new Portfolio(this.numberOfStocks));
		}

		randomPopulationInit();
	}
	
	private void randomPopulationInit(){
		
		Random rand = new Random();
		double sum = 0.0;
		
		for(int i = 0; i < populationSize; i++){
			
			sum = 0.0;
			for(int j = 0; j < numberOfStocks; j++){
				double randomStockPart = rand.nextDouble();
				sum += randomStockPart;
				
				population.get(i).getPortfolio().set(j,randomStockPart);
			}
			
			//now we have to normalize the result
			for(int j = 0; j < numberOfStocks; j++){	
				population.get(i).getPortfolio().set(j,population.get(i).getPortfolio().get(j) / sum);
			}
		}
	}
	
	public Portfolio calculateCurrentPortfolio() {
		// TODO Auto-generated method stub
		return null;
	}

	public int getNumberOfStocks() {
		return numberOfStocks;
	}

	public void setNumberOfStocks(int numberOfStocks) {
		this.numberOfStocks = numberOfStocks;
	}

	public List<Portfolio> getPopulation() {
		return population;
	}

	public void setPopulation(List<Portfolio> population) {
		this.population = population;
	}

	public int getPopulationSize() {
		return populationSize;
	}

	public void setPopulationSize(int populationSize) {
		this.populationSize = populationSize;
	}

}
