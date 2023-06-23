Class {
	#name : #Cat,
	#superclass : #CircleMorph,
	#category : #MouseGame
}

{ #category : #'event handling' }
Cat >> handleKeystroke: anEvent [
    | key |
    key := anEvent keyValue.
	Transcript show: 'key pressed'.
	Transcript show: anEvent keyString asLowercase.
	Transcript cr.
    key = 30 ifTrue: [ self moveCat: 0@ -5 ].  "up arrow"
    key = 31 ifTrue: [ self moveCat: 0@ 5 ].   "down arrow"
    key = 29 ifTrue: [ self moveCat: -5@0 ].  "left arrow"
    key = 28 ifTrue: [ self moveCat: 5@0 ].   "right arrow"
]

{ #category : #initialization }
Cat >> initialize [
	"comment stating purpose of message"
	| c |
	
	super initialize.
	
	c := Color r: 0.8 g: 0.8 b: 0.8.
	
	self color: c.
	self borderColor: Color white.
	self extent: 32@32.
	
	self registerForEvents.
	Transcript show: 'Cat initialized'.
	
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
	self height: 32
]

{ #category : #'as yet unclassified' }
Cat >> registerForEvents [
    self on: #keyStroke send: #handleKeystroke: to: self.
]
