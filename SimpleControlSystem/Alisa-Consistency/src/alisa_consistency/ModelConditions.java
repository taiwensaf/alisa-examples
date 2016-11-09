package alisa_consistency;

import org.osate.aadl2.ComponentImplementation;
import org.osate.aadl2.ComponentType;
import org.osate.aadl2.NamedElement;

public class ModelConditions {

	public static boolean isLeafComponent(NamedElement ne){
		if (ne instanceof ComponentImplementation){
			ComponentImplementation cimpl = (ComponentImplementation)ne;
			return cimpl.getAllSubcomponents().isEmpty();
		} else if (ne instanceof ComponentType){
			return true;
		}
		return false;
	}
}
