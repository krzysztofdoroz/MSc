package pl.edu.agh.msc.MPTGeneticAlgorithm;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import pl.edu.agh.msc.data.source.StockDataSource;
import pl.edu.agh.msc.data.source.interfaces.IDataSource;
import pl.edu.agh.msc.generic.genetic.algorithm.MPTPortfolio;

public class MPTGenAlgTest {

	List<String> filenames;
	IDataSource dataSource;

	@Before
	public void init() {
		filenames = new LinkedList<String>();
		filenames.add("/home/krzysztof/MSc/data-source/kghm.data");
		filenames.add("/home/krzysztof/MSc/data-source/tpsa.data");
		dataSource = new StockDataSource(filenames,null, null, null, null, 253);

		List<String> standardDevFileList = new LinkedList<String>();
		standardDevFileList
				.add("/home/krzysztof/MSc/R_scripts/output/kghm_standard_deviation");
		standardDevFileList
				.add("/home/krzysztof/MSc/R_scripts/output/tpsa_standard_deviation");
		dataSource.loadStockStandardDeviationData(standardDevFileList);

		List<String> correlationCoeffFileList = new LinkedList<String>();
		correlationCoeffFileList
				.add("/home/krzysztof/MSc/R_scripts/output/kghm-tpsa_correlation_coeff");
		dataSource
				.loadStockStandardCorrelationCoeffData(correlationCoeffFileList);

		List<String> covarianceData = new LinkedList<String>();
		covarianceData
				.add("/home/krzysztof/MSc/R_scripts/output/kghm-wig20_cov");
		covarianceData
				.add("/home/krzysztof/MSc/R_scripts/output/tpsa-wig20_cov");
		dataSource.loadStockCovarianceData(covarianceData);

		List<String> marketVarianceData = new LinkedList<String>();
		marketVarianceData
				.add("/home/krzysztof/MSc/R_scripts/output/wig20_var");
		dataSource.loadMarketVarianceData(marketVarianceData.get(0));
	}

	@Test
	public void testGA() {

		double money = 100.0;
		double[] stocskBought = new double[2];
		BufferedWriter bufferedWriter = null;
		BufferedWriter paretoWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File(
					"results")));
			paretoWriter = new BufferedWriter(new FileWriter(new File(
					"pareto_results")));
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("GA test:");
		MPTGeneticAlgorithm gen = new MPTGeneticAlgorithm(2, 16, 0.2, 0.1, 0.3,
				dataSource);

		for (int i = 0; i < 253; i++) {
			MPTPortfolio bestPortfolio = gen.calculateCurrentPortfolio();

			List<MPTPortfolio> riskOriented = gen.getRiskOrientedPopulation();
			List<MPTPortfolio> returnOriented = gen
					.getReturnOrientedpopulation();

			try {
					
				for (MPTPortfolio portfolio : riskOriented) {
					paretoWriter.write(portfolio.getRisk() / 100.0 + " "
							+ bestPortfolio.getValue() + "\n");
				}
				
				
				for (MPTPortfolio portfolio : returnOriented) {
					paretoWriter.write(portfolio.getRisk() / 100.0 + " "
							+ bestPortfolio.getValue() + "\n");
				}
				

			} catch (IOException e) {
				e.printStackTrace();
			}

			if (stocskBought[0] + stocskBought[1] > 0) {
				money = 0;
				int index = 0;
				for (double ammount : stocskBought) {
					money += ammount * dataSource.getStockData(index, i);
					index++;
				}
			}

			System.out.println(money);
			try {
				bufferedWriter.write(i + " " + money + " " + stocskBought[0]
						+ " " + stocskBought[1] + " " + bestPortfolio.getRisk()
						/ 100.0 + " " + bestPortfolio.getValue() + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}

			// buy stocks according to best portfolio
			stocskBought[0] = (bestPortfolio.getPortfolio().get(0) * money)
					/ dataSource.getStockData(0, i);
			stocskBought[1] = (bestPortfolio.getPortfolio().get(1) * money)
					/ dataSource.getStockData(1, i);

			// System.out.println("      CURRENT POPULATION STATE:  ");

			System.out.println("  BEST PORTFOLIO:");

			// money *= bestPortfolio.getValue();
			System.out.println(bestPortfolio);
			System.out.println("TOTAL MONEY:" + money);

			System.out
					.println("----------------------------------------------------");
		}
		try {
			bufferedWriter.close();
			paretoWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
