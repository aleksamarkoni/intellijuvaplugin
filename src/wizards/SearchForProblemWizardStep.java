package wizards;

import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Created by aleksamarkoni on 8.4.15..
 */
public class SearchForProblemWizardStep extends ModuleWizardStep {

    private Project myProjectOrNull;
    private final UvaModuleBuilder myBuilder;
    private final WizardContext myContext;
    private JComponent myMainPanel ;



    public SearchForProblemWizardStep(@Nullable Project project, UvaModuleBuilder builder, WizardContext context) {
        myProjectOrNull = project;
        myBuilder = builder;
        myContext = context;
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return myMainPanel;
    }

    @Override
    public JComponent getComponent() {

        if (myMainPanel == null)
        {
            myMainPanel = new SearchForProblemPanel(this);
        }
        return myMainPanel;
    }

    @Override
    public void updateDataModel() {
        myContext.setProjectBuilder(myBuilder);
    }

    public Project getMyProjectOrNull() {
        return myProjectOrNull;
    }
}
