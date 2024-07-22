/*
 * Created on 16/11/2005
 *
 */
package proverinterface;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.BevelBorder;

import logic.valuation.AbstractValuation;
import logic.valuation.CPLValuation;
import logic.valuation.MBCValuation;
import logicalSystems.classicalLogic.ClassicalConnectives;
import logicalSystems.classicalLogic.ClassicalSigns;
import main.newstrategy.c1.simple.C1SimpleStrategy;
import main.newstrategy.c1.simple.comparator.ConsistencyComplexityComparator;
import main.newstrategy.cpl.configurable.comparator.ComplexitySignedFormulaComparator;
import main.newstrategy.cpl.configurable.comparator.ConnectiveSignedFormulaComparator;
import main.newstrategy.cpl.configurable.comparator.ISignedFormulaComparator;
import main.newstrategy.cpl.configurable.comparator.InsertionOrderSignedFormulaComparator;
import main.newstrategy.cpl.configurable.comparator.NormalFormulaOrderSignedFormulaComparator;
import main.newstrategy.cpl.configurable.comparator.ReverseFormulaOrderSignedFormulaComparator;
import main.newstrategy.cpl.configurable.comparator.ReverseInsertionOrderSignedFormulaComparator;
import main.newstrategy.cpl.configurable.comparator.SignSignedFormulaComparator;
import main.newstrategy.cpl.simple.configurable.ConfigurableSimpleStrategy;
import main.newstrategy.mbc.simple.MBCSimpleStrategy;
import main.newstrategy.mbc.simple.configurable.MBCConfigurableSimpleStrategy;
import main.newstrategy.mbc.simple.optional.MBCSimpleWithOptionalRulesStrategy;
import main.newstrategy.mci.simple.MCISimpleStrategy;
import main.newstrategy.mci.simple.configurable.MCIConfigurableSimpleStrategy;
import main.newstrategy.mci.simple.optional.MCISimpleWithOptionalRulesStrategy;
import main.newstrategy.memorysaver.MemorySaverStrategy;
import main.newstrategy.simple.SimpleStrategy;
import main.newstrategy.simple.backjumping.BackjumpingSimpleStrategy;
import main.newstrategy.simple.learning.LearningSimpleStrategy;
import main.newstrategy.simple.newlearning.NewLearningSimpleStrategy;

/**
 * The frame that allows the user to configure the prover. It allows him/her to
 * choose the strategy, the parser used, the naumber of times to run each
 * problem... And it also allows him/her to set a list of configurations to run
 * with problem families.
 * 
 * @author Adolfo Gustavo Serra Seca Neto
 * 
 */
