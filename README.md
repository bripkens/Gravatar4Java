# Gravatar4Java

A small library for the generation of Gravatar URLs.

## Usage
Gravatar4Java is a Maven project and available in Maven Central. Just add the Gravatar4Java dependency to your pom.xml and you are done!

    <dependency>
        <groupId>de.bripkens</groupId> 
        <artifactId>gravatar4java</artifactId> 
        <version>1.1</version> 
    </dependency>


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


## Spring example

    <?xml version="1.0" encoding="UTF-8"?>
    <beans xmlns="http://www.springframework.org/schema/beans"
           xmlns:p="http://www.springframework.org/schema/p">
     
        <!-- Gravatar -->
        <bean id="gravatar"
              class="de.bripkens.gravatar.Gravatar"
              p:size="75"
              p:rating="GENERAL_AUDIENCE"
              p:standardDefaultImage="MONSTER" />
     
    </beans>