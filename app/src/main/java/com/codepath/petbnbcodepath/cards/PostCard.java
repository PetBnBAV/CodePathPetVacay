package com.codepath.petbnbcodepath.cards;

import android.content.Context;
import android.util.AttributeSet;

import com.codepath.petbnbcodepath.R;
import com.dexafree.materialList.cards.ExtendedCard;

/**
 * Created by gangwal on 3/21/15.
 */
public class PostCard extends ExtendedCard {

        public PostCard(final Context context) {
            super(context);
        }


        public PostCard(final Context context, AttributeSet attrs)
        {
            super(context);
        }

    public PostCard(Context context, AttributeSet attrs, int defStyle) {
        super(context);
    }

    @Override
        public int getLayout() {
            return R.layout.item_post;
        }
    }

