
/**
 * Starts Mental Math application window with specified width and height/.
 * 
 */

public class RunMentalMath
{
  public static void main (String [] args)
  {
      int width = 650, height = 350;
      MentalMath gui = new MentalMath(width, height);
      gui.setVisible (true);
  }
  
}