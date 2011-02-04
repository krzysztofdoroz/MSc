package pl.edu.agh.msc.simple.genetic.algoImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pl.edu.agh.msc.generic.genetic.algorithm.IGeneticAlgorithm;
import pl.edu.agh.msc.generic.genetic.algorithm.Portfolio;
import pl.edu.agh.msc.generic.genetic.algorithm.exception.InvalidPortfolioException;

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
	
	public void checkIfPortfolioIsValid(Portfolio portfolio) throws InvalidPortfolioException {
		
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
	
	//in this case fitness is equal to current portfolio value
	public double calculateFitness(Portfolio portfolio, int day){
		double result = 0.0;
		
		for(int i = 0; i < portfolio.getPortfolio().size(); i++ ){
			result += portfolio.getPortfolio().get(i) * data[i][day];
		}
		
		return result;
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
