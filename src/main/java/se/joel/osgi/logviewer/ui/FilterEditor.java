/**
 * User: Joel Binnquist (joel.binnquist@gmail.com)
 * Date: 2011-11-29
 * Time: 23:02
 */
package se.joel.osgi.logviewer.ui;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * A panel containing a filter editor. The filter is a regular expression.
 */
public class FilterEditor extends JPanel {
    private Pattern pattern;
    private String filterString;
    private Set<FilterChangedListener> listeners = new HashSet<FilterChangedListener>();
    private JTextField filterTextField;

    {
        setLayout(new MigLayout("", "[pref!][grow,fill]", null));
        createUI();
    }

    private void createUI() {
        add(new JLabel("Filter:"));
        filterTextField = new JTextField("");
        filterTextField.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent caretEvent) {
                try {
                    pattern = Pattern.compile(filterTextField.getText());
                    filterTextField.setBackground(Color.WHITE);
                    notifyListeners();
                } catch (PatternSyntaxException e) {
                    filterTextField.setBackground(Color.RED);
                }
            }
        });
        add(filterTextField);
    }

    public void addListener(FilterChangedListener listener) {
        listeners.add(listener);
    }

    public void removeListener(FilterChangedListener listener) {
        listeners.remove(listener);
    }

    public Pattern getPattern() {
        return pattern;
    }

    public interface FilterChangedListener {
        void onFilterChanged(FilterEditor source);
    }

    private void notifyListeners() {
        for (FilterChangedListener listener : listeners) {
            listener.onFilterChanged(this);
        }
    }
}
