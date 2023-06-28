Class {
	#name : #MouseStrategyRandom,
	#superclass : #MouseStrategy,
	#instVars : [
		'chosenSubway',
		'chosenSubwayExit'
	],
	#category : #MouseGame
}

{ #category : #'as yet unclassified' }
MouseStrategyRandom >> chooseExit: allSubways without: mySubway [

	mySubway
		ifNotNil: [
		chosenSubway := (allSubways copyWithout: mySubway) atRandom ]
		ifNil: [ chosenSubway := allSubways atRandom ].
	^ chosenSubway exits atRandom
]

{ #category : #'instance creation' }
MouseStrategyRandom >> newPosition: mouse subways: subways winningSubway: winningSubway [

	| mouseIntersectingExit availableSubwayExits |
	chosenSubwayExit ifNil: [ "no exit chosen -> choose one"
		mouse subway
			ifNotNil: [
			chosenSubwayExit := self chooseExit: mouse subway without: nil ]
			ifNil: [ chosenSubwayExit := self chooseExit: subways without: nil ] ].


	mouse subway
		ifNotNil: [ "mouse is in a subway""mouse subway = winningSubway ifTrue: [""mouse is at the winning subway -> stay""^ mouse position ]."
			mouse subway = chosenSubway ifFalse: [ "mouse is in another subway as the chosen exit is in -> choose an exit thats in the subway of the mouse"
				chosenSubway := mouse subway.
				chosenSubwayExit := chosenSubway exits atRandom ].


			(mouse subway exits anySatisfy: [ :e |
				 mouse bounds intersects: e bounds ]) ifTrue: [
				mouseIntersectingExit := mouse subway exits detect: [ :e |
					                         mouse bounds intersects: e bounds ] ].

			mouseIntersectingExit
				ifNotNil: ["mouse intersects currently with an exit"
					mouseIntersectingExit position = chosenSubwayExit position
						ifTrue: [ "mouse is at the chosen exit -> leave"
							chosenSubway := nil.
							chosenSubwayExit := self
								                    chooseExit: subways
								                    without: mouse subway.
							mouse subway: nil.
							^ mouseIntersectingExit position ]
						ifFalse: [ "mouse is in the subway, not at the chosen exit" ] ]
				ifNil: [ "in subway, not at an exit -> move to the chosen exit"
					^ chosenSubwayExit position ] ]
		ifNil: [
			(mouse bounds intersects: chosenSubwayExit bounds) ifTrue: [ "mouse is at the chosen exit -> enter the subway -> set new target location to an exit of that subway"
				chosenSubway ifNil: [ "why is the chosen subway nil?"
					Transcript
						show: 'The chosen subway is nil ... Why?';
						"chosenSubway := subways detect: [ :s |
						                s exits anySatisfy: [ :e |
							                e position = chosenSubwayExit position ] ]"
							cr ].
				mouse subway: chosenSubway.
				chosenSubway exits ifNil: [ "why is the chosen subway nil?"
					Transcript
						show: 'The chosen subways exits are nil ... Why?';
						cr ].
				availableSubwayExits := chosenSubway exits copyWithout:
					                        chosenSubwayExit.
				chosenSubwayExit := availableSubwayExits atRandom.
				Transcript
					show: 'Mouse reached the exit!';
					cr.
				^ mouse position ] ].




	"mouse is not in a subway -> move directly to the selected exit"
	^ chosenSubwayExit position
]
