package wekaCore;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import ui.FileExplorer;
import weka.classifiers.Classifier;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

public class PromoterFinder {
	private ArrayList<Classifier> m_models;
	private String m_basePath;
	
	public PromoterFinder(ArrayList<Classifier> models) throws Exception
	{
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
			double result = models.get(i).classifyInstance(instances.lastInstance());
			JOptionPane.showMessageDialog(null, result);
		}
	}
	
	private void importModels()
	{
		
	}
}
