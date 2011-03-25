package pl.edu.agh.msc.data.source;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.edu.agh.msc.data.source.interfaces.IDataSource;

public class StockDataSource implements IDataSource {

	private double[][] stockData;
	private double[][] stockStandardDevData;
	Map<Integer, Map<Integer,double[]>> correlationCoeffData = new HashMap<Integer, Map<Integer,double[]>>();
	
	private double[][] stockCovarianceData;
	private double[] marketIndexVarianceData;
	private int timeHorizon;

	public StockDataSource(List<String> filenames,
			List<String> standardDeviationFiles,
			List<String> standardCorrelationCoeffFiles,
			List<String> covarianceFiles, String marketVarianceFile,
			int timeHorizon) {
		this.timeHorizon = timeHorizon;
		stockData = new double[filenames.size()][timeHorizon];

		for (int i = 0; i < filenames.size(); i++) {
			File currentFile = new File(filenames.get(i));

			try {
				BufferedReader reader = new BufferedReader(new FileReader(
						currentFile));

				for (int j = 0; j < timeHorizon; j++) {
					// System.out.println(j + " " + reader.readLine());

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

		if (marketVarianceFile != null) {
			loadMarketVarianceData(marketVarianceFile);
		}

		if (covarianceFiles != null) {
			loadStockCovarianceData(covarianceFiles);
		}

		if (standardCorrelationCoeffFiles != null) {
			loadStockStandardCorrelationCoeffData(standardCorrelationCoeffFiles);
		}

		if (standardDeviationFiles != null) {
			loadStockStandardDeviationData(standardDeviationFiles);
		}

	}

	public void loadStockStandardDeviationData(List<String> filenames) {
		stockStandardDevData = new double[filenames.size()][timeHorizon];

		for (int i = 0; i < filenames.size(); i++) {
			File currentFile = new File(filenames.get(i));

			System.out.println("CURRENT FILENAME:" + currentFile);
			System.out.println((new File("")).getAbsolutePath());
			try {
				BufferedReader reader = new BufferedReader(new FileReader(
						currentFile));

				for (int j = 0; j < timeHorizon; j++) {
					// System.out.println(j + " " + reader.readLine());

					stockStandardDevData[i][j] = Double.parseDouble(reader
							.readLine());
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

	public void loadStockStandardCorrelationCoeffData(List<String> filenames) {

		for (int i = 0; i < filenames.size(); i++) {
			File currentFile = new File(filenames.get(i));
			double[] stockCorrelationCoeffData = new double[timeHorizon];

			try {
				BufferedReader reader = new BufferedReader(new FileReader(
						currentFile));

				int firstStockId = Integer.parseInt(reader.readLine());
				int secondStockId = Integer.parseInt(reader.readLine());
				
				for (int j = 0; j < timeHorizon; j++) {
					// System.out.println(j + " " + reader.readLine());

					stockCorrelationCoeffData[j] = Double.parseDouble(reader
							.readLine());
				}
				reader.close();
				
				//fill HM properly:
				Map<Integer,double[]> currentMap = correlationCoeffData.get(firstStockId);
			
				if (currentMap == null){
					currentMap = new HashMap<Integer, double[]>();
				}
				
				currentMap.put(secondStockId, stockCorrelationCoeffData);
				correlationCoeffData.put(firstStockId, currentMap);
				
				currentMap = correlationCoeffData.get(secondStockId);
				
				if (currentMap == null){
					currentMap = new HashMap<Integer, double[]>();
				} 
				
				currentMap.put(firstStockId, stockCorrelationCoeffData);
				correlationCoeffData.put(secondStockId, currentMap);
				
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

	public void loadStockCovarianceData(List<String> filenames) {
		stockCovarianceData = new double[filenames.size()][timeHorizon];

		for (int i = 0; i < filenames.size(); i++) {
			File currentFile = new File(filenames.get(i));

			try {
				BufferedReader reader = new BufferedReader(new FileReader(
						currentFile));

				for (int j = 0; j < timeHorizon; j++) {
					// System.out.println(j + " " + reader.readLine());

					stockCovarianceData[i][j] = Double.parseDouble(reader
							.readLine());
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

	public void loadMarketVarianceData(String filename) {
		marketIndexVarianceData = new double[timeHorizon];
		File currentFile = new File(filename);

		try {
			BufferedReader reader = new BufferedReader(new FileReader(
					currentFile));

			for (int i = 0; i < timeHorizon; i++) {
				marketIndexVarianceData[i] = Double.parseDouble(reader
						.readLine());
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

	public double getStockData(int stockNumber, int day) {
		return stockData[stockNumber][day];
	}

	public double getStandardDevData(int stockNumber, int day) {
		return stockStandardDevData[stockNumber][day];
	}

	public double getCorrelationCoeffData(int stockNumber,
			int secondStockNumber, int day) {
		return correlationCoeffData.get(stockNumber).get(secondStockNumber)[day];
	}

	public double getCovarianceData(int stockNumber, int day) {
		return stockCovarianceData[stockNumber][day];
	}

	public double getMarketVariance(int day) {
		return marketIndexVarianceData[day];
	}

	public Map<Integer, Map<Integer, double[]>> getCorrelationCoeffData() {
		return correlationCoeffData;
	}

}
