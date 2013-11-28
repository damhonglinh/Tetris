package model;

/**
 *
 * @author Dam Linh <damhonglinh@gmail.com>
 */
public class Score {

    private String id;
    private String name;
    private long score;

    public Score(String id, String name, long score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    @Override
    public String toString() {
        return id + " - " + name + " - " + score;
    }
}
