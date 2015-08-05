
/**
 * This class randomly generates problems for the appropriate level chosen
 * 
 * Author: Thu M Nguyen
 */

import java.util.Random;

public class Problem
{
    private int num1, num2, answer;
    private String operation;
    private Random rand = new Random(System.currentTimeMillis());

    /** Problem constructor
     * 
     * @param level determines the type of problem generated
     */
    public Problem(int level)
    {
        createProblem (level);
    }
    
    /** Generates operations and calls on setNums to generate operands
     * 
     * @param level determines the type of operation generated
     * level variable values 0-3 corresponds to actual levels 1-4 respectively
     * Level 4 problems are a random mix of level 2 and 3 problems
     */
    private void createProblem (int level)
    {
        if ((level == 0) || (level == 1))
        {  
            operation = " + ";
        }
        else if (level == 2)
        {  operation = " - ";}
        else if (level == 3)
        {
            if (((int)(rand.nextDouble()*2)) % 2 == 0)
            {
                operation = " + ";
                level = 1;
            }
            else
            {
                operation = " - ";
                level = 2;
            }
        }
        setNums(level);
    }
    
    /** Generates operands
     * 
     * @param level determines how large the operands can be
     * 
     */
    private void setNums(int level)
    {
        if (level == 0)
        {
            /** First level generates counting numbers from 1 through 9
             * while ensuring answer <= 10
             */
            num1 = (int)(rand.nextDouble()*9 + 1);
            num2 = (int)(rand.nextDouble()*(10-num1) + 1);
            answer = num1 + num2;
        }
        
        else if (level == 1)
        {
            // generates integers from 0 to 10
            num1 = (int)(rand.nextDouble()*11);
            num2 = (int)(rand.nextDouble()*11);
            answer = num1 + num2;
        }
            
        else if (level == 2)
        {
            // generates integers from 0 to 20
            num1 = (int)(rand.nextDouble()*21);
            num2 = (int)(rand.nextDouble()*21);
            if (num1 < num2)
            {
                int temp = num1;
                num1 = num2;
                num2 = temp;
            }
            answer = num1 - num2;
        }
    } //ends setNum method

    /**
     * @return the entire problem with equal sign as a string for display
     */
    public String getProblem()
    {
        return (num1 + operation + num2 + " = ");
    } //ends getProblem method
    
    /** 
     * 
     * @return the correct answer for the problem
     */
    public int getAnswer()
    {
        return answer;
    } // ends getAnswer method
}
