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
import pl.edu.agh.msc.generic.genetic.algorithm.Portfolio;

public class MPTGenAlgTest {

	List<String> filenames;
	IDataSource dataSource;

	@Before
	public void init(){
		filenames =  new LinkedList<String>();
		filenames.add("/home/krzysztof/MSc/data-source/kghm.data");
		filenames.add("/home/krzysztof/MSc/data-source/tpsa.data");
		dataSource = new StockDataSource(filenames, 253);
		
		List<String> standardDevFileList = new LinkedList<String>();
		standardDevFileList.add("/home/krzysztof/MSc/R_scripts/output/kghm_standard_deviation");
		standardDevFileList.add("/home/krzysztof/MSc/R_scripts/output/tpsa_standard_deviation");
		dataSource.loadStockStandardDeviationData(standardDevFileList);
		
		List<String> correlationCoeffFileList = new LinkedList<String>();
		correlationCoeffFileList.add("/home/krzysztof/MSc/R_scripts/output/kghm-tpsa_correlation_coeff");
		dataSource.loadStockStandardCorrelationCoeffData(correlationCoeffFileList);
	}
	
	@Test
	public void testRiskCalculating(){
		MPTGeneticAlgorithm gen = new MPTGeneticAlgorithm(2, 16, 0.2, 0.1, 0.3, dataSource);
		
		Portfolio portfolio = new Portfolio(2);
		portfolio.getPortfolio().set(0, 0.1);
		portfolio.getPortfolio().set(1, 0.9);
		
		System.out.println( gen.getRisk(portfolio, 0));
	}
	
}
