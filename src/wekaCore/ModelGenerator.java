package wekaCore;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import ui.FileExplorer;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class ModelGenerator {
	private ArrayList<Classifier> m_models;
	
	public ModelGenerator(String filePath) {
		J48 j48Model = new J48();
		NaiveBayes naiveBayesModel = new NaiveBayes();
		MultilayerPerceptron mlpModel = new MultilayerPerceptron();

		try {
			DataSource baseInstances = new DataSource(filePath);
			Instances instances = baseInstances.getDataSet();
			// TODO: set class attibute?
			instances.setClassIndex(instances.numAttributes() - 1);

			j48Model.buildClassifier(instances);
			naiveBayesModel.buildClassifier(instances);
			mlpModel.buildClassifier(instances);

			m_models = new ArrayList<Classifier>();
			
			m_models.add(j48Model);
			m_models.add(naiveBayesModel);
			m_models.add(mlpModel);
			
			evaluateModels(instances);
			
			/// TODO: Ask to save models
			saveModels();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveModels()
	{
		FileExplorer arf = new FileExplorer();
		String path = arf.searchDir();
		
		for (int i = 0; i < m_models.size(); i++) {
			ObjectOutputStream mlpOutputStream;
			try {
				mlpOutputStream = new ObjectOutputStream(
						new FileOutputStream(path + "/" + m_models.get(i).getClass().getSimpleName() + ".model"));
				mlpOutputStream.writeObject(m_models.get(i));
				mlpOutputStream.flush();
				mlpOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void evaluateModels(Instances instances)
	{
		Evaluation eval;
		for (int i = 0; i < m_models.size(); i++) {
			try {
				eval = new Evaluation(instances);
				eval.crossValidateModel(m_models.get(i), instances, 10, new Random(1));
				//JOptionPane.showMessageDialog(null, eval.correct());
				/// TODO: How to show the user the algorithm accuracy?
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<Classifier> getModels()
	{
		return m_models;
	}
}
