# ALISA Examples

The following examples are available to illustrate the use of incremental system assurance with ALISA.

## SituationalAwarenessSystem
This is the public release version of a situational Awareness System in AADL. It consists of three projects. 

The SituationalAwarenessCommon project contains a set of packages and property sets used by the other two projects.

The SituationalAwarenessSystem project contains the AADL model of the system including a detailed model of pull protocols, and a requirement specification in ReqSpec. The SEI tech Report http://resources.sei.cmu.edu/library/asset-view.cfm?assetid=447184 provides some details about the model. The report http://resources.sei.cmu.edu/library/asset-view.cfm?assetid=447176 summarizes a number of potential integration issues we have identified in the process of creating and analyzing the model. We have also illustrated safety analysis on a primarily software subsystem with this example (see http://resources.sei.cmu.edu/library/asset-view.cfm?assetid=447189).

The SituationalAwarenessRefArch project illustrates how a reference runtime architecture can be defined and elaborated into a specific situation awareness system.

This project does not depend on AlisaPredefined.

## AlisaPredefined
This is a single project that contains a predefined set of categories that can be used on requirements and verification plans.
It also contains a verification method registry for methods that invoke different analysis plug-ins in OSATE.

These definitions are utilized by some projects. In that case you need to check out this project into your workspace. In the future, the definitions will be automatically be included.
