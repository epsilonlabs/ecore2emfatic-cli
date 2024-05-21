package org.eclipse.emfatic.cli;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.emfatic.core.generator.emfatic.Writer;

import io.micronaut.configuration.picocli.PicocliRunner;

import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "Ecore2Emfatic", description = "...",
        mixinStandardHelpOptions = true)
public class Ecore2EmfaticCommand implements Runnable {

    @Parameters(index="0")
    private String pathToFile;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(Ecore2EmfaticCommand.class, args);
    }

    public void run() {
        // Ensure the Ecore metamodel is available
        EcorePackage.eINSTANCE.getName();

        // Set up a resource set with XMI support
        ResourceSet rs = new ResourceSetImpl();
        rs.getResourceFactoryRegistry().getExtensionToFactoryMap().put("*", new XMIResourceFactoryImpl());

        Resource r = rs.getResource(URI.createFileURI(pathToFile), true);
        System.out.println(new Writer().write(r, null, null));
    }
}
