package ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;

import weka.classifiers.Classifier;
import wekaCore.ModelGenerator;
import wekaCore.PromoterFinder;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements ActionListener {
	// Actions
	private String IMPORT_BASE = "importBase";
	private String GENERATE_MODELS = "generateModels";
	private String FIND_PROMOTERS = "findPromoters";
	private String EXPORT_RESULT = "exportResults";

	// Top layout and components
	private Box m_vTopLayout;
	private Box hBoxLayoutImportBase;
	private Component m_hBoxImportLeftSpacer;
	private JTextField m_basePath;
	private JButton m_importBaseBtn;
	private Component m_hBoxImportRightSpacer;
	private Box m_hBoxActionButtons;
	private Component m_hBoxActionBtnLeftSpacer;
	private JButton m_generateModelsBtn;
	private Component m_hBoxActionBtnLeftMiddleSpacer;
	private JButton m_findPromotersBtn;
	private Component m_hBoxActionBtnRightSpacer;

	// Tabs (center components)
	private JTabbedPane m_tabPanel;
	private JPanel m_j48Panel;
	private JPanel m_naivePanel;
	private JPanel m_mlpPanel;
	private JLabel m_j48Result;
	private JLabel m_naiveResult;
	private JLabel m_mlpResult;
	private JScrollPane m_j48ScrollPanel;
	private JScrollPane m_naiveScrollPanel;
	private JScrollPane m_mlpScrollPanel;
	private JButton m_j48Export;
	private JButton m_naiveExport;
	private JButton m_mlpExport;

	private ArrayList<Classifier> m_models;

	@SuppressWarnings("deprecation")
	public MainWindow() {
		setTitle("Projeto Final");
		setResizable(false);
		m_models = new ArrayList<Classifier>();

		createImportLayout();
		createActionButtonsLayout();
		createTopLayout();
		createTabs();

		m_tabPanel.setVisible(false);

		setSize(500, 100);
		show();
	}

	private void createTopLayout() {
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

		m_findPromotersBtn = new JButton("Prever");
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

	private void createTabs() {
		m_tabPanel = new JTabbedPane();
		m_tabPanel.setSize(500, 400);
		getContentPane().add(m_tabPanel, BorderLayout.CENTER);

		m_j48Panel = new JPanel();
		m_naivePanel = new JPanel();
		m_mlpPanel = new JPanel();

		m_j48Export = new JButton("Exportar J48");
		m_naiveExport = new JButton("Exportar Naive Bayes");
		m_mlpExport = new JButton("Exportar MLP");

		m_j48Export.setActionCommand(EXPORT_RESULT);
		m_j48Export.addActionListener(this);
		m_naiveExport.setActionCommand(EXPORT_RESULT);
		m_naiveExport.addActionListener(this);
		m_mlpExport.setActionCommand(EXPORT_RESULT);
		m_mlpExport.addActionListener(this);

		m_j48Result = new JLabel();
		m_naiveResult = new JLabel();
		m_mlpResult = new JLabel();

		m_j48Panel.setLayout(new BoxLayout(m_j48Panel, BoxLayout.PAGE_AXIS));
		m_naivePanel.setLayout(new BoxLayout(m_naivePanel, BoxLayout.PAGE_AXIS));
		m_mlpPanel.setLayout(new BoxLayout(m_mlpPanel, BoxLayout.PAGE_AXIS));

		m_j48Panel.add(m_j48Export);
		m_j48Panel.add(m_j48Result);
		m_naivePanel.add(m_naiveExport);
		m_naivePanel.add(m_naiveResult);
		m_mlpPanel.add(m_mlpExport);
		m_mlpPanel.add(m_mlpResult);

		m_j48ScrollPanel = new JScrollPane(m_j48Panel);
		m_j48ScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		m_j48ScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		m_naiveScrollPanel = new JScrollPane(m_naivePanel);
		m_naiveScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		m_naiveScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		m_mlpScrollPanel = new JScrollPane(m_mlpPanel);
		m_mlpScrollPanel.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		m_mlpScrollPanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		m_tabPanel.addTab("J48", m_j48ScrollPanel);
		m_tabPanel.addTab("Naive Bayes", m_naiveScrollPanel);
		m_tabPanel.addTab("MLP", m_mlpScrollPanel);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (IMPORT_BASE.equals(e.getActionCommand())) {
			FileExplorer fileExplorer = new FileExplorer();
			String filePath = fileExplorer.exploreArffFiles();
			if (!filePath.equals("")) {
				m_basePath.setText(filePath);
				m_generateModelsBtn.setEnabled(true);
			}
		} else if (GENERATE_MODELS.equals(e.getActionCommand())) {
			ModelGenerator models = new ModelGenerator(m_basePath.getText());
			ArrayList<Classifier> modelList = new ArrayList<Classifier>();
			ArrayList<String> evalList = new ArrayList<String>();
			modelList = models.getModels();
			evalList = models.getEvalResult();
			if (!modelList.isEmpty()) {
				m_models = modelList;

				// Weird stuff. Had to replace \n to <br> and set the text as
				// html inside the label to allow "multilines"...
				m_j48Result.setText("<html>" + modelList.get(0).toString().replace("\n", "<br>")
						+ evalList.get(0).replace("\n", "<br>") + "</html>");
				m_naiveResult.setText("<html>" + modelList.get(1).toString().replace("\n", "<br>")
						+ evalList.get(1).replace("\n", "<br>") + "</html>");
				m_mlpResult.setText("<html>" + modelList.get(2).toString().replace("\n", "<br>")
						+ evalList.get(2).replace("\n", "<br>") + "</html>");

				if (!m_tabPanel.isVisible()) {
					m_tabPanel.setVisible(true);
					setSize(getWidth(), getHeight() + m_tabPanel.getHeight());
				}
			}
		} else if (EXPORT_RESULT.equals(e.getActionCommand())) {
			JButton o = (JButton) e.getSource();
			String buttonName = o.getText();
			FileExplorer fileExplorer = new FileExplorer();
			if (buttonName.contains("J48")) {
				fileExplorer.exportResults(m_j48Result.getText(), "J48");
			} else if (buttonName.contains("Naive")) {
				fileExplorer.exportResults(m_naiveResult.getText(), "Naive Bayes");
			} else if (buttonName.contains("MLP")) {
				fileExplorer.exportResults(m_mlpResult.getText(), "MLP");
			}
		} else if (FIND_PROMOTERS.equals(e.getActionCommand())) {
			try {
				PromoterFinder finder = new PromoterFinder(m_models);
				HashMap<String, ArrayList<String>> results = finder.getResults();
				if (!results.isEmpty()) {
					for (Map.Entry<String, ArrayList<String>> entry : results.entrySet()) {
						String key = entry.getKey();
						ArrayList<String> value = entry.getValue();
						if (key.contains("J48")) {
							m_j48Result.setText(m_j48Result.getText().replace("</html>",
									"<br>" + value.toString().replace("\n", "<br>") + "</html>"));
						} else if (key.contains("Naive")) {
							m_naiveResult.setText(m_naiveResult.getText().replace("</html>",
									"<br>" + value.toString().replace("\n", "<br>") + "</html>"));
						} else if (key.contains("Multilayer")) {
							m_mlpResult.setText(m_mlpResult.getText().replace("</html>",
									"<br>" + value.toString().replace("\n", "<br>") + "</html>"));
						}
					}

					if (!m_tabPanel.isVisible()) {
						m_tabPanel.setVisible(true);
						setSize(getWidth(), getHeight() + m_tabPanel.getHeight());
					}
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}
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
