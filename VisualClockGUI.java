import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalTime;

public class VisualClockGUI extends JFrame {

    private JLabel timeLabel;
    private JTextField alarmHourField;
    private JTextField alarmMinuteField;
    private JLabel alarmStatus;

    private Timer timer;
    private boolean alarmEnabled = false;
    private int alarmHour = -1;
    private int alarmMinute = -1;

    public VisualClockGUI() {

        setTitle("Visual Clock");
        setSize(420, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        timeLabel = new JLabel("00:00:00", SwingConstants.CENTER);
        timeLabel.setFont(new Font("Monospaced", Font.BOLD, 48));
        timeLabel.setForeground(Color.GREEN);
        timeLabel.setBackground(Color.BLACK);
        timeLabel.setOpaque(true);

        add(timeLabel, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new GridLayout(3, 1));

        JPanel alarmPanel = new JPanel();
        alarmPanel.add(new JLabel("Alarm (HH MM): "));
        alarmHourField = new JTextField(2);
        alarmMinuteField = new JTextField(2);
        alarmPanel.add(alarmHourField);
        alarmPanel.add(alarmMinuteField);

        JButton setAlarmBtn = new JButton("Set Alarm");
        setAlarmBtn.addActionListener(this::setAlarm);
        alarmPanel.add(setAlarmBtn);

        JButton disableAlarmBtn = new JButton("Disable");
        disableAlarmBtn.addActionListener(e -> {
            alarmEnabled = false;
            alarmStatus.setText("Alarm: OFF");
        });
        alarmPanel.add(disableAlarmBtn);

        bottomPanel.add(alarmPanel);

        alarmStatus = new JLabel("Alarm: OFF", SwingConstants.CENTER);
        bottomPanel.add(alarmStatus);

        JPanel controlPanel = new JPanel();
        JButton startBtn = new JButton("Start");
        JButton stopBtn = new JButton("Stop");

        startBtn.addActionListener(e -> timer.start());
        stopBtn.addActionListener(e -> timer.stop());

        controlPanel.add(startBtn);
        controlPanel.add(stopBtn);

        bottomPanel.add(controlPanel);

        add(bottomPanel, BorderLayout.SOUTH);

        timer = new Timer(1000, e -> updateClock());
    }

    private void updateClock() {
        LocalTime now = LocalTime.now();
        String time = String.format("%02d:%02d:%02d",
                now.getHour(), now.getMinute(), now.getSecond());

        timeLabel.setText(time);

        if (alarmEnabled &&
                now.getHour() == alarmHour &&
                now.getMinute() == alarmMinute &&
                now.getSecond() == 0) {

            JOptionPane.showMessageDialog(this,
                    "ALARM! Time is " + String.format("%02d:%02d", alarmHour, alarmMinute),
                    "ALARM",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    private void setAlarm(ActionEvent e) {
        try {
            int h = Integer.parseInt(alarmHourField.getText());
            int m = Integer.parseInt(alarmMinuteField.getText());

            if (h < 0 || h > 23 || m < 0 || m > 59) {
                throw new NumberFormatException();
            }

            alarmHour = h;
            alarmMinute = m;
            alarmEnabled = true;
            alarmStatus.setText("Alarm: ON (" +
                    String.format("%02d:%02d", h, m) + ")");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Invalid time. Use HH (0-23) and MM (0-59).");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() ->
                new VisualClockGUI().setVisible(true));
    }
}
