package wekaCore;

import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.trees.J48;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;

public class GenerateModels {
	private J48 m_j48Model;
	private NaiveBayes m_naiveBayesModel;
	private MultilayerPerceptron m_mlpModel;

	public GenerateModels(String filePath) {
		m_j48Model = new J48();
		m_naiveBayesModel = new NaiveBayes();
		m_mlpModel = new MultilayerPerceptron();

		try {
			DataSource baseInstances = new DataSource(filePath);
			Instances instances = baseInstances.getDataSet();
			// TODO: set class attibute?
			instances.setClassIndex(instances.numAttributes() - 1);

			m_j48Model.buildClassifier(instances);
			m_naiveBayesModel.buildClassifier(instances);
			m_mlpModel.buildClassifier(instances);

			// Save models
			// TODO: open file explorer to let user choose where to save the models
			ObjectOutputStream j48OutputStream = new ObjectOutputStream(new FileOutputStream("C:/Users/Rodrigo/Desktop/j48.model"));
			j48OutputStream.writeObject(m_j48Model);
			j48OutputStream.flush();
			j48OutputStream.close();

			ObjectOutputStream naiveBayesOutputStream = new ObjectOutputStream(new FileOutputStream("C:/Users/Rodrigo/Desktop/naiveBayes.model"));
			naiveBayesOutputStream.writeObject(m_naiveBayesModel);
			naiveBayesOutputStream.flush();
			naiveBayesOutputStream.close();
			
			ObjectOutputStream mlpOutputStream = new ObjectOutputStream(new FileOutputStream("C:/Users/Rodrigo/Desktop/mlp.model"));
			mlpOutputStream.writeObject(m_naiveBayesModel);
			mlpOutputStream.flush();
			mlpOutputStream.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
