package wizards;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;

/**
 * Created by aleksamarkoni on 30.3.15..
 */
public class AccountInfoWizardStep extends ModuleWizardStep implements Disposable{

    private JLabel resultText;
    private JTextField jTextField;
    private JComponent myMainPanel;
    private Project myProjectOrNull;
    private final UvaModuleBuilder myProjectBuilder;
    private final WizardContext myContext;


    public AccountInfoWizardStep(@Nullable Project project, UvaModuleBuilder myProjectBuilder, WizardContext wizardContext) {
        this.myProjectBuilder = myProjectBuilder;
        this.myProjectOrNull = project;
        this.myContext = wizardContext;

    }

    @Override
    public JComponent getComponent() {
        if (myMainPanel == null) {
            myMainPanel = new JPanel();
            myMainPanel.setBorder(new TitledBorder("Uva solution wizard step"));
            myMainPanel.setPreferredSize(new Dimension(333, 364));

            JLabel enterUvaProbelIDJLabel = new JLabel("Enter UVA problem ID:");
            Font labelFont = enterUvaProbelIDJLabel.getFont();
            enterUvaProbelIDJLabel.setFont(new Font(labelFont.getName(), Font.PLAIN, 14));
            GroupLayout groupLayout = new GroupLayout(myMainPanel);
            myMainPanel.setLayout(groupLayout);
            groupLayout.setHorizontalGroup(
                    groupLayout.createParallelGroup()
                        .addGroup(groupLayout.createSequentialGroup()
                                    .addComponent(enterUvaProbelIDJLabel)
                                    .addGap(0, 0, Short.MAX_VALUE))
            );

            groupLayout.setVerticalGroup(
                    groupLayout.createParallelGroup()
                            .addGroup(groupLayout.createSequentialGroup()
                                    .addComponent(enterUvaProbelIDJLabel)
                                    .addContainerGap(0, Short.MAX_VALUE))
            );



        }
        return myMainPanel;
    }

    @Override
    public void updateDataModel() {
        myContext.setProjectBuilder(myProjectBuilder);
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return myMainPanel;
    }

    @Override
    public void onStepLeaving() {
        System.out.println("I am so happy omg ");
        super.onStepLeaving();
    }

    @Override
    public void dispose() {
    }
}
