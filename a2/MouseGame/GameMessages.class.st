Class {
	#name : #GameMessages,
	#superclass : #Object,
	#instVars : [
		'gameOverMessage',
		'countdownMessage'
	],
	#category : #MouseGame
}

{ #category : #accessing }
GameMessages >> countdownMessage: aCountdown [

	countdownMessage
		ifNil: [
			countdownMessage := StringMorph contents:
				                    'Game restarts in ' , aCountdown asString
				                    , ' seconds'.
			countdownMessage color: Color black.
			countdownMessage backgroundColor: Color white.
			countdownMessage borderWidth: 1.
			countdownMessage borderColor: Color black.
			countdownMessage position: gameOverMessage position
				+
				(0 @ (gameOverMessage height + countdownMessage height / 2 + 10)) ]
		ifNotNil: [
			countdownMessage contents:
				'Game restarts in ' , aCountdown asString , ' seconds' ].
			
		^countdownMessage 
]

{ #category : #removing }
GameMessages >> deleteCountdownMessage [

	countdownMessage delete.
	countdownMessage := nil.
]

{ #category : #removing }
GameMessages >> deleteGameOverMessage [

gameOverMessage delete.
	gameOverMessage := nil.
	
]

{ #category : #accessing }
GameMessages >> gameOverMessage: aScore [

	gameOverMessage ifNil: [
		gameOverMessage := StringMorph contents:
			                   'Game over! Your score: ' , aScore asString.
		gameOverMessage color: Color black.
		gameOverMessage backgroundColor: Color white.
		gameOverMessage borderWidth: 1.
		gameOverMessage borderColor: Color black ] ifNotNil: [ 
		gameOverMessage contents: 'Game over! Your score: ' , aScore asString.
		 ]. 

	^ gameOverMessage
]
