package pl.edu.agh.msc.data.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import pl.edu.agh.msc.data.source.interfaces.IDataSource;

public class StockDataSource implements IDataSource{

	private double[][] stockData;
	
	public StockDataSource(List<String> filenames, int timeHorizon){
		stockData = new double[filenames.size()][timeHorizon];
		
		for(int i = 0; i < filenames.size(); i++){
			File currentFile = new File(filenames.get(i));
			
			try {
				BufferedReader reader = new BufferedReader(new FileReader(currentFile));
				
				for(int j = 0; j < timeHorizon; j++){
					//System.out.println(j + " " + reader.readLine());
					
					stockData[i][j] = Double.parseDouble(reader.readLine());
				}
				reader.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public double getStockData(int stockNumber, int day) {
		return stockData[stockNumber][day];
	}

}
