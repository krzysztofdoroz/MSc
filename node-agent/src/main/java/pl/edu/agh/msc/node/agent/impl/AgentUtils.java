package pl.edu.agh.msc.node.agent.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import pl.edu.agh.msc.generic.genetic.algorithm.MPTPortfolio;
import pl.edu.agh.msc.generic.genetic.algorithm.Portfolio;

public class AgentUtils {
	
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
}
