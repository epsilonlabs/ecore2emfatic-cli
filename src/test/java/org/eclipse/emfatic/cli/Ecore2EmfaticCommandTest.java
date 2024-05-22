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

import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;
import io.micronaut.context.env.Environment;
import static org.hamcrest.MatcherAssert.assertThat; 
import static org.hamcrest.Matchers.containsString;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import org.junit.jupiter.api.Test;

public class Ecore2EmfaticCommandTest {

    @Test
    public void testStandaloneMetamodel() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            String[] args = new String[] { "src/test/resources/OO.ecore" };
            PicocliRunner.run(Ecore2EmfaticCommand.class, ctx, args);
            assertThat("Should see a PackageableElement in the output",
                baos.toString(), containsString("PackageableElement"));
        }
    }

    @Test
    public void testMetamodelWithPlatformImport() throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        try (ApplicationContext ctx = ApplicationContext.run(Environment.CLI, Environment.TEST)) {
            String[] args = new String[] {
                "--from=platform:/resource",
                "--to=src/test/resources/platformImport",
                "src/test/resources/platformImport/example2/ColoredTree.ecore"
            };
            PicocliRunner.run(Ecore2EmfaticCommand.class, ctx, args);
            assertThat("Should see 'extends Trees.Tree' in the output",
                baos.toString(), containsString("extends Trees.Tree"));
        }
    }
}
