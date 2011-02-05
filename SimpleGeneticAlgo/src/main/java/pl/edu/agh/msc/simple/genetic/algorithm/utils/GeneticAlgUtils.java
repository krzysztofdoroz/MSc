package pl.edu.agh.msc.simple.genetic.algorithm.utils;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pl.edu.agh.msc.generic.genetic.algorithm.Portfolio;

public class GeneticAlgUtils {

	public static void mergePopulation(List<Portfolio> population,
			List<Portfolio> children, List<Portfolio> mutants) {

		for (Portfolio newPortfolio : children) {
			population.add(newPortfolio);
		}

		for (Portfolio newPortfolio : mutants) {
			population.add(newPortfolio);
		}
	}

	public static void extinctTheWeakest(List<Portfolio> population,
			double extinctionCoeff) {

		int numberOfExtinctedPortfolios = (int) (population.size() * extinctionCoeff);

		for (int i = 0; i < numberOfExtinctedPortfolios; i++) {
			population.remove(population.size() - 1);
		}

	}

	public static List<Portfolio> createMutants(List<Portfolio> population,
			double mutationCoeff) {
		List<Portfolio> mutants = new LinkedList<Portfolio>();
		Random rand = new Random();

		int numberOfNewMutants = (int) (population.size() * mutationCoeff);

		for (int i = 0; i < numberOfNewMutants; i++) {
			int indexOfPortfolioToMutate = Math.abs(rand.nextInt() % population.size());
			mutants.add(mutate(population.get(indexOfPortfolioToMutate)));
		}
		return mutants;
	}

	public static List<Portfolio> breedNewPortfolios(
			List<Portfolio> population, double breedingCoeff) {

		List<Portfolio> children = new LinkedList<Portfolio>();

		int numberOfOffspring = (int) (population.size() * breedingCoeff);
		
		//numberOfOffspring has to be even!
		if(numberOfOffspring % 2 == 1){
			numberOfOffspring++;
		}
		
		for(int i = 0; i < numberOfOffspring; ){
			Portfolio parentA = population.get(i);
			i++;
			Portfolio parentB = population.get(i);
			i++;
			children.addAll(crossoverWithGenomeSwapping(parentA, parentB));
		}
		
		return children;
	}

	public static List<Portfolio> crossoverWithGenomeSwapping(
			Portfolio parentA, Portfolio parentB) {
		List<Portfolio> children = new LinkedList<Portfolio>();

		Portfolio childA = new Portfolio(parentA.getSize());
		Portfolio childB = new Portfolio(parentA.getSize());
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

	public static Portfolio mutate(Portfolio portfolio) {
		Portfolio mutant = new Portfolio(portfolio.getSize());

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
}
