"
Please describe the package using the class comment of the included manifest class. The manifest class also includes other additional metadata for the package. These meta data are used by other tools such as the SmalllintManifestChecker and the critics Browser
"
Class {
	#name : #ManifestMouseGame,
	#superclass : #PackageManifest,
	#category : #'MouseGame-Manifest'
}

{ #category : #'code-critics' }
ManifestMouseGame class >> ruleStringConcatenationRuleV1FalsePositive [

	<ignoreForCoverage>
	^ #(#(#(#RGPackageDefinition #(#MouseGame)) #'2023-06-23T20:02:43.762+02:00') )
]

{ #category : #'code-critics' }
ManifestMouseGame class >> ruleToDoCollectRuleV1FalsePositive [

	<ignoreForCoverage>
	^ #(#(#(#RGPackageDefinition #(#MouseGame)) #'2023-06-23T18:26:25.633+02:00') )
]
