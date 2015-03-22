package com.codepath.petbnbcodepath.cards;

import android.content.Context;

import com.codepath.petbnbcodepath.R;
import com.dexafree.materialList.cards.ExtendedCard;

/**
 * Created by gangwal on 3/21/15.
 */
public class PostCard extends ExtendedCard {

        public PostCard(final Context context) {
            super(context);
        }

        @Override
        public int getLayout() {
            return R.layout.item_post;
        }
    }

