package wekaCore;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import ui.FileExplorer;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class PromoterFinder {
	private ArrayList<Classifier> m_models;
	private String m_basePath;
	private HashMap<String,ArrayList<String>> m_results;
	
	public PromoterFinder(ArrayList<Classifier> models) throws Exception 
	{
		JOptionPane.showMessageDialog(null, "Importe uma base para ser classificada.");
		FileExplorer explorer = new FileExplorer();
		m_basePath = explorer.exploreArffFiles();
		
		m_results = new HashMap<String,ArrayList<String>>();
		
		if(!m_basePath.contains(".arff"))
		{
			return;
		}

		if (models.isEmpty()) 
		{
			importModels();
		} 
		else 
		{
			m_models = models;
		}

		classifyInstances();
	}

	private void classifyInstances() throws Exception {
		DataSource baseInstances = new DataSource(m_basePath);
		// Unlabled
		Instances instances = baseInstances.getDataSet();
		instances.setClassIndex(instances.numAttributes() - 1);

		Instances labeled = new Instances(instances);
		
		for (int i = 0; i < m_models.size(); i++) 
		{
			// label instances
			ArrayList<String> results = new ArrayList<String>();
			for (int j = 0; j < instances.numInstances(); j++) 
			{
				double result = m_models.get(i).classifyInstance(instances.instance(j));
				labeled.instance(j).setClassValue(result);

				double[] output = m_models.get(i).distributionForInstance(instances.instance(j));
				
				String formattedResult = "Atual: " + instances.classAttribute().value((int) instances.instance(j).classValue()) 
						+ ", Previsto: " + instances.classAttribute().value((int) result)
						+ ", Precisão: " + output[((int) result)] * 100 + "%\n";
				
				results.add(formattedResult);
			}
			m_results.put(m_models.get(i).getClass().getName(), results);
		}

		// save labeled data
		BufferedWriter writer = new BufferedWriter(new FileWriter(m_basePath.replace(".arff", "_CLASSIFICADA.arff")));
		writer.write(labeled.toString());
		writer.newLine();
		writer.flush();
		writer.close();
	}

	private void importModels()
	{
		JOptionPane.showMessageDialog(null, "Selecione os modelos que serão usados na classificação.");
		FileExplorer explorer = new FileExplorer();
		ArrayList<String> modelsPath = explorer.exploreModels();
		m_models = new ArrayList<Classifier>();
		for(int i = 0; i < modelsPath.size(); i++)
		{
			try {
				Classifier loadedClassifier = (Classifier) SerializationHelper.read(new FileInputStream(modelsPath.get(i)));
				m_models.add(loadedClassifier);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public HashMap<String, ArrayList<String>> getResults()
	{
		return m_results;
	}
}
