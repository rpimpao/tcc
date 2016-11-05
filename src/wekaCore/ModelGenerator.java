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
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.ConverterUtils.DataSource;

public class ModelGenerator {
	private ArrayList<Classifier> m_models;
	private ArrayList<String> m_evalResult;
	
	public ModelGenerator(String filePath) {
		J48 j48Model = new J48();
		NaiveBayes naiveBayesModel = new NaiveBayes();
		MultilayerPerceptron mlpModel = new MultilayerPerceptron();
		
		try {
			DataSource baseInstances = new DataSource(filePath);
			Instances instances = baseInstances.getDataSet();
			instances.setClassIndex(instances.numAttributes() - 1);
			Instances reducedInstances = instances;
			
			int dialogResult = JOptionPane.showConfirmDialog(null, "Você gostaria de reduzir a base?" + 
					" O processo pode ser demorado, mas a geração dos modelos será mais rápida.", "Atenção!", JOptionPane.YES_NO_OPTION);
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
			
			dialogResult = JOptionPane.showConfirmDialog(null, "Você gostaria de salvar os modelos gerados?", "Atenção!", JOptionPane.YES_NO_OPTION);
			if(dialogResult == JOptionPane.YES_OPTION){
				saveModels(filePath);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void saveModels(String filePath)
	{
		FileExplorer arf = new FileExplorer();
		String path = arf.searchDir();
		
		File arffFile = new File(filePath);
		
		for (int i = 0; i < m_models.size(); i++) {
			ObjectOutputStream mlpOutputStream;
			try {
				mlpOutputStream = new ObjectOutputStream(
						new FileOutputStream(path + "/" + m_models.get(i).getClass().getSimpleName() + "_" + arffFile.getName().replace(".arff", "") + ".model"));
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
		m_evalResult = new ArrayList<String>();
		for (int i = 0; i < m_models.size(); i++) {
			try {
				eval = new Evaluation(instances);
				eval.crossValidateModel(m_models.get(i), instances, 10, new Random(1));
				m_evalResult.add(eval.toSummaryString("\nResults\n======\n", false).concat(eval.toClassDetailsString("\nDetailed Accuracy Results\n======\n")).concat(eval.toMatrixString("\nConfusion Matrix\n======\n")));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<Classifier> getModels()
	{
		return m_models;
	}
	
	public ArrayList<String> getEvalResult()
	{
		return m_evalResult;
	}
}
