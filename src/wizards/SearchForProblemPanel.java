package wizards;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

/**
 * Created by aleksamarkoni on 8.4.15..
 */
public class SearchForProblemPanel extends JPanel {
    private SearchForProblemWizardStep step;

    public SearchForProblemPanel() {
        initComponents();
    }

    public SearchForProblemPanel(SearchForProblemWizardStep step) {
        this.step = step;
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());
        JPanel northPanel = new JPanel();
        northPanel.add(new JLabel("Enter UVA problem ID: "));
        final JTextField uvaProblemID = new JTextField();
        uvaProblemID.setPreferredSize(new Dimension(100, 30));
        northPanel.add(uvaProblemID);
        uvaProblemID.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
                System.out.println(uvaProblemID.getText());
            }
        });
        add(northPanel, BorderLayout.NORTH);
        add(new JLabel("Problem name: "), BorderLayout.CENTER);
        JButton refreshButton = new JButton("Check for new problems on the UVA site");
        add(refreshButton, BorderLayout.SOUTH);
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateAllTheData();
            }
        });
    }

    private void updateAllTheData() {
        test2();
    }

    private void test1() {
        ApplicationManager.getApplication().executeOnPooledThread(new Runnable() {
            @Override
            public void run() {
                ApplicationManager.getApplication().runReadAction(new Runnable() {
                    @Override
                    public void run() {
            //            doTheTest();
                    }
                });
            }
        });
    }

    private void test2() {
        ProgressManager.getInstance().run(new Task.Modal(step.getMyProjectOrNull(), "Title", false) {
            public void run(@NotNull ProgressIndicator progressIndicator) {
                // start your process
                doTheTest(progressIndicator);

            }
        });
    }

    public void doTheThing() {
        String url = "http://uhunt.felix-halim.net/api/cpbook/3";
        InputStream is = null;
        try {
            is = new URL(url).openStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }
            System.out.println(sb.toString());
        } catch (MalformedURLException me) {
            me.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void doTheTest(@NotNull ProgressIndicator progressIndicator) {

        // Set the progress bar percentage and text
        progressIndicator.setFraction(0.00);
        progressIndicator.setText("100% to finish");
        System.out.println("100% to finish");


        try {
            for (int i = 0; i < 10; i++) {
                Thread.sleep(5000);
                progressIndicator.setFraction((i + 1) * 0.10);
                progressIndicator.setText((1.00 - (i + 1) * 0.10) + "00% to finish");
                System.out.println((1.00 - (i+1) * 0.10) + "00% to finish");
            }
            progressIndicator.setText("finished");
            System.out.println("finished");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
