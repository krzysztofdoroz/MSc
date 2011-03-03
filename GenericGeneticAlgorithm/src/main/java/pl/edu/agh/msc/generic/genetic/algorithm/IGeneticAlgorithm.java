package pl.edu.agh.msc.generic.genetic.algorithm;

public interface IGeneticAlgorithm {

	Portfolio calculateCurrentPortfolio();
	Portfolio getPortfolioToMigrate();
	
}
