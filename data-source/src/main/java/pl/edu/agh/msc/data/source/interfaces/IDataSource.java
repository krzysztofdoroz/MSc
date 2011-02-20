package pl.edu.agh.msc.data.source.interfaces;

import java.util.List;

public interface IDataSource {

	double getStockData(int stockNumber, int day);
	double getStandardDevData(int stockNumber, int day);
	double getCorrelationCoeffData(int stockNumber, int day);
	void loadStockStandardDeviationData(List<String> filenames);
	void loadStockStandardCorrelationCoeffData(List<String> filenames);
}
