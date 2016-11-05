package ui;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileExplorer {
	public FileExplorer() {

	}

	public String exploreArffFiles() {
		String filePath = "";
		JFileChooser fileExplorer = new JFileChooser();
		fileExplorer.setDialogTitle("Selecione uma base ARFF");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("ARFF Files", "arff");
		fileExplorer.setFileFilter(filter);
		int ret = fileExplorer.showOpenDialog(fileExplorer);

		try {
			if(ret == JFileChooser.APPROVE_OPTION)
			{
				File file = fileExplorer.getSelectedFile();
				filePath = file.getAbsolutePath();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return filePath;
	}

	public String searchDir() {
		String path = new File("").getAbsolutePath();
		JFileChooser fileExplorer = new JFileChooser();
		fileExplorer.setDialogTitle("Selecione uma pasta");
		fileExplorer.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fileExplorer.setAcceptAllFileFilterUsed(false);
		int ret = fileExplorer.showOpenDialog(fileExplorer);
		
		if(ret == JFileChooser.APPROVE_OPTION)
		{
			path = fileExplorer.getSelectedFile().getAbsolutePath();
		}

		return path;
	}
	
	public ArrayList<String> exploreModels()
	{
		JFileChooser fileExplorer = new JFileChooser();
		fileExplorer.setDialogTitle("Selecione um ou mais modelos");
		fileExplorer.setMultiSelectionEnabled(true);
		int ret = fileExplorer.showOpenDialog(fileExplorer);
		
		ArrayList<String> modelsPath = new ArrayList<String>();
		if(ret == JFileChooser.APPROVE_OPTION)
		{
			File[] files = fileExplorer.getSelectedFiles();
			for(int i = 0; i < files.length; i++)
			{
				modelsPath.add(files[i].getAbsolutePath());
			}
		}
		return modelsPath;
	}
	
	public void exportResults(String toExport)
	{
		JOptionPane.showMessageDialog(null, "Selecione onde deseja salvar o arquivo.");
		String path = searchDir();
		String target;
		
		if(toExport.contains("J48"))
		{
			target = "J48 Results";
		}
		else if(toExport.contains("Naive"))
		{
			target = "Naive Bayes Results";
		}
		else if(toExport.contains("MLP") || toExport.contains("Multilayer"))
		{
			target = "MLP Results";
		}
		else
		{
			target = "Results";
		}

		
		try{
			PrintWriter writer = new PrintWriter(path + "/" + target + ".txt", "UTF-8");
		    toExport = toExport.replace("<html>", "");
		    toExport = toExport.replace("</html>", "");
		    toExport = toExport.replace("<br>", "\n");
		    writer.print(toExport);
		    writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}