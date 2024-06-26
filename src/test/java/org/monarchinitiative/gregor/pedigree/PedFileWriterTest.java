package org.monarchinitiative.gregor.pedigree;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class PedFileWriterTest {

	File tmpFile;

	@BeforeEach
	public void setUp() throws IOException {
		this.tmpFile = File.createTempFile("output", "ped");
	}

	@Test
	public void testWrite() throws IOException {
		ImmutableList.Builder<PedPerson> individuals = new ImmutableList.Builder<PedPerson>();
		individuals.add(new PedPerson("fam", "father", "0", "0", Sex.MALE, Disease.UNKNOWN));
		individuals.add(new PedPerson("fam", "mother", "0", "0", Sex.FEMALE, Disease.UNKNOWN));
		individuals.add(new PedPerson("fam", "son", "father", "mother", Sex.MALE, Disease.UNKNOWN));
		individuals.add(new PedPerson("fam", "daughter", "father", "mother", Sex.FEMALE, Disease.UNKNOWN));
		PedFileContents pedFileContents = new PedFileContents(new ImmutableList.Builder<String>().build(),
			individuals.build());

		PedFileWriter writer = new PedFileWriter(tmpFile);
		writer.write(pedFileContents);

		String fileContents = Files.asCharSource(tmpFile, Charsets.UTF_8).read();
		StringBuilder expectedContents = new StringBuilder();
		expectedContents.append("#PEDIGREE\tNAME\tFATHER\tMOTHER\tSEX\tDISEASE\n");
		expectedContents.append("fam\tfather\t0\t0\t1\t0\n");
		expectedContents.append("fam\tmother\t0\t0\t2\t0\n");
		expectedContents.append("fam\tson\tfather\tmother\t1\t0\n");
		expectedContents.append("fam\tdaughter\tfather\tmother\t2\t0\n");
		Assertions.assertEquals(expectedContents.toString(), fileContents);
	}

}
