package de.tudarmstadt.informatik.secuso.phishedu;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

public abstract class SwipeActivity extends FragmentActivity implements
		ViewPager.OnPageChangeListener{
	
	protected SwipePageAdapter mAdapter;
	protected ViewPager mPager;
	protected ImageView imgNext;
	protected ImageView imgPrevious;
	protected Button bStartLevel;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		setContentView(R.layout.fragment_pager);
		// set the new Content of your activity
		imgPrevious = (ImageView) findViewById(R.id.game_intro_arrow_back);
		imgPrevious.setVisibility(View.INVISIBLE);

		imgPrevious.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				previousPage();
			}
		});

		imgNext = (ImageView) findViewById(R.id.game_intro_arrow_forward);
		imgNext.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				nextPage();
			}
		});
		
		bStartLevel = (Button) findViewById(R.id.game_intro_start_button);
		bStartLevel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onStartClick();				
			}
		});
		bStartLevel.setText(this.startButtonText());
		bStartLevel.setVisibility(View.INVISIBLE);

		mAdapter = new SwipePageAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
		mPager.setOnPageChangeListener(SwipeActivity.this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.level_grid, menu);
		return true;
	}
	
	private void nextPage() {
		int currentPage = mPager.getCurrentItem();
		int totalPages = mPager.getAdapter().getCount();
		int nextPage = currentPage + 1;

		checkAndHideButtons(totalPages, nextPage);

		mPager.setCurrentItem(nextPage, true);
	}

	private void previousPage() {
		int currentPage = mPager.getCurrentItem();
		int previousPage = currentPage - 1;
		int totalPages = mPager.getAdapter().getCount();

		checkAndHideButtons(totalPages, previousPage);

		mPager.setCurrentItem(previousPage, true);
	}

	protected void checkAndHideButtons(int totalPages, int nextPage) {
		imgNext.setVisibility(View.VISIBLE);
		imgPrevious.setVisibility(View.VISIBLE);
		bStartLevel.setVisibility(View.INVISIBLE);
		if (nextPage == totalPages - 1) {
			imgNext.setVisibility(View.INVISIBLE);
			if(this.startButtonText()!=null){
			  bStartLevel.setVisibility(View.VISIBLE);
			}
		}
		if (nextPage == 0 ) {
			imgPrevious.setVisibility(View.INVISIBLE);
		}
	}

	private class SwipePageAdapter extends FragmentPagerAdapter {
		public SwipePageAdapter(FragmentManager fragmentManager) {
			super(fragmentManager);
		}

		@Override
		public int getCount() {
			return getPageCount();
		}

		@Override
		public Fragment getItem(int position) {
			SwipeFragment frag = new SwipeFragment();
			frag.init(position,SwipeActivity.this);
			return frag;
		}
	}
	
	@Override
	public void onPageScrollStateChanged(int arg0) {}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {}

	@Override
	public void onPageSelected(int position) {
		checkAndHideButtons(mPager.getAdapter().getCount(), position);
	}
	
	public static class SwipeFragment extends Fragment{
		
		/**
		 * 
		 */
		private SwipeActivity swipeActivity;
		private Integer page = 0;
		
		public void init(int level, SwipeActivity activity){
			this.swipeActivity=activity;
			this.page=level;
		}
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = this.swipeActivity.getPage(page,inflater,container,savedInstanceState);
			view.setOnClickListener(this.swipeActivity.new clickListener(page));
			return view;
		}
	}
	
	
	private class clickListener implements View.OnClickListener{
        private int page;
        
        public clickListener(int level){
        	this.page=level;
        }
		@Override
		public void onClick(View v) {
			SwipeActivity.this.onClickPage(page);
		}
		
	}
	
	protected abstract int getPageCount();
	protected abstract View getPage(int page, LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
	
	protected void onClickPage(int page){}
	protected void onStartClick(){}
	protected String startButtonText(){
		return null;
	}
	
}
