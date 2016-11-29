package alisa_consistency;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.Classifier;
import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.NamedElement;
import org.osate.aadl2.Property;
import org.osate.aadl2.UnitLiteral;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.FeatureCategory;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.aadl2.instance.InstanceObject;
import org.osate.results.results.ResultReport;
import org.osate.alisa.common.common.ResultIssue;
import org.osate.results.util.ResultsUtilExtension;
import org.osate.xtext.aadl2.properties.util.GetProperties;
import org.osate.xtext.aadl2.properties.util.PropertyUtils;

public class ModelVerifications {

	public static boolean isLeafComponent(ComponentInstance ci) {
		return ci.getComponentInstances().isEmpty();
	}

	public static boolean isConnected(ComponentInstance ci) {
		for (FeatureInstance fi : ci.getAllFeatureInstances()) {
			if (fi.getSrcConnectionInstances().isEmpty() && fi.getDstConnectionInstances().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	public static boolean isConnected(FeatureInstance fi) {
		return !(fi.getSrcConnectionInstances().isEmpty() && fi.getDstConnectionInstances().isEmpty());
	}

	/**
	 * Recursively consistency check that all leaf components have all features
	 * connected. Get report back on details of which ones do not.
	 */ // EList<ResultIssue>
	public static ResultReport allComponentFeaturesConnected(ComponentInstance ci) {
		ResultReport report = ResultsUtilExtension.createReport("AllFeaturesConnected", ci);
		for (ComponentInstance subi : ci.getAllComponentInstances()) {
			if (isLeafComponent(subi)) {
				for (FeatureInstance fi : subi.getAllFeatureInstances()) {
					if (!isConnected(fi)) {
						ResultsUtilExtension.addFail(report.getIssues(),
								"Feature " + fi.getName() + " of component "
										+ fi.getContainingComponentInstance().getName() + " not connected",
								fi, "AllFeatureConnected");
					}
				}
			}
		}
		return report;
	}

	public static boolean twoPowerInlets(ComponentInstance ci) {
		int count = 0;
		for (FeatureInstance fi : ci.getAllFeatureInstances(FeatureCategory.ABSTRACT_FEATURE)) {
			Classifier cl = fi.getFeature().getAllClassifier();
			if (cl.getName().equalsIgnoreCase("power")) {
				count++;
			}
		}
		return count == 2;
	}

	public static boolean hasVoltage(FeatureInstance fi, double v) {
			double volt = getVoltage(fi);
			return volt == v;
	}

	public static boolean hasWattageBudget(ComponentInstance ci, String featurename, double w) {
		for (FeatureInstance fi : ci.getAllFeatureInstances(FeatureCategory.ABSTRACT_FEATURE)) {
			if (fi.getName().equalsIgnoreCase(featurename)) {
				double watt = GetProperties.getPowerBudget(fi, 0.0);
				return watt == w;
			}
		}
		return false;
	}

	public static boolean sameVoltage(ComponentInstance ci) {
		EList<FeatureInstance> inlets = new BasicEList<FeatureInstance>();
		for (FeatureInstance fi : ci.getAllFeatureInstances(FeatureCategory.ABSTRACT_FEATURE)) {
			Classifier cl = fi.getFeature().getAllClassifier();
			if (cl.getName().equalsIgnoreCase("power")) {
				inlets.add(fi);
			}
		}
		if (inlets.size() == 2) {
			double v1 = getVoltage(inlets.get(0));
			double v2 = getVoltage(inlets.get(1));
			return v1 == v2;
		}
		return false;
	}

	public static boolean samePowerBudget(ComponentInstance ci) {
		EList<FeatureInstance> inlets = new BasicEList<FeatureInstance>();
		for (FeatureInstance fi : ci.getAllFeatureInstances(FeatureCategory.ABSTRACT_FEATURE)) {
			Classifier cl = fi.getFeature().getAllClassifier();
			if (cl.getName().equalsIgnoreCase("power")) {
				inlets.add(fi);
			}
		}
		if (inlets.size() == 2) {
			double pb1 = GetProperties.getPowerBudget(inlets.get(0), 0.0);
			double pb2 = GetProperties.getPowerBudget(inlets.get(1), 0.0);
			return pb1 == pb2;
		}
		return false;
	}

	public static double getVoltage(final FeatureInstance fi) {
		Property voltage = GetProperties.lookupPropertyDefinition(fi, "Physical", "Voltage");
		UnitLiteral volts = GetProperties.findUnitLiteral(voltage, "V");
		return PropertyUtils.getScaledNumberValue(fi, voltage, volts, 0.0);
	}

}
