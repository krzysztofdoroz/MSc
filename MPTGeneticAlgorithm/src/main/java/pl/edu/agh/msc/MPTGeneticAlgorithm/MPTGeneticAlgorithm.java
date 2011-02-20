package pl.edu.agh.msc.MPTGeneticAlgorithm;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;

import pl.edu.agh.msc.data.source.interfaces.IDataSource;
import pl.edu.agh.msc.generic.genetic.algorithm.IGeneticAlgorithm;
import pl.edu.agh.msc.generic.genetic.algorithm.Portfolio;

public class MPTGeneticAlgorithm implements IGeneticAlgorithm {

	private final double EXTINCTION_COEFF;
	static Logger logger = Logger.getLogger(MPTGeneticAlgorithm.class);
	private int numberOfStocks;
	private List<Portfolio> population;
	private int populationSize;
	private static int day = 0;
	private final double BREEDING_COEFF;
	private final double MUTATION_COEFF;
	private IDataSource stockDataSource;
	
	public MPTGeneticAlgorithm(int portfolioSize, int populationSize,
			double breedingCoeff, double mutationCoeff, double extinctionCoeff, IDataSource dataSource) {
		this.numberOfStocks = portfolioSize;
		this.population = new LinkedList<Portfolio>();
		this.populationSize = populationSize;
		this.stockDataSource = dataSource;

		for (int i = 0; i < this.populationSize; i++) {
			population.add(new Portfolio(this.numberOfStocks));
		}
		BREEDING_COEFF = breedingCoeff;
		MUTATION_COEFF = mutationCoeff;
		EXTINCTION_COEFF = extinctionCoeff;

		randomPopulationInit();
	}
	
	private void randomPopulationInit() {
		Random rand = new Random();
		double sum = 0.0;

		for (int i = 0; i < populationSize; i++) {

			sum = 0.0;
			for (int j = 0; j < numberOfStocks; j++) {
				double randomStockPart = rand.nextDouble();
				sum += randomStockPart;

				population.get(i).getPortfolio().set(j, randomStockPart);
			}

			// now we have to normalize the result
			for (int j = 0; j < numberOfStocks; j++) {
				population.get(i).getPortfolio()
						.set(j, population.get(i).getPortfolio().get(j) / sum);
			}
		}
	}
	
	public Portfolio calculateCurrentPortfolio() {
		for (Portfolio portfolio : population) {
			portfolio.setValue(calculateFitness(portfolio, day));
		}

		Collections.sort(population);
		Collections.reverse(population);

		List<Portfolio> children = MPTAlgorithmUtils.breedNewPortfolios(
				population, BREEDING_COEFF);
		List<Portfolio> mutants = MPTAlgorithmUtils.createMutants(population,
				MUTATION_COEFF);

		MPTAlgorithmUtils.extinctTheWeakest(population, EXTINCTION_COEFF);
		MPTAlgorithmUtils.mergePopulation(population, children, mutants);

		for (Portfolio portfolio : population) {
			portfolio.setValue(calculateFitness(portfolio, day));
		}
		
		return getBestPortfolio(day++);
	}
	
	public Portfolio getBestPortfolio(int day) {
		double max = 0.0;
		Portfolio bestPortfolio = null;

		for (int i = 0; i < population.size(); i++) {
			if (max < calculateFitness(population.get(i), day)) {
				bestPortfolio = population.get(i);
				max = calculateFitness(population.get(i), day);
			}
		}
		return bestPortfolio;
	}
	
	// in this case fitness is equal to current portfolio value
	public double calculateFitness(Portfolio portfolio, int day) {
		double result = 0.0;

		for (int i = 0; i < portfolio.getPortfolio().size(); i++) {

			result += (portfolio.getPortfolio().get(i) * stockDataSource.getStockData(i, day))
					/ stockDataSource.getStockData(i,day - 1 < 0 ? 0 : day - 1);
		}

		return result;
	}
}
