package pl.edu.agh.msc.test.data.source;

import java.util.LinkedList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

import pl.edu.agh.msc.data.source.StockDataSource;
import pl.edu.agh.msc.data.source.interfaces.IDataSource;

public class DataSourceTest {

	@Test
	public void testDataSource(){
		List<String> filesToLoad = new LinkedList<String>();
		filesToLoad.add("kghm.data");
		filesToLoad.add("tpsa.data");
		
		IDataSource dataSource = new StockDataSource(filesToLoad,null ,null, null, null, 253);

		assertEquals(14.9999, dataSource.getStockData(1, 2), 0.0001);	
		assertEquals(163.1, dataSource.getStockData(0, 249), 0.0001);	
	}
	
	@Test
	public void testCorrelationCoeffLoading(){
		List<String> filesToLoad = new LinkedList<String>();
		filesToLoad.add("kghm.data");
		filesToLoad.add("tpsa.data");
		
		List<String> coeffsToLoad = new LinkedList<String>();
		coeffsToLoad.add("kghm-tpsa_correlation_coeff");
		coeffsToLoad.add("kghm-pko_correlation_coeff");
		coeffsToLoad.add("tpsa-pko_correlation_coeff");
		
		IDataSource dataSource = new StockDataSource(filesToLoad,null ,coeffsToLoad, null, null, 253);
		
		assertEquals(3, ((StockDataSource)dataSource).getCorrelationCoeffData().size());
		
		assertEquals(-0.04407846, dataSource.getCorrelationCoeffData(1, 2, 0), 0.0001);
		assertEquals(-0.4714231,  dataSource.getCorrelationCoeffData(0, 1, 1), 0.0001);
		assertEquals(0.8143163, dataSource.getCorrelationCoeffData(2, 0, 2), 0.0001);
		assertEquals(-0.04407846, dataSource.getCorrelationCoeffData(2, 1, 0), 0.0001);
	}
	
}
