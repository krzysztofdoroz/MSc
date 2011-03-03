package pl.edu.agh.msc.simple.genetic.algo;

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
import pl.edu.agh.msc.simple.genetic.algoImpl.GeneticAlgorithmImpl;

public class GATests {

	List<String> filenames;
	IDataSource dataSource;

	@Before
	public void init(){
		filenames =  new LinkedList<String>();
		filenames.add("/home/krzysztof/MSc/data-source/kghm.data");
		filenames.add("/home/krzysztof/MSc/data-source/tpsa.data");
		dataSource = new StockDataSource(filenames,null, null, null, null, 253);
	}
	
	@Test
	public void testGA(){
		
		double money = 100.0;
		double[] stocskBought = new double[2];
		BufferedWriter bufferedWriter = null;
		try {
			bufferedWriter = new BufferedWriter(new FileWriter(new File("results")));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("GA test:");
		GeneticAlgorithmImpl gen = new GeneticAlgorithmImpl(2,16, 0.2, 0.1, 0.3, dataSource);
		
		for(int i = 0; i < 253 ; i++){
			Portfolio bestPortfolio = gen.calculateCurrentPortfolio();

			if (stocskBought[0] + stocskBought[1] > 0 ){
				money = 0;
				int index = 0;
				for(double ammount: stocskBought){
					money += ammount * dataSource.getStockData(index, i);
					index++;
				}
			}
			
			System.out.println(money);
			try {
				bufferedWriter.write(i + " " + money + " " + stocskBought[0] + " " + stocskBought[1] + "\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			//buy stocks according to best portfolio
			stocskBought[0] = (bestPortfolio.getPortfolio().get(0) * money) / dataSource.getStockData(0, i);
			stocskBought[1] = (bestPortfolio.getPortfolio().get(1) * money) / dataSource.getStockData(1, i);
			
			System.out.println("      CURRENT POPULATION STATE:  ");
			
			for(Portfolio portfolio : gen.getPopulation()){
			//	System.out.println(portfolio);
			}
			System.out.println("  BEST PORTFOLIO:");

			//money *= bestPortfolio.getValue();
			System.out.println(bestPortfolio);
			System.out.println("TOTAL MONEY:" + money);
			
			System.out.println("----------------------------------------------------");
		}
		try {
			bufferedWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
