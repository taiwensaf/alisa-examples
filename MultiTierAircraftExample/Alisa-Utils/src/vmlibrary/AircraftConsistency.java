package vmlibrary;

import com.google.common.base.Objects;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
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
    final Function1<FeatureInstance, Boolean> _function = (FeatureInstance fi) -> {
      return Boolean.valueOf(((GetProperties.getPowerBudget(fi, 0.0) != 0.0) || 
        (GetProperties.getPowerSupply(fi, 0.0) != 0.0)));
    };
    return IterableExtensions.<FeatureInstance>exists(ci.getFeatureInstances(), _function);
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
    final Function1<ComponentInstance, Boolean> _function = (ComponentInstance thread) -> {
      return Boolean.valueOf(this.hasOnlySamplingPorts(thread));
    };
    IterableExtensions.<ComponentInstance>forall(this.allThreads(ci), _function);
    return true;
  }
  
  public boolean hasOnlySamplingPorts(final ComponentInstance thread) {
    final Function1<FeatureInstance, Boolean> _function = (FeatureInstance fi) -> {
      FeatureCategory _category = fi.getCategory();
      return Boolean.valueOf(Objects.equal(_category, FeatureCategory.DATA_PORT));
    };
    return IterableExtensions.<FeatureInstance>forall(thread.getAllFeatureInstances(), _function);
  }
  
  public EList<ComponentInstance> allThreads(final ComponentInstance ci) {
    return ci.getAllComponentInstances(ComponentCategory.THREAD);
  }
}
