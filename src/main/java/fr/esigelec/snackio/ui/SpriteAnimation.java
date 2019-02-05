package fr.esigelec.snackio.ui;

import javafx.animation.Interpolator;
import javafx.animation.Transition;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class SpriteAnimation extends Transition{
    private final ImageView imageView;
    private final int count;
    private final int columns;
    private final int offsetX;
    private final int offsetY;
    private final int width;
    private final int height;
    private int lastIndex;

    public SpriteAnimation(
            ImageView imageView,
            Duration duration,
            int count,   int columns,
            int offsetX, int offsetY,
            int width,   int height) {
        this.imageView = imageView;
        this.count     = count;
        this.columns   = columns;
        this.offsetX   = offsetX;
        this.offsetY   = offsetY;
        this.width     = width;
        this.height    = height;
        setCycleDuration(duration);
        setInterpolator(Interpolator.LINEAR);
    }

    protected void interpolate(double k) {
        final int index = Math.min((int) Math.floor(k * count), count - 1);
        if (index != lastIndex && columns==10) {
            final int x = (index % columns) * width  + offsetX;
            imageView.setViewport(new Rectangle2D(x, offsetY, width, height));
            lastIndex = index;
        }
        if (index != lastIndex && columns==5) {
            if (index%columns==1 || index%columns==3) {
                final int y = (index % columns) * width + offsetX;
                imageView.setViewport(new Rectangle2D(offsetX, y, width, height));
                lastIndex = index;
            }
        }
        if(index != lastIndex && columns==9 && index!=0) {
            String url="poi/coin/coin0"+index+".png";
            imageView.setImage(new Image(url));
            lastIndex = index;
        }
    }
}
