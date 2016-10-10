package com.means.rabbit.base;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import in.srain.cube.views.ptr.loadmore.LoadMoreListViewContainer;
import net.duohuo.dhroid.util.DhUtil;
import android.support.v4.app.Fragment;

import com.means.rabbit.R;

public class RabbitListFragment extends Fragment {

	public void initRefreshListView(final PtrFrameLayout mPtrFrame,
			LoadMoreListViewContainer loadMoreListViewContainer) {

		final StoreHouseHeader header = new StoreHouseHeader(getActivity());
		header.setPadding(0, DhUtil.dip2px(getActivity(), 15), 0, 0);
		header.initWithString("Foods");
		header.setTextColor(getResources().getColor(R.color.text_2B_green));
		mPtrFrame.addPtrUIHandler(header);
		mPtrFrame.setHeaderView(header);
		mPtrFrame.setPinContent(false);

		loadMoreListViewContainer.useDefaultHeader();
		loadMoreListViewContainer.setAutoLoadMore(true);
		loadMoreListViewContainer.setShowLoadingForFirstPage(true);

	}

}
