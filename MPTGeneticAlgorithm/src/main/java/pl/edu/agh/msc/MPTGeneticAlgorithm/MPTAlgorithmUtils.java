package pl.edu.agh.msc.MPTGeneticAlgorithm;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pl.edu.agh.msc.generic.genetic.algorithm.Portfolio;

public class MPTAlgorithmUtils {

	public static List<MPTPortfolio> selectBestGenomes(
			List<MPTPortfolio> riskOrientedPopulation,
			List<MPTPortfolio> expectedReturnPopulation, double breedingCoeff) {

		List<MPTPortfolio> bestGenomes = new LinkedList<MPTPortfolio>();
		int populationSize = (int) (breedingCoeff * riskOrientedPopulation
				.size());

		for (int i = 0; i < populationSize; i++) {
			bestGenomes.add(riskOrientedPopulation.get(i));
			bestGenomes.add(expectedReturnPopulation.get(i));
		}

		return bestGenomes;
	}

	public static List<MPTPortfolio> breedNewPortfolios(
			List<MPTPortfolio> population) {

		List<MPTPortfolio> children = new LinkedList<MPTPortfolio>();

		int numberOfOffspring = population.size();

		if (numberOfOffspring % 2 == 1) {
			numberOfOffspring++;
		}

		for (int i = 0; i < numberOfOffspring;) {
			Portfolio parentA = population.get(i);
			i++;
			Portfolio parentB = population.get(i);
			i++;
			children.addAll(crossoverWithGenomeSwapping(parentA, parentB));
		}

		return children;
	}

	public static List<MPTPortfolio> createMutants(
			List<MPTPortfolio> population, double mutationCoeff) {
		List<MPTPortfolio> mutants = new LinkedList<MPTPortfolio>();
		Random rand = new Random();

		int numberOfNewMutants = (int) (population.size() * mutationCoeff);

		for (int i = 0; i < numberOfNewMutants; i++) {
			int indexOfPortfolioToMutate = Math.abs(rand.nextInt()
					% population.size());
			mutants.add(mutate(population.get(indexOfPortfolioToMutate)));
		}
		return mutants;
	}

	public static void extinctTheWeakest(List<MPTPortfolio> population,
			double extinctionCoeff) {
		int numberOfExtinctedPortfolios = (int) (population.size() * extinctionCoeff);

		for (int i = 0; i < numberOfExtinctedPortfolios; i++) {
			population.remove(population.size() - 1);
		}
	}

	public static void mergePopulation(List<MPTPortfolio> population,
			List<MPTPortfolio> mutants) {

		for (MPTPortfolio newPortfolio : mutants) {
			population.add(newPortfolio);
		}
	}

	public static MPTPortfolio mutate(MPTPortfolio portfolio) {
		MPTPortfolio mutant = new MPTPortfolio(portfolio.getSize());

		Random rand = new Random();
		int indexToMutate = Math.abs(rand.nextInt() % portfolio.getSize());
		double mutatedValue = rand.nextDouble();

		for (int i = 0; i < portfolio.getSize(); i++) {
			mutant.getPortfolio().set(i, portfolio.getPortfolio().get(i));
		}

		mutant.getPortfolio().set(indexToMutate, mutatedValue);
		mutant.normalize();

		return mutant;
	}

	public static List<MPTPortfolio> crossoverWithGenomeSwapping(
			Portfolio parentA, Portfolio parentB) {
		List<MPTPortfolio> children = new LinkedList<MPTPortfolio>();

		MPTPortfolio childA = new MPTPortfolio(parentA.getSize());
		MPTPortfolio childB = new MPTPortfolio(parentA.getSize());
		Random rand = new Random();
		int left, right, tmp;

		left = Math.abs(rand.nextInt() % parentA.getSize());
		right = Math.abs(rand.nextInt() % parentA.getSize());

		if (left > right) {
			tmp = left;
			left = right;
			right = tmp;
		}

		// childA: AAA |L| BBBBB |R| AAAA
		for (int i = 0; i < left; i++) {
			childA.getPortfolio().set(i, parentA.getPortfolio().get(i));
		}

		for (int i = left; i < right; i++) {
			childA.getPortfolio().set(i, parentB.getPortfolio().get(i));
		}

		for (int i = right; i < parentA.getSize(); i++) {
			childA.getPortfolio().set(i, parentA.getPortfolio().get(i));
		}
		childA.normalize();

		// childB: BBBB |L| AAAAAA |R| BBBBB
		for (int i = 0; i < left; i++) {
			childB.getPortfolio().set(i, parentB.getPortfolio().get(i));
		}

		for (int i = left; i < right; i++) {
			childB.getPortfolio().set(i, parentA.getPortfolio().get(i));
		}

		for (int i = right; i < parentA.getSize(); i++) {
			childB.getPortfolio().set(i, parentB.getPortfolio().get(i));
		}
		childB.normalize();

		children.add(childA);
		children.add(childB);

		return children;
	}

	public static void splitChildrenAccordingToTheirGenome(
			List<MPTPortfolio> children,
			List<MPTPortfolio> riskOrientedPopulation,
			List<MPTPortfolio> returnOrientedpopulation) {
		
		// children are already sorted according to risk
		
		for(int i = 0; i < children.size()/2; i++){
			riskOrientedPopulation.add(children.get(i));
		}
		
		for(int i = children.size()/2; i < children.size(); i++){
			 returnOrientedpopulation.add(children.get(i));
		}
		
	}

}
