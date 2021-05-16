import com.sun.org.apache.xerces.internal.xs.XSTerm;
import engine.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import javafx.util.Pair;
import levelGenerators.jcatlos.Config;
import org.json.simple.JSONObject;

class StudyResults {
    Map<String, LinkedList<Map>> Attempts;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    LinkedList<Future<Boolean>> futureResults = new LinkedList<>();

    public StudyResults(){
        Attempts = new LinkedHashMap<>();
    }

    public void PlayLevel(String key, String level, String generatorName, JButton button){

        String finalLevel = level;
        futureResults.add(executor.submit(
                () -> {
                    LinkedList<MarioResult> out = new LinkedList<>();
                    MarioResult result;
                    int lives = 3;
                    do {
                        MarioGame game = new MarioGame();
                        result = game.playGame(finalLevel, 100, 0, 30);
                        game.window.dispose();
                        out.add(result);
                        if(!generatorName.equals("test")){
                            AddAttempt(key, generatorName, result);
                        }
                        lives --;
                    } while (result.getGameStatus().toString().equals("LOSE") && lives > 0);
                    return true;
                }
        ));
    }

    public void AddAttempt(String key, String generator, MarioResult result){
        Map attempt = new LinkedHashMap<String, String>();

        attempt.put("generator", generator);
        attempt.put("status", result.getGameStatus().toString());
        attempt.put("percentage", result.getCompletionPercentage());
        attempt.put("lives", result.getCurrentLives());
        attempt.put("coins", result.getCurrentCoins());
        attempt.put("remaining_time", result.getRemainingTime());
        attempt.put("state", result.getMarioMode());
        attempt.put("mushrooms", result.getNumCollectedMushrooms());
        attempt.put("fire_flowers", result.getNumCollectedFireflower());
        attempt.put("total_kills", result.getKillsTotal());
        attempt.put("stomp_kills", result.getKillsByStomp());
        attempt.put("fireball_kills", result.getKillsByFire());
        attempt.put("shell_kills", result.getKillsByShell());
        attempt.put("fall_kills", result.getKillsByFall());
        attempt.put("bricks", result.getNumDestroyedBricks());
        attempt.put("jumps", result.getNumJumps());
        attempt.put("max_x_ jump", result.getMaxXJump());
        attempt.put("max_air_time", result.getMaxJumpAirTime());

        if(!Attempts.containsKey(key)){
            Attempts.put(key, new LinkedList<Map>());
        }
        
        Attempts.get(key).add(attempt);
    }

    public Thread ExportTo(String filename){

        return new Thread( () -> {
            while (!futureResults.isEmpty()){
                if(futureResults.getFirst().isDone()) futureResults.removeFirst();
            }

            JSONObject json = new JSONObject();
            json.putAll(Attempts);

            try {
                FileWriter myWriter = new FileWriter(filename);
                myWriter.write(json.toJSONString());
                myWriter.close();
            } catch (IOException e) {
                System.out.println("An error occurred");
                e.printStackTrace();
            }
            System.out.println("Exporting finished");
        });

    }
}


class Study implements ActionListener {
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JButton playButton = new JButton("Start test 1");
    JButton testButton = new JButton("Testing Level");
    JButton repeatButton = new JButton("Opakovať posledný test");
    JLabel textArea = new JLabel("Tlačidlo slúži na začatie ďalšieho testu. nestláčajte ho kým k tomu nebudete vyzvaní.");

    String level;
    ArrayList<Pair<String, String>> levelPairs; // Generator name, Level
    Pair<String, String> current_pair;
    MarioGame game;

    String testLevel;

    StudyResults Results = new StudyResults();

    int step = -1;

    private void closeHandler() throws InterruptedException {
        //System.out.println("Closing");
        Thread exportThread = Results.ExportTo("study_output.txt");
        exportThread.start();
        frame.dispose();
        exportThread.join();
        //System.out.println("Calling exit");
        System.exit(0);
    }

