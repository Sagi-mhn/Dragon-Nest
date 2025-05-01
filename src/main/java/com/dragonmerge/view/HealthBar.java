package com.dragonmerge.view;

import javafx.geometry.Pos;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class HealthBar extends StackPane {
    private final Rectangle backgroundBar;
    private final Rectangle foregroundBar;
    private final double maxWidth;

    public HealthBar(double width, double height, Color backgroundColor, Color foregroundColor, boolean alignLeft) {
        this.maxWidth = width;

        backgroundBar = createBar(width, height, backgroundColor);

        foregroundBar = createBar(width, height, foregroundColor);

        this.getChildren().addAll(backgroundBar, foregroundBar);

        StackPane.setAlignment(foregroundBar, alignLeft ? Pos.CENTER_LEFT : Pos.CENTER_RIGHT);
    }

    private Rectangle createBar(double width, double height, Color color) {
        Rectangle bar = new Rectangle(width, height);
        bar.setFill(color);
        bar.setArcWidth(10);
        bar.setArcHeight(10);
        return bar;
    }

    public void setHealthPercentage(double percentage) {
        percentage = Math.max(0, Math.min(1, percentage));
        foregroundBar.setWidth(maxWidth * percentage);
    }
}
