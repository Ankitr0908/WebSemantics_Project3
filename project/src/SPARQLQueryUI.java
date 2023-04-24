import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

public class SPARQLQueryUI extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;

    private JTextField txtOWLFile;
    private JTextArea txtQuery;
    private JTextArea txtResult;
    private JButton btnChooseOWL;
    private JButton btnExecute;

    public SPARQLQueryUI() {
        super("SPARQL Query UI");

        // Create the main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        // Create the label and text field for the OWL file path
        JLabel lblOWLFile = new JLabel("OWL file:");
        lblOWLFile.setBounds(10, 10, 100, 20);
        mainPanel.add(lblOWLFile);

        txtOWLFile = new JTextField();
        txtOWLFile.setBounds(120, 10, 300, 20);
        mainPanel.add(txtOWLFile);

        btnChooseOWL = new JButton("Choose OWL");
        btnChooseOWL.setBounds(430, 10, 120, 20);
        btnChooseOWL.addActionListener(this);
        mainPanel.add(btnChooseOWL);

        // Create the label and text area for the SPARQL query
        JLabel lblQuery = new JLabel("SPARQL query:");
        lblQuery.setBounds(10, 40, 100, 20);
        mainPanel.add(lblQuery);

        txtQuery = new JTextArea();
        txtQuery.setLineWrap(true);
        txtQuery.setWrapStyleWord(true);
        JScrollPane scrollQuery = new JScrollPane(txtQuery);
        scrollQuery.setBounds(120, 40, 430, 100);
        mainPanel.add(scrollQuery);

        // Create the label and text area for the query results
        JLabel lblResult = new JLabel("Results:");
        lblResult.setBounds(10, 150, 100, 20);
        mainPanel.add(lblResult);

        txtResult = new JTextArea();
        txtResult.setLineWrap(true);
        txtResult.setWrapStyleWord(true);
        txtResult.setEditable(false);
        JScrollPane scrollResult = new JScrollPane(txtResult);
        scrollResult.setBounds(120, 150, 430, 200);
        mainPanel.add(scrollResult);

        // Create the button for executing the query
        btnExecute = new JButton("Execute Query");
        btnExecute.setBounds(10, 360, 120, 20);
        btnExecute.addActionListener(this);
        mainPanel.add(btnExecute);

                // Add the main panel to the frame
                getContentPane().add(mainPanel);

                // Set the size and center the window
                setSize(570, 430);
                setLocationRelativeTo(null);
                setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            }
        
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == btnChooseOWL) {
                    // Open a file chooser dialog for selecting the OWL file
                    JFileChooser fileChooser = new JFileChooser();
                    FileFilter filter = new FileNameExtensionFilter("OWL files", "owl");
                    fileChooser.setFileFilter(filter);
                    int result = fileChooser.showOpenDialog(this);
                    if (result == JFileChooser.APPROVE_OPTION) {
                        File selectedFile = fileChooser.getSelectedFile();
                        txtOWLFile.setText(selectedFile.getAbsolutePath());
                    }
                } else if (e.getSource() == btnExecute) {
                    // Get the OWL file path and the SPARQL query text
                    String owlFilePath = txtOWLFile.getText();
                    String sparqlQuery = txtQuery.getText();
        
                    // Load the OWL file into a Jena model
                    Model model = ModelFactory.createDefaultModel();
                    model.read(owlFilePath);
        
                    // Create the query and execute it against the model
                    Query query = QueryFactory.create(sparqlQuery);
                    try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
                        // Get the query results and format them as text
                        org.apache.jena.query.ResultSet results = qexec.execSelect();
                        String resultText = ResultSetFormatter.asText(results, query);
        
                        // Set the text of the result text area
                        txtResult.setText(resultText);
                    }
                }
            }
        
            public static void main(String[] args) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        new SPARQLQueryUI().setVisible(true);
                    }
                });
            }
        }
        
