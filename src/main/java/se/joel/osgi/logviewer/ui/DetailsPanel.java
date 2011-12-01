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
    private JTextArea messageTextArea;
    private JTextField rawLogStringTextField;

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
        JScrollPane messageTextAreaScrollPane = new JScrollPane(messageTextArea);
        add(messageTextAreaScrollPane, "span, growx, wrap");
    }

    public void setLogEntry(LogEntry logEntry) {
        if (logEntry != null) {
            timeTextField.setText(Util.timeToString(logEntry));
            bundleIdTextField.setText(Util.getBundleId(logEntry));
            bundleLocationTextField.setText(Util.getLocation(logEntry));
            bundleStateTextField.setText(Util.stateToString(logEntry));
            serviceTextField.setText(Util.getService(logEntry));
            rawLogStringTextField.setText(Util.makeString(logEntry));
            messageTextArea.setText(logEntry.getMessage());
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
