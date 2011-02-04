package pl.edu.agh.msc.simple.genetic.algoImpl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import pl.edu.agh.msc.generic.genetic.algorithm.IGeneticAlgorithm;
import pl.edu.agh.msc.generic.genetic.algorithm.Portfolio;
import pl.edu.agh.msc.generic.genetic.algorithm.exception.InvalidPortfolioException;

public class GeneticAlgorithmImpl implements IGeneticAlgorithm {

	static Logger logger = Logger.getLogger(GeneticAlgorithmImpl.class);
	private int numberOfStocks;
	private List<Portfolio> population;
	private int populationSize;
	
	private static int[][] data = new int[][]{{100,101,103,104},{100,99,100,99}};
	
	public GeneticAlgorithmImpl(int portfolioSize, int populationSize){
		this.numberOfStocks = portfolioSize;
		this.population = new LinkedList<Portfolio>();
		this.populationSize = populationSize;
		
		for(int i = 0; i < this.populationSize; i++){
			population.add(new Portfolio(this.numberOfStocks));
		}

		randomPopulationInit();
	}
	
	public Portfolio getBestPortfolio(int day) {
		double max = 0.0;
		Portfolio bestPortfolio = null;
		
		for(int i = 0; i < population.size(); i++){
			if(max <  calculateFitness(population.get(i), day)){
				bestPortfolio = population.get(i);
				max = calculateFitness(population.get(i), day);
			}
		}
		
		return bestPortfolio;
	}
	
	
	public static void checkIfPortfolioIsValid(Portfolio portfolio) throws InvalidPortfolioException {
		
		double sum = 0.0;
		final double EPS = 0.001;
		
		//check if all values sum up to 1.0
		for(int i = 0; i < portfolio.getSize(); i++){
			sum += portfolio.getPortfolio().get(i);
		}
		
		if(Math.abs(sum - 1.0) > EPS){
			throw new InvalidPortfolioException();
		}
		
	}
	
	public static Portfolio crossover(Portfolio parentA, Portfolio parentB){
		Portfolio childPortfolio = new Portfolio(parentA.getSize());

		for(int i = 0; i < parentA.getSize(); i++){
			childPortfolio.getPortfolio().set(i, (parentA.getPortfolio().get(i) + parentB.getPortfolio().get(i))/2.0);
		}
		
		try{
			checkIfPortfolioIsValid(childPortfolio);
		} catch (InvalidPortfolioException e) {
			System.out.println("childPortfolio is not valid");
			logger.error("childPortfolio is not valid");
		}
		
		return childPortfolio;
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
	public static double calculateFitness(Portfolio portfolio, int day){
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
