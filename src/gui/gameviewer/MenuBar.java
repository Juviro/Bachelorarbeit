package gui.gameviewer;


import core.board.Parser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * @author Sabid Fejzula Class which basically is a JMenuBar with certain entrys.
 *
 * edited by Hauke Witte
 *
 */

public class MenuBar extends JMenuBar {

    /**
     * constructor.
     *
     * */
    public MenuBar(final GameFrame parent) {
        JMenu dat = new JMenu("Data");


        JMenuItem load = new JMenuItem("Load game");
        dat.add(load);



        this.add(dat);

        final MenuBar self = this;
        final JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("csv", "csv");
        chooser.setFileFilter(filter);
        load.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                int returnVal = chooser.showOpenDialog(self);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = chooser.getSelectedFile();
                    parent.setReplay(Parser.fileToStringArray(file));
                }
            }
        });
    }
}
