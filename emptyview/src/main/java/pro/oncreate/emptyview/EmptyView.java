package pro.oncreate.emptyview;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

/**
 * Created by Andrii Konovalenko, 2014-2017 years.
 * Copyright 2017 [Andrii Konovalenko]. All Rights Reserved.
 */

@SuppressWarnings("unused,WeakerAccess")
public class EmptyView {


    //
    // EmptyView params
    //


    private Context context;
    private ViewGroup parent;
    private EmptyViewOption emptyViewOption;
    private EmptyViewOption customViewOption;
    private EmptyViewOption connectionViewOption;
    private EmptyViewOption bannedViewOption;
    private EmptyViewOption accessDeniedViewOption;
    private OnClickEmptyViewListener onClickListener;


    //
    // EmptyView states and data
    //


    private LayoutInflater inflater;
    private States state;
    private boolean visible, enable;
    private int textResId;
    private int buttonResId;
    // private Animation animation;


    //
    // Constructor
    //


    private EmptyView(Context context, ViewGroup parent,
                      EmptyViewOption emptyViewOption,
                      EmptyViewOption customViewOption,
                      EmptyViewOption connectionViewOption,
                      EmptyViewOption bannedViewOption,
                      EmptyViewOption accessDeniedViewOption,
                      int textResId, int buttonResId,
                      OnClickEmptyViewListener onClickListener) {
        this.context = context;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.parent = parent;
        this.emptyViewOption = emptyViewOption;
        this.customViewOption = customViewOption;
        this.connectionViewOption = connectionViewOption;
        this.bannedViewOption = bannedViewOption;
        this.accessDeniedViewOption = accessDeniedViewOption;
        this.textResId = textResId;
        this.buttonResId = buttonResId;
        this.onClickListener = onClickListener;

        this.state = States.NONE;
        this.visible = false;
        this.enable = true;
        // animation = AnimationUtils.loadAnimation(context, R.anim.flip_anim);
    }


    //
    // Base logic
    //


    private View getEmptyView() {
        if (parent != null)
            return parent.findViewById(R.id.empty_view_layout);
        else return null;
    }

    private View createOrGetEmptyView() {
        try {
            View v = getEmptyView();
            if (v == null) {
                v = inflater.inflate(R.layout.empty_view, parent, false);
                parent.addView(v);
            }
            return v;
        } catch (Exception e) {
            return null;
        }
    }

