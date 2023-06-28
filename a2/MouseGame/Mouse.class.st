Class {
	#name : #Mouse,
	#superclass : #MovingMorph,
	#instVars : [
		'subway',
		'moveStrategy',
		'movement',
		'moveSize'
	],
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
	self color: Color white.
	self height: 20.
	moveStrategy := MouseStrategyRandom new.
	moveSize := 3.
	movement := 0 @ 0
]

{ #category : #accessing }
Mouse >> move: subways winningSubway: winningSubway [

	| point |
	point := moveStrategy
		         newPosition: self
		         subways: subways
		         winningSubway: winningSubway.
	"Transcript
		show: 'CurrentPosition ';
		show: self position asString;
		show: ', TargetPosition: ';
		show: point asString."

	"cr"
	self setMovementTowardsPosition: point
]

{ #category : #'as yet unclassified' }
Mouse >> moveMouse: point [

	| p morphSize |
	morphSize := self width.
	p := self position.
	self position: point + p.
	self left < owner left ifTrue: [
		self position setX: owner left setY: self position y.
		self width: morphSize ].
	self right > owner right ifTrue: [
		self position setX: owner right - morphSize setY: self position y.
		self width: morphSize ].
	self top < owner top ifTrue: [
		self position setX: self position x setY: owner top.
		self height: morphSize ].

	self bottom > owner bottom ifFalse: [ ^ self ].
	self position setX: self position x setY: owner bottom - morphSize.
	self height: morphSize
]

{ #category : #'as yet unclassified' }
Mouse >> moveStrategy: aStrategy [

	moveStrategy := aStrategy
]

{ #category : #'as yet unclassified' }
Mouse >> setMovementTowardsPosition: targetPoint [

	| optimalDirections selectedDirection deltaX deltaY |
	optimalDirections := Set new.
	deltaX := targetPoint x - self position x.
	deltaY := targetPoint y - self position y.

	deltaX abs > self width ifTrue: [
		deltaX > 0
			ifTrue: [ "Point is to the right" optimalDirections add: 'right' ]
			ifFalse: [
				deltaX < 0
					ifTrue: [ "Point is to the left" optimalDirections add: 'left' ]
					ifFalse: [ "Point is horizontally aligned" ] ] ].

	deltaY abs > self width ifTrue: [
		deltaY < 0
			ifTrue: [ "Point is up" optimalDirections add: 'up' ]
			ifFalse: [
				deltaY > 0
					ifTrue: [ "Point is down" optimalDirections add: 'down' ]
					ifFalse: [ "Point is vertically aligned" ] ] ].

	optimalDirections
		ifEmpty: [
			optimalDirections
				add: 'up';
				add: 'down';
				add: 'left';
				add: 'right'.
			selectedDirection := optimalDirections atRandom
			"selectedDirection := 'stay'" ]
		ifNotEmpty: [ selectedDirection := optimalDirections atRandom ].

	selectedDirection = 'up' ifTrue: [ movement := 0 @ (moveSize * -1) ].
	selectedDirection = 'down' ifTrue: [ movement := 0 @ moveSize ].
	selectedDirection = 'left' ifTrue: [ movement := moveSize * -1 @ 0 ].
	selectedDirection = 'right' ifTrue: [ movement := moveSize @ 0 ]
]

{ #category : #'stepping and presenter' }
Mouse >> step [

	self moveMouse: movement.
	self updateVisibility.
	movement := 0 @ 0
]

{ #category : #'stepping and presenter' }
Mouse >> stepTime [

	^ 33
]

{ #category : #accessing }
Mouse >> subway [
	^subway 
]

{ #category : #accessing }
Mouse >> subway: aSubway [

	subway := aSubway
]

{ #category : #'private - layout' }
Mouse >> updateVisibility [

	subway ifNil: [ self show ] ifNotNil: [ self hide ]
]
