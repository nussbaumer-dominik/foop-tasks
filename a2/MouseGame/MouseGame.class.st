Class {
	#name : #MouseGame,
	#superclass : #Morph,
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

| p cat  |
 "self := Morph new openInWorld."
super initialize.

"score := 0."

self position: 100@100.
self extent: 640@480.
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

"self restartGame."
]

{ #category : #'as yet unclassified' }
MouseGame >> restartGame [

	| subway pos numExits exit |
	"delete all subways"
	subways ifNotNil: [
		1 to: subways size do: [ :i |
			subway := subways at: i.
			subway delete ] ].



	pos := self position.

	subways := Array new: 4.

	1 to: subways size do: [ :i |
		"TODO generate random number of exits for that subway"
		"rand := Random new.
		rand := (rand nextValue) * 10."
		numExits := (1 to: 10) atRandom.
	"	Transcript show: 'numExits: '.
		Transcript show: numExits printString."
		subway := Subway new.
		subway setNumExits: numExits .
		"TODO generate random position"
		subway position: 320 @ 64 + pos.
		
		exits := Array new: numExits.
		1 to: numExits do: [ :j |
			exit := Exit new.
			exits at: j put: exit ].
		subways at: i put: subway ].
]
