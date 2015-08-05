
/**
 * Use to end program and close JFrame if user clicks the close-window button
 */

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class WindowDestroyer extends WindowAdapter
{
    public void windowClosing (WindowEvent e)
    {
        System.exit (0);
    }
}