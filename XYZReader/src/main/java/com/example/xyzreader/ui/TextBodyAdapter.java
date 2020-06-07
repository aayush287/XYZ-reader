package com.example.xyzreader.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.xyzreader.R;

public class TextBodyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String[] mArticleText;
    private Context mContext;
    private String url;
    private String headerText;
    private String headerSubTitle;


    private static final int IMAGE_VIEW_TYPE = 1;
    private static final int HEADER_VIEW_TYPE = 2;
    private static final int BODY_VIEW_TYPE = 3;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        mContext = parent.getContext();
        if (viewType == IMAGE_VIEW_TYPE){
            View view = layoutInflater.inflate(R.layout.image_detail_activity, parent, false);
            return new ImageViewHolder(view);
        }else if (viewType == HEADER_VIEW_TYPE){
            View view = layoutInflater.inflate(R.layout.detail_view_header, parent, false);
            return new HeaderViewHolder(view);
        }else{
            View view = layoutInflater.inflate(R.layout.article_text_view_item, parent, false);
            return new TextBodyViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
       if (getItemViewType(position) == IMAGE_VIEW_TYPE){
           ((ImageViewHolder)holder).setImageView(url);
       }else if (getItemViewType(position) == HEADER_VIEW_TYPE){
           ((HeaderViewHolder)holder).setTitle(headerText);
           ((HeaderViewHolder)holder).setSubTitle(headerSubTitle);
       }else {
           ((TextBodyViewHolder)holder).setBodyText(mArticleText[position-2]);
       }
    }

    @Override
    public int getItemCount() {
        if (mArticleText == null){
            return 0;
        }
        return mArticleText.length+2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return IMAGE_VIEW_TYPE;
        } else if (position == 1){
            return HEADER_VIEW_TYPE;
        }else {
            return BODY_VIEW_TYPE;
        }
    }

    public void setArticleText(String[] articleText) {
        mArticleText = articleText;
        notifyDataSetChanged();
    }

    public void setUrl(String url) {
        this.url = url;
        notifyDataSetChanged();
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
        notifyDataSetChanged();
    }

    public void setHeaderSubTitle(String headerSubTitle) {
        this.headerSubTitle = headerSubTitle;
        notifyDataSetChanged();
    }

    class TextBodyViewHolder extends RecyclerView.ViewHolder{
        TextView mBodyText;
        public TextBodyViewHolder(@NonNull View itemView) {
            super(itemView);

            mBodyText = itemView.findViewById(R.id.article_text);
        }

        void setBodyText(String body){
            mBodyText.setTypeface(Typeface.createFromAsset(mContext.getResources().getAssets(), "Roboto-Regular.ttf"));
            mBodyText.setText(Html.fromHtml(body.replaceAll("(\r\n|\n)", "<br />")));
        }
    }

    class ImageViewHolder extends RecyclerView.ViewHolder{
        ImageView mImageView;
        private int mMutedColor = 0xFF333333;
        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);

            mImageView = itemView.findViewById(R.id.photo);
        }

        void setImageView(String imageUrl){
            ImageLoaderHelper.getInstance(mContext).getImageLoader()
                    .get(imageUrl, new ImageLoader.ImageListener() {
                        @Override
                        public void onResponse(ImageLoader.ImageContainer imageContainer, boolean b) {
                            Bitmap bitmap = imageContainer.getBitmap();
                            if (bitmap != null) {
                                Palette p = Palette.generate(bitmap, 12);
                                mMutedColor = p.getDarkMutedColor(0xFF333333);
                                mImageView.setImageBitmap(imageContainer.getBitmap());
                            }
                        }

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {

                        }
                    });
        }
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView subTitle;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.article_title);
            subTitle = itemView.findViewById(R.id.article_byline);
        }

        void setTitle(String titleText){
            title.setText(titleText);
        }
        void setSubTitle(String subTitleText){
            subTitle.setText(subTitleText);
        }
    }

}
