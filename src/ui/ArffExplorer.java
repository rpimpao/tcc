package ui;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class ArffExplorer {
	private String m_filePath;
	public ArffExplorer()
	{
		exploreFiles();
	}
	
	private void exploreFiles()
	{	
		JFileChooser fileExplorer = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("ARFF Files", "arff");
		fileExplorer.setFileFilter(filter);
		fileExplorer.showOpenDialog(fileExplorer);
		
		File file = fileExplorer.getSelectedFile();
		m_filePath = file.getAbsolutePath();
	}
	
	public String getFilePath()
	{
		return m_filePath;
	}
}