public class ProverConfigurator extends JFrame implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8359803422699345192L;

	private JComboBox timesCombo;

	private JComboBox parsingLibNameCombo;

	private JComboBox rulesStructureNameCombo;

	private JComboBox strategyNameCombo;

	private JPanel strategyNamePanel;

	private JComboBox logicNameCombo;

	private JComboBox signedFormulaComparatorCombo;

	private JCheckBox saveOriginCheckbox;

	private JCheckBox discardBranchesCheckbox;

	private JCheckBox saveDiscardedBranchesCheckbox;

	// private JCheckBox backjumpingCheckbox;

	private JList selectionList;

	private LinkedList<ProverConfiguration> selectionListData;

	private JComboBox timeLimitCombo;

	private static final String CPL_LOGIC = "CPL - Classical Propositional Logic";

	private static final String MBC_LOGIC = "mbC - A Propositional Logic of Formal Inconcistency";

	private static final String MCI_LOGIC = "mCi - Another Propositional Logic of Formal Inconcistency";

	private static final String C1_LOGIC = "C1 - A Propositional Paraconsistent Logic";

	private static final String IPL_LOGIC = "IPL - Intuitionistic Propositional Logic";

	private static final String SATS5_PARSER = "sats5";

	// private static final String SATLFI_PARSER = "satlfi";

	private static final String SATCNF_PARSER = "satcnf2";

	private static final String SATLFIINCONSDEF_PARSER = "satlfiinconsdef";

	private static final String IPL_PARSER = "ipl";

	private static final String[] EMPTY_LIST = new String[] { "Empty prover configuration list" };

	// whenever a strategy is added here, verify createValuation method
	// TODO: the order here matters in setCPLAsCurrentLogicOption() (last command)
	private static final String[] CPL_STRATEGY_NAMES = new String[] {
			ConfigurableSimpleStrategy.class.getName(),
			SimpleStrategy.class.getName(),
			MemorySaverStrategy.class.getName(),
			BackjumpingSimpleStrategy.class.getName(),
			LearningSimpleStrategy.class.getName(),
			NewLearningSimpleStrategy.class.getName(),

	};

	private static final String[] MBC_STRATEGY_NAMES = new String[] {
			MBCSimpleStrategy.class.getName(),
			MBCSimpleWithOptionalRulesStrategy.class.getName(),
	// MBCConfigurableSimpleStrategy.class.getName(),
	};

	private static final String[] MCI_STRATEGY_NAMES = new String[] {
			MCISimpleStrategy.class.getName(),
			MCISimpleWithOptionalRulesStrategy.class.getName(),
	// MCIConfigurableSimpleStrategy.class.getName()
	};

	private static final String[] C1_STRATEGY_NAMES = new String[] { C1SimpleStrategy.class
			.getName() };

	private static final String[] IPL_STRATEGY_NAMES = new String[] {
			SimpleStrategy.class.getName(),
	};

	private static final String[] LOGIC_NAMES = new String[] { CPL_LOGIC, IPL_LOGIC };
