Class {
	#name : #Subway,
	#superclass : #CircleMorph,
	#instVars : [
		'exits'
	],
	#category : #MouseGame
}

{ #category : #adding }
Subway >> addExit: exit [ 
exits add: exit 
]

{ #category : #initialization }
Subway >> initialize [
super initialize.
self color: Color orange.
]

{ #category : #initialization }
Subway >> setNumExits: numExits [
exits := Set new.
]
