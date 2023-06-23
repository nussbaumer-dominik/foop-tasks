Class {
	#name : #MouseGame,
	#superclass : #PasteUpMorph,
	#instVars : [
		'subways',
		'exits',
		'cat'
	],
	#category : #MouseGame
}

{ #category : #'instance creation' }
MouseGame class >> failure [
	^ self status: 1
]

{ #category : #signalling }
MouseGame class >> signalFailure [
 	^ self failure signal
]

{ #category : #signalling }
MouseGame class >> signalFailure: aMessage [
 	^ self failure signal: aMessage
]

{ #category : #signalling }
MouseGame class >> signalSuccess [
 	^self success signal
]

{ #category : #signalling }
MouseGame class >> signalSuccess: aMessage [
 	^self success signal: aMessage
]

{ #category : #'instance creation' }
MouseGame class >> status: aPositiveInteger [
	^ self new
		status: aPositiveInteger;
		yourself
]

{ #category : #'instance creation' }
MouseGame class >> success [
	^ self status: 0
]

{ #category : #'as yet unclassified' }
MouseGame >> getExits [
^exits
]

{ #category : #'as yet unclassified' }
MouseGame >> getSubways [
^subways
]

{ #category : #'event handling' }
MouseGame >> handleKeystroke: anEvent [
    cat handleKeystroke: anEvent.
]

{ #category : #initialization }
MouseGame >> initialize [

| p |
 "self := Morph new openInWorld."
super initialize.

"score := 0."

self position: 100@100.
self extent: 800@800.
self color: Color black.
self setNameTo: 'Simple Shooter'.

cat := Cat new.
self addMorph: cat.
p := self position.
cat position: 420@400 + p.




"debugtext := DebugText new."
"self addMorph: debugtext."
"debugtext textColor: Color green.
debugtext position: 100@150.
debugtext width: 320.
debugtext height: 150.

debugtext string: score asString fontName: #Atlanta size: 22."
"debugtext extent: 320 @ 150."
"debugtext text: Text fromString: 'hello'."

self restartGame.
]

{ #category : #'as yet unclassified' }
MouseGame >> restartGame [

	| subway pos numExits exit positions x y exitBounds exitColor |
	"delete all subways"
	subways ifNotNil: [
		1 to: subways size do: [ :i |
			subway := subways at: i.
			subway delete ] ].



	pos := self position.

	subways := Array new: 5.
	
	"Array to store the used positions"
	positions := Set new.

	1 to: subways size do: [ :i |
		"Generate random number of exits for that subway"
		numExits := (2 to: 5) atRandom.
		
		subway := Subway new.
		subway setNumExits: numExits .
		
		"Generate a random color for this subway"
		exitColor := Color random.
		
		exits := Array new: numExits.
		1 to: numExits do: [ :j |
			exit := Exit new.
			
			"Generate random position that hasn't been used before"
			[ 
				x := (1 to: self width - exit getWidth) atRandom.
				y := (1 to: self height - exit getHeight) atRandom.
				
				exitBounds := (x @ y extent: exit getWidth @ exit getHeight).
				
				"Check if the position has been used, if it has, we continue generating new positions"
				positions anySatisfy: [ :existingBounds | existingBounds intersects: exitBounds ]
			] whileTrue.
			Transcript show: 'x: ', x printString; cr.
			
			"Add the new position to the set of used positions"
         positions add: exitBounds.
			exit position: x @ y + pos.
			exit color: exitColor.
			self addMorph: exit.
			exits at: j put: exit ].
		
		subways at: i put: subway ].
]
