package pl.edu.agh.msc.MPTGeneticAlgorithm;

import java.util.LinkedList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import pl.edu.agh.msc.data.source.StockDataSource;
import pl.edu.agh.msc.data.source.interfaces.IDataSource;

public class MPTFunctionsTest {

	List<String> filenames;
	IDataSource dataSource;
	
	@Before
	public void init(){
		filenames =  new LinkedList<String>();
		filenames.add("/home/krzysztof/MSc/data-source/kghm.data");
		filenames.add("/home/krzysztof/MSc/data-source/tpsa.data");
		dataSource = new StockDataSource(filenames,null, null, null, null, 253);
		
		List<String> standardDevFileList = new LinkedList<String>();
		standardDevFileList.add("/home/krzysztof/MSc/R_scripts/output/kghm_standard_deviation");
		standardDevFileList.add("/home/krzysztof/MSc/R_scripts/output/tpsa_standard_deviation");
		dataSource.loadStockStandardDeviationData(standardDevFileList);
		
		List<String> correlationCoeffFileList = new LinkedList<String>();
		correlationCoeffFileList.add("/home/krzysztof/MSc/R_scripts/output/kghm-tpsa_correlation_coeff");
		dataSource.loadStockStandardCorrelationCoeffData(correlationCoeffFileList);
		
		List<String> covarianceData = new LinkedList<String>();
		covarianceData.add("/home/krzysztof/MSc/R_scripts/output/kghm-wig20_cov");
		covarianceData.add("/home/krzysztof/MSc/R_scripts/output/tpsa-wig20_cov");
		dataSource.loadStockCovarianceData(covarianceData);
		
		List<String> marketVarianceData = new LinkedList<String>();
		marketVarianceData.add("/home/krzysztof/MSc/R_scripts/output/wig20_var");
		dataSource.loadMarketVarianceData(marketVarianceData.get(0));
	}
	
	@Test
	public void testExpectedValue(){
		MPTGeneticAlgorithm gen = new MPTGeneticAlgorithm(2, 16, 0.2, 0.1, 0.3, dataSource);
		
		MPTPortfolio portfolio = new MPTPortfolio(2);
		portfolio.getPortfolio().set(0, 0.1);
		portfolio.getPortfolio().set(1, 0.9);
		
		System.out.println( gen.calculateExpectedReturn(portfolio, 0));
	}
	
	@Test
	public void testRiskCalculating(){
		MPTGeneticAlgorithm gen = new MPTGeneticAlgorithm(2, 16, 0.2, 0.1, 0.3, dataSource);
		
		MPTPortfolio portfolio = new MPTPortfolio(2);
		portfolio.getPortfolio().set(0, 0.9);
		portfolio.getPortfolio().set(1, 0.1);
		
		System.out.println( gen.getRisk(portfolio, 0));
	}
	
	@Test
	public void testBreedingNewPortfolios(){
		List<MPTPortfolio> parents = new LinkedList<MPTPortfolio>();
		
		for(int i = 0 ; i < 6; i++){
			parents.add(new MPTPortfolio(2));
		}
		
		List<MPTPortfolio> result = MPTAlgorithmUtils.breedNewPortfolios(parents);
	
		assertEquals(6, result.size());
		
	}
	
}
