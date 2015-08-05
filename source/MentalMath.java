/**
 * This class handles the user interface and calls on the class Problem 
 * to generate problems for Mental Math
 * IMPORTANT NOTE: picturePane layout in constructor must be changed to
 * accommodate more than 10 problems
 * 
 * Author: Thu M Nguyen
 * 
 */
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.sound.midi.Instrument;
import javax.sound.midi.MidiChannel;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Synthesizer;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

public class MentalMath extends JFrame implements ActionListener, KeyListener
{
    public static final int TOTAL_PROBLEM = 10;  /* picturePane must be changed if this is more than 10 */
    private static final int[][] RIGHTMUSIC = {{67,70},{72,70},{76,70},{79,123},{76,35},{79,280}};  /* note and duration pairs for midi */
    private static final int[][] WRONGMUSIC = {{55, 200},{50,400}};
    private static final String[] levelName = {"Level 1: Counting number addition", "Level 2: Addition above 10", 
        "Level 3: Subtraction", "Level 4: Addition and Subtraction"};
    private CardLayout card;
    private JPanel cardPane, contentPane, menuPane;
    private JPanel picturePane, historyPane, problemPane;
    private JLabel[] picture = new JLabel[TOTAL_PROBLEM];
    private JLabel[] history = new JLabel[TOTAL_PROBLEM]; // old problems
    private ImageIcon grass, flower, grasshopper, right, wrong;
    private JLabel problemLabel, titleLabel;
    private JTextField input;
    private Problem problem;
    private int problemNum = 0, correctCount = 0, level = 0;
    
    /** MentalMath constructor
     * @param w width of application window 
     * @param h height of application window
     */
    public MentalMath(int w, int h)
    {
        setTitle ("Mental Math");
        setSize (w, h);
        WindowDestroyer listener = new WindowDestroyer ();
        addWindowListener (listener);
        grass = new ImageIcon(getClass().getClassLoader().getResource("img/grass.gif"));
        flower = new ImageIcon(getClass().getClassLoader().getResource("img/flower.gif"));
        grasshopper = new ImageIcon(getClass().getClassLoader().getResource("img/grasshopper.gif"));
        right = new ImageIcon(getClass().getClassLoader().getResource("img/right.gif"));
        wrong = new ImageIcon(getClass().getClassLoader().getResource("img/wrong.gif"));
        
        // menuPane
        menuPane = new JPanel();
        menuPane.setBackground(new Color(200,250,250));
        menuPane.setLayout(new BoxLayout(menuPane, BoxLayout.Y_AXIS));
        menuPane.setBorder(BorderFactory.createEmptyBorder(20,20,20,20));
        JLabel introLabel =new JLabel("Mental Math: Little Garden", JLabel.CENTER);
        introLabel.setFont(new Font("Arial", Font.BOLD, 25));
        introLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPane.add(introLabel);
        menuPane.add(new JLabel(""));
        JButton[] levelButton = new JButton[4];
        for (int i=0; i<levelButton.length; i++)
        {
            levelButton[i] = new JButton(levelName[i]);
            levelButton[i].setFont(new Font("Arial", Font.BOLD, 25));
            levelButton[i].setBorder(BorderFactory.createRaisedBevelBorder());
            levelButton[i].addActionListener(this);
            levelButton[i].setAlignmentX(Component.CENTER_ALIGNMENT);
            menuPane.add(Box.createRigidArea(new Dimension(0,15)));
            menuPane.add(levelButton[i]);
        }
           
        // components for contentPane
        titleLabel = new JLabel("", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 25));
        problemLabel = new JLabel();
        problemLabel.setFont(new Font("Arial", Font.BOLD, 25));
        input = new JTextField("");
        input.setFont(new Font("Arial", Font.BOLD, 25));
        input.setHorizontalAlignment(JTextField.CENTER);
        input.setColumns(3);
        input.addKeyListener(this);
        JButton enterButton = new JButton ("ENTER"); 
        enterButton.setFont(new Font("Arial", Font.BOLD, 25));
        enterButton.setBorder(BorderFactory.createRaisedBevelBorder());
        enterButton.addActionListener (this);
        
