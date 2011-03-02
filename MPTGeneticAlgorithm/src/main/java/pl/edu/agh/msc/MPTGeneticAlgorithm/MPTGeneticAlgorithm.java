package pl.edu.agh.msc.MPTGeneticAlgorithm;

import java.util.Collections;
import java.util.Comparator;
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
	private List<MPTPortfolio> riskOrientedPopulation;
	private List<MPTPortfolio> returnOrientedpopulation;
	private int populationSize;
	private static int day = 0;
	private final double BREEDING_COEFF;
	private final double MUTATION_COEFF;
	private IDataSource stockDataSource;

	public MPTGeneticAlgorithm(int portfolioSize, int populationSize,
			double breedingCoeff, double mutationCoeff, double extinctionCoeff,
			IDataSource dataSource) {
		this.numberOfStocks = portfolioSize;
		this.riskOrientedPopulation = new LinkedList<MPTPortfolio>();
		this.returnOrientedpopulation = new LinkedList<MPTPortfolio>();
		this.populationSize = populationSize;
		this.stockDataSource = dataSource;

		for (int i = 0; i < this.populationSize; i++) {
			riskOrientedPopulation.add(new MPTPortfolio(this.numberOfStocks));
			returnOrientedpopulation.add(new MPTPortfolio(this.numberOfStocks));
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

				riskOrientedPopulation.get(i).getPortfolio()
						.set(j, randomStockPart);
				returnOrientedpopulation.get(i).getPortfolio()
						.set(j, randomStockPart);
			}

			// now we have to normalize the result
			for (int j = 0; j < numberOfStocks; j++) {
				riskOrientedPopulation
						.get(i)
						.getPortfolio()
						.set(j,
								riskOrientedPopulation.get(i).getPortfolio()
										.get(j)
										/ sum);
				returnOrientedpopulation
						.get(i)
						.getPortfolio()
						.set(j,
								returnOrientedpopulation.get(i).getPortfolio()
										.get(j)
										/ sum);
			}
		}
	}

	public MPTPortfolio calculateCurrentPortfolio() {

		/*
		 * calculating risk and expected return of subpopulations
		 */

		for (MPTPortfolio portfolio : riskOrientedPopulation) {
			portfolio.setRisk(getRisk(portfolio, day));
			portfolio.setValue(calculateExpectedReturn(portfolio, day));
		}

		for (MPTPortfolio portfolio : returnOrientedpopulation) {
			portfolio.setRisk(getRisk(portfolio, day));
			portfolio.setValue(calculateExpectedReturn(portfolio, day));
		}

		/*
		 * sorting both subpopulations with appropriate comparators
		 */

		Collections.sort(returnOrientedpopulation);
		Collections.reverse(returnOrientedpopulation);

		Collections.sort(riskOrientedPopulation, new RiskComparator());

		for (Portfolio portfolio : returnOrientedpopulation) {
			System.out.println(portfolio);
		}

		/*
		 * selecting best genomes from both subpopulations to reproduction
		 */

		List<MPTPortfolio> reproducingGenomes = MPTAlgorithmUtils
				.selectBestGenomes(riskOrientedPopulation,
						returnOrientedpopulation, BREEDING_COEFF);

		/*
		 * breeding offspring
		 */

		List<MPTPortfolio> children = MPTAlgorithmUtils
				.breedNewPortfolios(reproducingGenomes);
		Collections.sort(children, new RiskComparator());

		/*
		 * creating mutants
		 */

		List<MPTPortfolio> riskOrientedmutants = MPTAlgorithmUtils
				.createMutants(riskOrientedPopulation, MUTATION_COEFF);
		List<MPTPortfolio> returnOrientedmutants = MPTAlgorithmUtils
				.createMutants(returnOrientedpopulation, MUTATION_COEFF);

		/*
		 * extinct the weakest genomes from both subpopulations
		 */

		MPTAlgorithmUtils.extinctTheWeakest(riskOrientedPopulation,
				EXTINCTION_COEFF);
		MPTAlgorithmUtils.extinctTheWeakest(returnOrientedpopulation,
				EXTINCTION_COEFF);

		/*
		 * merge subpopulations with new generation genomes (offspring +
		 * mutants)
		 */

		MPTAlgorithmUtils.splitChildrenAccordingToTheirGenome(children,
				riskOrientedPopulation, returnOrientedpopulation);

		MPTAlgorithmUtils.mergePopulation(riskOrientedPopulation,
				riskOrientedmutants);
		MPTAlgorithmUtils.mergePopulation(returnOrientedpopulation,
				returnOrientedmutants);

		/*
		 * calculate risk and expected return of newly introduced genomes
		 */

		for (MPTPortfolio portfolio : riskOrientedPopulation) {
			portfolio.setRisk(getRisk(portfolio, day));
			portfolio.setValue(calculateExpectedReturn(portfolio, day));
		}

		for (MPTPortfolio portfolio : returnOrientedpopulation) {
			portfolio.setValue(calculateExpectedReturn(portfolio, day));
			portfolio.setRisk(getRisk(portfolio, day));
		}

		return getBestPortfolio(day++);
	}

	public MPTPortfolio getBestPortfolio(int day) {
		double min = 10000000.0;
		double max = 0.0;
		MPTPortfolio bestPortfolio = null;
		MPTPortfolio bestPortfolioReturnOriented = null;
		MPTPortfolio bestPortfolioRiskOriented = null;

		// select portfolio with minimum risk
		/*
		for (int i = 0; i < returnOrientedpopulation.size(); i++) {
			if (min > getRisk(returnOrientedpopulation.get(i), day)) {
				bestPortfolioReturnOriented = returnOrientedpopulation.get(i);
				min = getRisk(returnOrientedpopulation.get(i), day);
			}
		}
		*/
		List<MPTPortfolio> allGenomes = new LinkedList<MPTPortfolio>();
		allGenomes.addAll(riskOrientedPopulation);
		allGenomes.addAll(returnOrientedpopulation);
		
		//sorting by expected return, then by risk
		Collections.sort(allGenomes);
		Collections.sort(allGenomes, new RiskComparator());
		
		/*
		// select portfolio with maximum return
		for (int i = 0; i < riskOrientedPopulation.size(); i++) {
			if (max < calculateExpectedReturn(riskOrientedPopulation.get(i),
					day)) {
				bestPortfolioRiskOriented = riskOrientedPopulation.get(i);
				max = calculateExpectedReturn(riskOrientedPopulation.get(i),
						day);
			}
		}

		if (bestPortfolioReturnOriented.getValue() > bestPortfolioRiskOriented
				.getValue()
				&& bestPortfolioReturnOriented.getRisk() < bestPortfolioRiskOriented
						.getRisk()){
			return bestPortfolioReturnOriented;
		} else if(bestPortfolioReturnOriented.getRisk() > ) {
			return bestPortfolioRiskOriented;
		}
		*/
			return allGenomes.get(0);
	}

	public double calculateFitness(Portfolio portfolio, int day) {
		double result = 0.0;

		for (int i = 0; i < portfolio.getPortfolio().size(); i++) {
			result += stockDataSource.getCovarianceData(i, day)
					/ stockDataSource.getMarketVariance(day);
		}

		return result;
	}

	// in this case fitness is calculated according to CAPT
	public double calculateExpectedReturn(Portfolio portfolio, int day) {
		double result = 0.0;

		for (int i = 0; i < portfolio.getPortfolio().size(); i++) {
			result += (portfolio.getPortfolio().get(i) * stockDataSource
					.getCovarianceData(i, day))
					/ stockDataSource.getMarketVariance(day);
		}
		return result;
	}

	public double getRisk(Portfolio portfolio, int day) {
		double risk = 0.0;
		double correlationCoeff = 0.0;
		double stockNumberOneWeigth = 0.0;
		double stockNumberTwoWeigth = 0.0;

		for (int i = 0; i < portfolio.getSize(); i++) {
			for (int j = 0; j < portfolio.getSize(); j++) {

				if (i == j) {
					correlationCoeff = 1.0;
				} else {
					correlationCoeff = stockDataSource.getCorrelationCoeffData(
							i / 2, day);
				}

				stockNumberOneWeigth = portfolio.getPortfolio().get(i);
				stockNumberTwoWeigth = portfolio.getPortfolio().get(j);

				risk += stockNumberOneWeigth * stockNumberTwoWeigth
						* stockDataSource.getStandardDevData(i, day)
						* stockDataSource.getStandardDevData(j, day)
						* correlationCoeff;
			}
		}

		return Math.sqrt(risk);
	}

	class RiskComparator implements Comparator<MPTPortfolio> {

		public int compare(MPTPortfolio portfolioA, MPTPortfolio portfolioB) {

			double portfolioARisk = portfolioA.getRisk();
			double portfolioBRisk = portfolioB.getRisk();

			if (portfolioARisk > portfolioBRisk) {
				return 1;
			} else if (portfolioARisk < portfolioBRisk) {
				return -1;
			} else {
				return 0;
			}
		}

	}

	class ExpectedReturnComparator implements Comparator<Portfolio> {

		public int compare(Portfolio portfolioA, Portfolio portfolioB) {
			double portfolioAExpReturn = calculateExpectedReturn(portfolioA,
					day);
			double portfolioBExpReturn = calculateExpectedReturn(portfolioB,
					day);

			if (portfolioAExpReturn > portfolioBExpReturn) {
				return 1;
			} else if (portfolioAExpReturn < portfolioBExpReturn) {
				return -1;
			} else {
				return 0;
			}
		}

	}

	public List<MPTPortfolio> getRiskOrientedPopulation() {
		return riskOrientedPopulation;
	}

	public void setRiskOrientedPopulation(
			List<MPTPortfolio> riskOrientedPopulation) {
		this.riskOrientedPopulation = riskOrientedPopulation;
	}

	public List<MPTPortfolio> getReturnOrientedpopulation() {
		return returnOrientedpopulation;
	}

	public void setReturnOrientedpopulation(
			List<MPTPortfolio> returnOrientedpopulation) {
		this.returnOrientedpopulation = returnOrientedpopulation;
	}

}
