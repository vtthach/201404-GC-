package cntg.imusm.gchat.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import cntg.imusm.commonutils.adapter.MyBaseAdapter;
import cntg.imusm.commonutils.view.GraphicsUtils;
import cntg.imusm.gchat.R;
import cntg.imusm.gchat.model.FriendModel;

/**
 * @Thach
 * @CreatedDate Mar 26, 2013
 */
public class FriendListAdapter extends MyBaseAdapter<FriendModel> {
	final String[] colorList = {"EEF2FBEF", "EEF8E0E0", "EEE0E0F8", "EECEF6F5",
			"EEF2EFFB" };

	public FriendListAdapter(Context context, List<FriendModel> itemList) {
		super(context, itemList);
	}

	// Tag for debug
	protected static final String TAG = FriendListAdapter.class.getName();

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get holder
		ViewHolderFriendList holder = onGetHolder(position, convertView, parent);
		// Set title
		holder.getTvName().setText(getItem(position).name);
		// Set status
		holder.getTvStatus().setText(getItem(position).status);
		// Set random background
		GraphicsUtils.setBackgroundView(holder.getViewLayout(), colorList[position
				% colorList.length], true);
		// Start animation
		onStartAnimationEmotion(holder);
		// return convertView
		return holder.mView;
	}

	private void onStartAnimationEmotion(ViewHolderFriendList holder) {
		Drawable animationBkg = holder.getImgEmotion().getBackground();
		if(animationBkg instanceof AnimationDrawable){
			((AnimationDrawable) animationBkg).start();
		}
	}

	/**
	 * @Thach Get view holder
	 * @return ViewHolder
	 */
	private ViewHolderFriendList onGetHolder(int position, View convertView,
			ViewGroup parent) {
		ViewHolderFriendList holder = null;
		if (convertView == null) {
			holder = new ViewHolderFriendList(convertView, parent);
		} else {
			holder = (ViewHolderFriendList) convertView.getTag();
		}
		return holder;
	}

	/**
	 * @Thach
	 * @Description: View holder
	 */
	public class ViewHolderFriendList {
		View mView;
		TextView tv_name;
		TextView tv_status;
		ImageView img_emotion;

		View layout;

		public ViewHolderFriendList(View mView, ViewGroup parent) {
			mView = inflater.inflate(R.layout.item_friend_list, parent, false);
			this.mView = mView;
			this.mView.setTag(ViewHolderFriendList.this);
		}

		// TextView display event title
		public TextView getTvName() {
			if (tv_name == null) {
				tv_name = (TextView) mView.findViewById(R.id.tv_name);
			}
			return tv_name;
		}

		// TextView display event title
		public TextView getTvStatus() {
			if (tv_status == null) {
				tv_status = (TextView) mView.findViewById(R.id.tv_status);
			}
			return tv_status;
		}

		// View container layout
		public View getViewLayout() {
			if (layout == null) {
				layout = mView.findViewById(R.id.layout);
			}
			return layout;
		}
		
		// View container layout
		public View getImgEmotion() {
			if (img_emotion == null) {
				img_emotion = (ImageView) mView.findViewById(R.id.img_emotion);
			}
			return img_emotion;
		}

		public Drawable getDrawbleEmotion(){
			Drawable animD = getImgEmotion().getBackground();
			if(animD instanceof AnimationDrawable){
				return animD.getCurrent();
			} else {
				return null;	
			}
		}
	}
}
