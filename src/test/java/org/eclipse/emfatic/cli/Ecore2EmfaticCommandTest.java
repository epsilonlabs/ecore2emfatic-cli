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
import static org.junit.jupiter.api.Assertions.fail;

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
            assertContains("Should see a PackageableElement in the output", baos.toString(), "PackageableElement");
        }
    }

    private void assertContains(String reason, String text, String substring) {
        if (!text.contains(substring)) {
            fail(String.format("%s, but did not contain '%s':\n%s", reason, substring, text));
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
            assertContains("Should see 'extends Trees.Tree' in the output",
                baos.toString(), "extends Trees.Tree");
        }
    }
}
