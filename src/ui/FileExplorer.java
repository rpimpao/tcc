package ui;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileExplorer {
	public FileExplorer() {

	}

	public String exploreArffFiles() {
		String filePath = "";
		JFileChooser fileExplorer = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("ARFF Files", "arff");
		fileExplorer.setFileFilter(filter);
		fileExplorer.showOpenDialog(fileExplorer);

		try {
			File file = fileExplorer.getSelectedFile();
			filePath = file.getAbsolutePath();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return filePath;
	}

	public String searchDir() {
		String path = null;
		JFileChooser fileExplorer = new JFileChooser();
		fileExplorer.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileExplorer.setAcceptAllFileFilterUsed(false);
		fileExplorer.showOpenDialog(null);
		path = fileExplorer.getSelectedFile().getAbsolutePath();

		return path;
	}
	
	public ArrayList<String> exploreModels()
	{
		JFileChooser fileExplorer = new JFileChooser();
		fileExplorer.setMultiSelectionEnabled(true);
		fileExplorer.showOpenDialog(fileExplorer);
		File[] files = fileExplorer.getSelectedFiles();
		ArrayList<String> modelsPath = new ArrayList<String>();
		for(int i = 0; i < files.length; i++)
		{
			modelsPath.add(files[i].getAbsolutePath());
		}
		return modelsPath;
	}
}