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
		
		IDataSource dataSource = new StockDataSource(filesToLoad, 253);

		assertEquals(14.9999, dataSource.getStockData(1, 2), 0.0001);	
		assertEquals(163.1, dataSource.getStockData(0, 249), 0.0001);	
	}
}