    public Study(){
        testLevel = new levelGenerators.randomMarioLevel.LevelGenerator().getTestLevel();

        //generators = new ArrayList<>();
        //generators.add(new levelGenerators.jcatlos.LevelGenerator());
        //generators.add(new levelGenerators.benWeber.LevelGenerator());

        levelGenerators.benWeber.LevelGenerator bw = new levelGenerators.benWeber.LevelGenerator();
        levelGenerators.jcatlos.LevelGenerator ks = new levelGenerators.jcatlos.LevelGenerator();

        levelPairs = new ArrayList<>();
        String kish_suffix = "(koopa)";
        for(int i=0; i<4; i++){
            levelPairs.add(
                    new Pair<String, String>(
                        bw.getGeneratorName(),
                        bw.getGeneratedLevel(new MarioLevelModel(300, 32), new MarioTimer(5 * 60 * 60 * 1000))
                    )
            );
            levelPairs.add(
                    new Pair<String, String>(
                        ks.getGeneratorName() + kish_suffix,
                        ks.getGeneratedLevel(new MarioLevelModel(300, 16), new MarioTimer(5 * 60 * 60 * 1000))
                    )
            );
            if(i >= 1){
                Config new_conf = ks.getLevelConfig();
                kish_suffix = "(bullet)";
                new_conf.setChallengeTag("bullet");
                ks.setLevelConfig(new_conf);
            }
        }
        Collections.shuffle(levelPairs);

        testButton.setBorder(BorderFactory.createEmptyBorder(30,50,30,50));
        testButton.addActionListener(this);
        playButton.setBorder(BorderFactory.createEmptyBorder(30,50,30,50));
        playButton.addActionListener(this);
        repeatButton.setBorder(BorderFactory.createEmptyBorder(30,50,30,50));
        repeatButton.addActionListener(this);
        repeatButton.setEnabled(false);

        panel.setBorder(BorderFactory.createEmptyBorder(200,200,200,200));
        panel.setLayout(new GridLayout(4,1));
        panel.add(textArea);
        panel.add(playButton);
        panel.add(testButton);
        panel.add(repeatButton);
        frame.add(panel, BorderLayout.CENTER);
        frame.setTitle("Super Mario Playtesting Experiment");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                try {
                    closeHandler();
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        JButton sourceButton = (JButton)e.getSource();
        if(sourceButton.getText().equals("Testing Level")){
            Results.PlayLevel("test", testLevel, "test", playButton);
            return;
        }

        if(sourceButton.getText().contains("Start")){
            step ++;
        }

        if(step == levelPairs.size()){
            try {
                closeHandler();
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        }


        current_pair = levelPairs.get(step);
        //current_generator = generators.get(step);
        //level = current_pair.getValue()
                //current_generator.getGeneratedLevel(new MarioLevelModel(150, 16), new MarioTimer(5 * 60 * 60 * 1000));
        playButton.setText("Start test " + (step+2));
        Results.PlayLevel("Experiment-"+(step+2), current_pair.getValue(), current_pair.getKey(), playButton);

        if(step == levelPairs.size() -1){
            textArea.setText("Po dohraní aktuálnej sady stlačte tlačidlo. Prosím nezatvárajte žiadne okno. Ďakujem za účasť na experimente.");
            playButton.setText("Ukončiť");
        }

        if(step >= 0){
            repeatButton.setEnabled(true);
        }
        /*new Thread(new Callable<LinkedList<MarioResult>>() {
            @Override
            public LinkedList<MarioResult> call() {
                int lives = 3;
                LinkedList<MarioResult> out = new LinkedList<>();
                MarioResult result;
                do {

                    result = game.playGame(level, 200, 0, 30);
                    Results.AddAttempt(current_generator.getGeneratorName(), result);
                    lives --;
                } while (result.getGameStatus().equals("LOSE") && lives > 0);
                return
            }
        }).start();*/

    }
}


public class StudyKishoutenketsu {

    public static void printResults(MarioResult result) {
        System.out.println("****************************************************************");
        System.out.println("Game Status: " + result.getGameStatus().toString() +
                " Percentage Completion: " + result.getCompletionPercentage());
        System.out.println("Lives: " + result.getCurrentLives() + " Coins: " + result.getCurrentCoins() +
                " Remaining Time: " + (int) Math.ceil(result.getRemainingTime() / 1000f));
        System.out.println("Mario State: " + result.getMarioMode() +
                " (Mushrooms: " + result.getNumCollectedMushrooms() + " Fire Flowers: " + result.getNumCollectedFireflower() + ")");
        System.out.println("Total Kills: " + result.getKillsTotal() + " (Stomps: " + result.getKillsByStomp() +
                " Fireballs: " + result.getKillsByFire() + " Shells: " + result.getKillsByShell() +
                " Falls: " + result.getKillsByFall() + ")");
        System.out.println("Bricks: " + result.getNumDestroyedBricks() + " Jumps: " + result.getNumJumps() +
                " Max X Jump: " + result.getMaxXJump() + " Max Air Time: " + result.getMaxJumpAirTime());
        System.out.println("****************************************************************");
    }

    public static void main(String[] args) {
        Study study = new Study();
    }
}
