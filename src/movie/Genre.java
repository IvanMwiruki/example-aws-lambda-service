package movie;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the genres a movie can be.
 */
public enum Genre {

    ACTION("Action"),
    //ADULT("Adult"),
    ADVENTURE("Adventure"),
    ANIMATION("Animation"),
    BIOGRAPHY("Biography"),
    COMEDY("Comedy"),
    CRIME("Crime"),
    DOCUMENTARY("Documentary"),
    DRAMA("Drama"),
    FAMILY("Family"),
    FANTASY("Fantasy"),
    FILMNOIR("Film-Noir"),
    GAMESHOW("Game-Show"),
    HISTORY("History"),
    HORROR("Horror"),
    MUSIC("Music"),
    MUSICAL("Musical"),
    MYSTERY("Mystery"),
    NEWS("News"),
    REALITYTV("Reality-TV"),
    ROMANCE("Romance"),
    SCIFI("Sci-Fi"),
    SHORT("Short"),
    SPORT("Sport"),
    TALKSHOW("Talk-Show"),
    THRILLER("Thriller"),
    WAR("War"),
    WESTERN("Western");

    private final String name;

    Genre(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Creates a list of all the genres.
     *
     * @return a list of all the genres
     */
    public static List<Genre> listGenres() {
        return Arrays.stream(values())
                .collect(Collectors.toList());
    }
}
