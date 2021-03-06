package com.avcmms.android.internship.irmonkey.view;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.avcmms.android.internship.irmonkey.R;

/**
 * Created by skobayashi1 on 2016/01/30.
 */
public class ButtonPadViewPager extends ViewPager {

    private final PagerAdapter mStaticPagerAdapter = new PagerAdapter() {
        @Override
        public int getCount() {
            return getChildCount();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            return getChildAt(position);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            removeViewAt(position);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public float getPageWidth(int position) {
            return position == 1 ? 7.0f / 9.0f : 1.0f;
        }
    };

    private final PageTransformer mPageTransformer = new PageTransformer() {
        @Override
        public void transformPage(View view, float position) {
            if (position < 0.0f) {
                // Pin the left page to the left side.
                view.setTranslationX(getWidth() * -position);
                view.setAlpha(Math.max(1.0f + position, 0.0f));
            } else {
                // Use the default slide transition when moving to the next page.
                view.setTranslationX(0.0f);
                view.setAlpha(1.0f);
            }
        }
    };

    public ButtonPadViewPager(Context context) {
        this(context, null);
    }

    public ButtonPadViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);

        setAdapter(mStaticPagerAdapter);
        setBackgroundColor(ContextCompat.getColor(context, android.R.color.black));
        addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private void recursivelySetEnabled(View view, boolean enabled) {
                if (view instanceof ViewGroup) {
                    final ViewGroup viewGroup = (ViewGroup) view;
                    for (int childIndex = 0; childIndex < viewGroup.getChildCount(); ++childIndex) {
                        recursivelySetEnabled(viewGroup.getChildAt(childIndex), enabled);
                    }
                } else {
                    view.setEnabled(enabled);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (getAdapter() == mStaticPagerAdapter) {
                    for (int childIndex = 0; childIndex < getChildCount(); ++childIndex) {
                        // Only enable subviews of the current page.
                        recursivelySetEnabled(getChildAt(childIndex), childIndex == position);
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setPageMargin(getResources().getDimensionPixelSize(R.dimen.pad_page_margin));
        setPageTransformer(false, mPageTransformer);


    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // Invalidate the adapter's data set since children may have been added during inflation.
        if (getAdapter() == mStaticPagerAdapter) {
            mStaticPagerAdapter.notifyDataSetChanged();
        }
    }
}