    private void fillEmptyView(View v, EmptyViewOption emptyViewOption) {
        try {
            stopAndRemoveAnimation();

            TextView textView = v.findViewById(R.id.empty_view_text);
            ImageView img = v.findViewById(R.id.empty_view_img);
            Button button = v.findViewById(R.id.empty_view_button);

            if (emptyViewOption.text != null) {
                textView.setVisibility(View.VISIBLE);
                textView.setText(emptyViewOption.text);

                int resId = emptyViewOption.textStyleId != EmptyViewOption.NO_RESOURCE ?
                        emptyViewOption.textStyleId : textResId;
                if (resId != EmptyViewOption.NO_RESOURCE)
                    applyTextStyle(resId, textView);

            } else textView.setVisibility(View.GONE);

            if (emptyViewOption.btnText != null) {
                button.setVisibility(View.VISIBLE);
                button.setText(emptyViewOption.btnText);

//                if (buttonResId != EmptyViewOption.NO_RESOURCE)
//                    applyTextStyle(buttonResId, button);

                if (onClickListener != null) {
                    button.setTag(emptyViewOption.state);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onClickListener.onEmptyViewClick((States) v.getTag());
                        }
                    });
                }
            } else button.setVisibility(View.GONE);

            if (emptyViewOption.imgResId != EmptyViewOption.NO_RESOURCE) {
                img.setVisibility(View.VISIBLE);
                img.setImageResource(emptyViewOption.imgResId);
//                img.startAnimation(animation);
//                animation.setAnimationListener(new Animation.AnimationListener() {
//                    @Override
//                    public void onAnimationStart(Animation animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animation animation) {
//                        EmptyView.this.animation = null;
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animation animation) {
//
//                    }
//                });
            } else img.setVisibility(View.GONE);

        } catch (Exception ignored) {
        }
    }

    private void applyTextStyle(int resId, TextView textView) {
        if (Build.VERSION.SDK_INT < 23)
            textView.setTextAppearance(context, resId);
        else
            textView.setTextAppearance(resId);
    }

    private void stopAndRemoveAnimation() {
        try {
//            if (animation != null)
//                animation.cancel();
        } catch (Exception ignored) {
        }
    }


    //
    // Change visibility
    //


    public EmptyView show() {
        if (getEmptyView() != null && enable) {
            try {
                getEmptyView().setVisibility(View.VISIBLE);
                visible = true;
            } catch (Exception ignored) {
            }
        }
        return this;
    }

    public EmptyView hide() {
        if (getEmptyView() != null && enable) {
            try {
                getEmptyView().setVisibility(View.GONE);
                visible = false;
            } catch (Exception ignored) {
            }
        }
        return this;
    }

    public EmptyView hideContent() {
        if (parent != null && enable) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                try {
                    View v = parent.getChildAt(i);
                    if (v.getId() != R.id.empty_view_layout
                            && !(v instanceof AppBarLayout)
                            && !(v instanceof Toolbar)
                            && !(v instanceof CollapsingToolbarLayout))
                        v.setVisibility(View.INVISIBLE);
                } catch (Exception ignored) {
                }
            }
        }
        return this;
    }

    public EmptyView showContent() {
        if (parent != null) {
            for (int i = 0; i < parent.getChildCount(); i++) {
                try {
                    View v = parent.getChildAt(i);
                    if (v.getId() != R.id.empty_view_layout)
                        v.setVisibility(View.VISIBLE);
                } catch (Exception ignored) {
                }
            }
        }
        return this;
    }


    //
    // Change change states
    //


    public EmptyView progress() {
        // TODO:
        this.state = States.PROGRESS;
        return this;
    }

    public EmptyView empty() {
        if (emptyViewOption != null && enable && this.state != States.EMPTY) {
            View v = createOrGetEmptyView();
            if (v != null) {
                fillEmptyView(v, emptyViewOption);
                this.state = States.EMPTY;
                visible = true;
            }
        }
        return this;
    }

    public EmptyView custom() {
        if (customViewOption != null && enable) {
            View v = createOrGetEmptyView();
            if (v != null) {
                fillEmptyView(v, customViewOption);
                this.state = States.CUSTOM;
                visible = true;
            }
        }
        return this;
    }

    public EmptyView connection() {
        if (connectionViewOption != null && enable) {
            View v = createOrGetEmptyView();
            if (v != null) {
                fillEmptyView(v, connectionViewOption);
                this.state = States.CONNECTION;
                visible = true;
            }
        }
        return this;
    }

    public EmptyView banned() {
        if (bannedViewOption != null && enable) {
            View v = createOrGetEmptyView();
            if (v != null) {
                fillEmptyView(v, bannedViewOption);
                this.state = States.BANNED;
                visible = true;
            }
        }
        return this;
    }

    public EmptyView accessDenied() {
        if (accessDeniedViewOption != null && enable) {
            View v = createOrGetEmptyView();
            if (v != null) {
                fillEmptyView(v, accessDeniedViewOption);
                this.state = States.ACCESS_DENIED;
                visible = true;
            }
        }
        return this;
    }

    public EmptyView reset() {
        final View v = getEmptyView();
        if (v != null) {
            v.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        stopAndRemoveAnimation();
                        parent.removeView(v);
                        state = States.NONE;
                        visible = false;
                    } catch (Exception ignored) {
                    }
                }
            });
        }
        return this;
    }


    //
    // Empty View getters and setters
    //


    public boolean isVisible() {
        return visible;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    //
    // Empty View builder
    //


    public static class Builder {
        private Context context;
        private ViewGroup parent;
        private EmptyViewOption emptyViewOption;
        private EmptyViewOption customViewOption;
        private EmptyViewOption connectionViewOption;
        private EmptyViewOption bannedViewOption;
        private EmptyViewOption accessDeniedViewOption;
        private OnClickEmptyViewListener onClickListener;
        private int textStyleId = EmptyViewOption.NO_RESOURCE;
        private int buttonStyleId = EmptyViewOption.NO_RESOURCE;

        private Builder(Context context) {
            this.context = context;
        }

        public static Builder create(Context context) {
            return new Builder(context);
        }

        public Builder where(ViewGroup parent) {
            this.parent = parent;
            return this;
        }

        public Builder empty(EmptyViewOption emptyViewOption) {
            this.emptyViewOption = emptyViewOption;
            if (this.emptyViewOption != null)
                this.emptyViewOption.state = States.EMPTY;
            return this;
        }

        public Builder custom(EmptyViewOption emptyViewOption) {
            this.customViewOption = emptyViewOption;
            if (this.customViewOption != null)
                this.customViewOption.state = States.CUSTOM;
            return this;
        }

        public Builder banned(EmptyViewOption emptyViewOption) {
            this.bannedViewOption = emptyViewOption;
            if (this.bannedViewOption != null)
                this.bannedViewOption.state = States.BANNED;
            return this;
        }

        public Builder accessDenied(EmptyViewOption emptyViewOption) {
            this.accessDeniedViewOption = emptyViewOption;
            if (this.accessDeniedViewOption != null)
                this.accessDeniedViewOption.state = States.ACCESS_DENIED;
            return this;
        }

        public Builder connection(EmptyViewOption emptyViewOption) {
            this.connectionViewOption = emptyViewOption;
            if (this.connectionViewOption != null)
                this.connectionViewOption.state = States.CONNECTION;
            return this;
        }

        public Builder setOnClickListener(OnClickEmptyViewListener onClickListener) {
            this.onClickListener = onClickListener;
            return this;
        }

        public Builder setTextStyle(int styleId) {
            this.textStyleId = styleId;
            return this;
        }

        public Builder setButtonStyle(int styleId) {
            this.buttonStyleId = styleId;
            return this;
        }

        public EmptyView build() {
            return new EmptyView(context, parent, emptyViewOption, customViewOption, connectionViewOption,
                    bannedViewOption, accessDeniedViewOption, textStyleId, buttonStyleId, onClickListener);
        }
    }


    //
    // Empty view option
    //


    public static class EmptyViewOption {
        public static final int NO_RESOURCE = -1;

        String text;
        String btnText;
        int imgResId = NO_RESOURCE;
        int textStyleId = NO_RESOURCE;
        States state;


        public EmptyViewOption(String text) {
            this.text = text;
        }

        public EmptyViewOption(int imgResId) {
            this.imgResId = imgResId;
        }

        public EmptyViewOption(String text, int imgResId) {
            this(text);
            this.imgResId = imgResId;
        }

        public EmptyViewOption(String text, String btnText) {
            this(text);
            this.btnText = btnText;
        }

        public EmptyViewOption(int imgResId, String btnText) {
            this(imgResId);
            this.btnText = btnText;
        }

        public EmptyViewOption(String text, int imgResId, String btnText) {
            this(text, imgResId);
            this.btnText = btnText;
        }

        public void setImgResId(int imgResId) {
            this.imgResId = imgResId;
        }
    }


    //
    // Empty View states
    //


    public enum States {
        NONE,
        PROGRESS, EMPTY,
        CUSTOM, CONNECTION,
        ACCESS_DENIED, BANNED
    }


    //
    // Listeners
    //


    public interface EmptyViewAdapterListener {
        void prepareEmptyView(EmptyView trueEmptyView);
    }

    public interface OnClickEmptyViewListener {
        void onEmptyViewClick(States state);
    }

}
