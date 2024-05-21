/*******************************************************************************
 * Copyright (c) 2024 The University of York.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.emfatic.cli;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.ETypeParameter;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EStringToStringMapEntryImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.emfatic.core.generator.emfatic.Writer;

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.core.annotation.TypeHint;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@TypeHint(value = {
    EEnumLiteral[].class,
    EParameter[].class,
    EStringToStringMapEntryImpl[].class,
    ETypeParameter[].class
})
@Command(
    name = "Ecore2Emfatic",
    description = "Generates Emfatic sources from an .ecore file",
    mixinStandardHelpOptions = true
)
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
