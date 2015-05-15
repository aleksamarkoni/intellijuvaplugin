package wizards;

/**
 * Created by aleksamarkoni on 14.5.15..
 */

import com.intellij.ide.util.projectWizard.ModuleBuilder;
import com.intellij.ide.util.projectWizard.ModuleWizardStep;
import com.intellij.ide.util.projectWizard.SettingsStep;
import com.intellij.ide.util.projectWizard.WizardContext;
import com.intellij.openapi.module.ModuleType;
import com.intellij.openapi.module.StdModuleTypes;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.projectRoots.JavaSdkType;
import com.intellij.openapi.projectRoots.SdkTypeId;
import com.intellij.openapi.roots.CompilerModuleExtension;
import com.intellij.openapi.roots.ContentEntry;
import com.intellij.openapi.roots.ModifiableRootModel;
import com.intellij.openapi.roots.OrderRootType;
import com.intellij.openapi.roots.libraries.Library;
import com.intellij.openapi.roots.libraries.LibraryTable;
import com.intellij.openapi.roots.ui.configuration.ModulesProvider;
import com.intellij.openapi.util.Pair;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.LocalFileSystem;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class UvaModuleBuilder extends ModuleBuilder {
    private String myCompilerOutputPath;
    private List<Pair<String, String>> mySourcePaths;
    private final List<Pair<String, String>> myModuleLibraries = new ArrayList();
    public static final int JAVA_WEIGHT = 100;
    public static final int BUILD_SYSTEM_WEIGHT = 80;
    public static final int JAVA_MOBILE_WEIGHT = 60;

    public UvaModuleBuilder() {
    }

    public final void setCompilerOutputPath(String compilerOutputPath) {
        this.myCompilerOutputPath = this.acceptParameter(compilerOutputPath);
    }

    public List<Pair<String, String>> getSourcePaths() {
        if(this.mySourcePaths == null) {
            ArrayList paths = new ArrayList();
            String path = this.getContentEntryPath() + File.separator + "src";
            (new File(path)).mkdirs();
            paths.add(Pair.create(path, ""));
            return paths;
        } else {
            return this.mySourcePaths;
        }
    }

    public void setSourcePaths(List<Pair<String, String>> sourcePaths) {
        this.mySourcePaths = sourcePaths != null?new ArrayList(sourcePaths):null;
    }

    public void addSourcePath(Pair<String, String> sourcePathInfo) {
        if(this.mySourcePaths == null) {
            this.mySourcePaths = new ArrayList();
        }

        this.mySourcePaths.add(sourcePathInfo);
    }

    public String getBuilderId() {
        return getClass().getName();
    }

    public ModuleType getModuleType() {
        return StdModuleTypes.JAVA;
    }

    public boolean isSuitableSdkType(SdkTypeId sdkType) {
        return sdkType instanceof JavaSdkType;
    }


    @Override
    public ModuleWizardStep[] createWizardSteps(@NotNull WizardContext wizardContext, @NotNull ModulesProvider modulesProvider) {
        ModuleType moduleType = this.getModuleType();
        //ModuleWizardStep[] proba = moduleType == null?ModuleWizardStep.EMPTY_ARRAY:moduleType.createWizardSteps(wizardContext, this, modulesProvider);
        //ModuleWizardStep[] mojistepovi = new ModuleWizardStep[proba.length + 1];
        ModuleWizardStep[] wizardSteps = new ModuleWizardStep[2];
        wizardSteps[0] = new AccountInfoWizardStep(wizardContext.getProject(), this, wizardContext);
        wizardSteps[1] = new SearchForProblemWizardStep(wizardContext.getProject(), this, wizardContext);
        return wizardSteps;
    }

    @Nullable
    public ModuleWizardStep modifySettingsStep(@NotNull SettingsStep settingsStep) {
        return StdModuleTypes.JAVA.modifySettingsStep(settingsStep, this);
    }

    public void setupRootModel(ModifiableRootModel rootModel) throws ConfigurationException {
        CompilerModuleExtension compilerModuleExtension = (CompilerModuleExtension)rootModel.getModuleExtension(CompilerModuleExtension.class);
        compilerModuleExtension.setExcludeOutput(true);
        if(this.myJdk != null) {
            rootModel.setSdk(this.myJdk);
        } else {
            rootModel.inheritSdk();
        }

        ContentEntry contentEntry = this.doAddContentEntry(rootModel);
        Iterator i$;
        Pair libInfo;
        String moduleLibraryPath;
        if(contentEntry != null) {
            List libraryTable = this.getSourcePaths();
            if(libraryTable != null) {
                i$ = libraryTable.iterator();

                while(i$.hasNext()) {
                    libInfo = (Pair)i$.next();
                    moduleLibraryPath = (String)libInfo.first;
                    (new File(moduleLibraryPath)).mkdirs();
                    VirtualFile sourceLibraryPath = LocalFileSystem.getInstance().refreshAndFindFileByPath(FileUtil.toSystemIndependentName(moduleLibraryPath));
                    if(sourceLibraryPath != null) {
                        contentEntry.addSourceFolder(sourceLibraryPath, false, (String)libInfo.second);
                    }
                }
            }
        }

        if(this.myCompilerOutputPath != null) {
            String libraryTable1;
            try {
                libraryTable1 = FileUtil.resolveShortWindowsName(this.myCompilerOutputPath);
            } catch (IOException var11) {
                libraryTable1 = this.myCompilerOutputPath;
            }

            compilerModuleExtension.setCompilerOutputPath(VfsUtil.pathToUrl(FileUtil.toSystemIndependentName(libraryTable1)));
        } else {
            compilerModuleExtension.inheritCompilerOutputPath(true);
        }

        LibraryTable libraryTable2 = rootModel.getModuleLibraryTable();

        Library.ModifiableModel modifiableModel;
        for(i$ = this.myModuleLibraries.iterator(); i$.hasNext(); modifiableModel.commit()) {
            libInfo = (Pair)i$.next();
            moduleLibraryPath = (String)libInfo.first;
            String sourceLibraryPath1 = (String)libInfo.second;
            Library library = libraryTable2.createLibrary();
            modifiableModel = library.getModifiableModel();
            modifiableModel.addRoot(getUrlByPath(moduleLibraryPath), OrderRootType.CLASSES);
            if(sourceLibraryPath1 != null) {
                modifiableModel.addRoot(getUrlByPath(sourceLibraryPath1), OrderRootType.SOURCES);
            }
        }

    }

    private static String getUrlByPath(String path) {
        return VfsUtil.getUrlForLibraryRoot(new File(path));
    }

    public void addModuleLibrary(String moduleLibraryPath, String sourcePath) {
        this.myModuleLibraries.add(Pair.create(moduleLibraryPath, sourcePath));
    }

    @Nullable
    protected static String getPathForOutputPathStep() {
        return null;
    }

    public int getWeight() {
        return 100;
    }


    @Override
    public String getPresentableName() {
        return "Uva Online Application";
    }
    @Override
    public String getDescription() {
        return "AsposeWizardPanel.myMainPanel.description";
    }

}
