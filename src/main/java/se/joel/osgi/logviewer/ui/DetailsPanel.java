/**
 * User: Joel Binnquist (joel.binnquist@gmail.com)
 * Date: 2011-11-29
 * Time: 23:02
 */
package se.joel.osgi.logviewer.ui;

import net.miginfocom.swing.MigLayout;
import org.osgi.service.log.LogEntry;

import javax.swing.*;

/**
 * A panel containing details of a log entry.
 */
public class DetailsPanel extends JPanel {

    private JTextField timeTextField;
    private JTextField bundleIdTextField;
    private JTextField bundleLocationTextField;
    private JTextField bundleStateTextField;
    private JTextField serviceTextField;
    private JTextField rawLogStringTextField;
    private JTextArea messageTextArea;
    private JTextArea exceptionTextArea;

    {
        // Time: <time> Bundle ID: <id> Location: <location> 
        // State: <state> Service: <service>
        // Headers: <headers>
        // Message: <message>
        // Exception: <exception>
        setLayout(new MigLayout("",
            "[pref!,right]5[pref!,left]10[pref!,right]5[pref!,left]10[pref!,right]5[grow,fill,left]"));
        createUI();
    }

    private void createUI() {
        add(new JLabel("Time:"));
        timeTextField = new JTextField(15);
        timeTextField.setEditable(false);
        add(timeTextField);

        add(new JLabel("Bundle ID:"));
        bundleIdTextField = new JTextField(5);
        bundleIdTextField.setEditable(false);
        add(bundleIdTextField);

        add(new JLabel("Bundle location:"), "gap para");
        bundleLocationTextField = new JTextField("");
        bundleLocationTextField.setEditable(false);
        add(bundleLocationTextField, "wrap");

        add(new JLabel("Bundle state:"));
        bundleStateTextField = new JTextField(15);
        bundleStateTextField.setEditable(false);
        add(bundleStateTextField);

        add(new JLabel("Service:"));
        serviceTextField = new JTextField("");
        serviceTextField.setEditable(false);
        add(serviceTextField, "span, growx, wrap");

        add(new JLabel("Raw log string:"));
        rawLogStringTextField = new JTextField("");
        add(rawLogStringTextField, "span, growx, wrap");

        add(new JLabel("Message:"));
        messageTextArea = new JTextArea("");
        messageTextArea.setRows(4);
        messageTextArea.setEditable(false);
        messageTextArea.setLineWrap(true);
        JScrollPane messageTextAreaScrollPane = new JScrollPane(messageTextArea);
        add(messageTextAreaScrollPane, "span, growx, wrap");

        add(new JLabel("Exception:"));
        exceptionTextArea = new JTextArea("");
        exceptionTextArea.setRows(4);
        exceptionTextArea.setEditable(false);
        exceptionTextArea.setLineWrap(true);
        JScrollPane exceptionTextAreaScrollPane = new JScrollPane(exceptionTextArea);
        add(exceptionTextAreaScrollPane, "span, growx, wrap");
    }

    public void setLogEntry(LogEntry logEntry) {
        if (logEntry != null) {
            timeTextField.setText(Util.timeToString(logEntry));
            timeTextField.setCaretPosition(0);
            bundleIdTextField.setText(Util.getBundleId(logEntry));
            bundleIdTextField.setCaretPosition(0);
            bundleLocationTextField.setText(Util.getLocation(logEntry));
            bundleLocationTextField.setCaretPosition(0);
            bundleStateTextField.setText(Util.stateToString(logEntry));
            bundleStateTextField.setCaretPosition(0);
            serviceTextField.setText(Util.getService(logEntry));
            serviceTextField.setCaretPosition(0);
            rawLogStringTextField.setText(Util.makeString(logEntry));
            rawLogStringTextField.setCaretPosition(0);
            messageTextArea.setText(logEntry.getMessage());
            messageTextArea.setCaretPosition(0);
            exceptionTextArea.setText(Util.exceptionString(logEntry));
            exceptionTextArea.setCaretPosition(0);
        }
//        logEntry.getTime();
//        logEntry.getBundle().getBundleId();
//        logEntry.getBundle().getLocation();
//        logEntry.getServiceReference().getBundle().getBundleContext().getService(logEntry.getServiceReference());
//        logEntry.getMessage();
//        logEntry.getBundle().getState();
//        logEntry.getBundle().getHeaders();
//        logEntry.getException();
    }
}
