/**
 * Copyright 2011-2014 Ben Ripkens
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

import org.junit.Before;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import static org.junit.Assert.*;

/**
 * @author Ben Ripkens <bripkens.dev@gmail.com>
 */
public class GravatarTest {
    private static final String EMAIL = "test@example.com";
    private static final String EMAIL_HASH = "55502f40dc8b7c769880b10874abc9d0";
    private Gravatar g;

    @Before
    public void setup() {
        g = new Gravatar();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullEmail() {
        g.getUrl(null);
    }

    @Test
    public void testDefaultUrl() {
        assertEquals(Gravatar.URL + EMAIL_HASH +
                Gravatar.FILE_TYPE_EXTENSION, g.getUrl(EMAIL));
    }

    @Test
    public void testCustomSize() {
        int size = 40;
        g.setSize(size);
        assertEquals(Gravatar.URL + EMAIL_HASH + Gravatar.FILE_TYPE_EXTENSION +
                "?s=" + size, g.getUrl(EMAIL));
    }

    @Test
    public void testDefaultSize() {
        g.setSize(Gravatar.DEFAULT_SIZE);
        assertEquals(Gravatar.URL + EMAIL_HASH +
                Gravatar.FILE_TYPE_EXTENSION, g.getUrl(EMAIL));
    }

    @Test
    public void testInvalidSizeTooSmall() {
        try {
            g.setSize(0);
            fail("Size is too small");
        } catch (AssertionError e) {
            // expected is not working for errors
        }
    }

    @Test
    public void testInvalidSizeTooBig() {
        try {
            g.setSize(2049);
            fail("Size is too big");
        } catch (AssertionError e) {
            // expected is not working for errors
        }
    }

    @Test
    public void testDefaultRating() {
        g.setRating(Gravatar.DEFAULT_RATING);
        assertEquals(Gravatar.URL + EMAIL_HASH +
                Gravatar.FILE_TYPE_EXTENSION, g.getUrl(EMAIL));
    }

    @Test
    public void testCustomRating() {
        Rating rating = Rating.RESTRICTED;
        g.setRating(rating);
        assertEquals(Gravatar.URL + EMAIL_HASH + Gravatar.FILE_TYPE_EXTENSION +
                "?r=" + rating.getKey(), g.getUrl(EMAIL));
    }

    @Test
    public void testNullRating() {
        try {
            g.setRating(null);
            fail("Rating is null");
        } catch (AssertionError e) {
            // expected is not working for errors
        }
    }

    @Test
    public void testHTTPSConnection() {
        g.setHttps(true);
        assertEquals(Gravatar.HTTPS_URL + EMAIL_HASH +
                Gravatar.FILE_TYPE_EXTENSION, g.getUrl(EMAIL));
    }

    @Test
    public void testForceDefaultImage() {
        g.setForceDefault(true);
        assertEquals(Gravatar.URL + EMAIL_HASH + Gravatar.FILE_TYPE_EXTENSION +
                "?f=y", g.getUrl(EMAIL));
    }

    @Test
    public void testNullStandardDefaultImage() {
        try {
            g.setStandardDefaultImage(null);
            fail("Standard default image is null");
        } catch (AssertionError e) {
            // expected is not working for errors
        }
    }

    @Test
    public void testStandardDefaultImage() {
        DefaultImage di = DefaultImage.MONSTER;
        g.setStandardDefaultImage(di);
        assertEquals(Gravatar.URL + EMAIL_HASH + Gravatar.FILE_TYPE_EXTENSION +
                "?d=" + di.getKey(), g.getUrl(EMAIL));
    }

    @Test
    public void testNullCustomDefaultImage() {
        try {
            g.setStandardDefaultImage(null);
            fail("Standard default image is null");
        } catch (AssertionError e) {
            // expected is not working for errors
        }
    }

    @Test
    public void testCustomDefaultImage() throws UnsupportedEncodingException {
        String img = "http://www.google.com";
        g.setCustomDefaultImage(img);
        assertEquals(Gravatar.URL + EMAIL_HASH + Gravatar.FILE_TYPE_EXTENSION +
                "?d=" + URLEncoder.encode(img, "UTF-8"), g.getUrl(EMAIL));
    }

    @Test
    public void testCustomAndStandardDefaultImageSwitch() throws Exception {
        String img = "http://www.google.com";
        g.setCustomDefaultImage(img);

        DefaultImage di = DefaultImage.MONSTER;
        g.setStandardDefaultImage(di);
        assertEquals(Gravatar.URL + EMAIL_HASH + Gravatar.FILE_TYPE_EXTENSION +
                "?d=" + di.getKey(), g.getUrl(EMAIL));

        g.setCustomDefaultImage(img);
        assertEquals(Gravatar.URL + EMAIL_HASH + Gravatar.FILE_TYPE_EXTENSION +
                "?d=" + URLEncoder.encode(img, "UTF-8"), g.getUrl(EMAIL));
    }

    @Test
    public void testCombination() {
        int size = 125;
        g.setSize(size);

        DefaultImage di = DefaultImage.MONSTER;
        g.setStandardDefaultImage(di);

        Rating r = Rating.ADULT_ONLY;
        g.setRating(r);

        g.setHttps(true);
        g.setForceDefault(true);

        String url = g.getUrl(EMAIL);

        assertTrue(url.startsWith(Gravatar.HTTPS_URL + EMAIL_HASH +
                Gravatar.FILE_TYPE_EXTENSION + "?"));

        assertTrue(url.contains("s=" + size + "&") ||
                url.endsWith("s=" + size));
        assertTrue(url.contains("r=" + r.getKey() + "&") ||
                url.endsWith("r=" + r.getKey()));
        assertTrue(url.contains("d=" + di.getKey() + "&") ||
                url.endsWith("d=" + di.getKey()));
        assertTrue(url.contains("f=y&") ||
                url.endsWith("f=y"));

        assertEquals(url, g.getUrl(EMAIL));
    }
}
