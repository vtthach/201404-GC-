package cntg.imusm.commonutils.adapter;

/**
 * Copyright (c)  TMA Mobile Solutions, TMA Solutions company. All Rights Reserved.
 * This software is the confidential and proprietary information of TMA Solutions, 
 * ("Confidential Information").
 * You shall not disclose such Confidential Information and shall use it only in accordance 
 * with the terms of the license agreement you entered into with TMA Solutions.
 *
 *
 * TMA SOLUTIONS MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE SOFTWARE, 
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE IMPLIED WARRANTIES OF MERCHANTABILITY, 
 * FITNESS FOR A PARTICULAR PURPOSE, OR NON-INFRINGEMENT. TMA SOLUTIONS SHALL NOT BE LIABLE FOR ANY DAMAGES 
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES
 */

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @author vtthach
 * @param <T>
 * @CreatedDate Mar 26, 2013
 * @Description: BaseAdapter with ArrayList<?>, must override getView(..) when
 *               extend from this class
 */
public abstract class MyBaseAdapter<T> extends BaseAdapter {
	// inflater for inflate a view
	public LayoutInflater inflater;

	// Context
	Context context;

	// List data
	List<T> itemList;

	// Tag for debug
	protected static final String TAG = MyBaseAdapter.class.getName();

	public MyBaseAdapter(Context context, List<T> itemList) {
		if (context == null) {
			throw new IllegalArgumentException(
					"cntg - Contructor CntgImuBaseAdapter error: context must not null");
		} else {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		if (itemList == null) {
			throw new IllegalArgumentException(
					"cntg - Contructor CntgImuBaseAdapter error: ItemList must not null");
		} else {
			this.itemList = itemList;
		}

		this.context = context;
	}

	/**
	 * 
	 * @Description: update list data and notify update GUI when data
	 *               change(search, update list...)
	 * @param itemList
	 *            : list data need to update
	 * @return: true: if update list data successfully, false: otherwise
	 */
	public void updateListData(List<T> itemList) {
		if (itemList == null) {
			throw new NullPointerException(
					"cntg - updateListData error: itemList null");
		}
		this.itemList.clear();
		this.itemList.addAll(itemList);
		itemList.clear();
		itemList = null;
		this.notifyDataSetChanged();
	}
	
	public List<T> getItemList(){
		return itemList;
	}

	@Override
	public long getItemId(int position) {
		return itemList != null ? position : -1;
	}

	@Override
	public T getItem(int position) {
		return itemList != null ? itemList.get(position) : null;
	}

	@Override
	public int getCount() {
		return itemList != null ? itemList.size() : 0;
	}

	@Override
	public abstract View getView(int position, View convertView,
			ViewGroup parent);
}
