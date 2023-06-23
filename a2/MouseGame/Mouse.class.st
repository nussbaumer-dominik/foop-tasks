Class {
	#name : #Mouse,
	#superclass : #CircleMorph,
	#category : #MouseGame
}

{ #category : #'instance creation' }
Mouse class >> failure [
	^ self status: 1
]

{ #category : #signalling }
Mouse class >> signalFailure [
 	^ self failure signal
]

{ #category : #signalling }
Mouse class >> signalFailure: aMessage [
 	^ self failure signal: aMessage
]

{ #category : #signalling }
Mouse class >> signalSuccess [
 	^self success signal
]

{ #category : #signalling }
Mouse class >> signalSuccess: aMessage [
 	^self success signal: aMessage
]

{ #category : #'instance creation' }
Mouse class >> status: aPositiveInteger [
	^ self new
		status: aPositiveInteger;
		yourself
]

{ #category : #'instance creation' }
Mouse class >> success [
	^ self status: 0
]

{ #category : #initialization }
Mouse >> initialize [
super initialize.
self color: Color white .
self height: 20
]

{ #category : #'stepping and presenter' }
Mouse >> step [ 
	"TODO do smthg"
]
