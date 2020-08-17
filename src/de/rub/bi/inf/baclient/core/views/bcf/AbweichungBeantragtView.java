package de.rub.bi.inf.baclient.core.views.bcf;

import java.awt.AWTKeyStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.KeyboardFocusManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.apstex.bcf.classes.extend.ExtendedData;
import com.apstex.bcf.classes.extend.HiddenObject;
import com.apstex.bcf.classes.extend.StoreyShifting;
import com.apstex.bcf.classes.markup.Comment;
import com.apstex.bcf.classes.markup.File;
import com.apstex.bcf.classes.markup.Header;
import com.apstex.bcf.classes.markup.Markup;
import com.apstex.bcf.classes.markup.Topic;
import com.apstex.bcf.classes.snapshot.Snapshot;
import com.apstex.bcf.classes.visinfo.Component;
import com.apstex.bcf.classes.visinfo.Direction;
import com.apstex.bcf.classes.visinfo.PerspectiveCamera;
import com.apstex.bcf.classes.visinfo.Point;
import com.apstex.bcf.classes.visinfo.Visinfo;
import com.apstex.bcf.core.BcfComment;
import com.apstex.bcf.core.BcfModel;
import com.apstex.bcf.core.BcfZipParser;
import com.apstex.gui.bcf.kernel.BcfKernel;
import com.apstex.gui.bcf.views.commentview.AddCommentDetailDialog;
import com.apstex.gui.bcf.views.commentview.ChooseModelDialog;
import com.apstex.gui.bcf.views.commentview.MiniColorChooser;
import com.apstex.gui.bcf.views.commentview.MiniColorChooserListener;
import com.apstex.gui.bcf.views.commentview.painttool.DrawPanel;
import com.apstex.gui.bcf.views.commentview.painttool.actions.DrawArrowAction;
import com.apstex.gui.bcf.views.commentview.painttool.actions.DrawEllipseAction;
import com.apstex.gui.bcf.views.commentview.painttool.actions.DrawLineAction;
import com.apstex.gui.bcf.views.commentview.painttool.actions.DrawPolylineAction;
import com.apstex.gui.bcf.views.commentview.painttool.actions.DrawRectangleAction;
import com.apstex.gui.bcf.views.commentview.painttool.actions.DrawStringAction;
import com.apstex.gui.bcf.views.commentview.painttool.actions.MoveAction;
import com.apstex.gui.bcf.views.commentview.painttool.actions.SelectModeAction;
import com.apstex.gui.bcf.views.commentview.painttool.model.DrawObject;
import com.apstex.gui.bcf.views.commentview.painttool.model.PModelEvent;
import com.apstex.gui.bcf.views.commentview.painttool.model.PModelListener;
import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.gui.core.model.cadobjectmodel.CadObject;
import com.apstex.gui.ifc.controller.StoreyTranslation;
import com.apstex.gui.ifc.controller.StoreyTranslationController;
import com.apstex.ifctoolbox.ifc.IfcBuildingStorey;
import com.apstex.ifctoolbox.ifc.IfcRoot;
import com.apstex.javax.vecmath.Vector3d;
import com.apstex.step.core.ClassInterface;

import de.xleitstelle.xbau.schema._2._1.AbweichungBeantragt;
import de.xleitstelle.xbau.schema._2._1.CodeAbweichungArt;
import de.xleitstelle.xbau.schema._2._1.Text;

