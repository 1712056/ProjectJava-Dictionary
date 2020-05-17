public class AnhVietList {
    private String word;
    private String meaning;

    public AnhVietList() {

    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    @Override
    public String toString() {
        return "AnhVietList{" +
                "word='" + word + '\'' +
                ", meaning='" + meaning + '\'' +
                '}';
    }

    public AnhVietList(String word, String meaning) {
        this.word = word;
        this.meaning = meaning;
    }

}
