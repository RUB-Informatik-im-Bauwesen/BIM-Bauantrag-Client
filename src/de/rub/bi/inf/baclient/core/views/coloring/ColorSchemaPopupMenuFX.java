package de.rub.bi.inf.baclient.core.views.coloring;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;

import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;

import com.apstex.gui.core.kernel.Kernel;
import com.apstex.gui.core.model.applicationmodel.ApplicationModelNode;
import com.apstex.ifctoolbox.ifcmodel.IfcModel;
import com.apstex.ifctoolbox.ifcmodel.IfcModel.IfcSchema;
import com.apstex.step.core.ClassInterface;

import de.rub.bi.inf.baclient.core.actions.XPlanungActionCollection;
import de.rub.bi.inf.baclient.core.geometry.GeometryUtil;
import de.rub.bi.inf.baclient.core.geometry.XPlanungCadObjectJ3D;
import de.rub.bi.inf.baclient.core.ifc.IfcUtil;
import de.rub.bi.inf.baclient.core.model.ChoiceProperty;
import de.rub.bi.inf.baclient.core.model.XPlanungModel;
import de.rub.bi.inf.baclient.core.model.XPlanungModelContainer;
import de.rub.bi.inf.baclient.core.utils.UIUtilities;
import de.rub.bi.inf.baclient.core.views.coloring.CreateColorRuleFrame.OPERATOR;
import de.rub.bi.inf.baclient.core.views.ifc.AddXPlanungToIfcModelView;
import de.rub.bi.inf.baclient.core.views.ifc.IfcExtrusionModelCreator;
import javafx.application.Platform;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTablePosition;
import javafx.scene.control.TreeTableView;
import javafx.util.Pair;
import net.opengis.gml._3.AbstractFeatureType;
import net.opengis.gml._3.AbstractGeometryType;
import net.opengis.gml._3.FeaturePropertyType;
import net.opengis.gml._3.PolygonType;

/**
 * Contains the PopUp-Menu Content if a template is right clicked.
 * 
 * @author Marcel Stepien
 *
 */
public class ColorSchemaPopupMenuFX extends JPopupMenu {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private TreeItem<Object> item = null;
	private JFrame owner = null;

	public ColorSchemaPopupMenuFX(JFrame owner, String title, TreeItem<Object> item) {
		super(title);
		this.owner = owner;
		this.item = item;
	}

	public void inititializeContent() throws IOException {

		// add action bindings
		if (item.getValue() instanceof ColorSchema) {
			this.add(UIUtilities.createMenuItem("Hinzufügen", new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					CreateColorRuleFrame colorRuleFrame = new CreateColorRuleFrame(owner);
					colorRuleFrame.setVisible(true);

					ColorRule rule = colorRuleFrame.getColorRule();
					if (rule != null) {
						((ColorSchema) item.getValue()).addRule(rule);
						item.getChildren().add(new TreeItem<Object>(rule));
					}

				}
			}, this.getClass().getResourceAsStream("icons/play.png")));
		}

		this.add(UIUtilities.createMenuItem("Bearbeiten", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (item.getValue() instanceof ColorSchema) {
					javafx.application.Platform.runLater(new Runnable() {

						@Override
						public void run() {

							TextInputDialog dialog = new TextInputDialog(((ColorSchema) item.getValue()).getTitel());
							dialog.setTitle("Erstelle neues Farbschema");
							dialog.setHeaderText("Bitte Titel des Farbschemas eingeben");
							dialog.setContentText("Titel Eingeben:");
							dialog.setWidth(250);

							// Traditional way to get the response value.
							Optional<String> result = dialog.showAndWait();
							if (result.isPresent()) {
								ColorSchema schema = (ColorSchema) item.getValue();
								schema.setTitel(result.get());
								ColorSchemaManager.getInstance().notifyComboboxes();
								ColorSchemaManager.getInstance().refresh();
							}

						}
					});

				}
				if (item.getValue() instanceof ColorRule) {
					ColorRule oldRule = (ColorRule) item.getValue();

					CreateColorRuleFrame colorRuleFrame = new CreateColorRuleFrame(owner, oldRule.getTitel(),
							oldRule.getPropertySetName(), oldRule.getPropertyName(), OPERATOR.valueOf(oldRule.getOperator()),
							oldRule.getValue(), oldRule.getColor());
					colorRuleFrame.setVisible(true);

					// waiting for input
					ColorRule newRule = colorRuleFrame.getColorRule();
					if (newRule != null) {
						oldRule.setColor(newRule.getColor());
						oldRule.setTitel(newRule.getTitel());
						oldRule.setPropertySetName(newRule.getPropertySetName());
						oldRule.setPropertyName(newRule.getPropertyName());
						oldRule.setOperator(newRule.getOperator());
						oldRule.setValue(newRule.getValue());

						// ColorSchemaManager.getInstance().notifyComboboxes();
						ColorSchemaManager.getInstance().refresh();
					}

				}
			}
		}, this.getClass().getResourceAsStream("icons/play.png")));

		this.add(UIUtilities.createMenuItem("Entfernen", new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (item.getValue() instanceof ColorRule) {
					((ColorSchema)item.getParent().getValue()).getRules().remove(item.getValue());
					item.getParent().getChildren().remove(item);
				}
				if (item.getValue() instanceof ColorSchema) {
					ColorSchemaManager.getInstance().removeColorSchema((ColorSchema)item.getValue());
				}

				ColorSchemaManager.getInstance().refresh();
			}
		}, this.getClass().getResourceAsStream("icons/play.png")));

	}

}
