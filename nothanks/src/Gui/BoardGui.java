package Gui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class BoardGui {

    public BoardGui(){
        //create the frame
        JFrame f = new JFrame("No Thanks!");
        f.getContentPane().setBackground(Color.black);

        //Create the panel with the chip in the middle of the frame, and take all button
        JPanel chip_center = new chip();// create Jpanel
        chip_center.setBounds(640,265,215,265);
        JLabel num_chips =new JLabel("55"); //text inside chip must be edited to call number of current chips
        num_chips.setFont(new Font("Serif", Font.BOLD, 85));
        num_chips.setForeground(new Color(255, 197, 24));
        chip_center.setLayout(null);//make it possible to rearrange
        num_chips.setBounds(58,58,100,100);
        JButton take =new JButton("Take all"); //button
        take.setBounds(50,200,100,50);
        chip_center.add(num_chips);
        chip_center.add(take);
        chip_center.setBackground(Color.black);


        //Create the card next to the chip in the middle of the frame
        JPanel Card_In_Play =new JPanel();
        Card_In_Play.setBounds(425,265,215,265);
        Card_In_Play.setBackground(new Color(255, 197, 24));
        Card_In_Play.setBorder(new LineBorder(new Color(255, 230, 142),10));
        JLabel card_Play_text =new JLabel("35", SwingConstants.CENTER);//text inside current card must be edited to call number of current card
        Card_In_Play.setLayout(null);
        card_Play_text.setBounds(0,0,215,265);
        card_Play_text.setFont(new Font("Serif",Font.BOLD,115));
        card_Play_text.setForeground(new Color(188, 59, 56));
        Card_In_Play.add(card_Play_text);


        //Create the card that contains the info of what card the player has.
        JPanel player_cards =new JPanel();
        player_cards.setBounds(425,580,200,250);
        player_cards.setBackground(new Color(255, 197, 24));
        player_cards.setBorder(new LineBorder(new Color(255, 230, 142),10));
        JLabel player_cards_text= new JLabel("You have the cards _____");// add info from call
        player_cards.add(player_cards_text);

        //Chips that the player has and button to toss
        JPanel player_chips=new chip();
        player_chips.setBounds(640,580,215,265);
        JLabel num_chips_p =new JLabel("5"); //text inside chip must be edited to call number of current chips
        num_chips_p.setFont(new Font("Serif", Font.BOLD, 85));
        num_chips_p.setForeground(new Color(255, 197, 24));
        player_chips.setLayout(null);//make it possible to rearrange
        num_chips_p.setBounds(58,58,100,100);
        JButton toss =new JButton("Toss Chip");
        toss.setBounds(50,0,100,50);
        player_chips.add(num_chips_p);
        player_chips.add(toss);
        player_chips.setBackground(Color.black);

        JPanel Score_board =new JPanel();
        Score_board.setBounds(10,10,265,215);
        JLabel Score =new JLabel("Scores");
        Score_board.setLayout(null);
        Score.setBounds(50,50,265,215);
        Score_board.setBorder(new TitledBorder(new LineBorder(Color.black, 5),
                "Score Board"));
        Score_board.setBackground(new Color(255, 249, 226));
        Score_board.add(Score);


        f.add(Score_board);
        f.add(player_chips);
        f.add(player_cards);
        f.add(Card_In_Play);
        f.add(chip_center);
        f.setSize(1280,800);
        f.setLayout(null);
        f.setVisible(true);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


    }
    class chip extends JPanel{
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //super.paintComponent(j);

            g.setColor(new Color(188, 59, 56));
            g.fillOval(50, 50, 100, 100);
            //j.drawOval(52,52,96,96);
            //j.setColor(Color.black);

        }

    }
}
