package vmlibrary;

import org.eclipse.emf.common.util.EList;
import org.osate.aadl2.ComponentCategory;
import org.osate.aadl2.instance.ComponentInstance;
import org.osate.aadl2.instance.FeatureCategory;
import org.osate.aadl2.instance.FeatureInstance;
import org.osate.xtext.aadl2.properties.util.GetProperties;

@SuppressWarnings("all")
public class AircraftConsistency {
  
  public boolean consistentElectricalBudget(final ComponentInstance ci, final String fname, final double budget) {
    final EList<FeatureInstance> fil = ci.getFeatureInstances();
    for (final FeatureInstance fi : fil) {
      boolean _equalsIgnoreCase = fi.getName().equalsIgnoreCase(fname);
      if (_equalsIgnoreCase) {
        final double prop = GetProperties.getPowerBudget(fi, 0.0);
        if ((prop == budget)) {
          return true;
        }
      }
    }
    return false;
  }
  
  public boolean consistentElectricalCapacity(final ComponentInstance ci, final double capacity) {
    final double prop = GetProperties.getPowerCapacity(ci, 0.0);
    return prop == capacity;
  }
  
  public boolean consistentWeightLimit(final ComponentInstance ci, final double limit) {
    final double prop = GetProperties.getWeightLimit(ci, 0.0);
    return prop == limit;
  }
  
  public boolean electricalPowerSelfSufficiency(final ComponentInstance ci) {
    final EList<FeatureInstance> fil = ci.getFeatureInstances();
    for (final FeatureInstance fi : fil) {
      return ((GetProperties.getPowerBudget(fi, 0.0) != 0.0) || 
        (GetProperties.getPowerSupply(fi, 0.0) != 0.0));
    }
    return false;
  }
  
  public boolean electricalPowerSelfSufficiency1(final ComponentInstance ci) {
	  for (FeatureInstance fi : ci.getFeatureInstances()) {
		  if (((GetProperties.getPowerBudget(fi, 0.0) != 0.0) || 
			        (GetProperties.getPowerSupply(fi, 0.0) != 0.0))) {
			  return false;
		  }
	  }
    return true;
  }
  
  public boolean CPUSelfSufficiency(final ComponentInstance ci) {
    return (this.hasNoExternalCPUDemand(ci) && this.providesNoCPUExternally(ci));
  }
  
  public boolean hasNoExternalCPUDemand(final ComponentInstance ci) {
    if ((GetProperties.hasAssignedPropertyValue(ci, "SEI::MIPSBudget") && 
      (GetProperties.getPowerBudget(ci, 0.0) != 0.0))) {
      return false;
    }
    return true;
  }
  
  public boolean providesNoCPUExternally(final ComponentInstance ci) {
    if ((GetProperties.hasAssignedPropertyValue(ci, "SEI::MIPSCapacity") && 
      (GetProperties.getPowerSupply(ci, 0.0) != 0.0))) {
      return false;
    }
    return true;
  }
  
  public boolean isRavenscarCompliant(final ComponentInstance ci) {
	  for (ComponentInstance thr : allThreads(ci)) {
		  if (!hasOnlySamplingPorts(thr)) {
			  return false;
		  }
	  }
    return true;
  }
  
  public boolean hasOnlySamplingPorts(final ComponentInstance thread) {
	  for (FeatureInstance fi: thread.getAllFeatureInstances()) {
		  if (fi.getCategory() != FeatureCategory.DATA_PORT) {
			  return false;
		  }
	  }
	  return true;
  }
  
  public EList<ComponentInstance> allThreads(final ComponentInstance ci) {
    return ci.getAllComponentInstances(ComponentCategory.THREAD);
  }
}
