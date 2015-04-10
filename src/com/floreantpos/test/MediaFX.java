package com.floreantpos.test;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

public class MediaFX extends Application {

	MediaPlayer mediaplayer;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		Button btn_play, btn_pause, btn_stop;

		btn_play = new Button("Start");
		btn_pause = new Button("Pause");
		btn_stop = new Button("Stop");

		btn_play.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				mediaplayer.play();
			}
		});
		btn_pause.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				mediaplayer.pause();
			}
		});
		btn_stop.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				mediaplayer.stop();
			}
		});

		Media videoFile = new Media(
				"file:///home/mshahriar/Videos/sadat2.mp4");
		
		//Media videoFile = new Media(
		//		"http://download.wavetlan.com/SVV/Media/HTTP/H264/Other_Media/H264_test5_voice_mp4_480x360.mp4");

		mediaplayer = new MediaPlayer(videoFile);
		// mediaplayer.setAutoPlay(true);
		mediaplayer.setVolume(0.1);
		
		MediaView mediaView = new MediaView(mediaplayer);
		mediaView.setFitWidth(1000); mediaView.setFitHeight(800); mediaView.setPreserveRatio(false);


		VBox root = new VBox();
		root.getChildren().addAll(btn_play,btn_pause,btn_stop,mediaView);

		Scene scene = new Scene(root, 1000, 800);
		stage.setScene(scene);

		stage.show();
	}

}
