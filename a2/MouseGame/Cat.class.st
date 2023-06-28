Class {
	#name : #Cat,
	#superclass : #MovingMorph,
	#instVars : [
		'movement'
	],
	#category : #MouseGame
}

{ #category : #'event handling' }
Cat >> handleKeystroke: anEvent [
	| key |
	key := anEvent keyString asLowercase.
	"Transcript show: 'Key pressed ', anEvent keyString asLowercase; cr."
	key = 'w' ifTrue: [ movement := 0 @ -5 ].  "up arrow"
	key = 's' ifTrue: [ movement := 0 @ 5 ].   "down arrow"
	key = 'a' ifTrue: [ movement := -5 @ 0 ].  "left arrow"
	key = 'd' ifTrue: [ movement := 5 @ 0 ].   "right arrow"

]

{ #category : #initialization }
Cat >> initialize [
	"comment stating purpose of message"
	| c |
	
	super initialize.
	
	movement := 0 @ 0.
	
	c := Color r: 0.8 g: 0.8 b: 0.8.
	
	self color: c.
	self borderColor: Color white.
	self extent: 32@32.
	
	self registerForEvents.
	
	"shootDelay := 0."
]

{ #category : #'as yet unclassified' }
Cat >> moveCat: point [

	| p |
	p := self position.
	self position: point + p.
	self left < owner left ifTrue: [
		self position setX: owner left setY: self position y.
		self width: 32 ].
	self right > owner right ifTrue: [
		self position setX: owner right - 32 setY: self position y.
		self width: 32 ].
	self top < owner top ifTrue: [
		self position setX: self position x setY: owner top.
		self height: 32 ].

	self bottom > owner bottom ifFalse: [ ^ self ].
	self position setX: self position x setY: owner bottom - 32.
	self height: 32.
]

{ #category : #'as yet unclassified' }
Cat >> registerForEvents [
    self on: #keyStroke send: #handleKeystroke: to: self.
]

{ #category : #'stepping and presenter' }
Cat >> step [ 
	self moveCat: movement .
	movement := 0@0
]

{ #category : #'stepping and presenter' }
Cat >> stepTime [

	^ 33
]
