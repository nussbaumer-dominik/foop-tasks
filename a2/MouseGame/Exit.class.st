"
An Exit is an exception that quit the image without saving with a given success status.

Exit signalFaillure: 'something went wrong'
"
Class {
	#name : #Exit,
	#superclass : #CircleMorph,
	#category : #MouseGame
}

{ #category : #'instance creation' }
Exit class >> failure [
	^ self status: 1
]

{ #category : #signalling }
Exit class >> signalFailure [
 	^ self failure signal
]

{ #category : #signalling }
Exit class >> signalFailure: aMessage [
 	^ self failure signal: aMessage
]

{ #category : #signalling }
Exit class >> signalSuccess [
 	^self success signal
]

{ #category : #signalling }
Exit class >> signalSuccess: aMessage [
 	^self success signal: aMessage
]

{ #category : #'instance creation' }
Exit class >> status: aPositiveInteger [
	^ self new
		status: aPositiveInteger;
		yourself
]

{ #category : #'instance creation' }
Exit class >> success [
	^ self status: 0
]

{ #category : #'as yet unclassified' }
Exit >> getHeight [
	^self height 
]

{ #category : #'as yet unclassified' }
Exit >> getWidth [
	^self width 
]

{ #category : #initialization }
Exit >> initialize [
super initialize.
self color: Color orange.
]
