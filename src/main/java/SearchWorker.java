import javax.swing.*;

public class SearchWorker extends SwingWorker<Boolean, Object> {

    private TextHighliter textHighliter;

    public SearchWorker(TextHighliter textHighliter) {
        this.textHighliter = textHighliter;
    }

    @Override
    protected void done() {
        super.done();
    }

    @Override
    protected Boolean doInBackground() throws Exception {
        textHighliter.findMatches();
        return true;
    }
}
