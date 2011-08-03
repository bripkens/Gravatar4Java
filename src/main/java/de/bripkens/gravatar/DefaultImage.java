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

/**
 * A default avatar can be received in case there is no avatar for an email
 * address, when the avatar is not appropriate for the audience (due to the
 * rating) or when you force default avatar retrieval.
 *
 * Default image descriptions were taken from the Gravatar website.
 *
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public enum DefaultImage {
    /**
     * Return an HTTP 404 error.
     */
    HTTP_404("404"),

    /**
     * A simple, cartoon-style silhouetted outline of a person
     * (does not vary by email hash).
     */
    MYSTERY_MAN("mm"),

    /**
     *  A geometric pattern based on an email hash.
     */
    IDENTICON("identicon"),

    /**
     * A generated 'monster' with different colors, faces, etc.
     */
    MONSTER("monsterid"),

    /**
     * Generated faces with differing features and backgrounds
     */
    WAVATAR("wavatar"),

    /**
     * Awesome generated, 8-bit arcade-style pixelated faces.
     */
    RETRO("retro");

    private final String key;

    private DefaultImage(String key) {
        this.key = key;
    }

    /**
     * Retrieve the query parameter value which indicates the desired default
     * image.
     *
     * @return Gravatar default image query parameter value
     */
    String getKey() {
        return key;
    }
}
