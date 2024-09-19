import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.*;

public class GradeCalculatorGUI extends JFrame{
    private JTextField numSubjectsFiels;
    private JTextField[] gradeFields;
    private JPanel gradesPanel;
    private JLabel resultLable;
    private JScrollPane scrollPane;
    private Color defaultLabelColor;

    public GradeCalculatorGUI(){
        setTitle("Custom Layout Grade Calculator");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        //Top Panel for Number of subjects
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        topPanel.add(new JLabel("Number of subject"));
        numSubjectsFiels = createStyledTextField("Enter number of subject");
        topPanel.add(numSubjectsFiels);
        JButton setSubjectsButton = new JButton("set Subject");
        topPanel.add(setSubjectsButton);
        add(topPanel, BorderLayout.NORTH);

        //Middle panel for grades input (with JScrollPane)
        gradesPanel = new JPanel();
        gradesPanel.setLayout(new GridLayout(0,2,10,10));
        scrollPane = new JScrollPane(gradesPanel);
        scrollPane.setPreferredSize(new Dimension(450,200));
        add(scrollPane, BorderLayout.CENTER);

        //Bottom panel for button and result
        JPanel bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel,BoxLayout.Y_AXIS));
        resultLable = new JLabel("Enter grade and press Calculate", SwingConstants.CENTER);
        resultLable.setFont(new Font("Arial", Font.BOLD, 14));
        defaultLabelColor = resultLable.getForeground();
        bottomPanel.add(resultLable);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER,10,10));
        JButton calculateButton = new JButton("Calculate");
        JButton clearButton = new JButton("Clear");
        buttonPanel.add(calculateButton);
        buttonPanel.add(clearButton);
        bottomPanel.add(buttonPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        //set subject button action
        setSubjectsButton.addActionListener(e -> setNumberOfSubjects());

        //Calculate button action
        calculateButton.addActionListener(e -> calculateGrade());

        //clear button action
        clearButton.addActionListener(e -> clearFields());
    }

    private JTextField createStyledTextField(String placeholderText){
        JTextField textField = new JTextField(10);
        textField.setFont(new Font("Arial", Font.PLAIN,16));
        textField.setForeground(Color.GRAY);
        textField.setText(placeholderText);
        textField.setHorizontalAlignment(SwingConstants.CENTER);
        textField.setBorder(new RoundedBorder(15));
        textField.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e){
                if (textField.getText().equals(placeholderText)){
                    textField.setText("");
                    textField.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent e){
                if (textField.getText().isEmpty()){
                    textField.setForeground(Color.GRAY);
                    textField.setText(placeholderText);
                }
            }
        });
        return  textField;
    }

    private void setNumberOfSubjects(){
        gradesPanel.removeAll();
        try {
            int numSubjects = Integer.parseInt(numSubjectsFiels.getText());
            gradeFields = new JTextField[numSubjects];
            for (int i = 0; i < numSubjects; i++){
                gradesPanel.add(new JLabel("Grade " + (i + 1) + ":"));
                gradeFields[i] = createStyledTextField("Enter grade");
                gradesPanel.add(gradeFields[i]);
            }
            gradesPanel.revalidate();
            gradesPanel.repaint();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid number of subjects.");
        }
    }

    private void calculateGrade(){
        try{
            double total = 0;
            for (JTextField field : gradeFields){
                total += Double.parseDouble(field.getText());
            }
            double average = total / gradeFields.length;
            Color gradeColor;
            String gradeLetter = switch ((int) average / 10){
                case 9, 10 -> {
                    gradeColor = Color.GREEN;
                    yield "A";
                }
                case 8, 7 -> {
                    gradeColor = Color.GREEN;
                    yield "B";
                }
                case 6 -> {
                    gradeColor = Color.GREEN;
                    yield "C";
                }
                case 5 ->{
                    gradeColor = Color.GREEN;
                    yield "D";
                }
                default -> {
                    gradeColor = Color.RED;
                    yield "F";
                }
            };
            resultLable.setText(("Average: " + average + ", Grade: " + gradeLetter));
            resultLable.setForeground(gradeColor);
        }catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter grades.");
        }
    }
    private void clearFields(){
        numSubjectsFiels.setText("");
        gradesPanel.removeAll();
        gradesPanel.revalidate();
        gradesPanel.repaint();
        resultLable.setText("Enter grade and press Calculate");
        resultLable.setForeground(defaultLabelColor);
    }
    private static class RoundedBorder extends AbstractBorder {
        private final int radius;

        RoundedBorder(int radius){
            this.radius = radius;
        }

        public void painyBorder(Component c, Graphics g, int x, int y, int width, int height){
            g.setColor(Color.LIGHT_GRAY);
            g.drawRoundRect(x,y,width - 1,height - 1, radius, radius);
        }

        public Insets getBorderInsets(Component c){
            return new Insets(5,5,5,5);
        }

        public Insets getBorderInsets(Component c, Insets insets){
            return getBorderInsets(c);
        }
    }

    public static void main(String[] args){
        SwingUtilities.invokeLater(()-> new GradeCalculatorGUI().setVisible(true));
    }
}