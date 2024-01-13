package agh.ics.oop.model;

import javafx.geometry.Pos;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;


public class ImageBox extends VBox {
    private final ImageView imageView;
    private ProgressBar progressBar;

    public ImageBox(Image image, int energy, int startEnergy) {
        imageView = new ImageView(image);
        double ratio = getEnergyRatio(startEnergy,energy);
        progressBar = new ProgressBar();
        progressBar.setProgress(ratio);
        progressBar.setStyle("-fx-accent: " + getEnergyBarColor(ratio));
        this.getChildren().addAll(imageView,progressBar);
        this.setAlignment(Pos.CENTER);
    }

    private String getEnergyBarColor(double ratio) {
        if (ratio > .5) return "rgb(" + (int)(Math.max(1 - ratio, 0) * 2 * 255) + ", 255, 0)";
        else return "rgb(255," + (int)(ratio * 2 * 255) + ", 0)";
    }

    public ImageBox(Image image) {
        imageView = new ImageView(image);
        this.getChildren().addAll(imageView);
        this.setAlignment(Pos.CENTER);
    }

    public void setRotation(int i){
        imageView.setRotate(i * 45);
    }

    public void setFit(double cell) {
        imageView.setFitWidth(cell);
        imageView.setFitHeight(cell);
    }
    private double getEnergyRatio(int startEnergy,int energy) {
        return Math.min(1. * energy / startEnergy, 1);
    }
}
