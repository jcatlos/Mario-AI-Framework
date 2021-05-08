import engine.core.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import engine.sprites.Mario;
import org.json.simple.JSONObject;

class StudyResults {
    Map<String, LinkedList<Map>> Attempts;
    ExecutorService executor = Executors.newSingleThreadExecutor();
    LinkedList<Future<Boolean>> futureResults = new LinkedList<>();

    public StudyResults(){
        Attempts = new LinkedHashMap<>();
    }

    public void PlayLevel(String level, String generatorName, JButton button){

        String finalLevel = level;
        futureResults.add(executor.submit(
                () -> {
                    LinkedList<MarioResult> out = new LinkedList<>();
                    MarioResult result;
                    int lives = 3;
                    do {
                        MarioGame game = new MarioGame();
                        result = game.playGame(finalLevel, 200, 0, 30);
                        game.window.dispose();
                        out.add(result);
                        AddAttempt(generatorName, result);
                        lives --;
                    } while (result.getGameStatus().toString().equals("LOSE") && lives > 0);
                    return true;
                }
        ));
    }

    public void AddAttempt(String generator, MarioResult result){
        Map attempt = new LinkedHashMap<String, String>();

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

        if(!Attempts.containsKey(generator)){
            Attempts.put(generator, new LinkedList<Map>());
        }
        
        Attempts.get(generator).add(attempt);
    }

    public void ExportTo(String filename){

        new Thread( () -> {
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
        }).start();

    }
}


class Study implements ActionListener {
    JFrame frame = new JFrame();
    JPanel panel = new JPanel();
    JButton playButton = new JButton("Start test 1");
    JLabel textArea = new JLabel("Tlačidlo slúži na začatie ďalšieho testu. nestláčajte ho kým k tomu nebudete vyzvaní.");

    String level;
    ArrayList<MarioLevelGenerator> generators;
    MarioLevelGenerator current_generator;
    MarioGame game;

    StudyResults Results = new StudyResults();

    int step = 0;

    public Study(){

        generators = new ArrayList<>();
        generators.add(new levelGenerators.jcatlos.LevelGenerator());
        generators.add(new levelGenerators.linear.LevelGenerator());
        generators.add(new levelGenerators.randomMarioLevel.LevelGenerator());

        Collections.shuffle(generators);

        playButton.setBorder(BorderFactory.createEmptyBorder(30,50,30,50));
        playButton.addActionListener(this);

        panel.setBorder(BorderFactory.createEmptyBorder(200,200,200,200));
        panel.setLayout(new GridLayout(2,1));
        panel.add(textArea);
        panel.add(playButton);
        frame.add(panel, BorderLayout.CENTER);
        frame.setTitle("Super Mario Playtesting Experiment");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(step == generators.size()){
            Results.ExportTo("study_output.txt");
            frame.dispose();
            return;
        }

        current_generator = generators.get(step);
        step ++;
        level = current_generator.getGeneratedLevel(new MarioLevelModel(150, 16), new MarioTimer(5 * 60 * 60 * 1000));
        playButton.setText("Start test " + (step+1));
        Results.PlayLevel(level, current_generator.getGeneratorName(), playButton);

        if(step == generators.size()){
            textArea.setText("Po dohraní aktuálnej sady stlačte tlačidlo. Prosím nezatvárajte žiadne okno. Ďakujem za účasť na experimente.");
            playButton.setText("Ukončiť");
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

    /*public static void start_study(){

        Runnable play_level = new Runnable() {
            @Override
            public void run() {
                MarioResult result = game.playGame(level, 200, 0, 30);
                game.window.dispose();
            }
        };
    }

    public static void play() {
            //MarioResult result = game.runGame(new agents.robinBaumgarten.Agent(), level, 200, 0, true, 30, 4);

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    MarioResult result = game.playGame(level, 200, 0, 30);
                    game.window.dispose();
                }
            });
        }
        catch (Exception ex){}
    }
     */
    public static void main(String[] args) {



        Study study = new Study();

        //printResults(game.playGame(level, 200, 0, 30));
        //printResults(game.runGame(new agents.robinBaumgarten.Agent(), level, 200, 0, true, 30, 4));
    }
}
