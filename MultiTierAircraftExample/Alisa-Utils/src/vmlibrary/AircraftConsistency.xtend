package vmlibrary

import org.osate.aadl2.instance.ComponentInstance
import static extension org.osate.xtext.aadl2.properties.util.GetProperties.*
//import static org.osate.aadl2.instance.util.
import static org.junit.Assert.*;
import static org.hamcrest.core.IsEqual.*
import org.osate.aadl2.instance.FeatureCategory
import org.osate.aadl2.ComponentCategory

class AircraftConsistency {
	def boolean consistentElectricalBudget(ComponentInstance ci, double budget) {
		val fil = ci.featureInstances
		for (fi : fil) {
			val prop = ci.getPowerBudget( 0.0)
			if(prop == budget) return true
		}
		return false
	}

	def boolean consistentElectricalBudget(ComponentInstance ci, String fname, double budget) {
		val fil = ci.featureInstances
		for (fi : fil) {
			if (fi.name.equalsIgnoreCase(fname)) {
				val prop = fi.getPowerBudget( 0.0)
				if(prop == budget) return true
			}
		}
		return false
	}

	def  boolean consistentElectricalCapacity(ComponentInstance ci, double capacity) {
		val prop = ci.getPowerCapacity( 0.0)
//		assertEquals("Required capacity as same property value", capacity, prop,0.1)
		assertThat("Electrical capacity requirement same as property value",prop, equalTo(capacity))
		return true
	}

	def  boolean consistentWeightLimit(ComponentInstance ci, double limit) {
		val prop = ci.getWeightLimit( 0.0)
		assertThat("Weight limit requirement same as WeightLimit property value",prop, equalTo(limit))
		return true
	}

	def boolean electricalPowerSelfSufficiency(ComponentInstance ci) {
		val fil = ci.featureInstances  
		for (fi : fil) {
			return fi.getPowerBudget( 0.0) != 0.0 || 
					fi.getPowerSupply(0.0) != 0.0
		}
	}

	def boolean electricalPowerSelfSufficiency1(ComponentInstance ci) {
		return ci.featureInstances.exists[fi|fi.getPowerBudget( 0.0) != 0.0 || 
					fi.getPowerSupply(0.0) != 0.0]
	}

	def boolean CPUSelfSufficiency(ComponentInstance ci) {
			hasNoExternalCPUDemand(ci) && providesNoCPUExternally(ci)
	}

	def boolean hasNoExternalCPUDemand(ComponentInstance ci) {
			if (ci.hasAssignedPropertyValue("SEI::MIPSBudget") &&
				ci.getPowerBudget( 0.0) != 0.0) {
				return false
			}
		return true
	}

	def boolean providesNoCPUExternally(ComponentInstance ci) {
			if (ci.hasAssignedPropertyValue( "SEI::MIPSCapacity") &&
				ci.getPowerSupply( 0.0) != 0.0) {
				return false
			}
		return true
	}
	
	def boolean isRavenscarCompliant(ComponentInstance ci){
		ci.allThreads.forall[thread| thread.hasOnlySamplingPorts ]
		return true
	}
	
	def boolean hasOnlySamplingPorts(ComponentInstance thread){
		thread.getAllFeatureInstances().forall[fi|fi.category == FeatureCategory.DATA_PORT]
	}
	
	def allThreads(ComponentInstance ci){
		ci.getAllComponentInstances(ComponentCategory.THREAD)
	}
}
	