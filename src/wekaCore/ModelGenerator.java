package wekaCore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JOptionPane;

import ui.FileExplorer;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.BestFirst;
import weka.attributeSelection.CfsSubsetEval;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.evaluation.Evaluation;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.meta.AttributeSelectedClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

public class ModelGenerator {
	private ArrayList<Classifier> m_models;
	
	public ModelGenerator(String filePath) {
		J48 j48Model = new J48();
		NaiveBayes naiveBayesModel = new NaiveBayes();
		MultilayerPerceptron mlpModel = new MultilayerPerceptron();

		//AttributeSelectedClassifier j48Classifier = new AttributeSelectedClassifier();
		//AttributeSelectedClassifier naiveClassifier = new AttributeSelectedClassifier();
		//AttributeSelectedClassifier mlpClassifier = new AttributeSelectedClassifier();
		
		try {
			DataSource baseInstances = new DataSource(filePath);
			Instances instances = baseInstances.getDataSet();
			instances.setClassIndex(instances.numAttributes() - 1);
			Instances reducedInstances = instances;
			
			int dialogResult = JOptionPane.showConfirmDialog(null, "Você gostaria de reduzir a base?" + 
					"O processo pode ser demorado, mas a geração dos modelos será mais rápida.", "Atenção!", JOptionPane.YES_NO_OPTION);
			if(dialogResult == JOptionPane.YES_OPTION){
				// Attribute selection
				AttributeSelection attsel = new AttributeSelection();
				CfsSubsetEval subsetEval = new CfsSubsetEval();
				BestFirst bestFirst = new BestFirst();

				attsel.setEvaluator(subsetEval);
				attsel.setSearch(bestFirst);
				attsel.SelectAttributes(instances);
				reducedInstances = attsel.reduceDimensionality(instances);
				
				JOptionPane.showMessageDialog(null, "Reduced vs normal " + reducedInstances.numAttributes() + " " + instances.numAttributes());
				
				// Save the reduced base to avoid waiting for hours...
				String reducedBasePath = filePath.replace(".arff", "_REDUCED.arff");
				ArffSaver saver = new ArffSaver();
				saver.setInstances(reducedInstances);
				saver.setFile(new File(reducedBasePath));
				saver.writeBatch();
			}
			
			j48Model.buildClassifier(reducedInstances);
			naiveBayesModel.buildClassifier(reducedInstances);
			mlpModel.buildClassifier(reducedInstances);

			m_models = new ArrayList<Classifier>();
			
			m_models.add(j48Model);
			m_models.add(naiveBayesModel);
			m_models.add(mlpModel);
			
			evaluateModels(reducedInstances);
			
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
				JOptionPane.showMessageDialog(null, "Evaluating model " + i);
				eval.crossValidateModel(m_models.get(i), instances, 10, new Random(1));
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
