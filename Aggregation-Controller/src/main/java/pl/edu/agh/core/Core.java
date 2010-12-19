package pl.edu.agh.core;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import pl.edu.agh.agg.controller.IAggregationController;
import pl.edu.agh.agg.controller.impl.AggregationControllerImpl;

public class Core {

	static Logger logger = Logger.getLogger(Core.class);

	public static void main(String[] args) {

		PropertyConfigurator.configure("log4j.properties");
		Resource resource = new ClassPathResource("context.xml");
		BeanFactory factory = new XmlBeanFactory(resource);

		IAggregationController aggController = (AggregationControllerImpl) factory
				.getBean("aggregationController");

		aggController.getResultsFromComputingNodes();

	}

}
