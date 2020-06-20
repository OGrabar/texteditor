import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextHighliter {

    private TextEditor textEditor;
    private String toSearch;
    private List<Integer> startIndexes = new ArrayList<>();
    private List<Integer> endIndexes = new ArrayList<>();
    private int current;

    public TextHighliter(TextEditor textEditor, String toSearch) {
        this.textEditor = textEditor;
        this.toSearch = toSearch;
    }

    public void findMatches() {
        String text = textEditor.getTextArea().getText();
        if (textEditor.isUseRegExp()) {
            Pattern pattern = Pattern.compile(toSearch);
            Matcher matcher = pattern.matcher(text);
            while (matcher.find()) {
                startIndexes.add(matcher.start());
                endIndexes.add(matcher.end());
            }
        } else {
            int lastIndex = 0;
            while (true) {
                lastIndex = text.indexOf(toSearch, lastIndex);
                if (lastIndex != -1) {
                    startIndexes.add(lastIndex);
                    endIndexes.add(lastIndex + toSearch.length());
                    lastIndex++;
                } else {
                    break;
                }
            }
        }
        showMatch();
    }

    public void showPrevious() {
        if (startIndexes.size() > 0) {
            current = (current == 0) ? startIndexes.size() - 1 : --current;
            showMatch();
        }
    }

    public void showNext() {
        if (startIndexes.size() > 0) {
            current = (current == startIndexes.size() - 1) ? 0 : ++current;
            showMatch();
        }
    }

    private void showMatch() {
        int start = startIndexes.get(current);
        int end = endIndexes.get(current);
        textEditor.getTextArea().setCaretPosition(end);
        textEditor.getTextArea().select(start, end);
        textEditor.getTextArea().grabFocus();
    }

}