//			MBC_LOGIC, MCI_LOGIC, C1_LOGIC, IPL_LOGIC };

	private final Map<String, String> strategyMap;

	// selection panel
	private JPanel addSelectionPanel;

	private static final String ADD_CONFIGURATION_TO_LIST = "Add selected prover configuration to list";

	private static final String ADD_ALL = "Add all";

	private static final String ADD_ALL_CONFIGURATIONS_HELP = "Add all possible configurations for current logic with current parser and current options";

	private static final String REMOVE_SELECTED_HELP = "Remove selected prover configurations";

	private static final String REMOVE_SELECTED = "Remove selected configurations";

	private static final String CLEAR_LIST = "Clear list";

	private static final String CLEAR_LIST_HELP = "Clear prover configuration list";

	public ProverConfigurator() {
		super("Prover Configurator");
		strategyMap = new HashMap<String, String>();

		JPanel panel = new JPanel(new GridLayout(0, 1));

		setLocation(getLocation().x + 20, getLocation().y + 200);

		panel.add(createLogicChooserArea());

		panel.add(createStrategyChooserArea());

		panel.add(createNumberOfTimesChooserArea());

		panel.add(createTimeLimitChooserArea());

		panel.add(createParserChooser());

		createRuleStructureChooser();

		panel.add(createComparatorArea());

		panel.add(createOptionPanel());

		setBorder(panel);

		createSelectionListAreaAndEnvolvingPanel(panel);

		setCPLAsCurrentLogicOption();

		this.pack();
	}

	private void createSelectionListAreaAndEnvolvingPanel(JPanel panel) {
		// Selection list panel
		addSelectionPanel = new JPanel(new BorderLayout());

		JButton addSelectionButton, addAllConfigurationsButton, clearSelectionButton, removeSelectedConfigurationsButton;

		addSelectionButton = new JButton(ADD_CONFIGURATION_TO_LIST);
		addSelectionButton.setToolTipText(ADD_CONFIGURATION_TO_LIST);
		addAllConfigurationsButton = new JButton(ADD_ALL);
		addAllConfigurationsButton.setToolTipText(ADD_ALL_CONFIGURATIONS_HELP);
		removeSelectedConfigurationsButton = new JButton(REMOVE_SELECTED);
		removeSelectedConfigurationsButton.setToolTipText(REMOVE_SELECTED_HELP);
		clearSelectionButton = new JButton(CLEAR_LIST);
		clearSelectionButton.setToolTipText(CLEAR_LIST_HELP);
		selectionListData = new LinkedList<ProverConfiguration>();
		selectionList = new JList(EMPTY_LIST);
		selectionList.setVisibleRowCount(5);

		JScrollPane selectionListPane = new JScrollPane(selectionList);

		addSelectionButton.addActionListener(this);
		addAllConfigurationsButton.addActionListener(this);
		removeSelectedConfigurationsButton.addActionListener(this);
		clearSelectionButton.addActionListener(this);

		JPanel addSelectionPanelAux = new JPanel(new GridLayout(0, 4));
		// JPanel addSelectionPanelAux = new JPanel();
		addSelectionPanelAux.add(addSelectionButton);
		addSelectionPanelAux.add(addAllConfigurationsButton);
		addSelectionPanelAux.add(removeSelectedConfigurationsButton);
		addSelectionPanelAux.add(clearSelectionButton);

		// JPanel optionsPanel = new JPanel();
		// optionsPanel.add(addSelectionPanelAux);
		// optionsPanel.add(clearSelectionButton);
		//
		// addSelectionPanel.add(optionsPanel, BorderLayout.NORTH);
		addSelectionPanel.add(addSelectionPanelAux, BorderLayout.NORTH);
		addSelectionPanel.add(selectionListPane, BorderLayout.SOUTH);

		addSelectionPanel.setBorder((BorderFactory.createTitledBorder(
				BorderFactory.createBevelBorder(BevelBorder.RAISED),
				"Prover configuration list for Several Problems Runner")));
		JPanel allPanel = new JPanel(new BorderLayout());
		allPanel.add(panel, BorderLayout.CENTER);
		allPanel.add(addSelectionPanel, BorderLayout.SOUTH);
		setContentPane(allPanel);
		// setContentPane(panel);
	}

	private void setBorder(JPanel panel) {
		// Setting border with name
		panel
				.setBorder(BorderFactory.createTitledBorder(BorderFactory
						.createBevelBorder(BevelBorder.RAISED),
						"Prover configuration"));
	}

	private JPanel createOptionPanel() {
		// Options area
		JPanel optionsPanel = new JPanel(new GridLayout(0, 3));
		saveOriginCheckbox = new JCheckBox("Save formula origins");
		saveOriginCheckbox
				.setToolTipText("Saves origin information for every formula");
		saveOriginCheckbox.setSelected(true);
		saveOriginCheckbox.addActionListener(this);
		optionsPanel.add(saveOriginCheckbox);

		discardBranchesCheckbox = new JCheckBox("Discard closed branches");
		discardBranchesCheckbox
				.setToolTipText("Discards closed branches from memory");
		discardBranchesCheckbox.addActionListener(this);
		optionsPanel.add(discardBranchesCheckbox);

		saveDiscardedBranchesCheckbox = new JCheckBox("Save discarded branches");
		saveDiscardedBranchesCheckbox
				.setToolTipText("Saves discarded closed branches from memory");
		saveDiscardedBranchesCheckbox.addActionListener(this);
		optionsPanel.add(saveDiscardedBranchesCheckbox);

		// backjumpingCheckbox = new JCheckBox("Use backjumping");
		// backjumpingCheckbox
		// .setToolTipText("Use backjumping technique when peforming proof");
		// backjumpingCheckbox.addActionListener(this);
		// optionsPanel.add(backjumpingCheckbox);
		return optionsPanel;
	}

	private JPanel createComparatorArea() {
		// Signed formula comparator area
		JPanel signedFormulaComparatorPanel = new JPanel(new GridLayout(0, 2));
		JLabel signedFormulaComparatorLabel = new JLabel(
				"Signed formula comparator:");
		signedFormulaComparatorCombo = new JComboBox(
				new Comparator[] {
						new InsertionOrderSignedFormulaComparator(),
						new ReverseInsertionOrderSignedFormulaComparator(),
						new ConnectiveSignedFormulaComparator(
								ClassicalConnectives.AND),
						new ConnectiveSignedFormulaComparator(
								ClassicalConnectives.OR),
						new ConnectiveSignedFormulaComparator(
								ClassicalConnectives.IMPLIES),
						new ConnectiveSignedFormulaComparator(
								ClassicalConnectives.BIIMPLIES),
						new ConnectiveSignedFormulaComparator(
								ClassicalConnectives.XOR),
						new SignSignedFormulaComparator(ClassicalSigns.TRUE),
						new SignSignedFormulaComparator(ClassicalSigns.FALSE),
						new ComplexitySignedFormulaComparator(
								ComplexitySignedFormulaComparator.ASCENDING),
						new ComplexitySignedFormulaComparator(
								ComplexitySignedFormulaComparator.DESCENDING),
						new NormalFormulaOrderSignedFormulaComparator(),
						new ReverseFormulaOrderSignedFormulaComparator(),
						new ConsistencyComplexityComparator() });
		signedFormulaComparatorCombo.setEnabled(true);
		// signedFormulaComparatorCombo.addActionListener(this);
		signedFormulaComparatorPanel.add(signedFormulaComparatorLabel);
		signedFormulaComparatorPanel.add(signedFormulaComparatorCombo);
		return (signedFormulaComparatorPanel);
	}

	private void createRuleStructureChooser() {
		rulesStructureNameCombo = new JComboBox(
				new String[] { RuleStructureFactory.CPL_NORMAL_BX,
						RuleStructureFactory.MBC, RuleStructureFactory.MCI,
						RuleStructureFactory.CPL_CONFIGURABLE,
						RuleStructureFactory.C1 });
	}

	private JPanel createParserChooser() {
		// Parser chooser area
		JPanel parsingLibNamePanel = new JPanel(new GridLayout(0, 2));
		JLabel parsingLibNameLabel = new JLabel("Parsing library name:");
		parsingLibNameCombo = new JComboBox(new String[] {});
		// parsingLibNameCombo = new JComboBox(new String[] { SATS5_PARSER,
		// SATCNF_PARSER,
		// SATLFIINCONSDEF_PARSER });
		parsingLibNamePanel.add(parsingLibNameLabel);
		parsingLibNamePanel.add(parsingLibNameCombo);
		return parsingLibNamePanel;
	}

	private JPanel createTimeLimitChooserArea() {
		// "Time limit" chooser area
		JPanel timeLimitPanel = new JPanel(new GridLayout(0, 2));
		JLabel timeLimitLabel = new JLabel(
				"Maximum number of minutes to run each problem:");
		timeLimitCombo = new JComboBox(new String[] { "1", "3", "10", "60",
				"600" });
		timeLimitPanel.add(timeLimitLabel);
		timeLimitPanel.add(timeLimitCombo);
		return (timeLimitPanel);
	}

	private JPanel createNumberOfTimesChooserArea() {
		// "Number of times" chooser area
		JPanel timesPanel = new JPanel(new GridLayout(0, 2));
		JLabel timesLabel = new JLabel("Number of times to run each problem:");
		timesCombo = new JComboBox(new String[] { "1", "2", "3", "4", "5" });
		timesPanel.add(timesLabel);
		timesPanel.add(timesCombo);
		return (timesPanel);
	}

	private JPanel createStrategyChooserArea() {
		// Strategy chooser area
		strategyNamePanel = new JPanel(new GridLayout(0, 2));
		JLabel strategyNameLabel = new JLabel("Strategy class name:");
		strategyNameCombo = new JComboBox();
		strategyNameCombo.addActionListener(this);
		strategyNamePanel.add(strategyNameLabel);
		strategyNamePanel.add(strategyNameCombo);
		return (strategyNamePanel);
	}

	private JPanel createLogicChooserArea() {
		// Logic chooser area
		JPanel logicNamePanel = new JPanel(new GridLayout(0, 2));
		JLabel logicNameLabel = new JLabel("Logic name:");
		logicNameCombo = new JComboBox(LOGIC_NAMES);
		logicNameCombo.addActionListener(this);
		logicNamePanel.add(logicNameLabel);
		logicNamePanel.add(logicNameCombo);
		return logicNamePanel;
	}

	public String getFirstParsingLibName() {
		return parsingLibNameCombo.getSelectedItem().toString();
	}

	public String getRulesStructureName() {
		return rulesStructureNameCombo.getSelectedItem().toString();
	}

	public int getTimes() {
		return Integer.parseInt(timesCombo.getSelectedItem().toString());
	}

	public long getTimeLimit() {
		return (Integer.parseInt(timeLimitCombo.getSelectedItem().toString()) * 60000);
	}

	public String getStrategyName() {
		return strategyNameCombo.getSelectedItem().toString();
	}

	public boolean getDiscardClosedBranches() {
		return discardBranchesCheckbox.isSelected();
	}

	public boolean getSaveOrigin() {
		return saveOriginCheckbox.isSelected();
	}

	public boolean getSaveDiscardedBranches() {
		return saveDiscardedBranchesCheckbox.isSelected();
	}

	// public boolean getUseBackjumping() {
	// return backjumpingCheckbox.isSelected();
	// }

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		manageLogicOptions(e);

		manageStrategyOptions(e);

		manageStrategyChoices(e);

		// manageRuleStructureChoice(e);

		manageSelectionList(e);
	}


	private void manageSelectionList(ActionEvent e) {
		if (e.getActionCommand().equals(ADD_CONFIGURATION_TO_LIST)) {
			ProverConfiguration pc = createProverConfigurationWithCurrentChoices();
			if (!selectionListData.contains(pc)) {
				selectionListData.addLast(pc);
			}

			selectionList.setListData(selectionListData.toArray());
		}
		if (e.getActionCommand().equals(ADD_ALL)) {
			addAllPossibleConfigurations();
		}
		if (e.getActionCommand().equals(REMOVE_SELECTED)) {
			removeSelectedConfigurations();
		}

		if (e.getActionCommand().equals(CLEAR_LIST)) {
			selectionList.setListData(Collections.EMPTY_LIST.toArray());
			selectionListData = new LinkedList<ProverConfiguration>();
		}
	}

	private void addAllPossibleConfigurations() {
		// for all strategy choices
		for (int i = 0; i < strategyNameCombo.getModel().getSize(); i++) {
			ProverConfiguration baseStrategyProverConfiguration = createProverConfigurationWithCurrentChoices();
			baseStrategyProverConfiguration
					.setStrategyName((String) strategyNameCombo.getModel()
							.getElementAt(i));
			baseStrategyProverConfiguration
					.setStrategyFullClassName(strategyMap.get(strategyNameCombo
							.getModel().getElementAt(i)));

			// TODO melhorar relacionamento entre estrat�gia e conjunto de
			// regras
			if (baseStrategyProverConfiguration.getStrategyName().equals(
					ConfigurableSimpleStrategy.class.getSimpleName())) {
				baseStrategyProverConfiguration
						.setRulesStructureName(RuleStructureFactory.CPL_CONFIGURABLE);
			}

			// adds all comparator options for base strategy
			addAllComparatorChoices(baseStrategyProverConfiguration);
		}

		selectionList.setListData(selectionListData.toArray());
	}

	private void addAllComparatorChoices(
			ProverConfiguration baseStrategyProverConfiguration) {
		for (int j = 0; j < signedFormulaComparatorCombo.getModel().getSize(); j++) {
			ProverConfiguration comparatorStrategyPC;
			comparatorStrategyPC = (ProverConfiguration) baseStrategyProverConfiguration
					.clone();

			ISignedFormulaComparator comparator = (ISignedFormulaComparator) signedFormulaComparatorCombo
					.getModel().getElementAt(j);
			comparatorStrategyPC.setSignedFormulaComparator(comparator);

			// for every comparator
			addStrategyOptions(comparatorStrategyPC);
		}
	}

	private void addStrategyOptions(ProverConfiguration proverConfig) {

		proverConfig.setSaveOrigin(getSaveOrigin());
		proverConfig.setDiscardClosedBranches(getDiscardClosedBranches());
		proverConfig.setSaveDiscardedBranches(getSaveDiscardedBranches());

		if ((proverConfig.getStrategyName().equals(
				BackjumpingSimpleStrategy.class.getSimpleName())
				|| proverConfig.getStrategyName().equals(
						LearningSimpleStrategy.class.getSimpleName()) || proverConfig
				.getStrategyName().equals(
						NewLearningSimpleStrategy.class.getSimpleName()))) {
			proverConfig.setSaveOrigin(true);
			proverConfig.setSaveDiscardedBranches(false);
		}

		if (!selectionListData.contains(proverConfig)) {
			selectionListData.addLast(proverConfig);
		}

	}

	private void removeSelectedConfigurations() {
		int[] selectedIndices = selectionList.getSelectedIndices();
		for (int i = 0, j = 0; i < selectedIndices.length; i++) {
			selectionListData.remove(selectedIndices[i] - j);
			j++;
		}
		selectionList.setListData(selectionListData.toArray());
	}

	private void manageStrategyChoices(ActionEvent e) {
		// Performs associated actions when a strategy is chosen
		if (e.getSource() == strategyNameCombo
				|| e.getSource() == logicNameCombo) {

			signedFormulaComparatorCombo.setEnabled(true);

			// Simple and MemorySaver for CPL
			// or Backjumping and Learning for CPL
			if ((strategyNameCombo.getSelectedItem()
					.equals(SimpleStrategy.class.getSimpleName()))
					|| (strategyNameCombo.getSelectedItem()
							.equals(MemorySaverStrategy.class.getSimpleName()))
					|| (strategyNameCombo.getSelectedItem()
							.equals(BackjumpingSimpleStrategy.class
									.getSimpleName()))
					|| (strategyNameCombo.getSelectedItem()
							.equals(LearningSimpleStrategy.class
									.getSimpleName()))
					|| (strategyNameCombo.getSelectedItem()
							.equals(NewLearningSimpleStrategy.class
									.getSimpleName()))

			) {
				rulesStructureNameCombo
						.setSelectedItem(RuleStructureFactory.CPL_NORMAL_BX);
			}

			// Configurable for CPL
			if (strategyNameCombo.getSelectedItem().equals(
					ConfigurableSimpleStrategy.class.getSimpleName())) {
				rulesStructureNameCombo
						.setSelectedItem(RuleStructureFactory.CPL_CONFIGURABLE);
				signedFormulaComparatorCombo.setEnabled(true);
			}

			// MBC Configurable
			if (strategyNameCombo.getSelectedItem().equals(
					MBCConfigurableSimpleStrategy.class.getSimpleName())) {
				signedFormulaComparatorCombo.setEnabled(true);
			}

			// MCI Configurable
			if (strategyNameCombo.getSelectedItem().equals(
					MCIConfigurableSimpleStrategy.class.getSimpleName())) {
				signedFormulaComparatorCombo.setEnabled(true);
			}

		}
	}

	private void manageLogicOptions(ActionEvent e) {
		if (e.getSource() == logicNameCombo) {
			if (logicNameCombo.getSelectedItem().equals(CPL_LOGIC)) {
				setCPLAsCurrentLogicOption();
			} else if (logicNameCombo.getSelectedItem().equals(MBC_LOGIC)) {
				setMBCAsCurrentLogicOption();
			} if (logicNameCombo.getSelectedItem().equals(MCI_LOGIC)) {
				setMCIAsCurrentLogicOption();
			} else if (logicNameCombo.getSelectedItem().equals(C1_LOGIC)) {
				setC1AsCurrentLogicOption();
			} else if (logicNameCombo.getSelectedItem().equals(IPL_LOGIC)) {
				setIPLAsCurrentLogicOption();
			}
		}
	}

	private void setCPLAsCurrentLogicOption() {
		parsingLibNameCombo.setModel(new DefaultComboBoxModel(new String[] {
				SATS5_PARSER, SATCNF_PARSER }));
		parsingLibNameCombo.setSelectedItem(SATS5_PARSER);
		strategyNameCombo.invalidate();
		strategyNameCombo.setModel(new DefaultComboBoxModel(
				getSimpleNames(CPL_STRATEGY_NAMES)));
		rulesStructureNameCombo
				.setSelectedItem(RuleStructureFactory.CPL_CONFIGURABLE);
	}

	private void setIPLAsCurrentLogicOption() {
		parsingLibNameCombo.setModel(new DefaultComboBoxModel(new String[] {
				IPL_PARSER }));
		parsingLibNameCombo.setSelectedItem(IPL_PARSER);
		strategyNameCombo.invalidate();
		strategyNameCombo.setModel(new DefaultComboBoxModel(
				getSimpleNames(IPL_STRATEGY_NAMES)));
		rulesStructureNameCombo
				.setSelectedItem(RuleStructureFactory.IPL);
	}

	private void setMBCAsCurrentLogicOption() {
		parsingLibNameCombo.setModel(new DefaultComboBoxModel(
				new String[] { SATLFIINCONSDEF_PARSER }));
		parsingLibNameCombo.setSelectedItem(SATLFIINCONSDEF_PARSER);
		strategyNameCombo.invalidate();
		strategyNameCombo.setModel(new DefaultComboBoxModel(
				getSimpleNames(MBC_STRATEGY_NAMES)));
		rulesStructureNameCombo.setSelectedItem(RuleStructureFactory.MBC);
	}

	private void setMCIAsCurrentLogicOption() {
		parsingLibNameCombo.setModel(new DefaultComboBoxModel(
				new String[] { SATLFIINCONSDEF_PARSER }));
		parsingLibNameCombo.setSelectedItem(SATLFIINCONSDEF_PARSER);
		strategyNameCombo.invalidate();
		strategyNameCombo.setModel(new DefaultComboBoxModel(
				getSimpleNames(MCI_STRATEGY_NAMES)));
		rulesStructureNameCombo.setSelectedItem(RuleStructureFactory.MCI);
	}

	private void setC1AsCurrentLogicOption() {
		parsingLibNameCombo.setModel(new DefaultComboBoxModel(
				new String[] { SATLFIINCONSDEF_PARSER }));
		parsingLibNameCombo.setSelectedItem(SATLFIINCONSDEF_PARSER);
		strategyNameCombo.invalidate();
		strategyNameCombo.setModel(new DefaultComboBoxModel(
				getSimpleNames(C1_STRATEGY_NAMES)));
		rulesStructureNameCombo.setSelectedItem(RuleStructureFactory.C1);
	}

	private String[] getSimpleNames(String[] strategyNames) {
		String[] newStrategyNames = new String[strategyNames.length];

		for (int i = 0; i < strategyNames.length; i++) {
			newStrategyNames[i] = getSimpleName(strategyNames[i].toString());
			strategyMap.put(newStrategyNames[i], strategyNames[i]);
		}

		return newStrategyNames;
	}

	private String getSimpleName(String className) {
		try {
			return Class.forName(className).getSimpleName();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(this.getClass().getName()
					+ "- Class not found " + e.getMessage());
		}
	}

	private void manageStrategyOptions(ActionEvent e) {

		// TODO: how to better implement this concern?
		// Backjumping and Learning Strategy must save origin

		// if discards branches, cannot save origin
		// if does not discard, cannot save discarded
		if (e.getSource() == discardBranchesCheckbox) {
			if (discardBranchesCheckbox.isSelected()) {
				saveOriginCheckbox.setSelected(false);
				// backjumpingCheckbox.setSelected(false);
			} else {
				saveDiscardedBranchesCheckbox.setSelected(false);
			}
		}
		// if saves origins, cannot discard or save branches, but can use
		// backjumping
		if (e.getSource() == saveOriginCheckbox) {
			if (saveOriginCheckbox.isSelected()) {
				discardBranchesCheckbox.setSelected(false);
				saveDiscardedBranchesCheckbox.setSelected(false);
			} else {
				// backjumpingCheckbox.setSelected(false);
			}
		}
		// if saves discarded, it has to discard and cannot save origins, nor
		// use backjumping
		if (e.getSource() == saveDiscardedBranchesCheckbox
				&& saveDiscardedBranchesCheckbox.isSelected()) {
			discardBranchesCheckbox.setSelected(true);
			saveOriginCheckbox.setSelected(false);
			// backjumpingCheckbox.setSelected(false);
		}

		// if the strategy is backjumping or learning, one cannot use the
		// options. The options are preset.
		if (strategyNameCombo.getSelectedItem().equals(
				BackjumpingSimpleStrategy.class.getSimpleName())
				|| (strategyNameCombo.getSelectedItem()
						.equals(LearningSimpleStrategy.class.getSimpleName()))
				|| (strategyNameCombo.getSelectedItem()
						.equals(NewLearningSimpleStrategy.class.getSimpleName()))

		) {
			saveOriginCheckbox.setSelected(true);
			// discardBranchesCheckbox.setSelected(false);
			saveDiscardedBranchesCheckbox.setSelected(false);

			return;
		}

	}

	public ISignedFormulaComparator getSignedFormulaComparator() {
		if (signedFormulaComparatorCombo.isEnabled()) {
			return (ISignedFormulaComparator) signedFormulaComparatorCombo
					.getSelectedItem();
		} else {
			return null;
		}
	}

	public Vector<ProverConfiguration> createProverConfigurationVector() {
		if (selectionListData.size() == 0) {
			Vector<ProverConfiguration> v = new Vector<ProverConfiguration>();
			v.add(createProverConfigurationWithCurrentChoices());
			return v;
		}
		return new Vector<ProverConfiguration>(selectionListData);
	}

	public ProverConfiguration createProverConfigurationWithCurrentChoices() {

		ProverConfiguration pc = new ProverConfiguration(getStrategyName());
		pc.setStrategyFullClassName(findClassName(getStrategyName()));
		pc.setFirstParsingLibName(getFirstParsingLibName());

		if (getFirstParsingLibName().equals(SATCNF_PARSER)) {
			pc.setTwoPhasesParserOption(false);
		} else {
			pc.setTwoPhasesParserOption(true);
		}

		pc.setRulesStructureName(getRulesStructureName());
		pc.setTimes(getTimes());
		pc.setTimeLimit(getTimeLimit());
		pc.setSaveOrigin(getSaveOrigin());
		pc.setDiscardClosedBranches(getDiscardClosedBranches());
		pc.setSaveDiscardedBranches(getSaveDiscardedBranches());
		pc.setSignedFormulaComparator(getSignedFormulaComparator());
		// pc.setUseBackjumping(getUseBackjumping());
		return pc;
	}

	private String findClassName(String strategyName) {

		return strategyMap.get(strategyName);
	}

	public AbstractValuation createValuation(String strategyName) {
		if (strategyName.equals(MBCSimpleStrategy.class.getSimpleName())
				|| strategyName.equals(MBCSimpleWithOptionalRulesStrategy.class
						.getName())
				|| strategyName.equals(MBCConfigurableSimpleStrategy.class
						.getSimpleName())
				|| strategyName.equals(MCISimpleStrategy.class.getSimpleName())
				|| strategyName.equals(MCISimpleWithOptionalRulesStrategy.class
						.getSimpleName())
				|| strategyName.equals(MCIConfigurableSimpleStrategy.class
						.getSimpleName())
				|| strategyName.equals(C1SimpleStrategy.class.getSimpleName())

		) {
			return new MBCValuation();
		}
		return new CPLValuation();
	}

}
