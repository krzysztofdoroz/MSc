package pl.edu.agh.msc.data.source.interfaces;

import java.util.List;

public interface IDataSource {

	double getStockData(int stockNumber, int day);
	double getStandardDevData(int stockNumber, int day);
	double getCorrelationCoeffData(int stockNumber, int day);
	double getCovarianceData(int stockNumber, int day);
	double getMarketVariance(int day);
	void loadStockStandardDeviationData(List<String> filenames);
	void loadStockStandardCorrelationCoeffData(List<String> filenames);
	void loadStockCovarianceData(List<String> filenames);
	void loadMarketVarianceData(String filename);
}
