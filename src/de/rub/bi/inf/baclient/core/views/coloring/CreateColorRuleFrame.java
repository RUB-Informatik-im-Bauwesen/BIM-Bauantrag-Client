package de.rub.bi.inf.baclient.core.views.coloring;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifc.IfcProperty;
import com.apstex.ifctoolbox.ifc.IfcPropertySet;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.JFXPanel;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

public class CreateColorRuleFrame extends JDialog {

	public enum OPERATOR {
		GLEICH, ENTHALTEN, KLEINER, GROESSER, NICHT_GLEICH, NICHT_ENTHALTEN
	};

	private class FilterObject {
		private ArrayList<IfcPropertySet> propSet;
		private String prop;
		private OPERATOR operator;
		private String value;

		public FilterObject() {
		}

		public ArrayList<IfcPropertySet> getPropSet() {
			return propSet;
		}

		public String getProp() {
			return prop;
		}

		public OPERATOR getOperator() {
			return operator;
		}

		public String getValue() {
			return value;
		}

		public void setPropSet(ArrayList<IfcPropertySet> propSet) {
			this.propSet = propSet;
		}

		public void setProp(String prop) {
			this.prop = prop;
		}

		public void setOperator(OPERATOR operator) {
			this.operator = operator;
		}

		public void setValue(String value) {
			this.value = value;
		}

	}

	private JFXPanel contentPanel = null;
	private FilterObject filterObject = null;

	private HashMap<String, ArrayList<IfcPropertySet>> propSetMap = null;
	private ColorRule rule = null;

	private String defaultTitel = "NewColorRule";

	private TextField titleTextField = null;
	private ComboBox<String> level1ComboBox = null;
	private ComboBox<String> level2ComboBox = null;
	private ComboBox<OPERATOR> level3ComboBox = null;
	private TextField level3TextField = null;
	private Color ruleColor = Color.BLUE;
	private ColorPicker colorPicker = null;
	private Pane colorPane = new Pane();

	private CreateColorRuleFrame self = null;

	public CreateColorRuleFrame(JFrame owner, String title, String propSet, String prop, OPERATOR operator, Object val,
			Color color) {
		this(owner);

		javafx.application.Platform.runLater(new Runnable() {
			@Override
			public void run() {

				if (title != null) {
					titleTextField.setText(title);
				}

				if (propSet != null) {
					level1ComboBox.setValue(propSet);
					if (prop != null) {
						level2ComboBox.setValue(prop);
						if (operator != null) {
							level3ComboBox.setValue(operator);
						} else {
							level3ComboBox.setValue(OPERATOR.GLEICH);
						}

						if (val != null) {
							level3TextField.setText(val.toString());
						}
					}
				}

				if (color != null) {
					ruleColor = color;

					colorPicker.setValue(color);

					colorPane.setStyle("-fx-background-color: "
									+ String.format("#%02x%02x%02x", (int) (ruleColor.getRed() * 255),
											(int) (ruleColor.getGreen() * 255), (int) (ruleColor.getBlue() * 255))
									+ ";");
				}
			}
		});
	}

