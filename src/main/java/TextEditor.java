import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.awt.*;
import java.io.*;

public class TextEditor extends JFrame {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 400;

    private static final String SAVE_BUTTON_ICON = "./resources/icons/saveButtonIcon.png";
    private static final String OPEN_BUTTON_ICON = "./resources/icons/openButtonIcon.png";
    private static final String START_SEARCH_BUTTON_ICON = "./resources/icons/startSearchButtonIcon.png";
    private static final String PREVIOUS_MATCH_BUTTON_ICON = "./resources/icons/previousMatchButtonIcon.png";
    private static final String NEXT_MATCH_BUTTON_ICON = "./resources/icons/nextMatchButtonIcon.png";

    private static final String SEARCH_NOT_STARTED = "You should click search button";
    private static final String SEARCH_FIELD_TOOLTIP = "Search";
    private static final int SEARCH_FIELD_COLUMNS = 35;

    private JPanel topPanel;
    private JTextField searchField;
    private JButton saveButton;
    private JButton openButton;
    private JButton startSearchButton;
    private JButton previousMatchButton;
    private JButton nextMatchButton;
    private JCheckBox useRegExCheckbox;
    private JFileChooser fileChooser;
    private JMenuBar menuBar;
    private JMenu menuFile;
    private JMenuItem menuOpen;
    private JMenuItem menuSave;
    private JMenuItem menuExit;
    private JMenu menuSearch;
    private JMenuItem menuStartSearch;
    private JMenuItem menuPreviousMatch;
    private JMenuItem menuNextMatch;
    private JMenuItem menuUseRegExp;
    private JTextArea textArea;
    private JScrollPane scrollPane;

    private TextHighliter textHighliter;
    private boolean useRegExp = false;

    public TextEditor() {
        initUI();
    }

    private void initUI() {
        initTopPanel();
        initTextArea();
        initMenu();
        initListeners();
        setNames();

        setTitle("TextEditor");
        setLayout(new BorderLayout());
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        setSize(new Dimension(WIDTH, HEIGHT));
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private void initTopPanel() {
        topPanel = new JPanel();
        searchField = new JTextField(SEARCH_FIELD_COLUMNS);
        saveButton = new JButton();
        saveButton.setIcon(new ImageIcon(SAVE_BUTTON_ICON));
        openButton = new JButton();
        openButton.setIcon(new ImageIcon(OPEN_BUTTON_ICON));
        startSearchButton = new JButton();
        startSearchButton.setIcon(new ImageIcon(START_SEARCH_BUTTON_ICON));
        previousMatchButton = new JButton();
        previousMatchButton.setIcon(new ImageIcon(PREVIOUS_MATCH_BUTTON_ICON));
        nextMatchButton = new JButton();
        nextMatchButton.setIcon(new ImageIcon(NEXT_MATCH_BUTTON_ICON));
        useRegExCheckbox = new JCheckBox("Use regex");
        fileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

        topPanel.setLayout(new FlowLayout());
        topPanel.add(saveButton);
        topPanel.add(openButton);
        topPanel.add(searchField);
        topPanel.add(startSearchButton);
        topPanel.add(previousMatchButton);
        topPanel.add(nextMatchButton);
        topPanel.add(useRegExCheckbox);
        add(fileChooser);

    }

    private void initMenu() {

        menuBar = new JMenuBar();

        menuFile = new JMenu("File");
        menuOpen = new JMenuItem("Open");
        menuSave = new JMenuItem("Save");
        menuExit = new JMenuItem("Exit");

        menuFile.add(menuOpen);
        menuFile.add(menuSave);
        menuFile.addSeparator();
        menuFile.add(menuExit);
        menuBar.add(menuFile);

        menuSearch = new JMenu("Search");
        menuStartSearch = new JMenuItem("Start search");
        menuPreviousMatch = new JMenuItem("Previous search");
        menuNextMatch = new JMenuItem("Next match");
        menuUseRegExp = new JMenuItem("Use regular expressions");

        menuSearch.add(menuStartSearch);
        menuSearch.add(menuPreviousMatch);
        menuSearch.add(menuNextMatch);
        menuSearch.add(menuUseRegExp);
        menuBar.add(menuSearch);

        setJMenuBar(menuBar);
    }

    private void initTextArea() {
        textArea = new JTextArea();
        textArea.setLineWrap(true);
        scrollPane = new JScrollPane(textArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    }

    private void initListeners() {
        openButton.addActionListener(actionEvent -> {
            fileChooser.showOpenDialog(this);
            File selectedFile = fileChooser.getSelectedFile();
            if (selectedFile != null) {
                try (FileReader reader = new FileReader(selectedFile)) {
                    textArea.read(reader, null);
                } catch (IOException e) {
                    textArea.setText("");
                    e.printStackTrace();
                }
            }
        });

        saveButton.addActionListener(actionEvent -> {
            fileChooser.showSaveDialog(this);
            File selectedFile = fileChooser.getSelectedFile();
            try (FileWriter writer = new FileWriter(selectedFile)) {
                textArea.write(writer);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


        startSearchButton.addActionListener(actionEvent -> {
            String toSearch = searchField.getText();
            textHighliter = new TextHighliter(this, toSearch);
            new SearchWorker(textHighliter).execute();
        });

        previousMatchButton.addActionListener(actionEvent -> {
            if (textHighliter != null) {
                textHighliter.showPrevious();
            } else {
                JOptionPane.showMessageDialog(this, SEARCH_NOT_STARTED);
            }
        });

        nextMatchButton.addActionListener(actionEvent -> {
            if (textHighliter != null) {
                textHighliter.showNext();
            } else {
                JOptionPane.showMessageDialog(this, SEARCH_NOT_STARTED);
            }
        });
        useRegExCheckbox.addChangeListener(changeEvent -> {
            useRegExp = !useRegExp;
        });

        menuOpen.addActionListener(openButton.getActionListeners()[0]);
        menuSave.addActionListener(saveButton.getActionListeners()[0]);
        menuExit.addActionListener(event -> dispose());
        menuStartSearch.addActionListener(startSearchButton.getActionListeners()[0]);
        menuPreviousMatch.addActionListener(previousMatchButton.getActionListeners()[0]);
        menuNextMatch.addActionListener(nextMatchButton.getActionListeners()[0]);
        menuUseRegExp.addActionListener(actionEvent -> {
            useRegExCheckbox.setSelected(!useRegExp);
            if (useRegExp) {
                menuUseRegExp.setText("Don't use RegExp");
            } else {
                menuUseRegExp.setText("Use RegExp");
            }
        });

    }

    private void setNames() {
        textArea.setName("TextArea");
        openButton.setName("OpenButton");
        saveButton.setName("SaveButton");
        searchField.setName("SearchField");
        startSearchButton.setName("StartSearchButton");
        previousMatchButton.setName("PreviousMatchButton");
        nextMatchButton.setName("NextMatchButton");
        useRegExCheckbox.setName("UseRegExCheckbox");
        fileChooser.setName("FileChooser");
        menuFile.setName("MenuFile");
        menuOpen.setName("MenuOpen");
        menuSave.setName("MenuSave");
        menuExit.setName("MenuExit");
        menuSearch.setName("MenuSearch");
        menuStartSearch.setName("MenuStartSearch");
        menuPreviousMatch.setName("MenuPreviousMatch");
        menuNextMatch.setName("MenuNextMatch");
        menuUseRegExp.setName("MenuUseRegExp");


        scrollPane.setName("ScrollPane");

    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public boolean isUseRegExp() {
        return useRegExp;
    }
}