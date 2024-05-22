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
import picocli.CommandLine.ArgGroup;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@TypeHint(value = {
        EEnumLiteral[].class,
        EParameter[].class,
        EStringToStringMapEntryImpl[].class,
        ETypeParameter[].class
})
@Command(name = "Ecore2Emfatic", description = "Generates Emfatic sources from an .ecore file", mixinStandardHelpOptions = true)
public class Ecore2EmfaticCommand implements Runnable {

    static class URIMapping {
        @Option(names = { "-f", "--from" })
        String from;

        @Option(names = { "-t", "--to"})
        String to;
    }

    @ArgGroup(exclusive = false, multiplicity = "0..*")
    private List<URIMapping> uriMappings;

    @Parameters(index = "0")
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

        Map<URI, URI> uriMap = rs.getURIConverter().getURIMap();
        if (uriMappings != null) {
            applyURIMappingsTo(uriMap);
        }

        Resource r = rs.getResource(URI.createFileURI(pathToFile), true);
        System.out.println(new Writer().write(r, null, null));
    }

    protected void applyURIMappingsTo(Map<URI, URI> uriMap) {
        for (URIMapping mapping : uriMappings) {
            if (mapping.from == null) {
                throw new IllegalArgumentException("Missing from");
            }
            if (mapping.to == null) {
                throw new IllegalArgumentException("Missing to");
            }

            try {
                File fTargetPath = new File(mapping.to).getCanonicalFile();
                String sPath = fTargetPath.getAbsolutePath();
                if (fTargetPath.isDirectory()) {
                    // If the target is a directory, ensure both source and target URIs are "prefix" URIs
                    // (i.e. that they end in a slash)
                    if (!mapping.from.endsWith("/")) {
                        mapping.from += "/";
                    }
                    if (!sPath.endsWith("/")) {
                        sPath += "/";
                    }
                }

                URI mappedURI = URI.createURI(mapping.from, true);
                URI fileURI = URI.createFileURI(sPath);
                uriMap.put(mappedURI, fileURI);
            } catch (IOException e) {
                throw new IllegalArgumentException("Could not compute canonical path for " + mapping.to);
            }
        }
    }
}
