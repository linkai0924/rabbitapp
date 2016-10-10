package com.means.rabbit.views;

import com.means.rabbit.R;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.loadmore.LoadMoreContainer;
import in.srain.cube.views.ptr.loadmore.LoadMoreHandler;
import in.srain.cube.views.ptr.loadmore.LoadMoreListViewContainer;
import net.duohuo.dhroid.adapter.INetAdapter.LoadSuccessCallBack;
import net.duohuo.dhroid.adapter.NetJSONAdapter;
import net.duohuo.dhroid.net.Response;
import net.duohuo.dhroid.util.DhUtil;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class RefreshListViewAndMore extends LinearLayout {

	View contentV;

	Context mContext;

	PtrFrameLayout mPtrFrame;

	ListView listV;

	NetJSONAdapter mAdapter;

	LoadMoreListViewContainer loadMoreListViewContainer;

	OnLoadSuccess onLoadSuccess;

	View mheadV, mEmptyV;

	LinearLayout emptyLayout;

	public RefreshListViewAndMore(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		initView();
	}

	private void initView() {
		LayoutInflater.from(mContext).inflate(
				R.layout.include_refresh_listview_base, this);
		emptyLayout = (LinearLayout) findViewById(R.id.empty_layout);

		listV = (ListView) findViewById(R.id.listview);
		mPtrFrame = (PtrFrameLayout) findViewById(R.id.ptr_frame);
		mPtrFrame.disableWhenHorizontalMove(true);
		loadMoreListViewContainer = (LoadMoreListViewContainer) findViewById(R.id.load_more_list_view_container);
		LoadMoreFootView footView = new LoadMoreFootView(mContext);
		loadMoreListViewContainer.setLoadMoreView(footView);
		loadMoreListViewContainer.setLoadMoreUIHandler(footView);
		final StoreHouseHeader header = new StoreHouseHeader(mContext);
		header.setPadding(0, DhUtil.dip2px(mContext, 15), 0,
				DhUtil.dip2px(mContext, 10));

		header.initWithString("LazyBunny");
		header.setTextColor(getResources().getColor(R.color.text_2B_green));
		mPtrFrame.addPtrUIHandler(header);
		mPtrFrame.setHeaderView(header);
		mPtrFrame.setPinContent(false);

		// loadMoreListViewContainer.useDefaultHeader();
		loadMoreListViewContainer.setAutoLoadMore(true);

		loadMoreListViewContainer.setLoadMoreHandler(new LoadMoreHandler() {
			@Override
			public void onLoadMore(LoadMoreContainer loadMoreContainer) {
				if (mAdapter != null) {
					mAdapter.showNext();
				}
			}
		});

		mPtrFrame.setPtrHandler(new PtrHandler() {
			@Override
			public void onRefreshBegin(PtrFrameLayout frame) {
				if (mAdapter != null) {
					mAdapter.refresh();
				}
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame,
					View content, View header) {
				return PtrDefaultHandler.checkContentCanBePulledDown(frame,
						listV, header);
			}
		});

		mPtrFrame.postDelayed(new Runnable() {

			@Override
			public void run() {
				mPtrFrame.autoRefresh(true);
//				if (mAdapter != null) {
//					mAdapter.refresh();  
//				}
			}
		}, 300);

	}

	public void refresh() {
		mPtrFrame.autoRefresh(true);
	}

	public void setListViewPadding(int left, int top, int right, int bottom) {
		listV.setPadding(left, top, right, bottom);
	}

	public void addHeadView(View headV) {
		mheadV = headV;
		listV.addHeaderView(headV);
	}

	public LoadMoreListViewContainer getLoadMoreListViewContainer() {
		return loadMoreListViewContainer;
	}

	public ListView getListView() {
		return listV;
	}

	public void removeHeadView() {
		if (mheadV != null) {
			mheadV.setVisibility(View.GONE);
			mheadV.setPadding(0, -mheadV.getHeight(), 0, 0);
		}
	}

	public void showHeadView() {
		if (mheadV != null) {
			mheadV.setVisibility(View.VISIBLE);
			mheadV.setPadding(0, 0, 0, 0);
		}
	}

	public void setEmptyView(View empty) {
		mEmptyV = empty;
		if (mEmptyV != null) {
			emptyLayout.addView(mEmptyV);
		}
	}

	public void setEmptyViewTop(View empty) {
		mEmptyV = empty;
		if (mEmptyV != null) {
			LayoutParams params = new LayoutParams(
					LinearLayout.LayoutParams.FILL_PARENT,
					LinearLayout.LayoutParams.FILL_PARENT);
			params.gravity = Gravity.TOP;
			emptyLayout.addView(mEmptyV, params);
		}
	}

	public void setAdapter(NetJSONAdapter adapter) {
		mAdapter = adapter;
		mAdapter.setOnLoadSuccess(new LoadSuccessCallBack() {

			@Override
			public void callBack(Response response) {

				if (onLoadSuccess != null) {
					onLoadSuccess.loadSuccess(response);
				}

				if (mAdapter.getPageNo() == 1) {
					if (mEmptyV != null) {
						emptyLayout
								.setVisibility(mAdapter.getValues().size() == 0 ? View.VISIBLE
										: View.GONE);
					}

					loadMoreListViewContainer
							.setShowLoadingForFirstPage(mAdapter.hasMore());
					loadMoreListViewContainer.loadMoreFinish(
							!mAdapter.hasMore(), mAdapter.hasMore());
				} else {
					loadMoreListViewContainer.loadMoreFinish(mAdapter
							.getValues().size() != 0 ? false : true, mAdapter
							.hasMore());

				}

				mPtrFrame.refreshComplete();
			}
		});
		listV.setAdapter(mAdapter);
	}

	public void setAdapterNoBindListView(NetJSONAdapter adapter) {
		mAdapter = adapter;
		mAdapter.setOnLoadSuccess(new LoadSuccessCallBack() {

			@Override
			public void callBack(Response response) {

				if (onLoadSuccess != null) {
					onLoadSuccess.loadSuccess(response);
				}

				if (mAdapter.getPageNo() == 1) {
					if (mEmptyV != null) {
						mEmptyV.setVisibility(mAdapter.getValues().size() != 0 ? View.VISIBLE
								: View.GONE);
					}
					loadMoreListViewContainer
							.setShowLoadingForFirstPage(mAdapter.hasMore());
					loadMoreListViewContainer.loadMoreFinish(
							!mAdapter.hasMore(), mAdapter.hasMore());
				} else {
					loadMoreListViewContainer.loadMoreFinish(mAdapter
							.getValues().size() != 0 ? false : true, mAdapter
							.hasMore());
				}

				mPtrFrame.refreshComplete();
			}
		});
		// listV.setAdapter(mAdapter);
	}

	public OnLoadSuccess getOnLoadSuccess() {
		return onLoadSuccess;
	}

	public void setOnLoadSuccess(OnLoadSuccess onLoadSuccess) {
		this.onLoadSuccess = onLoadSuccess;
	}

	public interface OnLoadSuccess {
		void loadSuccess(Response response);
	}

}
