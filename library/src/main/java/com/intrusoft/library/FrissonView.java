package com.intrusoft.library;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Region;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import static com.intrusoft.library.Utils.DEFAULT_ALPHA;
import static com.intrusoft.library.Utils.DEFAULT_TINT;
import static com.intrusoft.library.Utils.TAG_IMAGE;
import static com.intrusoft.library.Utils.TIDE_COUNT;
import static com.intrusoft.library.Utils.TIDE_HEIGHT_DP;

/**
 * Displays an arbitrary image with funky cuts.
 * The {@link FrissonView} class can load images from various sources (such as resources or bitmaps),
 * takes care of computing its measurement from the image so that it can be used in any layout manager,
 * and provides various display options such as scaling and tinting.
 *
 * @author Intruder Shanky
 * @since October 2016
 */
public class FrissonView extends View {

    private ScaleType scaleType;
    private float width;
    private float height;
    private float requiredWidth;
    private float requiredHeight;
    private int tintColor;
    private Path path;
    private boolean autoTint;
    private RectF viewBounds, scaleRect;
    private int imageSource;
    private int x;
    private int y;
    private Context context;
    private Paint paint;
    private Bitmap bitmap;
    private int tideCount;
    private int tideHeight;
    private int alphaValue;

    public enum ScaleType {
        CENTRE_CROP(0),
        FIT_XY(1);
        final int value;

        ScaleType(int value) {
            this.value = value;
        }
    }

    private static final ScaleType[] scaleTypeArray = {ScaleType.CENTRE_CROP, ScaleType.FIT_XY};

    public FrissonView(Context context) {
        super(context);
        init(context, null);
    }

