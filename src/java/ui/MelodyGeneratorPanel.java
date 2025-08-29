package ui;

import circle.CircleOfFifthsKeyFile;
import melodygenerator.MelodyGenerator;
import scale.KeyFile;
import scale.Note;
import utils.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MelodyGeneratorPanel extends JPanel {
    private JFrame parentWindow;

    private MelodyGenerator melodyGenerator = new MelodyGenerator();



    class ProgressionListPanel extends JPanel {

        public ProgressionListPanel() {
            setLayout(new BorderLayout());
            JPanel controlPanel = new JPanel();

            add(controlPanel, BorderLayout.SOUTH);

            JButton addProgressionBtn = new JButton("Open Progression");
            addProgressionBtn.addActionListener(ev -> {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(parentWindow) == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        String content = FileUtils.loadFile(file);
                        String[] progressions = content.split(",");
                        List<CircleOfFifthsKeyFile> progressionKeys = new ArrayList<>();
                        for (String progression : progressions) {
                            CircleOfFifthsKeyFile key = CircleOfFifthsKeyFile.fromString(progression);
                            progressionKeys.add(key);
                        }
                        setProgressions(progressionKeys);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            controlPanel.add(addProgressionBtn);

        }
    }

    private void setProgressions(List<CircleOfFifthsKeyFile> progressionKeys) {
        removeAll();
        ProgressionMusicScoreComponent scorePanel = new ProgressionMusicScoreComponent();
        List<Note> notes = melodyGenerator.generateMelodyProgression(CircleOfFifthsKeyFile.sharpMinor(KeyFile.C));
        scorePanel.setProgressions(progressionKeys, notes);
        scorePanel.setPreferredSize(new Dimension(progressionKeys.size()* 175, 400));
        JScrollPane progressionScroller = new JScrollPane(scorePanel);
        add(progressionScroller, BorderLayout.CENTER);

    }

    public MelodyGeneratorPanel(JFrame parentWindow) {
        this.parentWindow = parentWindow;
        setLayout(new BorderLayout());
        add(new ProgressionListPanel(), BorderLayout.LINE_START);



    }
}