public class AbweichungBeantragtView extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final Insets insets = new Insets(2, 2, 2, 2);
	private static final String initialTitle = "Bitte Begruendung Angeben...";
	private static final String initialAutor = "Bitte Autor Angeben...";
	private static final String initialCommentText = "Bitte Beschreibung Angeben...";
	private static final String initialVorschrift = "Bitte Vorschrift Angeben...";

	protected static final Font buttonFont = new Font("sansserif", Font.PLAIN, 9);
	protected static final Insets buttonInsets = new Insets(0, 0, 0, 0);

	private BufferedImage snapshot;
	private JPanel editorPanel;
	private JTextField titleTextField, vorschriftTextField, authorTextField;
	private JComboBox<String> typeComboBox;
	private JTextArea beschreibungTextArea;
	private FocusListener focusListener;
	private DrawPanel drawPanel;
	// clash comment:
	private ClassInterface clashObject1, clashObject2;

	private String commentId;

	// storey AbweichungBeantragt
	private AbweichungBeantragt antrag = null;

	public AbweichungBeantragtView() {
		this(null, null);
	}

	public AbweichungBeantragtView(ClassInterface clashObject1, ClassInterface clashObject2) {
		super(Kernel.getApplicationController().getMainFrame(), true);
		this.clashObject1 = clashObject1;
		this.clashObject2 = clashObject2;
		this.focusListener = new InternalFocusListener();
		init();
		this.pack();
		this.setResizable(false);
		Kernel.getApplicationController().centerDialogOnMainFrame(this);
		this.setVisible(true);
		this.titleTextField.requestFocus();
	}

	private ImageIcon getIcon(String iconPath, int size) {
		URL imageUrl = Thread.currentThread().getContextClassLoader().getResource(iconPath);
		ImageIcon icon = new ImageIcon(imageUrl);
		return new ImageIcon(icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
	}

	private JToggleButton createDrawToggleButton(String iconPath, String toolTip, boolean isSelected,
			ActionListener listener) {
		JToggleButton button = new JToggleButton(getIcon(iconPath, 20));
		button.setToolTipText(toolTip);
		button.setFont(buttonFont);
		if (listener != null)
			button.addActionListener(listener);
		button.setBorderPainted(false);
		button.setOpaque(false);
		button.setFocusable(true);
		button.setMargin(buttonInsets);
		button.setSelected(isSelected);
		return button;
	}

	private JButton createDrawButton(String iconPath, String toolTip, boolean isSelected, ActionListener listener) {
		JButton button = new JButton(getIcon(iconPath, 20));
		button.setToolTipText(toolTip);
		button.setFont(buttonFont);
		if (listener != null)
			button.addActionListener(listener);
		button.setBorderPainted(false);
		button.setOpaque(false);
		button.setFocusable(true);
		button.setMargin(buttonInsets);
		button.setSelected(isSelected);
		return button;
	}

	private void init() {
		this.setTitle("Erstelle Abweichungsantrag...");
		this.setLayout(new BorderLayout());

		JPanel northPanel = new JPanel();
		northPanel.setLayout(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		snapshot = Kernel.getApplicationController().createSnapshot();
		drawPanel = new DrawPanel(snapshot);
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = 0;
		northPanel.add(drawPanel, gbc);
		northPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

		JToolBar drawButtonsToolbar = new JToolBar(JToolBar.HORIZONTAL);
		drawButtonsToolbar.setFloatable(false);
		drawButtonsToolbar.setLayout(new FlowLayout(FlowLayout.CENTER));
		JToggleButton tb;
		ButtonGroup drawButtonGroup = new ButtonGroup();
		// Select
		final JToggleButton selectButton = createDrawToggleButton("icons/bcf/cursor2.png", "Select", false,
				new SelectModeAction(drawPanel));
		drawButtonGroup.add(selectButton);
		drawButtonsToolbar.add(selectButton);
		// Move Selected
		final JToggleButton moveButton = createDrawToggleButton("icons/bcf/move.png", "Move Selected", false,
				new MoveAction(drawPanel));
		drawButtonGroup.add(moveButton);
		drawButtonsToolbar.add(moveButton);
		moveButton.setEnabled(false);
		// Delete Selected
		final JButton deleteButton = createDrawButton("icons/bcf/delete.png", "Delete Selected", false,
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Enumeration<DrawObject> drawObjects = drawPanel.getPModel().getDrawObjects();
						HashSet<DrawObject> selectedObjects = new HashSet<DrawObject>();
						while (drawObjects.hasMoreElements()) {
							DrawObject drawObject = drawObjects.nextElement();
							if (drawObject.isSelected()) {
								selectedObjects.add(drawObject);
							}
						}
						drawPanel.getPModel().removeDrawObjects(selectedObjects);
					}
				});
		deleteButton.setFocusable(false);
		drawButtonsToolbar.add(deleteButton);
		deleteButton.setEnabled(false);
		drawPanel.getPModel().addModelListener(new PModelListener() {
			@Override
			public void modelActionPerformed(PModelEvent event) {
				switch (event.getEventCode()) {
				case PModelEvent.OBJECT_SELECTED:
				case PModelEvent.OBJECT_DESELECTED:
				case PModelEvent.OBJECTS_SELECTED:
				case PModelEvent.OBJECTS_DESELECTED:
					boolean isEnabled = drawPanel.getPModel().getSelectedDrawObjects().size() > 0;
					moveButton.setEnabled(isEnabled);
					deleteButton.setEnabled(isEnabled);
					break;
				}
			}
		});

		drawButtonsToolbar.addSeparator();
		// Line
		tb = createDrawToggleButton("icons/bcf/line.png", "Line", true, new DrawLineAction(drawPanel));
		drawButtonGroup.add(tb);
		drawButtonsToolbar.add(tb);
		// Polyline
		tb = createDrawToggleButton("icons/bcf/polyline.png", "Polyline", false, new DrawPolylineAction(drawPanel));
		drawButtonGroup.add(tb);
		drawButtonsToolbar.add(tb);
		// Arrow
		tb = createDrawToggleButton("icons/bcf/arrow.png", "Arrow", false, new DrawArrowAction(drawPanel));
		drawButtonGroup.add(tb);
		drawButtonsToolbar.add(tb);
		// Rectangle
		tb = createDrawToggleButton("icons/bcf/rect.png", "Rectangle", false, new DrawRectangleAction(drawPanel));
		drawButtonGroup.add(tb);
		drawButtonsToolbar.add(tb);
		// Ellipse
		tb = createDrawToggleButton("icons/bcf/circ.png", "Ellipse", false, new DrawEllipseAction(drawPanel));
		drawButtonGroup.add(tb);
		drawButtonsToolbar.add(tb);
		// Text
		final DrawStringAction drawStringAction = new DrawStringAction(drawPanel);
		tb = createDrawToggleButton("icons/bcf/text.png", "Text", false, drawStringAction);
		drawButtonGroup.add(tb);
		drawButtonsToolbar.add(tb);
		drawButtonsToolbar.addSeparator();
		// Line Width
		drawButtonsToolbar.add(new JLabel("Line Width: "));
		JSpinner lineWidthSpinner = new JSpinner(new SpinnerNumberModel(drawPanel.getCurrentLineWidth(), 1, 20, 1));
		lineWidthSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner spinner = (JSpinner) e.getSource();
				drawPanel.setCurrentLineWidth((Integer) spinner.getValue());
			}
		});
		drawButtonsToolbar.add(lineWidthSpinner);
		// Text Size
		drawButtonsToolbar.add(new JLabel("Font Size: "));
		JSpinner textSizeSpinner = new JSpinner(new SpinnerNumberModel(drawPanel.getCurrentFontSize(), 1, 100, 1));
		textSizeSpinner.setPreferredSize(lineWidthSpinner.getPreferredSize());
		textSizeSpinner.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JSpinner spinner = (JSpinner) e.getSource();
				drawPanel.setCurrentFontSize((Integer) spinner.getValue());
				drawStringAction.setFontSize((Integer) spinner.getValue());
				drawPanel.repaint();
			}
		});
		drawButtonsToolbar.add(textSizeSpinner);
		drawButtonsToolbar.addSeparator();
		MiniColorChooser miniColorChooser = new MiniColorChooser();
		miniColorChooser.addListener(new MiniColorChooserListener() {

			@Override
			public void colorChanged(Color newColor) {
				drawPanel.setCurrentColor(newColor);
				drawStringAction.setTextColor(newColor);
				drawPanel.repaint();
			}
		});
		drawButtonsToolbar.add(miniColorChooser);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.weightx = 0;
		northPanel.add(drawButtonsToolbar, gbc);

		this.add(northPanel, BorderLayout.NORTH);

		editorPanel = new JPanel();
		editorPanel.setLayout(new GridBagLayout());
		editorPanel.setBorder(BorderFactory.createEmptyBorder(15, 10, 5, 10));

		JLabel label;

		label = new JLabel("Begruendung: ");
		editorPanel.add(label, getGridBagConstraints(0, 0, 1, 1));

		titleTextField = new JTextField(initialTitle);
		Dimension dimCol1 = new Dimension(200, titleTextField.getPreferredSize().height);
		titleTextField.setPreferredSize(dimCol1);
		titleTextField.addFocusListener(focusListener);
		editorPanel.add(titleTextField, getGridBagConstraints(1, 0, 2, 1));

		label = new JLabel("Vorschrift: ");
		editorPanel.add(label, getGridBagConstraints(0, 1, 1, 1));

		vorschriftTextField = new JTextField(initialVorschrift);
		vorschriftTextField.setPreferredSize(dimCol1);
		vorschriftTextField.addFocusListener(focusListener);
		editorPanel.add(vorschriftTextField, getGridBagConstraints(1, 1, 2, 1));

		label = new JLabel("Author: ");
		editorPanel.add(label, getGridBagConstraints(0, 2, 1, 1));

		String authorName = BcfKernel.getAuthorName();
		authorTextField = new JTextField(authorName);
		authorTextField.setPreferredSize(dimCol1);
		authorTextField.addFocusListener(focusListener);
		if (authorName.equals(BcfKernel.INITIAL_AUTHOR_NAME)) {
			authorTextField.setEditable(true);
			authorTextField.setText(initialAutor);
		}
		editorPanel.add(authorTextField, getGridBagConstraints(1, 2, 2, 1));

		label = new JLabel("Art: ");
		editorPanel.add(label, getGridBagConstraints(0, 3, 1, 1));

		JPanel tempPanel;
		String[] items = {
				"Abweichung",
				"Ausnahme",
				"Befreihung"
		};
		
		typeComboBox = new JComboBox<>(items);
		typeComboBox.setSelectedItem(items[0]);
		tempPanel = new JPanel(new BorderLayout());
		tempPanel.add(typeComboBox);
		Dimension radioDim = new Dimension(200, tempPanel.getPreferredSize().height);
		typeComboBox.setSize(radioDim);
		tempPanel.setPreferredSize(radioDim);
		editorPanel.add(tempPanel, getGridBagConstraints(1, 3, 1, 1));

		
		label = new JLabel("Beschreibung: ");
		label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
		editorPanel.add(label, getGridBagConstraints(3, 0, 1, 1));
		beschreibungTextArea = new JTextArea(initialCommentText);
		beschreibungTextArea.setFont(titleTextField.getFont());
		beschreibungTextArea.addFocusListener(focusListener);
		beschreibungTextArea.setLineWrap(true);
		beschreibungTextArea.setWrapStyleWord(true);
		invertFocusTraversalBehaviour(beschreibungTextArea);
		JScrollPane scrollPane = new JScrollPane(beschreibungTextArea);
		scrollPane.setBorder(titleTextField.getBorder());
		Dimension dimCol2 = new Dimension(350, dimCol1.height * 4 + 6);
		scrollPane.setPreferredSize(dimCol2);
		editorPanel.add(scrollPane, getGridBagConstraints(4, 0, 1, 4));

		this.add(editorPanel, BorderLayout.CENTER);

		JPanel southPanel = new JPanel();
		southPanel.setLayout(new BorderLayout());

		JPanel optionsPanel = new JPanel();
		optionsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		// commitCheckBox = new JCheckBox("Local Mode", false);
		// commitCheckBox.setToolTipText("If selected, the comment will not been
		// committed to GTDS.");
		// optionsPanel.add(commitCheckBox);

		// ############ SELECTED OBJECTS ############
		final HashSet<IfcRoot> selectedIfcObjects = new HashSet<IfcRoot>();
		// save only IfcRoot objects in DB selection
		if (clashObject1 != null) {
			// clash comment
			if (clashObject1 instanceof IfcRoot)
				selectedIfcObjects.add((IfcRoot) clashObject1);
			if (clashObject2 instanceof IfcRoot)
				selectedIfcObjects.add((IfcRoot) clashObject2);
		} else {
			// normal comment
			for (ApplicationModelNode node : Kernel.getApplicationModelRoot().getNodes()) {
				for (Object ifcObject : node.getSelectionModel().getSelectedObjects(ClassInterface.class)) {
					if (ifcObject instanceof IfcRoot) {
						selectedIfcObjects.add((IfcRoot) ifcObject);
					}
				}
			}
		}
		JButton selectedObjectsButton = new JButton("Ausgewaehlte Objekte (" + selectedIfcObjects.size() + ") ...");
		selectedObjectsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[][] data = new Object[selectedIfcObjects.size()][3];
				int i = 0;
				for (IfcRoot ifcRoot : selectedIfcObjects) {
					data[i][0] = ifcRoot.getClassName() + "(#" + ifcRoot.getStepLineNumber() + ")";
					data[i][1] = ifcRoot.getName() != null ? ifcRoot.getName().getDecodedValue() : "";
					data[i][2] = ifcRoot.getGlobalId().getDecodedValue();
					i++;
				}
				String[] columnNames = new String[] { "Object", "Name", "GUID" };
				new AddCommentDetailDialog("Ausgewaehlte Objekte ...", data, columnNames);
			}
		});
		optionsPanel.add(selectedObjectsButton);

		// ############ HIDDEN OBJECTS ############
		HashSet<CadObject> hiddenCadObjects = new HashSet<CadObject>();
		for (ApplicationModelNode node : Kernel.getApplicationModelRoot().getNodes()) {
			hiddenCadObjects.addAll(node.getCadObjectModel().getCadObjects());
			hiddenCadObjects.removeAll(node.getVisibilityModel().getVisibleObjects());
		}
		final HashSet<IfcRoot> hiddenIfcObjects = new HashSet<IfcRoot>();
		for (CadObject cadObject : hiddenCadObjects) {
			ClassInterface ifcObject = cadObject.getIfcObject();
			if (ifcObject != null && ifcObject instanceof IfcRoot) {
				hiddenIfcObjects.add((IfcRoot) ifcObject);
			}
		}
		JButton hiddenObjectsButton = new JButton("Versteckte Objekte (" + hiddenIfcObjects.size() + ") ...");
		hiddenObjectsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[][] data = new Object[hiddenIfcObjects.size()][3];
				int i = 0;
				for (IfcRoot ifcRoot : hiddenIfcObjects) {
					data[i][0] = ifcRoot.getClassName() + "(#" + ifcRoot.getStepLineNumber() + ")";
					data[i][1] = ifcRoot.getName() != null ? ifcRoot.getName().getDecodedValue() : "";
					data[i][2] = ifcRoot.getGlobalId().getDecodedValue();
					i++;
				}
				String[] columnNames = new String[] { "Object", "Name", "GUID" };
				new AddCommentDetailDialog("Versteckte Objekte ...", data, columnNames);
			}
		});
		optionsPanel.add(hiddenObjectsButton);

		// ############ STOREY SHIFTING ############
		final HashSet<IfcBuildingStorey> shiftedIfcStoreys = new HashSet<IfcBuildingStorey>();
		StoreyTranslation storeyTranslation = StoreyTranslationController.getInstance().getStoreyTranslation();
		for (IfcBuildingStorey ifcBuildingStorey : storeyTranslation.getSortedStoreys()) {
			if (storeyTranslation.getTranslationInX(ifcBuildingStorey) != 0
					|| storeyTranslation.getTranslationInY(ifcBuildingStorey) != 0
					|| storeyTranslation.getTranslationInZ(ifcBuildingStorey) != 0) {
				shiftedIfcStoreys.add(ifcBuildingStorey);
			}
		}
		JButton storeyShiftingsButton = new JButton("Verschobene Etagen (" + shiftedIfcStoreys.size() + ") ...");
		storeyShiftingsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Object[][] data = new Object[shiftedIfcStoreys.size()][4];
				NumberFormat nf = NumberFormat.getInstance(Locale.ENGLISH);
				nf.setMinimumFractionDigits(3);
				nf.setMaximumFractionDigits(3);
				int i = 0;
				for (IfcBuildingStorey ifcBuildingStorey : shiftedIfcStoreys) {
					StoreyTranslation storeyTranslation = StoreyTranslationController.getInstance()
							.getStoreyTranslation();
					double x = storeyTranslation.getTranslationInX(ifcBuildingStorey);
					double y = storeyTranslation.getTranslationInY(ifcBuildingStorey);
					double z = storeyTranslation.getTranslationInZ(ifcBuildingStorey);
					data[i][0] = ifcBuildingStorey.getClassName() + "(#" + ifcBuildingStorey.getStepLineNumber() + ")";
					data[i][1] = ifcBuildingStorey.getName() != null ? ifcBuildingStorey.getName().getDecodedValue()
							: "";
					data[i][2] = ifcBuildingStorey.getGlobalId().getDecodedValue();
					data[i][3] = "(" + nf.format(x) + ", " + nf.format(y) + ", " + nf.format(z) + ")";
					i++;
				}
				String[] columnNames = new String[] { "Object", "Name", "GUID", "Translation" };
				new AddCommentDetailDialog("Verschobene Etagen ...", data, columnNames);
			}
		});
		optionsPanel.add(storeyShiftingsButton);

		southPanel.add(optionsPanel, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 3, 10));
		JButton button = new JButton("Erstellen");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				checkInputAndAddComment(selectedIfcObjects, hiddenIfcObjects, shiftedIfcStoreys);
			}
		});
		buttonPanel.add(button);
		button = new JButton("Abbrechen");
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AbweichungBeantragtView.this.dispose();
			}
		});
		buttonPanel.add(button);
		southPanel.add(buttonPanel, BorderLayout.EAST);

		this.add(southPanel, BorderLayout.SOUTH);
	}

	private String getStatus() {
		return Comment.STATUS_INFO;
	}

	private List<File> openChooseModelDialog() {
		ChooseModelDialog dialog = new ChooseModelDialog();
		return dialog.getChosenFiles();
	}

	private void checkInputAndAddComment(Collection<IfcRoot> selectedIfcObjects, Collection<IfcRoot> hiddenIfcObjects,
			Collection<IfcBuildingStorey> shiftedIfcStoreys) {
		String title = titleTextField.getText();
		String author = authorTextField.getText();
		String vorschrift = vorschriftTextField.getText();
		String status = getStatus();
		String verbalStatus = Comment.VERBAL_STATUS_OPEN;
		String commentText = ""; //Empty BCF Comment!
		Date currentDate = new Date();
		if (title.length() == 0 || title.equals(initialTitle)) {
			JOptionPane.showMessageDialog(null, "Bitte eine Begruendung angeben!");
			return;
		}
		if (vorschrift.length() == 0 || vorschrift.equals(initialVorschrift)) {
			JOptionPane.showMessageDialog(null, "Bitte eine Vorschrift angeben!");
			return;
		}
		if (author.length() == 0 || author.equals(BcfKernel.INITIAL_AUTHOR_NAME)) {
			JOptionPane.showMessageDialog(null, "Bitte Name des Autor angeben!");
			return;
		}
		/*if (commentText.length() == 0 || commentText.equals(initialCommentText)) {
			JOptionPane.showMessageDialog(null, "Bitte Kommentar angeben!");
			return;
		}*/
		this.setVisible(false);
		List<File> files = openChooseModelDialog();
		if (files.isEmpty()) {
			this.setVisible(true);
			return;
		}
		commentId = BcfModel.getNewUncompressedGuid();
		Header header = new Header(files);
		Topic topic = new Topic(commentId, null, title);
		List<Comment> comments = new ArrayList<Comment>();
		Comment c = new Comment(BcfModel.getNewUncompressedGuid(), verbalStatus, status, currentDate, author,
				commentText);
		comments.add(c);
		Markup markup = new Markup(header, topic, comments);
		Vector3d cameraViewPoint = new Vector3d();
		Vector3d cameraDirection = new Vector3d();
		Vector3d cameraUpVector = new Vector3d();
		Kernel.getApplicationController().getCurrentCameraView(cameraViewPoint, cameraDirection, cameraUpVector);
		double fieldOfView = 45;
		PerspectiveCamera perspectiveCamera = new PerspectiveCamera(toPoint(cameraViewPoint),
				toDirection(cameraDirection), toDirection(cameraUpVector), fieldOfView);
		ArrayList<Component> components = new ArrayList<Component>();
		for (IfcRoot ifcObject : selectedIfcObjects) {
			String ifcGuid = ((IfcRoot) ifcObject).getGlobalId().getDecodedValue();
			components.add(new Component(ifcGuid, null, null));
		}
		Visinfo visinfo = new Visinfo(components, null, perspectiveCamera, null, null);
		BufferedImage img = new BufferedImage(drawPanel.getWidth(), drawPanel.getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = img.createGraphics();
		drawPanel.paint(g);
		Snapshot snapshot = new Snapshot(img);
		snapshot.setImagePreview(BcfZipParser.createPreviewImage(snapshot.getImage()));
		BcfComment newComment = new BcfComment(commentId, markup, visinfo, snapshot);
		// extended data
		Collection<HiddenObject> hiddenObjects = new HashSet<HiddenObject>();
		for (IfcRoot ifcObject : hiddenIfcObjects) {
			hiddenObjects.add(new HiddenObject(ifcObject.getGlobalId().getDecodedValue()));
		}
		Collection<StoreyShifting> shiftedStoreys = new HashSet<StoreyShifting>();
		for (IfcBuildingStorey ifcBuildingStorey : shiftedIfcStoreys) {
			StoreyTranslation storeyTranslation = StoreyTranslationController.getInstance().getStoreyTranslation();
			double x = storeyTranslation.getTranslationInX(ifcBuildingStorey);
			double y = storeyTranslation.getTranslationInY(ifcBuildingStorey);
			double z = storeyTranslation.getTranslationInZ(ifcBuildingStorey);
			Point translation = new Point(x, y, z);
			shiftedStoreys.add(new StoreyShifting(ifcBuildingStorey.getGlobalId().getDecodedValue(), translation));
		}
		ExtendedData extendedData = new ExtendedData(hiddenObjects, shiftedStoreys);
		newComment.setExtendedData(extendedData);
		newComment.setUnsaved(true);
		BcfKernel.getBcfModel().addComment(newComment);

		// create and store AbweichungBeantragt
		// ========================================================================
		antrag = new AbweichungBeantragt();

		CodeAbweichungArt artObj = new CodeAbweichungArt();
		artObj.setCode((String)typeComboBox.getSelectedItem());
		artObj.setName("BCF Abweichung");
		// artObj.setListURI("");
		// artObj.setListVersionID("");
		antrag.setArt(artObj);

		Text vorschriftObj = new Text();
		vorschriftObj.getTextabsatz().add(vorschriftTextField.getText());
		antrag.setVorschrift(vorschriftObj);

		Text begruenungObj = new Text();
		begruenungObj.getTextabsatz().add(titleTextField.getText());
		antrag.setBegruendung(begruenungObj);

		Text beschreibungObj = new Text();
		beschreibungObj.getTextabsatz().add(beschreibungTextArea.getText());
		antrag.setBeschreibung(beschreibungObj);

		antrag.setReferenz(commentId);
		// ========================================================================

		this.dispose();
	}

	private static Point toPoint(Vector3d vector) {
		return new Point(vector.x, vector.y, vector.z);
	}

	private static Direction toDirection(Vector3d vector) {
		return new Direction(vector.x, vector.y, vector.z);
	}

	private static GridBagConstraints getGridBagConstraints(int gridx, int gridy, int gridWidth, int gridHeight) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.gridx = gridx;
		constraints.gridy = gridy;
		constraints.gridwidth = gridWidth;
		constraints.gridheight = gridHeight;
		constraints.anchor = GridBagConstraints.WEST;
		constraints.insets = insets;
		constraints.ipady = 0;
		return constraints;
	}

	public static void invertFocusTraversalBehaviour(JTextArea textArea) {
		Set<AWTKeyStroke> forwardKeys = textArea.getFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS);
		Set<AWTKeyStroke> backwardKeys = textArea.getFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS);

		// check that we WANT to modify current focus traversal keystrokes
		if (forwardKeys.size() != 1 || backwardKeys.size() != 1)
			return;
		final AWTKeyStroke fks = forwardKeys.iterator().next();
		final AWTKeyStroke bks = backwardKeys.iterator().next();
		final int fkm = fks.getModifiers();
		final int bkm = bks.getModifiers();
		final int ctrlMask = KeyEvent.CTRL_MASK + KeyEvent.CTRL_DOWN_MASK;
		final int ctrlShiftMask = KeyEvent.SHIFT_MASK + KeyEvent.SHIFT_DOWN_MASK + ctrlMask;
		if (fks.getKeyCode() != KeyEvent.VK_TAB || (fkm & ctrlMask) == 0 || (fkm & ctrlMask) != fkm) { // not currently
																										// CTRL+TAB for
																										// forward focus
																										// traversal
			return;
		}
		if (bks.getKeyCode() != KeyEvent.VK_TAB || (bkm & ctrlShiftMask) == 0 || (bkm & ctrlShiftMask) != bkm) { // not
																													// currently
																													// CTRL+SHIFT+TAB
																													// for
																													// backward
																													// focus
																													// traversal
			return;
		}

		// bind our new forward focus traversal keys
		Set<AWTKeyStroke> newForwardKeys = new HashSet<AWTKeyStroke>(1);
		newForwardKeys.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, 0));
		textArea.setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
				Collections.unmodifiableSet(newForwardKeys));
		// bind our new backward focus traversal keys
		Set<AWTKeyStroke> newBackwardKeys = new HashSet<AWTKeyStroke>(1);
		newBackwardKeys
				.add(AWTKeyStroke.getAWTKeyStroke(KeyEvent.VK_TAB, KeyEvent.SHIFT_MASK + KeyEvent.SHIFT_DOWN_MASK));
		textArea.setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
				Collections.unmodifiableSet(newBackwardKeys));

		// Now, it's still useful to be able to type TABs in some cases.
		// Using this technique assumes that it's rare however (if the user
		// is expected to want to type TAB often, consider leaving text area's
		// behaviour unchanged...). Let's add some key bindings, inspired
		// from a popular behaviour in instant messaging applications...
		TextInserter.applyTabBinding(textArea);

		// we could do the same stuff for RETURN and CTRL+RETURN for activating
		// the root pane's default button: omitted here for brevity
	}

	public static class TextInserter extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private JTextArea textArea;
		private String insertable;

		private TextInserter(JTextArea textArea, String insertable) {
			this.textArea = textArea;
			this.insertable = insertable;
		}

		public static void applyTabBinding(JTextArea textArea) {
			textArea.getInputMap(JComponent.WHEN_FOCUSED)
					.put(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, KeyEvent.CTRL_MASK + KeyEvent.CTRL_DOWN_MASK), "tab");
			textArea.getActionMap().put("tab", new TextInserter(textArea, "\t"));
		}

		public void actionPerformed(ActionEvent evt) {
			// could be improved to overtype selected range
			textArea.insert(insertable, textArea.getCaretPosition());
		}
	}

	class InternalFocusListener implements FocusListener {
		@Override
		public void focusGained(FocusEvent e) {
			if (e.getSource() instanceof JTextField) {
				JTextField textField = (JTextField) e.getSource();
				textField.selectAll();
			} else if (e.getSource() instanceof JTextArea) {
				JTextArea textArea = (JTextArea) e.getSource();
				textArea.selectAll();
			}
		}

		@Override
		public void focusLost(FocusEvent e) {

		}
	}

	public AbweichungBeantragt toAbweichungBeantragt() {
		return antrag;
	}
}