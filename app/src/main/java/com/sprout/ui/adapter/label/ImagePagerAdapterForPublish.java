package com.sprout.ui.adapter.label;

import android.app.Activity;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.sprout.app.weight.photodraweeview.ITagBean;
import com.sprout.app.weight.photodraweeview.OnPhotoTapListener;
import com.sprout.app.weight.photodraweeview.PhotoTagDraweeView;
import com.sprout.app.weight.photodraweeview.PictureTagFrameLayout;
import com.sprout.data.model.bean.IChooserModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ImagePagerAdapterForPublish extends PagerAdapter {

    private List<IChooserModel> mDatas = new ArrayList<>();
    private Activity mActivity;
    private int mMaxSize, mBottomPadding;
    private PictureTagFrameLayout.ITagLayoutCallBack mTagLayoutCallBack;
    private Map<String, List<ITagBean>> mTagBeansMap;
    private View mCurrentView;


    public ImagePagerAdapterForPublish(Activity activity,
                                       PictureTagFrameLayout.ITagLayoutCallBack tagLayoutCallBack) {
        mTagLayoutCallBack = tagLayoutCallBack;
        mActivity = activity;
        mMaxSize = mActivity.getResources().getDisplayMetrics().widthPixels;
    }

    public void setData(List<IChooserModel> dataList, Map<String, List<ITagBean>> tagBeanMap,
                        int bottomPadding) {
        if (dataList == null) {
            mDatas = new ArrayList<>();
        } else {
            mDatas = dataList;
        }
        mTagBeansMap = tagBeanMap;
        mBottomPadding = bottomPadding;
        notifyDataSetChanged();
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (object instanceof View) {
            mCurrentView = (View) object;
        }
    }

    public View getPrimaryItem() {
        return mCurrentView;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, final int position) {
        final IChooserModel mediaModel = getItem(position);
        if (mediaModel == null) {
            return null;
        }
        final Uri uri = Uri.parse("file://" + mediaModel.getFilePath());

        Log.d("Tag", "instantiateItem: "+mediaModel.getFilePath());

        /**
         * item重新初始化时，重置 tagBean 的 hasAdded
         */
        resetTagAdded(mediaModel);

        PictureTagFrameLayout pictureTagLayout = handleFrameLayout(mediaModel);

        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(mMaxSize, mMaxSize))
                .build();

        PhotoTagDraweeView photoView = instantiateImageItem(container, request, mediaModel);

        handleBgView(photoView, pictureTagLayout);

        container.addView(pictureTagLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        return pictureTagLayout;
    }

    private PictureTagFrameLayout handleFrameLayout(IChooserModel mediaModel){

        PictureTagFrameLayout pictureTagLayout = new PictureTagFrameLayout(mActivity);
        pictureTagLayout.setPadding(0, 0, 0, mBottomPadding);
        pictureTagLayout.setTagLayoutCallBack(mTagLayoutCallBack);

        if (mediaModel != null) {
            if (mTagBeansMap == null) {
                mTagBeansMap = new HashMap<>();
            }
            List<ITagBean> list = mTagBeansMap.get(mediaModel.getFilePath());
            if (list == null) {
                list = new ArrayList<>();
                mTagBeansMap.put(mediaModel.getFilePath(), list);
            }
            if(mediaModel.getHeight() > 0) {
                pictureTagLayout.updateTagViews(list,
                        1.0f * mediaModel.getWidth() / mediaModel.getHeight());
            }
        }
        return pictureTagLayout;
    }

    private void handleBgView(PhotoTagDraweeView photoDraweeView,
                              final PictureTagFrameLayout pictureTagLayout) {

        if(photoDraweeView == null || pictureTagLayout == null){
            return;
        }

        photoDraweeView.setEnabled(true);
        photoDraweeView.setEnableScale(false);  //设置 photoDraweeView 不可以放大缩小
        photoDraweeView.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(View view, float x, float y) {
                if (mTagLayoutCallBack != null) {
                    mTagLayoutCallBack.onSingleClick(x, y);
                }
            }
        });
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        pictureTagLayout.addView(photoDraweeView, params);
    }

    private PhotoTagDraweeView instantiateImageItem(ViewGroup container,
                                                    ImageRequest request, IChooserModel mediaModel) {
        if (container == null) {
            return null;
        }

        final PhotoTagDraweeView photoView = new PhotoTagDraweeView(container.getContext());
        photoView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        photoView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        DraweeController controller = Fresco.newDraweeControllerBuilder().build();
        photoView.setController(controller);
        photoView.setImageURI("file://" + mediaModel.getFilePath());
//        Glide.with(container.getContext()).load(mediaModel.getFilePath()).into(photoView);
        photoView.update(mediaModel.getWidth(), mediaModel.getHeight());
        return photoView;
    }

    public IChooserModel getItem(int position) {
        if (position < mDatas.size() && position >= 0) {
            return mDatas.get(position);
        } else {
            return null;
        }
    }

    private void resetTagAdded(IChooserModel mediaModel) {
        if (mediaModel == null || mTagBeansMap == null || mTagBeansMap.isEmpty()) {
            return;
        }

        List<ITagBean> list = mTagBeansMap.get(mediaModel.getFilePath());
        if (list != null && !list.isEmpty()) {
            for (int i = 0; i < list.size(); i++) {
                ITagBean tagBean = list.get(i);
                if(tagBean != null) {
                    tagBean.setHasAdded(false);
                }
            }
        }
    }

    public Map<String, List<ITagBean>> getTagBeansMap(){
        return mTagBeansMap;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(@NotNull View view, @NotNull Object object) {
        return view == object;
    }


}
