Class {
	#name : #MouseGame,
	#superclass : #PasteUpMorph,
	#instVars : [
		'subways',
		'exits',
		'mice',
		'cat',
		'winningSubway',
		'score'
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
    mice do: [ :mouse |
        "Check if the cat and the mouse are overlapping."
        (cat bounds intersects: mouse bounds) ifTrue: [
            "Collision detected, remove the mouse from the game."
				score := score + 1.
				self removeMouse: mouse.
        ]
    ].
]

{ #category : #'as yet unclassified' }
MouseGame >> checkGameEnd [
	"All mice are either caught or at the winning subway."
	
	| gameEnd |
	
	gameEnd := mice isEmpty or: self allMiceAtWinningSubway .

	gameEnd ifTrue: [ 
		"Display message"
		self gameOver 
		 ]
]

{ #category : #'as yet unclassified' }
MouseGame >> gameOver [
	"End the game and display a winning message."

	| message countdownMessage countdown |
	message := StringMorph contents:
		           'Game over! Your score: ' , score asString.
	message color: Color black.
	message backgroundColor: Color white.
	message borderWidth: 1.
	message borderColor: Color black.

	"Add a margin around the text."
	"message bounds: (message bounds inflateBy: 10@10)."

	"Center the message on the game board."
	message center: self center.

	self addMorph: message.

	countdownMessage := StringMorph contents:
		                    'Game restarts in ' , score asString
		                    , ' seconds'.
	countdownMessage color: Color black.
	countdownMessage backgroundColor: Color white.
	countdownMessage borderWidth: 1.
	countdownMessage borderColor: Color black.
	countdownMessage position: message position + (0 @ (message height + countdownMessage height / 2 + 10)).
	self addMorph: countdownMessage.

	countdown := 10.
	[ countdown > 0 ] whileTrue: [
		(Delay forSeconds: 1) wait.
		countdown := countdown - 1.
		countdownMessage contents:
			'Game restarts in ' , score asString , ' seconds' ].
	self restartGame
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
MouseGame >> placeMice: numMice [

	| mouse exit nonWinningExits |
	
	exits ifNil: [ ^ self ].  "Return if exits is nil."
	
	"Create a collection of non-winning exits."
    nonWinningExits := exits reject: [:eachExit | 
        winningSubway exits includes: eachExit
    ].
	
	mice := Set new: numMice.
		1 to: numMice do: [ :i |
			mouse := Mouse new.
			
			"Get a random exit"
			exit := nonWinningExits atRandom.
			self placeMouse: mouse atExit: exit .
			self addMorph: mouse.
			mice add: mouse.
		].
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

{ #category : #removing }
MouseGame >> removeMouse: aMouse [
    "Remove the mouse from the game."
    mice remove: aMouse ifAbsent: [].
    self removeMorph: aMouse.
]

{ #category : #'as yet unclassified' }
MouseGame >> restartGame [

	| subway pos numSubways numExits exit positions x y exitBounds exitColor |
	"delete all subways"
	subways ifNotNil: [
		1 to: subways size do: [ :i |
			subway := subways at: i.
			subway delete ] ].
	"delete all exits"
	exits ifNotNil: [
		1 to: exits size do: [ :i |
			exit := exits at: i.
			exit delete ] ].

	"reset the score"
	score := 0.

	pos := self position.

	numSubways := 3.
	subways := Set new.
	exits := Set new.
	
	"Array to store the used positions"
	positions := Set new.

	1 to: numSubways do: [ :i |
		"Generate random number of exits for that subway"
		numExits := (2 to: 5) atRandom.
		
		subway := Subway new.
		subway setNumExits: numExits .
		
		"Generate a random color for this subway"
		exitColor := Color random.
		
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
			"Transcript show: 'x: ', x printString; cr."
			
			"Add the new position to the set of used positions"
			positions add: exitBounds.
			exit position: x @ y + pos.
			exit color: exitColor.
			self addMorphBack: exit.
			exits add: exit.
			subway addExit: exit.
			].
		
		subways add: subway.
		].
	
	winningSubway := subways atRandom.
	
	self placeMice: 5.
]

{ #category : #'stepping and presenter' }
MouseGame >> step [
	self checkCollisions .
	self checkGameEnd .
]

{ #category : #'stepping and presenter' }
MouseGame >> stepTime [ 
	^33
]
