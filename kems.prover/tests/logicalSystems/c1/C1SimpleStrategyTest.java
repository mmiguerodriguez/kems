package logicalSystems.c1;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import logic.problem.Problem;
import logic.signedFormulas.SignedFormulaCreator;
import logic.signedFormulas.SignedFormulaList;
import main.newstrategy.Prover;
import main.newstrategy.c1.simple.C1SimpleStrategy;
import main.newstrategy.cpl.configurable.comparator.InsertionOrderSignedFormulaComparator;
import main.newstrategy.mbc.simple.MBCSimpleStrategy;
import main.tableau.Method;
import main.tableau.Proof;

import org.junit.Test;

import proverinterface.RuleStructureFactory;

public class C1SimpleStrategyTest {

	@Test
	public final void testClose_for_MBC() {
		Prover prover = new Prover();

		SignedFormulaCreator sfc = new SignedFormulaCreator("satlfiinconsdef");
		SignedFormulaList sfl = new SignedFormulaList();

		sfl.add(sfc.parseString("T A1->B1"));
		sfl.add(sfc.parseString("T A2->B2"));
		Problem p = new Problem("satlfiinconsdef");
		p.setSignedFormulaList(sfl);

		Method method = new Method(RuleStructureFactory.createRulesStructure(RuleStructureFactory.MBC));
		MBCSimpleStrategy str = new MBCSimpleStrategy(method);
		str.setComparator(new InsertionOrderSignedFormulaComparator());
		prover.setMethod(method);
		prover.setStrategy(str);

		Proof result = prover.prove(p);

		// System.out.println(result);
		// System.out.println(result.getProofTree().getNumberOfNodes());
		assertTrue(result.getProofTree().getNumberOfNodes() == 10);
		assertFalse(result.isClosed());
	}

	@Test
	public final void testClose_for_C1() {
		Prover prover = new Prover();

		SignedFormulaCreator sfc = new SignedFormulaCreator("satlfiinconsdef");
		SignedFormulaList sfl = new SignedFormulaList();

		sfl.add(sfc.parseString("T A1->B1"));
		sfl.add(sfc.parseString("T A2->B2"));

		Problem p = new Problem("satlfiinconsdef");
		p.setSignedFormulaList(sfl);

		Method method = new Method(RuleStructureFactory.createRulesStructure(RuleStructureFactory.C1));
		C1SimpleStrategy str = new C1SimpleStrategy(method);
		str.setComparator(new InsertionOrderSignedFormulaComparator());
		prover.setMethod(method);
		prover.setStrategy(str);

		Proof result = prover.prove(p);

		// System.out.println(result);
		// System.out.println(result.getProofTree().getNumberOfNodes());
		assertTrue(result.getProofTree().getNumberOfNodes() == 10);
		assertFalse(result.isClosed());
	}

	@Test
	public final void testClose_for_C1_v2() {
		Prover prover = new Prover();

		SignedFormulaCreator sfc = new SignedFormulaCreator("satlfiinconsdef");
		SignedFormulaList sfl = new SignedFormulaList();

		sfl.add(sfc.parseString("T !(A1->B1)"));
		// sfl.add(sfc.parseString("T A2->B2"));

		Problem p = new Problem("satlfiinconsdef");
		p.setSignedFormulaList(sfl);

		Method method = new Method(RuleStructureFactory.createRulesStructure(RuleStructureFactory.C1));
		C1SimpleStrategy str = new C1SimpleStrategy(method);
		str.setComparator(new InsertionOrderSignedFormulaComparator());
		prover.setMethod(method);
		prover.setStrategy(str);

		Proof result = prover.prove(p);

		// System.out.println(result);
		// System.out.println(result.getProofTree().getNumberOfNodes());
		// assertTrue(result.getProofTree().getNumberOfNodes() == 10);
		assertFalse(result.isClosed());
	}

	@Test
	public final void testClose_for_C1_2() {
		Prover prover = new Prover();

		SignedFormulaCreator sfc = new SignedFormulaCreator("satlfiinconsdef");
		SignedFormulaList sfl = new SignedFormulaList();

		sfl.add(sfc.parseString("F !(P&(!P&!(P&!P)))"));

		Problem p = new Problem("satlfiinconsdef");
		p.setSignedFormulaList(sfl);

		Method method = new Method(RuleStructureFactory.createRulesStructure(RuleStructureFactory.C1));
		C1SimpleStrategy str = new C1SimpleStrategy(method);
		str.setComparator(new InsertionOrderSignedFormulaComparator());
		prover.setMethod(method);
		prover.setStrategy(str);

		Proof result = prover.prove(p);

		// System.out.println(result);
		// System.out.println(result.getProofTree().getNumberOfNodes());
		assertTrue(result.getProofTree().getNumberOfNodes() == 9);
		assertTrue(result.isClosed());
	}

	@Test
	public final void testSeveralFilesAula() {
		// CCM example
		String base = "created/lfi/lfi_samples/C1/";
		testOneFile(base + "valid/C1_sample_Aula.prove", true, -1);
		// my (wrong) example
		base = "generated/lfiProblems/";
		testOneFile(base + "family5/family5_19.prove", true, -1);
	}