    public FrissonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FrissonView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attrs) {
        autoTint = true;
        tideCount = TIDE_COUNT;
        tideHeight = Utils.getPixelForDp(context, TIDE_HEIGHT_DP);
        alphaValue = DEFAULT_ALPHA;
        tintColor = DEFAULT_TINT;
        scaleType = ScaleType.CENTRE_CROP;
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FrissonView);
            if (array.hasValue(R.styleable.FrissonView_src))
                imageSource = array.getResourceId(R.styleable.FrissonView_src, -1);
            if (array.hasValue(R.styleable.FrissonView_autoTint))
                autoTint = array.getBoolean(R.styleable.FrissonView_autoTint, true);
            if (array.hasValue(R.styleable.FrissonView_tintColor)) {
                autoTint = false;
                tintColor = array.getColor(R.styleable.FrissonView_tintColor, DEFAULT_TINT);
            }
            if (array.hasValue(R.styleable.FrissonView_tideCount)) {
                tideCount = array.getInt(R.styleable.FrissonView_tideCount, TIDE_COUNT);
                if (tideCount < 3 || tideCount > 10)
                    tideCount = TIDE_COUNT;
            }
            if (array.hasValue(R.styleable.FrissonView_tideHeight))
                tideHeight = array.getDimensionPixelSize(R.styleable.FrissonView_tideHeight, Utils.getPixelForDp(context, TIDE_HEIGHT_DP));
            if (array.hasValue(R.styleable.FrissonView_alphaValue)) {
                alphaValue = array.getInt(R.styleable.FrissonView_alphaValue, DEFAULT_ALPHA);
                if (alphaValue > 255 || alphaValue < 0)
                    alphaValue = DEFAULT_ALPHA;
            }
            if (array.hasValue(R.styleable.FrissonView_scaleType))
                scaleType = scaleTypeArray[array.getInt(R.styleable.FrissonView_scaleType, 0)];
            array.recycle();
        }
        tideHeight /= 2;
        path = new Path();
        viewBounds = new RectF();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(tintColor);
        scaleRect = new RectF();
        this.context = context;
        if (imageSource != -1)
            try {
                bitmap = BitmapFactory.decodeResource(context.getResources(), imageSource);
                pickColorFromBitmap(bitmap);
            } catch (OutOfMemoryError error) {
                bitmap = null;
                Log.e(TAG_IMAGE, "Image is too large to process. " + error.getMessage());
            } catch (Exception e) {
                Log.e(TAG_IMAGE, e.getMessage());
            }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        Log.d("T", width + "");
        if (bitmap != null && scaleType == ScaleType.CENTRE_CROP) {
            float ratioChange = 1;
            if (width != bitmap.getWidth()) {
                ratioChange = width / bitmap.getWidth();
            }
            if (ratioChange * bitmap.getHeight() < height) {
                ratioChange = height / bitmap.getHeight();
            }
            requiredHeight = bitmap.getHeight() * ratioChange;
            requiredWidth = bitmap.getWidth() * ratioChange;
            y = (int) ((requiredHeight / 2) - (height / 2));
            x = (int) ((requiredWidth / 2) - (width / 2));
            if (x > 0) x = -x;
            if (y > 0) y = -y;
        }
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        int l = tideCount / 2;
        path = Utils.getWavePath(width, height, tideHeight, l * 20, 4);
        viewBounds.set(0, 0, width, height);
        canvas.clipPath(path);
        canvas.drawColor(Color.WHITE);
        paint.setAlpha(255);
        if (bitmap != null) {
            if (scaleType == ScaleType.CENTRE_CROP) {
                scaleRect.set(x, y, x + requiredWidth, y + requiredHeight);
                canvas.clipRect(scaleRect);
                canvas.drawBitmap(bitmap, null, scaleRect, paint);
            } else {
                canvas.drawBitmap(bitmap, null, viewBounds, paint);
            }
        }
        paint.setAlpha(alphaValue);
        canvas.drawPath(path, paint);
        canvas.clipRect(viewBounds, Region.Op.UNION);
        for (int i = 1; i <= tideCount; i++) {
            path = Utils.getWavePath(width, height, tideHeight, i * i * 20, 3);
            canvas.drawPath(path, paint);
        }
    }

    /**
     * @param scaleType scaleType of the image on {@link FrissonView}
     */
    public void setScaleType(@NonNull ScaleType scaleType) {
        this.scaleType = scaleType;
        invalidate();
    }


    /**
     * @param bitmap is object of Scaled Bitmap
     */
    public void setBitmap(@NonNull Bitmap bitmap) {
        this.bitmap = bitmap;
        pickColorFromBitmap(bitmap);
        invalidate();
    }

    /**
     * @param resId is drawable resource Id of image
     */
    public void setImageSource(@DrawableRes int resId) {
        this.imageSource = resId;
        try {
            bitmap = BitmapFactory.decodeResource(context.getResources(), imageSource);
            pickColorFromBitmap(bitmap);
        } catch (OutOfMemoryError error) {
            bitmap = null;
            Log.e(TAG_IMAGE, "Image is too large to process. " + error.getMessage());
        } catch (Exception e) {
            Log.e(TAG_IMAGE, e.getMessage());
        }
        invalidate();
    }

    /**
     * @param color is image tint to provide theme effect. This is optional.
     */
    public void setTintColor(@ColorInt int color) {
        if (color != 0) {
            this.tintColor = color;
            paint.setColor(tintColor);
            setAutoTint(false);
            invalidate();
        }
    }

    /**
     * Pick Color from bitmap
     *
     * @param bitmap
     */
    private void pickColorFromBitmap(Bitmap bitmap) {
        if (bitmap != null)
            Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
                    int defaultColor = 0x000000;
                    if (autoTint) {
                        if (palette.getDarkVibrantColor(defaultColor) != 0) {
                            paint.setColor(Math.abs(palette.getDarkVibrantColor(defaultColor)));
                            Log.d(TAG_IMAGE, "#" + Math.abs(palette.getDarkVibrantColor(defaultColor)));
                        } else if (palette.getDarkMutedColor(defaultColor) != 0) {
                            Log.d(TAG_IMAGE, palette.getMutedColor(defaultColor) + "");
                            paint.setColor(Math.abs(palette.getDarkMutedColor(defaultColor)));
                        } else {
                            paint.setColor(DEFAULT_TINT);
                        }
                    } else
                        paint.setColor(tintColor);
                }
            });
    }

    /**
     * {@link FrissonView} will automatically pick tintColor from image
     *
     * @param autoTint
     */
    public void setAutoTint(boolean autoTint) {
        this.autoTint = autoTint;
        pickColorFromBitmap(bitmap);
        invalidate();
    }
}
