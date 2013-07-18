/*
 * Copyright (C) 2011 Google Inc.
 * Licensed to The Android Open Source Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.ex.photo;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;

import com.android.ex.photo.fragments.PhotoViewFragment;

/**
 * Build intents to start app activities
 */
public class Intents {
    // Intent extras
    public static final String EXTRA_PHOTO_INDEX = "photo_index";
    public static final String EXTRA_INITIAL_PHOTO_URI = "initial_photo_uri";
    public static final String EXTRA_PHOTOS_URI = "photos_uri";
    public static final String EXTRA_RESOLVED_PHOTO_URI = "resolved_photo_uri";
    public static final String EXTRA_PROJECTION = "projection";
    public static final String EXTRA_THUMBNAIL_URI = "thumbnail_uri";
    public static final String EXTRA_MAX_INITIAL_SCALE = "max_scale";
    public static final String EXTRA_WATCH_NETWORK = "watch_network";

    /**
     * Gets a photo view intent builder to display the photos from phone activity.
     *
     * @param context The context
     * @return The intent builder
     */
    public static PhotoViewIntentBuilder newPhotoViewActivityIntentBuilder(Context context) {
        return new PhotoViewIntentBuilder(context, PhotoViewActivity.class);
    }

    /**
     * Gets a photo view intent builder to display the photo view fragment
     *
     * @param context The context
     * @return The intent builder
     */
    public static PhotoViewIntentBuilder newPhotoViewFragmentIntentBuilder(Context context) {
        return new PhotoViewIntentBuilder(context, PhotoViewFragment.class);
    }

    /** Gets a new photo view intent builder */
    public static PhotoViewIntentBuilder newPhotoViewIntentBuilder(
            Context context, Class<? extends Activity> cls) {
        return new PhotoViewIntentBuilder(context, cls);
    }

    /** Builder to create a photo view intent */
    public static class PhotoViewIntentBuilder {
        private final Intent mIntent;

        /** The index of the photo to show */
        private Integer mPhotoIndex;
        /** The URI of the initial photo to show */
        private String mInitialPhotoUri;
        /** The URI of the group of photos to display */
        private String mPhotosUri;
        /** The URL of the photo to display */
        private String mResolvedPhotoUri;
        /** The projection for the query to use; optional */
        private String[] mProjection;
        /** The URI of a thumbnail of the photo to display */
        private String mThumbnailUri;
        /** The maximum scale to display images at before  */
        private Float mMaxInitialScale;
        /**
         * True if the PhotoViewFragments should watch for network changes to restart their loaders
         */
        private boolean mWatchNetwork;

        private PhotoViewIntentBuilder(Context context, Class<?> cls) {
            mIntent = new Intent(context, cls);
        }

        /** Sets the photo index */
        public PhotoViewIntentBuilder setPhotoIndex(Integer photoIndex) {
            mPhotoIndex = photoIndex;
            return this;
        }

        /** Sets the initial photo URI */
        public PhotoViewIntentBuilder setInitialPhotoUri(String initialPhotoUri) {
            mInitialPhotoUri = initialPhotoUri;
            return this;
        }

        /** Sets the photos URI */
        public PhotoViewIntentBuilder setPhotosUri(String photosUri) {
            mPhotosUri = photosUri;
            return this;
        }

        /** Sets the query projection */
        public PhotoViewIntentBuilder setProjection(String[] projection) {
            mProjection = projection;
            return this;
        }

        /** Sets the resolved photo URI. This method is for the case
         *  where the URI given to {@link PhotoViewActivity} points directly
         *  to a single image and does not need to be resolved via a query
         *  to the {@link ContentProvider}. If this value is set, it supersedes
         *  {@link #setPhotosUri(String)}. */
        public PhotoViewIntentBuilder setResolvedPhotoUri(String resolvedPhotoUri) {
            mResolvedPhotoUri = resolvedPhotoUri;
            return this;
        }

        /**
         * Sets the URI for a thumbnail preview of the photo.
         */
        public PhotoViewIntentBuilder setThumbnailUri(String thumbnailUri) {
            mThumbnailUri = thumbnailUri;
            return this;
        }

        /**
         * Sets the maximum scale which an image is initially displayed at
         */
        public PhotoViewIntentBuilder setMaxInitialScale(float maxScale) {
            mMaxInitialScale = maxScale;
            return this;
        }

        /**
         * Enable watching the network for connectivity changes.
         *
         * When a change is detected, bitmap loaders will be restarted if required.
         */
        public PhotoViewIntentBuilder watchNetworkConnectivityChanges() {
            mWatchNetwork = true;
            return this;
        }

        /** Build the intent */
        public Intent build() {
            mIntent.setAction(Intent.ACTION_VIEW);

            mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

            if (mPhotoIndex != null) {
                mIntent.putExtra(EXTRA_PHOTO_INDEX, (int) mPhotoIndex);
            }

            if (mInitialPhotoUri != null) {
                mIntent.putExtra(EXTRA_INITIAL_PHOTO_URI, mInitialPhotoUri);
            }

            if (mInitialPhotoUri != null && mPhotoIndex != null) {
                throw new IllegalStateException(
                        "specified both photo index and photo uri");
            }

            if (mPhotosUri != null) {
                mIntent.putExtra(EXTRA_PHOTOS_URI, mPhotosUri);
            }

            if (mResolvedPhotoUri != null) {
                mIntent.putExtra(EXTRA_RESOLVED_PHOTO_URI, mResolvedPhotoUri);
            }

            if (mProjection != null) {
                mIntent.putExtra(EXTRA_PROJECTION, mProjection);
            }

            if (mThumbnailUri != null) {
                mIntent.putExtra(EXTRA_THUMBNAIL_URI, mThumbnailUri);
            }

            if (mMaxInitialScale != null) {
                mIntent.putExtra(EXTRA_MAX_INITIAL_SCALE, mMaxInitialScale);
            }

            if (mWatchNetwork == true) {
                mIntent.putExtra(EXTRA_WATCH_NETWORK, true);
            }

            return mIntent;
        }
    }
}