	public CreateColorRuleFrame(JFrame owner) {
		super(owner);
		this.setTitle("Create Color Rule");
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setSize(525, 300);
		this.setModal(true);
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		this.self = this;

		this.filterObject = new FilterObject();
		this.rule = new ColorRule();
		this.rule.setTitel(defaultTitel);
		this.rule.setColor(ruleColor);

		
		contentPanel = new JFXPanel();
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		// JScrollPane filterScrollPaneFX = new JScrollPane(jfxFilterPanel);

		javafx.application.Platform.runLater(new Runnable() {

			@Override
			public void run() {
				fillInternalMaps();

				Label lblLevel1 = new Label("Level 1 (By Property Set):");

				level1ComboBox = new ComboBox<String>(new SortedList<>(
						FXCollections.observableArrayList(propSetMap.keySet()), new Comparator<String>() {
							@Override
							public int compare(String o1, String o2) {
								return o1.compareTo(o2);
							}
						}));
				level1ComboBox.setStyle("-fx-pref-width: 310;");

				Label lblLevel2 = new Label("Level 2 (By Property):");
				level2ComboBox = new ComboBox<String>();
				level2ComboBox.setDisable(true);
				level2ComboBox.setStyle("-fx-pref-width: 310;");

				level1ComboBox.valueProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue ov, String oldVal, String newVal) {
						level2ComboBox.getItems().clear();

						ArrayList<String> newItemList = new ArrayList<>();
						for (IfcPropertySet propSet : propSetMap.get(newVal)) {
							for (IfcProperty prop : findPropertyFilter(propSet)) {
								if (!newItemList.contains(prop.getName().getDecodedValue())) {
									newItemList.add(prop.getName().getDecodedValue());
								}
							}
						}

						level2ComboBox.getItems().addAll(new SortedList<>(
								FXCollections.observableArrayList(newItemList), new Comparator<String>() {
									@Override
									public int compare(String o1, String o2) {
										return o1.compareTo(o2);
									}
								}));

						filterObject.setPropSet(propSetMap.get(newVal));
						filterObject.setProp(null);
						filterObject.setValue(null);

						level2ComboBox.setDisable(false);
					}
				});

				Label lblLevel3 = new Label("Level 3 (By Value):");
				level3TextField = new TextField();
				level3TextField.setDisable(true);
				level3ComboBox = new ComboBox<OPERATOR>(FXCollections.observableArrayList(OPERATOR.KLEINER,
						OPERATOR.GROESSER, OPERATOR.GLEICH, OPERATOR.ENTHALTEN, OPERATOR.NICHT_GLEICH, OPERATOR.NICHT_ENTHALTEN));
				level3ComboBox.getSelectionModel().select(OPERATOR.GLEICH);
				level3ComboBox.setDisable(true);
				level3ComboBox.setConverter(new StringConverter<OPERATOR>() {

					@Override
					public String toString(OPERATOR object) {
						return object.name();
					}

					@Override
					public OPERATOR fromString(String string) {
						return level3ComboBox.getItems().stream().filter(ap -> ap.name().equals(string)).findFirst()
								.orElse(null);
					}
				});
				level3ComboBox.setStyle("-fx-pref-width: 115;");

				level2ComboBox.valueProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue ov, String oldVal, String newVal) {
						filterObject.setProp(newVal);
						filterObject.setValue(null);

						level3ComboBox.setDisable(false);
						level3TextField.setDisable(false);
					}
				});

				level3ComboBox.valueProperty().addListener(new ChangeListener<OPERATOR>() {
					@Override
					public void changed(ObservableValue ov, OPERATOR oldVal, OPERATOR newVal) {
						filterObject.setOperator(newVal);
					}
				});
				filterObject.setOperator(OPERATOR.GLEICH); // Default Option

				level3TextField.textProperty().addListener(new ChangeListener<String>() {
					@Override
					public void changed(ObservableValue ov, String oldVal, String newVal) {
						filterObject.setValue(newVal);
					}
				});

				colorPane = new Pane();
				colorPane
						.setStyle(
								"-fx-background-color: "
										+ String.format("#%02x%02x%02x", (int) (ruleColor.getRed() * 255),
												(int) (ruleColor.getGreen() * 255), (int) (ruleColor.getBlue() * 255))
										+ ";");
				colorPane.setPrefSize(25, 10);

				colorPicker = new ColorPicker(ruleColor);
				colorPicker.setOnAction(new EventHandler<javafx.event.ActionEvent>() {
					@Override
					public void handle(javafx.event.ActionEvent event) {
						Color newColor = colorPicker.getValue();

						if (newColor != null) {
							ruleColor = newColor;

							colorPane.setStyle("-fx-background-color: "
									+ String.format("#%02x%02x%02x", (int) (ruleColor.getRed() * 255),
											(int) (ruleColor.getGreen() * 255), (int) (ruleColor.getBlue() * 255))
									+ ";");

						}
					}

				});
				colorPicker.setStyle("-fx-pref-width: 115;");

				Label lblLevel4 = new Label("Rule Title:");

				titleTextField = new TextField(defaultTitel);

				GridPane gridPane = new GridPane();
				gridPane.setHgap(5.0);
				gridPane.setVgap(5.0);

				gridPane.add(lblLevel4, 1, 0);
				gridPane.add(titleTextField, 2, 0, 2, 1);
				gridPane.add(lblLevel1, 1, 1);
				gridPane.add(level1ComboBox, 2, 1, 2, 1);
				gridPane.add(lblLevel2, 1, 2);
				gridPane.add(level2ComboBox, 2, 2, 2, 1);
				gridPane.add(lblLevel3, 1, 3);
				gridPane.add(level3ComboBox, 2, 3);
				gridPane.add(level3TextField, 3, 3);

				gridPane.add(new Label("Color Schema:"), 1, 4);
				gridPane.add(colorPicker, 2, 4, 2, 1);
				gridPane.add(colorPane, 3, 4);

				Scene scene = new Scene(gridPane);

				contentPanel.setScene(scene);
			}
		});
		// jfxFilterPanel.addMouseListener(new
		// ApplicationActions.FilterTreeMouseHandler());

		JScrollPane scrollPane = new JScrollPane(contentPanel);
		this.add(scrollPane, BorderLayout.CENTER);

		JPanel buttonPanel = new JPanel();
		buttonPanel.setBorder(new LineBorder(java.awt.Color.LIGHT_GRAY));
		buttonPanel.setMaximumSize(new Dimension(32767, 60));
		this.add(buttonPanel, BorderLayout.SOUTH);

		JButton btnOk = new JButton("OK");
		btnOk.setPreferredSize(new Dimension(100, 25));
		btnOk.setMinimumSize(new Dimension(100, 25));
		btnOk.setMaximumSize(new Dimension(100, 25));

		btnOk.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rule.setTitel(titleTextField.getText());

				if(!(level1ComboBox.getValue() == null)) {
					if(!level1ComboBox.getValue().isEmpty() || level1ComboBox.getValue().length() > 0) {
						rule.setPropertySetName(level1ComboBox.getValue());					
					}
				}
				

				if(!(level2ComboBox.getValue() == null)) {
					if(!level2ComboBox.getValue().isEmpty() || level2ComboBox.getValue().length() > 0) {
						rule.setPropertyName(level2ComboBox.getValue());					
					}
				}
				
				if(!(level3TextField.getText() == null)) {
					if(!level3TextField.getText().isEmpty() || level3TextField.getText().length() > 0) {					
						rule.setValue(level3TextField.getText());
					}
				}
				
				if(!(level3ComboBox.getValue() == null)) {					
					rule.setOperator(level3ComboBox.getValue().name());
				}

				rule.setColor(ruleColor);
				
				dispose();
			}

		});

		buttonPanel.add(btnOk);

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setPreferredSize(new Dimension(100, 25));
		btnCancel.setMinimumSize(new Dimension(100, 25));
		btnCancel.setMaximumSize(new Dimension(100, 25));

		btnCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				rule = null;
				dispose();
			}

		});

		this.addWindowListener(new WindowAdapter() {
			public void windowClosed(WindowEvent e) {
				// TODO
			}

			public void windowClosing(WindowEvent e) {
				rule = null;
			}
		});

		buttonPanel.add(btnCancel);
	}

	private void fillInternalMaps() {
		propSetMap = new HashMap<>();

		for (ApplicationModelNode node : Kernel.getApplicationModelRoot().getNodes()) {
			Collection<IfcPropertySet> propSets = node.getStepModel().getCollection(IfcPropertySet.class);

			for (IfcPropertySet propSet : propSets) {

				ArrayList arrListPropSets = propSetMap.get(propSet.getName().getDecodedValue());
				if (arrListPropSets == null) {
					arrListPropSets = new ArrayList();
					propSetMap.put(propSet.getName().getDecodedValue(), arrListPropSets);
				}

				arrListPropSets.add(propSet);
			}
		}

		// SortedList<ClassInterface> sortedList = new SortedList<>(arg0);
	}

	private ArrayList<IfcProperty> findPropertyFilter(IfcPropertySet propSet) {
		return new ArrayList<IfcProperty>(propSet.getHasProperties());
	}

	public ColorRule getColorRule() {
		return rule;
	}
}
