public class Person {
    private String id, name, category;
    private int birth_y, level;

    Person() {}
    Person(String i, String n, int b, String c, int l) {
        setId(i);
        setName(n);
        setBirthYear(b);
        setCategory(c);
        setLevel(l);
    }

    public void setId(String i) {
        id = i;
    }
    public String getId() {
        return id;
    }

    public void setName(String n) {
        name = n;
    }
    public String getName() {
        return name;
    }

    public void setBirthYear(int b) {
        birth_y = b;
    }
    public int getBirthYear() { return birth_y; }

    public void setCategory(String c) {
        category = c;
    }
    public String getCategory() {
        return category;
    }

    public void setLevel(int l) { level = l; }
    public int getLevel() { return level; }
}
