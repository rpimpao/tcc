package wekaCore;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import ui.FileExplorer;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils.DataSource;

public class PromoterFinder {
	private ArrayList<Classifier> m_models;
	private String m_basePath;
	
	public PromoterFinder(ArrayList<Classifier> models) throws Exception
	{
		JOptionPane.showMessageDialog(null, "Importe uma base para ser classificada.");
		FileExplorer explorer = new FileExplorer();
		m_basePath = explorer.exploreArffFiles();
		
		if(models.isEmpty())
		{
			importModels();
		}
		else
		{
			m_models = models;
		}
		
		DataSource baseInstances = new DataSource(m_basePath);
		Instances instances = baseInstances.getDataSet();
		// TODO: set class attibute?
		instances.setClassIndex(instances.numAttributes() - 1);
		
		for(int i = 0; i < m_models.size(); i++)
		{
			// Run with the last instance of the dataset
			double result = m_models.get(i).classifyInstance(instances.lastInstance());
			JOptionPane.showMessageDialog(null, result);
		}
	}
	
	private void importModels()
	{
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
}