        // set layouts and borders
        card = new CardLayout();        
        cardPane = new JPanel();
        cardPane.setLayout(card);
        contentPane = new JPanel();        
        contentPane.setLayout(new BorderLayout(5,5));
        problemPane = new JPanel();
        problemPane.setLayout(new BoxLayout(problemPane, BoxLayout.LINE_AXIS));
        problemPane.setBorder(BorderFactory.createEmptyBorder(0,170,10,170));
        picturePane = new JPanel(new GridLayout(2,5,5,5));
        picturePane.setBorder(BorderFactory.createEmptyBorder(0,10,0,0));
        historyPane = new JPanel(new GridLayout(TOTAL_PROBLEM+1,1,5,5));
        historyPane.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createEmptyBorder(0,0,0,10), BorderFactory.createEtchedBorder(EtchedBorder.RAISED)));
        historyPane.add(new JLabel("  Old Problems  "));
        contentPane.setBackground(new Color(200,250,250));
        problemPane.setBackground(new Color(200,250,250));
        picturePane.setBackground(new Color(200,250,250));
        historyPane.setBackground(new Color(200,250,250));
        
        // create and add components to picture and history panel
        for (int i = 0; i < TOTAL_PROBLEM; i++)
        {   
            picture[i] = new JLabel(grass);
            picturePane.add(picture[i]);
            history[i] = new JLabel();
            historyPane.add(history[i]);
        }
        
        // add components to problem panel
        problemPane.add(problemLabel);
        problemPane.add(input);
        problemPane.add(Box.createRigidArea(new Dimension(20, 0)));
        problemPane.add(enterButton);

        // add everything into contentPane
        contentPane.add(titleLabel, BorderLayout.PAGE_START);
        contentPane.add(picturePane, BorderLayout.CENTER);
        contentPane.add(historyPane, BorderLayout.LINE_END);
        contentPane.add(problemPane, BorderLayout.PAGE_END);
        
        cardPane.add(menuPane, "menu");
        cardPane.add(contentPane, "content");
        getContentPane().add(cardPane);
    } // ends MentalMath constructor
    
    /** Displays new problem in problem area */
    private void setProblem()
    {
        problem = new Problem(level);
        problemLabel.setText(problem.getProblem());
        input.setText("");
    }// ends setProblem method
    
    /** Displays result and then restart to option menu when dialog box is closed*/
    private void restart()
    {
        JFrame frame = new JFrame();
        JOptionPane.showMessageDialog(frame, "You got " + correctCount + " out of " + TOTAL_PROBLEM + " problems right!", 
            (correctCount * 10) + "%", JOptionPane.PLAIN_MESSAGE);
        card.show(cardPane, "menu");     
        // reset everything
        problemNum = 0;
        correctCount = 0;
        for (int i = 0; i < TOTAL_PROBLEM; i++)
        {
            picture[i].setIcon(grass);
            history[i].setIcon(null);
            history[i].setText(null);
        }
    }// ends restart method
    
    /** Plays feedback sound based on correctness of submitted answer 
     * 
     * @param t true if answer was correct else false 
     */
    private void playSound(boolean correct)
    {
        try
        {
            Synthesizer synth = MidiSystem.getSynthesizer();
            synth.open();
       
            final MidiChannel[] mc = synth.getChannels();
            Instrument[] instr = synth.getDefaultSoundbank().getInstruments(); 
            int[][] music;
            if (correct)
            {
                synth.loadInstrument(instr[56]);  //trumpet
                mc[4].programChange(56); 
                music = RIGHTMUSIC;
            }
            else //wrong
            {
                synth.loadInstrument(instr[58]);  //tuba
                mc[4].programChange(58);
                music = WRONGMUSIC;
            }
            for (int i = 0; i < music.length; i++)
            {
                mc[4].noteOn(music[i][0], 200);
                try { Thread.sleep(music[i][1]);}catch (InterruptedException e) {}
                mc[4].noteOff(music[i][0],200);
            }
        }
        catch (MidiUnavailableException e) {}  // sounds will not play, no need to do anything
    }// ends playSound method
    
    /** Updates display accordingly after each submission */
    private void updateDisplay()
    {
        String userAnswer = input.getText().trim();
        boolean correct = true;
        if (userAnswer.length() > 3)
        {    userAnswer = userAnswer.substring(0,3) + "..."; }
        
        //checking answer
        try
        {
            // correct answer
            if (Integer.parseInt(userAnswer) == problem.getAnswer())
            {
                picture[problemNum].setIcon(flower);
                history[problemNum].setIcon(right);
                correct = true;
                correctCount++;
            }
            // wrong answer
            else
            {   
                history[problemNum].setIcon(wrong); 
                picture[problemNum].setIcon(grasshopper);
                correct = false;
            }
        }
        // invalid answer
        catch (NumberFormatException ex)
        {   
            history[problemNum].setIcon(wrong);
            picture[problemNum].setIcon(grasshopper);
            correct = false;
        }
        
        playSound(correct);
        history[problemNum].setText(problem.getProblem() + userAnswer);
        problemNum++;
        
        // no more problems, restart
        if (problemNum == TOTAL_PROBLEM)
        {   restart(); }
        else if (problemNum < TOTAL_PROBLEM)
        {   setProblem();}
    } // ends updateDisplay method

    public void keyTyped (KeyEvent e) {}
    public void keyReleased (KeyEvent e) {}
    
    /** Deals with user pressing ENTER key as submission of answer */
    public void keyPressed (KeyEvent e)
    {
        int key = e.getKeyCode();        
        if (key == KeyEvent.VK_ENTER) 
        {  updateDisplay(); }
    }//ends keyPressed method
    
    /** Deals with all button clicks
     * 
     * @param e ENTER for submission of answer or 
     * otherwise one of the level choices was made from menu
     */
    public void actionPerformed (ActionEvent e)
    {
        String actionCommand = e.getActionCommand();
        // if "ENTER" button was clicked
        if (actionCommand.equals ("ENTER"))
        {   updateDisplay();}  
        
        // level choice was made
        for (int i=0; i<levelName.length; i++)
        {
            if (actionCommand.equals (levelName[i]))
            {
                level = i;
                titleLabel.setText("Mental Math: Little Garden - Level " + (level+1));
                setProblem();
                card.show(cardPane, "content");
            }
        }
        input.requestFocus();
    } //ends actionPerformed method
} //ends MentalMath class