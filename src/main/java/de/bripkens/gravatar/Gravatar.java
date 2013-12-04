/**
 * Copyright 2011 Ben Ripkens
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.bripkens.gravatar;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * A utility class for the generation of Gravatar URLs. This class
 * supports various settings, e.g. default image, custom default image and
 * avatar ratings that are meant to simplify the URL generation process.
 * <br />
 * A Gravatar instance can be used multiple times and the getUrl(...) method
 * is thread safe.
 * <br />
 * Example:
 * <pre>
 * {@code
 * String gravatarImageURL = new Gravatar()
 *               .setSize(50)
 *               .setHttps(true)
 *               .setRating(Rating.PARENTAL_GUIDANCE_SUGGESTED)
 *               .setStandardDefaultImage(DefaultImage.MONSTER)
 *               .getUrl("foobar@example.com");
 * }
 * </pre>
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class Gravatar {
    public static final String URL = "http://www.gravatar.com/avatar/";
    public static final String HTTPS_URL = "https://secure.gravatar.com/avatar/";
    public static final String FILE_TYPE_EXTENSION = ".jpg";
    public static final int DEFAULT_SIZE = 80;
    public static final Rating DEFAULT_RATING = Rating.GENERAL_AUDIENCE;
    private int size = DEFAULT_SIZE;
    private boolean https;
    private boolean forceDefault;
    private Rating rating = DEFAULT_RATING;
    private DefaultImage standardDefaultImage;
    private String customDefaultImage;

    /**
     * Retrieve the current setting for the desired size
     *
     * @return the size in pixel
     * @see Gravatar#setSize(int)
     */
    public int getSize() {
        return size;
    }

    /**
     * Set the desired size through the size property. All avatars have a
     * quadratic form, thus only one value is required. The size is expected
     * in pixel
     *
     * @param size The avatar size in pixel.
     * @return Fluent interface
     */
    public Gravatar setSize(int size) {
        assert size > 0 && size < 2049;

        this.size = size;
        return this;
    }

    /**
     * Check whether the avatar is to be retrieved over HTTPS
     *
     * @return true when the avatar should be retrieved over HTTPS
     */
    public boolean isHttps() {
        return https;
    }

    /**
     * Retrieve an avatar URL which allows retrieval over HTTPS.
     *
     * @param https Set to true to retrieve a HTTPS URL.
     * @return Fluent interface
     */
    public Gravatar setHttps(boolean https) {
        this.https = https;
        return this;
    }

    /**
     * Check whether the default avatar is enforced.
     *
     * @return true when it's enforced
     */
    public boolean isForceDefault() {
        return forceDefault;
    }

    /**
     * Enforce usage of the default image by passing true.
     *
     * @param forceDefault True to always retrieve the default image.
     * @return Fluent interface
     */
    public Gravatar setForceDefault(boolean forceDefault) {
        this.forceDefault = forceDefault;
        return this;
    }

    /**
     * Retrieve information about the allowed avatar rating
     *
     * @return avatar rating
     * @see Gravatar#setRating(Rating)
     */
    public Rating getRating() {
        return rating;
    }

    /**
     * Restrict the retrieved avatars to the ones appropriate for the audience.
     * Which ones are appropriate can be set through the rating.
     *
     * @param rating This rating will be allowed.
     * @return Fluent interface
     */
    public Gravatar setRating(Rating rating) {
        assert rating != null;

        this.rating = rating;
        return this;
    }

    /**
     * Retrieve the standard default image setting
     *
     * @return Currently set standard default image
     * @see Gravatar#setStandardDefaultImage(DefaultImage)
     */
    public DefaultImage getStandardDefaultImage() {
        return standardDefaultImage;
    }

    /**
     * Set the default image which will be retrieved when there is no avatar
     * for the given email, when the avatar can't be shown due to the rating
     * or when you enforce the default avatar.
     *
     * @param standardDefaultImage One of multiple default avatar choices.
     * @return Fluent interface
     */
    public Gravatar setStandardDefaultImage(DefaultImage standardDefaultImage) {
        assert standardDefaultImage != null;

        this.standardDefaultImage = standardDefaultImage;
        customDefaultImage = null;
        return this;
    }

    /**
     * Set the default image which will be retrieved when there is no avatar
     * for the given email, when the avatar can't be shown due to the rating
     * or when you enforce the default avatar.
     *
     * @param customDefaultImage Absolute URL to an image.
     * @param encoding           customDefaultImage's (first parameter) encoding
     * @return Fluent interface
     * @see java.net.URLEncoder#encode(String, String)
     */
    public Gravatar setCustomDefaultImage(String customDefaultImage,
                                          String encoding)
            throws UnsupportedEncodingException {
        assert customDefaultImage != null && encoding != null;

        this.customDefaultImage = URLEncoder.encode(customDefaultImage,
                encoding);

        standardDefaultImage = null;
        return this;
    }

    /**
     * Retrieve the custom default image. Please note that this URL is already
     * URL encoded.
     *
     * @return The custom default image
     * @see Gravatar#setCustomDefaultImage(String, String)
     */
    public String getCustomDefaultImage() {
        return customDefaultImage;
    }

    /**
     * Set the default image which will be retrieved when there is no avatar
     * for the given email, when the avatar can't be shown due to the rating
     * or when you enforce the default avatar.
     *
     * @param customDefaultImage Absolute URL to an image. An UTF-8 encoding
     *                           is expected.
     * @return Fluent interface
     */
    public Gravatar setCustomDefaultImage(String customDefaultImage)
            throws UnsupportedEncodingException {
        return setCustomDefaultImage(customDefaultImage, "UTF-8");
    }

    /**
     * Retrieve the gravatar URL for the given email.
     *
     * @param email The email for which the avatar URL should be returned.
     * @return URL to the gravatar.
     */
    public String getUrl(String email) {
        if (email == null) {
            throw new IllegalArgumentException("Email can't be null.");
        }

        String emailHash = DigestUtils.md5Hex(email.trim().toLowerCase());

        boolean firstParameter = true;
        // StringBuilder standard capacity is 16 characters while the minimum
        // url is 63 characters long. The maximum length without customDefaultImage
        // is 91.
        StringBuilder builder = new StringBuilder(91)
                .append(https ? HTTPS_URL : URL)
                .append(emailHash)
                .append(FILE_TYPE_EXTENSION);

        if (size != DEFAULT_SIZE) {
            addParameter(builder, "s", Integer.toString(size), firstParameter);
            firstParameter = false;
        }
        if (forceDefault) {
            addParameter(builder, "f", "y", firstParameter);
            firstParameter = false;
        }
        if (rating != DEFAULT_RATING) {
            addParameter(builder, "r", rating.getKey(), firstParameter);
            firstParameter = false;
        }
        if (customDefaultImage != null) {
            addParameter(builder, "d", customDefaultImage, firstParameter);
        } else if (standardDefaultImage != null) {
            addParameter(builder, "d", standardDefaultImage.getKey(), firstParameter);
        }

        return builder.toString();
    }

    private void addParameter(StringBuilder builder, String key, String value, Boolean firstParameter) {
        if (firstParameter) {
            builder.append("?");
        } else {
            builder.append("&");
        }
        builder.append(key).append("=").append(value);
    }
}
