import java.time.LocalDate;
import java.util.Date;

public class LookUpHistory {
    private LocalDate lookUpDate;
    private String lookUpWord;
    private int count;

    public LocalDate getLookUpDate() {
        return lookUpDate;
    }

    public void setLookUpDate(LocalDate lookUpDate) {
        this.lookUpDate = lookUpDate;
    }

    public String getLookUpWord() {
        return lookUpWord;
    }

    public void setLookUpWord(String lookUpWord) {
        this.lookUpWord = lookUpWord;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public LookUpHistory(LocalDate lookUpDate, String lookUpWord, int count) {
        this.lookUpDate = lookUpDate;
        this.lookUpWord = lookUpWord;
        this.count = count;
    }

    @Override
    public String toString() {
        return lookUpWord;
    }


}
