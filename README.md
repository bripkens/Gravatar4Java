# Gravatar4Java

A small library for the generation of Gravatar URLs.

## Example
    import de.bripkens.gravatar.DefaultImage;
    import de.bripkens.gravatar.Gravatar;
    import de.bripkens.gravatar.Rating;
    
    String gravatarImageURL = new Gravatar()
        .setSize(50)
        .setHttps(true)
        .setRating(Rating.PARENTAL_GUIDANCE_SUGGESTED)
        .setStandardDefaultImage(DefaultImage.MONSTER)
        .getUrl("foobar@example.com");