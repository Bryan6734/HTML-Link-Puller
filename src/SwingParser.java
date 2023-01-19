import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class SwingParser implements ActionListener {

    private JFrame mainFrame;
    private JTextArea inputTextArea;
    private JTextArea searchTextArea;
    private JPanel submitPanel;
    private JScrollPane outputScrollPane;
    private JTextArea outputTextArea;
    private int WIDTH=800;
    private int HEIGHT=700;

    public SwingParser(){
        prepareGUI();
    }

    public static void main(String[] args) {
        SwingParser swingParser = new SwingParser();
    }

    private void prepareGUI(){
        mainFrame = new JFrame("Bryan Sukidi's Link Scraper");
        mainFrame.setSize(WIDTH, HEIGHT);
        mainFrame.setLayout(new GridLayout(4, 1));

        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        // On the first row, put the inputtextarea in the middle of the row. Make it so that theres a gray border around it
        inputTextArea = new JTextArea("Enter URL here");
        inputTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        inputTextArea.setLineWrap(true);
        inputTextArea.setWrapStyleWord(true);

        // On the second row, put the searchtextarea in the middle of the row. Make it so that theres a gray border around it
        searchTextArea = new JTextArea("Enter search term here");
        searchTextArea.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        searchTextArea.setLineWrap(true);
        searchTextArea.setWrapStyleWord(true);



        submitPanel = new JPanel();
        submitPanel.setLayout(new FlowLayout());

        outputTextArea = new JTextArea(0, JLabel.CENTER);
        outputTextArea.setEditable(false);
        outputTextArea.setLineWrap(true);

        outputScrollPane = new JScrollPane(outputTextArea);
        outputScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);



        handleEvents();

        mainFrame.add(inputTextArea);
        mainFrame.add(searchTextArea);
        mainFrame.add(submitPanel);
        mainFrame.add(outputScrollPane);
//        mainFrame.add(outputTextArea);

        mainFrame.setVisible(true);

    }

    private void handleEvents(){

        JButton scrapeButton = new JButton("Scrape");
        scrapeButton.setActionCommand("scrape");
        scrapeButton.addActionListener(new SwingParser.ButtonClickListener());
        submitPanel.add(scrapeButton);

    }


    private ArrayList<String> scrapeWebsite(String inputURL, String searchTerm){
        ArrayList<String> urls = new ArrayList<>();

        // "https://www.milton.edu/"
        try{
            URL url = new URL(inputURL);
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(url.openStream())
            );
            String line;

            while ((line = reader.readLine()) != null){
                if (line.contains("href")){
                    try{
                        int beginIndex = line.indexOf("http");
                        int endIndexDouble = line.indexOf("\"", beginIndex);
                        int endIndexSingle = line.indexOf("'", beginIndex);

                        String link;

                        if (endIndexSingle == -1){
                            // if there is no single quote, then it must be double
                            link = line.substring(beginIndex, endIndexDouble);
                        } else if (endIndexDouble == -1) {
                            // if there is no double quote, then it must be a single
                            link = line.substring(beginIndex, endIndexSingle);
                        } else {
                            continue;
                        }

                        if (link.contains(searchTerm)){
                            urls.add(link);
                            System.out.println(link);
                        }


                    } catch(Exception e){
                    }
                }
            }
            reader.close();

        } catch(Exception ex){

        }

        return urls;
    }



    @Override
    public void actionPerformed(ActionEvent e) {

    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("scrape")) {
                String my_link = inputTextArea.getText();
                outputTextArea.setText("");

                String searchTerm = searchTextArea.getText();

                ArrayList<String> links = scrapeWebsite(my_link, searchTerm);
                for (String link : links){
                    outputTextArea.append(link + "\n");
                }
            }
        }
    }
}
