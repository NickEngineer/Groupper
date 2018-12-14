package nure.nick.labs.lab1.element;

public class Group {

    private String id = null;
    private String name = null;
    private String descript = null;

    public Group() {
    }

    public Group(String id, String name, String descript) {
        this.id = id;
        this.name = name;
        this.descript = descript;
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

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }
}
