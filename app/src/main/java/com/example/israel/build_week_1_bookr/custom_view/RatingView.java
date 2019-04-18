package com.example.israel.build_week_1_bookr.custom_view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.example.israel.build_week_1_bookr.R;

import java.util.ArrayList;

public class RatingView extends LinearLayout {

    public static final int MIN_RATING = 1;

    public RatingView(Context context) {
        super(context);

        init(null);
    }

    public RatingView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(attrs);
    }

    private void init(AttributeSet attrs) {

        // attributes
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.RatingView);
            maxRating = typedArray.getInteger(R.styleable.RatingView_maxRating, MIN_RATING);
            rating = typedArray.getInteger(R.styleable.RatingView_rating, MIN_RATING);
            // clamp
            if (rating < MIN_RATING) {
                rating = MIN_RATING;
            } else if (rating > maxRating) {
                rating = maxRating;
            }

            emptySymbol = typedArray.getResourceId(R.styleable.RatingView_emptySymbol, 0);
            filledSymbol = typedArray.getResourceId(R.styleable.RatingView_filledSymbol, 0);

            typedArray.recycle();
        } else {
            maxRating = MIN_RATING;
            rating = MIN_RATING;
        }

        // create symbols
        for (int i = 0; i < maxRating; ++i) {
            SymbolView symbolView = new SymbolView(getContext(), i);
            int symbolResId = rating > i ? filledSymbol : emptySymbol;
            Drawable symbolDrawable = symbolResId == 0 ? null : getContext().getDrawable(symbolResId);
            symbolView.setImageDrawable(symbolDrawable);
            addView(symbolView);
            symbolViews.add(symbolView);
        }
    }

    private int maxRating;
    private int rating;
    private int emptySymbol;
    private int filledSymbol;
    private ArrayList<SymbolView> symbolViews = new ArrayList<>();

    public int getMaxRating() {
        return maxRating;
    }

    public int getRating() {
        return rating;
    }

    public int getEmptySymbol() {
        return emptySymbol;
    }

    public int getFilledSymbol() {
        return filledSymbol;
    }

    public void setMaxRating(int maxRating) {
        if (maxRating == this.maxRating || maxRating < MIN_RATING) {
            return;
        }

        if (maxRating < this.maxRating) { // decrease symbol
            // remove views from the end
            for (int i = symbolViews.size() - 1; i > maxRating; --i) {
                SymbolView symbolView = symbolViews.get(i);
                removeView(symbolView);
            }
            // resize container down
            symbolViews.subList(maxRating, symbolViews.size()).clear();
        } else { // increase symbol
            // push new symbols
            for (int i = 0; i < maxRating - this.maxRating; ++i) {
                SymbolView symbolView = new SymbolView(getContext(), symbolViews.size());
                Drawable emptySymbolDrawable = emptySymbol == 0 ? null : getContext().getDrawable(emptySymbol);
                symbolView.setImageDrawable(emptySymbolDrawable);
                addView(symbolView);
                symbolViews.add(symbolView);
            }
        }

        this.maxRating = maxRating;
        this.rating = Math.min(this.rating, this.maxRating); // do not exceed new max
    }

    public void setRating(int rating) {
        if (rating == this.rating) {
            return;
        }

        int oldRating = this.rating;
        // clamp
        if (rating < MIN_RATING) {
            this.rating = MIN_RATING;
        } else if (rating > maxRating) {
            this.rating = maxRating;
        } else {
            this.rating = rating;
        }

        if (this.rating < oldRating) { // empty
            for (int i = oldRating - 1; i > this.rating - 1; --i) {
                SymbolView symbolView = symbolViews.get(i);

                Drawable emptySymbolDrawable = emptySymbol == 0 ? null : getContext().getDrawable(emptySymbol);
                symbolView.setImageDrawable(emptySymbolDrawable);
                Drawable drawable = symbolView.getDrawable();
                if (drawable instanceof Animatable) {
                    Animatable animatable = (Animatable)drawable;
                    animatable.start();
                }
            }
        } else { // fill
            for (int i = oldRating; i < this.rating; ++i) {
                SymbolView symbolView = symbolViews.get(i);

                Drawable filledSymbolDrawable = filledSymbol == 0 ? null : getContext().getDrawable(filledSymbol);
                symbolView.setImageDrawable(filledSymbolDrawable);
                Drawable drawable = symbolView.getDrawable();
                if (drawable instanceof Animatable) {
                    Animatable animatable = (Animatable)drawable;
                    animatable.start();
                }
            }
        }
    }

    public void setEmptySymbol(int emptySymbol) {
        if (emptySymbol == this.emptySymbol) {
            return;
        }

        this.emptySymbol = emptySymbol;

        for (int i = rating; i < maxRating; ++i) {
            SymbolView symbolView = symbolViews.get(i);
            symbolView.setImageDrawable(getContext().getDrawable(this.emptySymbol));
        }
    }

    public void setFilledSymbol(int filledSymbol) {
        if (filledSymbol == this.filledSymbol) {
            return;
        }

        this.filledSymbol = filledSymbol;

        for (int i = 0; i < rating; ++i) {
            SymbolView symbolView = symbolViews.get(i);
            symbolView.setImageDrawable(getContext().getDrawable(this.filledSymbol));
        }

    }

    private class SymbolView extends AppCompatImageView {

        public SymbolView(Context context, final int i) {
            super(context);

            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    RatingView.this.setRating(i + 1);
                }
            });
        }

    }
}
