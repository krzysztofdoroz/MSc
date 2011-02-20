package pl.edu.agh.msc.data.source.interfaces;

public interface IDataSource {

	double getStockData(int stockNumber, int day);
	double getStandardDevData(int stockNumber, int day);
	double getCorrelationCoeffData(int stockNumber, int day);
	
}
