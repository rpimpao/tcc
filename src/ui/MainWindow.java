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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import weka.classifiers.Classifier;
import wekaCore.ModelGenerator;


@SuppressWarnings("serial")
public class MainWindow extends JFrame implements ActionListener {
	// Actions
	private String IMPORT_BASE = "importBase";
	private String GENERATE_MODELS = "generateModels";
	private String IMPORT_MODELS = "importModels";
	private String FIND_PROMOTERS = "findPromoters";
	
	// Top layout
	private Box m_vTopLayout;
	
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
	private JButton m_findPromotersBtn;
	private Component m_hBoxActionBtnRightSpacer;
	
	// Tabs
	private JTabbedPane m_tabPanel;
	private JPanel m_j48Panel;
	private JPanel m_naivePanel;
	private JPanel m_mlpPanel;
	private JLabel m_j48Result;
	private JLabel m_naiveResult;
	private JLabel m_mlpResult;

	@SuppressWarnings("deprecation")
	public MainWindow() {
		setTitle("Projeto Final");
		setResizable(false);

		createImportLayout();
		createActionButtonsLayout();
		createTopLayout();
		createTabs();
		
		m_tabPanel.setVisible(false);

		setSize(500, 100);
		show();
	}

	private void createTopLayout()
	{
		m_vTopLayout = Box.createVerticalBox();
		getContentPane().add(m_vTopLayout, BorderLayout.NORTH);
		
		m_vTopLayout.add(hBoxLayoutImportBase);
		m_vTopLayout.add(m_hBoxActionButtons);
	}
	
	private void createImportLayout() {
		hBoxLayoutImportBase = Box.createHorizontalBox();

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

		m_hBoxActionBtnLeftSpacer = Box.createHorizontalGlue();
		m_hBoxActionButtons.add(m_hBoxActionBtnLeftSpacer);

		createActionButtons();
	}
	
	private void createTabs()
	{
		m_tabPanel = new JTabbedPane();
		m_tabPanel.setSize(500, 400);
		getContentPane().add(m_tabPanel, BorderLayout.CENTER);
		
		m_j48Panel = new JPanel();
		m_naivePanel = new JPanel();
		m_mlpPanel = new JPanel();
		
		m_tabPanel.addTab("J48", m_j48Panel);
		m_tabPanel.addTab("Naive Bayes", m_naivePanel);
		m_tabPanel.addTab("MLP", m_mlpPanel);
		
		m_j48Result = new JLabel();
		m_naiveResult = new JLabel();
		m_mlpResult = new JLabel();
		
		m_j48Panel.add(m_j48Result);
		m_naivePanel.add(m_naiveResult);
		m_mlpPanel.add(m_mlpResult);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (IMPORT_BASE.equals(e.getActionCommand())) {
			FileExplorer fileExplorer = new FileExplorer();
			String filePath = fileExplorer.exploreArffFiles();
			m_basePath.setText(filePath);
			m_generateModelsBtn.setEnabled(true);
			m_findPromotersBtn.setEnabled(true);
		} else if (GENERATE_MODELS.equals(e.getActionCommand())) {
			ModelGenerator models = new ModelGenerator(m_basePath.getText());
			ArrayList<Classifier> modelList = new ArrayList<Classifier>();
			modelList = models.getModels();
			if(!modelList.isEmpty())
			{
				// Weird stuff. Had to replace \n to <br> and set the text as html inside the label to allow "multilines"...
				m_j48Result.setText("<html>" + modelList.get(0).toString().replace("\n", "<br>") + "</html>");
				m_naiveResult.setText("<html>" + modelList.get(1).toString().replace("\n", "<br>") + "</html>");
				//m_mlpResult.setText("<html>" + modelList.get(2).toString().replace("\n", "<br>") + "</html>");
				
				m_tabPanel.setVisible(true);
				setSize(getWidth(), getHeight() + m_tabPanel.getHeight());
			}
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