	@Test
	public final void testSeveralFiles() {
		// CCM example
		String base = "created/lfi/lfi_samples/C1/";
		testOneFile(base + "valid/C1_sample_01.prove", true, 9);
		// my (wrong) example
		testOneFile(base + "invalid/C1_sample_02.prove", false, 19); // 19 //23
		// other order
		// two #3 instances from families 5 and 6
		testOneFile(base + "valid/C1_sample_03.prove", true, 44);
		testOneFile(base + "valid/C1_sample_04.prove", true, 54);
		// four Krause based examples
		testOneFile(base + "valid/C1_sample_05.prove", true, 16);
		testOneFile(base + "valid/C1_sample_06.prove", true, 14);
		testOneFile(base + "invalid/C1_sample_07.prove", false, 15);
		testOneFile(base + "valid/C1_sample_08.prove", true, 18);
		testOneFile(base + "valid/C1_sample_09.prove", true, 122);
		// testOneFile("valid/C1_sample_10.prove", true, 683);
		// testOneFile("valid/C1_sample_11.prove", true, 525);
		testOneFile(base + "valid/C1_sample_12.prove", true, 169);

		base = "generated/lfiProblems/";
		testOneFile(base + "first/C1/family1_10.prove", true, -1);
		testOneFile(base + "first/C1/family1_20.prove", true, -1);
		// testOneFile(base + "first/C1/family1_30.prove", true, -1);
		testOneFile(base + "second/C1/family2_01.prove", true, -1);
		testOneFile(base + "second/C1/family2_02.prove", true, -1);
		testOneFile(base + "second/C1/family2_03.prove", true, -1);
		testOneFile(base + "third/C1/family3_01.prove", true, -1);
		testOneFile(base + "third/C1/family3_02.prove", true, -1);
		testOneFile(base + "third/C1/family3_03.prove", true, -1);

		testOneFile(base + "fourth/C1/family4_01.prove", true, 14);
		testOneFile(base + "fourth/C1/family4_02.prove", true, 25);
		testOneFile(base + "fourth/C1/family4_03.prove", true, 47);
		testOneFile(base + "fourth/C1/family4_10.prove", true, 145);

		testOneFile(base + "family5/family5_01.prove", true, -1);
		testOneFile(base + "family5/family5_02.prove", true, -1);

		// testOneFile(base + "family5/family5_03.prove", true, -1);
		// testOneFile(base + "family5/family5_04.prove", true, -1);
		// testOneFile(base + "family5/family5_10.prove", true, -1);

		for (int i = 3; i <= 9; i++) {
			testOneFile(base + "family5/family5_0" + i + ".prove", true, -1);
		}
		for (int i = 10; i <= 30; i += 10) {
			testOneFile(base + "family5/family5_" + i + ".prove", true, -1);
		}
		// testOneFile(base + "family5/family5_99.prove", true, -1);

		testOneFile(base + "family6/family6_01.prove", true, 20);
		testOneFile(base + "family6/family6_02.prove", true, 37);
		testOneFile(base + "family6/family6_03.prove", true, 54);

		testOneFile(base + "family7/C1/family7_01.prove", true, 11); // 10 111
		testOneFile(base + "family7/C1/family7_02.prove", true, 40); // 10 111
		testOneFile(base + "family7/C1/family7_03.prove", true, 169); // 10 111

		testOneFile(base + "family8/C1/family8_01.prove", true, 13);
		testOneFile(base + "family8/C1/family8_02.prove", true, 30);
		testOneFile(base + "family8/C1/family8_03.prove", true, 182);
		// testOneFile(base + "family8/C1/family8_10.prove", true, 69);

		testOneFile(base + "family9/C1/family9_01.prove", true, 20);
		testOneFile(base + "family9/C1/family9_02.prove", true, 52);
		testOneFile(base + "family9/C1/family9_05.prove", true, 122);
	}

	public final void testOneFile(String problemFileName, boolean expected, int expectedNumberOfNodes) {
		Prover prover = new Prover();
		System.out.println("Trying " + problemFileName);

		SignedFormulaCreator sfc = new SignedFormulaCreator("satlfiinconsdef");

		Problem p = sfc
				.parseFile("../kems.problems/problems/"
						+ problemFileName);

		Method method = new Method(RuleStructureFactory.createRulesStructure(RuleStructureFactory.C1));
		C1SimpleStrategy str = new C1SimpleStrategy(method);
		str.setComparator(new InsertionOrderSignedFormulaComparator());
		prover.setMethod(method);
		prover.setStrategy(str);

		Proof result = prover.prove(p);

		System.out.println("Problem:" + result.getProblem().getFilename());
		System.out.println("Problem s-formulas:" + result.getProblem().getFormulas());
		System.out.println(result.getProofTree());

		 System.out.println(result);
		if (expected) {
			if (!result.isClosed()) {
				System.out.println("Problem:" + result.getProblem().getFilename());
				System.out.println("Problem s-formulas:" + result.getProblem().getFormulas());
				System.out.println(result.getProofTree());
			}
			assertTrue(result.isClosed());
		} else {
			assertFalse(result.isClosed());
		}
		if (expectedNumberOfNodes != -1) {
			assertTrue(result.getProofTree().getNumberOfNodes() == expectedNumberOfNodes);
		} else {
			System.out.print("Size of proof of " + problemFileName + ": ");
			System.out.println(result.getProofTree().getNumberOfNodes());
		}

	}

}
