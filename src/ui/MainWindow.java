package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.trees.J48;
import wekaCore.ModelGenerator;


@SuppressWarnings("serial")
public class MainWindow extends JFrame implements ActionListener {
	// Actions
	private String IMPORT_BASE = "importBase";
	private String GENERATE_MODELS = "generateModels";
	private String IMPORT_MODELS = "importModels";
	private String FIND_PROMOTERS = "findPromoters";
	
	// Top components
	private Box hBoxLayoutImportBase;
	private Component m_hBoxImportLeftSpacer;
	public JTextField m_basePath;
	private JButton m_importBaseBtn;
	private Component m_hBoxImportRightSpacer;

	// Middle components
	private Box m_hBoxActionButtons;
	private Component m_hBoxActionBtnLeftSpacer;
	private JButton m_generateModelsBtn;
	private Component m_hBoxActionBtnLeftMiddleSpacer;
	private JButton m_importModelsBtn;
	private Component m_hBoxActionBtnRightMiddleSpacer;
	private JButton m_findPromotersBtn;
	private Component m_hBoxActionBtnRightSpacer;
	
	private J48 m_j48Model;
	private NaiveBayes m_naiveBayesModel;
	private MultilayerPerceptron m_mlpModel;

	@SuppressWarnings("deprecation")
	public MainWindow() {
		setTitle("Projeto Final");
		setResizable(false);

		createImportLayout();
		createActionButtonsLayout();

		setSize(502, 85);
		show();
	}

	private void createImportLayout() {
		hBoxLayoutImportBase = Box.createHorizontalBox();
		getContentPane().add(hBoxLayoutImportBase, BorderLayout.NORTH);

		m_hBoxImportLeftSpacer = Box.createHorizontalStrut(20);
		hBoxLayoutImportBase.add(m_hBoxImportLeftSpacer);

		m_basePath = new JTextField();
		m_basePath.setEditable(false);
		hBoxLayoutImportBase.add(m_basePath);
		m_basePath.setColumns(10);

		m_importBaseBtn = new JButton("Importar base");
		m_importBaseBtn.setActionCommand(IMPORT_BASE);
		m_importBaseBtn.addActionListener(this);
		hBoxLayoutImportBase.add(m_importBaseBtn);

		m_hBoxImportRightSpacer = Box.createHorizontalStrut(20);
		hBoxLayoutImportBase.add(m_hBoxImportRightSpacer);
	}

	private void createActionButtons() {
		m_generateModelsBtn = new JButton("Gerar modelos");
		m_generateModelsBtn.setEnabled(false);
		m_generateModelsBtn.setActionCommand(GENERATE_MODELS);
		m_generateModelsBtn.addActionListener(this);
		m_hBoxActionButtons.add(m_generateModelsBtn);

		m_hBoxActionBtnLeftMiddleSpacer = Box.createHorizontalGlue();
		m_hBoxActionButtons.add(m_hBoxActionBtnLeftMiddleSpacer);

		m_importModelsBtn = new JButton("Importar modelos");
		m_importModelsBtn.setEnabled(false);
		m_importModelsBtn.setActionCommand(IMPORT_MODELS);
		m_importModelsBtn.addActionListener(this);
		m_hBoxActionButtons.add(m_importModelsBtn);

		m_hBoxActionBtnRightMiddleSpacer = Box.createHorizontalGlue();
		m_hBoxActionButtons.add(m_hBoxActionBtnRightMiddleSpacer);

		m_findPromotersBtn = new JButton("Encontrar promotores");
		m_findPromotersBtn.setEnabled(false);
		m_findPromotersBtn.setActionCommand(FIND_PROMOTERS);
		m_findPromotersBtn.addActionListener(this);
		m_hBoxActionButtons.add(m_findPromotersBtn);

		m_hBoxActionBtnRightSpacer = Box.createHorizontalGlue();
		m_hBoxActionButtons.add(m_hBoxActionBtnRightSpacer);
	}

	private void createActionButtonsLayout() {
		m_hBoxActionButtons = Box.createHorizontalBox();
		getContentPane().add(m_hBoxActionButtons, BorderLayout.CENTER);

		m_hBoxActionBtnLeftSpacer = Box.createHorizontalGlue();
		m_hBoxActionButtons.add(m_hBoxActionBtnLeftSpacer);

		createActionButtons();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (IMPORT_BASE.equals(e.getActionCommand())) {
			FileExplorer arffExplorer = new FileExplorer();
			arffExplorer.exploreFiles();
			m_basePath.setText(arffExplorer.getFilePath());
			m_generateModelsBtn.setEnabled(true);
			m_importModelsBtn.setEnabled(true);
		} else if (GENERATE_MODELS.equals(e.getActionCommand())) {
			ModelGenerator models = new ModelGenerator(m_basePath.getText());
			ArrayList<Classifier> modelList = new ArrayList<Classifier>();
			modelList = models.getModels();
			m_j48Model = (J48) modelList.get(0);
			m_naiveBayesModel = (NaiveBayes) modelList.get(1);
			m_mlpModel = (MultilayerPerceptron) modelList.get(2);
		} else if (IMPORT_MODELS.equals(e.getActionCommand())) {
			/// TODO: Block the button if a model was generated.
		} else if (FIND_PROMOTERS.equals(e.getActionCommand())) {
			/// TODO: Check if any model isnt null.
			/// The user may find promoters using only one model.
		}
	}
	
	public static void main(String[] args) {
		MainWindow app = new MainWindow();
		app.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
	}
}
