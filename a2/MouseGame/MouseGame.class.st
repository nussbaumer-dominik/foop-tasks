Class {
	#name : #MouseGame,
	#superclass : #PasteUpMorph,
	#instVars : [
		'subways',
		'exits',
		'mice',
		'cat',
		'winningSubway',
		'score',
		'restartCountdown',
		'gameStepTime',
		'gameMessages'
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
MouseGame >> allMiceAtWinningSubway [

	^ mice allSatisfy: [ :mouse |
		  | mouseAtExit |
		  mouseAtExit := false.
		  winningSubway exits do: [ :exit |
			  (mouse bounds intersects: exit bounds) ifTrue: [
				  mouseAtExit := true 
				].
			].
		  mouseAtExit ]
]

{ #category : #'as yet unclassified' }
MouseGame >> checkCollisions [
	"Check each mouse for collision with the cat."

	mice do: [ :mouse | "Check if the cat and the mouse are overlapping."
		mouse subway ifNil: [ 
			(cat bounds intersects: mouse bounds) ifTrue: [ "Collision detected, remove the mouse from the game."
			score := score + 1.
			self removeMouse: mouse ] ]
			 ]
		
]

{ #category : #'as yet unclassified' }
MouseGame >> checkGameEnd [
	"All mice are either caught or at the winning subway."

	| gameEnd |
	gameEnd := mice isEmpty or: [ self allMiceAtWinningSubway ].


	"check if a gameEnd state is reached and the restartCounter not already started"
	(gameEnd and: [ restartCountdown isNil ]) ifTrue: [ self gameOverMessage ]
]

{ #category : #'as yet unclassified' }
MouseGame >> gameOverMessage [
	"End the game and display a winning message."

	| message countdownMessage |
	message := gameMessages gameOverMessage: score.

	"Add a margin around the text."
	"message bounds: (message bounds inflateBy: 10@10)."

	"Center the message on the game board."
	message center: self center.

	self addMorph: message.

	restartCountdown := 10.
	countdownMessage := gameMessages countdownMessage: restartCountdown.
	self addMorph: countdownMessage.

	"set the stepTime to 1 second for counter display"
	gameStepTime := 1000
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

| key |
    key := anEvent keyString asLowercase.
    key = 'c' ifTrue: [ self toggleColorOfAllExits ].  "change the color of all exits"

	cat handleKeystroke: anEvent
]

{ #category : #initialization }
MouseGame >> initialize [

	| p form |
	"self := Morph new openInWorld."
	super initialize.

	score := 0.

	self position: 100 @ 100.
	self extent: 800 @ 800.
	self color: Color black.
	self setNameTo: 'Mouse Game'.

	cat := Cat new.
	form := self loadResource: 'images/cat-head.png'.
	cat form: form.
	self addMorph: cat.
	p := self position.
	cat position: 420 @ 400 + p.
	gameMessages := GameMessages new.
	gameStepTime := 33.

	self restartGame
]

{ #category : #'as yet unclassified' }
MouseGame >> loadResource: aString [
	"loads image from resources directory of foop-tasks repository"

	| repository path form |
	repository := IceRepository registry detect: [ :repo |
		              repo name = 'foop-tasks' ].

	path := repository location / 'a2/resources' / aString.
	"Transcript show: path asString."
	form := ImageReadWriter formFromFileNamed: path.
	^ form
]

{ #category : #'as yet unclassified' }
MouseGame >> moveMice [

	mice ifNotNil: [
		subways ifNotEmpty: [
			winningSubway ifNotNil: [ 
				mice do: [ :eachMouse |
				eachMouse move: subways winningSubway: winningSubway ] ] ]
				 ]
			
]

{ #category : #'as yet unclassified' }
MouseGame >> placeMice: numMice [

	| mouse exit nonWinningExits form |
	exits ifNil: [ ^ self ]. "Return if exits is nil."

	"Create a collection of non-winning exits."
	nonWinningExits := exits reject: [ :eachExit |
		                   winningSubway exits includes: eachExit ].

	mice := Set new: numMice.
	form := self loadResource: 'images/mouse-white.png'.
	1 to: numMice do: [ :i |
		mouse := Mouse new.
		mouse moveStrategy: MouseStrategyRandom new.
		mouse form: form.

		"Get a random exit"
		exit := nonWinningExits atRandom.
		self placeMouse: mouse atExit: exit.
		self addMorph: mouse.
		mice add: mouse ]
]

{ #category : #'as yet unclassified' }
MouseGame >> placeMouse: aMouse atExit: anExit [
    "Place aMouse at the center of anExit."

    | exitCenter mouseWidth mouseHeight newPosition |
    exitCenter := anExit center.
    mouseWidth := aMouse width.
    mouseHeight := aMouse height.
    newPosition := (exitCenter x - (mouseWidth / 2)) @ (exitCenter y - (mouseHeight / 2)).
    aMouse position: newPosition.
]

{ #category : #'as yet unclassified' }
MouseGame >> placeSubways: numSubways [

	| pos positions numExits subway exitColor exit x y exitBounds |
	
	subways ifNil: [ subways := Set new. ].
	exits ifNil: [ exits := Set new. ].
	
	"the own position"
	pos := self position.

	"Array to store the used positions"
	positions := Set new.

	1 to: numSubways do: [ :i | "Generate random number of exits for that subway"
		numExits := (2 to: 5) atRandom.

		subway := Subway new.
		subway setNumExits: numExits.

		"Generate a random color for this subway"
		exitColor := Color random.

		1 to: numExits do: [ :j |
			exit := Exit new.

			"Generate random position that hasn't been used before"
			[
			x := (1 to: self width - exit getWidth) atRandom.
			y := (1 to: self height - exit getHeight) atRandom.

			exitBounds := x @ y extent: exit getWidth @ exit getHeight.

			"Check if the position has been used, if it has, we continue generating new positions"
			positions anySatisfy: [ :existingBounds |
				existingBounds intersects: exitBounds ] ] whileTrue.
			"Transcript show: 'x: ', x printString; cr."

			"Add the new position to the set of used positions"
			positions add: exitBounds.
			exit position: x @ y + pos.
			exit myColor: exitColor.
			self addMorphBack: exit.
			exits add: exit.
			subway addExit: exit ].

		subways add: subway ]
]

{ #category : #removing }
MouseGame >> removeMouse: aMouse [
    "Remove the mouse from the game."
    mice remove: aMouse ifAbsent: [].
    self removeMorph: aMouse.
]

{ #category : #accessing }
MouseGame >> restartCountdownIteration [

	restartCountdown > 0
		ifTrue: [
			gameMessages countdownMessage: restartCountdown.
			restartCountdown := restartCountdown - 1 ]
		ifFalse: [
			gameMessages deleteGameOverMessage.
			gameMessages deleteCountdownMessage.
			gameStepTime := 33.
			self restartGame ]
]

{ #category : #'as yet unclassified' }
MouseGame >> restartGame [

	| pos numSubways |
	"delete all subways"
	subways ifNotNil: [ subways do: [ :eachSubway | eachSubway delete ].
		subways := Set new.
		 ].
	"delete all exits"
	exits ifNotNil: [ exits do: [ :eachExit | eachExit delete ].
		exits := Set new. ].
	"delete all mice"
	mice ifNotNil: [
		mice do: [ :eachMouse | eachMouse delete ].
		mice := Set new ].

	"reset the score"
	score := 0.

	"reset the counter"
	restartCountdown := nil.

	pos := self position.

	numSubways := 3.

	self placeSubways: numSubways.


	winningSubway := subways atRandom.

	self placeMice: 10
]

{ #category : #'stepping and presenter' }
MouseGame >> step [

	self checkCollisions.
	self checkGameEnd.
	restartCountdown
		ifNotNil: [ self restartCountdownIteration ]
		ifNil: [ self moveMice ]
]

{ #category : #'stepping and presenter' }
MouseGame >> stepTime [
	gameStepTime ifNil: [ 
		^33 ] ifNotNil: [ 
		^ gameStepTime 
		 ]
]

{ #category : #'as yet unclassified' }
MouseGame >> toggleColorOfAllExits [

	exits do: [ :eachExit | eachExit toggleColor ]
]
