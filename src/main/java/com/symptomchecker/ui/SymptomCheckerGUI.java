package com.symptomchecker.ui;

import com.symptomchecker.model.SymptomResult;
import com.symptomchecker.service.SymptomCheckerService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Main GUI class for the Symptom Checker application
 */
public class SymptomCheckerGUI extends JFrame {
    private SymptomCheckerService service;
    private List<JCheckBox> symptomCheckboxes;
    private JPanel resultsPanel;
    private JScrollPane resultsScrollPane;

    public SymptomCheckerGUI() {
        service = new SymptomCheckerService();
        symptomCheckboxes = new ArrayList<>();
        initializeGUI();
    }

    private void initializeGUI() {
        setTitle("AI Symptom Checker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(245, 245, 245));

        // Main container
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 24, 20, 24));
        mainPanel.setBackground(Color.WHITE);

        // Header
        JPanel headerPanel = createHeaderPanel();
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Symptoms panel
        JPanel symptomsPanel = createSymptomsPanel();
        mainPanel.add(symptomsPanel, BorderLayout.CENTER);

        // Results panel
        JPanel resultsContainer = createResultsPanel();
        mainPanel.add(resultsContainer, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Footer
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);

        pack();
        setSize(800, 700);
        setLocationRelativeTo(null);
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("AI Symptom Checker");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel, BorderLayout.NORTH);

        JLabel subtitleLabel = new JLabel(
            "<html>Select your symptoms below and click the button to see possible matching conditions. " +
            ".</html>");
        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitleLabel.setBorder(new EmptyBorder(10, 0, 10, 0));
        panel.add(subtitleLabel, BorderLayout.CENTER);

        // Info box
        JPanel infoBox = new JPanel(new BorderLayout());
        infoBox.setBorder(new TitledBorder("How it works (very basic):"));
        infoBox.setBackground(new Color(227, 242, 253));
        infoBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            new EmptyBorder(12, 14, 12, 14)
        ));

        JLabel infoLabel = new JLabel(
            "<html>- Counts how many of your symptoms match simple condition lists<br>" +
            "- If 3 or more symptoms match, we show that condition (up to 3)<br>" +
            "- Shows a quick percent = matched / total × 100</html>");
        infoLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        infoBox.add(infoLabel, BorderLayout.CENTER);

        panel.add(infoBox, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createSymptomsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            new EmptyBorder(12, 16, 12, 16)
        ));
        panel.setBackground(new Color(250, 250, 250));

        JLabel titleLabel = new JLabel("Choose Symptoms (15)");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(titleLabel, BorderLayout.NORTH);

        // Two columns of checkboxes
        JPanel columnsPanel = new JPanel(new GridLayout(0, 2, 20, 6));
        columnsPanel.setBackground(new Color(250, 250, 250));

        List<String> allSymptoms = service.getAllSymptoms();
        for (String symptom : allSymptoms) {
            JCheckBox checkbox = new JCheckBox(symptom);
            checkbox.setFont(new Font("Arial", Font.PLAIN, 14));
            checkbox.setBackground(new Color(250, 250, 250));
            symptomCheckboxes.add(checkbox);
            columnsPanel.add(checkbox);
        }

        JScrollPane scrollPane = new JScrollPane(columnsPanel);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(new Dimension(0, 200));
        panel.add(scrollPane, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        buttonPanel.setBackground(new Color(250, 250, 250));

        JButton checkButton = new JButton("Check Symptoms");
        checkButton.setFont(new Font("Arial", Font.PLAIN, 14));
        checkButton.setPreferredSize(new Dimension(160, 36));
        checkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                checkSymptoms();
            }
        });

        JButton clearButton = new JButton("Clear");
        clearButton.setFont(new Font("Arial", Font.PLAIN, 14));
        clearButton.setPreferredSize(new Dimension(120, 36));
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearAll();
            }
        });

        buttonPanel.add(checkButton);
        buttonPanel.add(clearButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createResultsPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.GRAY),
            new EmptyBorder(12, 14, 12, 14)
        ));
        container.setBackground(Color.WHITE);

        JLabel titleLabel = new JLabel("Results");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        container.add(titleLabel, BorderLayout.NORTH);

        resultsPanel = new JPanel();
        resultsPanel.setLayout(new BoxLayout(resultsPanel, BoxLayout.Y_AXIS));
        resultsPanel.setBackground(Color.WHITE);
        resultsPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        JLabel initialLabel = new JLabel("Select symptoms and click \"Check Symptoms\".");
        initialLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        resultsPanel.add(initialLabel);

        resultsScrollPane = new JScrollPane(resultsPanel);
        resultsScrollPane.setBorder(null);
        resultsScrollPane.setPreferredSize(new Dimension(0, 200));
        container.add(resultsScrollPane, BorderLayout.CENTER);

        return container;
    }

    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 24, 10, 24));
        panel.setBackground(Color.WHITE);

        JLabel disclaimerLabel = new JLabel(
            "<html><strong>Disclaimer:</strong> </html>");
        disclaimerLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        disclaimerLabel.setForeground(new Color(85, 85, 85));
        panel.add(disclaimerLabel, BorderLayout.CENTER);

        return panel;
    }

    private void checkSymptoms() {
        List<String> selectedSymptoms = getSelectedSymptoms();

        if (selectedSymptoms.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please select at least one symptom",
                "No Symptoms Selected",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        List<SymptomResult> results = service.checkSymptoms(selectedSymptoms);
        renderResults(results, selectedSymptoms);
    }

    private List<String> getSelectedSymptoms() {
        List<String> selected = new ArrayList<>();
        for (JCheckBox checkbox : symptomCheckboxes) {
            if (checkbox.isSelected()) {
                selected.add(checkbox.getText());
            }
        }
        return selected;
    }

    private void renderResults(List<SymptomResult> results, List<String> selectedSymptoms) {
        resultsPanel.removeAll();

        if (results.isEmpty()) {
            JPanel noResultPanel = createResultItemPanel(
                "No strong matches found",
                "Try selecting more symptoms or consult a medical professional.",
                null,
                selectedSymptoms
            );
            resultsPanel.add(noResultPanel);
        } else {
            for (SymptomResult result : results) {
                String details = "Matched " + result.getMatchedCount() +
                    " of " + result.getTotalSymptoms() + " symptoms.";
                JPanel itemPanel = createResultItemPanel(
                    result.getConditionName() + " (" + result.getPercentMatch() + "% match)",
                    details,
                    result.getAdvice(),
                    selectedSymptoms
                );
                resultsPanel.add(itemPanel);
                resultsPanel.add(Box.createVerticalStrut(10));
            }
        }

        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    private JPanel createResultItemPanel(String title, String details, String advice, List<String> selectedSymptoms) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 230, 201)),
            new EmptyBorder(10, 12, 10, 12)
        ));
        panel.setBackground(new Color(232, 245, 233));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(titleLabel);

        JLabel detailsLabel = new JLabel(details);
        detailsLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        detailsLabel.setBorder(new EmptyBorder(6, 0, 0, 0));
        panel.add(detailsLabel);

        if (advice != null) {
            JLabel adviceLabel = new JLabel("Advice: " + advice);
            adviceLabel.setFont(new Font("Arial", Font.PLAIN, 13));
            adviceLabel.setBorder(new EmptyBorder(10, 0, 0, 0));
            panel.add(adviceLabel);
        }

        JLabel debugLabel = new JLabel("Debug: selected = " + String.join(", ", selectedSymptoms));
        debugLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        debugLabel.setForeground(new Color(102, 102, 102));
        debugLabel.setBorder(new EmptyBorder(8, 0, 0, 0));
        panel.add(debugLabel);

        return panel;
    }

    private void clearAll() {
        for (JCheckBox checkbox : symptomCheckboxes) {
            checkbox.setSelected(false);
        }
        resultsPanel.removeAll();
        JLabel initialLabel = new JLabel("Select symptoms and click \"Check Symptoms\".");
        initialLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        resultsPanel.add(initialLabel);
        resultsPanel.revalidate();
        resultsPanel.repaint();
    }

    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SymptomCheckerGUI().setVisible(true);
            }
        });
    }
}

